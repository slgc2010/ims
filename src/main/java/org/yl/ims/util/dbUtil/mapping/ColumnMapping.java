package org.yl.ims.util.dbUtil.mapping;

public class ColumnMapping {
	private String property;
	private String name;
	private Class<?> javaType;
	private String jdbcType;
	private int length;
	private int decimalDigits;
	private boolean nullable=true;
	private String commet;
	private String defaultValue;
	private boolean generatedValue=false;
	public String getDefaultValue() {
		return defaultValue;
	}
	
	public int getDecimalDigits() {
		return decimalDigits;
	}

	public void setDecimalDigits(int decimalDigits) {
		this.decimalDigits = decimalDigits;
	}

	public boolean isGeneratedValue() {
		return generatedValue;
	}

	public void setGeneratedValue(boolean generatedValue) {
		this.generatedValue = generatedValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getCommet() {
		return commet;
	}
	public void setCommet(String commet) {
		this.commet = commet;
	}
	public boolean isNullable() {
		return nullable;
	}
	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Class<?> getJavaType() {
		return javaType;
	}
	public void setJavaType(Class<?> javaType) {
		this.javaType = javaType;
	}
	public String getJdbcType() {
		return jdbcType;
	}
	public void setJdbcType(String jdbcType) {
		this.jdbcType = jdbcType;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	
}
