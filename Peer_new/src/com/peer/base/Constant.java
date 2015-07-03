package com.peer.base;

import com.peer.utils.SDCardScanner;

/**
 * 定义全局属性接口 有些属性是可能已作废。
 */
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

	/* homepage recommend user or topic */
	public static String USER = "user";
	public static String TOPIC = "topic";
	public static String TIME = "time";

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
	public static String LABELUSER = "labeluser"; // 标签查标签（模糊查询）
	public static String LABELTOPIC = "topicuser";

	public static String TOPICBYLABEL = "topiclist"; // 精确查询
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

	// 用于进入聊天室intent.putExtras的键
	public static String IMAGE = "image";
	public static String OWNERNIKE = "ownernike";
	public static String THEME = "theme";
	public static String TAGNAME = "tagname";
	public static String ROOMID = "roomid";
	public static String TOPICID = "topicid";
	public static String FROMFLOAT = "float";
	// 全局配置
	public static String CAN_UPGRADE_SILENTLY = "can_upgrade_silently";
	public static String CAN_LOGIN = "can_login";
	public static String CAN_REGISTER_USER = "can_register_user";

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
	public static String S_SCREEN_WIDHT = "swidth";
	public static String S_SCREEN_WIDHT_VALUE = "480";

	/**
	 * 项目存储根目录
	 */
	public static final String DEFAULT_MAIN_DIRECTORY = SDCARD_DIR + "/peer/";

	/** 文件在本地的缓存目录 **/
	public static String C_FILE_CACHE_PATH = DEFAULT_MAIN_DIRECTORY
			+ "filecache/";
	/** 服务器地址 **/
	public static String SERVER_ADDRESS = "http://192.168.23.1:8080/tonghang-serverapi";
	//http://www.tonghang1.com:3000
	//http://192.168.23.1:8080/tonghang-serverapi
	//http://192.168.1.2:8080/tonghang-serverapi
	/** 登录请求地址 **/
	public static String LONIN_IN_URL = SERVER_ADDRESS + "/user/login.json";
	/** 基本注册请求地址 **/
	public static String REGISTTAG_IN_URL = SERVER_ADDRESS + "/user/regist.json";
	/** 修改用户信息请求地址 **/
	public static String UPDATE_IN_URL = SERVER_ADDRESS + "/user/update/{client_id}.json";
	/** 忘记密码请求地址 **/
	public static String FORGET_IN_URL = SERVER_ADDRESS + "/user/forget_password.json";
	/** 首页用户推荐请求地址 **/
	public static String USER_RECOMMEND_IN_URL = SERVER_ADDRESS + "/user/recommend.json";
	/** 相应标签下的用户请求地址 **/
	public static String LABEL_IN_URL = SERVER_ADDRESS + "/user/search/label.json";
	/** 参与话题的用户请求地址 **/
	public static String NUMBER_IN_URL = SERVER_ADDRESS + "/topic/number.json";
	/** 按行业搜用户请求地址 **/
	public static String SEARCH_USER_LABEL_URL = SERVER_ADDRESS + "/user/search/label.json";
	/** 按昵称搜用户请求地址 **/
	public static String SEARCH_USER_NICK_URL = SERVER_ADDRESS + "/user/search/nick.json";
	/** 修改用户密码请求地址 **/
	public static String UPDATE_PWD_IN_URL = SERVER_ADDRESS + "/user/update_pwd/{client_id}.json";
	/** 查看用户信息请求地址 **/
	public static String USER_IN_URL = SERVER_ADDRESS + "/user/{client_id}.json";
	/** 设置指定用户标签请求地址 **/
	public static String USER_UPDATE_LABEL_IN_URL = SERVER_ADDRESS + "/user/update_label/{client_id}.json";
	/** 话题推荐请求地址 **/
	public static String TOPIC_RECOMMEND_IN_URL = SERVER_ADDRESS + "/topic/recommend.json";
	/** 按行业搜话题请求地址 **/
	public static String SEARCH_TOPIC_LABEL_URL = SERVER_ADDRESS + "/topic/search/label.json";
	/** 按话题关键字搜话题请求地址 **/
	public static String SEARCH_TOPIC_SUBJECT_URL = SERVER_ADDRESS + "/topic/search/subject.json";
	/** 查看指定用户的话题列表请求地址 **/
	public static String USER_TOPIC_IN_URL = SERVER_ADDRESS + "/user/topic.json";
	/** 加入话题群聊请求地址 **/
	public static String JOIN_TOPIC_IN_URL = SERVER_ADDRESS + "/topic/join.json";
	/** 退出话题群聊请求地址 **/
	public static String LEAVE_TOPIC_IN_URL = SERVER_ADDRESS + "/topic/leave.json";
	/** 发表话题请求地址 **/
	public static String TOPIC_CREATE_URL = SERVER_ADDRESS + "/topic/create.json";
	/** 提交反馈信息请求地址 **/
	public static String SYSTEM_FEEDBACK_URL = SERVER_ADDRESS + "/system/feedback.json";
	/** 获取服务器端系统时间和相应参数请求地址 **/
	public static String SYSTEM_CONFIG_URL = SERVER_ADDRESS + "/system/system_config.json";
	/** 得到某人的好友申请列表请求地址 **/
	public static String FRIEND_INVITATION_URL = SERVER_ADDRESS + "/friend/invitation/{client_id}.json";
	/** 请求加好友地址 **/
	public static String FRIEND_ADD_URL = SERVER_ADDRESS + "/friend/add/{client_id}.json";
	/** 同意/拒绝加好友请求地址 **/
	public static String FRIEND_STATUS_URL = SERVER_ADDRESS + "/friend/{status}/{inviter_id}.json";
	/** 删除好友请求地址 **/
	public static String FRIEND_DELETE_URL = SERVER_ADDRESS + "/friend/delete/{client_id}.json";
	/** 得到某人的好友列表请求地址 **/
	public static String FRIEND_GET_URL = SERVER_ADDRESS + "/friend/get/{client_id}.json";
	
	/** 模拟client_id **/
	public static String CLIENT_ID = "44393c565b15e21df03336b8f4a82eff713c9a95";

}
