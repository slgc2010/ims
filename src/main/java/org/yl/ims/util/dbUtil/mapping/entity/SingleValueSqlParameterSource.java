package org.yl.ims.util.dbUtil.mapping.entity;

import org.springframework.jdbc.core.namedparam.AbstractSqlParameterSource;

public class SingleValueSqlParameterSource extends AbstractSqlParameterSource {
	private Object value;

	public SingleValueSqlParameterSource(Object value) {
		this.value = value;
	}

	@Override
	public boolean hasValue(String paramName) {
		return true;
	}

	@Override
	public Object getValue(String paramName) throws IllegalArgumentException {
		return value;
	}

}
