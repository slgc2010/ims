package org.yl.ims.service;

import org.yl.ims.model.db.MessageInfo;
import org.yl.ims.util.Page;

public interface MsgService {
	public Page<MessageInfo> getMsgData(int pageNumber, int pageSize);

	public MessageInfo getMessageInfoById(Integer blogId);

	public Integer delMessageInfoById(Integer blogId);

	public Integer saveOrUpdateMessageInfo();

	public MessageInfo getMessageInfoByStatus(Integer status);
}
