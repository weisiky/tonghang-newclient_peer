package com.peer.utils;

import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JsonDocHelper {
	static Gson gson = null;

	/***
	 * 
	 * 将对象序列化为JSON文本
	 * 
	 * @param object
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String toJSONString(Object object) throws Exception {
		try {
			if (gson == null) {
				gson = new Gson();
			}
			String str = gson.toJson(object);
			return str;

		} catch (Exception ex) {
			throw new Exception("Convert Process in Exception");
		}
	}

	public static <T> T toJSONObject(String json, Class<T> cls)
			throws Exception {
		try {
			if (gson == null) {
				gson = new Gson();
			}
			pLog.i("test", "json:"+json);
			Object obj = gson.fromJson(json, cls);

			return (T) obj;

		} catch (Exception ex) {
			pLog.i("test", "Exception:"+ex.toString());
			throw new Exception("Convert Process in Exception");
		}
	}

	public static <T> ArrayList<T> toJSONArrary(String json, Class<T> cls) {
		if (gson == null) {
			gson = new Gson();
		}

		ArrayList<T> retData = gson.fromJson(json,
				new TypeToken<ArrayList<T>>() {
				}.getType());
		ArrayList<T> retList = new ArrayList<T>();
		for (T t_data : retData) {
			T obj = gson.fromJson(t_data.toString(), cls);
			retList.add(obj);
		}

		return retList;
	}

}
