package org.yl.ims.model.db;

import java.io.Serializable;

import org.yl.ims.util.dbUtil.annotation.Id;
import org.yl.ims.util.dbUtil.annotation.MappedEntity;

@MappedEntity("messageinfo")
public class MessageInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private Integer id;
	private String messageInfo;
	private Integer status;

	public String getMessageInfo() {
		return messageInfo;
	}

	public void setMessageInfo(String messageInfo) {
		this.messageInfo = messageInfo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
