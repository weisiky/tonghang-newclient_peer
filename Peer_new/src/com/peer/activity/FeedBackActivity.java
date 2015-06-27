package com.peer.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.peer.base.pBaseActivity;
import com.peer.utils.pViewBox;

public class FeedBackActivity extends pBaseActivity{
	class PageViewList {
		private LinearLayout ll_back;
		private TextView tv_title;
		private EditText et_feedback_content;
		private Button commite_feedback;
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
		pageViewaList.tv_title.setText(getResources().getString(R.string.feedback));
		pageViewaList.et_feedback_content.addTextChangedListener(watcher);
		pageViewaList.commite_feedback.setEnabled(false);
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);
		pageViewaList.commite_feedback.setOnClickListener(this);
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
		return getLayoutInflater().inflate(R.layout.activity_feedback, null);
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
		case R.id.commite_feedback:
			//模拟发请求
			showToast("反馈成功！", Toast.LENGTH_LONG, true);
//			if(checkNetworkState()){
//				FeedBackTask task=new FeedBackTask();
//				task.execute(pageViewaList.et_feedback_content.getText().toString().trim());					
//			}else{
//				ShowMessage(getResources().getString(R.string.Broken_network_prompt));
//			}							
			break;

		default:
			break;
		}
		
	}
	
	
TextWatcher watcher=new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			if(!pageViewaList.et_feedback_content.getText().toString().trim().equals("")){
				pageViewaList.commite_feedback.setEnabled(true);
			}else {
				pageViewaList.commite_feedback.setEnabled(false);
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
			if(!pageViewaList.et_feedback_content.getText().toString().trim().equals("")){
				pageViewaList.commite_feedback.setEnabled(true);
			}else {
				pageViewaList.commite_feedback.setEnabled(false);
			}
		}
	};

}
