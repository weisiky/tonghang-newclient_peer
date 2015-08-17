package com.peer.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.http.Header;
import org.apache.tools.ant.taskdefs.Sleep;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.RemoteException;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.util.NetUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.peer.R;
import com.peer.IMimplements.easemobchatImp;
import com.peer.adapter.FriendsAdapter;
import com.peer.adapter.NewfriendsAdapter;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.base.pBaseApplication;
import com.peer.bean.NewFriendBean;
import com.peer.bean.RecommendUserBean;
import com.peer.fragment.ComeMsgFragment;
import com.peer.fragment.FriendsFragment;
import com.peer.fragment.HomeFragment;
import com.peer.fragment.MyFragment;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.utils.BussinessUtils;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.ManagerActivity;
import com.peer.utils.pIOUitls;
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

	private int index;
	private int currentTabIndex;
	private int intnewfriendsnum;
	private BadgeView unredmsg, bdnewfriendsnum;

	private PageViewList pageViewaList;

	private NewMessageBroadcastReceiver msgReceiver;

	private String isnumber = "^\\d+$";// 正则用来匹配纯数字
	
	private String mPageName = "MainActivity";

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
		 registerEMchat();
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

	@Override
	protected View loadTopLayout() {
		// TODO Auto-generated method stub
		// return getLayoutInflater().inflate(R.layout.top_layout, null);
		return null;
	}

	@Override
	protected View loadContentLayout() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected View loadBottomLayout() {
		// TODO Auto-generated method stub
		return getLayoutInflater().inflate(R.layout.activity_frabottom, null);
	}

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
						hideLoading();
						pLog.i("test", "statusCode:" + statusCode);
						pLog.i("test", "headers:" + headers);
						pLog.i("test", "responseString:" + responseString);
						pLog.i("test", "throwable:" + throwable);
//						showToast(
//								getResources().getString(R.string.config_error),
//								Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, responseString,
								throwable);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONArray errorResponse) {
						// TODO Auto-generated method stub
						hideLoading();
						pLog.i("test", "statusCode:" + statusCode);
						pLog.i("test", "headers:" + headers);
						pLog.i("test", "errorResponse:" + errorResponse);
						pLog.i("test", "throwable:" + throwable);
//						showToast(
//								getResources().getString(R.string.config_error),
//								Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						hideLoading();
						pLog.i("test", "statusCode:" + statusCode);
						pLog.i("test", "headers:" + headers);
						pLog.i("test", "errorResponse:" + errorResponse);
						pLog.i("test", "throwable:" + throwable);
//						showToast(
//								getResources().getString(R.string.config_error),
//								Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub

						pLog.i("test", "response:" + response.toString());

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
	
	private List<EMConversation> loadConversationsWithRecentChat() {
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
//		sortConversationByLastChatTime(list);
		return list;
	}
	

	/**
	 * 刷新未读消息数
	 */
	public void updateUnreadLabel() {
		int count = 0;
//		int count = easemobchatImp.getInstance().getUnreadMesTotal();
//		pLog.i("test", "未读消息count:" + count);
		for (EMConversation em : loadConversationsWithRecentChat()) {
			if(!em.getUserName().matches(isnumber)){
				EMConversation conversation = EMChatManager.getInstance()
						.getConversation(em.getUserName());
				if (conversation.getUnreadMsgCount() > 0) {				
					count = count + conversation.getUnreadMsgCount();
				}
			}else{
				
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
			pLog.i("test", "Main message:"+message.toString());

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
			updateUnreadLabel();
			System.out.println("监听到了");
			notifyNewMessage(message);
			if (comemsgfragment != null) {
				comemsgfragment.refresh();
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

}
