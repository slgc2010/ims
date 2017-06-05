package org.yl.ims.util.dbUtil.mapping.metadata;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrimaryKeyMetadata {
	private String name; // String => 主键的名称（可为 null）
	private String schema; // String => 表模式（可为 null）
	private List<String> columns = new ArrayList<String>(); // String => 列名称
	private short key_seq;// short => 主键中的序列号（值 1 表示主键中的第一列，值 2 表示主键中的第二列）。

	public PrimaryKeyMetadata(ResultSet rs) throws SQLException {
		if (rs.next()) {
			name = rs.getString("PK_NAME");
			schema = rs.getString("TABLE_SCHEM");
			key_seq = rs.getShort("KEY_SEQ");
			do {
				String column = rs.getString("COLUMN_NAME");
				columns.add(column);
			} while (rs.next());
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	public short getKey_seq() {
		return key_seq;
	}

	public void setKey_seq(short key_seq) {
		this.key_seq = key_seq;
	}
}
