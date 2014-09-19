package com.myheat.frame.tool;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

/**
 * 文件处理工具类
 * 
 * @author myheat
 *
 */
public class FileUtils {
	
	/** 路径:应用根目录 */
	public static String SDCARDROOT = Environment.getExternalStorageDirectory()
			+ File.separator;
	/** 根文件夹名称 */
	public static final String FOLDER = "downTool";
	/** 文件夹名称:软件根目录 */
	public static final String FOLDER_BASE = SDCARDROOT + FOLDER;
	/** 文件夹名称:偏好设置 ? */
	public static final String PREF_FILE_NAME = "pref_" + FOLDER;
	/** 文件夹名称:配置 */
	public static final String FOLDER_CONFIG = "conf";
	/** 文件夹名称:缓存 */
	public static final String FOLDER_CACHE = ".cache";
	/** 文件夹名称:数据 */
	public static final String FOLDER_DATA = "data";
	/** 文件夹名称:文件 */
	public static final String FOLDER_FILE = "file";
	/** 用户下载图片保存的路径 */
	public static final String FOLDER_IMAGE = "image";
	/** 用户下载视频保存的路径 */
	public static final String FOLDER_VIDEO = "video";
	/** 临时文件夹 */
	public static final String FOLDER_TMP = "tmp";
	/** 用户下载文档保存的路径 */
	public static final String FOLDER_DOCUMENT = "document";
	/** 获取外部存储设备的当前状态 */
	public static String SDSTATUS = Environment.getExternalStorageState();
	

	public static boolean isDirExist(String dirPath) {
		File dir = new File(dirPath);
		return dir.exists() && dir.isDirectory();
	}
	
	public static boolean isSDCardAvailable() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	public static boolean isTfCardAvailable() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	public static void checkRoot() {
		if (!isDirExist(FOLDER_BASE)) {
			createDir(FOLDER_BASE);
		}
	}

	public static void createDir(String... dirPath) {
		
		File dir = null;
		for (int i = 0; i < dirPath.length; i++) {
			dir = new File(dirPath[i]);
			if (!dir.exists() && !dir.isDirectory()) {
				dir.mkdirs();
			}
		}
	}
	
	public static void initFolders() {
		DebugLog.d("createDir=", "" + FOLDER_BASE);
		createDir(FOLDER_BASE);
	}

	public static String getBaseFilePath() {
		return FOLDER_BASE;
	}
	
	public static String getCacheFolder(){
		createDir(FOLDER_BASE + File.separator + FOLDER_CACHE);
		return FOLDER_BASE + File.separator + FOLDER_CACHE;
	}
	public static String getImageFolder() {
		createDir(FOLDER_BASE + File.separator + FOLDER_IMAGE);
		return FOLDER_BASE + File.separator + FOLDER_IMAGE;
	}

	public static String getTmpFolder() {
		createDir(FOLDER_BASE + File.separator + FOLDER_TMP);
		return FOLDER_BASE + File.separator + FOLDER_TMP;
	}

	public static String createTmpFile(String name) {
		return getTmpFolder() + File.separator + name;
	}
	
	public static String getDownloadDir(){
		createDir(FOLDER_BASE + File.separator + FOLDER_FILE);
		return FOLDER_BASE + File.separator + FOLDER_FILE;
	}

	
	public static String getDownloadTmpPath(String url) {
		return getTmpFolder() +  url.substring(url.lastIndexOf("/"));
	}
	
	public static String getDownloadPath(String url){
		return getDownloadDir() +  url.substring(url.lastIndexOf("/"));
	}
	
