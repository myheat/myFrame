package com.myheat.frame.services;

import java.util.HashMap;
import java.util.Map.Entry;

import com.myheat.frame.common.Constants;
import com.myheat.frame.db.controller.DownloadEntryController;
import com.myheat.frame.download.DataChanger;
import com.myheat.frame.download.DownloadTask;
import com.myheat.frame.entities.DownloadEntry;
import com.myheat.frame.entities.DownloadStatus;
import com.myheat.frame.tool.DebugLog;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

/**
 * @author myheat
 */
public class DownloadService extends Service {

	private DataChanger mDownloadChanger;
	

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// initialize DataChanger
		mDownloadChanger = DataChanger.getInstance();
		// query DownloadEntry history from db
		mDownloadChanger.setDownloadQueue(DownloadEntryController.queryAllUnCompletedRecord());
		// start uncompleted download
		recoverDownload();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// get action, DownloadEntry
		// download DownloadTask
		if (intent != null && intent.getExtras() != null) {
			DownloadStatus mActionType = (DownloadStatus) intent
					.getSerializableExtra(Constants.KEY_DOWNLOAD_ACTION);
			DownloadEntry entry = (DownloadEntry) intent
					.getSerializableExtra(Constants.KEY_DOWNLOAD_ENTRTY);

			switch (mActionType) {
			case ADD:
				addDownload(entry);
				break;
			case PAUSE:
				pauseDownload(entry);
				break;
			case RESUME:
				resumeDownload(entry);
				break;
			case CANCEL:
				cancelDownload(entry);
				break;
			case STARTALL:
				recoverDownload();
				break;
			case CANCELALL:
				interruptDownload();
				break;
			default:
				break;
			}
		}

		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 取消下载
	 * @param entry
	 */
	private void cancelDownload(DownloadEntry entry) {
		HashMap<String, DownloadTask> tasks = mDownloadChanger.getDownloadTasks();
		if (tasks.containsKey(entry.getId())) {
			tasks.get(entry.getId()).cancel();
		}
	}
	/**
	 * 重新下载
	 * @param entry
	 */
	private void resumeDownload(DownloadEntry entry) {
		if (mDownloadChanger.getDownloadQueue().containsKey(entry.getId())) {
			DownloadEntry entity = mDownloadChanger.getDownloadQueue().get(entry.getId());
			if (entity.getFileSize() == entity.getProgress()) {
				return;
			}
			addDownload(mDownloadChanger.getDownloadQueue().get(entry.getId()));
		}
	}
	/**
	 * 暂停下载
	 * @param entry
	 */
	private void pauseDownload(DownloadEntry entry) {
		HashMap<String, DownloadTask> tasks = mDownloadChanger.getDownloadTasks();
		if (tasks.containsKey(entry.getId())) {
			tasks.get(entry.getId()).pause();
		}
	}
	
	/**
	 * 恢复下载
	 */
	public void recoverDownload() {
		HashMap<String, DownloadEntry> queue = mDownloadChanger.getDownloadQueue();
		for (Entry<String, DownloadEntry> entry : queue.entrySet()) {
			if (entry.getValue().getStatus() == DownloadStatus.INTERRUPT) {
				addDownload(entry.getValue());
			}
		}
	}
	
	/**
	 * 中断下载
	 */
	public void interruptDownload() {
		HashMap<String, DownloadEntry> queue = mDownloadChanger.getDownloadQueue();
		HashMap<String, DownloadTask> tasks = mDownloadChanger.getDownloadTasks();
		for (Entry<String, DownloadTask> entry : tasks.entrySet()) {
			entry.getValue().pauseByNet();
		}
	}

	
	private synchronized void addDownload(DownloadEntry entry) {
		if (!mDownloadChanger.getDownloadQueue().containsKey(entry.getId())) {
			mDownloadChanger.getDownloadQueue().put(entry.getId(), entry);
		}

		// TODO
		if (mDownloadChanger.getDownloadTasks().size() >= Constants.MAX_DOWLOAD_THREAD_SIZE) {
			// update entry to waiting
			entry.setStatus(DownloadStatus.WAITING);
			DownloadEntryController.addOrUpdate(entry);
			mDownloadChanger.notifyDataChanged(entry);
		} else {
			startDownload(entry);
		}
	}

	private synchronized void startDownload(DownloadEntry entry) {
		// TODO Auto-generated method stub
		entry.setStatus(DownloadStatus.DOWNLOADING);
		DownloadEntryController.addOrUpdate(entry);
		mDownloadChanger.notifyDataChanged(entry);
		
		DownloadTask task = new DownloadTask(entry, mDownloadHandler);
		mDownloadChanger.getDownloadTasks().put(entry.getId(), task);
		task.start();
	}

	private Handler mDownloadHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case Constants.SERVICE_HANDLER_TAG:
				DownloadEntry entry = (DownloadEntry) msg.obj;
				mDownloadChanger.notifyDataChanged(entry);
				checkStatus(entry);
				break;
			default:
				break;
			}
		}

	};

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	protected void checkStatus(DownloadEntry entry) {
		switch (entry.getStatus()) {
		case CANCEL:
		case COMPLETE:
		case PAUSE:
			mDownloadChanger.getDownloadTasks().remove(entry.getId());
			HashMap<String, DownloadEntry> mDownloadQueue = mDownloadChanger.getDownloadQueue();
			for (Entry<String, DownloadEntry> entity : mDownloadQueue.entrySet()) {
				if (entity.getValue().getStatus() == DownloadStatus.WAITING) {
					addDownload(entity.getValue());
				}
			}
			break;
		case INTERRUPT:
			mDownloadChanger.getDownloadTasks().remove(entry.getId());
		case DOWNLOADING:
			// start notification
			break;
		default:
			break;
		}
	}
	
}
