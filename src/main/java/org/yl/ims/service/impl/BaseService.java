package org.yl.ims.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.yl.ims.util.Page;
import org.yl.ims.util.dbUtil.ObjectedJDBCTemplate;
import org.yl.ims.util.dbUtil.PagingSqlBuilder;

public class BaseService<T> {
	@Autowired
	protected ObjectedJDBCTemplate template;

	@Value("#{config['dataBase.type']}")
	protected String dataBaseType;

	/**
	 * 自动构建分页查询语句和查询总数语句
	 * 
	 * @param rawSql
	 * @param pageSize
	 * @param pageNumber
	 * @param row
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public Page<T> paging(String rawSql, int pageSize, int pageNumber,
			RowMapper<T> row) {
		String countSql = PagingSqlBuilder.getCountSql(rawSql);
		String pagingSql = PagingSqlBuilder.getPagingSql(dataBaseType, rawSql,
				pageSize, pageNumber);
		List<T> blogList = template.query(pagingSql, row);
		int records = template.queryForInt(countSql,
				new HashMap<String, String>());
		Page<T> result = new Page<T>(blogList, pageNumber, pageSize, records);
		return result;
	}

}
