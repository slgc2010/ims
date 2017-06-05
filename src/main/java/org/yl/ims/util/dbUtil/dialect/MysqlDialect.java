package org.yl.ims.util.dbUtil.dialect;

public class MysqlDialect extends Dialect {
	private String schema;

	public MysqlDialect(String schema) {
		super();
		this.schema = schema;
	}

	public char openQuote() {
		return '`';
	}

	public char closeQuote() {
		return '`';
	}

	public boolean isTableNameQuoted() {
		return true;
	}

	public boolean isColumnNameQuoted() {
		return true;
	}

	@Override
	public String getCatalog() {
		return null;
	}

	@Override
	public String getSchema() {
		return schema;
	}
}
