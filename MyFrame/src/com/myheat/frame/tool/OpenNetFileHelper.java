package com.myheat.frame.tool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.os.StatFs;
import android.util.Log;
import android.widget.Toast;

public class OpenNetFileHelper implements Callback {
	private Handler handler;
	private Context context;
	private String url;
	private String name;
	private ProgressDialog pd;

	private final int SUCC = 1;
	private final int FAIL = 2;


	public OpenNetFileHelper(Context context, String url) {
		this.context = context;
		this.url = url;
		handler = new Handler(this);
		// 获得网络文件的文件名
		String[] cut = url.split("/");
		name = cut[cut.length - 1].toLowerCase();
	}

	private void openFile() {
		// 调用第三方工具打开
		String expandName = getExtension(name, "");

		Intent intent = null;
		if ("txt".equals(expandName) || "rtf".equals(expandName)) {
			intent = MyIntent.getTextFileIntent(FileUtils.SDCARDROOT + name);
		} else if ("doc".equals(expandName) || "docx".equals(expandName)) {
			intent = MyIntent.getWordFileIntent(FileUtils.SDCARDROOT + name);
		} else if ("xls".equals(expandName) || "xlsx".equals(expandName)) {
			intent = MyIntent.getExcelFileIntent(FileUtils.SDCARDROOT + name);
		} else if ("pdf".equals(expandName)) {
			intent = MyIntent.getPdfFileIntent(FileUtils.SDCARDROOT + name);
		} else if ("ppt".equals(expandName) || "pptx".equals(expandName)) {
			intent = MyIntent.getPptFileIntent(FileUtils.SDCARDROOT + name);
		} else if ("jpg".equals(expandName) || "png".equals(expandName)) {
			intent = MyIntent.getImageFileIntent(FileUtils.SDCARDROOT + name);
		}

		if (intent != null) {
			context.startActivity(intent);
		} else {
			Toast.makeText(context, "格式不支持！", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 通过文件名获得扩展名 
	 * @param filename
	 * @param defExt
	 * @return
	 */
	public static String getExtension(String filename, String defExt) {
		if ((filename != null) && (filename.length() > 0)) {
			int i = filename.lastIndexOf('.');

			if ((i > -1) && (i < (filename.length() - 1))) {
				return filename.substring(i + 1);
			}
		}
		return defExt;
	}

	/**
	 * 
	 * 打开网络上的文档
	 * 
	 * */
	public void openNetDocumentFile() {
		// 1.判断SD是否存在
		if (!FileUtils.isSDCardAvailable()) {
			Toast.makeText(context, "找不到SD卡，无法打开！", Toast.LENGTH_LONG).show();
			return;
		}

		// 2.判断SD卡空间是否大于50M
		if (MyMemoryManager.getAvailMemoryLong(context) < 50) {
			Toast.makeText(context, "SD卡剩余空间不足，无法打开！", Toast.LENGTH_LONG)
					.show();
			return;
		}

		// 3.如果缓存文件夹不存在则创建
		File f = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/WisdomTree/document");
		if (!f.exists()) {
			f.mkdirs();
		}

		// 4.判断是否存在缓存文件
		final File file = new File(FileUtils.SDCARDROOT + name);

		if (file.exists()) {

			// 如果存在缓存文件则直接打开
			openFile();

		} else {
			// 如果不存在则先开线程下载再打开

			pd = new ProgressDialog(context);
			pd.setMessage("正在加载文件！");
			pd.show();

			new Thread(new Runnable() {
				@Override
				public void run() {
					try {

						// 获得文件输入流
						URL u = new URL(url);
						InputStream is = u.openStream();

						// 写入临时文件
						File tempFile = new File(FileUtils.SDCARDROOT + name + ".temp");
						FileOutputStream fos = new FileOutputStream(tempFile);
						byte[] buffer = new byte[1024];
						int length = -1;
						while ((length = is.read(buffer)) != -1) {
							fos.write(buffer, 0, length);
						}
						is.close();
						fos.close();

						// 临时文件名改回来
						tempFile.renameTo(file);
						// 发送消息打开文件
						handler.sendEmptyMessage(SUCC);

					} catch (Exception e) {
						// 发送消息，提示下载失败
						handler.sendEmptyMessage(FAIL);

					}

				}
			}).start();
		}

	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case SUCC:
			pd.dismiss();
			openFile();
			break;

		case FAIL:
			pd.dismiss();
			Toast.makeText(context, "加载文件失败！", Toast.LENGTH_LONG).show();
			break;
		}
		return false;
	}
}
