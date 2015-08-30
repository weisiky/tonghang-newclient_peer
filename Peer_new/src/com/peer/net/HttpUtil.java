package com.peer.net;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.peer.utils.pLog;

/**
 * 
 * Http网络请求工具类
 * 
 * @author zhangzg
 * 
 */

public class HttpUtil {
	// 实例化网络请求对象
	private static AsyncHttpClient client = new AsyncHttpClient();
	static {
		// 设置链接超时，如果不设置，默认为10s
		client.setTimeout(600000);
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
	public static void get(Context context, String urlString,
			HttpEntity entity, String contentType, RequestParams params,
			AsyncHttpResponseHandler res) {

		client.get(context, urlString, entity, contentType, res);
		// client.get(urlString, params, res);
	}

	/**
	 * post url里面带参数
	 * 
	 * @param urlString
	 * @param res
	 */
	public static void post(String urlString, RequestParams params,
			AsyncHttpResponseHandler res) {
		pLog.i("test", "urlString:" + urlString);
		pLog.i("test", "params:" + params.toString());
		client.post(urlString, params, res);
	}

	/**
	 * post url里面带参数
	 * 
	 * @param urlString
	 * @param res
	 */
	public static void post(Context context, String urlString,
			RequestParams params, JsonHttpResponseHandler res) {

		pLog.i("test", "urlString:" + urlString);
		pLog.i("test", "params:" + params.toString());

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

		pLog.i("test", "urlString:" + urlString);

		client.post(urlString, res);
	}

	/**
	 * get 带参数，获取json对象或者数组
	 * 
	 * @param urlString
	 * @param res
	 */
	public static void get(Context context, String urlString,
			HttpEntity entity, String contentType, JsonHttpResponseHandler res) {
		client.get(context, urlString, entity, contentType, res);
	}

	/**
	 * post 带参数，获取json对象或者数组
	 * 
	 * @param urlString
	 * @param res
	 */
	public static void post(Context context, String urlString,
			HttpEntity entity, String contentType, JsonHttpResponseHandler res) {
		client.post(context, urlString, entity, contentType, res);

	}

	/**
	 * post网络请求，返回JSON数据
	 * 
	 * @param urlString
	 * @param res
	 * @param contentType
	 *            application/x-jpg;charset=UTF-8
	 * 
	 */
	public static void post(Context context, String urlString,
			RequestParams params, String contentType,
			JsonHttpResponseHandler res) {

		// RequestParams params=new RequestParams();
		// params.p
		List<Header> headersList = new ArrayList<Header>();
		Header[] headers = headersList.toArray(new Header[headersList.size()]);
		client.post(context, urlString, headers, params, contentType, res);

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