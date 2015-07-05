package com.peer.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.peer.IMimplements.easemobchatImp;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.bean.ChatMsgEntityBean;
import com.peer.bean.ChatRoomBean;
import com.peer.bean.User;
import com.peer.titlepopwindow.ActionItem;
import com.peer.titlepopwindow.TitlePopup;
import com.peer.titlepopwindow.TitlePopup.OnItemOnClickListener;
import com.peer.utils.pViewBox;
import com.umeng.analytics.MobclickAgent;


/**
 *聊天室 
 */
public class ChatRoomActivity extends pBaseActivity{
	
	
	private TitlePopup titlePopup;
	private boolean page = true;
	private InputMethodManager manager;
	private String mPageName="ChatRoom";
	private List<ChatMsgEntityBean> msgList=new ArrayList<ChatMsgEntityBean>();
	private String toChatUsername;
	String theme = null;
	private EMConversation conversation;
//	private ChatMsgViewAdapter adapter;
	
	ScrollView mScrollView;
	
	class PageViewList {
		private LinearLayout ll_back;
		private TextView tv_title,tv_share,tv_nikename,theme_chat;
		private RelativeLayout host_imfor,rl_bottom;
		private ImageView host_image,im_downview;
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
		pageViewaList.tv_title.setText(getResources().getString(R.string.xieyi));
		pageViewaList.im_downview.setVisibility(View.VISIBLE);
		titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,page);
//		popupwindow();
		
		mScrollView = (ScrollView) findViewById(R.id.scrollContent);
		mScrollView.setVerticalScrollBarEnabled(false);  
		mScrollView.setHorizontalScrollBarEnabled(false);
		
