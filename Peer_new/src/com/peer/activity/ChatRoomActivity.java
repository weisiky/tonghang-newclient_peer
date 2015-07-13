package com.peer.activity;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.DateUtils;
import com.easemob.util.DensityUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.peer.IMimplements.easemobchatImp;
import com.peer.adapter.ChatMsgViewAdapter;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.bean.ChatMsgEntityBean;
import com.peer.bean.ChatRoomBean;
import com.peer.bean.JoinTopicBean;
import com.peer.bean.LoginBean;
import com.peer.bean.PersonpageBean;
import com.peer.bean.UserBean;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.service.FxService;
import com.peer.titlepopwindow.ActionItem;
import com.peer.titlepopwindow.TitlePopup;
import com.peer.titlepopwindow.TitlePopup.OnItemOnClickListener;
import com.peer.utils.BussinessUtils;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.pLog;
import com.peer.utils.pViewBox;
import com.umeng.analytics.MobclickAgent;

/**
 * 聊天室
 */
public class ChatRoomActivity extends pBaseActivity {

	private TitlePopup titlePopup;
	private boolean page = true;
	private InputMethodManager manager;
	private String mPageName = "ChatRoom";
	private List<ChatMsgEntityBean> msgList = new ArrayList<ChatMsgEntityBean>();
	private String toChatUsername;
	String theme = null;
	private EMConversation conversation;
	private ChatMsgViewAdapter adapter;
	private NewMessageBroadcastReceiver receiver;
	private String topicId;
	private int num = 1; // num用作判断用户是否点击theme_chat 0-no;1-yes

	ScrollView mScrollView;

	class PageViewList {
		private LinearLayout ll_back;
		private TextView tv_title, tv_share, tv_nikename, theme_chat;
		private RelativeLayout host_imfor, rl_bottom;
		private ImageView host_image, im_downview;
		private ListView lv_chat;
		private Button btn_send;
		private EditText et_sendmessage;
	}

	private PageViewList pageViewaList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(mPageName);
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

		mScrollView = (ScrollView) findViewById(R.id.scrollContent);
		mScrollView.setVerticalScrollBarEnabled(false);
		mScrollView.setHorizontalScrollBarEnabled(false);

