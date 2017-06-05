package org.yl.ims.util.dbUtil.mapping.entity;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class PropertyHelper {
	private Map<String, Field> exactMap = new HashMap<String, Field>();
	private Map<String, Field> lowwercaseMap = new HashMap<String, Field>();

	public PropertyHelper(Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		for (Field f : fields) {
			f.setAccessible(true);
			exactMap.put(f.getName(), f);
			lowwercaseMap.put(f.getName().toLowerCase(), f);
		}
	}

	public static Object castParam(int code, String value) {
		Object ret;
		switch (code) {
		case 4:
			ret = Integer.parseInt(value);
			break;
		case 93:
			try {
				ret = new Timestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS").parse(value).getTime());
			} catch (Exception e) {
				e.printStackTrace();
				ret = null;
			}
			break;
		default:
			ret = value;
		}
		return ret;
	}

	public void setValue(Object obj, String propertyName, Object value) {
		Field field = null;
		field = exactMap.get(propertyName);
		if (field == null) {
			field = lowwercaseMap.get(propertyName.toLowerCase());
		}
		if (field == null) {
			return;
		}
		try {
			field.set(obj, castValue(field, value));
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public Object getValue(Object obj, String propertyName) {
		Field field = null;
		field = exactMap.get(propertyName);
		if (field == null) {
			field = lowwercaseMap.get(propertyName.toLowerCase());
		}
		if (field == null) {
			return null;
		}
		try {
			return field.get(obj);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Object castValue(Field field, Object value) {
		Class<?> type = field.getType();
		Object result = value;
		if (value instanceof Number) {
			Number n = (Number) value;
			if (type == int.class || type == Integer.class) {
				result = n.intValue();
			}
			if (type == byte.class || type == Byte.class) {
				result = n.byteValue();
			}
			if (type == short.class || type == Short.class) {
				result = n.shortValue();
			}
			if (type == long.class || type == Long.class) {
				result = n.longValue();
			}
			if (type == float.class || type == Float.class) {
				result = n.floatValue();
			}
			if (type == double.class || type == Double.class) {
				result = n.doubleValue();
			}
			if (type == boolean.class || type == Boolean.class) {
				result = (n.intValue() != 0);
			}
			if (type == Date.class) {
				result = new Date(n.longValue());
			}
		}
		if (type == boolean.class || type == Boolean.class) {
			if (value instanceof String) {
				result = "true".equalsIgnoreCase(value.toString());
			}
		}
		return result;
	}
}
