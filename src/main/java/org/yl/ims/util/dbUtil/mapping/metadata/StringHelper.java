package org.yl.ims.util.dbUtil.mapping.metadata;

import java.util.Locale;

public class StringHelper {
	public static String toUpperCase(String str) {
		return str == null ? null : str.toUpperCase();
	}

	public static String toLowerCase(String str) {
		return str == null ? null : str.toLowerCase(Locale.ENGLISH);
	}
}
