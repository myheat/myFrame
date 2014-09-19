package com.myheat.frame.tool;

import java.io.File;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

/**
 * @author renzhiqiang:
 * @version 2013年11月4日 上午10:29:42 类说明 : 内存管理
 */
public class MyMemoryManager {

	/**
	 * 获取android当前可用内存大小
	 * @param ctx
	 * @return
	 */
	public static String getAvailMemory(Context ctx) {

		ActivityManager am = (ActivityManager) ctx
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		// mi.availMem; 当前系统的可用内存
		// 将获取的内存大小规格化
		return Formatter.formatFileSize(ctx, mi.availMem);
	}
	
	/**
	 * 当前系统的可用内存
	 * @param ctx
	 * @return
	 */
	public static long getAvailMemoryLong(Context ctx) {

		ActivityManager am = (ActivityManager) ctx
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		// mi.availMem; 当前系统的可用内存
		// 将获取的内存大小规格化
		return mi.availMem;
	}
	

//	public static String getTotalMemory(Context ctx) {
//		String str1 = "/proc/meminfo";// 系统内存信息文件
//		String str2;
//		String[] arrayOfString;
//		long initial_memory = 0;
//
//		try {
//			FileReader localFileReader = new FileReader(str1);
//			BufferedReader localBufferedReader = new BufferedReader(
//					localFileReader, 8192);
//			str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
//
//			arrayOfString = str2.split("//s+");
//			for (String num : arrayOfString) {
//				Log.i(str2, num + "/t");
//			}
//
//			initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
//			localBufferedReader.close();
//
//		} catch (IOException e) {
//		}
//		return Formatter.formatFileSize(ctx, initial_memory);// Byte转换为KB或者MB，内存大小规格化
//	}
}
