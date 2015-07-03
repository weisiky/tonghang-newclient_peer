package com.peer.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
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
import com.peer.bean.LoginResultBean;
import com.peer.bean.UserBean;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.utils.pLog;
import com.peer.utils.pViewBox;

/**
 * 登录页面
 * 
 * @author zhangzg
 * 
 */
public class LoginActivity extends pBaseActivity {

	LoginResultBean loginresult = new LoginResultBean();

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

			// if (email.length() > 0 && password.length() > 0) {
			showProgressBar();
			try {
				sendLoginRequest(email, password);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// } else {
			// showToast("请填写用户名与密码", Toast.LENGTH_SHORT, false);
			// }

			break;
		case R.id.tv_register_login:
			startActivityForLeft(RegisterAcountActivity.class, intent, false);
			break;

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
	 * @throws UnsupportedEncodingException
	 */

	private void sendLoginRequest(String email, String password)
			throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		final Intent intent = new Intent();

		List<BasicNameValuePair> baseParams = new ArrayList<BasicNameValuePair>();
		baseParams.add(new BasicNameValuePair("email", email));
		baseParams.add(new BasicNameValuePair("password", password));
		String params = PeerParamsUtils.ParamsToJsonString(baseParams);
		
		 ArrayList<String> tags=new ArrayList<String>();
		 
		 tags.add("销售");
		 tags.add("java");
		 tags.add("美食");
		HttpEntity entity = new StringEntity(params, "utf-8");

		HttpUtil.post(this, Constant.LONIN_IN_URL, entity, "application/json;charset=UTF-8",
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub

						hideLoading();

						pLog.i("test", "onFailure+statusCode:" + statusCode
								+ "headers:" + headers.toString()
								+ "responseString:" + responseString);

						super.onFailure(statusCode, headers, responseString,
								throwable);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONArray errorResponse) {
						// TODO Auto-generated method stub
						hideLoading();
						pLog.i("test", "onFailure+statusCode:" + statusCode
								+ "headers:" + headers.toString()
								+ "errorResponse:" + errorResponse.toString());
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						hideLoading();
						pLog.i("test", "onFailure:statusCode:" + statusCode);
						pLog.i("test", "throwable:" + throwable.toString());
						pLog.i("test", "headers:" + headers.toString());
						pLog.i("test",
								"errorResponse:" + errorResponse.toString());
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONArray response) {
						// TODO Auto-generated method stub
						hideLoading();
						pLog.i("test", "onSuccess+statusCode:" + statusCode
								+ "headers:" + headers.toString() + "response:"
								+ response.toString());
						super.onSuccess(statusCode, headers, response);
						Map result;
						try {
							result = (Map) response.get(0);
							String client_id = (String) result.get("client_id");
							pLog.i("client_id", client_id);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						hideLoading();
						pLog.i("test", "onSuccess:statusCode:" + statusCode);
						pLog.i("test", "headers:" + headers.toString());
						pLog.i("test", "response:" + response.toString());

						try {
							JSONObject result = response
									.getJSONObject("success");
							String pic_server = result.getString("pic_server");
							String sys_time = result.getString("sys_time");
							String code = result.getString("code");
							JSONObject user = result.getJSONObject("user");
							String sex = user.getString("sex");
							String image = user.getString("image");
							ArrayList<String> lab = new ArrayList<String>();
							JSONArray labels = (JSONArray) user.get("labels");
							for (int index = 0; index < labels.length(); index++)
								lab.add(labels.getString(index));

							pLog.i("test", "lab" + lab);
							String city = user.getString("city");
							String username = user.getString("username");
							String client_id = user.getString("client_id");
							String created_at = user.getString("created_at");
							String birth = user.getString("birth");

							UserBean userbean = new UserBean();
							userbean.setBirth(birth);
							userbean.setCity(city);
							userbean.setCreated_at(created_at);
							userbean.setEmail(pageViewaList.et_email_login
									.getText().toString().trim());
							userbean.setImage(image);
							userbean.setLabels(lab);
							userbean.setSex(sex);
							userbean.setUsername(username);

							loginresult.setSys_time(sys_time);
							loginresult.setPic_server(pic_server);
							loginresult.setUserBean(userbean);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						super.onSuccess(statusCode, headers, response);

					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String responseString) {
						// TODO Auto-generated method stub
						hideLoading();
						pLog.i("test", "onSuccess/statusCode:" + statusCode
								+ "headers:" + headers.toString()
								+ "responseString:" + responseString.toString());
						super.onSuccess(statusCode, headers, responseString);

					}

				});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (false) {
				// 监听返回按键
			} else {
				backPage();
			}
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
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
