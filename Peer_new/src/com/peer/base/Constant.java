package com.peer.base;


import com.peer.utils.SDCardScanner;

/**
 * 应用静态变量存储
 * 
 * @author zhangzg
 * 
 */

public class Constant {

	/**
	 * SD卡路径
	 */
	public static final String SDCARD_DIR = SDCardScanner.getExtSDCardPath();
	/**
	 * SharedPreferences名称
	 */
	public static final String SHARE_NAME = "peer";

	/**
	 * 手机屏幕的宽
	 */
	public final static String S_SCREEN_WIDHT = "swidth";
	public static String S_SCREEN_WIDHT_VALUE = "480";

	/**
	 * 项目存储根目录
	 */
	public static final String DEFAULT_MAIN_DIRECTORY = SDCARD_DIR + "/peer/";
	
	
	
}
