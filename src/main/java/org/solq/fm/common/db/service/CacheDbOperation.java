package org.solq.fm.common.db.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;

import org.solq.fm.common.db.anno.DataSource;
import org.solq.fm.common.db.config.DBConfig;
import org.solq.fm.common.db.model.HCriteria;
import org.solq.fm.common.db.model.HQuery;
import org.solq.fm.common.db.model.ICacheDbOperation;
import org.solq.fm.common.db.model.IDbOperation;
import org.solq.fm.common.db.model.IEntity;
import org.solq.fm.common.util.ArrayUtils;
import org.solq.fm.common.util.MapUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/***
 * cache 数据库操作实现
 * 
 * @author solq
 */
@Component(DBConfig.OPERATION_CACHEDB_OPERATION)
@SuppressWarnings({ "unchecked" })
public class CacheDbOperation implements ICacheDbOperation, IDbOperation, BeanPostProcessor {

    private boolean golbalOpenCahce = true;

    private ConcurrentHashMap<Class<?>, ConcurrentHashMap<Object, Object>> cacheMap = new ConcurrentHashMap<>();
    private Map<Class<? extends IDbOperation>, IDbOperation> dbProxyMap = new HashMap<>();

    @Override
    public <T extends IEntity> T find(Class<T> type, Serializable key) {
	if (!isOpenCache(type)) {
	    IDbOperation proxy = getProxy(type);
	    return proxy.find(type, key);
	}

	ConcurrentHashMap<Object, Object> cache = getCacheData(type);
	T ret = (T) cache.get(key);
	if (ret == null) {
	    IDbOperation proxy = getProxy(type);
	    ret = proxy.find(type, key);
	    if (ret != null) {
		ret = (T) MapUtils.putIfAbsent(cache, key, ret);
	    }
	}
	return ret;
    }

    @Override
    public void delete(Class<? extends IEntity> type, Serializable... keys) {
	if (isOpenCache(type)) {
	    ConcurrentHashMap<Object, Object> cache = getCacheData(type);
	    for (Serializable id : keys) {
		cache.remove(id);
	    }
	}

	// TODO lock
	IDbOperation proxy = getProxy(type);
	proxy.delete(type, keys);
    }

    @Override
    public void delete(IEntity... objects) {
	if (ArrayUtils.isEmpty(objects)) {
	    return;
	}
	// TODO lock
	Class<? extends IEntity> type = objects[0].getClass();
	if (isOpenCache(type)) {
	    ConcurrentHashMap<Object, Object> cache = getCacheData(type);
	    for (IEntity obj : objects) {
		cache.remove(obj);
	    }
	}

	IDbOperation proxy = getProxy(type);
	proxy.delete(objects);
    }

    @Override
    public void saveOrUpdate(IEntity... objects) {
	// TODO lock
	Class<? extends IEntity> type = objects[0].getClass();
	if (isOpenCache(type)) {
	    ConcurrentHashMap<Object, Object> cache = getCacheData(type);
	    for (IEntity obj : objects) {
		cache.put(obj.getId(), obj);
	    }
	}

	IDbOperation proxy = getProxy(type);
	proxy.saveOrUpdate(objects);
    }

    @Override
    public <T extends IEntity> List<T> query(Class<T> type, HQuery hQuery) {
	IDbOperation proxy = getProxy(type);
	return proxy.query(type, hQuery);
    }

    @Override
    public <T extends IEntity> List<T> query(Class<T> type, HCriteria hCriteria) {
	IDbOperation proxy = getProxy(type);
	return proxy.query(type, hCriteria);
    }

    @Override
    public <T extends IEntity> void forEach(Class<T> type, HQuery hQuery, Consumer<T> action) {
	IDbOperation proxy = getProxy(type);
	proxy.forEach(type, hQuery, action);
    }

    @Override
    public <T extends IEntity> void forEach(Class<T> type, HCriteria hCriteria, Consumer<T> action) {
	IDbOperation proxy = getProxy(type);
	proxy.forEach(type, hCriteria, action);
    }

    @Override
    public int count(Class<? extends IEntity> type, HCriteria hCriteria) {
	IDbOperation proxy = getProxy(type);
	return proxy.count(type, hCriteria);
    }

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    void postConstruct() {
	Map<String, IDbOperation> beans = applicationContext.getBeansOfType(IDbOperation.class);
	for (Entry<String, IDbOperation> entry : beans.entrySet()) {
	    IDbOperation obj = entry.getValue();
	    if (obj instanceof ICacheDbOperation) {
		continue;
	    }
	    dbProxyMap.put(obj.getClass(), entry.getValue());
	}
    }

    private boolean isOpenCache(Class<?> type) {
	DataSource dataSource = type.getAnnotation(DataSource.class);
	return golbalOpenCahce && dataSource != null && dataSource.openCache();
    }

    private IDbOperation getProxy(Class<?> type) {
	DataSource dataSource = type.getAnnotation(DataSource.class);
	Class<? extends IDbOperation> proxy = dataSource != null ? dataSource.proxy() : HibernateDbOperation.class;
	IDbOperation ret = dbProxyMap.get(proxy);
	return ret;
    }

    private ConcurrentHashMap<Object, Object> getCacheData(Class<?> type) {
	ConcurrentHashMap<Object, Object> ret = cacheMap.get(type);
	if (ret == null) {
	    ret = new ConcurrentHashMap<>();
	    ret = MapUtils.putIfAbsent(cacheMap, type, ret);
	}
	return ret;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
	return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
	return bean;
    }
}
