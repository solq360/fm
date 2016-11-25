package org.solq.fm.common.db.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.solq.fm.common.db.anno.DataSource;
import org.solq.fm.common.db.coder.AliasToBeanResultTransformer;
import org.solq.fm.common.db.config.DBConfig;
import org.solq.fm.common.db.model.HCriteria;
import org.solq.fm.common.db.model.HQuery;
import org.solq.fm.common.db.model.IDbOperation;
import org.solq.fm.common.db.model.IEntity;
import org.solq.fm.common.util.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/***
 * hibernate 数据库操作实现,支持多个数据源
 * 
 * @author solq
 */
@Component(DBConfig.OPERATION_HIBERNATE_DBOPERATION)
@SuppressWarnings({ "deprecation", "unchecked", "rawtypes" })
public class HibernateDbOperation implements IDbOperation {

    @Qualifier(DBConfig.DATA_SOURCE_SQL_MANAGER)
    @Autowired
    private MultDbSourceManger<SessionFactory> multDbSourceManger;

    @Override
    public <T extends IEntity> T find(Class<T> type, Serializable key) {
	return (T) doAction(type, s -> {
	    return s.get(type, key);
	});
    }

    @Override
    public void delete(Class<? extends IEntity> type, Serializable... keys) {

	doAction(type, s -> {
	    SessionFactory sessionFactory = getSessionFacory(type);
	    final ClassMetadata classMetadata = sessionFactory.getClassMetadata(type);
	    final String name = type.getSimpleName();
	    final String primary = classMetadata.getIdentifierPropertyName();
	    for (Object o : keys) {
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("DELETE ").append(name).append(" ").append(name.charAt(0));
		stringBuilder.append(" WHERE ");
		stringBuilder.append(name.charAt(0)).append(".").append(primary).append("=:").append(primary);
		final org.hibernate.Query query = s.createQuery(stringBuilder.toString());
		query.setParameter(primary, o);
		query.executeUpdate();
	    }
	    return null;
	});
    }

    @Override
    public void delete(IEntity... objects) {
	if (ArrayUtils.isEmpty(objects)) {
	    return;
	}
	Class<? extends IEntity> type = objects[0].getClass();
	doAction(type, s -> {
	    for (Object o : objects) {
		s.delete(o);
	    }
	    return null;
	});
    }

    @Override
    public void save(IEntity... objects) {
	if (ArrayUtils.isEmpty(objects)) {
	    return;
	}
	Class<? extends IEntity> type = objects[0].getClass();
	doAction(type, s -> {
	    for (Object o : objects) {
		s.save(o);
	    }
	    return null;
	});
    }

    @Override
    public void update(IEntity... objects) {
	if (ArrayUtils.isEmpty(objects)) {
	    return;
	}
	Class<? extends IEntity> type = objects[0].getClass();
	doAction(type, s -> {
	    for (Object o : objects) {
		s.saveOrUpdate(o);
	    }
	    return null;
	});
    }

    @Override
    public void saveOrUpdate(IEntity... objects) {
	if (ArrayUtils.isEmpty(objects)) {
	    return;
	}
	Class<? extends IEntity> type = objects[0].getClass();
	doAction(type, s -> {
	    for (Object o : objects) {
		s.saveOrUpdate(o);
	    }
	    return null;
	});
    }

    /***
     * 注意，从Hibernate 查出来的实例不是相同的，不能回写，只能做参考
     */
    @Override
    public <T extends IEntity> List<T> query(Class<T> type, HQuery hQuery) {
	return doAction(type, s -> {
	    final org.hibernate.Query<T> query = buildQuery(type, hQuery, s);
	    return query.list();
	});

    }

    /***
     * 注意，从Hibernate 查出来的实例不是相同的，不能回写，只能做参考
     */
    @Override
    public <T extends IEntity> List<T> query(Class<T> type, HCriteria hCriteria) {
	return doAction(type, s -> {
	    final Criteria retCriteria = s.createCriteria(type);
	    buildCriteria(hCriteria, retCriteria);
	    List<T> ret = retCriteria.list();
	    // 空表。。。
	    if (ret == null) {
		return new ArrayList<T>();
	    }
	    return new ArrayList<T>(ret);
	});
    }

    /****
     * @param hQuery
     *            当 HQuery.mapParams有值时 进行 and 查询
     */
    @Override
    public int count(Class<? extends IEntity> type, HCriteria hCriteria) {

	return doAction(type, s -> {
	    final Criteria retCriteria = s.createCriteria(type);
	    buildCriteria(hCriteria, retCriteria);

	    retCriteria.setProjection(Projections.rowCount());
	    Object ret = retCriteria.uniqueResult();
	    // 空表。。。
	    if (ret == null) {
		return 0;
	    }
	    return Number.class.cast(ret).intValue();
	});
    }

