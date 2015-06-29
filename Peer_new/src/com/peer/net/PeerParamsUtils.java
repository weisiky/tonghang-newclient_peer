package com.peer.net;

import java.net.URLEncoder;
import java.util.HashMap;

import android.content.Context;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.peer.activity.R;
import com.peer.utils.pLog;

/**
 * �����������������
 * 
 * @author zhangzg
 * 
 */
public class PeerParamsUtils {
	// ʵ��������
	private static RequestParams params = new RequestParams();

	/**
	 * ���ϵͳ���������ÿ���ӿڶ����봫�Ĳ�����
	 * 
	 * @param context
	 * @return
	 */
	public static RequestParams getDefaultParams(Context context) {
		RequestParams params = new RequestParams();

		return params;
	}

	/**
	 * ��¼������
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
		// �󶨲���
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
}
