package org.yl.ims.util.dbUtil.mapping.builder.impl;

import java.lang.reflect.Field;

import org.springframework.util.StringUtils;
import org.yl.ims.util.dbUtil.annotation.Column;
import org.yl.ims.util.dbUtil.mapping.ColumnMapping;
import org.yl.ims.util.dbUtil.mapping.builder.AnnotationColumnBuilder;


public class DefaultJPABuilder implements AnnotationColumnBuilder{

	public void parse(ColumnMapping column, Field field) {
		Column annotation=AnnotationUtils.getAnnotation(field, Column.class);
		if(annotation!=null){
			if(StringUtils.hasText(annotation.name())){
				column.setName(annotation.name());
			}
			if(StringUtils.hasText(annotation.type())){
				column.setJdbcType(annotation.type());
			}
			if(annotation.length()!=255){
				column.setLength(annotation.length());
			}
		}
	}
	
}
