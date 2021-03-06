package com.peer.net;

/**
 * Url地址统一管理
 * 
 * @author zhangzg
 * 
 */
public interface HttpConfig {

	/** IP **/
	 public static String SERVER_ADDRESS =
	 "http://192.168.23.1:8080/tonghang-serverapi";
	// "http://192.168.23.1:8080/tonghang-serverapi";
	// public static String SERVER_ADDRESS =
	// "http://192.168.31.192/tonghang-serverapi";
	// public static String SERVER_ADDRESS =
//	 "http://www.tonghang1.com:8088/tonghang-serverapi";
//	 "http://www.tonghang1.com:8080/tonghang-serverapi";
	/** 测试url **/
//	public static String SERVER_ADDRESS = "http://www.tonghang1.com:8088/tonghang-serverapi";
//	public static String SERVER_ADDRESS = "http://114.215.143.83:8080/tonghang-serverapi";

	// http://114.215.143.83:3000

	// "http://192.168.1.102:8080/tonghang-serverapi";

	/** 登入请求 **/
	public static String LONIN_IN_URL = SERVER_ADDRESS + "/user/newlogin.json";
	/** 注册基本信息请求 **/
	public static String REGISTTAG_IN_URL = SERVER_ADDRESS
			+ "/user/newregist.json";
	/** 编辑名片信息请求 **/
	public static String Card_IN_URL = SERVER_ADDRESS
			+ "/card/";
	/** 更新用户信息请求 **/
	public static String UPDATE_IN_URL = SERVER_ADDRESS + "/user/update/";
	/** 向所有同行发送推送请求 **/
	public static String PUSH_IN_URL = SERVER_ADDRESS + "/user/";
	/** 忘记密码请求 **/
	public static String FORGET_IN_URL = SERVER_ADDRESS
			+ "/user/forget_password.json";
	/** 推荐用户请求 **/
	public static String USER_RECOMMEND_IN_URL = SERVER_ADDRESS
			+ "/user/recommend.json";
	/** 相应标签下的用户 **/
	public static String LABEL_IN_URL = SERVER_ADDRESS
			+ "/user/search/label.json";
	/** 参与话题的用户 **/
	public static String NUMBER_IN_URL = SERVER_ADDRESS + "/topic/member.json";
	/** 按行业搜用户 **/
	public static String SEARCH_USER_LABEL_URL = SERVER_ADDRESS
			+ "/user/search/label.json";
	/** 按昵称搜用户 **/
	public static String SEARCH_USER_NICK_URL = SERVER_ADDRESS
			+ "/user/search/nick.json";
	/** 用户更改密码 **/
	public static String UPDATE_PWD_IN_URL = SERVER_ADDRESS
			+ "/user/update_pwd/";
	/** 查看用户信息 **/
	public static String USER_IN_URL = SERVER_ADDRESS + "/user/";
	/** 查看名片信息 **/
	public static String CARD_IN_URL = SERVER_ADDRESS + "/card/";
	/** 登入时，GPS信息 **/
	public static String LOGIN_GPS_URL = SERVER_ADDRESS + "/user/distance/";
	/** 查看某话题信息 **/
	public static String TOPIC_IN_URL = SERVER_ADDRESS + "/topic/";
	/** 设置指定用户标签 **/
	public static String USER_UPDATE_LABEL_IN_URL = SERVER_ADDRESS
			+ "/user/update_label/";
	/** 推荐话题 **/
	public static String TOPIC_RECOMMEND_IN_URL = SERVER_ADDRESS
			+ "/topic/recommend.json";
	/** 按行业搜话题 **/
	public static String SEARCH_TOPIC_LABEL_URL = SERVER_ADDRESS
			+ "/topic/search/label.json";
	/** 按内容搜话题 **/
	public static String SEARCH_TOPIC_SUBJECT_URL = SERVER_ADDRESS
			+ "/topic/search/subject.json";
	/** 查看指定用户的话题列表 **/
	public static String USER_TOPIC_IN_URL = SERVER_ADDRESS
			+ "/user/topic.json";
	/** 加入话题群聊 **/
	public static String JOIN_TOPIC_IN_URL = SERVER_ADDRESS
			+ "/topic/join.json";
	/** 删除话题群聊 **/
	public static String DELETE_TOPIC_IN_URL = SERVER_ADDRESS
			+ "/topic/delete.json";
	/** 退出话题群聊 **/
	public static String LEAVE_TOPIC_IN_URL = SERVER_ADDRESS
			+ "/topic/leave.json";
	/** 发表话题 **/
	public static String TOPIC_CREATE_URL = SERVER_ADDRESS
			+ "/topic/create.json";
	/** 提交反馈信息接口 **/
	public static String SYSTEM_FEEDBACK_URL = SERVER_ADDRESS
			+ "/system/feedback.json";
	/** 获取服务器端系统时间和相应参数 **/
	public static String SYSTEM_CONFIG_URL = SERVER_ADDRESS
			+ "/system/system_config.json";
	/** 得到某人的好友申请列表 **/
	public static String FRIEND_INVITATION_URL = SERVER_ADDRESS
			+ "/friend/invitation/";
	/** 请求加好友 **/
	public static String FRIEND_ADD_URL = SERVER_ADDRESS + "/friend/add/";
	/** 同意/拒绝加好友 **/
	public static String FRIEND_STATUS_URL = SERVER_ADDRESS + "/friend/";
	/** 删除好友 **/
	public static String FRIEND_DELETE_URL = SERVER_ADDRESS + "/friend/delete/";
	/** 交换名片请求 **/
	public static String CHANGE_CARD_URL = SERVER_ADDRESS + "/card/";
	/** 得到某人的好友列表 **/
	public static String FRIEND_GET_URL = SERVER_ADDRESS + "/friend/get/";
	/** 得到某人的黑名单列表 **/
	public static String BLACKLIST_GET_URL = SERVER_ADDRESS + "/block/";
	/** 添加黑名单用户列表 **/
	public static String ADDBLOCK_GET_URL = SERVER_ADDRESS + "/block/";
	/** 从黑名单移除用户列表 **/
	public static String DROPBLOCK_GET_URL = SERVER_ADDRESS + "/block/";
	/** 得到系統參數 **/
	public static String GET_SYSTEMCONFIG = SERVER_ADDRESS
			+ "/system/system_config.json";
}