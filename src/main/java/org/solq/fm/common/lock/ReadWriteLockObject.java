package org.solq.fm.common.lock;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.solq.fm.common.util.MapUtils;

/***
 * readWritelock 托管
 * 
 * @author solq
 */
public class ReadWriteLockObject<K> {

    private ConcurrentHashMap<K, ReadWriteLock> rwlocks = new ConcurrentHashMap<>();

    public ReadWriteLock getLock(K key) {
	ReadWriteLock ret = rwlocks.get(key);
	if (ret == null) {
	    ret = new ReentrantReadWriteLock();
	    ret = MapUtils.putIfAbsent(rwlocks, key, ret);
	}
	return ret;
    }

    public void remove(K key) {
	rwlocks.remove(key);
    }
}
