package com.myheat.frame.download;

import com.myheat.frame.common.Constants;
import com.myheat.frame.entities.DownloadEntry;
import com.myheat.frame.entities.DownloadStatus;

import android.content.Context;
import android.content.Intent;

/**
 * 下载管理类
 * @author myheat
 */
public class DownloadManager {

	private static DownloadManager instance;
	private Context context;

	private long previousExecuteTime;
	/** 最小时间间隔*/
	private static final long MIN_EXECUTION_INTERVEL = 1000;
		
	private DownloadManager(Context context) {
   this.context = context;
	}

	public static DownloadManager getInstance(Context context) {
		if (instance == null) {
			instance = new DownloadManager(context);
		}
		return instance;
	}

	public void add(DownloadEntry entry) {
		if(checkIfExecuteable()){
			context.startService(getDownloadIntent(entry, DownloadStatus.ADD));
		}
	}

	public void pause(DownloadEntry entry) {
		if(checkIfExecuteable()){
		context.startService(getDownloadIntent(entry, DownloadStatus.PAUSE));
		}
	}

	public void resume(DownloadEntry entry) {
		if(checkIfExecuteable()){
		context.startService(getDownloadIntent(entry, DownloadStatus.RESUME));
		}
	}

	public void cancel(DownloadEntry entry) {
		if(checkIfExecuteable()){
		context.startService(getDownloadIntent(entry, DownloadStatus.CANCEL));
		}
	}
	/**
	 * 控制，2次点击时间间隔
	 * @return
	 */
	private boolean checkIfExecuteable(){
		long currentExecuteTime = System.currentTimeMillis();
		if(currentExecuteTime - previousExecuteTime < MIN_EXECUTION_INTERVEL){
			return false;
		}else {
			previousExecuteTime = currentExecuteTime;
			return true;
		}
	}
	
	private Intent getDownloadIntent(DownloadEntry entry,DownloadStatus status){
		Intent service = new Intent(context,DownloadService.class);
		service.putExtra(Constants.KEY_DOWNLOAD_ENTRTY, entry);
		service.putExtra(Constants.KEY_DOWNLOAD_ACTION, status);
		return service;
	}
	
	public void addObserver(DataWatcher observer){
		DataChanger.getInstance().addObserver(observer);
	}
	
	public void removeObserver(DataWatcher observer){
		DataChanger.getInstance().deleteObserver(observer);
	}
	
	public void removeObservers(){
		DataChanger.getInstance().deleteObservers();
	}
}
