package org.solq.fm.common.lock;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.solq.fm.common.util.MapUtils;
/***
 * lock 托管
 * 
 * @author solq
 */
public class LockObject<K> {

    private ConcurrentHashMap<K, Lock> locks = new ConcurrentHashMap<>();

    public Lock getLock(K key) {
	Lock ret = locks.get(key);
	if (ret == null) {
	    ret = new ReentrantLock();
	    ret = MapUtils.putIfAbsent(locks, key, ret);
	}
	return ret;
    }

    public void lock(K key) {
	Lock lock = getLock(key);
	lock.lock();
    }

    public void remove(K key) {
	locks.remove(key);
    }

    public void unlock(K key) {
	Lock ret = locks.get(key);
	if (ret == null) {
	    throw new RuntimeException("解释lock失败 : 未找到 [" + key + "] 相应锁对象");
	}
	ret.unlock();
    }
}