		hideKeyboard();// 软键盘
		pageViewaList.btn_send.setEnabled(false);
		pageViewaList.btn_send.addTextChangedListener(new TextWatcher() {

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
		pageViewaList.ll_back.setOnClickListener(this);
		pageViewaList.im_downview.setOnClickListener(this);
		pageViewaList.host_image.setOnClickListener(this);
		pageViewaList.theme_chat.setOnClickListener(this);
		pageViewaList.tv_share.setOnClickListener(this);
		pageViewaList.btn_send.setOnClickListener(this);
	}

	@Override
	protected void processBiz() {
		// TODO Auto-generated method stub
		/** 判断群聊 **/
		if (ChatRoomBean.getInstance().getChatroomtype() == Constant.MULTICHAT) {

			toChatUsername = ChatRoomBean.getInstance().getTopicBean()
					.getTopic_id();
			pageViewaList.host_imfor.setVisibility(View.VISIBLE);

			/** 判断属于自己的房间 **/
			if (!ChatRoomBean.getInstance().isIsowner()) {
				titlePopup.addAction(new ActionItem(this, getResources()
						.getString(R.string.lookformember), R.color.white));
			} else {
				titlePopup.addAction(new ActionItem(this, getResources()
						.getString(R.string.exitroom), R.color.white));
				titlePopup.addAction(new ActionItem(this, getResources()
						.getString(R.string.lookformember), R.color.white));
			}

			Intent intent = new Intent();
			/** 判断是否从悬浮头像进入 **/
			if (intent.getStringExtra(Constant.FROMFLOAT) != null
					&& intent.getStringExtra(Constant.FROMFLOAT).equals(
							Constant.FROMFLOAT)) {
				pageViewaList.tv_title.setText(ChatRoomBean.getInstance()
						.getTopicBean().getLabel_name());
				pageViewaList.tv_nikename.setText(ChatRoomBean.getInstance()
						.getUserBean().getUsername());
				pageViewaList.theme_chat.setText(ChatRoomBean.getInstance()
						.getTopicBean().getSubject());
				topicId = ChatRoomBean.getInstance().getTopicBean()
						.getTopic_id();
			} else {
				pageViewaList.tv_title.setText(ChatRoomBean.getInstance()
						.getTopicBean().getLabel_name());
				UserBean u = ChatRoomBean.getInstance().getUserBean();
				pageViewaList.tv_nikename.setText(u.getUsername());
				theme = ChatRoomBean.getInstance().getTopicBean().getSubject();
				pageViewaList.theme_chat.setText(theme);
				pageViewaList.theme_chat.setEllipsize(TextUtils.TruncateAt
						.valueOf("END"));
				// topicId=toChatUsername;
			}

			/** 群聊，且为自己的房间时，退出显示头像 **/
			Intent serviceintent = new Intent(ChatRoomActivity.this,
					FxService.class);
			stopService(serviceintent);
			mShareFileUtils.setBoolean(Constant.ISFLOAT, false);

			easemobchatImp.getInstance().joingroup(toChatUsername);

			/** 发送加入话题请求 **/
			sendjoingroup(ChatRoomBean.getInstance().getUserBean()
					.getClient_id(), ChatRoomBean.getInstance().getTopicBean()
					.getTopic_id());

			/** 未读消息数清零 **/
			conversation = EMChatManager.getInstance().getConversation(
					toChatUsername);
			conversation.resetUnreadMsgCount();

		} else if (ChatRoomBean.getInstance().getChatroomtype() == Constant.SINGLECHAT) {
			/** 单聊页面 **/
			toChatUsername = ChatRoomBean.getInstance().getUserBean()
					.getClient_id();
			pageViewaList.host_imfor.setVisibility(View.GONE);
			pageViewaList.tv_title.setText(ChatRoomBean.getInstance()
					.getUserBean().getUsername());
			titlePopup.addAction(new ActionItem(this, getResources().getString(
					R.string.deletemes), R.color.white));
			conversation = EMChatManager.getInstance().getConversation(
					toChatUsername);
			for (int i = 0; i < conversation.getMsgCount(); i++) {
				EMMessage message = conversation.getMessage(i);

				TextMessageBody body = (TextMessageBody) message.getBody();
				String content = body.getMessage();
				String time = DateUtils.getTimestampString(new Date(message
						.getMsgTime()));

				ChatMsgEntityBean entity = new ChatMsgEntityBean();
				entity.setMessage(content);
				entity.setDate(time);
				try {
					entity.setImage(message
							.getStringAttribute(Constant.IMAGEURL));
					entity.setUserId(message
							.getStringAttribute(Constant.USERID));
				} catch (EaseMobException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (message.direct == EMMessage.Direct.SEND) {
					entity.setMsgType(Constant.SELF);
				} else {
					entity.setMsgType(Constant.OTHER);
				}
				msgList.add(entity);
			}
			adapter = new ChatMsgViewAdapter(this, msgList);
			pageViewaList.lv_chat.setAdapter(adapter);
			pageViewaList.lv_chat
					.setSelection(pageViewaList.lv_chat.getCount() - 1);

			conversation.resetUnreadMsgCount();
		}

	}

	@Override
	protected View loadTopLayout() {
		// TODO Auto-generated method stub
		return getLayoutInflater().inflate(R.layout.base_toplayout_title, null);
	}

	@Override
	protected View loadContentLayout() {
		// TODO Auto-generated method stub
		return getLayoutInflater().inflate(R.layout.activity_chatroom, null);
	}

	@Override
	protected View loadBottomLayout() {
		// TODO Auto-generated method stub
		return getLayoutInflater().inflate(R.layout.base_btn_send, null);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (true) {
				if (!JoinTopicBean.getInstance().getIsower()) {
					sendleavegroup(ChatRoomBean.getInstance().getUserBean()
							.getClient_id(), ChatRoomBean.getInstance()
							.getTopicBean().getTopic_id());
					backPage();
				} else {
					startfloatView();
				}
			} else {
				backPage();
			}
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
		case R.id.theme_chat:
			showtheme(num);
			num++;
			break;
		case R.id.tv_share:
			showToast("模拟测试", Toast.LENGTH_SHORT, false);
			break;

		case R.id.im_downview:
			titlePopup.show(v);
			break;
		case R.id.host_image:
			Intent intent = new Intent();
			if (isNetworkAvailable) {
				if (JoinTopicBean.getInstance().getIsower()) {
					UserBean u = JoinTopicBean.getInstance().getUserbean();
					PersonpageBean.getInstance().setUser(u);
					startActivityForLeft(PersonalPageActivity.class, intent, false);
				}else{
					try {
						senduser(JoinTopicBean.getInstance().getClient_id());
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else {
				showToast("网络未连接", Toast.LENGTH_SHORT, false);
			}

			break;
		case R.id.btn_send:
			
			/** 以前版本好像群聊走自己服务器，不走环信。具体不清楚。这里只发环信，可能出错 **/
//			 if(ChatRoomBean.getInstance().getChatroomtype()==Constant.MULTICHAT){
//			 reply();
//			 }else{
			 sendMessage();
//			 }
			break;
		default:
			break;
		}

	}

	/**
	 * 悬浮头像设置
	 * 
	 */
	public void startfloatView() {
		if (ChatRoomBean.getInstance().getChatroomtype() == Constant.MULTICHAT) {
			if (mShareFileUtils.getBoolean(Constant.ISFLOAT, false)) {
				Intent intentfloat = new Intent(ChatRoomActivity.this,
						FxService.class);
				UserBean u = ChatRoomBean.getInstance().getUserBean();
				intentfloat.putExtra(Constant.IMAGE, ChatRoomBean.getInstance()
						.getUserBean().getImage());
				intentfloat.putExtra(Constant.OWNERNIKE, ChatRoomBean
						.getInstance().getUserBean().getUsername());
				intentfloat.putExtra(Constant.THEME, ChatRoomBean.getInstance()
						.getTopicBean().getSubject());
				intentfloat.putExtra(Constant.TAGNAME, ChatRoomBean
						.getInstance().getTopicBean().getLabel_name());
				intentfloat.putExtra(Constant.USERID, ChatRoomBean
						.getInstance().getUserBean().getClient_id());
				intentfloat.putExtra(Constant.ROOMID, ChatRoomBean
						.getInstance().getTopicBean().getTopic_id());
				intentfloat.putExtra(Constant.TOPICID, ChatRoomBean
						.getInstance().getTopicBean().getTopic_id());
				intentfloat.putExtra(Constant.FROMFLOAT, "float");
				startService(intentfloat);
				finish();
				mShareFileUtils.setBoolean(Constant.ISFLOAT, true);
			} else {
				finish();
			}
		} else if (ChatRoomBean.getInstance().getChatroomtype() == Constant.SINGLECHAT) {
			// 把此会话的未读数置为0
			conversation.resetUnreadMsgCount();
			finish();
		} else {
			easemobchatImp.getInstance().exitgroup(toChatUsername);
			finish();
		}
	}

	/**
	 * 话题内容扩展 客户端效果 话题群聊页面，展示所有话题内容。
	 */
	private void showtheme(int i) {

		// TODO Auto-generated method stub
		if ((i % 2) == 0) {

			LayoutParams owner = pageViewaList.host_imfor.getLayoutParams();
			owner.height = DensityUtil.dip2px(this, 64);
			pageViewaList.host_imfor.setLayoutParams(owner);
			pageViewaList.theme_chat.setMaxLines(1);
			pageViewaList.theme_chat.setEllipsize(TextUtils.TruncateAt
					.valueOf("END"));
		} else {
			pageViewaList.theme_chat.setText(ChatRoomBean.getInstance()
					.getTopicBean().getSubject());
			if (theme.length() <= 14) { // 1行
				LayoutParams owner = pageViewaList.host_imfor.getLayoutParams();
				owner.height = DensityUtil.dip2px(this, 64);
				pageViewaList.host_imfor.setLayoutParams(owner);
				pageViewaList.theme_chat.setMaxLines(5);
			} else if (theme.length() <= 28) { // 2
				LayoutParams owner = pageViewaList.host_imfor.getLayoutParams();
				owner.height = DensityUtil.dip2px(this, 79);
				pageViewaList.host_imfor.setLayoutParams(owner);
				pageViewaList.theme_chat.setMaxLines(5);
			} else if (theme.length() <= 42) { // 3
				LayoutParams owner = pageViewaList.host_imfor.getLayoutParams();
				owner.height = DensityUtil.dip2px(this, 94);
				pageViewaList.host_imfor.setLayoutParams(owner);
				pageViewaList.theme_chat.setMaxLines(5);
			} else if (theme.length() <= 56) { // 4
				LayoutParams owner = pageViewaList.host_imfor.getLayoutParams();
				owner.height = DensityUtil.dip2px(this, 109);
				pageViewaList.host_imfor.setLayoutParams(owner);
				pageViewaList.theme_chat.setMaxLines(5);
			} else { // 5
				LayoutParams owner = pageViewaList.host_imfor.getLayoutParams();
				owner.height = DensityUtil.dip2px(this, 124);
				pageViewaList.host_imfor.setLayoutParams(owner);
				pageViewaList.theme_chat.setMaxLines(5);
			}
			pageViewaList.theme_chat.setEllipsize(TextUtils.TruncateAt
					.valueOf("END"));
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
					if (item.mTitle.equals(getResources().getString(
							R.string.exitroom))) {
						easemobchatImp.getInstance().exitgroup(toChatUsername);
						sendleavegroup(ChatRoomBean.getInstance().getUserBean()
								.getClient_id(), ChatRoomBean.getInstance()
								.getTopicBean().getTopic_id());
						finish();
					} else if (item.mTitle.equals(getResources().getString(
							R.string.deletemes))) {
						easemobchatImp.getInstance().clearConversation(
								toChatUsername);
						msgList.clear();
						adapter.notifyDataSetChanged();
					} else if (item.mTitle.equals(getResources().getString(
							R.string.lookformember))) {
						Intent intent = new Intent(ChatRoomActivity.this,
								ChatRoomListnikeActivity.class);
						intent.putExtra("groupId", ChatRoomBean.getInstance()
								.getTopicBean().getTopic_id());
						intent.putExtra("client_id", ChatRoomBean.getInstance()
								.getUserBean().getClient_id());
						startActivity(intent);
					}
				}

			}
		});
	}
	
	

	private void sendMessage() {
		// TODO Auto-generated method stub
		if(EMChatManager.getInstance().isConnected()){
			String content=pageViewaList.et_sendmessage.getText().toString().trim();	
			String imagurl = JoinTopicBean.getInstance().getUserbean().getImage();
			String userid = JoinTopicBean.getInstance().getUserbean().getClient_id();
			//环信发送消息，携带消息内容，自己头像，自己Id
			easemobchatImp.getInstance().sendMessage(content, Constant.SINGLECHAT, toChatUsername,imagurl,userid);			
			
			SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日   HH:mm:ss     ");     
			 Date   curDate   =   new   Date(System.currentTimeMillis());//获取当前时间     
			String   str   =   formatter.format(curDate);     
			ChatMsgEntityBean.getInstance().setDate(str);
			ChatMsgEntityBean.getInstance().setImage(imagurl);
			ChatMsgEntityBean.getInstance().setUserId(userid);
			ChatMsgEntityBean.getInstance().setMessage(content);
			ChatMsgEntityBean.getInstance().setMsgType(Constant.SELF);					
			msgList.add(ChatMsgEntityBean.getInstance());
			adapter.notifyDataSetChanged();
			pageViewaList.lv_chat
			.setSelection(pageViewaList.lv_chat.getCount() - 1);
			pageViewaList.et_sendmessage.setText("");
		}else{
			showToast(getResources().getString(R.string.broken_net), Toast.LENGTH_SHORT, false);
		}
	}

	private void initChatListener() {
		// TODO Auto-generated method stub
		// 注册接收消息广播
		receiver = new NewMessageBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(EMChatManager
				.getInstance().getNewMessageBroadcastAction());
		// 设置广播的优先级别大于Mainacitivity,这样如果消息来的时候正好在chat页面，直接显示消息，而不是提示消息未读
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
			// 如果是群聊消息，获取到group id
			String image = null;
			String fromuserid = null;
			String nickname = null;
			try {
				if (ChatRoomBean.getInstance().getChatroomtype() == Constant.MULTICHAT) {
					nickname = message.getStringAttribute(Constant.NICKNAME);
				} else {
					image = message.getStringAttribute(Constant.IMAGEURL);
				}
				fromuserid = message.getStringAttribute(Constant.USERID);

			} catch (EaseMobException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 获取到消息
			TextMessageBody txtBody = (TextMessageBody) message.getBody();
			String msg = txtBody.getMessage();

			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy年MM月dd日   HH:mm:ss     ");
			Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
			String str = formatter.format(curDate);
			ChatMsgEntityBean.getInstance().setUserId(fromuserid);
			ChatMsgEntityBean.getInstance().setImage(image);
			ChatMsgEntityBean.getInstance().setName(nickname);
			ChatMsgEntityBean.getInstance().setDate(str);
			ChatMsgEntityBean.getInstance().setMessage(msg);
			ChatMsgEntityBean.getInstance().setMsgType(Constant.OTHER);
			msgList.add(ChatMsgEntityBean.getInstance());
			adapter.notifyDataSetChanged();
			pageViewaList.lv_chat
					.setSelection(pageViewaList.lv_chat.getCount() - 1);
		}
	}

	/**
	 * 加入话题请求
	 * 
	 * @param client_id
	 * @param topic_id
	 * @exception UnsupportedEncodingException
	 */
	private void sendjoingroup(String client_id, String topic_id) {
		// TODO Auto-generated method stub
		final Intent intent = new Intent();
		HttpEntity entity = null;
		try {
			entity = PeerParamsUtils.getJoinParams(ChatRoomActivity.this,
					client_id, topic_id);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		HttpUtil.post(ChatRoomActivity.this, HttpConfig.JOIN_TOPIC_IN_URL,
				entity, "application/json;charset=utf-8",
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub

						hideLoading();


						super.onFailure(statusCode, headers, responseString,
								throwable);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONArray errorResponse) {
						// TODO Auto-generated method stub
						hideLoading();
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						hideLoading();
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub

						try {
							JoinTopicBean jointopicbean = JsonDocHelper
									.toJSONObject(
											response.getJSONObject("success")
													.toString(),
											JoinTopicBean.class);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						super.onSuccess(statusCode, headers, response);

					}

				});
	}

	/**
	 * 退出话题请求
	 * 
	 * @param client_id
	 * @param topic_id
	 * @exception UnsupportedEncodingException
	 */
	private void sendleavegroup(String client_id, String topic_id) {
		// TODO Auto-generated method stub
		final Intent intent = new Intent();
		HttpEntity entity = null;
		try {
			entity = PeerParamsUtils.getJoinParams(ChatRoomActivity.this,
					client_id, topic_id);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		HttpUtil.post(ChatRoomActivity.this, HttpConfig.LEAVE_TOPIC_IN_URL,
				entity, "application/json;charset=utf-8",
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub

						hideLoading();


						super.onFailure(statusCode, headers, responseString,
								throwable);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONArray errorResponse) {
						// TODO Auto-generated method stub
						hideLoading();
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						hideLoading();
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						try {
							JSONObject result = response
									.getJSONObject("success");
							String code = (String) result.get("code");
							if (code.equals("ok")) {
								showToast("已退出话题", Toast.LENGTH_SHORT, false);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						super.onSuccess(statusCode, headers, response);

					}

				});
	}
	
	
	/**
	 * 获取用户信息接口
	 * 
	 * @param client_id
	 * @throws UnsupportedEncodingException
	 */

	private void senduser(String client_id)
			throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		final Intent intent = new Intent();
		HttpEntity entity = null;
		try {
			entity = PeerParamsUtils.getUserParams(this,client_id);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		HttpUtil.post(this, HttpConfig.USER_IN_URL+client_id+".json", entity,
				"application/json;charset=utf-8",
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub

						super.onFailure(statusCode, headers, responseString,
								throwable);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONArray errorResponse) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}


					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						try {
							LoginBean loginBean = JsonDocHelper.toJSONObject(
									response.getJSONObject("success")
											.toString(), LoginBean.class);
							
							PersonpageBean.getInstance().setUser(LoginBean.getInstance().user);
							startActivityForLeft(PersonalPageActivity.class, intent, false);

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
