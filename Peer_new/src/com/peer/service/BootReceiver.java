package com.peer.service;

import com.easemob.chat.EMChatService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
	public BootReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
			Intent service = new Intent(context, EMChatService.class);
			context.startService(service);
		}
	}
}
