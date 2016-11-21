package org.solq.fm.test.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.solq.fm.common.db.model.IEntity;
import org.solq.fm.common.db.model.IdbDrive;
import org.solq.fm.common.db.model.HQuery;
import org.solq.fm.module.account.model.Account;
import org.springframework.stereotype.Component;

@Component
public class CacheDbDrive implements IdbDrive {

    @PostConstruct
    private void init() {
	System.out.println("xxxxxxx");

	List<Account> list = query(Account.class, new HQuery());

	// for(Account a : list){
	// System.out.println(a.getId());
	// }

    }

    @Override
    public <T extends IEntity> T find(Class<T> type, Serializable key) {
	// TODO Auto-generated method stub
	return null;
    }

 
    @Override
    public void saveOrUpdate(IEntity... objects) {
	// TODO Auto-generated method stub

    }

    @Override
    public <T extends IEntity> List<T> query(Class<T> type, HQuery query) {
	List<Account> list = new ArrayList<>();
	list.add(Account.of(1, "aa"));
	return (List<T>) list;
    }

    @Override
    public void delete(Class<? extends IEntity> type, Serializable... keys) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void delete(IEntity... objects) {
	// TODO Auto-generated method stub
	
    }

}
