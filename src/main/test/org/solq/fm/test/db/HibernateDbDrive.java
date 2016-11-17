package org.solq.fm.test.db;

import javax.annotation.PostConstruct;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.solq.fm.module.account.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HibernateDbDrive {

    @Autowired
    private SessionFactory sessionFactory;

    @PostConstruct
    private void init() {
	Session session = sessionFactory.openSession();
 	Transaction transaction = session.getTransaction();
	transaction.begin();
	session.saveOrUpdate(Account.of(1, "aesr"));
	transaction.commit();
	session.close();
	
	session = sessionFactory.openSession();
 	transaction = session.getTransaction();
	transaction.begin();
	
	Account a = session.load(Account.class, 1L);
	a.setName("aaa");
	
	session.saveOrUpdate("account-1", a);
	
	
	transaction.commit();
	session.close();
	
    }
}
