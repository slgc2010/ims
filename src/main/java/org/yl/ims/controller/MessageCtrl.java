package org.yl.ims.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yl.ims.model.db.MessageInfo;
import org.yl.ims.service.MsgService;

@Controller
@RequestMapping("message")
public class MessageCtrl {

	@Autowired
	private MsgService msgService;

	@RequestMapping("getFirstMessage")
	@ResponseBody
	public MessageInfo getFirstMessage(int status) {
		return msgService.getMessageInfoByStatus(status);
	}
}
