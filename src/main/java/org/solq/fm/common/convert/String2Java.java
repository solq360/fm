package org.solq.fm.common.convert;


/**
 * string 转换 java
 * 
 * @author solq
 */
public interface String2Java {

    public Class<?>[] getTypes();

    public Object convert(Class<?> type,String str);
}
