package com.peer.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.bean.PersonpageBean;
import com.peer.bean.SearchBean;
import com.peer.bean.UserBean;
import com.peer.titlepopwindow.TitlePopup;
import com.peer.utils.AutoWrapRadioGroup;
import com.peer.utils.ManagerActivity;
import com.peer.utils.pViewBox;


/**
 * 用户详细信息类
 * 包括‘我的’、‘他的’、‘她的’
 * 
 */
public class PersonalPageActivity extends pBaseActivity{
	
	private AutoWrapRadioGroup tag_container;
	private TitlePopup titlePopup;
	private boolean page = true;
	private RadioButton skill;
	
	class PageViewList {
		private LinearLayout ll_back,ll_personpagebottom,contentauto;
		private TextView tv_title,personnike,personcount,sex,city,tv_topic;
		private Button send,addfriends;
		private ImageView personhead;
		private RelativeLayout rl_topic;
	}

	private PageViewList pageViewaList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		pageViewaList = new PageViewList();
		pViewBox.viewBox(this, pageViewaList);
		
		
		tag_container = (AutoWrapRadioGroup) findViewById(R.id.tag_container);
		
		tag_container.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				RadioButton tempButton = (RadioButton)findViewById(checkedId);
				String selectlabel=tempButton.getText().toString();
				SearchBean.getInstance().setSearchname(selectlabel);
				SearchBean.getInstance().setSearchtype(Constant.USERBYLABEL);
				Intent intent=new Intent(PersonalPageActivity.this, SearchResultActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);
		pageViewaList.rl_topic.setOnClickListener(this);
		pageViewaList.send.setOnClickListener(this);
		pageViewaList.addfriends.setOnClickListener(this);
		
	}

	@Override
	protected void processBiz() {
		// TODO Auto-generated method stub
		/*UserBean userbean=PersonpageBean.getInstance().getUser();
		pageViewaList.personnike.setText(userbean.getUsername());
		pageViewaList.personcount.setText(userbean.getEmail());
		pageViewaList.city.setText(userbean.getCity());
		pageViewaList.sex.setText(userbean.getSex());
		for(int i=0;i<userbean.getLabels().size();i++){
			String tag=userbean.getLabels().get(i);					

			skill=(RadioButton) getLayoutInflater().inflate(R.layout.skill, tag_container, false);
			skill.setHeight((int)getResources().getDimension(R.dimen.hight));
			skill.setTextSize(20);
			skill.setTextColor(getResources().getColor(R.color.white));
			int pading=(int)getResources().getDimension(R.dimen.pading);
			skill.setText(tag);
			skill.setTag(""+i);
			tag_container.addView(skill);
		}*/
		ViewType();

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
		return getLayoutInflater().inflate(R.layout.activity_personalpage, null);
	}
	
	@Override
	protected View loadBottomLayout() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.rl_topic:
//			if(checkNetworkState()){
				Intent intent=new Intent(PersonalPageActivity.this,TopicActivity.class);
//				intent.putExtra("userId", userpage.getUserid());
//				intent.putExtra("image", userpage.getImage());
//				intent.putExtra("nike", userpage.getUsername());
//				intent.putExtra("email", userpage.getEmail());			
				startActivity(intent);
