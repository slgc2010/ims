package org.yl.ims.service.impl;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.yl.ims.model.db.Say;
import org.yl.ims.service.SayService;
import org.yl.ims.util.Page;
import org.yl.ims.util.dbUtil.mapping.entity.GeneratedSQL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SayServiceImpl extends BaseService<Say> implements SayService {
	private BeanPropertyRowMapper<Say> sayRowMapper = new BeanPropertyRowMapper<Say>(Say.class);

	@Override
	public Page<Say> getMsgData(int pageNumber, int pageSize) {
		String rawSql = "select * from shuoshuo order by createtime desc ";
		return this.paging(rawSql, pageSize, pageNumber, sayRowMapper);
	}

	@Override
	public Say getSayById(Integer sayId) {

		String rawSql = "select * from shuoshuo where id = :id";
		Map<String, Integer> p = new HashMap<String, Integer>();
		p.put("id", sayId);
		List<Say> query = this.template.query(rawSql, p, sayRowMapper);
		return (query.isEmpty() && query.size() > 0) ? query.get(0) : null;
	}

	@Override
	public Integer delSayById(Integer sayId) {
		GeneratedSQL sqlByObject = this.template.getSQLByObject(Say.class);
		Map<String, Integer> p = new HashMap<String, Integer>();
		p.put("id", sayId);
		return this.template.update(sqlByObject.getDelete(), p);
	}

	@Override
	public Integer saveOrUpdateSay(List<Say> ls) {

		return null;
	}

	@Override
	public Integer saveOrUpdateSay(Say ls) {

		return null;
	}

}
