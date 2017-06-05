package org.yl.ims.util;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class TemplateResourceLoader extends DefaultResourceLoader {
	private PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

	@Override
	public Resource getResource(String location) {
		Resource resource = super.getResource(location);
		if (null == resource) {
			resource = resolver.getResource(location);
		}
		return resource;
	}
}
