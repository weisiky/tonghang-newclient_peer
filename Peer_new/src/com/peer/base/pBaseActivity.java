package com.peer.base;

import java.util.Calendar;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.util.NetUtils;
import com.peer.IMimplements.easemobchatImp;
import com.peer.activity.LoginActivity;
import com.peer.activity.MainActivity;
import com.peer.activity.MyAcountActivity;
import com.peer.activity.PersonalPageActivity;
import com.peer.R;
import com.peer.activity.RegisterAcountActivity;
import com.peer.activity.SearchUserActivity;
import com.peer.activity.SettingActivity;
import com.peer.activity.MainActivity.IMconnectionListner;
import com.peer.base.pBaseApplication.OnNetworkStatusListener;
import com.peer.bean.ChatRoomBean;
import com.peer.service.FxService;
import com.peer.utils.BussinessUtils;
import com.peer.utils.HomeWatcher;
import com.peer.utils.HomeWatcher.OnHomePressedListener;
import com.peer.utils.ManagerActivity;
import com.peer.utils.pLog;
import com.peer.utils.pNetUitls;
import com.peer.utils.pShareFileUtils;
import com.peer.utils.pSysInfoUtils;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

/**
 * 基础Activity
 * 
 * @author zhangzg
 * 
 */

public abstract class pBaseActivity extends FragmentActivity implements
		OnClickListener {

	/** 共享文件工具类 **/
	public pShareFileUtils mShareFileUtils = new pShareFileUtils();
	/** 退出倒计时 **/
	private long mExitTime;
	/** 提示条 **/
	public Toast toast;
	/** 中间布局 **/
	private RelativeLayout contentLayout;
	/** 顶部布局 **/
	private RelativeLayout topLayout;
	/** 底部布局 **/
	private RelativeLayout bottomLayout;
	/** 灰色布局 **/
	private LinearLayout shadeBg;
	/** 当前页面的名称 **/
	public String currentPageName = null;
	/** 蒙板渐变动画 **/
	public Animation alphaAnimation;
	/** 首次进入页面的loading圈圈 **/
	public RelativeLayout baseProgressBarLayout;
	// 网络状态
	public boolean isNetworkAvailable;
	public pBaseApplication application;

	private ActivityManager activityManager;
	// 网络状态监听
	private OnNetworkStatusListener onNetworkStatusListener;
	// Home键监听
	private HomeWatcher mHomeWatcher;

	protected NotificationManager notificationManager;

	private NewMessageBroadcastReceiver msgReceiver;

	private MainActivity mainActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base);

		// 当前页面名称
		currentPageName = getLocalClassNameBySelf();
		// 获取手机屏幕跨度，图片适配使用
		Constant.S_SCREEN_WIDHT_VALUE = pSysInfoUtils.getDisplayMetrics(this).widthPixels
				+ "";
		findGlobalViewById();
		// 初始化ShareUtiles
		initShareUtils();
		// 友盟统计 发送策略
		MobclickAgent.updateOnlineConfig(this);
		// 友盟统计 数据加密
		AnalyticsConfig.enableEncrypt(true);

		ManagerActivity.getAppManager().addActivity(this);

		this.findViewById();
		this.setListener();
		this.processBiz();

		application = (pBaseApplication) getApplication();

		onNetworkStatusListener = new OnNetworkStatusListener() {
			public void networkOn() {
				isNetworkAvailable = true;
				onNetworkOn();
			}

			public void netWorkOff() {
				isNetworkAvailable = false;
				onNetWorkOff();
			}
		};
		application.addNetworkStatusListener(onNetworkStatusListener);

		isNetworkAvailable = pNetUitls.isNetworkAvailable(this);

		HomeKeyWatcher();

		// 友盟统计 发送策略
		MobclickAgent.updateOnlineConfig(this);
		// 友盟统计 数据加密
		AnalyticsConfig.enableEncrypt(true);

		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		mainActivity = new MainActivity();
		
