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
	
	
	public static int REFRESHHANDLE=321;
	
	public static String AGREED="agreed";
	
	public static String PENDING="pending";
	
	public static String ISFLOAT="isfloat";
		
	/*LocalStorage key*/
	public static String EMAIL="email";	
	
	public static String LOGINED="logined";
	public static String LOGOUT="logout";
	
	/*Share key*/
	public static String WX_APP_ID = "wxe2461f1b754deb94";
	
	
	/*homepage recommend user or topic*/
	public static String USER="user";
	public static String TOPIC="topic";
	public static String TIME="time";
	
	/*chatroom type*/
	public static int SINGLECHAT=1;
	public static int MULTICHAT=2;
	/*msg from*/
	public static int OTHER=0;
	public static int SELF=1;
	/*login status*/
	public static String LOGINSUCCESS="success";
	public static String LOGINFAIL="fail";
	/*search type*/
	public static String LABELUSER="labeluser";      //标签查标签（模糊查询）
	public static String LABELTOPIC="topicuser";   
	
	public static String TOPICBYLABEL="topiclist";    //精确查询
	public static String TOPICBYTOPIC="topicbytopic";
	public static String USERBYNIKE="userbynike";
	public static String USERBYLABEL="userbylabel";
//	public String TOPICLIST="topiclist";
	/*search int type*/
	public static int TOPIC_LABEL=1;
	public static int TOPIC_TOPICKEY=2;
	public static int USER_LABEL=3;
	public static int USER_NICK=4;
	
	/*web server*/
	public static String SERVER_ADDRESS = "192.168.23.1:8080/tonghang-web";
//	public static String SERVER_ADDRESS = "www.tonghang1.com:3000";
//	public static String SERVER_ADDRESS = "114.215.143.83:3000      192.168.23.1:8080/tonghang-web";
	
	public static int WEB_SERVER_PORT = 3000;
	public static String WEB_SERVER_ADDRESS="http://" + SERVER_ADDRESS;
	 /*connection status*/
	public static String CONNECTION="connection";
	public static String DISCONNECTION="disconnection";
	 /*imageurl*/
	public static String IMAGEURL="imageurl";
	public static String USERID="userid";
	public static String NICKNAME="nickname";
	 /*callback status*/
	public static String CALLBACKSUCCESS="success";
	public static String CALLBACKFAIL="fail"; 
	 /*restart app*/
	public static String RELOGIN="relogin";
	 
	//用于进入聊天室intent.putExtras的键
	public static String IMAGE="image";
	public static String OWNERNIKE="ownernike";
	public static String THEME="theme";
	public static String TAGNAME="tagname";
	public static String ROOMID="roomid";
	public static String TOPICID="topicid";
	public static String FROMFLOAT="float";
	//全局配置
	public static String CAN_UPGRADE_SILENTLY="can_upgrade_silently";
	public static String CAN_LOGIN="can_login";
	public static String CAN_REGISTER_USER="can_register_user";

}
