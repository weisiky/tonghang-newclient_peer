package com.peer.net;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Http网络请求工具类
 * 
 * @author zhangzg
 * 
 */

public class HttpUtil {
	// 实例话对象
	private static AsyncHttpClient client = new AsyncHttpClient();
	static {
		// 设置链接超时，如果不设置，默认为10s
		client.setTimeout(11000);
	}

	/**
	 * get用一个完整url获取一个string对象
	 * 
	 * @param urlString
	 * @param res
	 */
	public static void get(String urlString, AsyncHttpResponseHandler res) {
		client.get(urlString, res);
	}

	/**
	 * post用一个完整url获取一个string对象
	 * 
	 * @param urlString
	 * @param res
	 */
	public static void post(String urlString, AsyncHttpResponseHandler res) {
		client.post(urlString, res);
	}

	/**
	 * get url里面带参数
	 * 
	 * @param urlString
	 * @param res
	 */
	public static void get(String urlString, RequestParams params,
			AsyncHttpResponseHandler res) {
		client.get(urlString, params, res);
	}

	/**
	 * post url里面带参数
	 * 
	 * @param urlString
	 * @param res
	 */
	public static void post(String urlString, RequestParams params,
			AsyncHttpResponseHandler res) {
		client.post(urlString, params, res);
	}

	/**
	 * get 不带参数，获取json对象或者数组
	 * 
	 * @param urlString
	 * @param res
	 */
	public static void get(String urlString, JsonHttpResponseHandler res) {
		client.get(urlString, res);
	}

	/**
	 * post 不带参数，获取json对象或者数组
	 * 
	 * @param urlString
	 * @param res
	 */
	public static void post(String urlString, JsonHttpResponseHandler res) {
		client.post(urlString, res);
	}

	/**
	 * post 带参数，获取json对象或者数组
	 * 
	 * @param urlString
	 * @param res
	 */
	public static void get(String urlString, RequestParams params,
			JsonHttpResponseHandler res) {
		client.get(urlString, params, res);
	}

	/**
	 * post 带参数，获取json对象或者数组
	 * 
	 * @param urlString
	 * @param res
	 */
	public static void post(String urlString, RequestParams params,
			JsonHttpResponseHandler res) {
		client.post(urlString, params, res);
	}

	/**
	 * get 下载数据使用，会返回byte数据
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