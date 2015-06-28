package com.peer.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences的工具类
 * @author zhzhg
 * @version 1.0.0
 *
 */
public class pShareFileUtils {

	public static SharedPreferences mPreference = null;// 共享文件
	
	/**
	 * 初始化SharedPreferences
	 * @param context
	 * @param fileName 文件名
	 * @param mode 操作模式
	 */
	public void initSharePre(Context context,String fileName,int mode) {
		mPreference = context.getSharedPreferences(fileName, mode); 
	}
	
	
    /**
     * 从共享文件中获取字符串
     * 
     * @param key
     *            表签名
     * @param defValue
     *            值
     */
    public static String getString(String key, String defValue) {
        return mPreference.getString(key, defValue);
    }

    /**
     * 从共享文件中获取整型数据
     * 
     * @param key
     *            表签名
     * @param defValue
     *            值
     */
    public static int getInt(String key, int defValue) {
        return mPreference.getInt(key, defValue);
    }

    /**
     * 从共享文件中获取boolean数据
     * 
     * @param key
     *            表签名
     * @param defValue
     *            值
     */
    public static boolean getBoolean(String key, boolean defValue) {
        return mPreference.getBoolean(key, defValue);
    }

    /**
     * 保存字符串数据
     * 
     * @param key
     *            表签名
     * @param defValue
     *            值
     */
    public static void setString(String key, String defValue) {
    	mPreference.edit().putString(key, defValue).commit();
    }

    /**
     * 保存整型数据
     * 
     * @param key
     *            表签名
     * @param defValue
     *            值
     */
    public static void setInt(String key, int defValue) {
    	mPreference.edit().putInt(key, defValue).commit();
    }

    /**
     * 保存boolean数据
     * 
     * @param key
     *            表签名
     * @param defValue
     *            值
     */
    public static void setBoolean(String key, boolean defValue) {
    	mPreference.edit().putBoolean(key, defValue).commit();
    }
}
