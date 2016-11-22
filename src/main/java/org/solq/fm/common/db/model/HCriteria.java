package org.solq.fm.common.db.model;

import java.util.Map;

/***
 * HCriteria查询条件对象
 * 
 * @author solq
 */
public class HCriteria {
    // true and 逻辑
    private boolean queryByAnd;
    private Integer page;
    private Integer size;
    private Map<String, Object> mapParams;

    private HCriteria next;

    public static HCriteria of(Map<String, Object> mapParams, Integer page, Integer size, boolean queryByAnd) {
	HCriteria ret = new HCriteria();
	ret.page = page;
	ret.size = size;
	ret.mapParams = mapParams;
	ret.queryByAnd = queryByAnd;
	return ret;
    }

    public static HCriteria ofAnd(Map<String, Object> mapParams) {
	HCriteria ret = of(mapParams, null, null, true);
	return ret;
    }

    public static HCriteria ofOr(Map<String, Object> mapParams) {
	HCriteria ret = of(mapParams, null, null, false);
	return ret;
    }

    public void putNext(HCriteria... hCriterias) {
	HCriteria first = this;
	for (HCriteria criteria : hCriterias) {
	    first.next = criteria;
	    first = criteria;
	}
	first = null;
    }

    @SuppressWarnings("unchecked")
    public void putNext(Map<String, Object>... mapParams) {
	HCriteria first = this;
	while (this.next != null) {
	    first = next;
	}
	for (Map<String, Object> params : mapParams) {
	    HCriteria criteria = this.queryByAnd ? ofAnd(params) : ofOr(params);
	    first.next = criteria;
	    first = criteria;
	}
	first = null;
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

    public Map<String, Object> getMapParams() {
	return mapParams;
    }

    public boolean isQueryByAnd() {
	return queryByAnd;
    }

    public HCriteria getNext() {
	return next;
    }

}
