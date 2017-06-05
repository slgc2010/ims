package org.yl.ims.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.yl.ims.service.UserService;
import org.yl.ims.util.PathUtil;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PageCtrl {
	@Autowired
	private UserService userService;

	@RequestMapping(value = { "guest", "guest/*" })
	public String index(HttpServletRequest request, ModelMap map) {
		all(request,map);
		return "page/default";
	}

	@RequestMapping(value = { "going", "going/*" })
	public String index1(HttpServletRequest request, ModelMap map) {
		all(request,map);
		return "page/login";
	}

	@RequestMapping(value = { "admin","admin/*"  })
	public String admin(HttpServletRequest request, ModelMap map) {
		all(request,map);
		return "page/manager-default";
	}
	
	@RequestMapping(value = { "say","say/*"  })
	public String say(HttpServletRequest request, ModelMap map) {
		all(request,map);
		return "page/say-time";
	}
	@RequestMapping(value = { "user","user/*"  })
	public String userinfo(HttpServletRequest request, ModelMap map) {
		all(request,map);
		return "page/user";
	}
	@RequestMapping(value = { "usermine","usermine/*"  })
	public String usermine(HttpServletRequest request, ModelMap map) {
		all(request,map);
		return "page/birth";
	}

	@RequestMapping(value = { "cake","cake/*"  })
	public String usercake(HttpServletRequest request, ModelMap map) {
		all(request,map);
		return "page/cake";
	}

	@RequestMapping(value = { "erro","erro/*"  })
	public String usererro(HttpServletRequest request, ModelMap map) {
		all(request,map);
		return "page/404";
	}
	@RequestMapping(value = { "pic","pic/*"  })
	public String userpic(HttpServletRequest request, ModelMap map) {
        all(request,map);
		return "page/pic";
	}
	@RequestMapping(value = { "addblog","addblog/*"  })
	public String addblog(HttpServletRequest request, ModelMap map) {
		all(request,map);
		return "page/add-blog";
	}


	private ModelMap all(HttpServletRequest request, ModelMap map){
		map.put("rootPath", PathUtil.getRelatedRootPath(request));
		map.put("imsPath", PathUtil.getServletRelatedPath(request));
		return map;
	}
}
