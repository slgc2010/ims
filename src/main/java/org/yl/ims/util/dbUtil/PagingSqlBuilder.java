package org.yl.ims.util.dbUtil;

import org.apache.log4j.Logger;
import org.yl.ims.util.dbUtil.exception.ErrorCode;
import org.yl.ims.util.dbUtil.exception.PagingSqlBuilderException;

public class PagingSqlBuilder {

	private static final Logger log = Logger.getLogger(PagingSqlBuilder.class);

	private static final String ORACLE = "oracle";
	private static final String MYSQL = "mysql";
	private static final String SQLSERVER = "sqlserver";

	/**
	 * 获得计算总记录数的SQL语句
	 * 
	 * @param rawSql
	 * @return 创建日期：2012-9-26 修改说明：
	 * @author wangk
	 */
	public static String getCountSql(String rawSql) {
		String countSql = "SELECT COUNT(*) AS RECORDS " + rawSql.substring(rawSql.toUpperCase().indexOf("FROM"));
		int orderIndex = countSql.toUpperCase().lastIndexOf("ORDER");
		if (orderIndex >= 0) {
			countSql = countSql.substring(0, orderIndex).trim();
		}
		log.debug(countSql);
		return countSql;
	}

	/**
	 * 获得计算分组总记录数的SQL语句
	 * 
	 * @param rawSql
	 * @return 创建日期：2012-10-25 修改说明：
	 * @author wangk
	 */
	public static String getGroupCountSql(String rawSql) {
		String groupCountSql = "SELECT SUM(RECORDS) FROM (" + getCountSql(rawSql) + ") AS T";
		log.debug(groupCountSql);
		return groupCountSql;
	}

	/**
	 * 获得分页SQL语句
	 * 
	 * @param rawSql
	 * @param paging
	 * @return 创建日期：2012-9-26 修改说明：
	 * @author wangk
	 */
	public static String getPagingSql(String dataBaseType, String rawSql, int pageSize, int startPage) {
		int rows = pageSize * startPage;
		int start = pageSize * startPage - pageSize;
		int end = start + rows;
		if (dataBaseType.toLowerCase().indexOf(ORACLE) >= 0) {
			String pagingSql = "SELECT T.*, ROWNUM AS ROW_NUM FROM (" + rawSql + ") AS T WHERE ROWNUM < " + end;
			if (start == 0) {
				log.debug(pagingSql);
				return pagingSql;
			}
			pagingSql = "SELECT * FROM (" + pagingSql + ") AS T_O WHERE ROW_NUM >= " + start;
			log.debug(pagingSql);
			return pagingSql;
		}
		if (dataBaseType.toLowerCase().indexOf(MYSQL) >= 0) {
			String pagingSql = rawSql + " LIMIT " + start + ", " + pageSize;
			log.debug(pagingSql);
			return pagingSql;
		}
		if (dataBaseType.toLowerCase().indexOf(SQLSERVER) >= 0) {
			String pagingSql = "SELECT TOP " + end + rawSql.trim().substring(6);
			if (start == 0) {
				log.debug(pagingSql);
				return pagingSql;
			}
			String orders = rawSql.substring(rawSql.toUpperCase().lastIndexOf("ORDER"));
			int subIndex = rawSql.toUpperCase().indexOf("FROM") - 1;
			pagingSql = "SELECT * FROM (" + rawSql.substring(0, subIndex) + ", ROW_NUMBER() OVER(" + orders
					+ ") AS ROW_NUM " + rawSql.substring(subIndex, rawSql.toUpperCase().lastIndexOf("ORDER")).trim()
					+ ") AS T WHERE ROW_NUM >= " + start + " AND ROW_NUM < " + end;
			log.debug(pagingSql);
			return pagingSql;
		}
		throw new PagingSqlBuilderException(ErrorCode.PAGING_SQL_BUILDER_AS_UNKNOWN_DB_TYPE, "不支持的数据库");
	}

	public static void main(String[] args) {
		String sql = "select * from blog order by createtime desc";
		System.out.println(getCountSql(sql));
		System.out.println(getPagingSql("oracle", sql, 10, 1));
	}
}
