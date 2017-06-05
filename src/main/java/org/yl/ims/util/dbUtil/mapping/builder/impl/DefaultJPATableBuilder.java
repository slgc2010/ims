package org.yl.ims.util.dbUtil.mapping.builder.impl;

import java.lang.reflect.Field;

import org.yl.ims.util.dbUtil.annotation.Id;
import org.yl.ims.util.dbUtil.mapping.Table;
import org.yl.ims.util.dbUtil.mapping.builder.AnnotationTableBuilder;

public class DefaultJPATableBuilder implements AnnotationTableBuilder {
	public void parse(Table table, Class<?> clazz, Field field) {
		Id id = AnnotationUtils.getAnnotation(field, Id.class);
		if (id != null) {
			table.getPrimarykey().addColumn(table.getColumnByProperty(field.getName()));
		}
	}
}
