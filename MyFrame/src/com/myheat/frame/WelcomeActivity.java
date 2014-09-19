package com.myheat.frame;

import java.util.List;

import com.myheat.frame.tool.DebugLog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

/**
 * @author myheat
 * @version 2014年9月17日上午10:24:04
 */
public class WelcomeActivity extends Activity {

	private AlphaAnimation start_anima;
	View view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		view = View.inflate(this, R.layout.welcome, null);
		setContentView(view);
		initView();
		initData();
	}

	private void initData() {
		start_anima = new AlphaAnimation(0.3f, 1.0f);
		start_anima.setDuration(2000);
		view.startAnimation(start_anima);
		start_anima.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				redirectTo();
			}
		});
	}

	private void initView() {
		
		if(!hasShortcut()){
			addShortcutToDesktop();
		}
	}

	private void redirectTo() {
		startActivity(new Intent(getApplicationContext(), MainActivity.class));
		finish();
	}

	/**
	 * 添加桌面快捷方式
	 */
	private void addShortcutToDesktop() {

		Intent shortcut = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		// 不允许重建
		shortcut.putExtra("duplicate", false);
		// 设置名字
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				this.getString(R.string.app_name));
		// 设置图标
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
				Intent.ShortcutIconResource.fromContext(this,
						R.drawable.ic_launcher));
		// 设置意图和快捷方式关联程序
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT,
				new Intent(this, this.getClass()).setAction(Intent.ACTION_MAIN));
		// 发送广播
		sendBroadcast(shortcut);

	}

	/**
	 * 判断是否已经存在快捷方式
	 * 
	 * @return
	 */
	private boolean hasShortcut() {
		final String AUTHORITY = getAuthorityFromPermission(
				getApplicationContext(),
				"com.android.launcher.permission.READ_SETTINGS");
		final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
				+ "/favorites?notify=true");
		Cursor c = getContentResolver().query(CONTENT_URI,
				new String[] { "title" }, "title=?", new String[] { this.getString(R.string.app_name) },
				null);
		if (c != null && c.moveToNext()) {
			return true;//存在
		}
		return false;
	}

	/*
	 * 通过Context的getContentResolve获取系统的共享数据，在共享数据中，查找指定Uri的数据，也即launcher中的favorite表
	 * ，也即快捷方式表。
	 * 然后query的参数是筛选表的title列，并且title=“test”的行数据，如果查询有结果，表示快捷方式名为test的存在
	 * ，也即返回快捷方式已经添加到桌面。 那么如果你时间了，如果你的测试机是MIUI，或者HTC等rom，你会发现c为null，怎么回事，解释下
	 * android系统桌面的基本信息由一个launcher
	 * .db的Sqlite数据库管理，里面有三张表，其中一张表就是favorites。这个db文件一般放在data
	 * /data/com.android.launcher
	 * (launcher2)文件的databases下。但是对于不同的rom会放在不同的地方，例如MIUI放在data
	 * /data/com.miui.home
	 * /databases下面，htc放在data/data/com.htc.launcher/databases下面
	 * ，那么如何用程序来找到这个认证标示呢
	 */
	private String getAuthorityFromPermission(Context context, String permission) {
		if (TextUtils.isEmpty(permission)) {
			return null;
		}
		List<PackageInfo> packs = context.getPackageManager()
				.getInstalledPackages(PackageManager.GET_PROVIDERS);
		if (packs == null) {
			return null;
		}
		for (PackageInfo pack : packs) {
			ProviderInfo[] providers = pack.providers;
			if (providers != null) {
				for (ProviderInfo provider : providers) {
					if (permission.equals(provider.readPermission)
							|| permission.equals(provider.writePermission)) {
						return provider.authority;
					}
				}
			}
		}
		return null;
	}
}
