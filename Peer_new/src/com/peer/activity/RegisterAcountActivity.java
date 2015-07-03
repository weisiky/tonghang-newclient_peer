package com.peer.activity;

import java.util.ArrayList;

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
import com.peer.utils.pLog;
import com.peer.utils.pShareFileUtils;
import com.peer.utils.pViewBox;

/**
 * 
 * ע�������Ҫ��Ϣ��
 */
public class RegisterAcountActivity extends pBaseActivity {
	
	class PageViewList {
		private LinearLayout ll_back;
		private TextView tv_title, registe_remind;
		private EditText et_email_regist, et_password_registe,
				et_repassword_registe, et_nike_registe;
		private Button bt_registe_next;
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
		pageViewaList.tv_title.setText(getResources().getString(
				R.string.register_acount));
		pageViewaList.bt_registe_next.setEnabled(false);
		pageViewaList.et_email_regist.addTextChangedListener(textwatcher);
		pageViewaList.et_nike_registe.addTextChangedListener(textwatcher);
		pageViewaList.et_password_registe.addTextChangedListener(textwatcher);
		pageViewaList.et_repassword_registe.addTextChangedListener(textwatcher);

	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);
		pageViewaList.bt_registe_next.setOnClickListener(this);
	}

	@Override
	protected void processBiz() {
		// TODO Auto-generated method stub

	}

	@Override
	protected View loadTopLayout() {
		// TODO Auto-generated method stub
		return getLayoutInflater().inflate(R.layout.base_toplayout_title, null);
	}

	@Override
	protected View loadContentLayout() {
		// TODO Auto-generated method stub
		return getLayoutInflater().inflate(R.layout.activity_register_acount,
				null);
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
		case R.id.bt_registe_next:
			Registernext();
			break;

		default:
			break;
		}
	}

	private void Registernext() {
		// TODO Auto-generated method stub
		
		  String format ="^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$"; 
		  String email=pageViewaList.et_email_regist.getText().toString().trim();
		  String password=pageViewaList.et_password_registe.getText().toString().trim(); 
		  String repassword=pageViewaList.et_repassword_registe.getText().toString().trim(); 
		  String nikename=pageViewaList.et_nike_registe.getText().toString().trim();
		 
		 if(!email.matches(format)){
		  pageViewaList.registe_remind.setText(getResources().getString(R.string.erroremail)); 
		  return ; 
		  }else if(!password.matches("^[a-zA-Z0-9_]{5,17}$")){
		  pageViewaList.registe_remind.setText(getResources().getString(R.string.errorpswformat)); 
		  return ;
		  }else if(!password.equals(repassword)){
		  pageViewaList.registe_remind.setText(getResources().getString(R.string.notmatchpsw)); 
		  return ; 
		  }else if(pageViewaList.et_nike_registe.length()>10){
		  pageViewaList.registe_remind.setText(getResources().getString(R.string.errornike)); 
		  return ;
		  }else{
			  pShareFileUtils.setString("email", email);
			  pShareFileUtils.setString("password", password);
			  pShareFileUtils.setString("nikename", nikename);
//		  ArrayList<String> baseregister_list=new ArrayList<String>();
//		  
//		  baseregister_list.add(email); 
//		  baseregister_list.add(password);
//		  baseregister_list.add(nikename);
		 
		
		
		Intent intent = new Intent();
		startActivityForLeft(RegisterTagActivity.class, intent, false);
		pageViewaList.registe_remind.setText("");

	}

 }

	/*
	 * TextWatcher�����༭��
	 */
	TextWatcher textwatcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			testnull();
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			testnull();
		}
	};

	/*
	 * �ж�botton�Ƿ�ɵ��
	 */
	private void testnull() {
		// TODO Auto-generated method stub
		String email = pageViewaList.et_email_regist.getText().toString()
				.trim();
		String password = pageViewaList.et_password_registe.getText()
				.toString().trim();
		String repassword = pageViewaList.et_repassword_registe.getText()
				.toString().trim();
		String nikename = pageViewaList.et_nike_registe.getText().toString()
				.trim();
		if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)
				&& !TextUtils.isEmpty(repassword)
				&& !TextUtils.isEmpty(nikename)) {
			pageViewaList.bt_registe_next.setEnabled(true);
		} else {
			pageViewaList.bt_registe_next.setEnabled(false);
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
