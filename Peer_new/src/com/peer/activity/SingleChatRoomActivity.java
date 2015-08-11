package com.peer.activity;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.peer.bean.singlechatmsgListBean;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.service.FxService;
import com.peer.titlepopwindow.ActionItem;
import com.peer.titlepopwindow.TitlePopup;
import com.peer.titlepopwindow.TitlePopup.OnItemOnClickListener;
import com.peer.utils.BussinessUtils;
import com.peer.utils.ImageLoaderUtil;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.pIOUitls;
import com.peer.utils.pLog;
import com.peer.utils.pViewBox;
import com.umeng.analytics.MobclickAgent;

/**
 * 一对一聊天室
 * 进入单聊就两种方式：1.通过个人主页点击发信进入。2、通过“来信页面”，点击进入。
 */
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
	private int num = 1; // num用作判断用户是否点击theme_chat 0-no;1-yes


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
		pageViewaList.btn_send.setOnClickListener(this);
		pageViewaList.ll_back.setOnClickListener(this);
		initChatListener();
	}

	@Override
	protected void processBiz() {
		// TODO Auto-generated method stub
			/** 单聊页面,主要业务，获得聊天记录 **/
			Intent intent = getIntent();
			userbean = (UserBean) intent.getSerializableExtra("userbean");
			toChatUsername = userbean.getClient_id();
			
			pageViewaList.host_imfor.setVisibility(View.GONE);
			pageViewaList.tv_tagname.setText(userbean.getUsername());
			titlePopup.addAction(new ActionItem(this, getResources().getString(
					R.string.deletemes), R.color.white));
			if(isNetworkAvailable){
				/** 获取到与聊天人的会话对象。 **/
				
				conversation = EMChatManager.getInstance().getConversation(
						toChatUsername);
				for (int i = 0; i <  conversation.getMsgCount(); i++) {
					EMMessage message = conversation.getMessage(i);
					
					TextMessageBody body = (TextMessageBody) message.getBody();
					String content = body.getMessage();
					
					
					String time = DateUtils.getTimestampString(new Date(message
							.getMsgTime()));

					ChatMsgEntityBean entity = new ChatMsgEntityBean();
					entity.setMessage(content);
					entity.setDate(time);
					entity.setUserbean(userbean);
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
						entity.setMsgType(Constant.SELF);  //1代表自己、0代表他人
					} else {
						entity.setMsgType(Constant.OTHER);
					}
					singlechatmsgList.add(entity);
//					 把此会话的未读数置为0
					conversation.resetUnreadMsgCount();
				}
			}else{
				/** 获取本地的聊天信息 **/
				String his_message = pIOUitls.readFileByLines(Constant.C_FILE_CACHE_PATH,
						"meg_"
								+mShareFileUtils.getString(Constant.CLIENT_ID, "")
								+toChatUsername);
				pLog.i("test","his_message:"+ his_message);
				if(his_message!=null){
					
					singlechatmsgListBean sListBean=new singlechatmsgListBean();
					
					try {
						sListBean = JsonDocHelper.toJSONObject(his_message, singlechatmsgListBean.class);
						singlechatmsgList=sListBean.getSinglechatmsgList();
						pLog.i("test","singlechatmsgList:"+ singlechatmsgList.size());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						pLog.i("test","Exception:"+ e.toString());
						e.printStackTrace();
					}
					
				}
			}
			
			
			if(singlechatadapter == null){
				singlechatadapter = new ChatMsgViewAdapter(this, singlechatmsgList);
				pageViewaList.lv_chat.setAdapter(singlechatadapter);				
			}
			pageViewaList.lv_chat
					.setSelection(pageViewaList.lv_chat.getCount() - 1);
			refresh();
			

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
//		return getLayoutInflater().inflate(R.layout.base_btn_send, null);
		return null;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if(isNetworkAvailable){
				conversation = EMChatManager.getInstance().getConversation(
						toChatUsername);
				conversation.resetUnreadMsgCount();
			}
			String count;
			try {
				singlechatmsgListBean sListBean=new singlechatmsgListBean();
				sListBean.setSinglechatmsgList(singlechatmsgList);
				count = JsonDocHelper.toJSONString(sListBean);
				pIOUitls.saveStrToSD(Constant.C_FILE_CACHE_PATH,
						"meg_"
								+mShareFileUtils.getString(Constant.CLIENT_ID, "")
								+toChatUsername,false,count);
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
		case R.id.ll_back:
					backPage();
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
			String userid = mShareFileUtils.getString(Constant.CLIENT_ID, "");
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy年MM月dd日   HH:mm:ss     ");
			Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
			String str = formatter.format(curDate);
			ChatMsgEntityBean chat = new ChatMsgEntityBean();
			chat.setDate(str);
			chat.setImage(imagurl);
			chat.setUserId(userid);
			chat.setMessage(content);
			chat.setMsgType(Constant.SELF);
			
			// 环信发送消息，携带消息内容，自己头像，自己Id
					easemobchatImp.getInstance().sendMessage(content,
									Constant.SINGLECHAT, toChatUsername, imagurl, userid);
					singlechatmsgList.add(chat);
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
			// 如果是群聊消息，获取到group id
			String image = null;
			String fromuserid = null;
			String nickname = null;
			if(message.getChatType() == ChatType.Chat){
				pLog.i("test", "单聊监听message.getChatType()："+message.getChatType());
			try {
				image = message.getStringAttribute(Constant.IMAGEURL);
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
			ChatMsgEntityBean chat = new ChatMsgEntityBean();
			
			pLog.i("test","fromuserid:"+fromuserid);
			
			chat.setUserId(fromuserid);
			chat.setImage(image);
			chat.setDate(str);
			chat.setMessage(msg);
			chat.setMsgType(Constant.OTHER);
			chat.setUserbean(userbean);
			singlechatmsgList.add(chat);
			singlechatadapter.notifyDataSetChanged();
			pageViewaList.lv_chat
					.setSelection(pageViewaList.lv_chat.getCount() - 1);
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

}
