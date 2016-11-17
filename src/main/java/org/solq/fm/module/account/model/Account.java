package org.solq.fm.module.account.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicUpdate;
import org.solq.fm.common.db.model.IEntity;

@Entity
@DynamicUpdate(true)
public class Account implements IEntity {

    @Id
    private long id;
    private String name;
    private int birth;

    private String addr;
    private String ip;

    /** 分配置的节点机子 **/
    private String targetNode;
    /** 分配置的数据源 **/
    private String targetSource;

    public static Account of(long id, String name) {
	Account ret = new Account();
	ret.id = id;
	ret.name = name;
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

    public String getTargetNode() {
	return targetNode;
    }

    public String getTargetSource() {
	return targetSource;
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

    void setAddr(String addr) {
	this.addr = addr;
    }

    void setIp(String ip) {
	this.ip = ip;
    }

    void setTargetNode(String targetNode) {
	this.targetNode = targetNode;
    }

    void setTargetSource(String targetSource) {
	this.targetSource = targetSource;
    }

}
