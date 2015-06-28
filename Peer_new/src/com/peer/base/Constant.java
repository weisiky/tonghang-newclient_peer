package com.peer.base;

import java.util.HashSet;

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

	/** 文件在本地的缓存目录 **/
	public static String C_FILE_CACHE_PATH = DEFAULT_MAIN_DIRECTORY
			+ "filecache/";
	/** 服务器地址 **/
	public static String SERVER_ADDRESS = "www.baidu.com";
//	public static String SERVER_ADDRESS = "http://127.0.0.1:8080/tonghang-serverapi";
	/** 登录请求地址 **/
	public static String LONIN_IN_URL = SERVER_ADDRESS + "/user/login.json";

}
