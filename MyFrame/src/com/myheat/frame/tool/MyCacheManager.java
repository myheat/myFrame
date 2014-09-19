package com.myheat.frame.tool;

import java.io.File;
import java.text.DecimalFormat;

import android.content.Context;
import android.os.Environment;


/**
 * @author renzhiqiang:
 * @version 2013年9月23日 下午4:27:04 类说明 :
 */
public class MyCacheManager {

	
	/**
	 * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache)
	 * 
	 * @param context
	 */
	public static void cleanInternalCache(Context context) {
		deleteFilesByDirectory(context.getCacheDir());
	}

	/**
	 * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases)
	 * 
	 * @param context
	 */
	public static void cleanDatabases(Context context) {
		deleteFilesByDirectory(new File("/data/data/"
				+ context.getPackageName() + "/databases"));
	}

	/**
	 * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs)
	 * 
	 * @param context
	 */
	public static void cleanSharedPreference(Context context) {
		deleteFilesByDirectory(new File("/data/data/"
				+ context.getPackageName() + "/shared_prefs"));
	}

	/**
	 * 按名字清除本应用数据库
	 * 
	 * @param context
	 * @param dbName
	 */
	public static void cleanDatabaseByName(Context context, String dbName) {
		context.deleteDatabase(dbName);
	}

	/**
	 * 清除/data/data/com.xxx.xxx/files下的内容
	 * 
	 * @param context
	 */
	public static void cleanFiles(Context context) {
		deleteFilesByDirectory(context.getFilesDir());
	}

	/**
	 * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
	 * 
	 * @param context
	 */
	public static void cleanExternalCache(Context context) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			deleteFilesByDirectory(context.getExternalCacheDir());
		}
	}

	/**
	 * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除
	 * 
	 * @param filePath
	 */
	public static void cleanCustomCache(String filePath) {
		deleteFilesByDirectory(new File(filePath));
	}

	/**
	 * 清除本应用所有的数据
	 * 
	 * @param context
	 * @param filepath
	 */
	public static void cleanApplicationData(Context context, String... filepath) {
		cleanInternalCache(context);
		cleanExternalCache(context);
		cleanDatabases(context);
		cleanSharedPreference(context);
		cleanFiles(context);
		for (String filePath : filepath) {
			cleanCustomCache(filePath);
		}
	}
	
	public static void deleteAllFile(String folderFullPath) {
        File file = new File(folderFullPath);
        if (null != file && file.exists()) {
            if (file.isDirectory()) {
                File[] fileList = file.listFiles();
                for (int i = 0; i < fileList.length; i++) {
                    String filePath = fileList[i].getPath();
                    deleteAllFile(filePath);
                }
            } else if (file.isFile()) {
                file.delete();
            }
        }
    }

	/**
	 * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理
	 * 
	 * @param directory
	 */
	static void deleteFilesByDirectory(File directory) {
		if (directory != null && directory.exists() && directory.isDirectory()) {
			for (File item : directory.listFiles()) {
				item.delete();
			}
		}
	}
	
	/**
     * <获取文件夹或者文件大小>
     * 
     * @param String path 路径或者文件
     * @throw
     * @return String  文件的大小，以BKMG来计量
     */
    public static String getPathSize(String path) {
        String flieSizesString = "";
        File file = new File(path.trim());
        long fileSizes = 0;
        if (null != file && file.exists()) {
            if (file.isDirectory()) { // 如果路径是文件夹的时候
                fileSizes = getFileFolderTotalSize(file);
            } else if (file.isFile()) {
                fileSizes = file.length();
            }
        }
        flieSizesString = formatFileSizeToString(fileSizes);
        return flieSizesString;
    }
	
    private static long getFileFolderTotalSize(File fileDir) {
        long totalSize = 0;
        File fileList[] = fileDir.listFiles();
        for (int fileIndex = 0; fileIndex < fileList.length; fileIndex++) {
            if (fileList[fileIndex].isDirectory()) {
                totalSize =
                    totalSize + getFileFolderTotalSize(fileList[fileIndex]);
            } else {
                totalSize = totalSize + fileList[fileIndex].length();
            }
        }
        return totalSize;
    }
	/**
	 * 转换文件大小
	 * @param fileSize
	 * @return
	 */
	public static String formatFileSizeToString(long fileSize) {
        String fileSizeString = "";
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        if(fileSize == 0){
        	fileSizeString = "0byte";
        }else if (fileSize < 1024 && fileSize > 0) {
            fileSizeString = decimalFormat.format((double)fileSize) + "B";
        } else if (fileSize < (1 * 1024 * 1024)) {
            fileSizeString =
                decimalFormat.format((double)fileSize / 1024) + "K";
        } else if (fileSize < (1 * 1024 * 1024 * 1024)) {
            fileSizeString =
                decimalFormat.format((double)fileSize / (1 * 1024 * 1024))
                    + "M";
        } else {
            fileSizeString =
                decimalFormat.format((double)fileSize
                    / (1 * 1024 * 1024 * 1024))
                    + "G";
        }
        return fileSizeString;
    }
}
