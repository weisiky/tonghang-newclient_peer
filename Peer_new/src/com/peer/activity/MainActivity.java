package com.peer.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.NetUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.peer.R;
import com.peer.IMController.ShowNotification;
import com.peer.IMimplements.easemobchatImp;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.base.pBaseApplication;
import com.peer.bean.ChatMsgEntityBean;
import com.peer.bean.LoginBean;
import com.peer.bean.NewFriendBean;
import com.peer.fragment.ComeMsgFragment;
import com.peer.fragment.FriendsFragment;
import com.peer.fragment.HomeFragment;
import com.peer.fragment.MyFragment;
import com.peer.gps.MyLocationListener;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.utils.BussinessUtils;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.ManagerActivity;
import com.peer.utils.pLog;
import com.peer.utils.pViewBox;
import com.readystatesoftware.viewbadger.BadgeView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

/**
 * mainactivity
 */
public class MainActivity extends pBaseActivity {

	/**
	 * fragment定义
	 */
	private HomeFragment homefragment;
	private ComeMsgFragment comemsgfragment;
	private FriendsFragment friendsfragment;
	private MyFragment myfragment;
	private Fragment[] fragments;

	public static LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();

	private int index;
	private int currentTabIndex;
	private int intnewfriendsnum;
	private static BadgeView unredmsg;
	private BadgeView bdnewfriendsnum;

	private PageViewList pageViewaList;

	private NewMessageBroadcastReceiver msgReceiver;

	private static String isnumber = "^\\d+$";// 正则用来匹配纯数字

	private String mPageName = "MainActivity";
	private static final int UPDATE_NEW_MESSAGE_TEXT = 100;

	class PageViewList {
		/* bottom layout */
		private LinearLayout find, come, my, friends;
		private TextView tv_find, tv_come, tv_my, tv_friends, tv_newfriendsnum,
				showmessgenum;
		private ImageView iv_backfind, iv_backcome, iv_backfriends, iv_backmy;

