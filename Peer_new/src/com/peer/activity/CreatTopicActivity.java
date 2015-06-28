package com.peer.activity;

import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.peer.base.pBaseActivity;
import com.peer.utils.AutoWrapRadioGroup;
import com.peer.utils.pViewBox;

/*
 * 创建话题类
 * 
 * */
public class CreatTopicActivity extends pBaseActivity{
	
	private AutoWrapRadioGroup tag_container;
	private TextWatcher watcher;
	private List<String> list;
	private boolean isselect=false;
	
	class PageViewList {
		private LinearLayout ll_back,ll_inputtopic;
		private TextView tv_title,tv_newfriends;
		private RelativeLayout rl_mytopic;
		private EditText et_topic;
		private Button bt_creattopic;
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
		pageViewaList.tv_title.setText(getResources().getString(R.string.createtopic));
		
		tag_container = (AutoWrapRadioGroup) findViewById(R.id.tag_container);
		
		pageViewaList.bt_creattopic.setEnabled(false);
		
		pageViewaList.et_topic.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				if(isselect&&TextUtils.isEmpty(pageViewaList.et_topic.getText().toString().trim())){
					pageViewaList.bt_creattopic.setEnabled(false);
				}else{
					pageViewaList.bt_creattopic.setEnabled(true);
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
				if(isselect&&!TextUtils.isEmpty(pageViewaList.et_topic.getText().toString().trim())){
					pageViewaList.bt_creattopic.setEnabled(true);
				}else{
					pageViewaList.bt_creattopic.setEnabled(false);
				}				
			}
		});
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);
		pageViewaList.rl_mytopic.setOnClickListener(this);
		pageViewaList.bt_creattopic.setOnClickListener(this);
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
		return getLayoutInflater().inflate(R.layout.activity_creattopic, null);
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
		case R.id.bt_creattopic:
//			if(checkNetworkState()){
				ShareDialog();				
//			}else{
//				ShowMessage(getResources().getString(R.string.Broken_network_prompt));
//			}
			break;
		case R.id.rl_mytopic:
			Intent mytopic=new Intent(CreatTopicActivity.this,TopicActivity.class);
			startActivity(mytopic);		
			break;
		default:
			break;
		}
		
	}
	
	/*
	 * 创建话题时，是否分享
	 * */
	public void ShareDialog(){
		new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.sharedailog))  
		.setMessage(getResources().getString(R.string.isshare)) 
		.setNegativeButton(getResources().getString(R.string.notshare), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialoginterface, int i) {
				// TODO Auto-generated method stub
//				CreatTopicTask task=new CreatTopicTask();
//				task.execute(selectlabel,topic.getText().toString().trim());
				showToast("创建话题成功", Toast.LENGTH_SHORT, false);
			}
		}) 
		 .setPositiveButton(getResources().getString(R.string.sharesure), new DialogInterface.OnClickListener(){
             public void onClick(DialogInterface dialoginterface, int i){            	 
              
           showToast("分享暂时不提供", Toast.LENGTH_SHORT, false);
      		
             }
		 })
		 .show();  
	}
	
	
}
