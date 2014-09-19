package com.myheat.frame;

import java.io.File;
import com.myheat.frame.tool.FileUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

/**
 * 版本更新
 * @author Administrator
 *
 */
public class NotificationActivity extends Activity {
	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		File fy = new File(
				FileUtils.SDCARDROOT
				+ FileUtils.FOLDER_BASE 
				+ getResources().getString(R.string.app_name));
		if (fy != null) {
			if (fy.exists()) {
				openFile(this, fy);
			} else {
				showDialog(getResources().getString(
						R.string.download_file_no_exist));
			}

		}

	}

	/**
	 * 定义一个提示信息显示对话框
	 * 
	 * @param activity
	 * @param msg
	 */
	public void showDialog(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msg)
				.setCancelable(false)
				.setPositiveButton(getResources().getString(R.string.sure),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								NotificationActivity.this.finish();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * 在手机上安装Android应用程序文件
	 */
	public void openFile(Activity activity, File f) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(f),
				"application/vnd.android.package-archive");
		activity.startActivity(intent);
		activity.finish();
	}
}
