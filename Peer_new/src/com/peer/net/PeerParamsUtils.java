package com.peer.net;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.peer.activity.R;
import com.peer.utils.pLog;

/**
 * 网络请求参数工具类
 * 
 * @author zhangzg
 * 
 */
public class PeerParamsUtils {
	// 实例话对象
	private static RequestParams params = new RequestParams();

	/**
	 * 添加系统请求参数（每个接口都必须传的参数）
	 * 
	 * @param context
	 * @return
	 */
	public static RequestParams getDefaultParams(Context context) {
		RequestParams params = new RequestParams();

		return params;
	}

	/**
	 * 登录参数绑定
	 * 
	 * @param context
	 * @param key1
	 * @param key2
	 * @param methodName
	 * @return
	 */
	public static RequestParams getLoginParams(Context context, String email,
			String password) {
		RequestParams params = getDefaultParams(context);
		// 绑定参数
		// params.put("client_v", "3.7");
		// params.put("sign_method", "");
		// params.put("ttid", "");
		// params.put("platform", "01");
		// params.put("model", "MI+2S");
		// params.put("etag", "");
		// params.put("accept", "aaaaaa");
		// params.put("appkey", "0110010");
		// params.put("format", "json");
		// params.put("ver", "1.0");
		// params.put("school_id", "");
		// params.put("sign", "");
		// params.put("source", "android_001");
		// params.put("swidth", "720");
		// params.put("user_pass", "e10adc3949ba59abbe56e057f20f883e");
		// params.put("user_phone", "12121212121");
		// params.put("client_id", "");
		// params.put("usertoken", "");
		params.put("email", email);
		params.put("password", password);

		pLog.i("sendMsg", "params:" + params);

		return params;
	}
	
	/**
	 * 基本注册参数绑定
	 * 
	 * @param context
	 * @param key1
	 * @param key2
	 * @param key3
	 * @param list
	 * @param methodName
	 * @return
	 */
	public static RequestParams getRegisterTagParams(Context context, String email,
			String password , String nikename , List list) {
		RequestParams params = getDefaultParams(context);
		// 绑定参数
		// params.put("client_v", "3.7");
		// params.put("sign_method", "");
		// params.put("ttid", "");
		// params.put("platform", "01");
		// params.put("model", "MI+2S");
		// params.put("etag", "");
		// params.put("accept", "aaaaaa");
		// params.put("appkey", "0110010");
		// params.put("format", "json");
		// params.put("ver", "1.0");
		// params.put("school_id", "");
		// params.put("sign", "");
		// params.put("source", "android_001");
		// params.put("swidth", "720");
		// params.put("user_pass", "e10adc3949ba59abbe56e057f20f883e");
		// params.put("user_phone", "12121212121");
		// params.put("client_id", "");
		// params.put("usertoken", "");
		params.put("email", email);
		params.put("password", password);
		params.put("nikename", nikename);
		params.put("list", list);
		

		pLog.i("sendMsg", "params:" + params);

		return params;
	}
	
	
	/**
	 * 修改用户信息参数绑定
	 * 
	 * @param context
	 * @param key1
	 * @param key2
	 * @param key3
	 * @param key4
	 * @param methodName
	 * @return
	 */
	public static RequestParams getUpdateParams(Context context, String client_id, String tv_setbirth, String tv_sex, String tv_setaddress) {
		RequestParams params = getDefaultParams(context);
		// 绑定参数
		// params.put("client_v", "3.7");
		// params.put("sign_method", "");
		// params.put("ttid", "");
		// params.put("platform", "01");
		// params.put("model", "MI+2S");
		// params.put("etag", "");
		// params.put("accept", "aaaaaa");
		// params.put("appkey", "0110010");
		// params.put("format", "json");
		// params.put("ver", "1.0");
		// params.put("school_id", "");
		// params.put("sign", "");
		// params.put("source", "android_001");
		// params.put("swidth", "720");
		// params.put("user_pass", "e10adc3949ba59abbe56e057f20f883e");
		// params.put("user_phone", "12121212121");
		// params.put("client_id", "");
		// params.put("usertoken", "");
		params.put("client_id", client_id);
		params.put("tv_setbirth", tv_setbirth);
		params.put("tv_sex", tv_sex);
		params.put("tv_setaddress", tv_setaddress);

		pLog.i("sendMsg", "params:" + params);

		return params;
	}
	
	/**
	 * 找回密码参数绑定
	 * 
	 * @param context
	 * @param key1
	 * @param key2
	 * @param key3
	 * @param key4
	 * @param methodName
	 * @return
	 */
	public static RequestParams getFindpasswdParams(Context context, String email) {
		RequestParams params = getDefaultParams(context);
		// 绑定参数
		// params.put("client_v", "3.7");
		// params.put("sign_method", "");
		// params.put("ttid", "");
		// params.put("platform", "01");
		// params.put("model", "MI+2S");
		// params.put("etag", "");
		// params.put("accept", "aaaaaa");
		// params.put("appkey", "0110010");
		// params.put("format", "json");
		// params.put("ver", "1.0");
		// params.put("school_id", "");
		// params.put("sign", "");
		// params.put("source", "android_001");
		// params.put("swidth", "720");
		// params.put("user_pass", "e10adc3949ba59abbe56e057f20f883e");
		// params.put("user_phone", "12121212121");
		// params.put("client_id", "");
		// params.put("usertoken", "");
		params.put("email", email);

		pLog.i("sendMsg", "params:" + params);

		return params;
	}
}