    /***
     * 注意，从Hibernate 查出来的实例不是相同的，不能回写，只能做参考
     */
    @Override
    public <T extends IEntity> void forEach(Class<T> type, HQuery hQuery, Consumer<T> action) {
	doAction(type, s -> {
	    final org.hibernate.Query<T> query = buildQuery(type, hQuery, s);
	    final ScrollableResults scrollableResults = query.scroll(ScrollMode.FORWARD_ONLY);
	    try {
		int num = scrollableResults.getRowNumber();
		for (int index = 0; index < num; index++) {
		    try {
			final T entity = type.cast(scrollableResults.get(index));
			action.accept(entity);
		    } catch (Throwable throwable) {
			throwable.printStackTrace();
		    }

		}
	    } finally {
		scrollableResults.close();
	    }
	    return null;
	});
    }

    /***
     * 注意，从Hibernate 查出来的实例不是相同的，不能回写，只能做参考
     */
    @Override
    public <T extends IEntity> void forEach(Class<T> type, HCriteria hCriteria, Consumer<T> action) {
	doAction(type, s -> {
	    final Criteria retCriteria = s.createCriteria(type);
	    buildCriteria(hCriteria, retCriteria);
	    final ScrollableResults scrollableResults = retCriteria.scroll(ScrollMode.FORWARD_ONLY);
	    try {
		while (scrollableResults.next()) {
		    try {
			final T entity = type.cast(scrollableResults.get(0));
			action.accept(entity);
		    } catch (Throwable throwable) {
			throwable.printStackTrace();
		    }
		}
	    } finally {
		scrollableResults.close();
	    }
	    return null;
	});
    }

    /////////////////////// private/////////////////////////
    private <T extends IEntity> org.hibernate.Query<T> buildQuery(Class<T> type, HQuery hQuery, Session s) {
	final org.hibernate.Query<T> query = hQuery.isNamedQuery() ? s.getNamedQuery(hQuery.getSql()) : s.createSQLQuery(hQuery.getSql());
	if (hQuery.getSize() != null) {
	    query.setMaxResults(hQuery.getSize());
	    if (hQuery.getPage() != null) {
		query.setFirstResult(hQuery.startPage());
	    }
	}
	if (hQuery.getParams() != null) {
	    for (int i = 0; i < hQuery.getParams().length; i++) {
		query.setParameter(i, hQuery.getParams()[i]);
	    }
	} else if (hQuery.getMapParams() != null) {
	    Map<String, Object> params = hQuery.getMapParams();
	    for (String key : params.keySet()) {
		Object value = params.get(key);
		if (value instanceof Collection) {
		    query.setParameterList(key, (Collection) value);
		} else {
		    query.setParameter(key, value);
		}
	    }
	}

	// 编码对象处理
	// ((SQLQuery)query).addEntity(type);
	// if(Map.class.isAssignableFrom(type)){
	// query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	// }else
	if (query instanceof SQLQuery) {
	    query.setResultTransformer(new AliasToBeanResultTransformer(type));
	}
	// System.out.println(query.getClass());
	return query;
    }

    private void buildCriteria(HCriteria hCriteria, final Criteria retCriteria) {
	if (hCriteria == null || ArrayUtils.isEmpty(hCriteria.getMapParams())) {

	} else {
	    HCriteria curr = hCriteria;
	    Criterion left = null, right = null;
	    Iterator<Entry<String, Object>> iterator = curr.getMapParams().entrySet().iterator();
	    if (iterator.hasNext()) {
		Entry<String, Object> entry = iterator.next();
		left = Restrictions.eq(entry.getKey(), entry.getValue());
	    }
	    while (true) {
		while (iterator.hasNext()) {
		    Entry<String, Object> entry = iterator.next();
		    right = Restrictions.eq(entry.getKey(), entry.getValue());
		    if (curr.isQueryByAnd()) {
			left = Restrictions.and(left, right);
		    } else {
			left = Restrictions.or(left, right);
		    }
		}
		curr = curr.getNext();
		if (curr == null) {
		    break;
		}
		iterator = curr.getMapParams().entrySet().iterator();
	    }

	    if (left != null) {
		retCriteria.add(left);
	    }
	    if (hCriteria.getSize() != null) {
		retCriteria.setMaxResults(hCriteria.getSize());
		if (hCriteria.getPage() != null) {
		    retCriteria.setFirstResult(hCriteria.startPage());
		}
	    }
	}
	// System.out.println(retCriteria.toString());
    }

    private <R> R doAction(Class<?> type, Function<Session, R> action) {
	SessionFactory sessionFactory = getSessionFacory(type);
	Session session = sessionFactory.getCurrentSession();
	Transaction transaction = session.getTransaction();
	transaction.begin();
	try {
	    return action.apply(session);
	} finally {
	    transaction.commit();
	}
    }

    private SessionFactory getSessionFacory(Class<?> type) {
	DataSource dataSource = type.getAnnotation(DataSource.class);
	String sourceName = dataSource != null ? dataSource.value() : DBConfig.DATA_SOURCE_1;
	SessionFactory sessionFactory = multDbSourceManger.getDbSource(sourceName);
	if (sessionFactory == null) {
	    throw new RuntimeException("未找到数据源 : " + sourceName);
	}
	return sessionFactory;
    }
}
