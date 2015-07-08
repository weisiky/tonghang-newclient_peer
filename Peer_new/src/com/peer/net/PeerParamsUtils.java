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
	public static Map<String, Object> getDefaultParams(Context context) {
		Map<String, Object> defaultParams = new HashMap<String, Object>();
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
	public static HttpEntity getLoginParams(Context context, String email,
			String password) throws UnsupportedEncodingException, Exception {
		Map<String, Object> loginParams = getDefaultParams(context);
		loginParams.put("email", email);
		loginParams.put("password", password);
		HttpEntity entity = new StringEntity(
				JsonDocHelper.toJSONString(loginParams), "utf-8");
		return entity;
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
	public static HttpEntity getFindPassWordParams(Context context, String email)
			throws UnsupportedEncodingException, Exception {
		Map<String, Object> loginParams = getDefaultParams(context);
		loginParams.put("email", email);
		HttpEntity entity = new StringEntity(
				JsonDocHelper.toJSONString(loginParams), "utf-8");
		return entity;
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
	public static HttpEntity getUserParams(Context context, String client_id)
			throws UnsupportedEncodingException, Exception {
		Map<String, Object> loginParams = getDefaultParams(context);
		loginParams.put("client_id", client_id);
		HttpEntity entity = new StringEntity(
				JsonDocHelper.toJSONString(loginParams), "utf-8");
		return entity;
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
	public static HttpEntity getUpdatepasswdParams(Context context, String oldpasswd , String newpasswd)
			throws UnsupportedEncodingException, Exception {
		Map<String, Object> loginParams = getDefaultParams(context);
		loginParams.put("old_passwd", oldpasswd);
		loginParams.put("new_passwd", newpasswd);
		HttpEntity entity = new StringEntity(
				JsonDocHelper.toJSONString(loginParams), "utf-8");
		return entity;
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
	public static HttpEntity getRegisterTagParams(Context context,
			String email, String password, String username, List labels)
			throws Exception {
		Map<String, Object> registerTagParams = getDefaultParams(context);
		registerTagParams.put("email", email);
		registerTagParams.put("password", password);
		registerTagParams.put("username", username);
		registerTagParams.put("labels", labels);
		HttpEntity entity = new StringEntity(
				JsonDocHelper.toJSONString(registerTagParams), "utf-8");
		return entity;
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
	public static HttpEntity getHomeParams(Context context, String client_id,
			int page) throws Exception {
		Map<String, Object> registerTagParams = getDefaultParams(context);
		registerTagParams.put("client_id", client_id);
		registerTagParams.put("pageindex", page);
		HttpEntity entity = new StringEntity(
				JsonDocHelper.toJSONString(registerTagParams), "utf-8");
		return entity;
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
	public static HttpEntity getUpdateParams(Context context,
			 String username, String birth, String sex , String address)
			throws Exception {
		Map<String, Object> registerTagParams = getDefaultParams(context);
		
		registerTagParams.put("username", username);
		registerTagParams.put("sex", birth);
		registerTagParams.put("birth", sex);
		registerTagParams.put("city", address);
		HttpEntity entity = new StringEntity(
				JsonDocHelper.toJSONString(registerTagParams), "utf-8");
		return entity;
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
	public static HttpEntity getRemTopicParams(Context context, String client_id,
			int page) throws Exception {
		Map<String, Object> registerTagParams = getDefaultParams(context);
		registerTagParams.put("client_id", client_id);
		registerTagParams.put("pageindex", page);
		HttpEntity entity = new StringEntity(
				JsonDocHelper.toJSONString(registerTagParams), "utf-8");
		return entity;
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
	public static HttpEntity getSearchResultParams(Context context,
			String name, int page, String contanttype) throws Exception {
		Map<String, Object> registerTagParams = getDefaultParams(context);
		// registerTagParams.put("email", email);
		// registerTagParams.put("password", password);
		// registerTagParams.put("username", username);
		// registerTagParams.put("labels", labels);
		HttpEntity entity = new StringEntity(
				JsonDocHelper.toJSONString(registerTagParams), "utf-8");
		return entity;
	}

}
