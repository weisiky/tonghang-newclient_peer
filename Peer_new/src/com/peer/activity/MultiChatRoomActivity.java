package com.peer.activity;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
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
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMConversation.EMConversationType;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.DateUtils;
import com.easemob.util.DensityUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.peer.R;
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
import com.peer.share.ShareConfig;
import com.peer.titlepopwindow.ActionItem;
import com.peer.titlepopwindow.TitlePopup;
import com.peer.titlepopwindow.TitlePopup.OnItemOnClickListener;
import com.peer.utils.ImageLoaderUtil;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.pLog;
import com.peer.utils.pViewBox;
import com.umeng.analytics.MobclickAgent;

/**
 * 群聊室
 * @author weisiky
 *
 */
public class MultiChatRoomActivity extends pBaseActivity{


	private TitlePopup titlePopup;
	private boolean page = true;
	private InputMethodManager manager;
	private String mPageName = "ChatRoom";
	private List<ChatMsgEntityBean> multichatmsgList = new ArrayList<ChatMsgEntityBean>();
	private String toChatUsername;
	String theme = null;
	private EMConversation conversation;
	private ChatMsgViewAdapter multichatadapter;
//	public static MultiChatRoomActivity multiactivityInstance = null;
	private NewMessageBroadcastReceiver receiver;
	private UserBean userbean;
	private int num = 1; // num用作判断用户是否点击theme_chat 0-no;1-yes
	
	//环信获取历史记录pageindex
	private int pageindex = 20;
	
	
	


	class PageViewList {
		private TextView tv_tagname, tv_share, tv_nikename, theme_chat;
		private RelativeLayout host_imfor, rl_bottom;
		private ImageView host_image, im_downview;
		private ListView lv_chat;
		private Button btn_send;
		private EditText et_sendmessage;
		private LinearLayout ll_back;
	}

	private PageViewList pageViewaList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
						| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

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

