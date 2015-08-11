package com.peer.activity;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.peer.R;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.bean.LoginBean;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.utils.BussinessUtils;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.pLog;
import com.peer.utils.pViewBox;

/*
 * 更改密码（发请求）
 * */
public class UpdatePasswordActivity extends pBaseActivity {

	private TextWatcher textwatcher;
	private PageViewList pageViewaList;

	class PageViewList {
		private LinearLayout ll_back;
		private TextView tv_title, updatepasw_remind;
		private EditText et_oldpasw, et_newpasw, et_repasw;
		private Button bt_changesubmite;
	}

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
				R.string.updatepassword));
		pageViewaList.bt_changesubmite.setEnabled(false);
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);
		CreateTextwatcher();
		pageViewaList.et_oldpasw.addTextChangedListener(textwatcher);
		pageViewaList.et_newpasw.addTextChangedListener(textwatcher);
		pageViewaList.et_repasw.addTextChangedListener(textwatcher);

		pageViewaList.bt_changesubmite.setOnClickListener(this);
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
		return getLayoutInflater().inflate(R.layout.activity_changpassword,
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
		case R.id.bt_changesubmite:
			UpdatePassword();

			break;

		default:
			break;
		}

	}

	private void UpdatePassword() {
		// TODO Auto-generated method stub
		String old = pageViewaList.et_oldpasw.getText().toString().trim();
		final String newpasws = pageViewaList.et_newpasw.getText().toString()
				.trim();
		String testnew = pageViewaList.et_repasw.getText().toString().trim();
		if (!newpasws.matches("^[a-zA-Z0-9_]{5,17}$")) {
			pageViewaList.updatepasw_remind.setText(getResources().getString(
					R.string.errorpswformat));
			return;
		} else if (!newpasws.equals(testnew)) {
			pageViewaList.updatepasw_remind.setText(getResources().getString(
					R.string.oldnewnot));
			return;
		} else {
			if (isNetworkAvailable) {
				sendUpdatePassword(
						mShareFileUtils.getString(Constant.CLIENT_ID, ""), old,
						newpasws);
			} else {
				showToast(
						getResources()
								.getString(R.string.Broken_network_prompt),
						Toast.LENGTH_SHORT, false);
			}
		}
	}

	/**
	 * 更改用户密码请求
	 * 
	 * @param client_id
	 * @param oldpasswd
	 * @throws newpasswd
	 **/

	private void sendUpdatePassword(String client_id, String oldpasswd,
			String newpasswd) {
		// TODO Auto-generated method stub
		final Intent intent = new Intent();
		// HttpEntity entity = null;
		// try {
		// entity = PeerParamsUtils.getUpdatepasswdParams(
		// UpdatePasswordActivity.this, oldpasswd , newpasswd);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		RequestParams params = null;
		try {
			params = PeerParamsUtils.getUpdatepasswdParams(
					UpdatePasswordActivity.this, oldpasswd, newpasswd);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		HttpUtil.post(this, HttpConfig.UPDATE_PWD_IN_URL + client_id + ".json",
				params, new JsonHttpResponseHandler() {

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
							JSONObject result = response.getJSONObject("success");

							String code = result.getString("code");
							pLog.i("test", "code:"+code);
							if(code.equals("200")){
								LoginBean loginBean = JsonDocHelper.toJSONObject(
										response.getJSONObject("success")
										.toString(), LoginBean.class);
								if (loginBean != null) {
									
									BussinessUtils.saveUserData(loginBean,
											mShareFileUtils);
									showToast("修改成功！", Toast.LENGTH_SHORT, false);
									startActivityForLeft(MyAcountActivity.class,
											intent, false);
								}
							}else if(code.equals("500")){
								
							}else{
								String message = result.getString("message");
								showToast(message, Toast.LENGTH_SHORT, false);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						super.onSuccess(statusCode, headers, response);
					}

//					@Override
//					public void onSuccess(int statusCode, Header[] headers,
//							String responseString) {
//						// TODO Auto-generated method stub
//						hideLoading();
//						super.onSuccess(statusCode, headers, responseString);
//						Intent login_complete = new Intent();
//						startActivityForLeft(MainActivity.class,
//								login_complete, false);
//					}

				});
	}

	private void CreateTextwatcher() {
		// TODO Auto-generated method stub
		textwatcher = new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				TestNull();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				TestNull();
			}
		};

	}

	private void TestNull() {
		// TODO Auto-generated method stub
		String old = pageViewaList.et_oldpasw.getText().toString();
		String newpasws = pageViewaList.et_newpasw.getText().toString();
		String testnew = pageViewaList.et_repasw.getText().toString();
		if (!old.equals("") && !newpasws.equals("") && !testnew.equals("")) {
			pageViewaList.bt_changesubmite.setEnabled(true);
		} else {
			pageViewaList.bt_changesubmite.setEnabled(false);
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
