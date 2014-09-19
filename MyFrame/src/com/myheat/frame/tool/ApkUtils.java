package com.myheat.frame.tool;

import java.io.File;
import java.util.List;

import org.json.JSONObject;
import com.myheat.frame.MyApplication;
import com.myheat.frame.R;
import com.myheat.frame.entities.VersionInfo;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 * app处理工具类
 * 
 * @author myheat
 * 
 */
public class ApkUtils {

	/**
	 * 在手机上安装Android应用程序文件
	 */
	public static void openFile(Context ctx, File f) {
		if (ctx == null)
			return;
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(f),
				"application/vnd.android.package-archive");
		ctx.startActivity(intent);
	}

	/**
	 * 卸载Android应用程序
	 * 
	 * @param packageName
	 */
	public static void uninstallApk(String packageName, Activity activity) {
		Uri packageURI = Uri.parse("package:" + packageName);
		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
		activity.startActivityForResult(uninstallIntent, 1);
	}

	/**
	 * 获得当前版本信息存储在versionInfo对象里
	 * 
	 * @param act
	 * @return
	 */
	public static VersionInfo checkVersion(Activity act) {
		VersionInfo versionInfo = new VersionInfo();
		// 用来存储获取的应用信息数据　　　　
		List<PackageInfo> packages = act.getPackageManager()
				.getInstalledPackages(0);
		for (int i = 0; i < packages.size(); i++) {
			PackageInfo packageInfo = packages.get(i);
			// 判断一个应用是否为系统应用
			if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
				// 如果非系统应用,手机上安装的当前应用程序信息存储在tmpInfo里了
				if (packageInfo.applicationInfo
						.loadLabel(act.getPackageManager())
						.toString()
						.equals(act.getResources().getString(R.string.app_name))) {
					versionInfo.setAppName(packageInfo.applicationInfo
							.loadLabel(act.getPackageManager()).toString());
					versionInfo.setPackageName(packageInfo.packageName);
					versionInfo.setVersionName(packageInfo.versionName);
					versionInfo.setVersionCode(packageInfo.versionCode);
					versionInfo.setAppIcon(packageInfo.applicationInfo
							.loadIcon(act.getPackageManager()));
				}
			}
		}
		return versionInfo;
	}
	
	/**
	 * 版本更新检测
	 * @param ctx
	 */
	public static void checkVersionUpdate(Activity act,
			Handler vHandler,int what) {
		if(MyApplication.NETWORK_ISCONN){
			// 检测当前版本
			MyApplication.localVersion = checkVersion(act);
			new GetViesionThread(vHandler,what).start();
		}else {
			//Toast.makeText(act, "网络异常", Toast.LENGTH_SHORT).show();
		}
	}
	/**
	 * 得服务器最新版本信息
	 * @author myheat
	 * 
	 */
	static class GetViesionThread extends Thread {
		Handler vHandler;
		int what;

		public GetViesionThread(Handler vHandler,int what) {
			this.vHandler = vHandler;
			this.what = what;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			Looper.prepare();
//			try {
//				String response = Http
//						.getText(Constant.appVersion);
//
//				Log.d("downloadNewVersionXML==", "" + response);
//				if (null != response && !"".equals(response)) {
//
//					JSONObject jsonObject = new JSONObject(response);
//					String status = jsonObject.getString("status");
//
//					if (Constant.Net.JSON_STATUS_200.equals(status)) {// 获取版本信息成功
//						if(jsonObject.isNull("ver")){
//							return ;
//						};
//						JSONObject jsonObject1 = jsonObject.getJSONObject("ver");
//
//						String versionName = jsonObject1
//								.getString("vname");
//						String versionIntroduce = jsonObject1
//								.getString("content");
//						String versionNumber = jsonObject1
//								.getString("code");
//						String versionPath = jsonObject1
//								.getString("downurl");
//
//						MyApplication.newVersion.setVersionName(versionName);
//						MyApplication.newVersion.setVersionCode(Integer
//								.valueOf(versionNumber));
//						MyApplication.newVersion
//								.setVersionUpContent(versionIntroduce);
//						MyApplication.newVersion
//								.setAppPath(Constant.Net.HOST_NAME + versionPath);
//
//						if (MyApplication.newVersion.getVersionCode() > MyApplication.localVersion
//								.getVersionCode()) {
//							MyApplication.isUpdateVersion = true;
//						} else {
//							MyApplication.isUpdateVersion = false;
//						}
//						if(vHandler != null){
//							vHandler.sendEmptyMessage(what);
//						}
//					}
//				}
//
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				return;
//			}
		}
	}
}
