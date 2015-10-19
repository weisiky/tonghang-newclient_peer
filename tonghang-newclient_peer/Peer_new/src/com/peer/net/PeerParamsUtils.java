package com.peer.net;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.media.JetPlayer.OnJetEventListener;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.peer.IMimplements.easemobchatUser;
import com.peer.R;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.pLog;

/**
 * 网络请求参数工具类
 * 
 * @author zhangzg
 * 
 */
public class PeerParamsUtils {

	/**
	 * 添加系统请求参数（每个接口都必须传的参数）
	 * 
	 * @param context
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static HashMap<String, Object> getDefaultParams(Context context) {
		Map<String, String> defaultmap = new HashMap<String, String>();
		HashMap<String, Object> defaultParams = new HashMap<String, Object>();
		defaultmap.put("client_id", ((pBaseActivity)context).mShareFileUtils.getString(Constant.CLIENT_ID, ""));
		defaultParams.put("token", defaultmap);
		return defaultParams;
	}

	/**
	 * 登录参数绑定
	 * 
	 * @param context
	 * @param email
	 * @param password
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public static RequestParams getLoginParams(Context context, String email,
			String password) throws Exception {
		Map<String, Object> loginParams = getDefaultParams(context);
		loginParams.put("email", email);
		loginParams.put("password", password);
		RequestParams params = new RequestParams();
		params.put("mapstr", JsonDocHelper.toJSONString(loginParams));
		return params;
	}
	
	/**
	 * 按标签搜用户参数绑定
	 * 
	 * @param context
	 * @param label_name
	 * @param pageindex
	 * @param client_id
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public static RequestParams getSearchUserByLabelParams(Context context, String label_name,
			int pageindex , String client_id,boolean byDistance) throws Exception {
		Map<String, Object> loginParams = getDefaultParams(context);
		loginParams.put("label_name", label_name);
		loginParams.put("pageindex", pageindex);
		loginParams.put("client_id", client_id);
		loginParams.put("byDistance", byDistance);
		RequestParams params = new RequestParams();
		params.put("mapstr", JsonDocHelper.toJSONString(loginParams));
		return params;
	}
	
	
	/**
	 * 按昵称搜用户参数绑定
	 * 
	 * @param context
	 * @param username
	 * @param pageindex
	 * @param client_id
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public static RequestParams getSearchUserByNikeParams(Context context, String username,
			int pageindex , String client_id,boolean byDistance) throws Exception {
		Map<String, Object> loginParams = getDefaultParams(context);
		loginParams.put("username", username);
		loginParams.put("pageindex", pageindex);
		loginParams.put("client_id", client_id);
		loginParams.put("byDistance", byDistance);
		RequestParams params = new RequestParams();
		params.put("mapstr", JsonDocHelper.toJSONString(loginParams));
		return params;
	}

	/**
	 * 按标签搜话题参数绑定
	 * 
	 * @param context
	 * @param label_name
	 * @param pageindex
	 * @param client_id
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public static RequestParams getSearchTopicByLabelParams(Context context, String label_name,
			int pageindex , String client_id) throws Exception {
		Map<String, Object> loginParams = getDefaultParams(context);
		loginParams.put("label_name", label_name);
		loginParams.put("pageindex", pageindex);
		loginParams.put("client_id", client_id);
		RequestParams params = new RequestParams();
		params.put("mapstr", JsonDocHelper.toJSONString(loginParams));
		return params;
	}
	
	/**
	 * 按关键字搜话题参数绑定
	 * 
	 * @param context
	 * @param subject
	 * @param pageindex
	 * @param client_id
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public static RequestParams getSearchTopicBySubjectParams(Context context, String subject,
			int pageindex , String client_id) throws Exception {
		Map<String, Object> loginParams = getDefaultParams(context);
		loginParams.put("subject", subject);
		loginParams.put("pageindex", pageindex);
		loginParams.put("client_id", client_id);
		RequestParams params = new RequestParams();
		params.put("mapstr", JsonDocHelper.toJSONString(loginParams));
		return params;
	}
	
	
	/**
	 * 同意/拒绝某人为好友参数绑定
	 * 
	 * @param context
	 * @param client_id
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public static RequestParams getaddfriendParams(Context context,
			String client_id) throws Exception {
		Map<String, Object> loginParams = getDefaultParams(context);
		loginParams.put("invitee_id", client_id);
		RequestParams params = new RequestParams();
		params.put("mapstr", JsonDocHelper.toJSONString(loginParams));
		return params;
	}
	
	
	
	/**
	 * 创建话题参数绑定
	 * 
	 * @param context
	 * @param client_id
	 * @param subject
	 * @param label_name
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public static RequestParams getcreatetopicParams(Context context,
			String client_id, String subject, String label_name)
			throws UnsupportedEncodingException, Exception {
		Map<String, Object> loginParams = getDefaultParams(context);
		loginParams.put("client_id", client_id);
		loginParams.put("subject", subject);
		loginParams.put("label_name", label_name);
		RequestParams params = new RequestParams();
		params.put("mapstr", JsonDocHelper.toJSONString(loginParams));
		return params;
	}

	/**
	 * 添加好友参数绑定
	 * 
	 * @param context
	 * @param invitee_id
	 * @param reason
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public static RequestParams getaddfriendParams(Context context,
			String invitee_id, String reason)
			throws UnsupportedEncodingException, Exception {
		Map<String, Object> loginParams = getDefaultParams(context);
		loginParams.put("invitee_id", invitee_id);
		loginParams.put("reason", reason);
		RequestParams params = new RequestParams();
		params.put("mapstr", JsonDocHelper.toJSONString(loginParams));
		return params;
	}

	/**
	 * 删除好友参数绑定
	 * 
	 * @param context
	 * 
	 * @param friend_id
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public static RequestParams getdeletefriendParams(Context context,
			String friend_id) throws UnsupportedEncodingException, Exception {
		Map<String, Object> loginParams = getDefaultParams(context);
		loginParams.put("friend_id", friend_id);
		// HttpEntity entity = new StringEntity(
		// JsonDocHelper.toJSONString(loginParams), "utf-8");
		RequestParams params = new RequestParams();
		params.put("mapstr", JsonDocHelper.toJSONString(loginParams));
		return params;
	}
	
	
	/**
	 * 获取某人黑名单参数绑定
	 * 
	 * @param context
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public static RequestParams getblacklistParams(Context context
			) throws UnsupportedEncodingException, Exception {
		Map<String, Object> loginParams = getDefaultParams(context);
		RequestParams params = new RequestParams();
		params.put("mapstr", JsonDocHelper.toJSONString(loginParams));
		return params;
	}

	/**
	 * 获取未读取消息参数绑定
	 * 
	 * @param context
	 * 
	 * @param users
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public static RequestParams getComMsgParams(Context context,
			easemobchatUser users) throws UnsupportedEncodingException,
			Exception {
		Map<String, Object> loginParams = getDefaultParams(context);
		loginParams.put("entries", users.getEasemobchatusers());
		RequestParams params = new RequestParams();
		params.put("mapstr", JsonDocHelper.toJSONString(loginParams));
		return params;
	}

	/**
	 * 找回密码参数绑定
	 * 
	 * @param context
	 * @param email
	 * @param password
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public static RequestParams getFindPassWordParams(Context context,
			String email) throws UnsupportedEncodingException, Exception {
		Map<String, Object> loginParams = getDefaultParams(context);
		loginParams.put("email", email);
		RequestParams params = new RequestParams();
		params.put("mapstr", JsonDocHelper.toJSONString(loginParams));
		return params;
	}

	/**
	 * 获取用户信息参数绑定
	 * 
	 * @param context
	 * @param client_id
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public static RequestParams getUserParams(Context context, String client_id)
			throws UnsupportedEncodingException, Exception {
		Map<String, Object> loginParams = getDefaultParams(context);
		loginParams.put("client_id", client_id);
		RequestParams params = new RequestParams();
		params.put("mapstr", JsonDocHelper.toJSONString(loginParams));
		return params;
	}
	
	
	
	/**
	 * 获取名片信息参数绑定
	 * 
	 * @param context
	 * @param other_id
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public static RequestParams getCardParams(Context context, String other_id)
			throws UnsupportedEncodingException, Exception {
		Map<String, Object> loginParams = getDefaultParams(context);
		loginParams.put("other_id", other_id);
		RequestParams params = new RequestParams();
		params.put("mapstr", JsonDocHelper.toJSONString(loginParams));
		return params;
	}
	
	
	/**
	 * GPS参数绑定
	 * 
	 * @param context
	 * @param x_point
	 * @param y_point
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public static RequestParams getGPSParams(Context context, Double x_point,Double y_point)
			throws UnsupportedEncodingException, Exception {
		Map<String, Object> loginParams = getDefaultParams(context);
		loginParams.put("x_point", x_point);
		loginParams.put("y_point", y_point);
		RequestParams params = new RequestParams();
		params.put("mapstr", JsonDocHelper.toJSONString(loginParams));
		return params;
	}
	
	
	/**
	 * 获取用户信息参数绑定
	 * 
	 * @param context
	 * @param client_id
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public static RequestParams getTopicParams(Context context, String topic_id)
			throws UnsupportedEncodingException, Exception {
		Map<String, Object> loginParams = getDefaultParams(context);
		loginParams.put("topic_id", topic_id);
		RequestParams params = new RequestParams();
		params.put("mapstr", JsonDocHelper.toJSONString(loginParams));
		return params;
	}

	/**
	 * 加入话题参数绑定
	 * 
	 * @param context
	 * @param client_id
	 * @param topic_id
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public static RequestParams getJoinParams(Context context,
			String client_id, String topic_id , boolean isOwner)
			throws UnsupportedEncodingException, Exception {
		Map<String, Object> loginParams = getDefaultParams(context);
		loginParams.put("client_id", client_id);
		loginParams.put("topic_id", topic_id);
		loginParams.put("isOwner", isOwner);
		RequestParams params = new RequestParams();
		params.put("mapstr", JsonDocHelper.toJSONString(loginParams));
		return params;
	}
	
	
	/**
	 * 加入话题参数绑定
	 * 
	 * @param context
	 * @param topic_id
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public static RequestParams getdeletetopicParams(Context context,
			 String topic_id )
					throws UnsupportedEncodingException, Exception {
		Map<String, Object> loginParams = getDefaultParams(context);
		loginParams.put("huanxin_group_id", topic_id);
		RequestParams params = new RequestParams();
		params.put("mapstr", JsonDocHelper.toJSONString(loginParams));
		return params;
	}

	/**
	 * 修改当前用户标签参数绑定
	 * 
	 * @param client_id
	 * @param label_name
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public static RequestParams getUserLabelsParams(Context context,
			String client_id, List<String> label_name)
			throws UnsupportedEncodingException, Exception {
		Map<String, Object> loginParams = getDefaultParams(context);
		loginParams.put("client_id", client_id);
		loginParams.put("label_name", label_name);
		RequestParams params = new RequestParams();
		params.put("mapstr", JsonDocHelper.toJSONString(loginParams));
		return params;
	}

	/**
	 * 更改密码参数绑定
	 * 
	 * @param context
	 * @param email
	 * @param password
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public static RequestParams getUpdatepasswdParams(Context context,
			String oldpasswd, String newpasswd)
			throws UnsupportedEncodingException, Exception {
		Map<String, Object> loginParams = getDefaultParams(context);
		loginParams.put("old_passwd", oldpasswd);
		loginParams.put("new_passwd", newpasswd);
		RequestParams params = new RequestParams();
		params.put("mapstr", JsonDocHelper.toJSONString(loginParams));
		return params;
	}

	/**
	 * 注册标签参数绑定
	 * 
	 * @param
	 * 
	 * @param context
	 * @param email
	 * @param password
	 * @param username
	 * @param labels
	 * @return
	 * @throws Exception
	 */
	public static RequestParams getRegisterTagParams(Context context,
			String email, String password, String username)
			throws Exception {
		RequestParams params = new RequestParams();
		HashMap<String, Object> registerTagParams = getDefaultParams(context);
		registerTagParams.put("email", email);
		registerTagParams.put("password", password);
		registerTagParams.put("username", username);
		params.put("mapstr", JsonDocHelper.toJSONString(registerTagParams));
		return params;
	}

