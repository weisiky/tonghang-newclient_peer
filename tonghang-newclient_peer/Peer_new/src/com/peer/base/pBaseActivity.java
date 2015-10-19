package com.peer.base;

import java.io.UnsupportedEncodingException;
import java.util.List;

import net.google.niofile.AdManager;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.peer.R;
import com.peer.IMController.ShowNotification;
import com.peer.IMimplements.easemobchatImp;
import com.peer.activity.LoginActivity;
import com.peer.activity.MainActivity;
import com.peer.activity.MultiChatRoomActivity;
import com.peer.activity.MyAcountActivity;
import com.peer.activity.SettingActivity;
import com.peer.base.pBaseApplication.OnNetworkStatusListener;
import com.peer.bean.ChatRoomBean;
import com.peer.bean.LoginBean;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.service.FxService;
import com.peer.utils.BussinessUtils;
import com.peer.utils.HomeWatcher;
import com.peer.utils.HomeWatcher.OnHomePressedListener;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.ManagerActivity;
import com.peer.utils.pLog;
import com.peer.utils.pNetUitls;
import com.peer.utils.pShareFileUtils;
import com.peer.utils.pSysInfoUtils;
import com.peer.utils.zManifestInfoUtils;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

/**
 * 基础Activity
 * 
 * @author zhangzg
 * 
 */