		hideKeyboard();//软键盘
		pageViewaList.btn_send.setEnabled(false);
		pageViewaList.btn_send.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				if(TextUtils.isEmpty(pageViewaList.et_sendmessage.getText().toString().trim())){
					pageViewaList.btn_send.setEnabled(false);
				}else{
					pageViewaList.btn_send.setEnabled(true);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				if(TextUtils.isEmpty(pageViewaList.et_sendmessage.getText().toString().trim())){
					pageViewaList.btn_send.setEnabled(false);
				}else{
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
		
		if(ChatRoomBean.getInstance().getChatroomtype()==Constant.MULTICHAT){			
			toChatUsername=ChatRoomBean.getInstance().getTopic().getHuangxin_group_id();
			pageViewaList.host_imfor.setVisibility(View.VISIBLE);
			if(!ChatRoomBean.getInstance().isIsowner()){
				titlePopup.addAction(new ActionItem(this, getResources().getString(R.string.lookformember), R.color.white));			
			}else{
				titlePopup.addAction(new ActionItem(this, getResources().getString(R.string.exitroom), R.color.white));
				titlePopup.addAction(new ActionItem(this, getResources().getString(R.string.lookformember), R.color.white));				
			}
			Intent intent=getIntent();
			if(intent.getStringExtra(Constant.FROMFLOAT)!=null&&intent.getStringExtra(Constant.FROMFLOAT).equals(Constant.FROMFLOAT)){
//				pageViewaList.tv_tagname.setText(intent.getStringExtra(Constant.TAGNAME));
				
				pageViewaList.tv_nikename.setText(intent.getStringExtra(Constant.OWNERNIKE));
//				LoadImageUtil.imageLoader.displayImage(intent.getStringExtra(Constant.IMAGE), ownerimg,LoadImageUtil.options);	
				pageViewaList.theme_chat.setText(intent.getStringExtra(Constant.THEME));
//				topicId=intent.getStringExtra(Constant.TOPICID);				
			}else{
//				pageViewaList.tv_tagname.setText(ChatRoomBean.getInstance().getTopic().getLabel_name());
				User u=ChatRoomBean.getInstance().getTopic().getUser();
				pageViewaList.tv_nikename.setText(u.getUsername());
//				LoadImageUtil.imageLoader.displayImage(u.getImage(), ownerimg,LoadImageUtil.options);
				theme = ChatRoomBean.getInstance().getTopic().getSubject();
				pageViewaList.theme_chat.setText(theme);
				pageViewaList.theme_chat.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
//				topicId=toChatUsername;
			}
			
//			Intent serviceintent = new Intent(ChatRoomActivity.this, FxService.class);
//			stopService(serviceintent);			
//			LocalStorage.saveBoolean(ChatRoomActivity.this, Constant.ISFLOAT, false);
			
			easemobchatImp.getInstance().joingroup(toChatUsername);
			
//			ReplieTask task=new ReplieTask();
//			task.execute(ChatRoomTypeUtil.getInstance().getTopic().getTopicid());
			conversation = EMChatManager.getInstance().getConversation(toChatUsername);
			conversation.resetUnreadMsgCount();		
		}else if(ChatRoomBean.getInstance().getChatroomtype()==Constant.SINGLECHAT){
			toChatUsername=ChatRoomBean.getInstance().getUser().getClient_Id();
			pageViewaList.host_imfor.setVisibility(View.GONE);
//			pageViewaList.tv_tagname.setText(ChatRoomBean.getInstance().getUser().getUsername());
			titlePopup.addAction(new ActionItem(this, getResources().getString(R.string.deletemes), R.color.white));
			conversation = EMChatManager.getInstance().getConversation(toChatUsername);
			for(int i=0;i<conversation.getMsgCount();i++){
				EMMessage message =conversation.getMessage(i);				
				
				TextMessageBody body=(TextMessageBody) message.getBody();
				String content=body.getMessage();			
				String   time =DateUtils.getTimestampString(new Date(message.getMsgTime())) ; 
					
				ChatMsgEntityBean entity=new ChatMsgEntityBean();
				entity.setMessage(content);
				entity.setDate(time);
				try {
					entity.setImage(message.getStringAttribute(Constant.IMAGEURL));
					entity.setUserId(message.getStringAttribute(Constant.USERID));
				} catch (EaseMobException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(message.direct==EMMessage.Direct.SEND){
					entity.setMsgType(Constant.SELF);
				}else{
					entity.setMsgType(Constant.OTHER);
				}			
				msgList.add(entity);
			}	
//			adapter=new ChatMsgViewAdapter(this, msgList);
//			pageViewaList.lv_chat.setAdapter(adapter);
			pageViewaList.lv_chat.setSelection(pageViewaList.lv_chat.getCount() - 1);	
		
			conversation.resetUnreadMsgCount();				
		}

	}

	@Override
	protected View loadTopLayout() {
		// TODO Auto-generated method stub
		// return getLayoutInflater().inflate(R.layout.top_layout, null);
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.theme_chat:		
//			showtheme(num);
//			num++;
			showToast("模拟测试", Toast.LENGTH_SHORT, false);
			break;
		case R.id.tv_share:
			showToast("模拟测试", Toast.LENGTH_SHORT, false);			
			break;

		case R.id.im_downview:
			titlePopup.show(v);
			break;
		case R.id.host_image:
//			FriendsTask task=new FriendsTask();
//			task.execute();
			showToast("模拟测试", Toast.LENGTH_SHORT, false);
			break;
		case R.id.btn_send:
//			if(ChatRoomTypeUtil.getInstance().getChatroomtype()==Constant.MULTICHAT){
//				reply();
//			}else{
//				sendMessage();
//			}		
			showToast("模拟测试", Toast.LENGTH_SHORT, false);
			break;
		default:
			break;
		}
		
	}
	
	
	/**
	 * 输入法软盘
	 */
	private void hideKeyboard() {
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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
	 *下拉框
	 * 
	private void popupwindow() {
		// TODO Auto-generated method stub		
		
		titlePopup.setItemOnClickListener(new OnItemOnClickListener() {			
			@Override
			public void onItemClick(ActionItem item, int position) {
				// TODO Auto-generated method stub
				if(!checkNetworkState()){
					ShowMessage(getResources().getString(R.string.Broken_network_prompt));
				}else{
					if(item.mTitle.equals(getResources().getString(R.string.exitroom))){
						easemobchatImp.getInstance().exitgroup(toChatUsername);
						finish();
					}else if(item.mTitle.equals(getResources().getString(R.string.deletemes))){
						easemobchatImp.getInstance().clearConversation(toChatUsername);
						msgList.clear();
						adapter.notifyDataSetChanged();
					}else if(item.mTitle.equals(getResources().getString(R.string.lookformember))){
						Intent intent=new Intent(ChatRoomActivity.this,ChatRoomListnikeActivity.class);
						intent.putExtra("topicId", topicId);
						intent.putExtra("groupId", ChatRoomTypeUtil.getInstance().getTopic().getHuangxin_group_id());
						startActivity(intent);			
					}
				}				
					
			}
		});
	}*/
}
