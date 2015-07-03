package com.peer.net;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Http�������󹤾���
 * 
 * @author zhangzg
 * 
 */

public class HttpUtil {
	// ʵ��������
	private static AsyncHttpClient client = new AsyncHttpClient();
	static {
		// �������ӳ�ʱ����������ã�Ĭ��Ϊ10s
		client.setTimeout(11000);
	}

	/**
	 * get��һ������url��ȡһ��string����
	 * 
	 * @param urlString
	 * @param res
	 */
	public static void get(String urlString, AsyncHttpResponseHandler res) {
		client.get(urlString, res);
	}

	/**
	 * post��һ������url��ȡһ��string����
	 * 
	 * @param urlString
	 * @param res
	 */
	public static void post(String urlString, AsyncHttpResponseHandler res) {
		client.post(urlString, res);
	}

	/**
	 * get url���������
	 * 
	 * @param urlString
	 * @param res
	 */
	public static void get(Context context, String urlString,
			HttpEntity entity, String contentType, RequestParams params,
			AsyncHttpResponseHandler res) {

		client.get(context, urlString, entity, contentType, res);
		// client.get(urlString, params, res);
	}

	/**
	 * post url���������
	 * 
	 * @param urlString
	 * @param res
	 */
	public static void post(String urlString, RequestParams params,
			AsyncHttpResponseHandler res) {
		client.post(urlString, params, res);
	}

	/**
	 * get ������������ȡjson�����������
	 * 
	 * @param urlString
	 * @param res
	 */
	public static void get(String urlString, JsonHttpResponseHandler res) {
		client.get(urlString, res);
	}

	/**
	 * post ������������ȡjson�����������
	 * 
	 * @param urlString
	 * @param res
	 */
	public static void post(String urlString, JsonHttpResponseHandler res) {
		client.post(urlString, res);
	}

	/**
	 * get ����������ȡjson�����������
	 * 
	 * @param urlString
	 * @param res
	 */
	public static void get(Context context , String urlString, HttpEntity entity,String contentType,
			JsonHttpResponseHandler res) {

		client.get(context, urlString, entity,contentType,res);
	}

	/**
	 * post ����������ȡjson�����������
	 * 
	 * @param urlString
	 * @param res
	 */
	public static void post(Context context, String urlString,
			HttpEntity entity, String contentType,JsonHttpResponseHandler res) {
		client.post(context, urlString, entity, contentType, res);
		
	}

	/**
	 * get ��������ʹ�ã��᷵��byte����
	 * 
	 * @param urlString
	 * @param res
	 */
	public static void get(String uString, BinaryHttpResponseHandler bHandler) {
		client.get(uString, bHandler);
	}

	public static AsyncHttpClient getClient() {
		return client;
	}
}