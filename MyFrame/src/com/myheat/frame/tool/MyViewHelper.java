package com.myheat.frame.tool;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

public class MyViewHelper {

	private static ProgressDialog pd;

	public static void showPD(Context context) {
		pd = new ProgressDialog(context);
		pd.setTitle("提示：");
		pd.setMessage("正在加载数据，请稍等！");
		pd.show();
	}

	public static void dismissPD() {
		if (pd != null) {
			pd.dismiss();
		}
	}

	public static void showToast(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}

	public static void showFailToast(Context context) {
		showToast(context, "获取数据失败！");
	}
}
