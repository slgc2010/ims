package org.yl.ims.util.dbUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.yl.ims.util.dbUtil.dialect.Dialect;
import org.yl.ims.util.dbUtil.mapping.ColumnMapping;
import org.yl.ims.util.dbUtil.mapping.PrimaryKey;
import org.yl.ims.util.dbUtil.mapping.Table;
import org.yl.ims.util.dbUtil.mapping.TableBuilder;
import org.yl.ims.util.dbUtil.mapping.entity.GeneratedSQL;
import org.yl.ims.util.dbUtil.mapping.entity.PropertyHelper;
import org.yl.ims.util.dbUtil.mapping.metadata.TableMetaDataBuilder;
import org.yl.ims.util.dbUtil.mapping.metadata.TableMetadata;
import org.yl.ims.util.dbUtil.statement.NameParamStatementGenerator;

public class DBManager {
	private Connection conn;
	private Dialect dialect;

	public static DBManager getDBManager(Connection conn) throws SQLException,
			IOException {
		return new DBManager(conn);
	}

	public DBManager(Connection conn) throws SQLException, IOException {
		this.conn = conn;
		this.dialect = Dialect.getDialect(conn);
	}

	public int executeUpdate(String sql, Object param) throws SQLException {
		synchronized (conn) {
			PreparedStatement ps = null;
			int result = 0;
			try {
				NameParamStatementGenerator generator = new NameParamStatementGenerator(
						sql);
				generator.createStatement(conn);
				generator.setParams(param);
				ps = generator.getPreparedStatement();
				result = ps.executeUpdate();
				if (!conn.getAutoCommit())
					conn.commit();
			} catch (SQLException e) {
				throw e;
			} finally {
				closeStateMent(ps);
			}
			return result;
		}
	}

	public int[] executeUpdate(String sql, List<?> params) throws SQLException {
		synchronized (conn) {
			int[] result = null;
			PreparedStatement ps = null;
			try {
				conn.setAutoCommit(false);
				NameParamStatementGenerator generator = new NameParamStatementGenerator(
						sql);
				generator.createStatement(conn);
				for (Object param : params) {
					generator.addBatch(param);
				}
				ps = generator.getPreparedStatement();
				result = ps.executeBatch();
				conn.commit();
			} catch (SQLException e) {
				conn.rollback();
				throw e;
			} finally {
				closeStateMent(ps);
				conn.setAutoCommit(true);
			}
			return result;
		}
	}

	public int executeUpdate(String sql) throws SQLException {
		synchronized (conn) {
			PreparedStatement ps = null;
			int result = 0;
			try {
				ps = conn.prepareStatement(sql);
				result = ps.executeUpdate();
				if (!conn.getAutoCommit())
					conn.commit();
			} catch (SQLException e) {
				throw e;
			} finally {
				closeStateMent(ps);
			}
			return result;
		}
	}

	public List<String> getTableNames() throws SQLException {
		List<String> r = new ArrayList<String>();
		DatabaseMetaData metaData = conn.getMetaData();
		ResultSet rs = metaData.getTables(null, "public", null,
				new String[] { "TABLE" });
		while (rs.next()) {
			String tableName = rs.getString("TABLE_NAME");
			r.add(tableName);
		}
		return r;
	}

	public List<TableMetadata> getTables() throws SQLException {
		List<TableMetadata> r = new ArrayList<TableMetadata>();
		DatabaseMetaData metaData = conn.getMetaData();
		TableMetaDataBuilder builder = new TableMetaDataBuilder(metaData);
		List<String> tableNames = getTableNames();
		for (String tablename : tableNames) {
			TableMetadata table = builder.getTableMetadata(dialect, tablename);
			r.add(table);
		}
		return r;
	}

	public TableMetadata getTable(String name) throws SQLException {
		DatabaseMetaData metaData = conn.getMetaData();
		TableMetaDataBuilder builder = new TableMetaDataBuilder(metaData);
		return builder.getTableMetadata(dialect, name);
	}

	public void close() {
		closeConnection(conn);
	}

