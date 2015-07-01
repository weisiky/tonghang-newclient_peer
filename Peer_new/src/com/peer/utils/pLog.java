package com.peer.utils;

import android.util.Log;

/**
 * ��־��ӡ��
 * 
 * @author zhzhg
 * @version 1.0.0
 */
public class pLog {

	/** �Ƿ��ӡ��־ **/
	public static boolean isDebug = true;
	/** ��־��ǩ **/
	public static String LOG_TAG = "frame";

	public static void v(String tag, String msg) {
		if (isDebug) {
			Log.v(tag, msg);
		}
	}

	public static void i(String tag, String msg) {
		if (isDebug) {
			Log.i(tag, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (isDebug) {
			Log.d(tag, msg);
		}
	}

	public static void w(String tag, String msg) {
		if (isDebug) {
			Log.w(tag, msg);
		}
	}

	public static void w(String tag, Throwable tr) {
		if (isDebug) {
			Log.w(tag, tr);
		}
	}

	public static void e(String tag, String msg) {
		if (isDebug) {
			Log.e(tag, msg);
		}
	}

	public static void print(String tag, String msg) {
		// System.out.println("tag=="+msg);
	}
}
