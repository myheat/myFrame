package com.myheat.frame.common;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.myheat.frame.tool.TextUtil;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

/**
 * 公共方法
 * 
 * @author myheat
 * 
 */
public class Common {

	private static String hexString = "0123456789ABCDEF";

	/**
	 * 16进制加密
	 * 
	 * @param bytes
	 * @return
	 */
	public static String bytes2HexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
			sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
		}
		return sb.toString();

	}

	/**
	 * 使用sha1算法对字符串进行加密，返回加密后的字符串
	 * 
	 * @param str
	 *            原始字符串
	 * @param prefix
	 *            前缀
	 * @return 加密后的字符串
	 */
	public static String sha1(String str, String prefix) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			prefix = (prefix != null) ? prefix : "";
			md.update((prefix + str).getBytes("utf-8"));
			byte[] out = md.digest();
			StringBuilder buf = new StringBuilder();
			char HEX[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
					'A', 'B', 'C', 'D', 'E', 'F' };
			for (int i = 0; i < out.length; i++) {
				byte b = out[i];
				buf.append(HEX[(b >> 4) & 0x0F]);
				buf.append(HEX[b & 0x0F]);
			}
			return buf.toString();
		} catch (Exception ex) {
			return "";
		}
	}

	/**
	 * MD5加密，32位
	 * 
	 * @param str
	 * @return
	 */
	public static String MD5(String str) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

		char[] charArray = str.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		byte[] md5Bytes = md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	/**
	 * 可逆的加密算法
	 * 
	 * @param str
	 * @return
	 */
	public static String encryptmd5(String str) {
		char[] a = str.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 'l');
		}
		String s = new String(a);
		return s;
	}

	/**
	 * 正则邮箱
	 * 
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email) {
		String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(email.trim());
		boolean isMatched = matcher.matches();
		return isMatched;
	}

	/**
	 * 密码由6-16个字符组成，可使用数字、英文字母、下划线 区分大小
	 * 
	 * @param string
	 * @return
	 */
	public static boolean checkString(String string) {
		String check = "^[0-9A-Za-z_]{6,20}$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(string.trim());
		boolean isMatched = matcher.matches();
		return isMatched;
	}

	/**
	 * 判断是否全是数字
	 * 
	 * @param str
	 * @return
	 */
	public boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 正则手机号码
	 */
	public static boolean isMobileNO(String mobiles) {

		String NSString = "";
		/**
		 * 手机号码 移动：134[0-8],135,136,137,138,139,150,151,157,158,159,182,187,188
		 * 联通：130,131,132,152,155,156,185,186 电信：133,1349,153,180,189
		 */

		// NSString = "^1(3[0-9]|5[0-35-9]|8[025-9])\\d{8}$";
		/**
		 * 10 * 中国移动：China Mobile 11 *
		 * 134[0-8],135,136,137,138,139,150,151,157,158,159,182,187,188 12
		 */
		// NSString = "^1(34[0-8]|(3[5-9]|5[017-9]|8[278])\\d)\\d{7}$";
		/**
		 * 15 * 中国联通：China Unicom 16 * 130,131,132,152,155,156,185,186 17
		 */
		// NSString = "^1(3[0-2]|5[256]|8[56])\\d{8}$";
		/**
		 * 20 * 中国电信：China Telecom 21 * 133,1349,153,180,189 22
		 */
		// NSString = "^1((33|53|8[09])[0-9]|349)\\d{7}$";

		/**
		 * 大陆地区固话及小灵通 区号：010,020,021,022,023,024,025,027,028,029 号码：七位或八位
		 */
		// NSString * PHS = @"^0(10|2[0-5789]|\\d{3})\\d{7,8}$";

		NSString = "^[1]([0-9][0-9]{1}|59|58|88|89)[0-9]{8}$";

		Pattern p = Pattern.compile(NSString);
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 检查字符串 多少位
	 * 
	 * @param string
	 * @param num
	 * @return
	 */
	public static boolean checkString(String string, int num) {
		String check = "^[a-zA-Z0-9][a-zA-Z0-9\\.\\-@]{0,15}$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(string.trim());
		boolean isMatched = matcher.matches();
		return isMatched;
	}

	/**
	 * 文件名 1到15位
	 */
	public static boolean checkFileName(String userName) {
		String check = "[-A-Za-z0-9]{1,15}$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(userName.trim());
		boolean isMatched = matcher.matches();
		return isMatched;
	}

	/**
	 * 判断密码 是否相等
	 * 
	 * @param s1
	 * @param s2
	 * @return 
	 * 0 字符串不能为空 
	 * 1 s1 密码不能为空
	 * 2 s2确认密码不能为空 
	 * 3相等 
	 * 4密码不相等
	 */
	public static int checkPasswordSure(String s1, String s2) {

		if (s1 == null || s2 == null)
			return 0;

		if (TextUtil.isValidate(s1)) {
			return 1;
		} else if (TextUtil.isValidate(s2)) {
			return 2;
		}

		if (s1.trim().equals(s2.trim())) {
			return 3;
		} else {
			return 4;
		}
	}

	/**
	 * 转化时间格式yyyy-MM-dd HH:mm
	 * 
	 * @param time
	 * @return
	 */
	public static String formatTimeHHmm(String time) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String str = formatter.format(Long.valueOf(time) * 1000);
		return str;
	}

	/**
	 * 转化时间格式yyyy年MM月dd日
	 * 
	 * @param time
	 * @return
	 */
	public static String formatTimeY(String time) {
		if (TextUtil.isValidate(time)) {
			return "-";
		}
		String[] times = time.split("-");
		for (int i = 0; i < times.length; i++) {
			if (i == 0) {
				time = times[i] + "年";
			} else if (i == 1) {
				time = time + times[i] + "月";
			} else if (i == 2) {
				time = time + times[i] + "日";
			}
		}
		return time;
	}
	
    /** 得到圆周*/
	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}
    /** 地球半径 */
	private static double EARTH_RADIUS = 6378137.0;
	/**
	 * 计算经纬度距离
	 * 
	 * @param lng1
	 * @param lng2
	 * @param lat1
	 * @param lat2
	 * @return
	 */
	public static double distanceBetween(String lng1, String lng2, String lat1,
			String lat2) {
		try {
			double radLat1 = rad(Double.valueOf(lat1));
			double radLat2 = rad(Double.valueOf(lat2));
			double difLat = radLat1 - radLat2;
			double difLng = rad(Double.valueOf(lng1))
					- rad(Double.valueOf(lng2));
			double s = 2 * Math
					.asin(Math.sqrt(Math.pow(Math.sin(difLat / 2), 2)
							+ Math.cos(radLat1) * Math.cos(radLat2)
							* Math.pow(Math.sin(difLng / 2), 2)));
			s = s * EARTH_RADIUS;
			// 方式一
			BigDecimal b = new BigDecimal(s / 1000);
			// 四舍五入,保留两个小数.
			int saveBitNum = 2;
			double c = b.setScale(saveBitNum, BigDecimal.ROUND_HALF_UP)
					.doubleValue();
			// Log.e("distanceBetween=", "" + c);
			return c;
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}

	}

	/**
	 * 转换数据单位 B KB MB GB
	 * 
	 * @param size
	 * @return
	 */
	public static String KBtoMB(Float size) {
		DecimalFormat df = new DecimalFormat("###.##");
		float f = 0;
		if (size < 1024) {
			return (df.format(new Float(f).doubleValue()) + "B");
		} else if (size < 1024 * 1024) {
			f = size / 1024;
			return (df.format(new Float(f).doubleValue()) + "KB");
		} else if (size < 1024 * 1024 * 1024) {
			f = size / (1024 * 1024);
			return (df.format(new Float(f).doubleValue()) + "MB");
		} else {
			f = size / (1024 * 1024 * 1024);
			return (df.format(new Float(f).doubleValue()) + "GB");
		}
	}

	/**
	 * 根据字符串，转换年化收益率，保留1位小数
	 * 
	 * @return
	 */
	public static String getFloatRate(String rateString) {

		if (TextUtil.isValidate(rateString)) {
			return "0";
		}
		DecimalFormat df = new DecimalFormat("#.0 ");

		// return df.format(Float.valueOf(rateString) * 100) + "%";
		return df.format(Float.valueOf(rateString)) + "%";
	}

	/**
	 * <ScrollView嵌套ListView问题>
	 */
	public static void setListViewHeightToSaleProduct(ListView listView,
			final BaseAdapter adapter) {
		if (adapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < adapter.getCount(); i++) {
			View listItem = adapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (adapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	/** 去掉重复string */
	public static List<String> removerepeatedchar(String s) {
		if (s == null)
			return null;
		List<String> groups = new ArrayList<String>();
		int i = 0, len = s.length();
		while (i < len) {
			char c = s.charAt(i);
			groups.add(String.valueOf(c));
			i++;
			while (i < len && s.charAt(i) == c) {// 这个是如果这两个值相等，就让i+1取下一个元素
				i++;
			}
		}
		return groups;
	}
}
