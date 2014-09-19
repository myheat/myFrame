package com.myheat.frame.services;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import com.myheat.frame.MyApplication;
import com.myheat.frame.NotificationActivity;
import com.myheat.frame.R;
import com.myheat.frame.tool.ApkUtils;
import com.myheat.frame.tool.DebugLog;
import com.myheat.frame.tool.FileUtils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.widget.RemoteViews;

/**
 * 
 */
public class MyService extends Service {
	
	/** app下载通知*/
	private static final int NOTIFICATION_ID = 0x12;
	private NotificationManager mNM;
	Notification mNotification = new Notification();
	private RemoteViews view = null;

	private boolean downing_Falg = true;
	private int icon = android.R.drawable.stat_sys_download;
	
	// 生成一个下载线程
	ApkDownLoadThread apkDownLoadThread = null;

	private volatile Handler mServiceHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				File file = new File(FileUtils.SDCARDROOT + FileUtils.FOLDER_BASE
						+ getResources().getString(R.string.app_name));
				if(file.exists()){
					file.delete();
				}
				
				// 启动下载线程
				Thread thread = new Thread(apkDownLoadThread);
				thread.start();
				//启动下载进度线程
				new getDownNoticeProThread().start();
				// 调用第三方工具下载
				// Intent i = new
				// Intent(Intent.ACTION_VIEW,Uri.parse(Constant.versionAppUrl));
				// MyApplication.activity.startActivity(i);
				// Toast.makeText(MyApplication.activity, "正在下载,请稍候...",
				// Toast.LENGTH_SHORT).show();
				break;
			case 1:
				// 设置通知栏显示内容
				mNotification.contentView = view;
				mNotification.contentView.setProgressBar(R.id.notice_pb, 100,
						msg.arg1, false);
				mNotification.contentView.setTextViewText(R.id.notice_tv, "已下载"
						+ msg.arg1 + "%");
				
				DebugLog.d("mNotification.contentView=", msg.arg1+"%");
				
				mNotification.defaults = 0;
				mNM.notify(NOTIFICATION_ID, mNotification);
				if(msg.arg1 >= 100){
					// 下载成功
					setUpNotification(getString(R.string.app_name), 0,
							NOTIFICATION_ID,
							FileUtils.FOLDER_BASE
							+ getResources().getString(R.string.app_name));
				stopSelf();
				}
				break;
			}
		}

	};

	@Override
	public void onCreate() {
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		apkDownLoadThread = new ApkDownLoadThread(this);
		
		// 通过RemoteViews 设置notification中View 的属性
		view = new RemoteViews(this.getPackageName(),
				R.layout.notification_pro);

	}

	@Override
	public void onStart(Intent intent, int startId) {
		// 得到当前线程的Looper实例，由于当前线程是UI线程也可以通过Looper.getMainLooper()得到
	    Looper.myLooper();
	    if(MyApplication.isUpdateVersion){
	    	// 启动一个Handler
			mServiceHandler.sendEmptyMessage(0);
	    }
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * service里下载线程
	 * 
	 * @author myheat
	 * 
	 */
	class ApkDownLoadThread implements Runnable {
        Context context;
		public ApkDownLoadThread(Context context) {
			this.context = context;
		}

		@Override
		public void run() {
			// 使用Notifiaction提示客户下载结果 1为正在下载
			setUpNotification(context.getString(R.string.app_name), 1, NOTIFICATION_ID,
					FileUtils.FOLDER_BASE);
			downing_Falg = true;
			// 将下载的文件，保存到目录当中
			int result;
			DebugLog.d("MyApplication.newVersion.getAppPath()=", MyApplication.newVersion.getAppPath());
//				result = Http
//						.downFileStatus(MyApplication.newVersion.getAppPath()
//								,FileUtils.SDCARDROOT + FileUtils.FOLDER_BASE, 
//								context.getString(R.string.app_name));
//			if (result == -1) {
//				// 下载失败
//				setUpNotification(context.getString(R.string.app_name), result, NOTIFICATION_ID,
//						FileUtils.FOLDER_BASE);
//			} else if (result == 0) {
//				// 下载成功
//				if (MyApplication.isUpdateVersion) {
//					File fy = new File(
//							FileUtils.SDCARDROOT
//							+FileUtils.FOLDER_BASE 
//							+ context.getString(R.string.app_name));
//					ApkUtils.openFile(context, fy);
//				}
//			}
		}
	}

	/**
	 * 实时得到下载进度
	 * @author myheat
	 * 
	 */
	class getDownNoticeProThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			Looper.prepare();
			Message msg = new Message();
//			int filesize = Http
//					.getDownLoadFileSize(MyApplication.newVersion.getAppPath());//新版本下载路径
////			DebugLog.d("apkSize=", "" + filesize);
//			
//			while (downing_Falg) {
//				try {
//					Thread.sleep(500);//刷新下载进度0.5秒
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				// 获得指定名的文件大小
//				int downLoadSize = FileUtils.getApkFile(
//						Constant.SDCARDROOT
//						+Constant.FOLDER_BASE 
//						+ getString(R.string.app_name_apk));
//
////				DebugLog.d("downLoadSize=", "" + downLoadSize);
//				
//				float b = 0;
//				// 获得进度条数据
//				if(filesize != 0){
//				 b = (downLoadSize * 100 / filesize);	
//				}
//				String result = new DecimalFormat("###,###,###.#").format(b);
//				if (downLoadSize >= filesize) {
//					downing_Falg = false;
//				}
//				
//				msg.arg1 = Integer.valueOf(result);
//				msg.what = 1;
//				mServiceHandler.handleMessage(msg);
//			}
//
		}

	}

	/**
	 * 创建通知 SoftID 为线程ID
	 * @param text
	 * @param resultMessage
	 * @param NOTIFY_ID
	 * @param path
	 */
	private void setUpNotification(String text, int resultMessage,
			int NOTIFY_ID, String path) {
		CharSequence tickerText = null;
		if (resultMessage == -1) {
			tickerText = text + " 下载失败";
			icon = R.drawable.ic_launcher;
			downing_Falg = false;
		} else if (resultMessage == 0) {
			tickerText = text + " 下载成功";
			icon = R.drawable.ic_launcher;
			downing_Falg = false;
		} else if (resultMessage == 1) {
			tickerText = text + " 正在下载";
//			icon = R.drawable.img_downing;
		}
		long when = System.currentTimeMillis();
		 mNotification = new Notification(icon, tickerText, when);
		// 发出默认声音
		mNotification.defaults = Notification.DEFAULT_SOUND;
		// mNotification.icon = icon;
		// 放置在"正在运行"栏目中
		mNotification.flags = Notification.FLAG_AUTO_CANCEL;

		// 通过PendingIntetn设置要跳往的Activity，这里也可以设置发送一个服务或者广播，
		// 不过在这里的操作都必须是用户点击notification之后才触发的
		Intent notificationIntent = new Intent(this,
				NotificationActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(
				this, 0, notificationIntent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		// 设置通知显示的参数
		mNotification.setLatestEventInfo(MyService.this, tickerText, path,
				contentIntent);
		// 获得一个NotificationManger 对象，此对象可以对notification做统一管理，只需要知道ID
		mNM.notify(NOTIFICATION_ID, mNotification);
	}

}