	/**
	 * 在SD卡上创建文件
	 */
	public static File createSDCardFile(String pathName) throws IOException {
		File file = new File(SDCARDROOT + pathName);
		if (!file.exists())
			file.createNewFile();
		return file;
	}
	/**
	 * 在SD卡上创建目录
	 * 
	 * @param dirName
	 * @return
	 */
	public static File createSDDir(String dirName) {
		File file = new File(dirName);
		if (!file.exists())
			file.mkdir();
		return file;
	}
	/**
	 * 
	 * @param filePath
	 * @return
	 */
	public static File createFileDir(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			File parentFile = file.getParentFile();
			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}
			parentFile = null;
		}
		return file;
	}
	
	public static boolean deleteFile(String fileName) {
		try {
			if (fileName == null) {
				return false;
			}
			File f = new File(fileName);

			if (f == null || !f.exists()) {
				return false;
			}

			if (f.isDirectory()) {
				return false;
			}
			return f.delete();
		} catch (Exception e) {
			// Log.d(FILE_TAG, e.getMessage());
			return false;
		}
	}

	public static boolean deleteFileOfDir(String dirName, boolean isRecurse) {
		boolean blret = false;
		try {
			File f = new File(dirName);
			if (f == null || !f.exists()) {
				// Log.d(FILE_TAG, "file" + dirName + "not isExist");
				return false;
			}

			if (f.isFile()) {
				blret = f.delete();
				return blret;
			} else {
				File[] flst = f.listFiles();
				if (flst == null || flst.length <= 0) {
					return true;
				}

				int filenumber = flst.length;
				File[] fchilda = f.listFiles();
				for (int i = 0; i < filenumber; i++) {
					File fchild = fchilda[i];
					if (fchild.isFile()) {
						blret = fchild.delete();
						if (!blret) {
							break;
						}
					} else if (isRecurse) {
						blret = deleteFileDir(fchild.getAbsolutePath(), true);
					}
				}
			}
		} catch (Exception e) {
			blret = false;
		}

		return blret;
	}

	public static boolean deleteFileDir(String dirName, boolean isRecurse) {
		boolean blret = false;
		try {
			File f = new File(dirName);
			if (f == null || !f.exists()) {
				// Log.d(FILE_TAG, "file" + dirName + "not isExist");
				return false;
			}
			if (f.isFile()) {
				blret = f.delete();
				return blret;
			} else {
				File[] flst = f.listFiles();
				if (flst == null || flst.length <= 0) {
					f.delete();
					return true;
				}
				int filenumber = flst.length;
				File[] fchilda = f.listFiles();
				for (int i = 0; i < filenumber; i++) {
					File fchild = fchilda[i];
					if (fchild.isFile()) {
						blret = fchild.delete();
						if (!blret) {
							break;
						}
					} else if (isRecurse) {
						blret = deleteFileDir(fchild.getAbsolutePath(), true);
					}
				}

				// 删除当前文件夹
				blret = new File(dirName).delete();
			}
		} catch (Exception e) {
			// Log.d(FILE_TAG, e.getMessage());
			blret = false;
		}

		return blret;
	}

	/**
	 * 移动文件
	 * 
	 * @param filePath
	 */
	public static void removeToDir(String filePath, String toFilePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			return;
		}
		file.renameTo(new File(toFilePath));
	}
	
	/**
	 * 将一个InputStream 里面的数据写入到SD卡
	 */
	public static File write2SDFromInput(String path, String fileName,
			InputStream inputStream) {
		File file = null;
		OutputStream output = null;
		createSDDir(path);
		try {
			file = createSDCardFile(path + fileName);
			output = new FileOutputStream(file);
			byte buffer[] = new byte[4 * 1024];

			while ((inputStream.read(buffer)) != -1) {
				output.write(buffer);
			}
			output.flush();
		} catch (Exception e) {
			System.out.println("write2SDFromInput1:" + e.toString());
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("write2SDFromInput2:" + e.toString());
			}
		}
		return file;
	}

	/**
	 * 读取path目录中指定fileName的文件大小
	 */
	public static int getApkFile(String pathName) {
		if (isDirExist(pathName)) {
			File file = new File(pathName);
			return (int) file.length();
		} else {
			return 0;
		}
	}

	public static byte[] readStream(InputStream inStream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;

	}

	/**
	 * 取手机内存中的图片
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getLocationImg(String url) {
		try {
			File f = new File(url);
			Bitmap bitmap = null;
			if (url.contains("sdcard/")) {
				File file = new File(url);
				if (file.exists()) {
					// InputStream is = new FileInputStream(file);
					// OutputStream os = new FileOutputStream(f);
					// CopyStream(is, os);
					// os.close();
					bitmap = decodeFile(file);
				}
				return bitmap;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	// decode这个图片并且按比例缩放以减少内存消耗，虚拟机对每张图片的缓存大小也是有限制的
	static int REQUIRED_SIZE = 70;

	private static Bitmap decodeFile(File f) {

		try {

			// decode image size

			BitmapFactory.Options o = new BitmapFactory.Options();

			o.inJustDecodeBounds = true;

			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.

			int width_tmp = o.outWidth, height_tmp = o.outHeight;

			int scale = 1;

			while (true) {

				if (width_tmp / 2 < REQUIRED_SIZE

				|| height_tmp / 2 < REQUIRED_SIZE)

					break;

				width_tmp /= 2;

				height_tmp /= 2;

				scale *= 2;

			}

			// decode with inSampleSize

			BitmapFactory.Options o2 = new BitmapFactory.Options();

			o2.inSampleSize = scale;

			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);

		} catch (FileNotFoundException e) {

		}

		return null;

	}

	public static void CopyStream(InputStream is, OutputStream os) {

		final int buffer_size = 1024;

		try {

			byte[] bytes = new byte[buffer_size];

			for (;;) {

				int count = is.read(bytes, 0, buffer_size);

				if (count == -1)

					break;

				os.write(bytes, 0, count);

			}

		} catch (Exception ex) {

		}
	}

	/**
	 * 删除文件夹
	 * 
	 * @param filePathAndName
	 *            String 文件夹路径及名称 如c:/fqf
	 * @param fileContent
	 *            String
	 * @return boolean
	 */
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹

		} catch (Exception e) {
			System.out.println("删除文件夹操作出错");
			e.printStackTrace();

		}
	}

	/**
	 * 删除文件夹里面的所有文件
	 * 
	 * @param path
	 *            String 文件夹路径 如 c:/fqf
	 */
	public static void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
			}
		}
	}
}
