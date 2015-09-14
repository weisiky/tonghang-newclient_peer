package com.peer.IMController;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.util.EMLog;
import com.easemob.util.EasyUtils;
import com.peer.R;
import com.peer.activity.MainActivity;
import com.peer.utils.pLog;
import com.peer.utils.pShareFileUtils;

/**
 * 新消息提醒class 2.1.8把新消息提示相关的api移除出sdk，方便开发者自由修改 开发者也可以继承此类实现相关的接口
 * 
 * this class is subject to be inherited and implement the relative APIs
 */
public class ShowNotification {

	protected String[] msgs;
	protected final static String[] msg_ch = { "发来一条消息", "发来一张图片", "发来一段语音",
			"发来位置信息", "发来一个视频", "发来一个文件", "%1个联系人发来%2条消息" };
	protected NotificationManager notificationManager;

	protected long lastNotifiyTime=0;

	public ShowNotification(NotificationManager notificationManager) {
		super();
		this.notificationManager = notificationManager;
		msgs=msg_ch;
	}

	/**
	 * 发送通知栏提示 This can be override by subclass to provide customer
	 * implementation
	 * 
	 * @param message
	 */
	@SuppressWarnings({ "deprecation", "incomplete-switch" })
	public void sendNotification(Context content, EMMessage message,
			pShareFileUtils mShareFileUtils, boolean isShowTips,String user_name) {
		
		pLog.i("zzg", "message:" + message.toString());
		
		String username = message.getFrom();
		String notifyText =user_name+" ";
		switch (message.getType()) {
		case TXT:
			TextMessageBody txtBody = (TextMessageBody) message.getBody();
			notifyText+="对你说"+txtBody.getMessage();
			break;
		case IMAGE:
			notifyText += msgs[1];
			break;
		case VOICE:

			notifyText += msgs[2];
			break;
		case LOCATION:
			notifyText += msgs[3];
			break;
		case VIDEO:
			notifyText += msgs[4];
			break;
		case FILE:
			notifyText += msgs[5];
			break;
		}
		Calendar c = Calendar.getInstance();
		int hours = c.get(Calendar.HOUR_OF_DAY);
		int munite = c.get(Calendar.MINUTE);
		// 构建一个通知对象(需要传递的参数有三个,分别是图标,标题和 时间)
		Notification notification = new Notification(R.drawable.logo, "同行",
				System.currentTimeMillis());
		Intent intent;
		intent = new Intent(content, MainActivity.class);

		PendingIntent pendingIntent = PendingIntent.getActivity(content, 0,
				intent, 0);
		notification.setLatestEventInfo(content, "通知标题", "通知显示的内容",
				pendingIntent);
		notification.setLatestEventInfo(content, "同行", notifyText, pendingIntent);
		notification.flags = Notification.FLAG_AUTO_CANCEL;// 点击后自动消失

		if (System.currentTimeMillis() - lastNotifiyTime < 1000) {
			// received new messages within 2 seconds, skip play ringtone
			return;
		}

		lastNotifiyTime = System.currentTimeMillis();
		ToNotifyStyle(notification, mShareFileUtils);

		notificationManager.notify(1, notification);// 发动通知,id由自己指定，每一个Notification对应的唯一标志
		if (isShowTips) {
			notificationManager.cancel(1);
		}

	}

	@SuppressWarnings("static-access")
	private void ToNotifyStyle(Notification notification,
			pShareFileUtils mShareFileUtils) {
		// TODO Auto-generated method stub
		if (mShareFileUtils.getBoolean("sound", true)
				&& mShareFileUtils.getBoolean("vibrate", true)) {
			notification.defaults = Notification.DEFAULT_SOUND
					| Notification.DEFAULT_VIBRATE;
		} else {
			if (mShareFileUtils.getBoolean("sound", true)) {
				notification.defaults = Notification.DEFAULT_SOUND;// 声音默认
			} else if (mShareFileUtils.getBoolean("vibrate", true)) {
				notification.defaults = Notification.DEFAULT_VIBRATE;
			}
		}

	}

}
