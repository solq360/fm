package org.solq.fm.common.db.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.solq.fm.common.db.model.HCriteria;
import org.solq.fm.common.db.model.HQuery;
import org.solq.test.fm.db.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestCahce {
    @Autowired
    private CacheDbOperation cacheDbOperation;

    @PostConstruct
    public void test_query() {
	List<Account> list = cacheDbOperation.query(Account.class, HQuery.of("testxx1", 10, 50));
	list.forEach((a) -> {
	    System.out.println("hql : " + a.getId());

	});
	list = cacheDbOperation.query(Account.class, HQuery.ofSql("select a.id,a.name from Account a", 10, 50));
	list.forEach((a) -> {
	    System.out.println("sql : " + a.getId());
	});

	list = cacheDbOperation.query(Account.class, HQuery.of("testxx2", 1, 50));
	list.forEach((a) -> {
	    System.out.println("testxx2 : " + a.getId());
	});

	Map<String, Object> mapParams = new HashMap<>();
	mapParams.put("id", 3L);
	mapParams.put("birth", 1);
	HCriteria hCriteria = HCriteria.ofOr(mapParams);

	Map<String, Object> nextParams = new HashMap<>();
	nextParams.put("id", 5L);
	hCriteria.putNext(HCriteria.ofOr(nextParams));

	cacheDbOperation.forEach(Account.class, hCriteria, (a) -> {
	    System.out.println(a.getId());
	});

	// forEach(Account.class, HQuery.ofSql("select a.id,a.name from Account
	// a", 10, 50), (a) -> {
	// System.out.println(a.getId());
	// });
	Account a = cacheDbOperation.find(Account.class, 6L);
	Account b = cacheDbOperation.find(Account.class, 7L);
	cacheDbOperation.saveOrUpdate(a, b);
    }

}