	/**
	 * 编辑名片参数绑定
	 * 
	 * @param phone
	 * @param email
	 * @param realname
	 * @param company
	 * @param position
	 * @param worktime
	 * @param school
	 * @param experience
	 * @param major
	 * @param admissiontime
	 * @return
	 * @throws Exception
	 */
	public static RequestParams getCardParams(Context context,
			String phone, String email,String raelname,
			String company,String position,String worktime,String school,
			String experience,String major,String admissiontime)
			throws Exception {
		RequestParams params = new RequestParams();
		HashMap<String, Object> registerTagParams = getDefaultParams(context);
		registerTagParams.put("companyname", company);
		registerTagParams.put("position", position);
		registerTagParams.put("work_date", worktime);
		registerTagParams.put("email", email);
		registerTagParams.put("phone", phone);
		registerTagParams.put("schoolname", school);
		registerTagParams.put("realname", raelname);
		registerTagParams.put("major", major);
		registerTagParams.put("diploma", experience);
		registerTagParams.put("school_date", admissiontime);
		params.put("mapstr", JsonDocHelper.toJSONString(registerTagParams));
		pLog.i("test", "mapstr:"+params.toString());
		return params;
	}
	
	
	/**
	 * 获取首页参数绑定
	 * 
	 * @param
	 * 
	 * @param context
	 * @param client_id
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public static RequestParams getHomeParams(Context context,
			String client_id, int page , boolean byDistance) throws Exception {
		Map<String, Object> registerTagParams = getDefaultParams(context);
		registerTagParams.put("client_id", client_id);
		registerTagParams.put("pageindex", page);
		registerTagParams.put("byDistance", byDistance);
		RequestParams params = new RequestParams();
		params.put("mapstr", JsonDocHelper.toJSONString(registerTagParams));
		return params;
	}

	/**
	 * 获取用户好友申请列表
	 * 
	 * @param
	 * 
	 * @param context
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public static RequestParams getNewFriendsParams(Context context,
			int pageindex) throws Exception {
		RequestParams params = new RequestParams();
		params.put("pageindex", pageindex);
		return params;
	}

	/**
	 * 更改用户信息参数绑定
	 * 
	 * @param
	 * 
	 * @param context
	 * @param client_id
	 * @param tv_setbirth
	 * @param tv_sex
	 * @param tv_setaddress
	 * @param username
	 * @return
	 * @throws Exception
	 */
	public static RequestParams getUpdateParams(Context context,
			String client_id, String tv_setbirth, String tv_sex, String tv_setaddress,String username)
			throws Exception {
		Map<String, Object> registerTagParams = getDefaultParams(context);

		registerTagParams.put("client_id", client_id);
		registerTagParams.put("birth", tv_setbirth);
		registerTagParams.put("sex", tv_sex);
		registerTagParams.put("city", tv_setaddress);
		registerTagParams.put("username", username);
		RequestParams params = new RequestParams();
		params.put("mapstr", JsonDocHelper.toJSONString(registerTagParams));
		return params;
	}
	
	
	/**
	 * 更改用户生日信息参数绑定
	 * 
	 * @param context
	 * @param client_id
	 * @param tv_setbirth
	 * @return
	 * @throws Exception
	 */
	public static RequestParams getUpdateBirthParams(Context context,
			String client_id, String tv_setbirth)
					throws Exception {
		Map<String, Object> registerTagParams = getDefaultParams(context);
		
		registerTagParams.put("client_id", client_id);
		registerTagParams.put("birth", tv_setbirth);
		RequestParams params = new RequestParams();
		params.put("mapstr", JsonDocHelper.toJSONString(registerTagParams));
		return params;
	}
	
	
	/**
	 * 更改用户生日信息参数绑定
	 * 
	 * @param context
	 * @param client_id
	 * @param tv_setaddress
	 * @return
	 * @throws Exception
	 */
	public static RequestParams getUpdateCityParams(Context context,
			String client_id, String tv_setaddress)
					throws Exception {
		Map<String, Object> registerTagParams = getDefaultParams(context);
		
		registerTagParams.put("client_id", client_id);
		registerTagParams.put("city", tv_setaddress);
		RequestParams params = new RequestParams();
		params.put("mapstr", JsonDocHelper.toJSONString(registerTagParams));
		return params;
	}
	
	
	/**
	 * 更改用户昵称信息参数绑定
	 * 
	 * @param context
	 * @param client_id
	 * @param username
	 * @return
	 * @throws Exception
	 */
	public static RequestParams getUpdateUsernameParams(Context context,
			String client_id, String username)
					throws Exception {
		Map<String, Object> registerTagParams = getDefaultParams(context);
		
		registerTagParams.put("client_id", client_id);
		registerTagParams.put("username", username);
		RequestParams params = new RequestParams();
		params.put("mapstr", JsonDocHelper.toJSONString(registerTagParams));
		return params;
	}
	
	
	/**
	 * 更改用户性别信息参数绑定
	 * 
	 * @param context
	 * @param client_id
	 * @param tv_setsex
	 * @return
	 * @throws Exception
	 */
	public static RequestParams getUpdateSexParams(Context context,
			String client_id, String tv_setsex)
					throws Exception {
		Map<String, Object> registerTagParams = getDefaultParams(context);
		
		registerTagParams.put("client_id", client_id);
		registerTagParams.put("sex", tv_setsex);
		RequestParams params = new RequestParams();
		params.put("mapstr", JsonDocHelper.toJSONString(registerTagParams));
		return params;
	}
	
	
	
