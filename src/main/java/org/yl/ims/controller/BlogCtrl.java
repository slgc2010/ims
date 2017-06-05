package org.yl.ims.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yl.ims.model.db.Blog;
import org.yl.ims.service.BlogService;
import org.yl.ims.util.Page;

@Controller
@RequestMapping("blog")
public class BlogCtrl {
	@Autowired
	private BlogService blogService;

	@RequestMapping("bloglist")
	@ResponseBody
	public Page<Blog> getBlog(int pageSize, int pageIndex) {
		return blogService.getBlogData(pageIndex, pageSize);
	}

	@RequestMapping("delblog")
	public int delBlog(int bid) {
		return blogService.delBlogById(bid);
	}

	@RequestMapping("saveblog")
	public int saveBlog(int bid) {
		return blogService.saveOrUpdateBlog(bid);
	}
}
