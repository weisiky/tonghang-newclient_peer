package com.peer.base;

import java.util.HashSet;

import com.peer.utils.SDCardScanner;

/**
 * Ӧ�þ�̬�����洢
 * 
 * @author zhangzg
 * 
 */

public class Constant {

	/**
	 * SD��·��
	 */
	public static final String SDCARD_DIR = SDCardScanner.getExtSDCardPath();
	/**
	 * SharedPreferences����
	 */
	public static final String SHARE_NAME = "peer";

	/**
	 * �ֻ���Ļ�Ŀ�
	 */
	public final static String S_SCREEN_WIDHT = "swidth";
	public static String S_SCREEN_WIDHT_VALUE = "480";

	/**
	 * ��Ŀ�洢��Ŀ¼
	 */
	public static final String DEFAULT_MAIN_DIRECTORY = SDCARD_DIR + "/peer/";
	
	/** �ļ��ڱ��صĻ���Ŀ¼ **/
	public static String C_FILE_CACHE_PATH = DEFAULT_MAIN_DIRECTORY+"filecache/";

}
