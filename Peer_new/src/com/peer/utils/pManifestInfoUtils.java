package com.peer.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

/**
 * ��Ҫ��ȡAndroidManifest.xml������Ϣ
 * 
 * @author zhzhg
 * @version 1.0.0
 */
public class pManifestInfoUtils {
	private Context context;

	public pManifestInfoUtils(Context context) {
		this.context = context;
	}

	/**
	 * ��ȡapp������
	 * 
	 * @return
	 */
	public String getAppName() {
		String appName = null;
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			appName = context.getString(info.applicationInfo.labelRes);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return appName;
	}

	/**
	 * ��ȡicon drawable��id
	 * 
	 * @return
	 */
	public int getAppIcon() {
		int appIcon = 0;
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			appIcon = info.applicationInfo.icon;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return appIcon;
	}

	/**
	 * ��ȡversioncode
	 * 
	 * @return
	 */
	public int getVersionCode() {
		int versionCode = 0;
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			versionCode = info.versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * ��ȡversionName
	 * 
	 * @return
	 */
	public String getVersionName() {
		String versionName = "";
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			versionName = info.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return versionName;
	}

	/**
	 * ��ȡmanifest�����ļ����metaֵ
	 * 
	 * @param key
	 *            meta��name
	 * @return
	 */
	public String getMetaData(String key) {
		String value = "";
		try {
			ApplicationInfo aInfo = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			Bundle bundle = aInfo.metaData;
			if (bundle != null) {
				value = bundle.getString(key);
			} else {
				return "";
			}

		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}

		return value;
	}
}
