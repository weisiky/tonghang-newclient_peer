package com.peer.net;

import java.net.URLEncoder;
import java.util.HashMap;

import android.content.Context;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.peer.activity.R;

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
	 * ����������
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
		params.put("email", email);
		params.put("password", password);
		return params;
	}

}