		hideKeyboard();// 软键盘
		pageViewaList.btn_send.setEnabled(false);
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
		pageViewaList.host_image.setOnClickListener(this);
		pageViewaList.theme_chat.setOnClickListener(this);
		pageViewaList.tv_share.setOnClickListener(this);
		pageViewaList.btn_send.setOnClickListener(this);
		pageViewaList.ll_back.setOnClickListener(this);
		initChatListener();
	}

	@SuppressWarnings("static-access")
	@Override
	protected void processBiz() {
		// TODO Auto-generated method stub
		pLog.i("test","Chatroomtype1:"+ChatRoomBean.getInstance().getChatroomtype());
		/** 判断群聊 **/
			pageViewaList.host_imfor.setVisibility(View.VISIBLE);
			Intent intent = getIntent();
			/** 判断是否从悬浮头像进入 **/
			pLog.i("test", "FROMFLOAT:"+intent.getStringExtra(Constant.FROMFLOAT));
			pLog.i("test", "FROMFLOAT:"+Constant.FROMFLOAT.equals(
					intent.getStringExtra(Constant.FROMFLOAT)));
			if (intent.getStringExtra(Constant.FROMFLOAT) != null
					&& intent.getStringExtra(Constant.FROMFLOAT).equals(
							Constant.FROMFLOAT)) {
				toChatUsername = mShareFileUtils.getString(Constant.F_TOPICID, "");
				pLog.i("test", "从悬浮头像进:"+toChatUsername);
				pageViewaList.tv_tagname.setText(mShareFileUtils.getString(Constant.F_TAGNAME, ""));
				pageViewaList.tv_nikename.setText(mShareFileUtils.getString(Constant.USERNAME, ""));
				pageViewaList.theme_chat.setText(mShareFileUtils.getString(Constant.F_THEME, ""));
				// ImageLoader加载图片
				ImageLoaderUtil.getInstance().showHttpImage(
						mShareFileUtils.getString(Constant.PIC_SERVER, "")
						+mShareFileUtils.getString(Constant.IMAGE, "")
						, pageViewaList.host_image,
						R.drawable.mini_avatar_shadow);
				mShareFileUtils.setBoolean(Constant.FLOAT,true);
				
				titlePopup.addAction(new ActionItem(this, getResources()
						.getString(R.string.exitroom), R.color.white));
				titlePopup.addAction(new ActionItem(this, getResources()
						.getString(R.string.lookformember), R.color.white));
			} else {
				
				toChatUsername = ChatRoomBean.getInstance().getTopicBean()
						.getTopic_id();
				pageViewaList.tv_tagname.setText(ChatRoomBean.getInstance()
						.getTopicBean().getLabel_name());
				pageViewaList.tv_nikename.setText(ChatRoomBean.getInstance().getTopicBean()
						.getUsername());
				theme = ChatRoomBean.getInstance().getTopicBean().getSubject();
				pageViewaList.theme_chat.setText(theme);
				pageViewaList.theme_chat.setEllipsize(TextUtils.TruncateAt
						.valueOf("END"));
				// ImageLoader加载图片
				ImageLoaderUtil.getInstance().showHttpImage(
						mShareFileUtils.getString(Constant.PIC_SERVER, "")
						+ChatRoomBean.getInstance().getTopicBean().getImage() 
						, pageViewaList.host_image,
						R.drawable.mini_avatar_shadow);
				mShareFileUtils.setBoolean(Constant.FLOAT,false);
				
				/** 判断属于自己的房间 **/
				if (!ChatRoomBean.getInstance().getIsowner()) {
					titlePopup.addAction(new ActionItem(this, getResources()
							.getString(R.string.lookformember), R.color.white));
				} else {
					mShareFileUtils.setString(Constant.F_THEME, theme);
					mShareFileUtils.setString(Constant.F_TAGNAME, ChatRoomBean.getInstance()
						.getTopicBean().getLabel_name());
					mShareFileUtils.setString(Constant.F_ROOMID,toChatUsername);
					mShareFileUtils.setString(Constant.F_TOPICID,toChatUsername);
					titlePopup.addAction(new ActionItem(this, getResources()
							.getString(R.string.exitroom), R.color.white));
					titlePopup.addAction(new ActionItem(this, getResources()
							.getString(R.string.lookformember), R.color.white));
				}
			}
			/** 群聊，且为自己的房间时，退出显示头像 **/
			Intent serviceintent = new Intent(MultiChatRoomActivity.this,
					FxService.class);
			stopService(serviceintent);
			mShareFileUtils.setBoolean(Constant.ISFLOAT, false);
			
			/** 发送加入话题请求 **/
			sendjoingroup(mShareFileUtils.getString(Constant.CLIENT_ID, "")
					,toChatUsername, ChatRoomBean.getInstance().getIsowner());
			pLog.i("test", "toChatUsername:"+toChatUsername);
			easemobchatImp.getInstance().joingroup(toChatUsername);
//			conversation = EMChatManager.getInstance().getConversation(
//					toChatUsername);
			conversation = EMChatManager.getInstance()
					.getConversationByType(toChatUsername,EMConversationType.GroupChat);
			 // 把此会话的未读数置为0
	        conversation.markAllMessagesAsRead();

	        // 初始化db时，每个conversation加载数目是getChatOptions().getNumberOfMessagesLoaded
	        // 这个数目如果比用户期望进入会话界面时显示的个数不一样，就多加载一些
	        final List<EMMessage> msgs = conversation.getAllMessages();
	        pLog.i("test", "msgs:"+msgs.size());
	        
	        int msgCount = msgs != null ? msgs.size() : 0;
	        
	        pLog.i("test", "msgCount:"+msgCount);
	        pLog.i("test", "conversation.getAllMsgCount():"+conversation.getAllMsgCount());
//	        if (msgCount < conversation.getAllMsgCount() && msgCount < pageindex) {
	            String msgId = null;
	            if (msgs != null && msgs.size() > 0) {
	                msgId = msgs.get(0).getMsgId();
	                pLog.i("test", "msgs.get(0):"+msgs.get(0));
	            }
//	            List<EMMessage> messages = conversation.loadMoreGroupMsgFromDB(msgId, pageindex);
//	            pLog.i("test", "messages.size():"+messages.size());
//	            for (int i = 0; i <  messages.size(); i++) {
//					EMMessage message = messages.get(i);
//					pLog.i("test", "message:"+message.toString());
//					TextMessageBody body = (TextMessageBody) message.getBody();
//					String content = body.getMessage();
//					
//					
//					String time = DateUtils.getTimestampString(new Date(message
//							.getMsgTime()));
//
//					ChatMsgEntityBean entity = new ChatMsgEntityBean();
//					entity.setMessage(content);
//					entity.setDate(time);
//					try {
//						entity.setImage(message
//								.getStringAttribute(Constant.IMAGEURL));
//						entity.setUserId(message
//								.getStringAttribute(Constant.USERID));
//					} catch (EaseMobException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					if (message.direct == EMMessage.Direct.SEND) {
//						entity.setMsgType(Constant.SELF);  //1代表自己、0代表他人
//					} else {
//						entity.setMsgType(Constant.OTHER);
//					}
//					multichatmsgList.add(entity);
//				}
	            for (int i = 0; i <  msgs.size(); i++) {
					EMMessage message = msgs.get(i);
					pLog.i("test", "message:"+message.toString());
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
								.getFrom());
					} catch (EaseMobException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (message.direct == EMMessage.Direct.SEND) {
						entity.setMsgType(Constant.SELF);  //1代表自己、0代表他人
					} else {
						entity.setMsgType(Constant.OTHER);
					}
					multichatmsgList.add(entity);
				}
	        
	        
			//获取此会话的所有消息
