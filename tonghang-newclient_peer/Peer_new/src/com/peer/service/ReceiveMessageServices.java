package com.peer.service;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.json.JSONObject;

import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.peer.IMController.ShowNotification;
import com.peer.base.Constant;
import com.peer.bean.LoginBean;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.pLog;
import com.peer.utils.pShareFileUtils;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

/**
 * 接收环信消息
 * 
 * @author Kelvin
 * @version 1.0.0
 */

public class ReceiveMessageServices extends Service implements EMEventListener {

	protected ShowNotification showNotification;
	public NotificationManager notificationManager;

	/** 共享文件工具类 **/
	public pShareFileUtils mShareFileUtils = new pShareFileUtils();

	private static final int UPDATE_NEW_MESSAGE_TEXT = 100;

	/** 服务是否正在运行中 **/
	private boolean isRuning = false;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (!isRuning) {
			isRuning = true;
			notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			showNotification = new ShowNotification(notificationManager);

			mShareFileUtils.initSharePre(getApplicationContext(),
					Constant.SHARE_NAME, 0);
			
			EMChatManager.getInstance().registerEventListener(
					this,
					new EMNotifierEvent.Event[] {
							EMNotifierEvent.Event.EventNewMessage,
							EMNotifierEvent.Event.EventOfflineMessage,
							EMNotifierEvent.Event.EventDeliveryAck,
							EMNotifierEvent.Event.EventReadAck });
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		isRuning = false;
//		Intent i = new Intent(getApplication(), ReceiveMessageServices.class);
//		getApplication().startService(i);
		Intent setAlertIntent = new Intent(getApplicationContext(),
				ReceiveMessageReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				getApplicationContext(), 1, setAlertIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				System.currentTimeMillis(), 5000, pendingIntent);
	}

	/**
	 * 环信聊天监听
	 */

	@SuppressWarnings("unused")
	@Override
	public void onEvent(EMNotifierEvent event) {
		// TODO Auto-generated method stub
		switch (event.getEvent()) {
		case EventNewMessage: {
			// 获取到message
			EMMessage message = (EMMessage) event.getData();
			Message message1 = new Message();
			message1.what = UPDATE_NEW_MESSAGE_TEXT;
			message1.obj = message;
			handler.sendMessage(message1);

			break;
		}
		case EventDeliveryAck: {
			// 获取到message
			EMMessage message = (EMMessage) event.getData();
			break;
		}
		case EventReadAck: {
			// 获取到message
			EMMessage message = (EMMessage) event.getData();
			break;
		}
		case EventOfflineMessage: {
			EMMessage message = (EMMessage) event.getData();
			break;
		}
		default:
			break;
		}
	}

	/**
	 * 获取用户信息接口
	 * 
	 * @param client_id
	 * @throws UnsupportedEncodingException
	 */
	private void senduser(String client_id, String o_client_id,
			final EMMessage message) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		final Intent intent = new Intent();

		RequestParams params = null;
		try {
			params = PeerParamsUtils.getUserParams(getApplicationContext(),
					client_id);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		HttpUtil.post(HttpConfig.USER_IN_URL + client_id + ".json?client_id="
				+ o_client_id, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				try {
					JSONObject result = response.getJSONObject("success");

					pLog.i("zzg", "result:" + result.toString());

					String code = result.getString("code");

					if (code.equals("200")) {
						LoginBean loginBean = JsonDocHelper.toJSONObject(
								response.getJSONObject("success").toString(),
								LoginBean.class);
						if (loginBean != null) {
							// singlenotifyNewMessage(loginBean, message);

							pLog.i("zzg",
									"getUsername:"
											+ loginBean.user.getUsername());

							showNotification.sendNotification(
									getApplicationContext(), message,
									mShareFileUtils, false,
									loginBean.user.getUsername());
						}
					} else if (code.equals("500")) {

					} else {
						String message = result.getString("message");
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				super.onSuccess(statusCode, headers, response);

			}

		});
	}

	Handler handler = new Handler() {
		@SuppressWarnings("static-access")
		@Override
		public void handleMessage(android.os.Message message) {
			switch (message.what) {
			case UPDATE_NEW_MESSAGE_TEXT:
				EMMessage msg = (EMMessage) message.obj;
				pLog.i("test", "msg:" + mShareFileUtils.getBoolean(Constant.SERVICEISRUN, true));
				try {
					if (mShareFileUtils.getBoolean(Constant.SERVICEISRUN, true)) {
						senduser(msg.getFrom(), mShareFileUtils.getString(
								Constant.CLIENT_ID, ""), msg);
					}
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
			default:
				break;
			}
		}
	};

}
