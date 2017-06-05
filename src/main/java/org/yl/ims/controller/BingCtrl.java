package org.yl.ims.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yl.ims.model.BingModel;

@Controller
@RequestMapping("bingapi")
public class BingCtrl {

	@RequestMapping("getBingData")
	@ResponseBody
	public BingModel getBingData() {

		String url = "https://www.dujin.org/sys/bing/1920.php";
		return new BingModel(url);
		
	}

/*	public BingModel getBingData(int previous) {
		String s = Bing.getBingData(previous);
		String url = s.substring(s.indexOf("http"),s.indexOf(".jpg")+4);
		return new BingModel(url);

	}*/
}
