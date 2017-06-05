package org.yl.ims.service;

import java.util.List;

import org.yl.ims.model.db.Say;
import org.yl.ims.util.Page;

public interface SayService {
	public Page<Say> getMsgData(int pageNumber, int pageSize);

	public Say getSayById(Integer sayId);

	public Integer delSayById(Integer sayId);

	public Integer saveOrUpdateSay(List<Say> ls);

	public Integer saveOrUpdateSay(Say say);
}
