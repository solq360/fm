package org.solq.fm.test.db;

import java.io.Serializable;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Function;

import javax.annotation.PostConstruct;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.metadata.ClassMetadata;
import org.solq.fm.common.db.model.HQuery;
import org.solq.fm.common.db.model.IEntity;
import org.solq.fm.common.db.model.IdbDrive;
import org.solq.fm.module.account.model.Account;
import org.solq.fm.module.account.model.Account2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HibernateDbDrive implements IdbDrive {

    @Autowired
    private SessionFactory sessionFactory;

    private <R> R doAction(Function<Session, R> action) {
	Session session = sessionFactory.getCurrentSession();
	Transaction transaction = session.getTransaction();
	transaction.begin();
	try {
	    return action.apply(session);
	} finally {
	    transaction.commit();
	}

    }

    @PostConstruct
    private void init() {
	testQueryList();
    }

    void testQueryList() {
	List<Account> list = query(Account.class, HQuery.of("testxx1", 10, 50));
	System.out.println(list.size());
	
	list = query(Account.class, HQuery.ofSql("select a.id,a.name from Account a", 10, 50));
	System.out.println(list.size());

	// list = query(Account.class, HQuery.of("testxx2"));
	// System.out.println(list.size());
    }

    void testdelete() {
	saveOrUpdate(Account.of(1, "aesr"));
	saveOrUpdate(Account.of(1, "aesr"));

	delete(Account.of(1L, "aa"));
	delete(Account.class, 2L);
    }

    void test2() {

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

    void test1() {
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

    @Override
    public <T extends IEntity> T find(Class<T> type, Serializable key) {
	return (T) doAction(s -> {
	    return s.get(type, key);
	});
    }

    @SuppressWarnings("deprecation")
    @Override
    public void delete(Class<? extends IEntity> type, Serializable... keys) {

	doAction(s -> {
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
	doAction(s -> {
	    for (Object o : objects) {
		s.delete(o);
	    }
	    return null;
	});
    }

    @Override
    public void saveOrUpdate(IEntity... objects) {
	doAction(s -> {
	    for (Object o : objects) {
		s.saveOrUpdate(o);
	    }
	    return null;
	});
    }

    @SuppressWarnings({ "deprecation", "unchecked" })
    @Override
    public <T extends IEntity> List<T> query(Class<T> type, HQuery hQuery) {
	return doAction(s -> {
	    final org.hibernate.Query<T> query = hQuery.isNamedQuery() ? s.createNamedQuery(hQuery.getSql()) : s.createQuery(hQuery.getSql());

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
	    }
	    return query.list();
	});

    }

}
