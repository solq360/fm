package org.solq.fm.common.util;

import java.util.Collection;
import java.util.Map;

public abstract class ArrayUtils {

    public static boolean isEmpty(Object[] ar) {
 	return ar == null ||ar.length==0;
    }

    public static boolean isEmpty(Collection<?> ar) {
	return ar == null ||ar.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map) {
	return map == null ||map.isEmpty();
    }

}
