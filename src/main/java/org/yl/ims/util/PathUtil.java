package org.yl.ims.util;

import javax.servlet.http.HttpServletRequest;

public class PathUtil {
	public static String getRelatedRootPath(HttpServletRequest request) {
		String uri = request.getRequestURI();
		String path = request.getPathInfo();
		String servletPath = request.getServletPath();
		if (uri.indexOf(";") > 0) {
			uri = uri.substring(0, uri.indexOf(";"));
		}
		return uri.replace(servletPath + path, "");
	}

	public static String getServletRelatedPath(HttpServletRequest request) {
		String uri = request.getRequestURI();
		String path = request.getPathInfo() == null ? ""  : request.getPathInfo();
		if (uri.indexOf(";") > 0) {
			uri = uri.substring(0, uri.indexOf(";"));
		}
		return uri.replace(path, "");
	}

	public static String getBaseUrl(HttpServletRequest request) {
		String uri = request.getRequestURL().toString();
		String path = request.getPathInfo();
		return uri.replace(path, "");
	}
}