//		registerEMchat();


	}

	/**
	 * 环信注册监听
	 */
	private void registerEMchat() {
		// TODO Auto-generated method stub
		if (EMChatManager.getInstance().isConnected()) {
			msgReceiver = new NewMessageBroadcastReceiver();
			IntentFilter intentFilter = new IntentFilter(EMChatManager
					.getInstance().getNewMessageBroadcastAction());
			intentFilter.setPriority(3);
			registerReceiver(msgReceiver, intentFilter);
			EMChatManager.getInstance().addConnectionListener(
					new IMconnectionListner());
			EMChat.getInstance().setAppInited();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		EMChatManager.getInstance().activityResumed();
		MobclickAgent.onResume(this);
		
		
		/*
		 * 判断是否满足悬浮头像启动逻辑。满足——启动
		 */
		System.out.println("悬浮头像是否存在："
				+ mShareFileUtils.getBoolean(Constant.ISFLOAT, false));
		if (ChatRoomBean.getInstance().getTopicBean() != null) {
			if (mShareFileUtils.getBoolean(Constant.ISFLOAT, false)) {
				Intent resintent = new Intent(pBaseActivity.this,
						FxService.class);
				resintent.putExtra(Constant.F_IMAGE,
						mShareFileUtils.getString(Constant.F_IMAGE, ""));
				resintent.putExtra(Constant.F_OWNERNIKE,
						mShareFileUtils.getString(Constant.F_OWNERNIKE, ""));
				resintent.putExtra(Constant.F_THEME,
						mShareFileUtils.getString(Constant.F_THEME, ""));
				resintent.putExtra(Constant.F_TAGNAME,
						mShareFileUtils.getString(Constant.F_TAGNAME, ""));
				resintent.putExtra(Constant.F_USERID,
						mShareFileUtils.getString(Constant.F_USERID, ""));
				resintent.putExtra(Constant.F_ROOMID,
						mShareFileUtils.getString(Constant.F_ROOMID, ""));
				resintent.putExtra(Constant.F_TOPICID,
						mShareFileUtils.getString(Constant.F_TOPICID, ""));
				resintent.putExtra(Constant.FROMFLOAT,
						mShareFileUtils.getString(Constant.FROMFLOAT, ""));
				startService(resintent);
			}
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
		Intent intent = new Intent(pBaseActivity.this, FxService.class);// 悬浮头像停止运行
		stopService(intent);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mHomeWatcher.stopWatch();
	}

	/**
	 * 初始化共享工具类
	 */
	public void initShareUtils() {
		mShareFileUtils.initSharePre(getApplicationContext(),
				Constant.SHARE_NAME, 0);
	}

	/**
	 * 获取全局页面控件对象
	 */
	@SuppressWarnings("deprecation")
	protected void findGlobalViewById() {
		topLayout = (RelativeLayout) findViewById(R.id.topLayout);
		contentLayout = (RelativeLayout) findViewById(R.id.contentLayout);
		bottomLayout = (RelativeLayout) findViewById(R.id.bottomLayout);
		shadeBg = (LinearLayout) findViewById(R.id.shadeBg);

		RelativeLayout.LayoutParams layoutParamsContent = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT);
		RelativeLayout.LayoutParams layoutParamsTop = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams layoutParamsBottom = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		View topView = loadTopLayout();
		if (topView == null) {
			topLayout.setVisibility(View.GONE);
		} else {
			topLayout.setVisibility(View.VISIBLE);
			topLayout.addView(topView, layoutParamsTop);
		}
		View contentView = loadContentLayout();
		if (contentView == null) {
			contentLayout.setVisibility(View.GONE);
		} else {
			contentLayout.setVisibility(View.VISIBLE);
			contentLayout.addView(contentView, layoutParamsContent);
		}
		View bottomView = loadBottomLayout();
		if (bottomView == null) {
			bottomLayout.setVisibility(View.GONE);
		} else {
			bottomLayout.setVisibility(View.VISIBLE);
			bottomLayout.addView(bottomView, layoutParamsTop);
		}

		baseProgressBarLayout = (RelativeLayout) findViewById(R.id.baseProgressBarLayout);

		alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.shade_alpha);

	}

	/**
	 * 网络连通后回调该函数
	 */
	public abstract void onNetworkOn();

	/**
	 * 网络断开后回调该函数
	 */
	public abstract void onNetWorkOff();

	/**
	 * 获取页面控件对象
	 */
	protected abstract void findViewById();

	/**
	 * 绑定监听事件
	 */
	protected abstract void setListener();

	/**
	 * 处理业务
	 */
	protected abstract void processBiz();

	/**
	 * 获取顶部布局
	 */
	protected abstract View loadTopLayout();

	/**
	 * 获取中间布局
	 * 
	 */
	protected abstract View loadContentLayout();

	/**
	 * 获取底部布局
	 * 
	 */
	protected abstract View loadBottomLayout();

	/**
	 * 页面左侧进入
	 * 
	 * @param targetActivity
	 *            目标页面
	 * @param intent
	 * @param isNewActivity
	 *            是否需要一个新的页面
	 */
	public void startActivityForLeft(Class targetActivity, Intent intent,
			boolean isNewActivity) {
		if (!isNewActivity) {
			intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		}
		if (targetActivity != null) {
			intent.setClass(this, targetActivity);
			startActivity(intent);
			if (BussinessUtils.hasEclair()) {
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
			}
		}
		this.finish();
	}

	/**
	 * 页面从右侧进入
	 * 
	 * @param targetActivity
	 *            目标页面
	 * @param intent
	 * @param isNewActivity
	 *            是否需要一个新的页面
	 */
	@SuppressWarnings("rawtypes")
	public void startActivityRight(Class targetActivity, Intent intent,
			boolean isNewActivity) {
		if (!isNewActivity) {
			intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		}
		if (targetActivity != null) {
			intent.setClass(this, targetActivity);
			startActivity(intent);
			if (BussinessUtils.hasEclair()) {
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
			}
		}
		this.finish();
	}

	/**
	 * 页面从上到下进入
	 * 
	 * @param targetActivity
	 *            目标页面
	 * @param intent
	 * @param isNewActivity
	 *            是否需要一个新的页面
	 */
	public void startActivityForDown(Class targetActivity, Intent intent,
			boolean isNewActivity) {
		if (!isNewActivity) {
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		}
		if (targetActivity != null) {
			intent.setClass(this, targetActivity);
			startActivity(intent);
			if (BussinessUtils.hasEclair()) {
				overridePendingTransition(R.anim.push_down_in,
						R.anim.push_down_out);
			}
		}
		this.finish();
	}

	/**
	 * 页面从上到下进入
	 * 
	 * @param targetActivity
	 *            目标页面
	 * @param intent
	 * @param isNewActivity
	 *            是否需要一个新的页面
	 */
	public void startActivityForUp(Class targetActivity, Intent intent,
			boolean isNewActivity) {
		if (!isNewActivity) {
			intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		}
		if (targetActivity != null) {
			intent.setClass(this, targetActivity);
			startActivity(intent);
			if (BussinessUtils.hasEclair()) {
				overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
			}
		}

		this.finish();
	}

	/**
	 * 待返回结果页面跳转
	 * 
	 * @param targetActivity
	 *            目标页面
	 * @param intent
	 * @param requestCode
	 *            跳转码
	 * @param isNewActivity
	 *            是否需要一个新的页面
	 */
	public void startActivityForResult(Class targetActivity, Intent intent,
			int requestCode, boolean isNewActivity) {
		if (!isNewActivity) {
			intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		}
		if (targetActivity != null) {
			intent.setClass(this, targetActivity);
			startActivityForResult(intent, requestCode);
			if (BussinessUtils.hasEclair()) {
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
			}
		}
		this.finish();
	}

	/**
	 * 返回上一个页面
	 */
	public void backPage() {
		Intent intent = new Intent();

		if (getLocalClassNameBySelf().contains("RegisterAcountActivity")
				|| getLocalClassNameBySelf().contains("FindPasswordActivity")) {
			startActivityRight(LoginActivity.class, intent, false);
		} else if (getLocalClassNameBySelf().contains("Recommend_topic")
				|| getLocalClassNameBySelf().contains("ChatRoomActivity")) {
			startActivityRight(MainActivity.class, intent, true);
		} else if (getLocalClassNameBySelf().contains("xieyiActivity")
				|| getLocalClassNameBySelf().contains("GetAddressInfoActivity")
				|| getLocalClassNameBySelf().contains("SettingActivity")
				|| getLocalClassNameBySelf().contains("SearchUserActivity")
				|| getLocalClassNameBySelf().contains("CreatTopicActivity")
				|| getLocalClassNameBySelf().contains("MyAcountActivity")
				|| getLocalClassNameBySelf().contains("MultiChatRoomActivity")
				|| getLocalClassNameBySelf()
						.contains("PersonalMessageActivity")
				|| getLocalClassNameBySelf().contains("PersonalPageActivity")
				|| getLocalClassNameBySelf().contains("OtherPageActivity")
				|| getLocalClassNameBySelf().contains("SearchResultActivity")
				|| getLocalClassNameBySelf().contains("NewFriendsActivity")
				|| getLocalClassNameBySelf().contains("TopicActivity")
				|| getLocalClassNameBySelf().contains("AddFriendsActivity")
				|| getLocalClassNameBySelf().contains("UpdateNikeActivity")
				|| getLocalClassNameBySelf().contains("MySkillActivity")
				|| getLocalClassNameBySelf().contains(
						"ChatRoomListnikeActivity")
				|| getLocalClassNameBySelf().contains("RegisterTagActivity")
				|| getLocalClassNameBySelf().contains("SingleChatRoomActivity")
				|| getLocalClassNameBySelf().contains("SearchTopicActivity")) {
			finish();
		} else if (getLocalClassNameBySelf().contains("UpdatePasswordActivity")) {
			startActivityRight(MyAcountActivity.class, intent, false);
		} else if (getLocalClassNameBySelf().contains("NewFunctionActivity")
				|| getLocalClassNameBySelf().contains("FeedBackActivity")
				|| getLocalClassNameBySelf().contains("MessageNotifyActivity")) {
			startActivityRight(SettingActivity.class, intent, false);
		} else {
			moveTaskToBack(true);
		}

	}

	/**
	 * 退出APP
	 */
	public void exitApp() {
		if ((System.currentTimeMillis() - mExitTime) > 2000) {
			showToast("再次点击退出", Toast.LENGTH_SHORT, false);
			mExitTime = System.currentTimeMillis();
		} else {
			System.exit(0);

		}
	}

	/**
	 * 显示toast，会判断当前是否在toast，如果正在toast，先取消，再显示最新的toast，防止toast时间过长
	 * 
	 * @param arg
	 *            toast内容
	 * @param length
	 *            toast显示长短（Toast.LENGTH_LONG,Toast.LENGTH_SHORT）
	 * @param isCenter
	 *            提示条是否要居中
	 */
	public void showToast(String arg, int length, boolean isCenter) {
		if (toast == null) {
			toast = Toast.makeText(getApplicationContext(), arg, length);

		} else {
			toast.setText(arg);
		}
		// if (isCenter) {
		// toast.setGravity(Gravity.CENTER, 0, 0);
		// } else {
		// toast.setGravity(Gravity.BOTTOM, 0, 0);
		// }
		toast.show();
	}

	/**
	 * 返回当前类名
	 * 
	 * @return
	 */
	public String getLocalClassNameBySelf() {
		String lClssName = getLocalClassName();
		if (lClssName.contains(".")) {
			lClssName = lClssName.replace(".", "@@");
			String lClassArray[] = lClssName.split("@@");
			return lClassArray[lClassArray.length - 1];
		}
		return lClssName;
	}

	/**
	 * 显示loading圈圈
	 */
	public void showProgressBar() {
		showAlphaBg();
		baseProgressBarLayout.setVisibility(View.VISIBLE);
	}

	/**
	 * 隐藏dialog
	 */
	public void hideLoading() {
		hidAlphaBg();
		shadeBg.clearAnimation();
		baseProgressBarLayout.setVisibility(View.GONE);
	}

	/**
	 * 显示渐变背景
	 */
	public void showAlphaBg() {
		shadeBg.setOnClickListener(this);
		shadeBg.setVisibility(View.VISIBLE);
		shadeBg.startAnimation(alphaAnimation);
	}

	/**
	 * 显示渐变背景
	 */
	public void hidAlphaBg() {
		shadeBg.setVisibility(View.GONE);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// 返回上个页面
			backPage();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

	private void HomeKeyWatcher() {
		// TODO Auto-generated method stub
		mHomeWatcher = new HomeWatcher(this);
		mHomeWatcher.setOnHomePressedListener(new OnHomePressedListener() {

			@Override
			public void onHomePressed() {
				// TODO Auto-generated method stub
				Intent intent = new Intent(pBaseActivity.this, FxService.class);
				stopService(intent);
			}

			@Override
			public void onHomeLongPressed() {
				// TODO Auto-generated method stub

			}
		});
		mHomeWatcher.startWatch();
	}

	/*
	 * 处理公共监听事件
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_back:
			backPage();
			break;
		default:
			break;

		}
	}

	/**
	 * 当应用在前台时，如果当前消息不是属于当前会话，在状态栏提示一下 如果不需要，注释掉即可
	 * 
	 * @param message
	 */
	protected void notifyNewMessage(EMMessage message) {
		TextMessageBody txtBody = (TextMessageBody) message.getBody();
		String ticker = txtBody.getMessage();

		Calendar c = Calendar.getInstance();
		int hours = c.get(Calendar.HOUR_OF_DAY);
		int munite = c.get(Calendar.MINUTE);
		// 构建一个通知对象(需要传递的参数有三个,分别是图标,标题和 时间)
		Notification notification = new Notification(R.drawable.logo, "同行",
				System.currentTimeMillis());
		Intent intent = new Intent(pBaseActivity.this, MainActivity.class);

		PendingIntent pendingIntent = PendingIntent.getActivity(
				pBaseActivity.this, 0, intent, 0);
		notification.setLatestEventInfo(getApplicationContext(), "通知标题",
				"通知显示的内容", pendingIntent);
		notification.setLatestEventInfo(this, "同行", ticker, pendingIntent);
		notification.flags = Notification.FLAG_AUTO_CANCEL;// 点击后自动消失
		ToNotifyStyle(notification);
		/*
		 * int starth=LocalStorage.getInt(BasicActivity.this, "starth", 22); int
		 * startm=LocalStorage.getInt(BasicActivity.this, "startm", 30); int
		 * endh=LocalStorage.getInt(BasicActivity.this, "endh", 8); int
		 * endm=LocalStorage.getInt(BasicActivity.this, "endm", 30);
		 * if(DateFormat.is24HourFormat(this)){
		 * if(!((hours>starth&&munite>startm) || (hours<endh&&munite<endm))){
		 * ToNotifyStyle(notification); } }else{ String amorpm;
		 * if(c.get(Calendar.AM_PM)==0){ amorpm="am";
		 * if(!((hours>starth&&munite>startm) || (hours<endh&&munite<endm))){
		 * ToNotifyStyle(notification); } }else{ amorpm="pm"; hours=hours+12;
		 * if(!((hours>starth&&munite>startm) || (hours<endh&&munite<endm))){
		 * ToNotifyStyle(notification); } }
		 * 
		 * }
		 */
		notificationManager.notify(1, notification);// 发动通知,id由自己指定，每一个Notification对应的唯一标志

	}

	private void ToNotifyStyle(Notification notification) {
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

	private class NewMessageBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			abortBroadcast();
			// 消息id
			String msgId = intent.getStringExtra("msgid");
			// 发消息的人的username(userid)
			String msgFrom = intent.getStringExtra("from");
			// 更方便的方法是通过msgId直接获取整个message
			EMMessage message = EMChatManager.getInstance().getMessage(msgId);

			// if (SingleChatRoomActivity.activityInstance != null) {
			// if (message.getChatType() == ChatType.GroupChat) {
			// if
			// (message.getTo().equals(SingleChatRoomActivity.activityInstance.getToChatUsername()))
			// return;
			// } else {
			// if
			// (msgFrom.equals(SingleChatRoomActivity.activityInstance.getToChatUsername()))
			// return;
			// }
			// }

			mainActivity.updateUnreadLabel();
			System.out.println("监听到了");
			notifyNewMessage(message);

			mainActivity.refreshUnReadSum();

		}
	}

	public class IMconnectionListner implements EMConnectionListener {

		@Override
		public void onConnected() {
			// TODO Auto-generated method stub
			runOnUiThread(new Runnable() {
				public void run() {
					pLog.i("system", "环信转态连接良好");
				}
			});
		}

		@Override
		public void onDisconnected(final int error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (error == EMError.USER_REMOVED) {
						// 显示帐号已经被移除
						showToast("帐号已经被移除", Toast.LENGTH_SHORT, false);
					} else if (error == EMError.CONNECTION_CONFLICT) {
						// 显示帐号在其他设备登陆
						showToast("帐号在其他设备登陆，请重新登入", Toast.LENGTH_SHORT, false);
						BussinessUtils.clearUserData(mShareFileUtils);
						ManagerActivity.getAppManager().restart(
								pBaseActivity.this);
					} else {
						if (NetUtils.hasNetwork(pBaseActivity.this))
							// 连接不到聊天服务器
							easemobchatImp.getInstance().login(
									mShareFileUtils.getString(
											Constant.CLIENT_ID, "").replace(
											"-", ""),
									mShareFileUtils.getString(
											Constant.PASSWORD, ""));

						// else
						// 当前网络不可用，请检查网络设置
						// homefragment.base_neterror_item.setVisibility(View.VISIBLE);
					}
				}
			});
		}
	}

}
