package com.peer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.peer.base.pBaseActivity;
import com.peer.titlepopwindow.TitlePopup;
import com.peer.utils.AutoWrapRadioGroup;
import com.peer.utils.pViewBox;


/*
 * �û���Ϣ��
 * */
public class PersonalPageActivity extends pBaseActivity{
	
	private AutoWrapRadioGroup tag_container;
	private TitlePopup titlePopup;
	private boolean page = true;
	
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
		pageViewaList.tv_title.setText(getResources().getString(R.string.xieyi));
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
			showToast("ģ�ⷢ����Ϣ", Toast.LENGTH_SHORT, false);
			break;
		case R.id.addfriends:
			Intent addfriends=new Intent(PersonalPageActivity.this,AddFriendsActivity.class);
			startActivity(addfriends);
			showToast("ģ����Ӻ���", Toast.LENGTH_SHORT, false);
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
}
