package com.peer.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.utils.BussinessUtils;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.MD5;
import com.peer.utils.pLog;
import com.peer.utils.pViewBox;

/**
 * 注册第一步。 填写基本信息 为提交请求
 * 
 */
public class RegisterAcountActivity extends pBaseActivity {

	class PageViewList {
		private LinearLayout ll_back;
		private TextView tv_title, registe_remind, xieyi;
		private EditText et_email_regist, et_password_registe,
				et_repassword_registe, et_nike_registe;
		private Button bt_registe_next;
	}

	private PageViewList pageViewaList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_acount);
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
				R.string.register_acount));
		pageViewaList.bt_registe_next.setEnabled(false);
		pageViewaList.et_email_regist.addTextChangedListener(textwatcher);
		pageViewaList.et_nike_registe.addTextChangedListener(textwatcher);
		pageViewaList.et_password_registe.addTextChangedListener(textwatcher);
		pageViewaList.et_repassword_registe.addTextChangedListener(textwatcher);

		/** 文字加高亮色 **/
		SpannableStringBuilder builder = new SpannableStringBuilder(
				pageViewaList.xieyi.getText().toString());
		ForegroundColorSpan colorspan = new ForegroundColorSpan(getResources()
				.getColor(R.color.backcolornol));
		builder.setSpan(colorspan, 16, 31, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		pageViewaList.xieyi.setText(builder);

	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);
		pageViewaList.bt_registe_next.setOnClickListener(this);
		pageViewaList.xieyi.setOnClickListener(this);
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
		case R.id.bt_registe_next:
			Registernext();
			break;
		case R.id.xieyi:
			Intent xieyi = new Intent(RegisterAcountActivity.this,
					xieyiActivity.class);
			startActivity(xieyi);
			break;

		default:
			break;
		}
	}

	private void Registernext() {
		// TODO Auto-generated method stub

		String format = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
		String email = pageViewaList.et_email_regist.getText().toString()
				.trim();
		String password = pageViewaList.et_password_registe.getText()
				.toString().trim();
		String repassword = pageViewaList.et_repassword_registe.getText()
				.toString().trim();
		String nikename = pageViewaList.et_nike_registe.getText().toString()
				.trim();

		if (!email.matches(format)) {
			pageViewaList.registe_remind.setText(getResources().getString(
					R.string.erroremail));
			return;
		} else if (!password.matches("^[a-zA-Z0-9_]{5,17}$")) {
			pageViewaList.registe_remind.setText(getResources().getString(
					R.string.errorpswformat));
			return;
		} else if (!password.equals(repassword)) {
			pageViewaList.registe_remind.setText(getResources().getString(
					R.string.notmatchpsw));
			return;
		} else if (pageViewaList.et_nike_registe.length() > 10) {
			pageViewaList.registe_remind.setText(getResources().getString(
					R.string.errornike));
			return;
		} else {
			String md5password = BussinessUtils.strMd5(password);
			sendRegister(email, md5password, nikename);
			pageViewaList.registe_remind.setText("");

		}

	}

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
	 * 判断botton是否可以点击
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

	/**
	 * 注册基本信息请求
	 * 
	 * @param email
	 * @param password
	 * @throws Exception
	 */

	@SuppressWarnings("unchecked")
	private void sendRegister(String email, final String password,
			String nikename) {
		// TODO Auto-generated method stub
		final Intent intent = new Intent();

		RequestParams params = null;
		try {
			params = PeerParamsUtils.getRegisterTagParams(this, email,
					password, nikename);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		HttpUtil.post(HttpConfig.REGISTTAG_IN_URL, params,
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub

						showToast(
								getResources().getString(
										R.string.config_registe),
								Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, responseString,
								throwable);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONArray errorResponse) {
						// TODO Auto-generated method stub

						showToast(
								getResources().getString(
										R.string.config_registe),
								Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub

						showToast(
								getResources().getString(
										R.string.config_registe),
								Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub

						LoginBean loginBean;

						pLog.i("test", "response:" + response.toString());
						try {
							JSONObject result = response
									.getJSONObject("success");

							String code = result.getString("code");
							pLog.i("test", "code:" + code);
							if (code.equals("200")) {
								loginBean = JsonDocHelper.toJSONObject(response
										.getJSONObject("success").toString(),
										LoginBean.class);

								if (loginBean != null) {
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

									Intent register_tag = new Intent();
									startActivityForLeft(
											RegisterTagActivity.class,
											register_tag, false);

								}
							} else if (code.equals("500")) {

							} else {
								String message = result.getString("message");
								showToast(message, Toast.LENGTH_SHORT, false);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							showToast("服务器处理异常...", Toast.LENGTH_SHORT, false);
						}

						super.onSuccess(statusCode, headers, response);
					}

				});
	}

}
