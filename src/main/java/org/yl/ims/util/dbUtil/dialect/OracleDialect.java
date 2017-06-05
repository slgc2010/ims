package org.yl.ims.util.dbUtil.dialect;

public class OracleDialect extends Dialect {
	private String schema;

	public OracleDialect(String schema) {
		super();
		this.schema = schema;
		defaultJDBCType.put(Integer.class, "NUMBER");
		defaultJDBCType.put(int.class, "NUMBER");
		defaultJDBCType.put(Byte.class, "NUMBER");
		defaultJDBCType.put(byte.class, "NUMBER");
		defaultJDBCType.put(Short.class, "NUMBER");
		defaultJDBCType.put(Long.class, "NUMBER");
		defaultJDBCType.put(short.class, "NUMBER");
		defaultJDBCType.put(long.class, "NUMBER");
		defaultJDBCType.put(String.class, "VARCHAR2");
		defaultLengthMap.put("VARCHAR2", 255);
	}

	public String autoGeneratedValue() {
		return "";
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