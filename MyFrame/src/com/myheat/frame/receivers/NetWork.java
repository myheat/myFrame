package com.myheat.frame.receivers;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import com.myheat.frame.MyApplication;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * 网络相关操作
 * 
 * @author myheat
 *
 */
public class NetWork {

	/**
	 * 检查网络是否开启
	 * 
	 * @param context
	 */
	public static boolean isNetworkAvailable(Context context) {
		boolean isAvailable = false;
		// 获得网络系统连接服务
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// 获取MOBILE状态
		State state = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		if (State.CONNECTED == state) {
			isAvailable = true;
		}
		// 获得WIFI状态
		state = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		if (State.CONNECTED == state) {
			isAvailable = true;
		}
		MyApplication.NETWORK_ISCONN = isAvailable;
		if (!isAvailable) {
			Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
		}

		if (is2G(context) && isAvailable == true) {
			Toast.makeText(context, "当前2G网络，数据加载可能会缓慢", Toast.LENGTH_SHORT)
					.show();
		}
		;

		return isAvailable;
		// 跳转到MOBILE网络设置界面
		// mAct.startActivity(new
		// Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
		// 跳转到无线wifi网络设置界面
		// mAct.startActivity(new
		// Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
	}

	/**
	 * 判断当前网络是否是3G网络
	 * 
	 * @param context
	 * @return boolean
	 */
	public static boolean is3G(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
			return true;
		}
		return false;
	}

	/**
	 * 判断当前网络是否是wifi网络
	 * 
	 * @param context
	 * @return boolean
	 */
	public static boolean isWifi(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	/**
	 * 判断当前网络是否是2G网络
	 * 
	 * @param context
	 * @return boolean
	 */
	public static boolean is2G(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& (activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_EDGE
						|| activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_GPRS || activeNetInfo
						.getSubtype() == TelephonyManager.NETWORK_TYPE_CDMA)) {
			return true;
		}
		return false;
	}

	/**
	 * wifi是否打开
	 */
	public static boolean isWifiEnabled(Context context) {
		ConnectivityManager mgrConn = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mgrTel = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return ((mgrConn.getActiveNetworkInfo() != null && mgrConn
				.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
				.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
	}

	/**
	 * 获得本机ip地址
	 * 
	 * @return
	 */
	public static String GetHostIp() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> ipAddr = intf.getInetAddresses(); ipAddr
						.hasMoreElements();) {
					InetAddress inetAddress = ipAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress();
					}
				}
			}
		} catch (SocketException ex) {
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 获取本机串号imei
	 * 
	 * @param context
	 * @return
	 */
	public static String getIMEI(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}

	public static String getTelephone(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(context.TELEPHONY_SERVICE);
		return telephonyManager.getLine1Number();

		/*
		 * Battery status：电池充电/未充电状态 
		 * Battery level：电池剩余电量 
		 * Phone number：手机序列号
		 * Network：所处的移动网络 
		 * Signal strength：
		 * 信号度 Network type：
		 * 网络制式 Service
		 * state：所在服务区 
		 * Roaming：漫游/未漫游 
		 * Data access：共访问的数据大小 
		 * IMEI：IMEI码 IMEI
		 * SV：IMEI码的版本 
		 * IMSI：国际移动用户识别码 Wi-Fi 
		 * Mac address：G1无线Wi-Fi网络的Mac地址。
		 * Bluetooth address：蓝牙地址 
		 * Up time：正常运行时间 
		 * Awake Time：手机唤醒时间
		 */
	}

	/**
	 * 判断是否是FastMobileNetWork，将3G或者3G以上的网络称为快速网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isFastMobileNetwork(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		switch (telephonyManager.getNetworkType()) {
		case TelephonyManager.NETWORK_TYPE_1xRTT:
			return false; // ~ 50-100 kbps
		case TelephonyManager.NETWORK_TYPE_CDMA:
			return false; // ~ 14-64 kbps
		case TelephonyManager.NETWORK_TYPE_EDGE:
			return false; // ~ 50-100 kbps
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
			return true; // ~ 400-1000 kbps
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
			return true; // ~ 600-1400 kbps
		case TelephonyManager.NETWORK_TYPE_GPRS:
			return false; // ~ 100 kbps
		case TelephonyManager.NETWORK_TYPE_HSDPA:
			return true; // ~ 2-14 Mbps
		case TelephonyManager.NETWORK_TYPE_HSPA:
			return true; // ~ 700-1700 kbps
		case TelephonyManager.NETWORK_TYPE_HSUPA:
			return true; // ~ 1-23 Mbps
		case TelephonyManager.NETWORK_TYPE_UMTS:
			return true; // ~ 400-7000 kbps
			// case TelephonyManager.NETWORK_TYPE_EHRPD:
			// return true; // ~ 1-2 Mbps
		case TelephonyManager.NETWORK_TYPE_EVDO_B:
			return true; // ~ 5 Mbps
			// case TelephonyManager.NETWORK_TYPE_HSPAP:
			// return true; // ~ 10-20 Mbps
		case TelephonyManager.NETWORK_TYPE_IDEN:
			return false; // ~25 kbps
			// case TelephonyManager.NETWORK_TYPE_LTE:
			// return true; // ~ 10+ Mbps
		case TelephonyManager.NETWORK_TYPE_UNKNOWN:
			return false;
		default:
			return false;
		}
	}

	/** 没有网络 */
	public static final int NETWORKTYPE_INVALID = 0x000;
	/** wap网络 */
	public static final int NETWORKTYPE_WAP = 0x001;
	/** 2G网络 */
	public static final int NETWORKTYPE_2G = 0x002;
	/** 3G和3G以上网络，或统称为快速网络 */
	public static final int NETWORKTYPE_3G = 0x003;
	/** wifi网络 */
	public static final int NETWORKTYPE_WIFI = 0x004;

	/**
	 * 获取网络状态，wifi,wap,2g,3g
	 * 
	 * @param context
	 *            上下文
	 * @return int 网络状态 {@link #NETWORKTYPE_2G}, {@link #NETWORKTYPE_3G},
	 *         {@link #NETWORKTYPE_INVALID} {@link #NETWORKTYPE_WAP}
	 *         {@link #NETWORKTYPE_WIFI}
	 */
	public static int getNetWorkType(Context context) {
		int mNetWorkType = NETWORKTYPE_INVALID;
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()) {
			String type = networkInfo.getTypeName();

			if (type.equalsIgnoreCase("WIFI")) {
				mNetWorkType = NETWORKTYPE_WIFI;
			} else if (type.equalsIgnoreCase("MOBILE")) {
				String proxyHost = android.net.Proxy.getDefaultHost();

				mNetWorkType = TextUtils.isEmpty(proxyHost) ? (isFastMobileNetwork(context) ? NETWORKTYPE_3G
						: NETWORKTYPE_2G)
						: NETWORKTYPE_WAP;
			}
		} else {
			mNetWorkType = NETWORKTYPE_INVALID;
		}

		return mNetWorkType;
	}

}
