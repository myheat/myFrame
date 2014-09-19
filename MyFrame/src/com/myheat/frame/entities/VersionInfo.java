package com.myheat.frame.entities;

import java.io.Serializable;

import android.graphics.drawable.Drawable;
import android.util.Log;

/** 软件版本信息
 * 
 * @author renzhiqiang
 *
 */
public class VersionInfo implements Serializable {
	
	
	private static final long serialVersionUID = 5072451371903532592L;
	/** 应用名称 */
	private String appName = "";
	/** 包名 */
	private String packageName = "";
	/** 版本名 */
	private String versionName = "";
	/** 版本号 */
	private int versionCode = 0;
	/** 软件图标 */
	private Drawable appIcon = null;
	/** 应用简介 */
	private String versionIntroduction = "";
	/** 应用更新内容 */
	private String versionUpdataContent = "";
	/** app下载路径 */
	private String appPath = "";
	/** 本地保存路径 */
	private String appLocalDownLoadPath = "";
	
	private String status = "";

	public VersionInfo() {
		super();
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public Drawable getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}

	public String getVersionIntroduction() {
		return versionIntroduction;
	}

	public void setVersionIntroduction(String versionIntroduction) {
		this.versionIntroduction = versionIntroduction;
	}

	public String getVersionUpdataContent() {
		return versionUpdataContent;
	}

	public void setVersionUpdataContent(String versionUpdataContent) {
		this.versionUpdataContent = versionUpdataContent;
	}

	public String getAppPath() {
		return appPath;
	}

	public void setAppPath(String appPath) {
		this.appPath = appPath;
	}

	public String getAppLocalDownLoadPath() {
		return appLocalDownLoadPath;
	}

	public void setAppLocalDownLoadPath(String appLocalDownLoadPath) {
		this.appLocalDownLoadPath = appLocalDownLoadPath;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void print() {
		Log.v("app", "Name:" + appName + " Package:" + packageName);
		Log.v("app", "Name:" + appName + " versionName:" + versionName);
		Log.v("app", "Name:" + appName + " versionCode:" + versionCode);
	}
}
