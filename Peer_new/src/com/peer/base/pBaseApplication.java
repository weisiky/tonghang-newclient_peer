package com.peer.base;

import java.util.ArrayList;
import java.util.List;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.peer.crash.CrashHandler;
import com.peer.utils.ImageLoaderUtil;
import com.peer.utils.pLog;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

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

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		pLog.isDebug = true;

		// 程序crash处理程序。
		CrashHandler.instance(this).init();
		// 初始化图片加载程序
		ImageLoaderUtil.getInstance();

		registNetworkReceiver();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		ImageLoader.getInstance().init(config);

	}

	public static pBaseApplication getInstance() {
		return instance;
	}

	@Override
	public void onTerminate() {
		super.onTerminate();

		unRegisterReceiver();
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
