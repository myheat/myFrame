package com.myheat.frame.tool;

import java.util.Collection;
import java.util.Map;

/** 
 * 对象非空判断
 * @author myheat
 *   
 */
public class TextUtil {
	/**
	 * 
	 * @param content
	 * @return
	 */
	public static boolean isValidate(String content){
		return content != null && !"".equals(content.trim());
	}
	/**
	 * 
	 * @param list
	 * @return
	 */
	public static boolean isValidate(Collection<?> list){
		return list != null && list.size() > 0;
	}
	/**
	 * 
	 * @param map
	 * @return
	 */
	public static boolean isValidate(Map<?, ?> map) {
		if (map != null && map.size() > 0) {
			return true;
		}
		return false;
	}
}
