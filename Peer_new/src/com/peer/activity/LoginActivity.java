package com.peer.activity;

import java.io.UnsupportedEncodingException;
import java.util.Set;

import org.apache.http.Header;
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
import com.loopj.android.http.RequestParams;
import com.peer.R;
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
import com.peer.utils.MD5;
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
		setContentView(R.layout.activity_login);
		findViewById();
		setListener();
		processBiz();
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

	private void sendLoginRequest(String email, final String password)
			throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		final Intent intent = new Intent();
		// HttpEntity entity = null;
		// try {
		// entity = PeerParamsUtils.getLoginParams(this, email, password);
		// } catch (Exception e2) {
		// // TODO Auto-generated catch block
		// e2.printStackTrace();
		// }

		pLog.i("test", "password:" + password);

		RequestParams params = null;
		try {
			params = PeerParamsUtils.getLoginParams(this, email, password);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpUtil.post(HttpConfig.LONIN_IN_URL, params,
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						showToast(
								getResources().getString(R.string.config_login),
								Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, responseString,
								throwable);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONArray errorResponse) {
						showToast(
								getResources().getString(R.string.config_login),
								Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						showToast(
								getResources().getString(R.string.config_login),
								Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@SuppressWarnings("static-access")
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						try {
							JSONObject result = response
									.getJSONObject("success");

							String code = result.getString("code");
							pLog.i("test", "code:" + code);
							if (code.equals("200")) {

								LoginBean loginBean = JsonDocHelper
										.toJSONObject(
												response.getJSONObject(
														"success").toString(),
												LoginBean.class);
								pLog.i("test",
										"getBirth:"
												+ loginBean.user.getLabels());
								if (loginBean != null
										&& loginBean.user.getBirth() != null
										&& loginBean.user.getLabels().size() != 0) {
									String md5password = BussinessUtils
											.strMd5(password);
									mShareFileUtils.setString(
											Constant.PASSWORD, md5password);

									BussinessUtils.saveUserData(loginBean,
											mShareFileUtils);

									// 注册极光
									JPushInterface.setAlias(getApplication(),
											loginBean.user.getClient_id(),
											new TagAliasCallback() {
												@Override
												public void gotResult(int code,
														String arg1,
														Set<String> arg2) {
													// TODO Auto-generated
													// method
													// stub
													System.out.println("code"
															+ code);
													pLog.i("注册极光结果放回", String
															.valueOf(code));
												}
											});

									String client_id = mShareFileUtils
											.getString(Constant.CLIENT_ID, "");
									easemobchatImp.getInstance().login(
											client_id.replace("-", ""),
											mShareFileUtils.getString(
													Constant.PASSWORD, ""));
									easemobchatImp.getInstance()
											.loadConversationsandGroups();

									startActivityForLeft(MainActivity.class,
											intent, false);
								} else if (loginBean.user.getLabels().size() == 0) {
									showToast(
											getResources().getString(
													R.string.completemessage),
											Toast.LENGTH_SHORT, false);
									String md5password = BussinessUtils
											.strMd5(password);
									mShareFileUtils.setString(
											Constant.CLIENT_ID,
											loginBean.user.getClient_id());
									mShareFileUtils.setString(
											Constant.USERNAME,
											loginBean.user.getUsername());
									mShareFileUtils.setString(
											Constant.PASSWORD, md5password);
									Intent intent = new Intent();
									startActivityForLeft(
											RegisterTagActivity.class, intent,
											false);
								} else if (loginBean.user.getBirth() == null) {
									showToast(
											getResources().getString(
													R.string.completemessage),
											Toast.LENGTH_SHORT, false);
									mShareFileUtils.setString(
											Constant.CLIENT_ID,
											loginBean.user.getClient_id());
									mShareFileUtils.setString(
											Constant.USERNAME,
											loginBean.user.getUsername());
									Intent intent = new Intent();
									startActivityForLeft(
											RegisterCompleteActivity.class,
											intent, false);
								}
							} else if (code.equals("500")) {

							} else {
								String message = result.getString("message");
								showToast(message, Toast.LENGTH_SHORT, false);
							}
						} catch (Exception e1) {
							pLog.i("test", "Exception:" + e1.toString());
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

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
