package com.myheat.frame.receivers;

import com.myheat.frame.MyApplication;
import com.myheat.frame.tool.DebugLog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.widget.Toast;

/**
 * 	 renzhiqiang:
 * @version 2013年9月27日 下午6:02:52 类说明 :
 * 网络监听
 */
public class ConnectionChangeReceiver extends BroadcastReceiver {

	private static final String TAG = ConnectionChangeReceiver.class
			.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		boolean isAvailable = false;

		// 获得网络连接服务
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
//		 State state = connManager.getActiveNetworkInfo().getState();
		 
		// 获取WIFI网络连接状态
		State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		// 判断是否正在使用WIFI网络
		if (State.CONNECTED == state) {
			isAvailable = true;
		}
		// 获取GPRS网络连接状态
		state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		// 判断是否正在使用GPRS网络
		if (State.CONNECTED == state) {
			isAvailable = true;
		}

		if (isAvailable) {
//			Toast.makeText(context, "网络连接正常", Toast.LENGTH_LONG).show();
		}else{
			Toast.makeText(context, "网络未连接", Toast.LENGTH_LONG).show();
		}
		DebugLog.d(TAG, "网络状态改变值=" + isAvailable);
		
		MyApplication.NETWORK_ISCONN  = isAvailable;
	}

	/**
	 * 记得在Manifest文件里面进行权限声明，和广播接收器注册。 < !-- Needed to check when the network
	 * connection changes --> < uses-permission
	 * android:name="android.permission.ACCESS_NETWORK_STATE"/> < receiver
	 * android:name="you_package_name.ConnectionChangeReceiver"
	 * android:label="NetworkConnection"> < intent-filter> < action
	 * android:name="android.net.conn.CONNECTIVITY_CHANGE"/> < /intent-filter> <
	 * /receiver>
	 * 
	 * 使用方式一： 1. 在Activity的onCreate中: //注册网络监听 IntentFilter filter = new
	 * IntentFilter();
	 * filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
	 * registerReceiver(mNetworkStateReceiver, filter); 2.
	 * 在Activity中的onDestroy中: //取消监听 unregisterReceiver(mNetworkStateReceiver);
	 * 
	 * 使用方式二： 1. 应用启动时，启动Service，在Service的onCreate方法中注册网络监听： //注册网络监听
	 * IntentFilter filter = new IntentFilter();
	 * filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
	 * registerReceiver(mNetworkStateReceiver, filter); 2.
	 * 应用退出时，Service关闭，在Service的onDestroy方法中取消监听： //取消监听
	 * unregisterReceiver(mNetworkStateReceiver);
	 */
}
