package com.myheat.frame.download;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import org.apache.http.HttpStatus;

import com.myheat.frame.entities.DownloadEntry;
import com.myheat.frame.tool.DebugLog;

/**
 * @author myheat
 * 文件下载线程
 */
public class DownloadThread extends Thread {
    /** 连接超时*/
	private static final int TIMEOUT_CONNECTION = 15000;
	/** 连接准备时间*/
	private static final int TIMEOUT_READ = 15000;
	/** 流获取大小常量 */
	private static final int IO_BUFFER_SIZE = 4 * 1024;
    /** 下载实例对象*/
	private DownloadEntry entry;
	private int blocks;
	/** 下载起始位置*/
	private int index;
	/** 开始下载位置*/
	private int startPosition;
	/** 最后下载位置*/
	private int endPosition;
	/** 是否完成*/
	private boolean isCompleted = false;
	/** 下载任务*/
	private DownloadTask downloadTask;
	/** 是否暂停*/
	private boolean isPaused;
	/** 下载长度*/
	private Integer downloadedLength;
	/** 是否网络错误*/
	private boolean isNetError;

	public DownloadThread(DownloadTask downloadTask, int index, int blocks,
			DownloadEntry entry) {
		this.downloadTask = downloadTask;
		this.index = index;
		this.blocks = blocks;
		this.entry = entry;
		
		this.downloadedLength = entry.getDownloadedData().get(index);
		startPosition = index * blocks + downloadedLength;
		endPosition = (index + 1) * blocks - 1;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			URL url = new URL(entry.getUrl());
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(TIMEOUT_CONNECTION);
			connection.setReadTimeout(TIMEOUT_READ);
			connection.setRequestProperty(
							"Accept",
							"image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash,"
							+ " application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, "
							+ "application/x-ms-application, application/vnd.ms-excel, "
							+ "application/vnd.ms-powerpoint, application/msword, */*");
			connection.setRequestProperty("Accept-Language", "zh-CN");
			connection.setRequestProperty("Referer", url.toString());
			connection.setRequestProperty("Charset", "UTF-8");
			connection.setRequestProperty("Range", "bytes=" + startPosition
					+ "-" + endPosition);// 设置获取实体数据的范围
			connection
					.setRequestProperty(
							"User-Agent",
							"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; "
							+ ".NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
			connection.setRequestProperty("Connection", "Keep-Alive");
			int statusCode = connection.getResponseCode();
			long contentLength = connection.getContentLength();
			InputStream in = null;
			switch (statusCode) {
			case HttpStatus.SC_OK:
			case HttpStatus.SC_PARTIAL_CONTENT:
				String encoding = connection.getContentEncoding();
				if (encoding != null && "gzip".equalsIgnoreCase(encoding))
					in = new GZIPInputStream(connection.getInputStream());
				else if (encoding != null
						&& "deflate".equalsIgnoreCase(encoding))
					in = new InflaterInputStream(connection.getInputStream());
				else
					in = connection.getInputStream();
				// if set path , write the data into file
				RandomAccessFile file = new RandomAccessFile(entry.getPath(),
						"rwd");
				file.setLength(entry.getFileSize());
				file.seek(startPosition);
				byte[] b = new byte[IO_BUFFER_SIZE];
				int read;
				while ((read = in.read(b)) != -1) {
					downloadedLength += read;
					file.write(b, 0, read);
					DebugLog.d("DownloadThread---", index + "====" + downloadedLength);
					downloadTask.update(index, read, downloadedLength);
					if (isPaused) {
						break;
					}
				}
				isCompleted = true;
				file.close();
				in.close();
			}
		} catch (Exception e) {
			isNetError = true;
			e.printStackTrace();
		}

	}

	public boolean isCompleted() {
		return isCompleted;
	}
	
	public boolean isNetError(){
		return isNetError;
	}

	public void pause() {
		isPaused = true;
	}
}
