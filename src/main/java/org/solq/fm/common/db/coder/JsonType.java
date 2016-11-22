package org.solq.fm.common.db.coder;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.TextType;
import org.hibernate.usertype.DynamicParameterizedType;
import org.hibernate.usertype.UserType;
import org.solq.fm.common.util.JsonUtils;

/***
 * Hibernate json 支持
 * 
 * @author solq
 */
public class JsonType implements DynamicParameterizedType, UserType {
    // 实体类
    private Class<?> entityClass;
    // 字段javaType
    private Type fieldType;
    // 字段javaClass
    private Class<?> returnedClass;

    @Override
    public void setParameterValues(Properties parameters) {
	returnedClass = ((ParameterType) parameters.get(PARAMETER_TYPE)).getReturnedClass();
	final String fieldName = parameters.getProperty(PROPERTY);
	try {
	    entityClass = Class.forName((String) parameters.get(ENTITY));
	    fieldType = entityClass.getDeclaredField(fieldName).getGenericType();
	} catch (NoSuchFieldException | SecurityException | ClassNotFoundException e) {
	    throw new RuntimeException("JsonType error : {} ", e);
	}
    }

    @Override
    public int[] sqlTypes() {
	return new int[] { TextType.INSTANCE.sqlType() };
    }

    @Override
    public Class<?> returnedClass() {
	return returnedClass;
    }

    @Override
    public boolean equals(Object o, Object o1) throws HibernateException {
	if (o == o1) {
	    return true;
	}
	if (o == null || o == null) {
	    return false;
	}

	return o.equals(o1);
    }

    @Override
    public int hashCode(Object o) throws HibernateException {
	return o.hashCode();
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
	return value;
    }

    @Override
    public boolean isMutable() {
	return true;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
	return ((Serializable) value);
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
	return cached;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
	return original;
    }

    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {
	String json = resultSet.getString(names[0]);
	if (json == null || json.isEmpty()) {
	    return null;
	}
	Object result = JsonUtils.string2Object(json, fieldType);
	return result;
    }

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
	if (value != null) {
	    String strValue = JsonUtils.object2String(value);
	    preparedStatement.setString(index, strValue);
	} else {
	    preparedStatement.setNull(index, TextType.INSTANCE.sqlType());
	}
    }

}