	/**
	 * 更改用户头像信息参数绑定
	 * 
	 * @param context
	 * @param client_id
	 * @param tv_setbirth
	 * @return
	 * @throws Exception
	 */
	public static RequestParams getUpdatePhotoParams(Context context,
			String client_id)
					throws Exception {
		Map<String, Object> registerTagParams = getDefaultParams(context);
		
		registerTagParams.put("client_id", client_id);
		RequestParams params = new RequestParams();
		params.put("mapstr", JsonDocHelper.toJSONString(registerTagParams));
		return params;
	}

	/**
	 * 获取话题推荐参数绑定
	 * 
	 * @param
	 * 
	 * @param context
	 * @param client_id
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public static RequestParams getRemTopicParams(Context context,
			String client_id, int page) throws Exception {
		Map<String, Object> registerTagParams = getDefaultParams(context);
		registerTagParams.put("client_id", client_id);
		registerTagParams.put("pageindex", page);
		RequestParams params = new RequestParams();
		params.put("mapstr", JsonDocHelper.toJSONString(registerTagParams));
		return params;
	}

	/**
	 * 注册标签参数绑定
	 * 
	 * @param
	 * 
	 * @param context
	 * @param email
	 * @param password
	 * @param username
	 * @param labels
	 * @return
	 * @throws Exception
	 */
	public static RequestParams getSearchResultParams(Context context,
			String name, int page, String contanttype) throws Exception {
		Map<String, Object> registerTagParams = getDefaultParams(context);
		// registerTagParams.put("email", email);
		// registerTagParams.put("password", password);
		// registerTagParams.put("username", username);
		// registerTagParams.put("labels", labels);
		RequestParams params = new RequestParams();
		params.put("mapstr", JsonDocHelper.toJSONString(registerTagParams));
		return params;
	}

	/**
	 * 获取参与话题用户参数绑定
	 * 
	 * @param
	 * 
	 * @param context
	 * @param groupid
	 * @param page
	 * @param client_id
	 * @return
	 * @throws Exception
	 */
	public static RequestParams getUserNumberParams(Context context,
			String groupid, int page, String client_id) throws Exception {
		Map<String, Object> registerTagParams = getDefaultParams(context);
		registerTagParams.put("topic_id", groupid);
		registerTagParams.put("pageindex", page);
		registerTagParams.put("client_id", client_id);
		RequestParams params = new RequestParams();
		params.put("mapstr", JsonDocHelper.toJSONString(registerTagParams));
		return params;
	}
	
	/**
	 * 获取用户反馈参数绑定
	 * 
	 * @param context
	 * @param client_id
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public static RequestParams getFeedParams(Context context, String client_id,String content
			) throws Exception {
		Map<String, Object> loginParams = getDefaultParams(context);
		loginParams.put("client_id", client_id);
		loginParams.put("content", content);
		RequestParams params = new RequestParams();
		params.put("mapstr", JsonDocHelper.toJSONString(loginParams));
		return params;
	}

}