		public LinearLayout ll_find, ll_come, ll_friends, ll_my;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_frabottom);
		findViewById();
		setListener();
		processBiz();
		registerEMchat();
		registerGPS();
		mLocationClient.start();
		// Timer time = new Timer();
		// time.schedule(new TimerTask() {
		// @Override
		// public void run() {
		// TODO Auto-generated method stub
		if (MyLocationListener.w > 0 && MyLocationListener.j > 0) {
			try {
				sendgps(mShareFileUtils.getString(Constant.CLIENT_ID, ""),
						MyLocationListener.w, MyLocationListener.j);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		// }
		// },1000*8);
		// 考虑到用户流量的限制，目前我们默认在Wi-Fi接入情况下才进行自动提醒。如需要在任意网络环境下都进行更新自动提醒，
		// 则请在update调用之前添加以下代码：
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		if (pBaseApplication.updateflag) {
			pBaseApplication.updateflag = false;
			UmengUpdateAgent.update(this);
		}

		// v2.4版本以后的SDK中默认开启了集成检测功能，在调用任意的更新接口后，我们将替您自动检查上述集成过程中2、3两个步骤是否被正确完成。
		// 如果正确完成不会出现任何提示，否则会以如下的toast提示您。
		// 你可以通过调用UmengUpdateAgent.setUpdateCheckConfig(false)来禁用此功能。
		// UmengUpdateAgent.setUpdateCheckConfig(false);

	}

	private void init() {
		// TODO Auto-generated method stub
		/* init fragment */
		homefragment = new HomeFragment();
		comemsgfragment = new ComeMsgFragment();
		friendsfragment = new FriendsFragment();
		myfragment = new MyFragment();
		fragments = new Fragment[] { homefragment, comemsgfragment,
				friendsfragment, myfragment };
		getSupportFragmentManager().beginTransaction()
				.add(R.id.fragment_container, homefragment)
				.add(R.id.fragment_container, comemsgfragment)
				.hide(comemsgfragment)
				.add(R.id.fragment_container, friendsfragment)
				.hide(friendsfragment).add(R.id.fragment_container, myfragment)
				.hide(myfragment).show(homefragment).commit();

		index = 0;
		pageViewaList.tv_find.setTextColor(getResources().getColor(
				R.color.bottomtextblue));
		pageViewaList.iv_backfind.setImageResource(R.drawable.peer_press);

		unredmsg = new BadgeView(this, pageViewaList.showmessgenum);
		bdnewfriendsnum = new BadgeView(this, pageViewaList.tv_newfriendsnum);

	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		pageViewaList = new PageViewList();
		pViewBox.viewBox(this, pageViewaList);

	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processBiz() {
		// TODO Auto-generated method stub
		init();
	}

	// @Override
	// protected View loadTopLayout() {
	// // TODO Auto-generated method stub
	// // return getLayoutInflater().inflate(R.layout.top_layout, null);
	// return null;
	// }
	//
	// @Override
	// protected View loadContentLayout() {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// protected View loadBottomLayout() {
	// // TODO Auto-generated method stub
	// return getLayoutInflater().inflate(R.layout.activity_frabottom, null);
	// }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	public void onTabClicked(View v) {
		// TODO Auto-generated method stub
		pageViewaList.tv_find.setTextColor(getResources().getColor(
				R.color.bottomtextgray));
		pageViewaList.tv_come.setTextColor(getResources().getColor(
				R.color.bottomtextgray));
		pageViewaList.tv_friends.setTextColor(getResources().getColor(
				R.color.bottomtextgray));
		pageViewaList.tv_my.setTextColor(getResources().getColor(
				R.color.bottomtextgray));

		pageViewaList.iv_backfind.setImageResource(R.drawable.peer_nol);
		pageViewaList.iv_backcome.setImageResource(R.drawable.come_mess_nol);
		pageViewaList.iv_backfriends
				.setImageResource(R.drawable.find_label_nol);
		pageViewaList.iv_backmy.setImageResource(R.drawable.mysetting_nol);

		switch (v.getId()) {
		case R.id.ll_find:
			index = 0;
			pageViewaList.tv_find.setTextColor(getResources().getColor(
					R.color.bottomtextblue));
			pageViewaList.iv_backfind.setImageResource(R.drawable.peer_press);
			break;
		case R.id.ll_come:
			index = 1;
			pageViewaList.tv_come.setTextColor(getResources().getColor(
					R.color.bottomtextblue));
			pageViewaList.iv_backcome
					.setImageResource(R.drawable.come_mess_press);
			break;
		case R.id.ll_friends:
			index = 2;
			pageViewaList.tv_friends.setTextColor(getResources().getColor(
					R.color.bottomtextblue));
			pageViewaList.iv_backfriends
					.setImageResource(R.drawable.find_label_press);
			break;
		case R.id.ll_my:
			index = 3;
			pageViewaList.tv_my.setTextColor(getResources().getColor(
					R.color.bottomtextblue));
			pageViewaList.iv_backmy
					.setImageResource(R.drawable.mysetting_press);
			break;
		default:
			break;
		}
		if (currentTabIndex != index) {
			FragmentTransaction trx = getSupportFragmentManager()
					.beginTransaction();
			trx.hide(fragments[currentTabIndex]);
			if (!fragments[index].isAdded()) {
				trx.add(R.id.fragment_container, fragments[index]);
			}
			trx.show(fragments[index]).commit();
		}
		currentTabIndex = index;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(mPageName);

		EMChatManager.getInstance().registerEventListener(
				this,
				new EMNotifierEvent.Event[] {
						EMNotifierEvent.Event.EventNewMessage,
						EMNotifierEvent.Event.EventOfflineMessage,
						EMNotifierEvent.Event.EventDeliveryAck,
						EMNotifierEvent.Event.EventReadAck });

		// TODO Auto-generated method stub
		try {
			sendnewfriend(mShareFileUtils.getString(Constant.CLIENT_ID, ""));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onNetworkOn() {
		// TODO Auto-generated method stub
		homefragment.base_neterror_item.setVisibility(View.GONE);
		if (EMChatManager.getInstance().isConnected()) {

		} else {
			easemobchatImp.getInstance().login(
					mShareFileUtils.getString(Constant.CLIENT_ID, "").replace(
							"-", ""),
					mShareFileUtils.getString(Constant.PASSWORD, ""));
		}
	}

	@Override
	public void onNetWorkOff() {
		// TODO Auto-generated method stub
		homefragment.base_neterror_item.setVisibility(View.VISIBLE);
	}

	/**
	 * 获取新朋友请求
	 * 
	 * @param client_id
	 * @throws UnsupportedEncodingException
	 */
	private void sendnewfriend(String client_id) {
		// TODO Auto-generated method stub

		// RequestParams params = null;
		// try {
		// params = PeerParamsUtils.getNewFriendsParams(this, pageindex++);
		// } catch (Exception e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }

		HttpUtil.post(HttpConfig.FRIEND_INVITATION_URL + client_id + ".json",
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub
						// hideLoading();
						// showToast(
						// getResources().getString(R.string.config_error),
						// Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, responseString,
								throwable);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONArray errorResponse) {
						// TODO Auto-generated method stub
						// hideLoading();
						// showToast(
						// getResources().getString(R.string.config_error),
						// Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						// hideLoading();
						// showToast(
						// getResources().getString(R.string.config_error),
						// Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						try {
							NewFriendBean newfriendbean = JsonDocHelper
									.toJSONObject(
											response.getJSONObject("success")
													.toString(),
											NewFriendBean.class);

							if (newfriendbean != null) {

								intnewfriendsnum = newfriendbean
										.getInvitationbean().size();
								friendsfragment
										.setNewfriendsNum(intnewfriendsnum);
								runOnUiThread(new Runnable() {
									public void run() {
										updatenewfriends();
										updateUnreadLabel();
									}
								});
							}

						} catch (Exception e1) {
							pLog.i("test", "Exception:" + e1.toString());
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						super.onSuccess(statusCode, headers, response);
					}

				});

	}

