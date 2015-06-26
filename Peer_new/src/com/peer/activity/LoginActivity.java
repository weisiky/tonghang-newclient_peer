package com.peer.activity;

import org.apache.tools.ant.taskdefs.Sleep;

import android.content.Intent;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.peer.base.pBaseActivity;
import com.peer.utils.pViewBox;

public class LoginActivity extends pBaseActivity{
	class PageViewList {
		private EditText et_email_login,et_password_login;
		private Button bt_login_login;
		private TextView tv_register_login,tv_forgetpasw_login,tv_remind_login;
		private RelativeLayout baseProgressBarLayout;
		
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

	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.bt_login_login.setOnClickListener(this);
		pageViewaList.tv_register_login.setOnClickListener(this);
	}

	@Override
	protected void processBiz() {
		// TODO Auto-generated method stub

	}

	@Override
	protected View loadTopLayout() {
		// TODO Auto-generated method stub
		// return getLayoutInflater().inflate(R.layout.top_layout, null);
		return null;
	}

	@Override
	protected View loadContentLayout() {
		// TODO Auto-generated method stub
		return getLayoutInflater().inflate(R.layout.activity_login, null);
	}
	
	@Override
	protected View loadBottomLayout() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_login_login:		
			Intent Login = new Intent(LoginActivity.this,MainActivity.class);
			startActivity(Login);			
			break;
		case R.id.tv_register_login:		
				Intent register = new Intent(LoginActivity.this,RegisterAcountActivity.class);
				startActivity(register);			
			break;
		default:
			break;
		}
		
	}

}
