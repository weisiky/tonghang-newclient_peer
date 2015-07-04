package com.peer.net;

/**
 * Url地址统一管理
 * 
 * @author zhangzg
 * 
 */
public interface HttpConfig {

	/** ��������ַ **/
	public static String SERVER_ADDRESS = "http://192.168.1.2:8080/tonghang-serverapi";
	// http://www.tonghang1.com:3000
	// http://192.168.23.1:8080/tonghang-serverapi
	// http://192.168.1.2:8080/tonghang-serverapi
	/** ��¼�����ַ **/
	public static String LONIN_IN_URL = SERVER_ADDRESS + "/user/login.json";
	/** ����ע�������ַ **/
	public static String REGISTTAG_IN_URL = SERVER_ADDRESS
			+ "/user/regist.json";
	/** �޸��û���Ϣ�����ַ **/
	public static String UPDATE_IN_URL = SERVER_ADDRESS
			+ "/user/update/{client_id}.json";
	/** �������������ַ **/
	public static String FORGET_IN_URL = SERVER_ADDRESS
			+ "/user/forget_password.json";
	/** ��ҳ�û��Ƽ������ַ **/
	public static String USER_RECOMMEND_IN_URL = SERVER_ADDRESS
			+ "/user/recommend.json";
	/** ��Ӧ��ǩ�µ��û������ַ **/
	public static String LABEL_IN_URL = SERVER_ADDRESS
			+ "/user/search/label.json";
	/** ���뻰����û������ַ **/
	public static String NUMBER_IN_URL = SERVER_ADDRESS + "/topic/number.json";
	/** ����ҵ���û������ַ **/
	public static String SEARCH_USER_LABEL_URL = SERVER_ADDRESS
			+ "/user/search/label.json";
	/** ���ǳ����û������ַ **/
	public static String SEARCH_USER_NICK_URL = SERVER_ADDRESS
			+ "/user/search/nick.json";
	/** �޸��û����������ַ **/
	public static String UPDATE_PWD_IN_URL = SERVER_ADDRESS
			+ "/user/update_pwd/{client_id}.json";
	/** �鿴�û���Ϣ�����ַ **/
	public static String USER_IN_URL = SERVER_ADDRESS
			+ "/user/{client_id}.json";
	/** ����ָ���û���ǩ�����ַ **/
	public static String USER_UPDATE_LABEL_IN_URL = SERVER_ADDRESS
			+ "/user/update_label/{client_id}.json";
	/** �����Ƽ������ַ **/
	public static String TOPIC_RECOMMEND_IN_URL = SERVER_ADDRESS
			+ "/topic/recommend.json";
	/** ����ҵ�ѻ��������ַ **/
	public static String SEARCH_TOPIC_LABEL_URL = SERVER_ADDRESS
			+ "/topic/search/label.json";
	/** ������ؼ����ѻ��������ַ **/
	public static String SEARCH_TOPIC_SUBJECT_URL = SERVER_ADDRESS
			+ "/topic/search/subject.json";
	/** �鿴ָ���û��Ļ����б������ַ **/
	public static String USER_TOPIC_IN_URL = SERVER_ADDRESS
			+ "/user/topic.json";
	/** ���뻰��Ⱥ�������ַ **/
	public static String JOIN_TOPIC_IN_URL = SERVER_ADDRESS
			+ "/topic/join.json";
	/** �˳�����Ⱥ�������ַ **/
	public static String LEAVE_TOPIC_IN_URL = SERVER_ADDRESS
			+ "/topic/leave.json";
	/** �����������ַ **/
	public static String TOPIC_CREATE_URL = SERVER_ADDRESS
			+ "/topic/create.json";
	/** �ύ������Ϣ�����ַ **/
	public static String SYSTEM_FEEDBACK_URL = SERVER_ADDRESS
			+ "/system/feedback.json";
	/** ��ȡ��������ϵͳʱ�����Ӧ���������ַ **/
	public static String SYSTEM_CONFIG_URL = SERVER_ADDRESS
			+ "/system/system_config.json";
	/** �õ�ĳ�˵ĺ��������б������ַ **/
	public static String FRIEND_INVITATION_URL = SERVER_ADDRESS
			+ "/friend/invitation/{client_id}.json";
	/** ����Ӻ��ѵ�ַ **/
	public static String FRIEND_ADD_URL = SERVER_ADDRESS
			+ "/friend/add/{client_id}.json";
	/** ͬ��/�ܾ��Ӻ��������ַ **/
	public static String FRIEND_STATUS_URL = SERVER_ADDRESS
			+ "/friend/{status}/{inviter_id}.json";
	/** ɾ�����������ַ **/
	public static String FRIEND_DELETE_URL = SERVER_ADDRESS
			+ "/friend/delete/{client_id}.json";
	/** �õ�ĳ�˵ĺ����б������ַ **/
	public static String FRIEND_GET_URL = SERVER_ADDRESS
			+ "/friend/get/{client_id}.json";
}