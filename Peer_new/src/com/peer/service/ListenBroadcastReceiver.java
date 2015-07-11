package com.peer.service;

import com.easemob.chat.EMChatService;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ListenBroadcastReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		boolean isServiceRunning = false;
		if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
			// Toast.makeText(context, "广播启动serveice", 0).show();
			ActivityManager manager = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			for (RunningServiceInfo service : manager
					.getRunningServices(Integer.MAX_VALUE)) {
				if ("com.easemob.chat.EMChatService".equals(service.service
						.getClassName())) {
					isServiceRunning = true;
				}
			}
			if (!isServiceRunning) {
				Intent i = new Intent(context, EMChatService.class);
				context.startService(i);
			}
		}
	}

}
