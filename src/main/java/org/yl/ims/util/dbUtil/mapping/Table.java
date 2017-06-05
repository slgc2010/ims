package org.yl.ims.util.dbUtil.mapping;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("rawtypes")
public class Table {
	private String name;
	private Class tableClass;
	private boolean schemaupdate = true;
	private Map<String, ColumnMapping> columns = new LinkedHashMap<String, ColumnMapping>();
	private Map<String, ColumnMapping> properties = new LinkedHashMap<String, ColumnMapping>();
	private PrimaryKey primarykey = new PrimaryKey();;
	private Set<ForeignKey> foreignkeys = new HashSet<ForeignKey>();
	private Set<Index> indexes = new HashSet<Index>();
	private Set<UniqueKey> uniques = new HashSet<UniqueKey>();

	public boolean isSchemaupdate() {
		return schemaupdate;
	}

	public void setSchemaupdate(boolean schemaupdate) {
		this.schemaupdate = schemaupdate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Class getTableClass() {
		return tableClass;
	}

	public void setTableClass(Class tableClass) {
		this.tableClass = tableClass;
	}

	public Map<String, ColumnMapping> getColumns() {
		return columns;
	}

	public Map<String, ColumnMapping> getProperties() {
		return properties;
	}

	public PrimaryKey getPrimarykey() {
		return primarykey;
	}

	public void setPrimarykey(PrimaryKey primarykey) {
		this.primarykey = primarykey;
	}

	public Set<ForeignKey> getForeignkeys() {
		return foreignkeys;
	}

	public void setForeignkeys(Set<ForeignKey> foreignkeys) {
		this.foreignkeys = foreignkeys;
	}

	public Set<Index> getIndexes() {
		return indexes;
	}

	public void setIndexes(Set<Index> indexes) {
		this.indexes = indexes;
	}

	public Set<UniqueKey> getUniques() {
		return uniques;
	}

	public void setUniques(Set<UniqueKey> uniques) {
		this.uniques = uniques;
	}

	public void addColumn(ColumnMapping column) {
		columns.put(column.getName(), column);
		properties.put(column.getProperty(), column);
	}

	public ColumnMapping getColumn(String name) {
		return columns.get(name);
	}

	public ColumnMapping getColumnByProperty(String property) {
		return properties.get(property);
	}

	public boolean containsProperty(String propertyName) {
		return properties.containsKey(propertyName);
	}

	public boolean containsColumn(String columnName) {
		return columns.containsKey(columnName);
	}
}
