package org.solq.fm.module.account.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.solq.fm.common.db.JsonType;
import org.solq.fm.common.db.model.IEntity;

@Entity
@DynamicUpdate(true)
@NamedQueries({
	@NamedQuery(name = "testxx1", query = "select t.id,t.name from Account as t"),
	@NamedQuery(name = "testxx2", query = "from Account"),
})
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

    @Type(type = JsonType.NAME)
    private Set<Long> tmp1 = new HashSet<>();
    @Type(type = JsonType.NAME)
    @Column(name="a")
    private Map<Long, Account2> tmp2 = new HashMap<>();

    public static Account of(long id, String name) {
	Account ret = new Account();
	ret.id = id;
	ret.name = name;
	for (long i = 0; i < 5; i++) {
	    ret.tmp1.add(i);
	}
	ret.tmp2.put(id, Account2.of(id, name));
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

    public void setAddr(String addr) {
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

    public Set<Long> getTmp1() {
	return tmp1;
    }

    public void setTmp1(Set<Long> tmp1) {
	this.tmp1 = tmp1;
    }

    public Map<Long, Account2> getTmp2() {
	return tmp2;
    }

    public void setTmp2(Map<Long, Account2> tmp2) {
	this.tmp2 = tmp2;
    }

}
