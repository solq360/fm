package org.solq.test.fm.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.solq.fm.common.db.model.HCriteria;
import org.solq.fm.common.db.model.HQuery;
import org.solq.fm.common.db.service.HibernateDbOperation;
import org.solq.test.fm.db.model.Account;
import org.solq.test.fm.db.model.Account2;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class TestHibernateDbOperation extends HibernateDbOperation {

    @Test
    public void test_query() {
	List<Account> list = query(Account.class, HQuery.of("testxx1", 10, 50));
	System.out.println(list.size());

	list = query(Account.class, HQuery.ofSql("select a.id,a.name from Account a", 10, 50));
	System.out.println(list.size());

    }

    @PostConstruct
    public void test_criteria() {
	Map<String, Object> mapParams = new HashMap<>();
	mapParams.put("id", 3L);
	mapParams.put("birth", 1);

	int count = count(Account.class, HCriteria.ofOr(mapParams));
	List<Account> list = query(Account.class, HCriteria.ofOr(mapParams));
	System.out.println(count);
	System.out.println(list.size());
    }

    @Test
    public void testdelete() {
	saveOrUpdate(Account.of(1, "aesr"));
	saveOrUpdate(Account.of(1, "aesr"));

	delete(Account.of(1L, "aa"));
	delete(Account.class, 2L);
    }

    @Test
    public void test2() {

	int count = 5000000;
	new Thread(() -> {
	    long start = System.currentTimeMillis();
	    for (int i = 0; i < count; i++) {
		saveOrUpdate(Account.of(i, "aesr"));
	    }
	    long end = System.currentTimeMillis();
	    System.out.println(("one : " + (end - start)));
	}).start();

    }

    @Test
    public void test1() {
	saveOrUpdate(Account.of(1, "aesr"));
	Account aa = find(Account.class, 1L);
	Account bb = find(Account.class, 1L);
	System.out.println(aa.equals(bb));

	Account a = find(Account.class, 1L);
	System.out.println(a.getTmp1().size());
	System.out.println(a.getTmp2().size());
	for (Long k : a.getTmp1()) {
	    System.out.println(k);
	}
	for (Entry<Long, Account2> entry : a.getTmp2().entrySet()) {
	    System.out.println(entry.getKey());
	}
	int count = 1;
	new Thread(() -> {

	    long start = System.currentTimeMillis();
	    for (int i = 0; i < count; i++) {
		a.setAddr("a" + i);
		saveOrUpdate(a);
	    }
	    long end = System.currentTimeMillis();
	    System.out.println(("one : " + (end - start)));
	}).start();

	new Thread(() -> {

	    long start = System.currentTimeMillis();
	    for (int i = 0; i < count; i++) {
		a.setName("a" + i);
		saveOrUpdate(a);
	    }
	    long end = System.currentTimeMillis();
	    System.out.println("two : " + (end - start));
	}).start();

    }

}
