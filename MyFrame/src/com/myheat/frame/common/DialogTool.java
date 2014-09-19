package com.myheat.frame.common;

import java.util.ArrayList;

import com.myheat.frame.MyApplication;
import com.myheat.frame.R;
import com.myheat.frame.services.MyService;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DialogTool {

	/**
	 * 公共信息提示对话
	 * 
	 * @param activity
	 * @param msg
	 * @param title
	 */
	public static void showDialog(Activity act, String msg, String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(act);
		builder.setMessage(msg)
				.setTitle(title)
				.setCancelable(false)
				.setPositiveButton(act.getResources().getString(R.string.sure),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								
								
							}
						})
				.setNegativeButton(
						act.getResources().getString(R.string.cancel),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * 公共信息提示对话
	 * 
	 * @param act
	 * @param msg
	 */
	public static void showDialogNoTitle(Activity act, String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(act);
		builder.setMessage(msg)
				.setCancelable(false)
				.setPositiveButton(act.getResources().getString(R.string.sure),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						})
				.setNegativeButton(
						act.getResources().getString(R.string.cancel),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * 应用 版本更新提示对话
	 * 
	 * @param activity
	 * @param msg
	 * @param apkName
	 * @param apkPath
	 */
	public static void showDialogNewVersion(final Activity act) {
		AlertDialog.Builder builder = new AlertDialog.Builder(act);
		builder.setMessage(MyApplication.newVersion.getVersionUpdataContent())
				.setTitle(
						act.getResources().getString(R.string.tell_update)
								+ MyApplication.newVersion.getVersionName())
				.setCancelable(false)
				.setPositiveButton(
						act.getResources().getString(R.string.app_update),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent();
								intent.setClass(act, MyService.class);
								act.startService(intent);
							}
						})
				.setNegativeButton(
						act.getResources().getString(R.string.next_update),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
							}
						});
		AlertDialog alert = builder.create();
		alert.setCancelable(true);
		alert.show();
	}

	/**
	 * 弹出输入--内容文本?
	 * 
	 * @param tx
	 * @param title
	 */
	public static void dialogEditText(Context ctx, final TextView tx,
			String title) {
		final EditText editText = new EditText(ctx);
		editText.setText(tx.getText().toString());
		new AlertDialog.Builder(ctx).setTitle(title).setView(editText)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						tx.setText(editText.getText().toString());
					}

				}).setNegativeButton("取消", null).show();
	}

	// ------------------------------------------分享标识
	/** 新浪微博 */
	public static final int SINA_SHARE = 0X9010;
	/** QQ空间 */
	public static final int QQ_ROOM_SHARE = 0X9021;
	/** 腾讯微博 */
	public static final int Tencent_SHARE = 0x9022;
	/** 微信朋友圈 */
	public static final int WEIXIN_FRIEND_SHARE = 0x9031;
	/** 微信好友 */
	public static final int WEIXIN_SHARE = 0X9032;
	/** 短信 */
	public static final int MSG_SHARE = 0X9041;
	/** 人人网 */
	public static final int RENREN_SHARE = 0x9054;

//	/**
//	 * 打开分享弹出界面
//	 * @param ctx
//	 * 
//	 */
//	public static void openShareDialog(final Context ctx,
//			final Activity act,
//			final Handler handler, final JokeInfo jokeInfo) {
//		final ArrayList<ShareItem> shares = new ArrayList<ShareItem>();
//
//		shares.add(new ShareItem("新浪", R.drawable.share_sina_style,SINA_SHARE));
//		shares.add(new ShareItem("QQ空间", R.drawable.share_qzone_style,QQ_ROOM_SHARE));
//		shares.add(new ShareItem("腾讯微博", R.drawable.share_qq_weibo_style,Tencent_SHARE));
//		shares.add(new ShareItem("好友", R.drawable.share_weixin_style,WEIXIN_SHARE));
//		shares.add(new ShareItem("朋友圈", R.drawable.share_friend_style,WEIXIN_FRIEND_SHARE));
//		shares.add(new ShareItem("短信", R.drawable.share_sms_style,MSG_SHARE));
//		shares.add(new ShareItem("人人网", R.drawable.share_renren_style,RENREN_SHARE));
//
//		View menuView = View.inflate(ctx, R.layout.gridview_share, null);
//		// 创建AlertDialog
//		final ShareDialog menuDialog = new ShareDialog(ctx,R.style.ShareDialog);
//		
//		menuDialog.setContentView(menuView);
//		GridView menuGrid = (GridView) menuView
//				.findViewById(R.id.gridview_shareGrid);
//		menuGrid.setAdapter(new ShareAdapter(ctx, shares));
//		menuGrid.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1,
//					int position, long arg3) {
//				if (handler == null) {
//					DebugLog.e("DialogTool=", "你没有为handler设置值");
//					return;
//				}
//				ShareItem shareItem = shares.get(position);
//				Message msg = handler.obtainMessage();
//				msg.obj = jokeInfo;
//				switch (shareItem.getFlag()) {
//				case QQ_ROOM_SHARE:
//					QQShare.doShareToQQ(act, jokeInfo);
//					break;
//				case Tencent_SHARE:
//					TencentWeiboShare.addPic(ctx, jokeInfo);
//					break;
//				case WEIXIN_SHARE:
//					WeiXinShare.shareWebPage(ctx, jokeInfo,WeiXinShare.WEIXIN_friend);	
//					break;
//				case WEIXIN_FRIEND_SHARE:
//					WeiXinShare.shareWebPage(ctx, jokeInfo,WeiXinShare.WEIXIN_friend_Circle);
//					break;
//				case SINA_SHARE:
//					String uid = (String) ShareSPUtils.getParam(ctx,
//							ShareSPUtils.SINA_openid, "");
//					if("".equals(uid)){
//						SinaShare.mWeibo.anthorize(ctx,
//								new SinaShare.AuthListener(ctx, handler));
//					}else{
//						SinaShare.reqMsg(act, jokeInfo);
//					}
//					break;
//				case MSG_SHARE:
//					CommonLeHa.sendMsg(ctx, jokeInfo);
//					break;
//				case RENREN_SHARE:
//					long id = (Long)ShareSPUtils.getParam(ctx, ShareSPUtils.RENREN_openid, 0l);
//					 if(id != 0){
//							RenRenShare.renRenShare(act,jokeInfo);
//						}else {
//							RenRenShare.loginRenRen(act);	
//						}
//					break;
//					// 新浪授权成功
//				case SinaShare.OBTAIN_TOKEN:
//					if (jokeInfo != null) {
//						SinaShare.reqMsg(act, jokeInfo);
//					}
//					break;
//				}
//				
//				if (menuDialog != null) {
//					menuDialog.dismiss();
//				}
//			}
//		});
//		menuDialog.show();
//	}

}