//			}else{
//				ShowMessage(getResources().getString(R.string.Broken_network_prompt));
//			}						
			break;
		case R.id.send:
			showToast("模拟发送", Toast.LENGTH_SHORT, false);
			break;
		case R.id.addfriends:
			Intent addfriends=new Intent(PersonalPageActivity.this,AddFriendsActivity.class);
			startActivity(addfriends);
			showToast("模拟加好友", Toast.LENGTH_SHORT, false);
			break;
		default:
			break;
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
	
	
	private void ViewType() {
		// TODO Auto-generated method stub			
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.weight=1;
		params.gravity=Gravity.CENTER_VERTICAL;
		String userclient_id=mShareFileUtils.getString(Constant.CLIENT_ID, "");
		
		
		if(userclient_id.equals(PersonpageBean.getInstance().getUser().getClient_id())){
			MypersonPage();
//		}else if(PersonpageBean.getInstance().getUser().isIs_friends()){
//			friendsPersonPage();
		}else{
			unfriendsPersonPage();
		}
						
	}
	
	
	/**
	 * 当前用户的个人信息展示页 
	 */
	public void MypersonPage(){
		UserBean userbean=PersonpageBean.getInstance().getUser();
		pageViewaList.tv_title.setText(getResources().getString(R.string.personalpage_own));
		pageViewaList.tv_topic.setText(getResources().getString(R.string.topic_owen));
		pageViewaList.ll_personpagebottom.setVisibility(View.INVISIBLE);
		if(isNetworkAvailable){
			pageViewaList.personnike.setText(userbean.getUsername());
			pageViewaList.personcount.setText(userbean.getEmail());
			pageViewaList.city.setText(userbean.getCity());
			pageViewaList.sex.setText(userbean.getSex());
			ArrayList<String> lables = userbean.getLabels();
			for(int i=0;i<lables.size();i++){
				String tag=lables.get(i);		
				skill=(RadioButton) getLayoutInflater().inflate(R.layout.skill, tag_container, false);
				skill.setHeight((int)getResources().getDimension(R.dimen.hight));
				skill.setTextSize(20);
				skill.setTextColor(getResources().getColor(R.color.white));
				int pading=(int)getResources().getDimension(R.dimen.pading);
				skill.setText(tag);
				skill.setTag(""+i);
				tag_container.addView(skill);
			}
		}
	}
	
	
	/**
	 * 
	 * 与当前用户非好友的用户展示 
	 *
	 */
	private void unfriendsPersonPage(){
		final UserBean userbean=PersonpageBean.getInstance().getUser();
		if(userbean.getSex().toString().equals("男")){
			pageViewaList.tv_topic.setText(getResources().getString(R.string.topic_other));
			pageViewaList.tv_title.setText(getResources().getString(R.string.personalpage_other));
		}else{
			pageViewaList.tv_topic.setText(getResources().getString(R.string.topic_nvother));
			pageViewaList.tv_title.setText(getResources().getString(R.string.personalpage_nvother));
		}
		
		pageViewaList.personnike.setText(userbean.getUsername());
		pageViewaList.personcount.setText(userbean.getEmail());
		pageViewaList.city.setText(userbean.getCity());
		pageViewaList.sex.setText(userbean.getSex());
		for(int i=0;i<userbean.getLabels().size();i++){
			String tag=userbean.getLabels().get(i);					

			skill=(RadioButton) getLayoutInflater().inflate(R.layout.skill, tag_container, false);
			skill.setHeight((int)getResources().getDimension(R.dimen.hight));
			skill.setTextSize(20);
			skill.setTextColor(getResources().getColor(R.color.white));
			int pading=(int)getResources().getDimension(R.dimen.pading);
			skill.setText(tag);
			skill.setTag(""+i);
			tag_container.addView(skill);
		}
		
		pageViewaList.send.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
/*				ChatRoomTypeUtil.getInstance().setUserId(PersonpageUtil.getInstance().getUser().getUserid());
				ChatRoomTypeUtil.getInstance().setHuanxingId(PersonpageUtil.getInstance().getUser().getHuangxin_username());
				ChatRoomTypeUtil.getInstance().setTitle(PersonpageUtil.getInstance().getUser().getUsername());
				
				ChatRoomTypeUtil.getInstance().setChatroomtype(Constant.SINGLECHAT);				
				ChatRoomTypeUtil.getInstance().setUser(userpage);				
				Intent intent=new Intent(PersonalPageActivity.this,ChatRoomActivity.class);
				startActivity(intent);
				ManagerActivity.getAppManager().finishActivity();*/
			}
		});
		pageViewaList.addfriends.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub				
				Intent intent=new Intent(PersonalPageActivity.this,AddFriendsActivity.class);
				intent.putExtra("user_client_id", userbean.getClient_id());
				intent.putExtra("image", userbean.getImage());
				intent.putExtra("nike", userbean.getUsername());
				intent.putExtra("email", userbean.getEmail());
				startActivity(intent);
				ManagerActivity.getAppManager().finishActivity();
			}
		});
	}
}
