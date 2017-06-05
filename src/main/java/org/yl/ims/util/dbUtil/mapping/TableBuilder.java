package org.yl.ims.util.dbUtil.mapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;
import org.yl.ims.util.dbUtil.annotation.Ignore;
import org.yl.ims.util.dbUtil.annotation.MappedEntity;
import org.yl.ims.util.dbUtil.dialect.Dialect;
import org.yl.ims.util.dbUtil.mapping.builder.AnnotationColumnBuilder;
import org.yl.ims.util.dbUtil.mapping.builder.AnnotationTableBuilder;
import org.yl.ims.util.dbUtil.mapping.builder.impl.AnnotationUtils;
import org.yl.ims.util.dbUtil.mapping.builder.impl.CommentColumnBuilder;
import org.yl.ims.util.dbUtil.mapping.builder.impl.DefaultJPABuilder;
import org.yl.ims.util.dbUtil.mapping.builder.impl.DefaultJPATableBuilder;
import org.yl.ims.util.dbUtil.mapping.metadata.ColumnMetadata;
import org.yl.ims.util.dbUtil.mapping.metadata.TableMetaDataBuilder;
import org.yl.ims.util.dbUtil.mapping.metadata.TableMetadata;

public class TableBuilder {
	private List<Class<?>> mappedClasses;
	private List<Table> tables;
	private Dialect dialect;
	private AnnotationColumnBuilder[] columnBuilders = new AnnotationColumnBuilder[] { new DefaultJPABuilder(),
			new CommentColumnBuilder() };
	private AnnotationTableBuilder[] tableBuilders = new AnnotationTableBuilder[] { new DefaultJPATableBuilder() };

	public TableBuilder(List<Class<?>> mappedClasses, Dialect dialect) {
		this.mappedClasses = mappedClasses;
		this.dialect = dialect;
	}

	public List<Table> getTables() {
		if (tables == null) {
			parseTables();
		}
		return tables;
	}

	public Map<Class<?>, Table> getClassTableMap() {
		if (tables == null) {
			parseTables();
		}
		Map<Class<?>, Table> map = new HashMap<Class<?>, Table>();
		for (Table tab : tables) {
			map.put(tab.getTableClass(), tab);
		}
		return map;
	}

	public void schemaUpdate(Connection conn) throws SQLException {
		List<Table> tablestoupdate = this.getTables();
		List<String> scripts = new ArrayList<String>();
		TableMetaDataBuilder metabuilder = new TableMetaDataBuilder(conn.getMetaData());
		for (Table table : tablestoupdate) {
			TableMetadata tableinfo = metabuilder.getTableMetadata(dialect, table.getName());
			if (tableinfo == null) {
				StringBuffer createSql = new StringBuffer();
				createSql.append(dialect.getCreateTableString()).append(" ");
				createSql.append(dialect.fullTableName(dialect.quoteTableName(table.getName())));
				createSql.append("(");
				for (ColumnMapping column : table.getColumns().values()) {
					createSql.append(dialect.columnString(column)).append(",");
				}
				createSql.setLength(createSql.length() - 1);
				String pk = dialect.primaryKeyString(table.getPrimarykey());
				if (StringUtils.hasText(pk)) {
					createSql.append(",").append(pk);
				}
				createSql.append(")");
				scripts.add(createSql.toString());
			} else {
				for (ColumnMapping column : table.getColumns().values()) {
					ColumnMetadata columnmeta = tableinfo.getColumnMetadata(column.getName());
					if (columnmeta == null) {
						StringBuffer altersql = new StringBuffer();
						altersql.append(dialect.getAlterTableString()).append(dialect.quoteTableName(table.getName()))
								.append(" add ");
						altersql.append(dialect.columnString(column));
						scripts.add(altersql.toString());
					} else {
						if (dialect.lengthAvailble(column.getJdbcType()) && column.getLength() != 0
								&& columnmeta.getColumnSize() != 0 && column.getLength() > columnmeta.getColumnSize()) {
							StringBuffer altersql = new StringBuffer();
							altersql.append(dialect.getAlterTableString())
									.append(dialect.quoteTableName(table.getName())).append(" modify ");
							altersql.append(dialect.columnString(column));
							scripts.add(altersql.toString());
						}
					}
				}
			}
		}
		conn.setAutoCommit(false);
		Statement stmt = conn.createStatement();
		try {
			for (String sql : scripts) {
				System.out.println(sql);
				stmt.execute(sql);
			}
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			stmt.close();
		}
	}

	private void parseTables() {
		tables = new ArrayList<Table>();
		for (Class<?> clazz : mappedClasses) {
			Table table = new Table();
			MappedEntity mapped = AnnotationUtils.getAnnotation(clazz, MappedEntity.class);
			table.setTableClass(clazz);
			table.setSchemaupdate(mapped.schemaUpdate());
			table.setName(StringUtils.hasText(mapped.value()) ? mapped.value() : clazz.getSimpleName());
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				boolean notStatic = true;
				int mod = field.getModifiers();
				notStatic = ((mod | Modifier.STATIC) != mod);
				if (!hasAnnotation(field.getAnnotations(), Ignore.class) && notStatic) {
					table.addColumn(getColumn(field));
					processTable(table, clazz, field);
				}
			}
			tables.add(table);
		}
	}

	private void processTable(Table table, Class<?> clazz, Field field) {
		for (AnnotationTableBuilder builder : tableBuilders) {
			builder.parse(table, clazz, field);
		}
	}

	private ColumnMapping getColumn(Field field) {
		ColumnMapping column = new ColumnMapping();
		column.setProperty(field.getName());
		column.setName(field.getName());
		column.setJavaType(field.getType());
		column.setJdbcType(dialect.getDefaultJdbcType(column.getJavaType()));
		column.setLength(dialect.getDefaultLength(column.getJavaType()));
		for (AnnotationColumnBuilder builder : columnBuilders) {
			builder.parse(column, field);
		}
		return column;
	}

	private boolean hasAnnotation(Annotation[] annos, Class<? extends Annotation> target) {
		for (Annotation annotation : annos) {
			if (annotation.annotationType().equals(target)) {
				return true;
			}
		}
		return false;
	}
}
