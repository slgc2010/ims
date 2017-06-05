package org.yl.ims.util.dbUtil.mapping.builder.impl;

import java.lang.reflect.Field;

import org.yl.ims.util.dbUtil.annotation.Comment;
import org.yl.ims.util.dbUtil.mapping.ColumnMapping;
import org.yl.ims.util.dbUtil.mapping.builder.AnnotationColumnBuilder;


public class CommentColumnBuilder implements AnnotationColumnBuilder {
	public void parse(ColumnMapping column, Field field) {
		Comment comment = AnnotationUtils.getAnnotation(field, Comment.class);
		if (comment != null) {
			column.setCommet(comment.value());
		}
	}
}
