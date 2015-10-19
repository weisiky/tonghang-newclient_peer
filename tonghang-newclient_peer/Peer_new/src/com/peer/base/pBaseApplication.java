package com.peer.base;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;

import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMChatService;
import com.easemob.chat.EMMessage;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.peer.IMController.ShowNotification;
import com.peer.bean.LoginBean;
import com.peer.crash.CrashHandler;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.service.ListenBroadcastReceiver;
import com.peer.service.ReceiveMessageReceiver;
import com.peer.utils.ImageLoaderUtil;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.pLog;
import com.peer.utils.pShareFileUtils;

/**
 * 基础Application
 * 
 * @author zhangzg
 * 
 */

public class pBaseApplication extends Application implements EMEventListener {

	private List<OnNetworkStatusListener> networkStatusListeners = new ArrayList<pBaseApplication.OnNetworkStatusListener>();

	private static pBaseApplication instance;
	private BroadcastReceiver netWorkReceiver;
	/** 控制更新参数 **/
	public static boolean updateflag = true;

	protected ShowNotification showNotification;
	public NotificationManager notificationManager;

	/** 共享文件工具类 **/
	public pShareFileUtils mShareFileUtils = new pShareFileUtils();

	private static final int UPDATE_NEW_MESSAGE_TEXT = 100;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		pLog.isDebug = true;

		// 程序crash处理程序。
//		 CrashHandler.instance(this).init();
		// 初始化图片加载程序
		ImageLoaderUtil.getInstance();
		// 注册网络监听器
		registNetworkReceiver();

		// 图片缓存路径
		File cacheDir = new File(Constant.C_IMAGE_CACHE_PATH);

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this).threadPriority(Thread.NORM_PRIORITY - 2)
		 .denyCacheImageMultipleSizesInMemory()
		 .discCache(new UnlimitedDiscCache(cacheDir))
		 .discCacheFileNameGenerator(new Md5FileNameGenerator())
		 .tasksProcessingOrder(QueueProcessingType.LIFO)
				.build();
		ImageLoader.getInstance().init(config);
		

		// 初始化环信
		initEMChat();

		// 极光PUSH
		JPushInterface.setDebugMode(false);
		JPushInterface.init(this);
		// ShareSDK初始化
		ShareSDK.initSDK(this);

		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_TIME_TICK);
		ListenBroadcastReceiver receiver = new ListenBroadcastReceiver();
		registerReceiver(receiver, filter);

		isExitFile();

		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		showNotification = new ShowNotification(notificationManager);

		mShareFileUtils.initSharePre(getApplicationContext(),
				Constant.SHARE_NAME, 0);

		initReceiveMessageServices();

	}
	

	/**
	 * 注册不死服务
	 */
	private void initReceiveMessageServices() {
		// TODO Auto-generated method stub
		Intent setAlertIntent = new Intent(getApplicationContext(),
				ReceiveMessageReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				getApplicationContext(), 1, setAlertIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				System.currentTimeMillis(), 5000, pendingIntent);
		
//		EMChatManager.getInstance().registerEventListener(
//				this,
//				new EMNotifierEvent.Event[] {
//						EMNotifierEvent.Event.EventNewMessage,
//						EMNotifierEvent.Event.EventOfflineMessage,
//						EMNotifierEvent.Event.EventDeliveryAck,
//						EMNotifierEvent.Event.EventReadAck });
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
		Log.i("zzg", "onLowMemory:+onLowMemory");
	}

	@Override
	public void onTrimMemory(int level) {
		// TODO Auto-generated method stub
		super.onTrimMemory(level);
		Log.i("zzg", "pBaseApplication:+onTrimMemory");
	}

	private void isExitFile() {
		// TODO Auto-generated method stub
		File file = new File(Constant.DEFAULT_MAIN_DIRECTORY);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	/* 初始化环信sdk */
	private void initEMChat() {
		// TODO Auto-generated method stub
		int pid = android.os.Process.myPid();
		String processAppName = getAppName(pid);
		// 如果app启用了远程的service，此application:onCreate会被调用2次
		// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
		// 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process
		// name就立即返回

		if (processAppName == null
				|| !processAppName.equalsIgnoreCase("com.peer")) {
			// "com.easemob.chatuidemo"为demo的包名，换到自己项目中要改成自己包名
			// 则此application::onCreate 是被service 调用的，直接返回
			return;
		}

		EMChat.getInstance().init(this);

		EMChatManager.getInstance().getChatOptions()
				.setShowNotificationInBackgroud(false);
		EMChatOptions options = EMChatManager.getInstance().getChatOptions();
		options.setNotifyBySoundAndVibrate(false);
		EMChat.getInstance().setDebugMode(true);
	}

	private String getAppName(int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager) this
				.getSystemService(ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		PackageManager pm = this.getPackageManager();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i
					.next());
			try {
				if (info.pid == pID) {
					CharSequence c = pm.getApplicationLabel(pm
							.getApplicationInfo(info.processName,
									PackageManager.GET_META_DATA));
					processName = info.processName;
					return processName;
				}
			} catch (Exception e) {
			}
		}
		return processName;
	}

	public static pBaseApplication getInstance() {
		return instance;
	}

	@Override
	public void onTerminate() {
		super.onTerminate();

		unRegisterReceiver();

		Intent serviceIntent = new Intent(this, EMChatService.class);
		startService(serviceIntent);
	}

	public void unRegisterReceiver() {
		if (netWorkReceiver != null) {
			unregisterReceiver(netWorkReceiver);
			netWorkReceiver = null;
		}
	}

	public void addNetworkStatusListener(OnNetworkStatusListener listener) {
		networkStatusListeners.add(listener);
	}

	public void removeNetworkStatusListener(OnNetworkStatusListener listener) {
		networkStatusListeners.remove(listener);
	}

	/**
	 * 注册广播接受，在网络状态改变时接受系统发送的广播
	 */
	protected void registNetworkReceiver() {
		if (netWorkReceiver == null) {
			netWorkReceiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent
							.getAction())) {
						NetworkInfo tmpInfo = (NetworkInfo) intent.getExtras()
								.get(ConnectivityManager.EXTRA_NETWORK_INFO);
						NetworkInfo.State state = tmpInfo.getState();

						if (state == State.DISCONNECTED) {
							for (OnNetworkStatusListener listener : networkStatusListeners) {
								listener.netWorkOff();
							}
						} else if (state == State.CONNECTED) {
							for (OnNetworkStatusListener listener : networkStatusListeners) {
								listener.networkOn();
							}
						}
					}
				}
			};
		}

		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(netWorkReceiver, mFilter);
	}

	public static interface OnNetworkStatusListener {
		/**
		 * 网络连通后回调该函数
		 */
		public void networkOn();

		/**
		 * 网络断开后回调该函数
		 */
		public void netWorkOff();
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
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpUtil.post(HttpConfig.USER_IN_URL + client_id + ".json?client_id="
				+ o_client_id, params, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONArray errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

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
				pLog.i("zzg", "msg:" + msg.getFrom());
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