//			List<EMMessage> messages = conversation.getAllMessages();
//			List<EMMessage> messages = conversation.loadMoreGroupMsgFromDB("1", 10);
//			pLog.i("test", "群聊conversation:"+conversation);
//			pLog.i("test", "conversation.getMsgCount():"+conversation.getMsgCount());
//			for (int i = 0; i <  conversation.getUnreadMsgCount(); i++) {
//				EMMessage message = conversation.getMessage(i);
//				pLog.i("test", "message:"+message.toString());
//				TextMessageBody body = (TextMessageBody) message.getBody();
//				String content = body.getMessage();
//				
//				
//				String time = DateUtils.getTimestampString(new Date(message
//						.getMsgTime()));
//
//				ChatMsgEntityBean entity = new ChatMsgEntityBean();
//				entity.setMessage(content);
//				entity.setDate(time);
//				try {
//					entity.setImage(message
//							.getStringAttribute(Constant.IMAGEURL));
//					entity.setUserId(message
//							.getStringAttribute(Constant.USERID));
//				} catch (EaseMobException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				if (message.direct == EMMessage.Direct.SEND) {
//					entity.setMsgType(Constant.SELF);  //1代表自己、0代表他人
//				} else {
//					entity.setMsgType(Constant.OTHER);
//				}
//				multichatmsgList.add(entity);
//			}
			if(multichatadapter == null){
				multichatadapter = new ChatMsgViewAdapter(this, multichatmsgList);
				pageViewaList.lv_chat.setAdapter(multichatadapter);				
			}
			pageViewaList.lv_chat
					.setSelection(pageViewaList.lv_chat.getCount() - 1);
			refresh();
//	       }
			/** 未读消息数清零 **/
