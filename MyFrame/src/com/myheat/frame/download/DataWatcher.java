package com.myheat.frame.download;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import com.myheat.frame.entities.DownloadEntry;

/**
 * 观察者
 * @author myheat
 */
public abstract class DataWatcher implements Observer {

	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		onDownloadStatusChanged(((DataChanger)observable).getDownloadQueue());
		
	}

	public abstract void onDownloadStatusChanged(HashMap<String, DownloadEntry> entries);

}
