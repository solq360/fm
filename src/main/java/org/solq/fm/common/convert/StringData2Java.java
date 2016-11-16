package org.solq.fm.common.convert;

import org.springframework.stereotype.Component;

/**
 * string 转换 java
 * 
 * @author solq
 */
@Component
public class StringData2Java implements String2Java {

    @Override
    public Class<?>[] getTypes() {
 	return new Class<?>[]{String.class};
    }

    @Override
    public Object convert(Class<?> type,String str) {
	return str;
    }

}
