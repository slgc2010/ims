package org.yl.ims.util.dbUtil.mapping.metadata;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IndexMetadata {
	private final String name;
	private final List<ColumnMetadata> columns = new ArrayList<ColumnMetadata>();

	IndexMetadata(ResultSet rs) throws SQLException {
		name = rs.getString("INDEX_NAME");
	}

	public String getName() {
		return name;
	}

	public void addColumn(ColumnMetadata column) {
		if (column != null)
			columns.add(column);
	}

	public ColumnMetadata[] getColumns() {
		return (ColumnMetadata[]) columns.toArray(new ColumnMetadata[0]);
	}

	public String toString() {
		return "IndexMatadata(" + name + ')';
	}
}
