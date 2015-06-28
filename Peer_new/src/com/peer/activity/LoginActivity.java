package com.peer.activity;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.utils.pLog;
import com.peer.utils.pViewBox;



/*
 * 登入页类
 * */
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
		pageViewaList.tv_forgetpasw_login.setOnClickListener(this);
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
			String email = pageViewaList.et_email_login.getText().toString()
					.trim();
			String password = pageViewaList.et_password_login.getText()
					.toString().trim();

			if (email.length() > 0 && password.length() > 0) {
				showProgressBar();
				sendLoginRequest(email, password);
			} else {
				showToast("请输入用户名与密码", Toast.LENGTH_SHORT, false);
			}
		case R.id.tv_register_login:
			startActivityForLeft(RegisterAcountActivity.class, intent, false);
			
		case R.id.tv_forgetpasw_login:
			startActivityForLeft(FindPasswordActivity.class, intent, false);
			break;
		default:
			break;
		}
		
	}

	/**
	 * 请求登录接口
	 * 
	 * @param email
	 * @param password
	 */

	private void sendLoginRequest(String email, String password) {
		// TODO Auto-generated method stub
		final Intent intent = new Intent();
		RequestParams params = PeerParamsUtils.getLoginParams(
				LoginActivity.this, email, password);
		HttpUtil.get(Constant.LONIN_IN_URL, params,
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub

						hideLoading();
						pLog.i("test", "onFailure+statusCode:" + statusCode + "headers:"
								+ headers.toString() + "responseString:"
								+ responseString);

						super.onFailure(statusCode, headers, responseString,
								throwable);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONArray errorResponse) {
						// TODO Auto-generated method stub
						hideLoading();
						pLog.i("test", "onFailure+statusCode:" + statusCode + "headers:"
								+ headers.toString() + "errorResponse:"
								+ errorResponse.toString());
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						hideLoading();
						pLog.i("test", "onFailure:statusCode:" + statusCode);
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONArray response) {
						// TODO Auto-generated method stub
						hideLoading();
						pLog.i("test", "onSuccess+statusCode:" + statusCode + "headers:"
								+ headers.toString() + "response:"
								+ response.toString());
						super.onSuccess(statusCode, headers, response);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						hideLoading();
						pLog.i("test", "onSuccess:statusCode:" + statusCode + "headers:"
								+ headers.toString() + "response:"
								+ response.toString());
						super.onSuccess(statusCode, headers, response);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String responseString) {
						// TODO Auto-generated method stub
						hideLoading();
						pLog.i("test", "onSuccess:statusCode:" + statusCode + "headers:"
								+ headers.toString() + "responseString:"
								+ responseString.toString());
						super.onSuccess(statusCode, headers, responseString);
					}

				});
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
