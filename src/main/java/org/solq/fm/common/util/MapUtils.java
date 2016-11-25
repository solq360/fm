package org.solq.fm.common.util;

import java.util.concurrent.ConcurrentHashMap;

public abstract class MapUtils {

    public static <K,V> V putIfAbsent(ConcurrentHashMap<K, V> map, K key, V value) {
	V pre = map.putIfAbsent(key, value);
	return pre == null ? value : pre;
    }
}
