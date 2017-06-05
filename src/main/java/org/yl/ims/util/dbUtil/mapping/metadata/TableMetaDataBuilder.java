package org.yl.ims.util.dbUtil.mapping.metadata;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.yl.ims.util.dbUtil.dialect.Dialect;


public class TableMetaDataBuilder {
	private DatabaseMetaData meta;
	private String[] types = new String[] { "TABLE", "VIEW", "SYNONYM" };

	public TableMetaDataBuilder(DatabaseMetaData metaData) {
		this.meta = metaData;
	}

	public TableMetadata getTableMetadata(Dialect dialect, String tablename) throws SQLException {
		return getTableMetadata(tablename, dialect.getSchema(), dialect.getCatalog(), dialect.isTableNameQuoted());
	}

	public TableMetadata getTableMetadata(String name, String schema, String catalog, boolean isQuoted)
			throws SQLException {
		TableMetadata table;
		ResultSet rs = null;
		try {
			if ((isQuoted && meta.storesMixedCaseQuotedIdentifiers())) {
				rs = meta.getTables(catalog, schema, name, types);
			} else if ((isQuoted && meta.storesUpperCaseQuotedIdentifiers())
					|| (!isQuoted && meta.storesUpperCaseIdentifiers())) {
				rs = meta.getTables(StringHelper.toUpperCase(catalog), StringHelper.toUpperCase(schema),
						StringHelper.toUpperCase(name), types);
			} else if ((isQuoted && meta.storesLowerCaseQuotedIdentifiers())
					|| (!isQuoted && meta.storesLowerCaseIdentifiers())) {
				rs = meta.getTables(StringHelper.toLowerCase(catalog), StringHelper.toLowerCase(schema),
						StringHelper.toLowerCase(name), types);
			} else {
				rs = meta.getTables(catalog, schema, name, types);
			}
			while (rs.next()) {
				String tableName = rs.getString("TABLE_NAME");
				if (name.equalsIgnoreCase(tableName)) {
					table = new TableMetadata(rs, meta, false);
					return table;
				}
			}
			return null;
		} finally {
			rs.close();
		}
	}
}
