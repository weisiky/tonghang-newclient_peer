package com.peer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.peer.base.pBaseActivity;
import com.peer.utils.pViewBox;


/*
 * ’“ªÿ√‹¬Î¿‡
 * */
public class FindPasswordActivity extends pBaseActivity{
	class PageViewList {
		private LinearLayout ll_back;
		private TextView tv_title,email_test;
		private EditText et_email_find;
		private Button bt_findpassword;
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
		pageViewaList.tv_title.setText(getResources().getString(R.string.findpassword));
		
		if(TextUtils.isEmpty(pageViewaList.et_email_find.getText().toString())){
			pageViewaList.bt_findpassword.setEnabled(false);
		}else{
			pageViewaList.bt_findpassword.setEnabled(true);
		}
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);
		pageViewaList.bt_findpassword.setOnClickListener(this);
		
		
		pageViewaList.et_email_find.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
					// TODO Auto-generated method stub
					if(TextUtils.isEmpty(pageViewaList.et_email_find.getText().toString())){
						pageViewaList.bt_findpassword.setEnabled(false);
					}else{
						pageViewaList.bt_findpassword.setEnabled(true);
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
					if(TextUtils.isEmpty(pageViewaList.et_email_find.getText().toString())){
						pageViewaList.bt_findpassword.setEnabled(false);
					}else{
						pageViewaList.bt_findpassword.setEnabled(true);
					}
				}
			});
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
		return getLayoutInflater().inflate(R.layout.activity_findpassword, null);
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
		case R.id.bt_findpassword:
			Intent findpassword = new Intent(FindPasswordActivity.this,FindPasswordResultActivity.class);
			startActivity(findpassword);
			
			break;

		default:
			break;
		}
		
	}
}
