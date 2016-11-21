package org.solq.fm.module.account.model;

import javax.persistence.Id;

import org.solq.fm.common.db.model.IEntity;

public class Account2 implements IEntity {

    @Id
    private long id;
    private String name;
    private int birth;

    private String addr;
    private String ip;

    public static Account2 of(long id, String name) {
	Account2 ret = new Account2();
	ret.id = id;
	ret.name = name;
	for (int i = 0; i < 5; i++) {

	}
	return ret;
    }

    // get and setter

    @Override
    public Object getId() {
	return id;
    }

    public String getName() {
	return name;
    }

    public int getBirth() {
	return birth;
    }

    public String getAddr() {
	return addr;
    }

    public String getIp() {
	return ip;
    }

    void setId(long id) {
	this.id = id;
    }

    public void setName(String name) {
	this.name = name;
    }

    void setBirth(int birth) {
	this.birth = birth;
    }

    public void setAddr(String addr) {
	this.addr = addr;
    }

    void setIp(String ip) {
	this.ip = ip;
    }

}
