package org.yl.ims.util.dbUtil.mapping.metadata;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TableMetadata {
	private final String catalog;
	private final String schema;
	private final String name;
	private final Map<String, ColumnMetadata> columns = new HashMap<String, ColumnMetadata>();
	private final Map<String, ForeignKeyMetadata> foreignKeys = new HashMap<String, ForeignKeyMetadata>();
	private final Map<String, IndexMetadata> indexes = new HashMap<String, IndexMetadata>();
	private PrimaryKeyMetadata primarykey;

	TableMetadata(ResultSet rs, DatabaseMetaData meta, boolean extras) throws SQLException {
		catalog = rs.getString("TABLE_CAT");
		schema = rs.getString("TABLE_SCHEM");
		name = rs.getString("TABLE_NAME");
		initColumns(meta);
		initPK(meta);
		if (extras) {
			initForeignKeys(meta);
			initIndexes(meta);
		}
	}

	private void initPK(DatabaseMetaData meta) throws SQLException {
		ResultSet rs = meta.getPrimaryKeys(catalog, schema, name);
		this.primarykey = new PrimaryKeyMetadata(rs);
		if (primarykey.getColumns().size() == 0) {
			this.primarykey = null;
		}
	}

	public String getName() {
		return name;
	}

	public String getCatalog() {
		return catalog;
	}

	public String getSchema() {
		return schema;
	}

	@Override
	public String toString() {
		return "TableMetadata(" + name + ')';
	}

	public ColumnMetadata getColumnMetadata(String columnName) {
		return (ColumnMetadata) columns.get(StringHelper.toLowerCase(columnName));
	}

	public ForeignKeyMetadata getForeignKeyMetadata(String keyName) {
		return (ForeignKeyMetadata) foreignKeys.get(StringHelper.toLowerCase(keyName));
	}

	public PrimaryKeyMetadata getPrimarykey() {
		return primarykey;
	}

	public IndexMetadata getIndexMetadata(String indexName) {
		return (IndexMetadata) indexes.get(StringHelper.toLowerCase(indexName));
	}

	private void addForeignKey(ResultSet rs) throws SQLException {
		String fk = rs.getString("FK_NAME");
		if (fk == null) {
			return;
		}
		ForeignKeyMetadata info = getForeignKeyMetadata(fk);
		if (info == null) {
			info = new ForeignKeyMetadata(rs);
			foreignKeys.put(StringHelper.toLowerCase(info.getName()), info);
		}
		info.addReference(rs);
	}

	private void addIndex(ResultSet rs) throws SQLException {
		String index = rs.getString("INDEX_NAME");
		if (index == null) {
			return;
		}
		IndexMetadata info = getIndexMetadata(index);
		if (info == null) {
			info = new IndexMetadata(rs);
			indexes.put(StringHelper.toLowerCase(info.getName()), info);
		}
		info.addColumn(getColumnMetadata(rs.getString("COLUMN_NAME")));
	}

	public void addColumn(ResultSet rs) throws SQLException {
		String column = rs.getString("COLUMN_NAME");
		if (column == null) {
			return;
		}
		if (getColumnMetadata(column) == null) {
			ColumnMetadata info = new ColumnMetadata(rs);
			columns.put(StringHelper.toLowerCase(info.getName()), info);
		}
	}

	public Map<String, ColumnMetadata> getColumns() {
		return columns;
	}

	private void initForeignKeys(DatabaseMetaData meta) throws SQLException {
		ResultSet rs = null;
		try {
			rs = meta.getImportedKeys(catalog, schema, name);
			while (rs.next()) {
				addForeignKey(rs);
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

	private void initIndexes(DatabaseMetaData meta) throws SQLException {
		ResultSet rs = null;
		try {
			rs = meta.getIndexInfo(catalog, schema, name, false, true);
			while (rs.next()) {
				if (rs.getShort("TYPE") == DatabaseMetaData.tableIndexStatistic) {
					continue;
				}
				addIndex(rs);
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

	private void initColumns(DatabaseMetaData meta) throws SQLException {
		ResultSet rs = null;
		try {
			rs = meta.getColumns(catalog, schema, name, "%");
			while (rs.next()) {
				addColumn(rs);
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}
}
