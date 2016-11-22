package org.solq.fm.common.db.model;

import java.util.Map;

/***
 * hquery查询对象
 * 
 * @author solq
 */
public class HQuery {

    private boolean namedQuery;
    private String sql;
    private Integer page;
    private Integer size;

    private Object[] params;
    private Map<String, Object> mapParams;

    public static HQuery of(String nameIndex) {
	return of(nameIndex, null, null);
    }

    public static HQuery of(String sql, Integer page, Integer size, Object... params) {
	HQuery ret = new HQuery();
	ret.sql = sql;
	ret.page = page;
	ret.size = size;
	ret.params = params;
	ret.namedQuery = true;
	return ret;
    }

    public static HQuery of(String sql, Integer page, Integer size, Map<String, Object> mapParams) {
	HQuery ret = new HQuery();
	ret.sql = sql;
	ret.page = page;
	ret.size = size;
	ret.mapParams = mapParams;
	ret.namedQuery = true;
	return ret;
    }

    public static HQuery ofSql(String sql) {
	return ofSql(sql, null, null);
    }

    public static HQuery ofSql(String sql, Integer page, Integer size, Object... params) {
	HQuery ret = of(sql, page, size, params);
	ret.namedQuery = false;
	return ret;
    }

    public static HQuery ofSql(String sql, Integer page, Integer size, Map<String, Object> mapParams) {
	HQuery ret = of(sql, page, size, mapParams);
	ret.namedQuery = false;
	return ret;
    }

    public static HQuery of(Map<String, Object> mapParams) {
	HQuery ret = new HQuery();
	ret.mapParams = mapParams;
	return ret;
    }

    public int startPage() {
	return (page - 1) * size;
    }

    public Integer getPage() {
	return page;
    }

    public Integer getSize() {
	return size;
    }

    public Object[] getParams() {
	return params;
    }

    public boolean isNamedQuery() {
	return namedQuery;
    }

    public String getSql() {
	return sql;
    }

    public Map<String, Object> getMapParams() {
	return mapParams;
    }

}
