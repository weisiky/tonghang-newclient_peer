package com.peer.base;

import com.peer.utils.SDCardScanner;


public class Constant {
	public static int REFRESHHANDLE = 321;

	public static String AGREED = "agreed";

	public static String PENDING = "pending";

	public static String ISFLOAT = "isfloat";

	/* LocalStorage key */
	public static String EMAIL = "email";

	public static String LOGINED = "logined";
	public static String LOGOUT = "logout";

	/* Share key */
	public static String WX_APP_ID = "wxe2461f1b754deb94";

	

	/* chatroom type */
	public static int SINGLECHAT = 1;
	public static int MULTICHAT = 2;
	/* msg from */
	public static int OTHER = 0;
	public static int SELF = 1;
	/* login status */
	public static String LOGINSUCCESS = "success";
	public static String LOGINFAIL = "fail";
	/* search type */
	public static String LABELUSER = "labeluser"; 
	public static String LABELTOPIC = "topicuser";

	public static String TOPICBYLABEL = "topiclist"; 
	public static String TOPICBYTOPIC = "topicbytopic";
	public static String USERBYNIKE = "userbynike";
	public static String USERBYLABEL = "userbylabel";
	// public String TOPICLIST="topiclist";
	/* search int type */

	// public static String SERVER_ADDRESS = "www.tonghang1.com:3000";
	// public static String SERVER_ADDRESS =
	// "114.215.143.83:3000      192.168.23.1:8080/tonghang-web";

	public static int WEB_SERVER_PORT = 3000;
	/* connection status */
	public static String CONNECTION = "connection";
	public static String DISCONNECTION = "disconnection";
	/* imageurl */
	public static String IMAGEURL = "imageurl";
	public static String USERID = "userid";
	public static String NICKNAME = "nickname";
	/* callback status */
	public static String CALLBACKSUCCESS = "success";
	public static String CALLBACKFAIL = "fail";
	/* restart app */
	public static String RELOGIN = "relogin";

	
	public static String IMAGE = "image";
	public static String OWNERNIKE = "ownernike";
	public static String THEME = "theme";
	public static String TAGNAME = "tagname";
	public static String ROOMID = "roomid";
	public static String TOPICID = "topicid";
	public static String FROMFLOAT = "float";
	
	public static String CAN_UPGRADE_SILENTLY = "can_upgrade_silently";
	public static String CAN_LOGIN = "can_login";
	public static String CAN_REGISTER_USER = "can_register_user";
	/** 获取SD */
	public static final String SDCARD_DIR = SDCardScanner.getExtSDCardPath();
	/** SharedPreferences文件名称 */
	public static final String SHARE_NAME = "peer";

	/** 屏幕跨度 */
	public static String S_SCREEN_WIDHT = "swidth";
	public static String S_SCREEN_WIDHT_VALUE = "480";
	/** 软件默认存储路径 */
	public static final String DEFAULT_MAIN_DIRECTORY = SDCARD_DIR + "/peer/";
	/** 文件缓存目录 **/
	public static String C_FILE_CACHE_PATH = DEFAULT_MAIN_DIRECTORY
			+ "filecache/";
	/** 图片缓存目录 **/
	public static String C_IMAGE_CACHE_PATH = DEFAULT_MAIN_DIRECTORY
			+ "imagecache/";
	/** 客户端client_id **/
	public static String CLIENT_ID = "44393c565b15e21df03336b8f4a82eff713c9a95";
	
	/** 用户信息 **/
		public static String USER = "user";
		public static String ID = "id";
		public static String SEX = "sex";
		public static String USERNAME = "username";
		public static String PHONE = "phone";
		public static String BIRTH = "birth";
		public static String USER_IMAGE = "image";
		public static String CREATED_AT = "created_at";
		public static String CITY = "city";
		public static String TOPIC = "topic";
		public static String TIME = "time";
		public static String PIC_SERVER = "pic_server";
		public static String SYS_TIME = "sys_time";
		public static String LABELS = "Labels";

}
