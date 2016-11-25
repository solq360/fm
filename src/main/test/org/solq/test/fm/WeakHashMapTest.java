package org.solq.test.fm;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import org.solq.test.fm.db.model.Account;

public class WeakHashMapTest {

    public static void main(String[] args) {
	WeakHashMap wMap = new WeakHashMap();
	Account p1 = Account.of(1L, "aaa");
	wMap.put(p1, "zs");
	p1 = null;
	// gc
	try {
	    System.gc();
	    Thread.sleep(1000);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}

	for (Object o : wMap.entrySet()) {
	    System.out.println(o);
	}
    }
}
