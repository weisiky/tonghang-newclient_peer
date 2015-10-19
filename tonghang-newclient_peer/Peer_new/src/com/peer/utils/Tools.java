package com.peer.utils;


import android.os.Environment;
/**
 * sd card tools
 * @author Cocoon-break
 *
 */
public class Tools {
	
	public static boolean hasSdcard(){
		String state = Environment.getExternalStorageState();
		if(state.equals(Environment.MEDIA_MOUNTED)){
			return true;
		}else{
			return false;
		}
	}
}
