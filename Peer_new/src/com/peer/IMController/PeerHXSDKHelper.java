package com.peer.IMController;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.EMChatRoomChangeListener;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.util.EMLog;
import com.easemob.util.EasyUtils;

/**
 * Demo UI HX SDK helper class which subclass HXSDKHelper
 * 
 * @author easemob
 * 
 */
public class PeerHXSDKHelper extends HXSDKHelper {

	private static final String TAG = "DemoHXSDKHelper";

	/**
	 * EMEventListener
	 */
	protected EMEventListener eventListener = null;
	/**
	 * 用来记录foreground Activity
	 */
	private List<Activity> activityList = new ArrayList<Activity>();

	public void pushActivity(Activity activity) {
		if (!activityList.contains(activity)) {
			activityList.add(0, activity);
		}
	}

	public void popActivity(Activity activity) {
		activityList.remove(activity);
	}

	@Override
	public synchronized boolean onInit(Context context) {
		return super.onInit(context);
	}

	@Override
	protected void initListener() {
		super.initListener();
		IntentFilter callFilter = new IntentFilter(EMChatManager.getInstance()
				.getIncomingCallBroadcastAction());
		// 注册消息事件监听
		initEventListener();
	}

	/**
	 * 全局事件监听 因为可能会有UI页面先处理到这个消息，所以一般如果UI页面已经处理，这里就不需要再次处理 activityList.size()
	 * <= 0 意味着所有页面都已经在后台运行，或者已经离开Activity Stack
	 */
	protected void initEventListener() {
		eventListener = new EMEventListener() {
			private BroadcastReceiver broadCastReceiver = null;

			@Override
			public void onEvent(EMNotifierEvent event) {
				EMMessage message = null;
				if (event.getData() instanceof EMMessage) {
					message = (EMMessage) event.getData();
					EMLog.d(TAG, "receive the event : " + event.getEvent()
							+ ",id : " + message.getMsgId());
				}

				switch (event.getEvent()) {
				case EventNewMessage:
					// 应用在后台，不需要刷新UI,通知栏提示新消息
					if (activityList.size() <= 0) {
						HXSDKHelper.getInstance().getNotifier()
								.onNewMsg(message);
					}
					break;
				case EventOfflineMessage:
					if (activityList.size() <= 0) {
						EMLog.d(TAG, "received offline messages");
						List<EMMessage> messages = (List<EMMessage>) event
								.getData();
						HXSDKHelper.getInstance().getNotifier()
								.onNewMesg(messages);
					}
					break;
				// below is just giving a example to show a cmd toast, the app
				// should not follow this
				// so be careful of this
				case EventNewCMDMessage: {

					EMLog.d(TAG, "收到透传消息");
					// 获取消息body
					CmdMessageBody cmdMsgBody = (CmdMessageBody) message
							.getBody();
					final String action = cmdMsgBody.action;// 获取自定义action

					// 获取扩展属性 此处省略
					// message.getStringAttribute("");
					EMLog.d(TAG, String.format("透传消息：action:%s,message:%s",
							action, message.toString()));

					final String CMD_TOAST_BROADCAST = "easemob.demo.cmd.toast";
					IntentFilter cmdFilter = new IntentFilter(
							CMD_TOAST_BROADCAST);

					if (broadCastReceiver == null) {
						broadCastReceiver = new BroadcastReceiver() {

							@Override
							public void onReceive(Context context, Intent intent) {
								// TODO Auto-generated method stub
								Toast.makeText(appContext,
										intent.getStringExtra("cmd_value"),
										Toast.LENGTH_SHORT).show();
							}
						};

						// 注册广播接收者
						appContext.registerReceiver(broadCastReceiver,
								cmdFilter);
					}

					Intent broadcastIntent = new Intent(CMD_TOAST_BROADCAST);
					appContext.sendBroadcast(broadcastIntent, null);

					break;
				}
				case EventDeliveryAck:
					message.setDelivered(true);
					break;
				case EventReadAck:
					message.setAcked(true);
					break;
				// add other events in case you are interested in
				default:
					break;
				}

			}
		};

		EMChatManager.getInstance().registerEventListener(eventListener);

		EMChatManager.getInstance().addChatRoomChangeListener(
				new EMChatRoomChangeListener() {
					private final static String ROOM_CHANGE_BROADCAST = "easemob.demo.chatroom.changeevent.toast";
					private final IntentFilter filter = new IntentFilter(
							ROOM_CHANGE_BROADCAST);
					private boolean registered = false;

					private void showToast(String value) {
						if (!registered) {
							// 注册广播接收者
							appContext.registerReceiver(
									new BroadcastReceiver() {

										@Override
										public void onReceive(Context context,
												Intent intent) {
											Toast.makeText(
													appContext,
													intent.getStringExtra("value"),
													Toast.LENGTH_SHORT).show();
										}

									}, filter);

							registered = true;
						}

						Intent broadcastIntent = new Intent(
								ROOM_CHANGE_BROADCAST);
						broadcastIntent.putExtra("value", value);
						appContext.sendBroadcast(broadcastIntent, null);
					}

					@Override
					public void onChatRoomDestroyed(String roomId,
							String roomName) {
						showToast(" room : " + roomId + " with room name : "
								+ roomName + " was destroyed");
						Log.i("info", "onChatRoomDestroyed=" + roomName);
					}

					@Override
					public void onMemberJoined(String roomId, String participant) {
						showToast("member : " + participant
								+ " join the room : " + roomId);
						Log.i("info", "onmemberjoined=" + participant);

					}

					@Override
					public void onMemberExited(String roomId, String roomName,
							String participant) {
						showToast("member : " + participant
								+ " leave the room : " + roomId
								+ " room name : " + roomName);
						Log.i("info", "onMemberExited=" + participant);

					}

					@Override
					public void onMemberKicked(String roomId, String roomName,
							String participant) {
						showToast("member : " + participant
								+ " was kicked from the room : " + roomId
								+ " room name : " + roomName);
						Log.i("info", "onMemberKicked=" + participant);

					}

				});
	}

	@Override
	public HXNotifier createNotifier() {
		return new HXNotifier() {
			public synchronized void onNewMsg(final EMMessage message) {
				if (EMChatManager.getInstance().isSlientMessage(message)) {
					return;
				}

				String chatUsename = null;
				List<String> notNotifyIds = null;
				// 获取设置的不提示新消息的用户或者群组ids

				if (notNotifyIds == null || !notNotifyIds.contains(chatUsename)) {
					// 判断app是否在后台
					if (!EasyUtils.isAppRunningForeground(appContext)) {
						EMLog.d(TAG, "app is running in backgroud");
						sendNotification(message, false);
					} else {
						sendNotification(message, true);

					}

					viberateAndPlayTone(message);
				}
			}
		};
	}

	@Override
	public void logout(final boolean unbindDeviceToken,
			final EMCallBack callback) {
		endCall();
		super.logout(unbindDeviceToken, new EMCallBack() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				if (callback != null) {
					callback.onSuccess();
				}
			}

			@Override
			public void onError(int code, String message) {
				// TODO Auto-generated method stub
				if (callback != null) {
					callback.onError(code, message);
				}
			}

			@Override
			public void onProgress(int progress, String status) {
				// TODO Auto-generated method stub
				if (callback != null) {
					callback.onProgress(progress, status);
				}
			}

		});
	}

	void endCall() {
		try {
			EMChatManager.getInstance().endCall();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected HXSDKModel createModel() {
		// TODO Auto-generated method stub
		return null;
	}

}
