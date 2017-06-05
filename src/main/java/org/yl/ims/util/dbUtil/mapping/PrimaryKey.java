package org.yl.ims.util.dbUtil.mapping;

import java.util.HashSet;
import java.util.Set;

public class PrimaryKey {
	private String name;
	private Set<String> properties = new HashSet<String>();
	private Set<String> columns = new HashSet<String>();

	public String getName() {
		return name;
	}

	public Set<String> getColumns() {
		return columns;
	}

	public void setColumns(Set<String> columns) {
		this.columns = columns;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<String> getProperties() {
		return properties;
	}

	public void setProperties(Set<String> properties) {
		this.properties = properties;
	}

	public void addProperty(String property) {
		this.properties.add(property);
	}

	public boolean hasProperty(String property) {
		return this.properties.contains(property);
	}

	public void addColumn(String column) {
		this.columns.add(column);
	}

	public void addColumn(ColumnMapping column) {
		this.columns.add(column.getName());
		this.properties.add(column.getProperty());
	}

	public boolean hasColumn(String column) {
		return this.columns.contains(column);
	}
}
