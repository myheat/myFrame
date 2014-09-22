package com.myheat.frame;

import com.myheat.frame.entities.VersionInfo;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

/**
 * @author myheat
 * @version 2014年9月17日上午10:29:56
 */
public class MyApplication extends Application {

	/** 判断网络是否连接 */
	public static boolean NETWORK_ISCONN = false;
	/** 当前版本信息 */
	public static VersionInfo localVersion = new VersionInfo();
	/** 服务器上最新版本信息 */
	public static VersionInfo newVersion = new VersionInfo();
	
	/** 是否需要更新 */
	public static boolean isUpdateVersion = false;

	/** 判断用户是否本地登录 false未登录 true 登录 */
	public static boolean isLoginFlag = false;
	
	public static Context mContext;
	public static int mAppState;

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this.getApplicationContext();
		mAppState = -1;
	}

	public synchronized static void setAppState(int state) {
		mAppState = state;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	
}
