package com.peer.net;

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
import com.peer.activity.R;
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
	public static HashMap<String, Object> getDefaultParams(Context context) {
		HashMap<String, Object> defaultParams = new HashMap<String, Object>();
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
		pLog.i("test", "params:" + params.toString());
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
			String client_id, String topic_id)
			throws UnsupportedEncodingException, Exception {
		Map<String, Object> loginParams = getDefaultParams(context);
		loginParams.put("client_id", client_id);
		loginParams.put("topic_id", topic_id);
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
			String email, String password, String username, List labels)
			throws Exception {
		RequestParams params = new RequestParams();
		HashMap<String, Object> registerTagParams = getDefaultParams(context);
		registerTagParams.put("email", email);
		registerTagParams.put("password", password);
		registerTagParams.put("username", username);
		registerTagParams.put("labels", labels);
		params.put("mapstr", JsonDocHelper.toJSONString(registerTagParams));
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
			String client_id, int page) throws Exception {
		Map<String, Object> registerTagParams = getDefaultParams(context);
		registerTagParams.put("client_id", client_id);
		registerTagParams.put("pageindex", page);
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
	 * @param client_id
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public static RequestParams getNewFriendsParams(Context context,
			int pageindex) throws Exception {
		Map<String, Object> registerTagParams = getDefaultParams(context);
		registerTagParams.put("pageindex", pageindex);
		RequestParams params = new RequestParams();
		params.put("mapstr", JsonDocHelper.toJSONString(registerTagParams));
		return params;
	}

	/**
	 * 更改用户信息参数绑定
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
	public static RequestParams getUpdateParams(Context context,
			String username, String birth, String sex, String address)
			throws Exception {
		Map<String, Object> registerTagParams = getDefaultParams(context);

		registerTagParams.put("username", username);
		registerTagParams.put("sex", birth);
		registerTagParams.put("birth", sex);
		registerTagParams.put("city", address);
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
		registerTagParams.put("topicid", groupid);
		registerTagParams.put("pageindex", page);
		registerTagParams.put("client_id", client_id);
		RequestParams params = new RequestParams();
		params.put("mapstr", JsonDocHelper.toJSONString(registerTagParams));
		return params;
	}

}
