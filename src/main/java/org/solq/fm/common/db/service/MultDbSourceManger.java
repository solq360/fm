package org.solq.fm.common.db.service;

import java.util.HashMap;
import java.util.Map;

/***
 * 多个数据库来源管理
 * 
 * @author solq
 */
public class MultDbSourceManger<T> {

    private Map<String, T> dbSourceMap = new HashMap<>();

    public T getDbSource(String dataSource) {
	return dbSourceMap.get(dataSource);
    }

    public synchronized void register(String dataSource, T source) {
	dbSourceMap.put(dataSource, source);
    }

    public Map<String, T> getDbSourceMap() {
	return dbSourceMap;
    }

    public void setDbSourceMap(Map<String, T> dbSourceMap) {
        this.dbSourceMap = dbSourceMap;
    }

}
