package org.yl.ims.model;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.jdbc.core.RowMapper;

public abstract class BaseRowMapper<T> implements RowMapper<T> {
	static final String serialVersionUID = "serialVersionUID";

	public static Object setEntityProperties(Class<?> clazz, ResultSet rs) {
		Object obj = null;
		try {
			obj = clazz.newInstance();
			Field[] fields = clazz.getDeclaredFields();
			for (Field f : fields) {
				String field = f.toString();
				if ((field.substring(field.lastIndexOf(".") + 1)).equals(serialVersionUID))
					continue;
				String fType = f.getGenericType().toString();
				Pattern pattern = Pattern.compile("^class.*");// 查找以class开头
				Matcher matcher = pattern.matcher(fType);
				boolean isRight = matcher.matches(); // 当条件满足时，将返回true，否则返回false
				if (isRight) {
					Class<?> c = Class.forName(fType.substring(6));
					PropertyDescriptor pd = new PropertyDescriptor(f.getName(), clazz);
					Method wM = pd.getWriteMethod();// 获得写方法
					if (c.equals(Integer.class)) {
						wM.invoke(obj, rs.getInt(f.getName()));
					} else if (c.equals(Byte.class)) {
						wM.invoke(obj, rs.getByte(f.getName()));
					} else if (c.equals(Long.class)) {
						wM.invoke(obj, rs.getLong(f.getName()));
					} else if (c.equals(Double.class)) {
						wM.invoke(obj, rs.getDouble(f.getName()));
					} else if (c.equals(Float.class)) {
						wM.invoke(obj, rs.getFloat(f.getName()));
					} else if (c.equals(Character.class)) {
						wM.invoke(obj, rs.getCharacterStream(f.getName()));
					} else if (c.equals(Short.class)) {
						wM.invoke(obj, rs.getShort(f.getName()));
					} else if (c.equals(BigDecimal.class)) {
						wM.invoke(obj, rs.getBigDecimal(f.getName()));
					} else if (c.equals(Boolean.class)) {
						wM.invoke(obj, rs.getBoolean(f.getName()));
					} else if (c.equals(Date.class)) {
						wM.invoke(obj, rs.getDate(f.getName()));
					} else if (c.isPrimitive()) {
						// TODO anything
					} else {
						rs.getString(f.getName());
						wM.invoke(obj, rs.getString(f.getName()));
					}
				}
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return obj;
	}
}
