package com.peer.activity;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TimeUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMChatRoomChangeListener;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.EMValueCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatRoom;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMConversation.EMConversationType;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.DateUtils;
import com.easemob.util.EMLog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.peer.R;
import com.peer.IMController.HXSDKHelper;
import com.peer.IMController.ShowNotification;
import com.peer.IMimplements.easemobchatImp;
import com.peer.adapter.ChatMsgViewAdapter;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.bean.ChatMsgEntityBean;
import com.peer.bean.LoginBean;
import com.peer.bean.UserBean;
import com.peer.bean.singlechatmsgListBean;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.titlepopwindow.ActionItem;
import com.peer.titlepopwindow.TitlePopup;
import com.peer.titlepopwindow.TitlePopup.OnItemOnClickListener;
import com.peer.utils.BussinessUtils;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.Tools;
import com.peer.utils.pIOUitls;
import com.peer.utils.pLog;
import com.peer.utils.pViewBox;
import com.umeng.analytics.MobclickAgent;

/**
 * 一对一聊天室 进入单聊就两种方式：1.通过个人主页点击发信进入。2、通过“来信页面”，点击进入。
 */
@SuppressWarnings("serial")
public class SingleChatRoomActivity extends pBaseActivity {

	private TitlePopup titlePopup;
	private boolean page = true;
	private InputMethodManager manager;
	private String mPageName = "ChatRoom";
	private ArrayList<ChatMsgEntityBean> singlechatmsgList = new ArrayList<ChatMsgEntityBean>();
	private String toChatUsername;
	String theme = null;
	private EMConversation conversation;
	private ChatMsgViewAdapter singlechatadapter;
	public static SingleChatRoomActivity singleactivityInstance = null;
	private NewMessageBroadcastReceiver receiver;
	private String topicId;
	private UserBean userbean;

	// 图片选择器
	private String[] items;

	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	Bitmap photo;
	byte[] img;
	private static final String IMAGE_FILE_NAME = "faceImage.png";
	int width, height;

	class PageViewList {
		private TextView tv_tagname, tv_share, tv_nikename, theme_chat;
		private RelativeLayout rl_bottom;
		private ImageView host_image, im_downview;
		private ListView lv_chat;
		private Button btn_send, sendphoto;
		private EditText et_sendmessage;
		private LinearLayout ll_back;
	}

	private PageViewList pageViewaList;

