package com.peer.net;

import java.net.URLEncoder;
import java.util.HashMap;

import android.content.Context;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.peer.activity.R;

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
	 * 两个参数绑定
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
		params.put("email", email);
		params.put("password", password);
		return params;
	}

}
