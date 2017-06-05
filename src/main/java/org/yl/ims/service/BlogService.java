package org.yl.ims.service;

import org.yl.ims.model.db.Blog;
import org.yl.ims.util.Page;

public interface BlogService {
	public Page<Blog> getBlogData(int pageNumber, int pageSize);

	public Blog getBlogById(Integer blogId);

	public Integer delBlogById(Integer blogId);

	public Integer saveOrUpdateBlog(Integer blogId);
}
