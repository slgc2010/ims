package org.yl.ims.util.dbUtil.mapping.builder;

import java.lang.reflect.Field;

import org.yl.ims.util.dbUtil.mapping.Table;

public interface AnnotationTableBuilder {
	public void parse(Table table, Class<?> clazz, Field field);
}
