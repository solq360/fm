package org.solq.fm.common.convert;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * string 转换 java 服务
 * 
 * @author solq
 */
@Service
public class StringConvertJavaService implements ApplicationContextAware {
    private static Map<Class<?>, String2Java> map = new HashMap<>();

    public static void register(String2Java... converts) {
	for (String2Java c : converts) {
	    for (Class<?> t : c.getTypes()) {
		map.put(t, c);
	    }
	}
    }

    public static Object convert(Class<?> type, String str) {
	String2Java c = map.get(type);
	if (c != null) {
	    return c.convert(type,str);
	}

	throw new RuntimeException("未支持类型转换 type {" + type + "} - data {" + str + "}");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
	Map<String, String2Java> beans = applicationContext.getBeansOfType(String2Java.class);
	if (!beans.isEmpty()) {
	    register(beans.values().toArray(new String2Java[0]));
	}
    }
}
