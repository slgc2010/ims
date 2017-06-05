package org.yl.ims.service.impl;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.yl.ims.model.db.Blog;
import org.yl.ims.service.BlogService;
import org.yl.ims.util.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlogServiceImpl extends BaseService<Blog> implements BlogService {

	private BeanPropertyRowMapper<Blog> blogRowMapper = new BeanPropertyRowMapper<Blog>(Blog.class);

	@Override
	public Page<Blog> getBlogData(int pageNumber, int pageSize) {
		String rawSql = "select * from blog order by createTime desc";
		return this.paging(rawSql, pageSize, pageNumber, blogRowMapper);
	}

	@Override
	public Blog getBlogById(Integer blogId) {
		String rawSql = "select * from blog where id = :id";
		Map<String, Integer> p = new HashMap<String, Integer>();
		p.put("id", blogId);
		List<Blog> query = this.template.query(rawSql, p, blogRowMapper);
		return (!query.isEmpty() && query.size() > 0) ? query.get(0) : null;
	}

	@Override
	public Integer delBlogById(Integer blogId) {
		String rawSql = "delete blog where id = :id";
		Map<String, Integer> p = new HashMap<String, Integer>();
		p.put("id", blogId);
		return this.template.update(rawSql, p);
	}

	@Override
	public Integer saveOrUpdateBlog(Integer blogId) {
		String rawSql = "Update blog where id = :id";
		Map<String, Integer> p = new HashMap<String, Integer>();
		p.put("id", blogId);
		return this.template.update(rawSql, p);
	}

}