//			conversation.resetUnreadMsgCount();

		

	}

	@Override
	protected View loadTopLayout() {
		// TODO Auto-generated method stub
		// return getLayoutInflater().inflate(R.layout.base_toplayout_title,
		// null);
		return null;
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

	@SuppressWarnings("static-access")
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			 if (!ChatRoomBean.getInstance().getIsowner()) {
			 sendleavegroup(mShareFileUtils.getString(Constant.CLIENT_ID, "")
					 , ChatRoomBean.getInstance()
			 .getTopicBean().getTopic_id(),ChatRoomBean.getInstance().getIsowner());
			 easemobchatImp.getInstance().exitgroup(toChatUsername);
			 pLog.i("test","环信退出成功");
			 backPage();
			 } else {
			startfloatView();
			 }
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

	@SuppressWarnings({ "static-access", "static-access" })
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
//		super.onClick(v);
		switch (v.getId()) {
		case R.id.theme_chat:
			showtheme(num);
			num++;
			break;
		case R.id.tv_share:
			ShareConfig shareConfig = new ShareConfig(
					MultiChatRoomActivity.this);
//			shareConfig.shareWxFriend("同行话题分享", "",
//					pageViewaList.tv_tagname.getText()
//							.toString().trim(), "", "");
			
			shareConfig.shareCircleFriend("", "",
					"同行话题#"+pageViewaList.tv_tagname.getText()
							.toString().trim()+"#", "", "");
			break;

		case R.id.im_downview:
			titlePopup.show(v);
			break;
		case R.id.host_image:
			if (isNetworkAvailable) {
				/**
				 * 当从悬浮头像进入时，或者为自己的房间时，点击头像显示自己的信息
				 */
				if(mShareFileUtils.getBoolean(Constant.FLOAT,false)
						||ChatRoomBean.getInstance().getIsowner()){
					UserBean u = new UserBean();
					ArrayList<String> labels = JsonDocHelper.toJSONArrary(
							mShareFileUtils.getString(Constant.LABELS, ""), String.class);
					u.setBirth(mShareFileUtils.getString(Constant.BIRTH, ""));
					u.setCity(mShareFileUtils.getString(Constant.CITY, ""));
					u.setClient_id(mShareFileUtils.getString(Constant.CLIENT_ID, ""));
					u.setCreated_at(mShareFileUtils.getString(Constant.CREATED_AT, ""));
					u.setEmail(mShareFileUtils.getString(Constant.EMAIL, ""));
					u.setImage(mShareFileUtils.getString(Constant.IMAGE, ""));
					u.setLabels(labels);
					u.setSex(mShareFileUtils.getString(Constant.SEX, ""));
					u.setUsername(mShareFileUtils.getString(Constant.USERNAME, ""));
					PersonpageBean.getInstance().setUser(u);
					Intent intent = new Intent(MultiChatRoomActivity.this,PersonalPageActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent();
					intent.putExtra("client_id", ChatRoomBean.getInstance().getTopicBean().getUser_id());
					startActivityForLeft(OtherPageActivity.class, intent, false);
				}
			} else {
				showToast("网络未连接", Toast.LENGTH_SHORT, false);
			}

			break;
		case R.id.btn_send:
			sendMessage();
			break;
		case R.id.ll_back:
				 if (!ChatRoomBean.getInstance().getIsowner()) {
					 sendleavegroup(mShareFileUtils.getString(Constant.CLIENT_ID, "")
							 , ChatRoomBean.getInstance()
					 .getTopicBean().getTopic_id(),ChatRoomBean.getInstance().getIsowner());
					 easemobchatImp.getInstance().exitgroup(toChatUsername);
					 pLog.i("test","环信退出成功");
					 backPage();
					 pLog.i("test", "不是房主");
//					 onBackPressed();
					 } else {
						 pLog.i("test", "是房主");
					startfloatView();
					 }
//			onBackPressed();
		default:
			break;
		}

	}

	/**
	 * 悬浮头像设置
	 * 
	 */
	public void startfloatView() {
		
		if (!mShareFileUtils.getBoolean(Constant.ISFLOAT, false)) {
				Intent intentfloat = new Intent(MultiChatRoomActivity.this,
						FxService.class);
//				intentfloat.putExtra(Constant.F_THEME
//						, mShareFileUtils.getString(Constant.F_THEME,""));
//				intentfloat.putExtra(Constant.F_TAGNAME
//						, mShareFileUtils.getString(Constant.F_TAGNAME, ""));
//				intentfloat.putExtra(Constant.F_ROOMID
//						, mShareFileUtils.getString(Constant.F_ROOMID,""));
//				intentfloat.putExtra(Constant.F_TOPICID
//						, mShareFileUtils.getString(Constant.F_TOPICID, ""));
				intentfloat.putExtra(Constant.FROMFLOAT, "fromfloat");
				startService(intentfloat);
				mShareFileUtils.setBoolean(Constant.ISFLOAT, true);
			}
			finish();
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
						sendleavegroup(mShareFileUtils.getString(Constant.CLIENT_ID, "")
								, ChatRoomBean.getInstance()
								.getTopicBean().getTopic_id()
								,ChatRoomBean.getInstance().getIsowner());
						finish();
					} else if (item.mTitle.equals(getResources().getString(
							R.string.lookformember))) {
						Intent intent = new Intent(MultiChatRoomActivity.this,
								ChatRoomListnikeActivity.class);
						intent.putExtra("groupId", ChatRoomBean.getInstance()
								.getTopicBean().getTopic_id());
						intent.putExtra("client_id", mShareFileUtils.getString(Constant.CLIENT_ID, ""));
						startActivity(intent);
					}
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
//			String userid = mShareFileUtils.getString(Constant.CLIENT_ID, "");
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy年MM月dd日   HH:mm:ss     ");
			Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
			String str = formatter.format(curDate);
			ChatMsgEntityBean chat = new ChatMsgEntityBean();
			chat.setDate(str);
			chat.setImage(imagurl);
//			chat.setUserId(userid);
			chat.setMessage(content);
			chat.setMsgType(Constant.SELF);
			
			// 环信发送消息，携带消息内容，自己头像，自己Id
			easemobchatImp.getInstance().sendMessage(content,
									Constant.MULTICHAT, toChatUsername, imagurl);
			multichatmsgList.add(chat);
			refresh();
			pageViewaList.lv_chat
					.setSelection(pageViewaList.lv_chat.getCount() - 1);
			pageViewaList.et_sendmessage.setText("");
		} else {
			showToast(getResources().getString(R.string.network_wait),
					Toast.LENGTH_SHORT, false);
			easemobchatImp.getInstance().login(
					mShareFileUtils.getString(Constant.CLIENT_ID, "")
					.replace("-", ""),
					mShareFileUtils.getString(
							Constant.PASSWORD, ""));
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
			pLog.i("test", "Mul message:"+message.toString());
			// 如果是群聊消息，获取到group id
			String image = null;
			String fromuserid = null;
			if(message != null){
				if(message.getChatType() == ChatType.GroupChat){
						try {
							image = message.getStringAttribute(Constant.IMAGEURL);
						} catch (EaseMobException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						fromuserid = message.getFrom();
						// 获取到消息
						TextMessageBody txtBody = (TextMessageBody) message.getBody();
						String msg = txtBody.getMessage();
						
						SimpleDateFormat formatter = new SimpleDateFormat(
								"yyyy年MM月dd日   HH:mm:ss     ");
						Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
						String str = formatter.format(curDate);
						ChatMsgEntityBean chat = new ChatMsgEntityBean();
						
//						pLog.i("test","fromuserid:"+fromuserid);
						
						chat.setUserId(fromuserid);
						chat.setImage(image);
						chat.setDate(str);
						chat.setMessage(msg);
						chat.setMsgType(Constant.OTHER);
						
						multichatmsgList.add(chat);
						pLog.i("test","multichatmsgList:"+multichatmsgList.size());
						if(multichatadapter == null){
							multichatadapter = new ChatMsgViewAdapter(context, multichatmsgList);
							pageViewaList.lv_chat.setAdapter(multichatadapter);	
						}
						multichatadapter.notifyDataSetChanged();
						pageViewaList.lv_chat
						.setSelection(pageViewaList.lv_chat.getCount() - 1);
			}
		}
		}
	}

	/**
	 * 加入话题请求
	 * 
	 * @param client_id
	 * @param topic_id
	 * @exception UnsupportedEncodingException
	 */
	private void sendjoingroup(String client_id, String topic_id , boolean isOwner) {
		// TODO Auto-generated method stub
		final Intent intent = new Intent();
		RequestParams params = null;
		try {
			params = PeerParamsUtils.getJoinParams(MultiChatRoomActivity.this,
					client_id, topic_id,isOwner);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpUtil.post(HttpConfig.JOIN_TOPIC_IN_URL, params,
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub

						hideLoading();
						showToast(getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, responseString,
								throwable);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONArray errorResponse) {
						// TODO Auto-generated method stub
						hideLoading();
						showToast(getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						hideLoading();
						showToast(getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						pLog.i("test", "res:"+response.toString());

						try {
							JSONObject result = response.getJSONObject("success");

							String code = result.getString("code");
							pLog.i("test", "code:"+code);
							if(code.equals("200")){
								JoinTopicBean jointopicbean = JsonDocHelper
										.toJSONObject(
												response.getJSONObject("success")
												.toString(),
												JoinTopicBean.class);
							}else if(code.equals("500")){
								
							}else{
								String message = result.getString("message");
								showToast(message, Toast.LENGTH_SHORT, false);
							}
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
	private void sendleavegroup(String client_id, String topic_id , boolean isOwner) {
		// TODO Auto-generated method stub
		final Intent intent = new Intent();
		RequestParams params = null;
		try {
			params = PeerParamsUtils.getJoinParams(MultiChatRoomActivity.this,
					client_id, topic_id,isOwner);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpUtil.post(HttpConfig.LEAVE_TOPIC_IN_URL, params,
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub

						hideLoading();
						showToast(getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, responseString,
								throwable);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONArray errorResponse) {
						// TODO Auto-generated method stub
						hideLoading();
						showToast(getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						hideLoading();
						showToast(getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
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
							String code = String.valueOf(result.get("code"));
							pLog.i("test", "code:"+code);
							if (code.equals("200")) {
								showToast("话题已退出", Toast.LENGTH_SHORT, false);
							}else if(code.equals("500")){
								
							}else{
								String message = result.getString("message");
								showToast(message, Toast.LENGTH_SHORT, false);
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
	 

	private void senduser(String client_id,String o_client_id) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		final Intent intent = new Intent();
		RequestParams params = null;
		try {
			params = PeerParamsUtils.getUserParams(this, client_id);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpUtil.post(HttpConfig.USER_IN_URL + client_id + ".json?client_id="+o_client_id, params,
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub
						showToast(getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, responseString,
								throwable);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONArray errorResponse) {
						// TODO Auto-generated method stub
						showToast(getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						showToast(getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						pLog.i("test","response:"+response.toString());
						try {
							JSONObject result = response.getJSONObject("success");

							String code = result.getString("code");
							pLog.i("test", "code:"+code);
							if(code.equals("200")){
								LoginBean loginBean = JsonDocHelper.toJSONObject(
										response.getJSONObject("success")
										.toString(), LoginBean.class);
								if(loginBean!=null){
									PersonpageBean.getInstance().setUser(
											loginBean.user);
									Intent intent = new Intent(MultiChatRoomActivity.this,PersonalPageActivity.class);
									startActivity(intent);
								}
							}else if(code.equals("500")){
								
							}else{
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
	*/
	
	
	
	private void refresh() {

		if(multichatadapter != null){
			multichatadapter.notifyDataSetChanged();
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


}
