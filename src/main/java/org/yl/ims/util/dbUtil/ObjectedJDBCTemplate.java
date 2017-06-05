package org.yl.ims.util.dbUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.util.ResourceUtils;
import org.yl.ims.util.dbUtil.annotation.MappedEntity;
import org.yl.ims.util.dbUtil.dialect.Dialect;
import org.yl.ims.util.dbUtil.mapping.ColumnMapping;
import org.yl.ims.util.dbUtil.mapping.PrimaryKey;
import org.yl.ims.util.dbUtil.mapping.Table;
import org.yl.ims.util.dbUtil.mapping.TableBuilder;
import org.yl.ims.util.dbUtil.mapping.entity.GeneratedSQL;

public class ObjectedJDBCTemplate extends NamedParameterJdbcTemplate implements InitializingBean {
	private String basePath;
	private DataSource dataSource;
	private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

	public ObjectedJDBCTemplate(DataSource dataSource) {
		super(dataSource);
		this.dataSource = dataSource;
	}

	private void buildTable() throws ClassNotFoundException, IOException, SQLException {
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		MetadataReaderFactory metadataReaderFactory = new SimpleMetadataReaderFactory(resourcePatternResolver);
		String packageSearchPath = ResourceUtils.CLASSPATH_URL_PREFIX + basePath + DEFAULT_RESOURCE_PATTERN;
		Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
		List<Class<?>> mappers = new ArrayList<Class<?>>();
		for (Resource resource : resources) {
			if (resource.isReadable()) {
				MetadataReader reader = metadataReaderFactory.getMetadataReader(resource);
				ClassMetadata classMetadata = reader.getClassMetadata();
				String className = classMetadata.getClassName();
				Class<?> clazz = Class.forName(className);
				MappedEntity m = (MappedEntity) clazz.getAnnotation(MappedEntity.class);
				if (null != m) {
					mappers.add(clazz);
				}
			}
		}
		Connection connection = null;
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			registerEntity(mappers, connection);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DataSourceUtils.releaseConnection(connection, dataSource);
		}
	}

	public void registerEntity(List<Class<?>> list, Connection conn) throws SQLException {
		Dialect dialect = Dialect.getDialect(conn);
		TableBuilder builder = new TableBuilder(list, dialect);
		try {
			synchronized (conn) {
				builder.schemaUpdate(conn);
			}
			buildSQL(builder);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void buildSQL(TableBuilder builder) throws SQLException {
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
				insertVals.append(":").append(en.getValue().getProperty()).append(",");
				// update
				update.append(en.getValue().getName()).append("=");
				update.append(":").append(en.getValue().getProperty()).append(",");
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
				update.append(":").append(columns.get(col).getProperty()).append(" AND ");
				// delete
				delete.append(col).append("=");
				delete.append(":").append(columns.get(col).getProperty()).append(" AND ");
			}

			if (primarykey.getColumns().size() == 0) {
				throw new SQLException(String.format("%s must have an identity field", table.getTableClass()));
			}
			update.setLength(update.length() - 4);
			delete.setLength(delete.length() - 4);
			sql.setInsert(insert.toString());
			sql.setUpdate(update.toString());
			sql.setDelete(delete.toString());
			sql.setDrop("drop table " + table.getName());
			SQL.put(table.getTableClass(), sql);
		}
	}

	public void save(Object param) {
		GeneratedSQL generatedSQL = getSQLByObject(param);
		String insert = generatedSQL.getInsert();
		this.update(insert, new BeanPropertySqlParameterSource(param));
	}

	public void updateById(Object param) {
		GeneratedSQL generatedSQL = getSQLByObject(param);
		String update = generatedSQL.getUpdate();
		this.update(update, new BeanPropertySqlParameterSource(param));
	}

	public void deleteById(Object param) {
		GeneratedSQL generatedSQL = getSQLByObject(param);
		String delete = generatedSQL.getDelete();
		this.update(delete, new BeanPropertySqlParameterSource(param));
	}

	private GeneratedSQL getSQLByObject(Object param) {
		if (param == null) {
			throw new IllegalArgumentException("object can not be null");
		}
		Class<?> clazz = param.getClass();
		GeneratedSQL generatedSQL = SQL.get(clazz);
		if (generatedSQL == null) {
			throw new IllegalArgumentException(String.format("class %s is not in mapping", clazz.getName()));
		}
		return generatedSQL;
	}

	public GeneratedSQL getSQLByObject(Class<?> clazz) {
		GeneratedSQL generatedSQL = SQL.get(clazz);
		if (generatedSQL == null) {
			throw new IllegalArgumentException(String.format("class %s is not in mapping", clazz.getName()));
		}
		return generatedSQL;
	}

	public void dropAll() {
		Collection<GeneratedSQL> values = SQL.values();
		for (GeneratedSQL s : values) {
			this.update(s.getDrop(), new HashMap<String, Object>());
		}
	}

	private Map<Class<?>, GeneratedSQL> SQL = new HashMap<Class<?>, GeneratedSQL>();

	// public static void main(String[] args) throws Exception {
	// Initializer ini = new Initializer();
	// Class.forName("oracle.jdbc.OracleDriver");
	// ini.setDataSource(new SingleConnectionDataSource(
	// "jdbc:oracle:thin:@192.168.1.120:1521:dbc", "andromeda",
	// "pass$123", false));
	// ini.setBasePath("com/edu/dk/sns/model/db/");
	// ini.afterPropertiesSet();
	// }

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		buildTable();
	}
}
