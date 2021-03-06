package com.peer.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.peer.R;
import com.peer.IMimplements.easemobchatImp;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.utils.BussinessUtils;
import com.peer.utils.ManagerActivity;
import com.peer.utils.pLog;
import com.peer.utils.pViewBox;

/**
 * 找回密码
 * 
 * @author zhangzg
 * 
 */
public class FindPasswordActivity extends pBaseActivity {
	class PageViewList {
		private LinearLayout ll_back;
		private TextView tv_title, email_test;
		private EditText et_email_find;
		private Button bt_findpassword;
	}

	private PageViewList pageViewaList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_findpassword);
		findViewById();
		setListener();
		processBiz();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		pageViewaList = new PageViewList();
		pViewBox.viewBox(this, pageViewaList);
		pageViewaList.tv_title.setText(getResources().getString(
				R.string.findpassword));

		if (TextUtils.isEmpty(pageViewaList.et_email_find.getText().toString())) {
			pageViewaList.bt_findpassword.setEnabled(false);
		} else {
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
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(pageViewaList.et_email_find.getText()
						.toString())) {
					pageViewaList.bt_findpassword.setEnabled(false);
				} else {
					pageViewaList.bt_findpassword.setEnabled(true);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(pageViewaList.et_email_find.getText()
						.toString())) {
					pageViewaList.bt_findpassword.setEnabled(false);
				} else {
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
	public void onClick(View v) {
		// TODO Auto-generated method stub

		super.onClick(v);
		switch (v.getId()) {
		case R.id.bt_findpassword:
			String format = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
			String email = pageViewaList.et_email_find.getText().toString()
					.trim();
			if (isNetworkAvailable) {
				if (!email.matches(format)) {
					showToast(getResources().getString(R.string.erroremail),
							Toast.LENGTH_SHORT, false);
					return;
				} else {
					sendfindpasswd(email);
				}
			} else {

			}
			break;

		default:
			break;
		}

	}

	/**
	 * 请求找回密码接口
	 * 
	 * @param email
	 * 
	 */
	private void sendfindpasswd(String email) {
		// TODO Auto-generated method stub
		// HttpEntity entity = null;
		// try {
		// entity = PeerParamsUtils.getFindPassWordParams(this, email);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		RequestParams params = null;
		try {
			params = PeerParamsUtils.getFindPassWordParams(this, email);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpUtil.post(this, HttpConfig.FORGET_IN_URL, params,
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub

						showToast(
								getResources().getString(R.string.config_error),
								Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, responseString,
								throwable);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONArray errorResponse) {
						// TODO Auto-generated method stub
						showToast(
								getResources().getString(R.string.config_error),
								Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						showToast(
								getResources().getString(R.string.config_error),
								Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						try {
							JSONObject result = response
									.getJSONObject("success");

							String code = result.getString("code");
							if (code.equals("200")) {
								String message = result.getString("message");
								showToast(message, Toast.LENGTH_SHORT, false);
								Intent intent = new Intent();
								startActivityForLeft(
										FindPasswordResultActivity.class,
										intent, false);
							} else if (code.equals("500")) {

							} else {
								String message = result.getString("message");
								showToast(message, Toast.LENGTH_SHORT, false);
							}

						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						super.onSuccess(statusCode, headers, response);
					}

				});
	}

	@Override
	public void onNetworkOn() {
		// TODO Auto-generated method stub
//		sendSystemConfig();

	}

	@Override
	public void onNetWorkOff() {
		// TODO Auto-generated method stub

	}

}
