package com.peer.activity;

import org.apache.tools.ant.taskdefs.Sleep;

import android.content.Intent;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.peer.base.pBaseActivity;
import com.peer.utils.pViewBox;

/**
 * 登录页面
 * 
 * @author zhangzg
 * 
 */

public class LoginActivity extends pBaseActivity {
	class PageViewList {
		private EditText et_email_login, et_password_login;
		private Button bt_login_login;
		private TextView tv_register_login, tv_forgetpasw_login,
				tv_remind_login;
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
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.bt_login_login:
			startActivityForLeft(MainActivity.class, intent, false);
			break;
		case R.id.tv_register_login:
			startActivityForLeft(RegisterAcountActivity.class, intent, false);
			break;
		default:
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (false) {
				// 处理当前方法里面的返回按键事件
			} else {
				backPage();
			}
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

}
