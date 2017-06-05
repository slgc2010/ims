package org.yl.ims.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.yl.ims.model.db.MessageInfo;
import org.yl.ims.service.MsgService;
import org.yl.ims.util.Page;

@Service
public class MsgServiceImpl extends BaseService<MessageInfo> implements MsgService {
	private BeanPropertyRowMapper<MessageInfo> messageInfoRowMapper = new BeanPropertyRowMapper<MessageInfo>(
			MessageInfo.class);

	@Override
	public Page<MessageInfo> getMsgData(int pageNumber, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MessageInfo getMessageInfoById(Integer blogId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer delMessageInfoById(Integer blogId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer saveOrUpdateMessageInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MessageInfo getMessageInfoByStatus(Integer status) {
		String sql = "select * from messageinfo where status = :status";
		Map<String, Integer> p = new HashMap<String, Integer>();
		p.put("status", status);
		List<MessageInfo> msgList = template.query(sql, p, messageInfoRowMapper);

		return msgList.size() > 0 ? msgList.get(0) : null;
	}

}
