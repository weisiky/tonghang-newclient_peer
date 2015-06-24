package com.peer.base;

import com.peer.utils.pLog;

import android.app.Application;

/**
 * »ù´¡Application
 * 
 * @author zhangzg
 *
 */



public class pBaseApplication extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		pLog.isDebug = true;

	}

}