public abstract class pBaseActivity extends FragmentActivity implements
		OnClickListener, EMEventListener {

	/** 共享文件工具类 **/
	public pShareFileUtils mShareFileUtils = new pShareFileUtils();
	/** 退出倒计时 **/
	private long mExitTime;
	/** 提示条 **/
	public Toast toast;
	// /** 中间布局 **/
	// private RelativeLayout contentLayout;
	// /** 顶部布局 **/
	// private RelativeLayout topLayout;
	// /** 底部布局 **/
	// private RelativeLayout bottomLayout;
	/** 灰色布局 **/
	// private LinearLayout shadeBg;
	/** 当前页面的名称 **/
	public String currentPageName = null;
	/** 蒙板渐变动画 **/
	public Animation alphaAnimation;
	/** 首次进入页面的loading圈圈 **/
	// public RelativeLayout baseProgressBarLayout;
	// 网络状态
	public boolean isNetworkAvailable;
	public pBaseApplication application;

	private ActivityManager activityManager;
	// 网络状态监听
	private OnNetworkStatusListener onNetworkStatusListener;
	// Home键监听
	private HomeWatcher mHomeWatcher;

	public NotificationManager notificationManager;
	protected ShowNotification showNotification;

	private static final int UPDATE_NEW_MESSAGE_TEXT = 102;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.base);
		AdManager.getInstance(this).init("55b6776ef7488d73", "7fd430361ac0d3f0", true);
		

		// 当前页面名称
		currentPageName = getLocalClassNameBySelf();
		// 获取手机屏幕跨度，图片适配使用
		Constant.S_SCREEN_WIDHT_VALUE = pSysInfoUtils.getDisplayMetrics(this).widthPixels
				+ "";
		// findGlobalViewById();
		// 初始化ShareUtiles
		initShareUtils();
		// 友盟统计 发送策略
		MobclickAgent.updateOnlineConfig(this);
		// 友盟统计 数据加密
		AnalyticsConfig.enableEncrypt(true);

		ManagerActivity.getAppManager().addActivity(this);

		application = (pBaseApplication) getApplication();

		onNetworkStatusListener = new OnNetworkStatusListener() {
			public void networkOn() {
				isNetworkAvailable = true;
				sendSystemConfig();
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
		showNotification = new ShowNotification(notificationManager);

		alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.shade_alpha);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		EMChatManager.getInstance().activityResumed();
		MobclickAgent.onResume(this);
		EMChatManager.getInstance().registerEventListener(
				this,
				new EMNotifierEvent.Event[] {
						EMNotifierEvent.Event.EventNewMessage,
						EMNotifierEvent.Event.EventOfflineMessage,
						EMNotifierEvent.Event.EventDeliveryAck,
						EMNotifierEvent.Event.EventReadAck });
		/*
		 * 判断是否满足悬浮头像启动逻辑。满足——启动
		 */
		System.out.println("悬浮头像是否存在："
				+ mShareFileUtils.getBoolean(Constant.ISFLOAT, false));
		if (ChatRoomBean.getInstance().getTopicBean() != null) {
			if (mShareFileUtils.getBoolean(Constant.ISFLOAT, false)) {
				Intent resintent = new Intent(pBaseActivity.this,
						FxService.class);

				resintent.putExtra(Constant.FROMFLOAT, "fromfloat");
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
	// @SuppressWarnings("deprecation")
	// protected void findGlobalViewById() {
	// topLayout = (RelativeLayout) findViewById(R.id.topLayout);
	// contentLayout = (RelativeLayout) findViewById(R.id.contentLayout);
	// bottomLayout = (RelativeLayout) findViewById(R.id.bottomLayout);
	// shadeBg = (LinearLayout) findViewById(R.id.shadeBg);
	//
	// RelativeLayout.LayoutParams layoutParamsContent = new
	// RelativeLayout.LayoutParams(
	// RelativeLayout.LayoutParams.FILL_PARENT,
	// RelativeLayout.LayoutParams.FILL_PARENT);
	// RelativeLayout.LayoutParams layoutParamsTop = new
	// RelativeLayout.LayoutParams(
	// RelativeLayout.LayoutParams.FILL_PARENT,
	// RelativeLayout.LayoutParams.WRAP_CONTENT);
	// RelativeLayout.LayoutParams layoutParamsBottom = new
	// RelativeLayout.LayoutParams(
	// RelativeLayout.LayoutParams.FILL_PARENT,
	// RelativeLayout.LayoutParams.WRAP_CONTENT);
	// View topView = loadTopLayout();
	// if (topView == null) {
	// topLayout.setVisibility(View.GONE);
	// } else {
	// topLayout.setVisibility(View.VISIBLE);
	// topLayout.addView(topView, layoutParamsTop);
	// }
	// View contentView = loadContentLayout();
	// if (contentView == null) {
	// contentLayout.setVisibility(View.GONE);
	// } else {
	// contentLayout.setVisibility(View.VISIBLE);
	// contentLayout.addView(contentView, layoutParamsContent);
	// }
	// View bottomView = loadBottomLayout();
	// if (bottomView == null) {
	// bottomLayout.setVisibility(View.GONE);
	// } else {
	// bottomLayout.setVisibility(View.VISIBLE);
	// bottomLayout.addView(bottomView, layoutParamsTop);
	// }
	//
	// baseProgressBarLayout = (RelativeLayout)
	// findViewById(R.id.baseProgressBarLayout);
	// }

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

	// /**
	// * 获取顶部布局
	// */
	// protected abstract View loadTopLayout();
	//
	// /**
	// * 获取中间布局
	// *
	// */
	// protected abstract View loadContentLayout();
	//
	// /**
	// * 获取底部布局
	// *
	// */
	// protected abstract View loadBottomLayout();

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
				|| getLocalClassNameBySelf().contains("RegisterTagActivity")
				|| getLocalClassNameBySelf().contains("FindPasswordActivity")) {
			startActivityRight(LoginActivity.class, intent, false);
		} else if (getLocalClassNameBySelf().contains("MultiChatRoomActivity")
				|| getLocalClassNameBySelf().contains("WebViewActivity")) {
			startActivityRight(MainActivity.class, intent, true);
		} else if (getLocalClassNameBySelf().contains("xieyiActivity")
				|| getLocalClassNameBySelf().contains("Recommend_topic")
				|| getLocalClassNameBySelf().contains("GetAddressInfoActivity")
				|| getLocalClassNameBySelf().contains("SettingActivity")
				|| getLocalClassNameBySelf().contains("SearchUserActivity")
				|| getLocalClassNameBySelf().contains("CreatTopicActivity")
				|| getLocalClassNameBySelf().contains("MyAcountActivity")

				|| getLocalClassNameBySelf()
						.contains("NewCardChange")
				|| getLocalClassNameBySelf()
						.contains("PersonalMessageActivity")
				|| getLocalClassNameBySelf().contains("EditCardActivity")
				|| getLocalClassNameBySelf().contains("PersonalPageActivity")
				|| getLocalClassNameBySelf().contains("CardActivity")
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
				|| getLocalClassNameBySelf().contains("BlackListActivity")
				|| getLocalClassNameBySelf().contains("MessageNotifyActivity")) {
			startActivityRight(SettingActivity.class, intent, false);
		} else {
			Intent mIntent = new Intent();
			mIntent.setAction("com.peer.service.ReceiveMessageServices");
			Intent eintent = new Intent(getExplicitIntent(pBaseActivity.this,
					mIntent));
			startService(eintent);
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

	// /**
	// * 显示loading圈圈
	// */
	// public void showProgressBar() {
	// showAlphaBg();
	// baseProgressBarLayout.setVisibility(View.VISIBLE);
	// }
	//
	// /**
	// * 隐藏dialog
	// */
	// public void hideLoading() {
	// hidAlphaBg();
	// shadeBg.clearAnimation();
	// baseProgressBarLayout.setVisibility(View.GONE);
	// }
	//
	// /**
	// * 显示渐变背景
	// */
	// public void showAlphaBg() {
	// shadeBg.setOnClickListener(this);
	// shadeBg.setVisibility(View.VISIBLE);
	// shadeBg.startAnimation(alphaAnimation);
	// }
	//
	// /**
	// * 显示渐变背景
	// */
	// public void hidAlphaBg() {
	// shadeBg.setVisibility(View.GONE);
	// }

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
	 * 环信聊天监听
	 */

	@SuppressWarnings({ "incomplete-switch", "static-access" })
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

	Handler handler = new Handler() {
		@SuppressWarnings("static-access")
		@Override
		public void handleMessage(android.os.Message message) {
			switch (message.what) {
			case UPDATE_NEW_MESSAGE_TEXT:
				// MainActivity.updateUnreadLabel();
				EMMessage msg = (EMMessage) message.obj;
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

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		EMChatManager.getInstance().unregisterEventListener(this);
	}

	/**
	 * 获取用户信息接口
	 * 
	 * @param client_id
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("unused")
	private void senduser(String client_id, String o_client_id,
			final EMMessage message) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		final Intent intent = new Intent();
		RequestParams params = null;
		try {
			params = PeerParamsUtils.getUserParams(pBaseActivity.this,
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
				showToast(getResources().getString(R.string.config_error),
						Toast.LENGTH_SHORT, false);
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONArray errorResponse) {
				// TODO Auto-generated method stub
				showToast(getResources().getString(R.string.config_error),
						Toast.LENGTH_SHORT, false);
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				showToast(getResources().getString(R.string.config_error),
						Toast.LENGTH_SHORT, false);
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				try {
					JSONObject result = response.getJSONObject("success");
					String code = result.getString("code");
					if (code.equals("200")) {
						LoginBean loginBean = JsonDocHelper.toJSONObject(
								response.getJSONObject("success").toString(),
								LoginBean.class);
						if (loginBean != null) {
							// singlenotifyNewMessage(loginBean, message);
							showNotification.sendNotification(
									pBaseActivity.this, message,
									mShareFileUtils, false,
									loginBean.user.getUsername());
						}
					} else if (code.equals("500")) {

					} else {
						String message = result.getString("message");
						showToast(message, Toast.LENGTH_SHORT, false);
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				super.onSuccess(statusCode, headers, response);

			}

		});
	}

	/**
	 * 强制下载更新
	 */

	public void showTipsDialog() {
		// TODO Auto-generated method stub
		pLog.i("ban", "app_version:"+mShareFileUtils.getString(Constant.APP_VERSION, ""));
		pLog.i("ban", "can_upgrade_silently:"+mShareFileUtils.getBoolean(Constant.CAN_UPGRADE_SILENTLY, true));
		pLog.i("ban", "app_link:"+mShareFileUtils.getString(Constant.APP_LINK, ""));

		final CustomDialog customDialog = new CustomDialog(this, R.style.dialog);
		customDialog.setContentView(R.layout.dialog_no_icon);

		((TextView) customDialog.findViewById(R.id.txtMsg))
				.setText("由于系统升级，请更新您的版本!");

		customDialog.setTitle("下载");
		Button btnSure = ((Button) customDialog.findViewById(R.id.btnSure));
		btnSure.setText("确定");
		Button btnCancel = ((Button) customDialog.findViewById(R.id.btnCancel));
		btnCancel.setText("取消");
		btnCancel.setVisibility(View.GONE);
		btnSure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				customDialog.cancel();
				
				//启动服务去下载
				Intent mIntent = new Intent();
				mIntent.setAction("com.peer.service.UpdateVerServices");
				Intent eintent = new Intent(getExplicitIntent(
						pBaseActivity.this, mIntent));
				eintent.putExtra("apkUrl",
						mShareFileUtils.getString(Constant.APP_LINK, ""));
//						"http://192.168.23.1:8080/tonghang-serverapi/home/app/Peer_umeng_unsign_signed.apk");
				startService(eintent);
				
				//关闭app并强制下载
				ManagerActivity.getAppManager().AppExit(
						pBaseActivity.this);
			}
		});
		
		customDialog.setOnCancelListener(new OnCancelListener() {
	        
	        public void onCancel(DialogInterface dialog) {
	            // TODO Auto-generated method stub
	    
	               // Toast.makeText(getBaseContext(), "点击了back", Toast.LENGTH_SHORT).show();
	        }
	    });
		
		customDialog.setOnKeyListener(new OnKeyListener()
        {
            public boolean onKey(DialogInterface dialog,
                    int keyCode, KeyEvent event)
            {
                if (keyCode == KeyEvent.KEYCODE_BACK)
                {
                    dialog.dismiss();
                    finish();
//                    System.exit(0);
                    return true;
                }
                else
                {
                    return false;
                }
            }
        });
		
		customDialog.show();
	}
	
	/**
	 * 可选下载更新
	 */

	public void showselectDialog() {
		// TODO Auto-generated method stub

		final CustomDialog customDialog = new CustomDialog(this, R.style.dialog);
		customDialog.setContentView(R.layout.dialog_no_icon);

		((TextView) customDialog.findViewById(R.id.txtMsg))
				.setText("发现新版本，是否立即下载!");

		customDialog.setTitle("下载");
		Button btnSure = ((Button) customDialog.findViewById(R.id.btnSure));
		btnSure.setText("确定");
		Button btnCancel = ((Button) customDialog.findViewById(R.id.btnCancel));
		btnCancel.setText("取消");
		btnSure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				customDialog.cancel();
				Intent mIntent = new Intent();
				mIntent.setAction("com.peer.service.UpdateVerServices");
				Intent eintent = new Intent(getExplicitIntent(
						pBaseActivity.this, mIntent));
				eintent.putExtra("apkUrl",
						mShareFileUtils.getString(Constant.APP_LINK, ""));
//						"http://192.168.23.1:8080/tonghang-serverapi/home/app/Peer_umeng_unsign_signed.apk");
				startService(eintent);

			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				customDialog.cancel();
			}
		});
		
		customDialog.setOnCancelListener(new OnCancelListener() {
	        
	        public void onCancel(DialogInterface dialog) {
	            // TODO Auto-generated method stub
	    
	               // Toast.makeText(getBaseContext(), "点击了back", Toast.LENGTH_SHORT).show();
	        }
	    });
		
		customDialog.show();
		
		
	}
	
	

	public static Intent getExplicitIntent(Context context,
			Intent implicitIntent) {
		// Retrieve all services that can match the given intent
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent,
				0);
		// Make sure only one match was found
		if (resolveInfo == null || resolveInfo.size() != 1) {
			return null;
		}
		// Get component info and create ComponentName
		ResolveInfo serviceInfo = resolveInfo.get(0);
		String packageName = serviceInfo.serviceInfo.packageName;
		String className = serviceInfo.serviceInfo.name;
		ComponentName component = new ComponentName(packageName, className);
		// Create a new intent. Use the old one for extras and such reuse
		Intent explicitIntent = new Intent(implicitIntent);
		// Set the component to be explicit
		explicitIntent.setComponent(component);
		return explicitIntent;
	}
	
	
	
	/**
	 * 断网重连时，获取系统消息
	 */
	private void sendSystemConfig() {
		HttpEntity entity = null;
		HttpUtil.post(HttpConfig.GET_SYSTEMCONFIG,
				new JsonHttpResponseHandler() {
					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub
						showToast(
								getResources().getString(R.string.config_error),
								Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, responseString,
								throwable);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONArray errorResponse) {
						// TODO Auto-generated method stub
						showToast(
								getResources().getString(R.string.config_error),
								Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						showToast(
								getResources().getString(R.string.config_error),
								Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@SuppressWarnings("static-access")
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub

						pLog.i("test", "res:"+response.toString());
						
						Intent intent = new Intent();
						JSONObject reasult;
						try {
							reasult = response.getJSONObject("success");
							boolean canlogin = reasult.getJSONObject("system").getBoolean(
									"can_login");
							Boolean canupgradesilently = reasult.getJSONObject("system")
									.getBoolean(
											"can_upgrade_silently");
							Boolean canupgrade = reasult.getJSONObject("system")
									.getBoolean(
											"can_upgrade");
							String appversion = reasult.getJSONObject("system")
									.getString(
											"app_version");
							int appcode = reasult.getJSONObject("system")
									.getInt(
											"app_code");
							String applink = reasult.getJSONObject("system")
									.getString(
											"app_link");
							boolean canregisteruser = reasult
									.getJSONObject("system")
									.getBoolean("can_register_user");
							String time = reasult.getJSONObject("system").getString(
									"time");
							boolean useadv = reasult.getJSONObject("system").getBoolean(
									"use_adv");
							boolean thirdadv = reasult.getJSONObject("system").getBoolean(
									"third_adv");
							String selfadvurl = reasult.getJSONObject("system").getString(
									"self_adv_url");
							String selfimg = reasult.getJSONObject("system").getString(
									"self_img");

							mShareFileUtils.setBoolean(
									Constant.CAN_LOGIN,
									canlogin);
							//判断强制（0）或者可选（1）
							mShareFileUtils
									.setBoolean(
											Constant.CAN_UPGRADE_SILENTLY,
											canupgradesilently);
							//判断升级开关是否开启
							mShareFileUtils
							.setBoolean(
									Constant.CAN_UPGRADE,
									canupgrade);
							//最新app版本号
							mShareFileUtils
							.setString(
									Constant.APP_VERSION,
									appversion);
							
							mShareFileUtils.setInt(Constant.APP_CODE, appcode);
							
							//app下载链接
							mShareFileUtils
							.setString(
									Constant.APP_LINK,
									applink);
							
							
							
							
							mShareFileUtils.setBoolean(
									Constant.CAN_REGISTER_USER, canregisteruser);
							mShareFileUtils.setString(
									Constant.TIME,time);
							mShareFileUtils.setBoolean(
									Constant.USE_ADV,
									useadv);
							mShareFileUtils.setBoolean(
									Constant.THIRD_ADV,
									thirdadv);
							mShareFileUtils.setString(
									Constant.SELF_ADV_URL,
									selfadvurl);
							mShareFileUtils.setString(
									Constant.SELF_IMG,
									selfimg);
							
							if(canlogin){
								zManifestInfoUtils utils = new zManifestInfoUtils(pBaseActivity.this);
								int code_now = utils.getVersionCode();
								String name_now = utils.getVersionName();
								
								
								if(code_now < appcode){
									pLog.i("test", "canupgrade:"+canupgrade);
									if(canupgrade){
										if(getLocalClassNameBySelf().contains("WelComeActivity")){
											//欢迎页面不做处理
										}else{
											if (pBaseApplication.updateflag) {
												pBaseApplication.updateflag = false;
												if(canupgradesilently){
													//强制
													showTipsDialog();
												}else{
													//可选
													showselectDialog();
												}
											}
										}
									}
								}
							}else{
								BussinessUtils.clearUserData(mShareFileUtils);
								ManagerActivity.getAppManager().restart(
										pBaseActivity.this);
								easemobchatImp.getInstance().logout();
							}
							
							

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							startActivityForLeft(LoginActivity.class, intent,
									false);
							e.printStackTrace();
						}
						super.onSuccess(statusCode, headers, response);
					}

				});

	}

}
