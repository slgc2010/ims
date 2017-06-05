package org.yl.ims.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yl.ims.model.db.Say;
import org.yl.ims.service.SayService;
import org.yl.ims.util.Page;

@Controller
@RequestMapping("memory")
public class SayCtrl {

	@Autowired
	private SayService sayService;

	@RequestMapping("getShuoShuo")
	@ResponseBody
	public Page<Say> getSayData(int pageNumber, int pageSize) {
		return sayService.getMsgData(pageNumber, pageSize);
	}
}
