package com.myheat.frame.download;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Observable;

import com.myheat.frame.entities.DownloadEntry;
import com.myheat.frame.tool.TextUtil;

/**
 * 被观察者
 * @author myheat
 */
public class DataChanger extends Observable {

	private static DataChanger instance;
	/** 正在下载的任务队列*/
	private HashMap<String, DownloadTask> mDownloadingTask ;
	/** 正在下载的任务实体*/
	private LinkedHashMap<String, DownloadEntry> mDownloadingEntries;

	private DataChanger() {
		mDownloadingTask = new HashMap<String, DownloadTask>();
		mDownloadingEntries = new LinkedHashMap<String, DownloadEntry>();
	}

	public static DataChanger getInstance() {

		if (instance == null) {
			instance = new DataChanger();
		}
		return instance;
	}

	public void notifyDataChanged(DownloadEntry entry) {
		setChanged();
		notifyObservers();
	}

	public HashMap<String, DownloadEntry> getDownloadQueue() {
		// TODO Auto-generated method stub
		return mDownloadingEntries;
	}

	public HashMap<String, DownloadTask> getDownloadTasks(){
		return mDownloadingTask;
	}
	
	public void setDownloadQueue(LinkedHashMap<String, DownloadEntry> queue) {
		if (TextUtil.isValidate(queue)) {
			mDownloadingEntries = queue;
		}
	}
}
