package org.solq.fm.common.convert;

import org.springframework.stereotype.Component;

/**
 * long data 转换 java
 * 
 * @author solq
 */
@Component
public class LongData2Java implements String2Java {

 
    @Override
    public Object convert(Class<?> type,String str) {
	return new Long(str);
    }

    @Override
    public Class<?>[] getTypes() {
 	return new Class<?>[]{Long.class,long.class};
    }

}