	private static final int EVENT_NEW_MESSAGE = 100;
	private static final int EVENT_NEW_OTHER_MESSAGE = 101;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_singlesend);
		findViewById();
		setListener();
		processBiz();
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
						| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		items = getResources().getStringArray(R.array.pictrue);
		pageViewaList.sendphoto.setDrawingCacheEnabled(true);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(mPageName);
		// register the event listener when enter the foreground
		EMChatManager.getInstance().registerEventListener(
				this,
				new EMNotifierEvent.Event[] {
						EMNotifierEvent.Event.EventNewMessage,
						EMNotifierEvent.Event.EventOfflineMessage,
						EMNotifierEvent.Event.EventDeliveryAck,
						EMNotifierEvent.Event.EventReadAck });
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		EMChatManager.getInstance().unregisterEventListener(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		pageViewaList = new PageViewList();
		pViewBox.viewBox(this, pageViewaList);
		pageViewaList.im_downview.setVisibility(View.VISIBLE);
		titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, page);
		popupwindow();

		hideKeyboard();// 软键盘
		pageViewaList.btn_send.setEnabled(false);
		pageViewaList.et_sendmessage.setHint(getResources().getString(
				R.string.sendmessage));
		pageViewaList.et_sendmessage.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(pageViewaList.et_sendmessage.getText()
						.toString().trim())) {
					pageViewaList.btn_send.setEnabled(false);
				} else {
					pageViewaList.btn_send.setEnabled(true);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(pageViewaList.et_sendmessage.getText()
						.toString().trim())) {
					pageViewaList.btn_send.setEnabled(false);
				} else {
					pageViewaList.btn_send.setEnabled(true);
				}
			}
		});
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.im_downview.setOnClickListener(this);
		pageViewaList.btn_send.setOnClickListener(this);
		pageViewaList.sendphoto.setOnClickListener(this);
		pageViewaList.ll_back.setOnClickListener(this);
		// initChatListener();
	}

	@Override
	protected void processBiz() {
		// TODO Auto-generated method stub
		/** 单聊页面,主要业务，获得聊天记录 **/
		Intent intent = getIntent();
		userbean = (UserBean) intent.getSerializableExtra("userbean");

		toChatUsername = userbean.getClient_id();

		pageViewaList.tv_tagname.setText(userbean.getUsername());
		titlePopup.addAction(new ActionItem(this, getResources().getString(
				R.string.deletemes), R.color.white));
		if (!isNetworkAvailable) {
			pLog.i("test", "走网络已连接");
			/** 获取到与聊天人的会话对象。 **/

			conversation = EMChatManager.getInstance().getConversation(
					toChatUsername);
			pLog.i("test", "单聊conversation:" + conversation);
			pLog.i("test",
					"conversation.getMsgCount():" + conversation.getMsgCount());
			for (int i = 0; i < conversation.getMsgCount(); i++) {
				EMMessage message = conversation.getMessage(i);
				pLog.i("test", "message:" + message.toString());
				pLog.i("test", "message:" + message.getType());

				if (message.getType().equals(EMMessage.Type.TXT)) {
					pLog.i("test", "文本消息");
					TextMessageBody body = (TextMessageBody) message.getBody();
					String content = body.getMessage();

					String time = DateUtils.getTimestampString(new Date(message
							.getMsgTime()));
					pLog.i("test", "client_id:" + message.getFrom());

					ChatMsgEntityBean entity = new ChatMsgEntityBean();
					entity.setType("TXT");
					entity.setMessage(content);
					entity.setDate(time);
					entity.setUserbean(userbean);
					entity.setUserId(message.getFrom());
					try {
						entity.setImage(message
								.getStringAttribute(Constant.IMAGEURL));
					} catch (EaseMobException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (message.direct == EMMessage.Direct.SEND) {
						entity.setMsgType(Constant.SELF); // 1代表自己、0代表他人
					} else {
						entity.setMsgType(Constant.OTHER);
					}
					singlechatmsgList.add(entity);
				} else if (message.getType().equals(EMMessage.Type.IMAGE)) {
					pLog.i("test", "图片消息");
					ImageMessageBody body = (ImageMessageBody) message
							.getBody();
					String content;
					if (message.direct == EMMessage.Direct.SEND) {
						content = body.getLocalUrl();
					} else {
						content = body.getRemoteUrl();
					}
					pLog.i("type", "body:" + content);

					String time = DateUtils.getTimestampString(new Date(message
							.getMsgTime()));

					ChatMsgEntityBean entity = new ChatMsgEntityBean();
					entity.setType("IMAGE");
					entity.setMessage(content);
					entity.setDate(time);
					entity.setUserbean(userbean);
					entity.setUserId(message.getFrom());
					try {
						entity.setImage(message
								.getStringAttribute(Constant.IMAGEURL));
					} catch (EaseMobException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (message.direct == EMMessage.Direct.SEND) {
						entity.setMsgType(Constant.SELF); // 1代表自己、0代表他人
					} else {
						entity.setMsgType(Constant.OTHER);
					}
					singlechatmsgList.add(entity);
				}
			}
			if (singlechatadapter == null) {
				singlechatadapter = new ChatMsgViewAdapter(this,
						singlechatmsgList);
				pageViewaList.lv_chat.setAdapter(singlechatadapter);
			}
			pageViewaList.lv_chat
					.setSelection(pageViewaList.lv_chat.getCount() - 1);
			refresh();
			// 把此会话的未读数置为0
			conversation.resetUnreadMsgCount();
		} else {
			pLog.i("test", "走网络未连接");
			/** 获取本地的聊天信息 **/
			String his_message = pIOUitls.readFileByLines(
					Constant.C_FILE_CACHE_PATH,
					"meg_" + mShareFileUtils.getString(Constant.CLIENT_ID, "")
							+ toChatUsername);
			pLog.i("test", "his_message:" + his_message);
			if (his_message != null) {

				singlechatmsgListBean sListBean = new singlechatmsgListBean();

				try {
					sListBean = JsonDocHelper.toJSONObject(his_message,
							singlechatmsgListBean.class);
					singlechatmsgList = sListBean.getSinglechatmsgList();
					pLog.i("test",
							"singlechatmsgList:" + singlechatmsgList.size());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					pLog.i("test", "Exception:" + e.toString());
					e.printStackTrace();
				}

			}
			if (singlechatadapter == null) {
				singlechatadapter = new ChatMsgViewAdapter(this,
						singlechatmsgList);
				pageViewaList.lv_chat.setAdapter(singlechatadapter);
			}
			pageViewaList.lv_chat
					.setSelection(pageViewaList.lv_chat.getCount() - 1);
			refresh();
		}

		onConversationInit();
	}

	protected void onConversationInit() {

		EMChatManager.getInstance().getConversationByType(toChatUsername,
				EMConversationType.Chat);

		EMChatManager.getInstance().addChatRoomChangeListener(
				new EMChatRoomChangeListener() {

					@Override
					public void onChatRoomDestroyed(String roomId,
							String roomName) {
						if (roomId.equals(toChatUsername)) {
							finish();
						}
					}

					@Override
					public void onMemberJoined(String roomId, String participant) {
					}

					@Override
					public void onMemberExited(String roomId, String roomName,
							String participant) {

					}

					@Override
					public void onMemberKicked(String roomId, String roomName,
							String participant) {
						if (roomId.equals(toChatUsername)) {
							String curUser = EMChatManager.getInstance()
									.getCurrentUser();
							if (curUser.equals(participant)) {
								EMChatManager.getInstance().leaveChatRoom(
										toChatUsername);
								finish();
							}
						}
					}

				});
	}

//	@Override
//	protected View loadTopLayout() {
//		// TODO Auto-generated method stub
//		// return getLayoutInflater().inflate(R.layout.base_toplayout_title,
//		// null);
//		return null;
//	}

//	@Override
//	protected View loadContentLayout() {
//		// TODO Auto-generated method stub
//		return getLayoutInflater().inflate(R.layout.activity_singlesend, null);
//	}

//	@Override
//	protected View loadBottomLayout() {
//		// TODO Auto-generated method stub
//		// return getLayoutInflater().inflate(R.layout.base_btn_send, null);
//		return null;
//	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (isNetworkAvailable) {
				conversation = EMChatManager.getInstance().getConversation(
						toChatUsername);
				conversation.resetUnreadMsgCount();
			}
			String count;
			try {
				singlechatmsgListBean sListBean = new singlechatmsgListBean();
				sListBean.setSinglechatmsgList(singlechatmsgList);
				count = JsonDocHelper.toJSONString(sListBean);
				pIOUitls.saveStrToSD(Constant.C_FILE_CACHE_PATH, "meg_"
						+ mShareFileUtils.getString(Constant.CLIENT_ID, "")
						+ toChatUsername, false, count);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			backPage();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.im_downview:
			titlePopup.show(v);
			break;
		case R.id.btn_send:
			sendMessage();
			break;
		case R.id.sendphoto:
			showDialog();
			break;
		case R.id.ll_back:
			onBackPressed();
		default:
			break;
		}

	}

	/**
	 * 输入法软盘
	 */
	private void hideKeyboard() {
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	@Override
	public void onNetworkOn() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNetWorkOff() {
		// TODO Auto-generated method stub

	}

	/**
	 * 下拉框
	 */
	private void popupwindow() {
		// TODO Auto-generated method stub

		titlePopup.setItemOnClickListener(new OnItemOnClickListener() {
			@Override
			public void onItemClick(ActionItem item, int position) {
				// TODO Auto-generated method stub
				if (!isNetworkAvailable) {
					showToast(
							getResources().getString(
									R.string.Broken_network_prompt),
							Toast.LENGTH_SHORT, false);
				} else {
					easemobchatImp.getInstance().clearConversation(
							toChatUsername);
					singlechatmsgList.clear();
					refresh();

				}

			}
		});
	}

	/**
	 * 发信
	 */
	private void sendMessage() {
		// TODO Auto-generated method stub
		if (EMChatManager.getInstance().isConnected()) {
			/** 消息内容 **/
			String content = pageViewaList.et_sendmessage.getText().toString()
					.trim();
			/** 发送者头像 **/
			String imagurl = mShareFileUtils.getString(Constant.IMAGE, "");
			/** 发送者id **/
			// String userid = mShareFileUtils.getString(Constant.CLIENT_ID,
			// "");
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy年MM月dd日   HH:mm:ss     ");
			Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
			String str = formatter.format(curDate);
			ChatMsgEntityBean chat = new ChatMsgEntityBean();
			chat.setDate(str);
			chat.setImage(imagurl);
			chat.setType("TXT");
			// chat.setUserId(userid);
			chat.setMessage(content);
			chat.setMsgType(Constant.SELF);

			// 环信发送消息，携带消息内容，自己头像，自己Id
			easemobchatImp.getInstance().sendMessage(content,
					Constant.SINGLECHAT, toChatUsername, imagurl);
			singlechatmsgList.add(chat);
			refresh();
			pageViewaList.lv_chat
					.setSelection(pageViewaList.lv_chat.getCount() - 1);
			pageViewaList.et_sendmessage.setText("");
		} else {
			showToast(getResources().getString(R.string.network_wait),
					Toast.LENGTH_SHORT, false);
			easemobchatImp.getInstance().login(
					mShareFileUtils.getString(Constant.CLIENT_ID, "").replace(
							"-", ""),
					mShareFileUtils.getString(Constant.PASSWORD, ""));
		}

		EMChatManager.getInstance().joinChatRoom(toChatUsername,
				new EMValueCallBack<EMChatRoom>() {

					@Override
					public void onSuccess(EMChatRoom value) {
						// TODO Auto-generated method stub
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								EMChatManager.getInstance().getChatRoom(
										toChatUsername);
							}
						});
					}

					@Override
					public void onError(final int error, String errorMsg) {
						// TODO Auto-generated method stub
						pLog.i("zzg", "error:" + error);
						pLog.i("zzg", "errorMsg:" + errorMsg);
						// finish();
					}
				});
	}

	/**
	 * 发图片
	 */
	@SuppressWarnings("static-access")
	private void sendImgMessage(File newfile) {
		// TODO Auto-generated method stub
		if (EMChatManager.getInstance().isConnected()) {

			/** 发送者头像 **/
			String imagurl = mShareFileUtils.getString(Constant.IMAGE, "");
			// 08-29 22:01:08.789: I/type(19269):
			// 图片路径：/storage/sdcard0/peer/imagecache/head.png
			pLog.i("type", "图片路径：" + newfile.getPath());
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy年MM月dd日   HH:mm:ss     ");
			Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
			String str = formatter.format(curDate);
			ChatMsgEntityBean chat = new ChatMsgEntityBean();
			chat.setDate(str);
			chat.setImage(imagurl);
			chat.setType("IMAGE");
			// chat.setUserId(userid);
			chat.setMessage(newfile.getPath());
			chat.setMsgType(Constant.SELF);

			// 环信发送消息，携带消息内容，自己头像，自己Id
			easemobchatImp.getInstance().sendImgMessage(newfile,
					toChatUsername, imagurl);
			singlechatmsgList.add(chat);
			refresh();
			pageViewaList.lv_chat
					.setSelection(pageViewaList.lv_chat.getCount() - 1);
			pageViewaList.et_sendmessage.setText("");
		} else {
			showToast(getResources().getString(R.string.network_wait),
					Toast.LENGTH_SHORT, false);
			easemobchatImp.getInstance().login(
					mShareFileUtils.getString(Constant.CLIENT_ID, "").replace(
							"-", ""),
					mShareFileUtils.getString(Constant.PASSWORD, ""));
		}
	}

	/**
	 * 监听接收消息
	 */
	private void initChatListener() {
		// TODO Auto-generated method stub
		// 注册接收消息广播
		receiver = new NewMessageBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(EMChatManager
				.getInstance().getNewMessageBroadcastAction());
		// 设置广播的优先级别大于MainAcitivity,这样如果消息来的时候正好在chat页面，直接显示消息，而不是提示消息未读
		intentFilter.setPriority(5);
		registerReceiver(receiver, intentFilter);
	}

	/**
	 * 消息广播接收者
	 * 
	 */
	private class NewMessageBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 记得把广播给终结掉
			abortBroadcast();
			String username = intent.getStringExtra("from");
			String msgid = intent.getStringExtra("msgid");
			// 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
			EMMessage message = EMChatManager.getInstance().getMessage(msgid);

			// type定义 0：文本消息 1：图片消息
			int msgType = intent.getIntExtra("type", 0);
			pLog.i("type",
					"new message id:" + msgid + " from:" + message.getFrom()
							+ " type:" + msgType);

			String from = message.getFrom();
			if (msgType == 0) {

				// 如果是群聊消息，获取到group id
				String image = null;
				String nickname = null;
				if (message.getChatType() == ChatType.Chat) {
					pLog.i("test",
							"单聊监听message.getChatType()："
									+ message.getChatType());
					try {
						image = message.getStringAttribute(Constant.IMAGEURL);

					} catch (EaseMobException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// 获取到消息
					TextMessageBody txtBody = (TextMessageBody) message
							.getBody();
					String msg = txtBody.getMessage();

					SimpleDateFormat formatter = new SimpleDateFormat(
							"yyyy年MM月dd日   HH:mm:ss     ");
					Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
					String str = formatter.format(curDate);

					pLog.i("test", "to:" + from);
					pLog.i("test", "toChatUsername:" + toChatUsername);

					if (from.equals(toChatUsername)) {
						ChatMsgEntityBean chat = new ChatMsgEntityBean();

						chat.setUserId(from);
						chat.setImage(image);
						chat.setDate(str);
						chat.setType("TXT");
						chat.setMessage(msg);
						chat.setMsgType(Constant.OTHER);
						chat.setUserbean(userbean);
						singlechatmsgList.add(chat);
						singlechatadapter.notifyDataSetChanged();
						pageViewaList.lv_chat
								.setSelection(pageViewaList.lv_chat.getCount() - 1);
					}
				} else {
					multinotifyNewMessage(message);
				}
			} else if (msgType == 1) {
				pLog.i("type", "message:" + message.toString());
				// 如果是群聊消息，获取到group id
				String image = null;
				String nickname = null;
				try {
					image = message.getStringAttribute(Constant.IMAGEURL);

				} catch (EaseMobException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 获取到消息
				ImageMessageBody body = (ImageMessageBody) message.getBody();
				String content = body.getRemoteUrl();
				pLog.i("type", "监听：" + body.getRemoteUrl());

				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy年MM月dd日   HH:mm:ss     ");
				Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
				String str = formatter.format(curDate);

				pLog.i("test", "to:" + from);
				pLog.i("test", "toChatUsername:" + toChatUsername);

				if (from.equals(toChatUsername)) {
					ChatMsgEntityBean chat = new ChatMsgEntityBean();

					chat.setUserId(from);
					chat.setImage(image);
					chat.setDate(str);
					chat.setType("IMAGE");
					chat.setMessage(content);
					chat.setMsgType(Constant.OTHER);
					chat.setUserbean(userbean);
					singlechatmsgList.add(chat);
					singlechatadapter.notifyDataSetChanged();
					pageViewaList.lv_chat.setSelection(pageViewaList.lv_chat
							.getCount() - 1);
				}

			}
		}
	}

	private void refresh() {

		if (singlechatadapter != null) {
			singlechatadapter.notifyDataSetChanged();
		}
	}

	public String getToChatUsername() {
		return toChatUsername;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			unregisterReceiver(receiver);
			receiver = null;
		} catch (Exception e) {
		}
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
				SingleChatRoomActivity.this, 0, intent, 0);
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

	// 图片选择器
	private void showDialog() {

		new AlertDialog.Builder(this)
				.setTitle(getResources().getString(R.string.sendimg))
				.setItems(items, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0: // 选择本地
							Intent intentFromGallery = new Intent(
									Intent.ACTION_PICK, null);
							intentFromGallery
									.setDataAndType(
											MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
											"image/*");
							startActivityForResult(intentFromGallery,
									IMAGE_REQUEST_CODE);
							break;
						case 1: // 拍照

							Intent intentFromCapture = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							if (Tools.hasSdcard()) {
								intentFromCapture.putExtra(
										MediaStore.EXTRA_OUTPUT,
										Uri.fromFile(new File(Environment
												.getExternalStorageDirectory(),
												IMAGE_FILE_NAME)));
							}
							startActivityForResult(intentFromCapture,
									CAMERA_REQUEST_CODE);
							break;
						}
					}
				})
				.setNegativeButton(getResources().getString(R.string.cancel),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).show();

	}

	/*
	 * public byte[] getBitmapByte(Bitmap bitmap) { if (bitmap == null) { return
	 * null; } final ByteArrayOutputStream os = new ByteArrayOutputStream();
	 * 
	 * bitmap.compress(Bitmap.CompressFormat.PNG, 100, os); return
	 * os.toByteArray(); }
	 */

	public void startPhotoZoom(Uri uri) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");

		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);

		intent.putExtra("outputX", 250);
		intent.putExtra("outputY", 250);

		intent.putExtra("outputFormat", "PNG");
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, RESULT_REQUEST_CODE);
	}

	public Bitmap getImageToView(Intent data, String uuid) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			photo = extras.getParcelable("data");
			BussinessUtils.sendBitmapFile(photo, uuid);
		}
		return photo;

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_CANCELED) {
			switch (requestCode) {
			case IMAGE_REQUEST_CODE:
				startPhotoZoom(data.getData());
				break;
			case CAMERA_REQUEST_CODE:
				if (Tools.hasSdcard()) {
					File tempFile = new File(
							Environment.getExternalStorageDirectory() + "/"
									+ IMAGE_FILE_NAME);
					startPhotoZoom(Uri.fromFile(tempFile));
				} else {
					showToast(getResources().getString(R.string.sdcard),
							Toast.LENGTH_LONG, false);
				}
				break;
			case RESULT_REQUEST_CODE:
				if (data != null) {
					String uuidstr = BussinessUtils.getUUID();
					Bitmap bt = getImageToView(data, uuidstr);
					File file = new File(Constant.C_IMAGE_CACHE_PATH + uuidstr
							+ ".png");
					pLog.i("type", "File路径：" + Constant.C_IMAGE_CACHE_PATH
							+ uuidstr + ".png");
					if (file.exists()) {
						pLog.i("type", "文件存在");
						sendImgMessage(file);
					}

				}
				break;
			}
		}
	}

	/**
	 * 环信聊天监听
	 */

	@SuppressWarnings("incomplete-switch")
	@Override
	public void onEvent(EMNotifierEvent event) {
		// TODO Auto-generated method stub
		switch (event.getEvent()) {
		case EventNewMessage: {
			// 获取到message
			EMMessage message = (EMMessage) event.getData();
			String username = message.getFrom();

			Message message1 = new Message();

			// 如果是当前会话的消息，刷新聊天页面
			if (username.equals(getToChatUsername())) {

				String from = message.getFrom();
				String image = null;
				String msg = null;
				String str = BussinessUtils.getSysTime();

				ChatMsgEntityBean chat = new ChatMsgEntityBean();

				try {
					image = message.getStringAttribute(Constant.IMAGEURL);
				} catch (EaseMobException e) {
					e.printStackTrace();
				}

				switch (message.getType()) {
				case IMAGE: // 图片

					// 获取到消息
					ImageMessageBody body = (ImageMessageBody) message
							.getBody();
					String content = body.getRemoteUrl();
					chat.setType("IMAGE");
					chat.setMessage(content);

					break;

				case TXT: // 文本

					// 获取到消息
					TextMessageBody txtBody = (TextMessageBody) message
							.getBody();
					msg = txtBody.getMessage();
					chat.setType("TXT");
					chat.setMessage(msg);
					break;
				}
				chat.setUserId(from);
				chat.setImage(image);
				chat.setDate(str);
				chat.setMsgType(Constant.OTHER);
				chat.setUserbean(userbean);

				singlechatmsgList.add(chat);

				message1.what = EVENT_NEW_MESSAGE;

				// 声音和震动提示有新消息

				// showNotification.sendNotification(SingleChatRoomActivity.this,
				// message, mShareFileUtils, true);
			} else {
				// 如果消息不是和当前聊天ID的消息

				message1.what = EVENT_NEW_OTHER_MESSAGE;
				message1.obj = message;

			}

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
			case EVENT_NEW_MESSAGE:
				singlechatadapter.notifyDataSetChanged();
				pageViewaList.lv_chat.setSelection(pageViewaList.lv_chat
						.getCount() - 1);
				break;

			case EVENT_NEW_OTHER_MESSAGE:
				try {
					EMMessage msg = (EMMessage) message.obj;
					senduser(msg.getFrom(),
							mShareFileUtils.getString(Constant.CLIENT_ID, ""),
							msg);
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
			params = PeerParamsUtils.getUserParams(SingleChatRoomActivity.this,
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
					pLog.i("test", "code:" + code);
					if (code.equals("200")) {
						LoginBean loginBean = JsonDocHelper.toJSONObject(
								response.getJSONObject("success").toString(),
								LoginBean.class);
						if (loginBean != null) {
							// singlenotifyNewMessage(loginBean, message);
							showNotification.sendNotification(
									SingleChatRoomActivity.this, message,
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
}
