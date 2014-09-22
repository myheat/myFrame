package com.myheat.frame.download;

import java.io.File;
import java.util.HashMap;

import com.myheat.frame.common.Constants;
import com.myheat.frame.db.controller.DownloadEntryController;
import com.myheat.frame.entities.DownloadEntry;
import com.myheat.frame.entities.DownloadStatus;
import com.myheat.frame.tool.TextUtil;

import android.os.Handler;
import android.os.Message;

/**
 * @author myheat
 * 
 * 文件下载任务
 * 
 */
public class DownloadTask extends Thread {
    /** 当前下载实例对象 */
	private DownloadEntry entry;
	/** */
	private DownloadThread[] downloads;
	/** 回传主线程，更新UI */
	private Handler mDownloadHandler;
	/** */
	private int percent = 0;
	private DownloadStatus status = DownloadStatus.DOWNLOADING;
	private boolean isFinished;

	public DownloadTask(DownloadEntry entry, Handler mDownloadHandler) {
		this.entry = entry;
		this.mDownloadHandler = mDownloadHandler;
		
		//初始化单个文件的下载线程数量
		if (!TextUtil.isValidate(entry.getDownloadedData())) {
			HashMap<Integer, Integer> data = new HashMap<Integer, Integer>();
			for (int i = 0; i < Constants.MAX_DOWLOAD_THREAD_SIZE; i++) {
				data.put(i, 0);
			}
			entry.setDownloadedData(data);
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
        //根据文件下载线程数，平分每个线程下载内容大小
		int blocks = entry.getFileSize() / Constants.MAX_FILE_THREAD_SIZE;
        //确定文件线程数下载完整，需要的块数
		if (entry.getFileSize() % Constants.MAX_FILE_THREAD_SIZE != 0) {
			blocks++;
		}
        
		downloads = new DownloadThread[Constants.MAX_DOWLOAD_THREAD_SIZE];
		
		try {
			while (!checkIfIsFinished()) {
				for (int i = 0; i < downloads.length; i++) {
					if (downloads[i] == null || (downloads[i].isNetError() && downloads[i].isCompleted())) {
						downloads[i] = new DownloadThread(this,i,blocks, entry);
						downloads[i].setPriority(Thread.MAX_PRIORITY);
						downloads[i].start();
					}
				}
				Thread.sleep(500);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		switch (status) {
		case CANCEL:
			entry.setDownloadedData(null);
			entry.setProgress(0);
			File file = new File(entry.getPath());
			if (file.exists()) {
				file.delete();
			}
			break;
		case PAUSE:
		case COMPLETE:
		case INTERRUPT:
			entry.setStatus(status);
			DownloadEntryController.addOrUpdate(entry);
			Message msg = new Message();
			msg.what = 1;
			msg.obj = entry;
			mDownloadHandler.sendMessage(msg);
			break;
		default:
			break;
		}
		
	}
	
	private synchronized boolean checkIfIsFinished() {
		boolean isFinished = false;
		for (int i = 0; i < downloads.length; i++) {
			if (downloads[i] != null) {
				isFinished = downloads[i].isCompleted();
				if (!isFinished) {
					return false;
				}
			}else {
				return false;
			}
		}
		return isFinished;
	}
	
	/**
	 * 更新下载进度
	 * @param index
	 * @param readLength
	 * @param curPos
	 */
	public synchronized void update(int index,int readLength, int curPos) {
		HashMap<Integer, Integer> mDownloadData = entry.getDownloadedData();
		mDownloadData.put(index, curPos);
		entry.setProgress(entry.getProgress() + readLength);
		entry.setDownloadedData(mDownloadData);
		if (entry.getProgress() == entry.getFileSize()) {
			status = DownloadStatus.COMPLETE;
			return;
		}
		if(entry.getProgress() * 100l / entry.getFileSize()  > percent){
			Message msg = new Message();
			msg.what = 1;
			msg.obj = entry;
			mDownloadHandler.sendMessage(msg);
		}
	}
	
	public void pause() {
		status = DownloadStatus.PAUSE;
		for (int i = 0; i < downloads.length; i++) {
			if (downloads[i] != null) {
				downloads[i].pause();
			}
		}
	}

	public void cancel() {
		status = DownloadStatus.CANCEL;
		for (int i = 0; i < downloads.length; i++) {
			if (downloads[i] != null) {
				downloads[i].pause();
			}
		}
	}
    /**
     * 因为网络问题，暂停
     */
	public void pauseByNet() {
		status = DownloadStatus.INTERRUPT;
		for (int i = 0; i < downloads.length; i++) {
			if (downloads[i] != null) {
				downloads[i].pause();
			}
		}
	}

}
