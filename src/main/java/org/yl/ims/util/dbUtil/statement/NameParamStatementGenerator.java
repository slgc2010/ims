package org.yl.ims.util.dbUtil.statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.yl.ims.util.dbUtil.mapping.entity.PropertyHelper;

public class NameParamStatementGenerator {
	// insert into table(col1,col2,col2)
	// values(#property1#,#property2#,#property3#)
	private static Pattern pattern = Pattern.compile("\\#.+?\\#");
	private List<String> properties = new ArrayList<String>();
	private String treatedsql;
	private PreparedStatement ps;
	private static Set<Class<?>> singleValues = new HashSet<Class<?>>();
	static {
		singleValues.add(String.class);
		singleValues.add(int.class);
		singleValues.add(Integer.class);
		singleValues.add(long.class);
		singleValues.add(Long.class);
	}

	public NameParamStatementGenerator(String sql) {
		Matcher matcher = pattern.matcher(sql);
		StringBuffer buf = new StringBuffer();
		int start = 0;
		while (matcher.find()) {
			String g = matcher.group();
			properties.add(g.substring(1, g.length() - 1));
			int s = matcher.start();
			buf.append(sql.substring(start, s)).append("?");
			start = matcher.end();
		}
		buf.append(sql.substring(start, sql.length()));
		this.treatedsql = buf.toString();
	}

	public String getTreatedSql() {
		return treatedsql;
	}

	public void setPreparedStatement(PreparedStatement ps) {
		this.ps = ps;
	}

	public void createStatement(Connection conn) throws SQLException {
		this.ps = conn.prepareStatement(treatedsql);
	}

	public PreparedStatement getPreparedStatement() {
		return ps;
	}

	@SuppressWarnings("rawtypes")
	public void setParams(Object v) throws SQLException {
		if (ps == null) {
			throw new UnsupportedOperationException("PreparedStatement not set");
		}
		if (singleValues.contains(v.getClass()) && properties.size() == 1) {
			this.ps.setObject(1, v);
			return;
		}
		if (v instanceof Map) {
			Map m = (Map) v;
			for (int i = 0; i < properties.size(); i++) {
				String property = properties.get(i);
				this.ps.setObject(i + 1, getValue(m, property));
			}
		} else {
			PropertyHelper helper = new PropertyHelper(v.getClass());
			for (int i = 0; i < properties.size(); i++) {
				String property = properties.get(i);
				this.ps.setObject(i + 1, helper.getValue(v, property));
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private Object getValue(Map m, String property) {
		Object value = m.get(property);
		if (value == null) {
			for (Object k : m.keySet()) {
				String key = (k == null ? "" : k.toString());
				if (key.toLowerCase().equals(property.toLowerCase())) {
					value = m.get(k);
					break;
				}
			}
		}
		return value;
	}

	public void addBatch(Object v) throws SQLException {
		setParams(v);
		this.ps.addBatch();
	}

	public static void main(String[] args) {
		NameParamStatementGenerator g = new NameParamStatementGenerator(
				"insert into table(col1,col2,col2) values (#property1#,#property2#,#property3#) not end");
		System.out.println(g.getTreatedSql());
		System.out.println(Arrays.toString(g.properties.toArray()));
		System.out.println(Math.ceil(0.1));
	}
}
