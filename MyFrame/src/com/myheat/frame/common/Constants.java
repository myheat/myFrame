package com.myheat.frame.common;

public class Constants {
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
	public final static String LOGIN = Constants.Net.HOST_NAME
			+ Constants.Net.VERSION + "/login.php";
	
	 /** 文件的最大下载线程数*/
		public static final int MAX_DOWLOAD_THREAD_SIZE = 1;
		/** 下载一个文件的线程数*/
		public static final int MAX_FILE_THREAD_SIZE = 2;
		/** 下载实例对象键名*/
		public static final String KEY_DOWNLOAD_ENTRTY = "key_download_entrty";
		/** 下载状态键名*/
		public static final String KEY_DOWNLOAD_ACTION = "key_download_action";
		
		public static final int SERVICE_HANDLER_TAG = 1;

}
