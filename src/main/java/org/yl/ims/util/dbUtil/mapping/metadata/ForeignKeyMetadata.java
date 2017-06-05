package org.yl.ims.util.dbUtil.mapping.metadata;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ForeignKeyMetadata {
	private final String name;
	private final String refTable;
	private final Map<String, String> references = new HashMap<String, String>();

	ForeignKeyMetadata(ResultSet rs) throws SQLException {
		name = rs.getString("FK_NAME");
		refTable = rs.getString("PKTABLE_NAME");
	}

	public String getName() {
		return name;
	}

	public String getReferencedTableName() {
		return refTable;
	}

	void addReference(ResultSet rs) throws SQLException {
		references.put(StringHelper.toLowerCase(rs.getString("FKCOLUMN_NAME")), rs.getString("PKCOLUMN_NAME"));
	}

	// private boolean hasReference(Column column, Column ref) {
	// String refName = (String)
	// references.get(StringHelper.toLowerCase(column.getName()));
	// return ref.getName().equalsIgnoreCase(refName);
	// }
	public String toString() {
		return "ForeignKeyMetadata(" + name + ')';
	}
}
