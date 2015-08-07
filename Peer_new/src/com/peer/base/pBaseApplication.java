package com.peer.base;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;

import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMChatService;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.peer.crash.CrashHandler;
import com.peer.service.ListenBroadcastReceiver;
import com.peer.utils.ImageLoaderUtil;
import com.peer.utils.pLog;

/**
 * 基础Application
 * 
 * @author zhangzg
 * 
 */

public class pBaseApplication extends Application {

	private List<OnNetworkStatusListener> networkStatusListeners = new ArrayList<pBaseApplication.OnNetworkStatusListener>();

	private static pBaseApplication instance;
	private BroadcastReceiver netWorkReceiver;
	/** 控制更新参数 **/
	public static boolean updateflag = true;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		pLog.isDebug = true;

		// 程序crash处理程序。
		CrashHandler.instance(this).init();
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
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		ImageLoader.getInstance().init(config);

		// 初始化环信
		initEMChat();

		// 极光PUSH
		JPushInterface.setDebugMode(false);
		JPushInterface.init(this);
		// ShareSDK初始化
		ShareSDK.initSDK(this);
		
		IntentFilter filter=new IntentFilter();
		filter.addAction(Intent.ACTION_TIME_TICK);
		ListenBroadcastReceiver receiver=new ListenBroadcastReceiver();
		registerReceiver(receiver, filter);

	}

	/* 初始化环信sdk */
	private void initEMChat() {
		// TODO Auto-generated method stub
		int pid = android.os.Process.myPid();
		String processAppName = getAppName(pid);

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

}