	public void updatenewfriends() {
		if (intnewfriendsnum > 0) {
			bdnewfriendsnum.setText(String.valueOf(intnewfriendsnum));
			// bdnewfriendsnum.setTextSize(11);
			// bdnewfriendsnum.setBadgeMargin(1,1);
			bdnewfriendsnum.show();
		} else {
			bdnewfriendsnum.hide();
		}
	}

	/**
	 * 环信注册监听
	 */
	private void registerEMchat() {
		// TODO Auto-generated method stub
		// if (EMChatManager.getInstance().isConnected()) {
		msgReceiver = new NewMessageBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(EMChatManager
				.getInstance().getNewMessageBroadcastAction());
		intentFilter.setPriority(3);
		registerReceiver(msgReceiver, intentFilter);
		pLog.i("emc", "msgReceiver");
		EMChatManager.getInstance().addConnectionListener(
				new IMconnectionListner());

		EMChat.getInstance().setAppInited();
		// }
	}

	/**
	 * GPS监听
	 */
	private void registerGPS() {
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		initLocation();
	}

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		int span = 1000;
		option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(false);// 可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		mLocationClient.setLocOption(option);
	}

	private static List<EMConversation> loadConversationsWithRecentChat() {
		// 获取所有会话，包括陌生人
		Hashtable<String, EMConversation> conversations = EMChatManager
				.getInstance().getAllConversations();
		List<EMConversation> list = new ArrayList<EMConversation>();
		// 过滤掉messages seize为0的conversation
		for (EMConversation conversation : conversations.values()) {
			if (conversation.getAllMessages().size() != 0)
				list.add(conversation);
		}
		// 排序
		// sortConversationByLastChatTime(list);
		return list;
	}

	public void refreshUnReadSum() {

		if (comemsgfragment != null) {
			comemsgfragment.refresh();
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
			if (message.getChatType() == ChatType.Chat) {
				try {
					senduser(message.getFrom(),
							mShareFileUtils.getString(Constant.CLIENT_ID, ""),
							message);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				multinotifyNewMessage(message);
			}
			pLog.i("test", "Main message:" + message.toString());
			updateUnreadLabel();
			if (comemsgfragment != null) {
				comemsgfragment.refresh();
			}

		}
	}

	/**
	 * 单聊信息提示 当应用在前台时，如果当前消息不是属于当前会话，在状态栏提示一下 如果不需要，注释掉即可
	 * 
	 * @param bean
	 * @param message
	 */
	protected void singlenotifyNewMessage(LoginBean bean, EMMessage message) {
		TextMessageBody txtBody = (TextMessageBody) message.getBody();
		String ticker = bean.user.getUsername() + " " + "对你说:"
				+ txtBody.getMessage();

		Calendar c = Calendar.getInstance();
		int hours = c.get(Calendar.HOUR_OF_DAY);
		int munite = c.get(Calendar.MINUTE);
		// 构建一个通知对象(需要传递的参数有三个,分别是图标,标题和 时间)
		Notification notification = new Notification(R.drawable.logo, "同行",
				System.currentTimeMillis());
		Intent intent;
		intent = new Intent(MainActivity.this, MainActivity.class);

		PendingIntent pendingIntent = PendingIntent.getActivity(
				MainActivity.this, 0, intent, 0);
		notification.setLatestEventInfo(getApplicationContext(), "通知标题",
				"通知显示的内容", pendingIntent);
		notification.setLatestEventInfo(this, "同行", ticker, pendingIntent);
		notification.flags = Notification.FLAG_AUTO_CANCEL;// 点击后自动消失
		ToNotifyStyle(notification);
		notificationManager.notify(1, notification);// 发动通知,id由自己指定，每一个Notification对应的唯一标志

	}

	/**
	 * 群聊消息提示 当应用在前台时，如果当前消息不是属于当前会话，在状态栏提示一下 如果不需要，注释掉即可
	 * 
	 * @param message
	 */
	@SuppressWarnings("deprecation")
	protected void multinotifyNewMessage(EMMessage message) {
		TextMessageBody txtBody = (TextMessageBody) message.getBody();
		String ticker = "话题消息：" + txtBody.getMessage();

		Calendar c = Calendar.getInstance();
		int hours = c.get(Calendar.HOUR_OF_DAY);
		int munite = c.get(Calendar.MINUTE);
		// 构建一个通知对象(需要传递的参数有三个,分别是图标,标题和 时间)
		Notification notification = new Notification(R.drawable.logo, "同行",
				System.currentTimeMillis());
		Intent intent = new Intent();

		PendingIntent pendingIntent = PendingIntent.getActivity(
				MainActivity.this, 0, intent, 0);
		notification.setLatestEventInfo(getApplicationContext(), "通知标题",
				"通知显示的内容", pendingIntent);
		notification.setLatestEventInfo(this, "同行", ticker, pendingIntent);
		notification.flags = Notification.FLAG_AUTO_CANCEL;// 点击后自动消失
		ToNotifyStyle(notification);
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

	public class IMconnectionListner implements EMConnectionListener {

		@Override
		public void onConnected() {
			// TODO Auto-generated method stub
			runOnUiThread(new Runnable() {
				public void run() {
					homefragment.base_neterror_item.setVisibility(View.GONE);
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
								MainActivity.this);
					} else {
						if (NetUtils.hasNetwork(MainActivity.this))
							// 连接不到聊天服务器
							easemobchatImp.getInstance().login(
									mShareFileUtils.getString(
											Constant.CLIENT_ID, "").replace(
											"-", ""),
									mShareFileUtils.getString(
											Constant.PASSWORD, ""));

						else
							// 当前网络不可用，请检查网络设置
							homefragment.base_neterror_item
									.setVisibility(View.VISIBLE);
					}
				}
			});
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
			params = PeerParamsUtils
					.getUserParams(MainActivity.this, client_id);
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
									MainActivity.this, message,
									mShareFileUtils, false,
									loginBean.user.getUsername());
						}
					} else if (code.equals("500")) {

					} else {
						String message = result.getString("message");
						showToast(message, Toast.LENGTH_SHORT, false);
					}
				} catch (Exception e1) {
					pLog.i("test", "Exception:" + e1.toString());
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				super.onSuccess(statusCode, headers, response);

			}

		});
	}

	/**
	 * 获取用户信息接口
	 * 
	 * @param client_id
	 * @param x_point
	 * @param y_point
	 * @throws UnsupportedEncodingException
	 */
	private void sendgps(String client_id, Double x_point, Double y_point)
			throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		final Intent intent = new Intent();
		RequestParams params = null;
		try {
			params = PeerParamsUtils.getGPSParams(MainActivity.this, x_point,
					y_point);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpUtil.post(HttpConfig.LOGIN_GPS_URL + client_id + ".json", params,
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

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						pLog.i("test", "response:" + response.toString());
						try {
							JSONObject result = response
									.getJSONObject("success");

							String code = result.getString("code");
							pLog.i("test", "code:" + code);
							if (code.equals("200")) {
								mLocationClient.stop();
								pLog.i("test", "gps关闭");
							} else if (code.equals("500")) {

							} else {
								String message = result.getString("message");
								showToast(message, Toast.LENGTH_SHORT, false);
							}
						} catch (Exception e1) {
							pLog.i("test", "Exception:" + e1.toString());
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						super.onSuccess(statusCode, headers, response);

					}

				});
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
			EMMessage eMessage = (EMMessage) event.getData();

			// try {
			// senduser(message.getFrom(),
			// mShareFileUtils.getString(Constant.CLIENT_ID, ""),
			// message);
			// } catch (UnsupportedEncodingException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }

			Message message1 = new Message();
			message1.what = UPDATE_NEW_MESSAGE_TEXT;
			message1.obj = eMessage;
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
	 * 刷新未读消息数
	 */
	public static void updateUnreadLabel() {
		int count = 0;
		// int count = easemobchatImp.getInstance().getUnreadMesTotal();
		for (EMConversation em : loadConversationsWithRecentChat()) {
			if (!em.getUserName().matches(isnumber)) {
				EMConversation conversation = EMChatManager.getInstance()
						.getConversation(em.getUserName());
				if (conversation.getUnreadMsgCount() > 0) {
					count = count + conversation.getUnreadMsgCount();
				}
			} else {

			}
		}
		if (count > 0) {
			unredmsg.setText(String.valueOf(count));
			// unredmsg.setTextSize(11);
			// unredmsg.setBadgeMargin(1,1);
			unredmsg.show();
		} else {
			unredmsg.hide();
		}
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message message) {
			switch (message.what) {
			case UPDATE_NEW_MESSAGE_TEXT:
				EMMessage msg = (EMMessage) message.obj;
				pLog.i("zzg", "msg:" + msg.getFrom());
				try {
					senduser(msg.getFrom(),
							mShareFileUtils.getString(Constant.CLIENT_ID, ""),
							msg);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				updateUnreadLabel();
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

}
