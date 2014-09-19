package com.myheat.frame.common;

public class Constant {
	/**
	 * 与网络相关的常量定义
	 * 
	 */
	public interface Net {
		/** IP地址 */
		// public String HOST_IP =
		// "http:218.75.155.42";//http://192.168.1.118:188

		/** 域名地址 */
		public String HOST_NAME = "";//

		/** 目录 */
		public String Directory = "";
		/** 版本 */
		public String VERSION = Directory + "/";

		/** HTTP请求方式:GET */
		public static final String HTTP_METHOD_GET = "GET";
		/** HTTP请求方式:POST */
		public static final String HTTP_METHOD_POST = "POST";
		/** 请求成功 */
		public final static String JSON_STATUS_200 = "200";
		/** 数据已存在 */
		public final static String JSON_STATUS_300 = "300";
		/** 找不到数据 */
		public final static String JSON_STATUS_201 = "201";
		/** 数据待审核中 */
		public final static String JSON_STATUS_202 = "202";
		/** 请求参数错误 */
		public final static String JSON_STATUS_400 = "400";
		/** 请求未授权/未通过身份验证 */
		public final static String JSON_STATUS_401 = "401";
		/** 请求被拒绝(恶意访问、超过配额等) */
		public final static String JSON_STATUS_403 = "403";
		/** 请求的方法未找到 */
		public final static String JSON_STATUS_404 = "404";
		/** 加载失败 */
		public final static String JSON_STATUS_500 = "500";

	}

	// ----------------------------------------------------接口类型定义
	/** 登录 */
	public final static String LOGIN = Constant.Net.HOST_NAME
			+ Constant.Net.VERSION + "/login.php";

}
