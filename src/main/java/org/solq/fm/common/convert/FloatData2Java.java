package org.solq.fm.common.convert;

import org.springframework.stereotype.Component;

/**
 * Double data 转换 java
 * 
 * @author solq
 */
@Component
public class FloatData2Java implements String2Java {

    @Override
    public Class<?>[] getTypes() {
 	return new Class<?>[]{Float.class,float.class};
    }

    @Override
    public Object convert(Class<?> type,String str) {
	return new Float(str);
    }

}
