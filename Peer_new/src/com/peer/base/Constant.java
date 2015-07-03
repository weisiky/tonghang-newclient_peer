package com.peer.base;

import com.peer.utils.SDCardScanner;

/**
 * ����ȫ�����Խӿ� ��Щ�����ǿ��������ϡ�
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
	public static String LABELUSER = "labeluser"; // ��ǩ���ǩ��ģ����ѯ��
	public static String LABELTOPIC = "topicuser";

	public static String TOPICBYLABEL = "topiclist"; // ��ȷ��ѯ
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

	// ���ڽ���������intent.putExtras�ļ�
	public static String IMAGE = "image";
	public static String OWNERNIKE = "ownernike";
	public static String THEME = "theme";
	public static String TAGNAME = "tagname";
	public static String ROOMID = "roomid";
	public static String TOPICID = "topicid";
	public static String FROMFLOAT = "float";
	// ȫ������
	public static String CAN_UPGRADE_SILENTLY = "can_upgrade_silently";
	public static String CAN_LOGIN = "can_login";
	public static String CAN_REGISTER_USER = "can_register_user";

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
	public static String S_SCREEN_WIDHT = "swidth";
	public static String S_SCREEN_WIDHT_VALUE = "480";

	/**
	 * ��Ŀ�洢��Ŀ¼
	 */
	public static final String DEFAULT_MAIN_DIRECTORY = SDCARD_DIR + "/peer/";

	/** �ļ��ڱ��صĻ���Ŀ¼ **/
	public static String C_FILE_CACHE_PATH = DEFAULT_MAIN_DIRECTORY
			+ "filecache/";
	/** ��������ַ **/
	public static String SERVER_ADDRESS = "http://192.168.23.1:8080/tonghang-serverapi";
	//http://www.tonghang1.com:3000
	//http://192.168.23.1:8080/tonghang-serverapi
	//http://192.168.1.2:8080/tonghang-serverapi
	/** ��¼�����ַ **/
	public static String LONIN_IN_URL = SERVER_ADDRESS + "/user/login.json";
	/** ����ע�������ַ **/
	public static String REGISTTAG_IN_URL = SERVER_ADDRESS + "/user/regist.json";
	/** �޸��û���Ϣ�����ַ **/
	public static String UPDATE_IN_URL = SERVER_ADDRESS + "/user/update/{client_id}.json";
	/** �������������ַ **/
	public static String FORGET_IN_URL = SERVER_ADDRESS + "/user/forget_password.json";
	/** ��ҳ�û��Ƽ������ַ **/
	public static String USER_RECOMMEND_IN_URL = SERVER_ADDRESS + "/user/recommend.json";
	/** ��Ӧ��ǩ�µ��û������ַ **/
	public static String LABEL_IN_URL = SERVER_ADDRESS + "/user/search/label.json";
	/** ���뻰����û������ַ **/
	public static String NUMBER_IN_URL = SERVER_ADDRESS + "/topic/number.json";
	/** ����ҵ���û������ַ **/
	public static String SEARCH_USER_LABEL_URL = SERVER_ADDRESS + "/user/search/label.json";
	/** ���ǳ����û������ַ **/
	public static String SEARCH_USER_NICK_URL = SERVER_ADDRESS + "/user/search/nick.json";
	/** �޸��û����������ַ **/
	public static String UPDATE_PWD_IN_URL = SERVER_ADDRESS + "/user/update_pwd/{client_id}.json";
	/** �鿴�û���Ϣ�����ַ **/
	public static String USER_IN_URL = SERVER_ADDRESS + "/user/{client_id}.json";
	/** ����ָ���û���ǩ�����ַ **/
	public static String USER_UPDATE_LABEL_IN_URL = SERVER_ADDRESS + "/user/update_label/{client_id}.json";
	/** �����Ƽ������ַ **/
	public static String TOPIC_RECOMMEND_IN_URL = SERVER_ADDRESS + "/topic/recommend.json";
	/** ����ҵ�ѻ��������ַ **/
	public static String SEARCH_TOPIC_LABEL_URL = SERVER_ADDRESS + "/topic/search/label.json";
	/** ������ؼ����ѻ��������ַ **/
	public static String SEARCH_TOPIC_SUBJECT_URL = SERVER_ADDRESS + "/topic/search/subject.json";
	/** �鿴ָ���û��Ļ����б������ַ **/
	public static String USER_TOPIC_IN_URL = SERVER_ADDRESS + "/user/topic.json";
	/** ���뻰��Ⱥ�������ַ **/
	public static String JOIN_TOPIC_IN_URL = SERVER_ADDRESS + "/topic/join.json";
	/** �˳�����Ⱥ�������ַ **/
	public static String LEAVE_TOPIC_IN_URL = SERVER_ADDRESS + "/topic/leave.json";
	/** �����������ַ **/
	public static String TOPIC_CREATE_URL = SERVER_ADDRESS + "/topic/create.json";
	/** �ύ������Ϣ�����ַ **/
	public static String SYSTEM_FEEDBACK_URL = SERVER_ADDRESS + "/system/feedback.json";
	/** ��ȡ��������ϵͳʱ�����Ӧ���������ַ **/
	public static String SYSTEM_CONFIG_URL = SERVER_ADDRESS + "/system/system_config.json";
	/** �õ�ĳ�˵ĺ��������б������ַ **/
	public static String FRIEND_INVITATION_URL = SERVER_ADDRESS + "/friend/invitation/{client_id}.json";
	/** ����Ӻ��ѵ�ַ **/
	public static String FRIEND_ADD_URL = SERVER_ADDRESS + "/friend/add/{client_id}.json";
	/** ͬ��/�ܾ��Ӻ��������ַ **/
	public static String FRIEND_STATUS_URL = SERVER_ADDRESS + "/friend/{status}/{inviter_id}.json";
	/** ɾ�����������ַ **/
	public static String FRIEND_DELETE_URL = SERVER_ADDRESS + "/friend/delete/{client_id}.json";
	/** �õ�ĳ�˵ĺ����б������ַ **/
	public static String FRIEND_GET_URL = SERVER_ADDRESS + "/friend/get/{client_id}.json";
	
	/** ģ��client_id **/
	public static String CLIENT_ID = "44393c565b15e21df03336b8f4a82eff713c9a95";

}
