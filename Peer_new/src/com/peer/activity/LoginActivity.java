package com.peer.activity;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.peer.IMimplements.easemobchatImp;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.bean.LoginBean;
import com.peer.bean.LoginResultBean;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.utils.BussinessUtils;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.pLog;
import com.peer.utils.pShareFileUtils;
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
		pageViewaList.et_email_login.addTextChangedListener(textwatcher);
		pageViewaList.et_password_login.addTextChangedListener(textwatcher);
		pageViewaList.bt_login_login.setEnabled(false);

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

			if (isNetworkAvailable) {

				showProgressBar();
				try {
					sendLoginRequest(email, password);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				showToast(
						getResources()
								.getString(R.string.Broken_network_prompt),
						Toast.LENGTH_SHORT, false);
			}

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
		HttpEntity entity = null;
		try {
			entity = PeerParamsUtils.getLoginParams(this, email, password);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		HttpUtil.post(this, HttpConfig.LONIN_IN_URL, entity,
				"application/json;charset=utf-8",
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub

						hideLoading();
						super.onFailure(statusCode, headers, responseString,
								throwable);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONArray errorResponse) {
						// TODO Auto-generated method stub
						hideLoading();
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						hideLoading();
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						hideLoading();
						try {
							LoginBean loginBean = JsonDocHelper.toJSONObject(
									response.getJSONObject("success")
											.toString(), LoginBean.class);
							if (loginBean != null) {

								BussinessUtils.saveUserData(loginBean,
										mShareFileUtils);

								// 注册极光
//								JPushInterface.setAlias(getApplication(),
//										loginBean.user.get
//												.getHuangxin_username(),
//										new TagAliasCallback() {
//											@Override
//											public void gotResult(int code,
//													String arg1,
//													Set<String> arg2) {
//												// TODO Auto-generated method
//												// stub
//												System.out.println("code"
//														+ code);
//												pLog.i("注册极光结果放回",
//														String.valueOf(code));
//											}
//										});

								// easemobchatImp.getInstance().login(pShareFileUtils.getString("client_id",
								// ""), pShareFileUtils.getString("password",
								// ""));
								// easemobchatImp.getInstance().loadConversationsandGroups();
								startActivityForLeft(MainActivity.class,
										intent, false);
							}

						} catch (Exception e1) {
							pLog.i("test", "Exception:" + e1.toString());
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						/*
						 * try { JSONObject result = response
						 * .getJSONObject("success"); String pic_server =
						 * result.getString("pic_server"); String sys_time =
						 * result.getString("sys_time"); String code =
						 * result.getString("code"); JSONObject user =
						 * result.getJSONObject("user"); String sex =
						 * user.getString("sex"); String image =
						 * user.getString("image"); ArrayList<String> lab = new
						 * ArrayList<String>(); JSONArray labels = (JSONArray)
						 * user.get("labels"); for (int index = 0; index <
						 * labels.length(); index++)
						 * lab.add(labels.getString(index)); String city =
						 * user.getString("city"); String username =
						 * user.getString("username"); String client_id =
						 * user.getString("client_id"); String created_at =
						 * user.getString("created_at"); String birth =
						 * user.getString("birth");
						 * 
						 * UserBean userbean = new UserBean();
						 * userbean.setBirth(birth); userbean.setCity(city);
						 * userbean.setCreated_at(created_at);
						 * userbean.setEmail(pageViewaList.et_email_login
						 * .getText().toString().trim());
						 * userbean.setImage(image); userbean.setLabels(lab);
						 * userbean.setSex(sex); userbean.setUsername(username);
						 * 
						 * loginresult.setSys_time(sys_time);
						 * loginresult.setPic_server(pic_server);
						 * loginresult.setUserBean(userbean);
						 * 
						 * } catch (JSONException e) { // TODO Auto-generated
						 * catch block e.printStackTrace(); }
						 */
						super.onSuccess(statusCode, headers, response);

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

	TextWatcher textwatcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			String email = pageViewaList.et_email_login.getText().toString()
					.trim();
			String password = pageViewaList.et_password_login.getText()
					.toString().trim();
			if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
				pageViewaList.bt_login_login.setEnabled(true);
			} else {
				pageViewaList.bt_login_login.setEnabled(false);
			}
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
			String email = pageViewaList.et_email_login.getText().toString()
					.trim();
			String password = pageViewaList.et_password_login.getText()
					.toString().trim();
			if (!email.equals("") && !password.equals("")) {
				pageViewaList.bt_login_login.setEnabled(true);
			} else {
				pageViewaList.bt_login_login.setEnabled(false);
			}
		}
	};

	@Override
	public void onNetworkOn() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNetWorkOff() {
		// TODO Auto-generated method stub

	}

}
