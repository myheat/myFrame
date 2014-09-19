package com.myheat.frame.common;

import android.net.Uri;
/**
 * ActivityForResult工具类
 * 
 * @author rendongwei
 * 
 */
public class ActivityForResultUtil {
	/** 调用系统相机拍照 */
	public static final int SYSTEM_CAMERA = 0x2001;
	/** 调用自定义相机拍照 */
	public static final int CUSTOM_CAMERA = 0x2002;
	/** 选择系统相册图片 */
	public static final int SYSTEM_ALBUM = 0x2003;
	/** 调用系统裁剪图片 */ 
	public static final int SYSTEM_CROP = 0x2004; 
    /** 图片类型*/
	public static final String IMAGE_UNSPECIFIED = "image/*";
	/** 临时图片名*/
	public static final String TEMP_PNG = "temp.png";
	
	public static Uri imguri = null;
	public static String fileName = "";
	public static String systemDataPath = "/data/data/";
}
