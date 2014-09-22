package com.myheat.frame;

import java.util.HashMap;
import java.util.Map.Entry;
import com.myheat.frame.download.DataWatcher;
import com.myheat.frame.download.DownloadManager;
import com.myheat.frame.entities.DownloadEntry;
import com.myheat.frame.tool.DebugLog;
import com.myheat.frame.tool.FileUtils;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author myheat
 * @version 2014年9月17日下午6:00:14
 */
public class MutiDownActivity extends Activity {
	DataWatcher mDataWatcher = new DataWatcher() {

		@Override
		public void onDownloadStatusChanged(
				HashMap<String, DownloadEntry> mDownloadQueue) {
			updateDownloadStatus(mDownloadQueue);
		}
	};

	private void updateDownloadStatus(
			HashMap<String, DownloadEntry> mDownloadQueue) {
		tv.setText(null);
		for (Entry<String, DownloadEntry> entry : mDownloadQueue.entrySet()) {

			tv.append("downloading:"
					+ entry.getValue().getName()
					+ ","
					+ entry.getValue().getStatus().name()
					+ (entry.getValue().getProgress() * 100l / entry.getValue()
							.getFileSize()) + "%\n");
		}
	}

	TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.muti_down);

		// 初始化下载类
		DownloadManager.getInstance(this);
		// 创建下载目录
		FileUtils.initFolders();

		Button download1 = (Button) findViewById(R.id.download1);
		download1.setOnClickListener(oc);
		Button pause1 = (Button) findViewById(R.id.pause1);
		pause1.setOnClickListener(oc);
		Button resume1 = (Button) findViewById(R.id.resume1);
		resume1.setOnClickListener(oc);

		Button download2 = (Button) findViewById(R.id.download2);
		download2.setOnClickListener(oc);
		Button pause2 = (Button) findViewById(R.id.pause2);
		pause2.setOnClickListener(oc);
		Button resume2 = (Button) findViewById(R.id.resume2);
		resume2.setOnClickListener(oc);

		tv = ((TextView) (findViewById(R.id.progress1)));

	}

	private DownloadEntry entry1;
	private DownloadEntry entry2;
	OnClickListener oc = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.download1:
				entry1 = new DownloadEntry();
				entry1.setId("123");
				entry1.setName("weixin");
				entry1.setFileSize(24390789);
				entry1.setCreateTime(System.currentTimeMillis());
				entry1.setUrl("http://bcs.duapp.com/jsontest/weixin.apk");
				String path = FileUtils.getDownloadPath(entry1.getUrl());
				DebugLog.d("path=", path);
				entry1.setPath(path);
				DownloadManager.getInstance(MutiDownActivity.this).add(entry1);
				break;
			case R.id.pause1:
				DownloadManager.getInstance(MutiDownActivity.this).pause(entry1);
				break;
			case R.id.resume1:
				DownloadManager.getInstance(MutiDownActivity.this).resume(entry1);
				break;
			case R.id.download2:
				entry2 = new DownloadEntry();
				entry2.setId("124");
				entry2.setName("gfan");
				entry2.setFileSize(3098521);
				entry2.setCreateTime(System.currentTimeMillis());
				entry2.setUrl("http://bcs.duapp.com/jsontest/gfan.apk");
				String path2 = FileUtils.getDownloadPath(entry2.getUrl());
				DebugLog.d("path2=", path2);
				entry2.setPath(path2);
				DownloadManager.getInstance(MutiDownActivity.this).add(entry2);
				break;
			case R.id.pause2:
				DownloadManager.getInstance(MutiDownActivity.this).pause(entry2);
				break;
			case R.id.resume2:
				DownloadManager.getInstance(MutiDownActivity.this).resume(entry2);
				break;
			}
		}
	};

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		DownloadManager.getInstance(this).removeObserver(mDataWatcher);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		DownloadManager.getInstance(this).addObserver(mDataWatcher);
	}
	
}