	private static void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
			}
		}
	}

	public int[] executeUpdate(List<String> sqls) throws SQLException {
		synchronized (conn) {
			Statement st = null;
			int result[] = null;
			try {
				conn.setAutoCommit(false);
				st = conn.createStatement();
				for (String sql : sqls) {
					st.addBatch(sql);
				}
				result = st.executeBatch();
				conn.commit();
			} catch (SQLException e) {
				conn.rollback();
				throw e;
			} finally {
				conn.setAutoCommit(true);
				closeStateMent(st);
			}
			return result;
		}
	}

	public <T> List<T> queryForList(String sql, Class<T> resultType)
			throws SQLException {
		synchronized (conn) {
			List<T> result = new ArrayList<T>();
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				PropertyHelper helper = new PropertyHelper(resultType);
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				List<String> labels = getColumnLabels(rs);
				while (rs.next()) {
					T row;
					row = resultType.newInstance();
					for (String label : labels) {
						helper.setValue(row, label, rs.getObject(label));
					}
					result.add(row);
				}
			} catch (SQLException e) {
				throw e;
			} catch (InstantiationException e) {
				throw new SQLException(e);
			} catch (IllegalAccessException e) {
				throw new SQLException(e);
			} finally {
				closeStateMent(ps, rs);
			}
			return result;
		}
	}

	public <T> List<T> queryForList(String sql, Object param,
			Class<T> resultType) throws SQLException {
		synchronized (conn) {
			List<T> result = new ArrayList<T>();
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				NameParamStatementGenerator generator = new NameParamStatementGenerator(
						sql);
				generator.createStatement(conn);
				generator.setParams(param);
				ps = generator.getPreparedStatement();
				PropertyHelper helper = new PropertyHelper(resultType);
				rs = ps.executeQuery();
				List<String> labels = getColumnLabels(rs);
				while (rs.next()) {
					T row;
					row = resultType.newInstance();
					for (String label : labels) {
						helper.setValue(row, label, rs.getObject(label));
					}
					result.add(row);
				}
			} catch (SQLException e) {
				throw e;
			} catch (InstantiationException e) {
				throw new SQLException(e);
			} catch (IllegalAccessException e) {
				throw new SQLException(e);
			} finally {
				closeStateMent(ps, rs);
			}
			return result;
		}
	}

	public List<Map<String, Object>> queryForList(String sql)
			throws SQLException {
		synchronized (conn) {
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				List<String> labels = getColumnLabels(rs);
				while (rs.next()) {
					Map<String, Object> row = new HashMap<String, Object>();
					for (String label : labels) {
						row.put(label.toLowerCase(), rs.getObject(label));
					}
					result.add(row);
				}
			} catch (SQLException e) {
				throw e;
			} finally {
				closeStateMent(ps, rs);
			}
			return result;
		}
	}

	private static void closeStateMent(Statement st) {
		closeStateMent(st, (ResultSet) null);
	}

	private static void closeStateMent(Statement st, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
			}
		}
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
			}
		}
	}

	private static List<String> getColumnLabels(ResultSet rs)
			throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();
		int count = meta.getColumnCount();
		List<String> labels = new ArrayList<String>();
		for (int i = 1; i <= count; i++) {
			labels.add(meta.getColumnLabel(i));
		}
		return labels;
	}

	public Map<String, Object> queryForOne(String sql) throws SQLException {
		synchronized (conn) {
			PreparedStatement ps = null;
			ResultSet rs = null;
			Map<String, Object> result = new HashMap<String, Object>();
			try {
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				List<String> labels = getColumnLabels(rs);
				if (rs.next()) {
					for (String label : labels) {
						result.put(label.toLowerCase(), rs.getObject(label));
					}
				}
			} catch (SQLException e) {
				throw e;
			} finally {
				closeStateMent(ps, rs);
			}
			return result;
		}
	}

	public int queryForInt(String sql, Object param) throws SQLException {
		synchronized (conn) {
			PreparedStatement ps = null;
			ResultSet rs = null;
			int result = 0;
			try {
				NameParamStatementGenerator generator = new NameParamStatementGenerator(
						sql);
				generator.createStatement(conn);
				generator.setParams(param);
				ps = generator.getPreparedStatement();
				rs = ps.executeQuery();
				if (rs.next()) {
					result = rs.getInt(1);
				}
			} catch (SQLException e) {
				throw e;
			} finally {
				closeStateMent(ps, rs);
			}
			return result;
		}
	}

	public int queryForInt(String sql) throws SQLException {
		synchronized (conn) {
			PreparedStatement ps = null;
			ResultSet rs = null;
			int result = 0;
			try {
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				if (rs.next()) {
					result = rs.getInt(1);
				}
			} catch (SQLException e) {
				throw e;
			} finally {
				closeStateMent(ps, rs);
			}
			return result;
		}
	}

	public String queryForString(String sql) throws SQLException {
		synchronized (conn) {
			PreparedStatement ps = null;
			ResultSet rs = null;
			String result = null;
			try {
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				if (rs.next()) {
					result = rs.getString(1);
				}
			} catch (SQLException e) {
				throw e;
			} finally {
				closeStateMent(ps, rs);
			}
			return result;
		}
	}

	public void registerEntity(Class<?> clazz) {
		registerEntity(clazz, conn);
	}

	public void registerEntity(List<Class<?>> list) {
		registerEntity(list, conn);
	}

	public void registerEntity(Class<?> clazz, Connection conn) {
		List<Class<?>> list = new ArrayList<Class<?>>();
		list.add(clazz);
		registerEntity(list, conn);
	}

	public void registerEntity(List<Class<?>> list, Connection conn) {
		TableBuilder builder = new TableBuilder(list, dialect);
		try {
			synchronized (conn) {
				builder.schemaUpdate(conn);
			}
			printSQL(builder);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private Map<Class<?>, GeneratedSQL> SQL = new HashMap<Class<?>, GeneratedSQL>();

	public GeneratedSQL getSQL(Class<?> clazz) {
		return SQL.get(clazz);
	}

	private void printSQL(TableBuilder builder) throws SQLException {
		List<Table> tables = builder.getTables();
		for (Table table : tables) {
			GeneratedSQL sql = new GeneratedSQL();
			// insert
			StringBuffer insert = new StringBuffer();
			insert.append("insert into ").append(table.getName());
			insert.append(" (");
			StringBuffer insertVals = new StringBuffer();
			// update
			StringBuffer update = new StringBuffer();
			update.append("update ").append(table.getName()).append(" set ");
			Map<String, ColumnMapping> columns = table.getColumns();
			for (Entry<String, ColumnMapping> en : columns.entrySet()) {
				// insert
				insert.append(en.getValue().getName()).append(",");
				insertVals.append(":").append(en.getValue().getProperty())
						.append(",");
				// update
				update.append(en.getValue().getName()).append("=");
				update.append(":").append(en.getValue().getProperty())
						.append(",");
			}
			// insert
			insert.setLength(insert.length() - 1);
			insertVals.setLength(insertVals.length() - 1);
			insert.append(") values (").append(insertVals).append(")");
			// update
			update.setLength(update.length() - 1);
			update.append(" where ");
			// delete
			StringBuffer delete = new StringBuffer();
			delete.append("delete from ").append(table.getName());
			delete.append(" where ");
			PrimaryKey primarykey = table.getPrimarykey();
			for (String col : primarykey.getColumns()) {
				// update
				update.append(col).append("=");
				update.append(":").append(columns.get(col).getProperty())
						.append(" AND ");
				// delete
				delete.append(col).append("=");
				delete.append(":").append(columns.get(col).getProperty())
						.append(" AND ");
			}

			if (primarykey.getColumns().size() == 0) {
				throw new SQLException(
						String.format("%s must have an identity field",
								table.getTableClass()));
			}
			update.setLength(update.length() - 4);
			delete.setLength(delete.length() - 4);
			System.out.println(insert);
			System.out.println(update);
			System.out.println(delete);
			sql.setInsert(insert.toString());
			sql.setUpdate(update.toString());
			sql.setDelete(delete.toString());
			SQL.put(table.getTableClass(), sql);
		}
	}
}
