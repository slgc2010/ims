package org.yl.ims.util.dbUtil.mapping.builder;

import java.lang.reflect.Field;

import org.yl.ims.util.dbUtil.mapping.ColumnMapping;


public interface AnnotationColumnBuilder {
	public void parse(ColumnMapping column,Field field);
}
