package com.peer.utils;

import org.apache.http.HttpHost;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

/**
 * ���繤���࣬��Ҫ�Ե�ǰ�豸������״̬�����ͽ����ж�
 * @author zhzhg
 * @version 1.0.0
 */
public class pNetUitls {
	public static final int TYPE_MOBILE_CMNET = 1;
	public static final int TYPE_MOBILE_CMWAP = 2;
	public static final int TYPE_WIFI = 3;
	public static final int TYPE_NO = 0;
	public static final int TYPE_MOBILE_CTWAP = 4; // �ƶ���������
	
	
	/**
	 * 检查当前网络是否可用
	 * 
	 * @param context
	 * @return
	 */

	public static boolean isNetworkAvailable(Context context) {
		
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		ConnectivityManager connectivityManager = (ConnectivityManager) 
				context.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivityManager != null) {
			NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
			if (networkInfo != null) {
				for (NetworkInfo info : networkInfo) {
					if (info.getState() == State.CONNECTED) {
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * ��õ�ǰ��������
	 * @param mContext ������
	 * @return TYPE_MOBILE_CMNET:1 TYPE_MOBILE_CMWAP:2 TYPE_WIFI:3 TYPE_NO:0(δ֪����)
	 */
	public static int getNetWorkType(Context mContext) {
		ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		// ��õ�ǰ������Ϣ
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isAvailable()) {
			int currentNetWork = networkInfo.getType();
			if (currentNetWork == ConnectivityManager.TYPE_MOBILE) {
				if (networkInfo.getExtraInfo() != null && networkInfo.getExtraInfo().equals("cmwap")) {
					// ��ǰ����Ϊ:cmwap����
					return TYPE_MOBILE_CMWAP;
				} else if (networkInfo.getExtraInfo() != null && networkInfo.getExtraInfo().equals("uniwap")) {
					// ��ǰ����Ϊ:uniwap����
					return TYPE_MOBILE_CMWAP;
				} else if (networkInfo.getExtraInfo() != null && networkInfo.getExtraInfo().equals("3gwap")) {
					// ��ǰ����Ϊ:3gwap����
					return TYPE_MOBILE_CMWAP;
				} else if (networkInfo.getExtraInfo() != null && networkInfo.getExtraInfo().contains("ctwap")) {
					// ��ǰ����Ϊctwap����
					return TYPE_MOBILE_CTWAP;
				} else {
					// ��ǰ����Ϊ:net����
					return TYPE_MOBILE_CMNET;
				}

			} else if (currentNetWork == ConnectivityManager.TYPE_WIFI) {
				// ��ǰ����Ϊ:WIFI����
				return TYPE_WIFI;
			} else{
				// δ֪���ƶ�����
				return TYPE_MOBILE_CMNET;
			}
		}
		// ��ǰ����Ϊ:�������ǿ��ǵ�����
		return TYPE_NO;
	}

	/**
	 * ��ȡ����host
	 * @param context ������
	 * @return ����host
	 */
	public static HttpHost getProxyHost(Context context) {
		HttpHost proxy = null;
		int tNetType = getNetWorkType(context);
		if (TYPE_MOBILE_CTWAP == tNetType) {
			proxy = new HttpHost("10.0.0.200", 80);
		} else if (TYPE_MOBILE_CMWAP == tNetType) {
			proxy = new HttpHost("10.0.0.172", 80);
		}
		return proxy;
	}

}
