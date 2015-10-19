package com.peer.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.peer.utils.JsonDocHelper;
import com.peer.utils.pLog;
import com.peer.utils.pViewBox;

public class EditCardActivity extends pBaseActivity{
	
	private int mYear;
	private int mMonth;
	private int mDay;
	
	//控制该页面中，谁点击了日期选择控件。
	//true--tv_tagname_5点击。false--tv_tagname_9点击。
	private boolean flag;
	
	private static final int SHOW_DATAPICK = 0;
	private static final int DATE_DIALOG_ID = 1;
	
	//判断页面来源
	private String pd;	
	private String oclient_id;
	private String date;
	
	private boolean update;
	
	class PageViewList {
		private LinearLayout ll_back;
		private TextView tv_title,tv_tagname_6,tv_tagname_10,tv_warn;
		private Button bt_change_card;
		private EditText et_tagname_1,et_tagname_2
				,et_tagname_3,et_tagname_4
				,et_tagname_5,et_tagname_7
				,et_tagname_8,et_tagname_9;
		
	}

	private PageViewList pageViewaList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editcard);
		findViewById();
		setListener();
		processBiz();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		pageViewaList = new PageViewList();
		pViewBox.viewBox(this, pageViewaList);
		pageViewaList.tv_title
				.setText(getResources().getString(R.string.editcard));
		Intent intent = getIntent();
		intent.getExtras();
		pd = intent.getStringExtra("flag");
		if(pd.equals("1")){
			pageViewaList.bt_change_card.setText("提交");
		}else if(pd.equals("2")){
			pageViewaList.bt_change_card.setText("交换");
			oclient_id = intent.getStringExtra("client_id");
		}else{
			pageViewaList.bt_change_card.setText("查看");
			oclient_id = intent.getStringExtra("client_id");
			date = intent.getStringExtra("date");
		}
		
		pageViewaList.tv_tagname_6.setText(mShareFileUtils.getString(
				Constant.CARD_WORKTIME, ""));
		pageViewaList.tv_tagname_10.setText(mShareFileUtils.getString(
				Constant.CARD_ADMISSIONTIME, ""));
		pageViewaList.et_tagname_1.setText(mShareFileUtils.getString(
				Constant.CARD_PHONE, ""));
		pageViewaList.et_tagname_3.setText(mShareFileUtils.getString(
				Constant.CARD_REALNAME, ""));
		pageViewaList.et_tagname_4.setText(mShareFileUtils.getString(
				Constant.CARD_COMPANY, ""));
		pageViewaList.et_tagname_5.setText(mShareFileUtils.getString(
				Constant.CARD_POSITION, ""));
		pageViewaList.et_tagname_7.setText(mShareFileUtils.getString(
				Constant.CARD_SCHOOL, ""));
		pageViewaList.et_tagname_8.setText(mShareFileUtils.getString(
				Constant.CARD_EXPERIENCE, ""));
		pageViewaList.et_tagname_9.setText(mShareFileUtils.getString(
				Constant.CARD_MAJOR, ""));
		if(mShareFileUtils.getString(
				Constant.CARD_EMAIL, "").equals("")){
			pageViewaList.tv_warn.setVisibility(View.VISIBLE);
			pageViewaList.et_tagname_2.setText(mShareFileUtils.getString(
					Constant.EMAIL, ""));
			update = false;
		}else{
			pageViewaList.et_tagname_2.setText(mShareFileUtils.getString(
					Constant.CARD_EMAIL, ""));
			update = true;
		}
		
		
		
		pageViewaList.bt_change_card.setEnabled(false);
		pageViewaList.et_tagname_1.addTextChangedListener(textwatcher);
		pageViewaList.et_tagname_2.addTextChangedListener(textwatcher);
		pageViewaList.et_tagname_3.addTextChangedListener(textwatcher);
		pageViewaList.et_tagname_4.addTextChangedListener(textwatcher);
		pageViewaList.et_tagname_5.addTextChangedListener(textwatcher);
		pageViewaList.tv_tagname_6.addTextChangedListener(textwatcher);
		pageViewaList.et_tagname_7.addTextChangedListener(textwatcher);
		pageViewaList.et_tagname_8.addTextChangedListener(textwatcher);
		pageViewaList.et_tagname_9.addTextChangedListener(textwatcher);
		pageViewaList.tv_tagname_10.addTextChangedListener(textwatcher);
		
		setDateTime();
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);
		pageViewaList.bt_change_card.setOnClickListener(this);
		pageViewaList.tv_tagname_6.setOnClickListener(this);
		pageViewaList.tv_tagname_10.setOnClickListener(this);
	}

	@Override
	protected void processBiz() {
		// TODO Auto-generated method stub

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
		String phone = pageViewaList.et_tagname_1.getText().toString().trim();
		String email = pageViewaList.et_tagname_2.getText().toString().trim();
		String realname = pageViewaList.et_tagname_3.getText().toString().trim();
		String company = pageViewaList.et_tagname_4.getText().toString().trim();
		String position = pageViewaList.et_tagname_5.getText().toString().trim();
		String worktime = pageViewaList.tv_tagname_6.getText().toString().trim();
		String school = pageViewaList.et_tagname_7.getText().toString().trim();
		String experience = pageViewaList.et_tagname_8.getText().toString().trim();
		String major = pageViewaList.et_tagname_9.getText().toString().trim();
		String admissiontime = pageViewaList.tv_tagname_10.getText().toString().trim();
		if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(phone)
				&& !TextUtils.isEmpty(company)
				&& !TextUtils.isEmpty(worktime)
				&& !TextUtils.isEmpty(school)
				&& !TextUtils.isEmpty(admissiontime)
				&& !TextUtils.isEmpty(position)) {
			if(!phone.equals(mShareFileUtils.getString(Constant.CARD_PHONE, ""))||
					!email.equals(mShareFileUtils.getString(Constant.CARD_EMAIL, ""))||
					!company.equals(mShareFileUtils.getString(Constant.CARD_COMPANY, ""))||
					!position.equals(mShareFileUtils.getString(Constant.CARD_POSITION, ""))||
					!worktime.equals(mShareFileUtils.getString(Constant.CARD_WORKTIME, ""))||
					!school.equals(mShareFileUtils.getString(Constant.CARD_SCHOOL, ""))||
					!admissiontime.equals(mShareFileUtils.getString(Constant.CARD_ADMISSIONTIME, ""))||
					!experience.equals(mShareFileUtils.getString(Constant.CARD_EXPERIENCE, ""))||
					!major.equals(mShareFileUtils.getString(Constant.CARD_MAJOR, ""))){
				pageViewaList.bt_change_card.setEnabled(true);
			}else{
				pageViewaList.bt_change_card.setEnabled(false);
			}
		} else {
			pageViewaList.bt_change_card.setEnabled(false);
		}
	}
	


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.bt_change_card:
			ActionCheck(update);
			break;
		case R.id.tv_tagname_6:
				if(isNetworkAvailable){
					flag = true;
					ChangBirthday();
				}else{
					showToast(getResources().getString(R.string.Broken_network_prompt), Toast.LENGTH_SHORT, false);
				}
				break;
		case R.id.tv_tagname_10:
			if(isNetworkAvailable){
				flag = false;
				ChangBirthday();
			}else{
				showToast(getResources().getString(R.string.Broken_network_prompt), Toast.LENGTH_SHORT, false);
			}
			break;
		}

	}
	
	/**
	 * 初始化Calendar
	 */
	private void setDateTime() {
		final Calendar c = Calendar.getInstance();
		mYear = 1993;
		mMonth = 1;
		mDay = 1;
	}
	
	/**
	 * ChangBirthday类
	 */

	private void ChangBirthday() {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.what = SHOW_DATAPICK;
		dateandtimeHandler.sendMessage(msg);
	}
	
	Handler dateandtimeHandler = new Handler() {
		@SuppressWarnings("deprecation")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_DATAPICK:
				showDialog(DATE_DIALOG_ID);
				break;
			}
		}
	};
	
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}
		return null;
	}
	
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {

		switch (id) {
		case DATE_DIALOG_ID:
			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
			break;
		}
	}
	
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDateDisplay();
		}
	};

	private void updateDateDisplay() {

		if(flag){
			pageViewaList.tv_tagname_6.setText(new StringBuilder()
			.append(mYear).append("-")
			.append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1))
			.append("-").append((mDay < 10) ? "0" + mDay : mDay));
		}else{
			pageViewaList.tv_tagname_10.setText(new StringBuilder()
			.append(mYear).append("-")
			.append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1))
			.append("-").append((mDay < 10) ? "0" + mDay : mDay));
		}

	}
	
	

	private void ActionCheck(Boolean update) {
		// TODO Auto-generated method stub
		
		String email_format = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
		String phone_format = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
		
		String phone = pageViewaList.et_tagname_1.getText().toString().trim();
		String email = pageViewaList.et_tagname_2.getText().toString().trim();
		String realname = pageViewaList.et_tagname_3.getText().toString().trim();
		String company = pageViewaList.et_tagname_4.getText().toString().trim();
		String position = pageViewaList.et_tagname_5.getText().toString().trim();
		String worktime = pageViewaList.tv_tagname_6.getText().toString().trim();
		String school = pageViewaList.et_tagname_7.getText().toString().trim();
		String experience = pageViewaList.et_tagname_8.getText().toString().trim();
		String major = pageViewaList.et_tagname_9.getText().toString().trim();
		String admissiontime = pageViewaList.tv_tagname_10.getText().toString().trim();
		
		if (!phone.matches(phone_format)) {
			showToast(getResources().getString(R.string.errornumber)
					, Toast.LENGTH_SHORT, false);
			return;
		} else if (!email.matches(email_format)) {
			showToast(getResources().getString(R.string.erroremail)
					, Toast.LENGTH_SHORT, false);
			return;
		}else if(company.equals("")){
			showToast(getResources().getString(R.string.errorcompany)
					, Toast.LENGTH_SHORT, false);
			return;
		}else if(position.equals("")){
			showToast(getResources().getString(R.string.errorposition)
					, Toast.LENGTH_SHORT, false);
			return;
		}else if(worktime.equals("")){
			showToast(getResources().getString(R.string.errorworktime)
					, Toast.LENGTH_SHORT, false);
			return;
		}else if(school.equals("")){
			showToast(getResources().getString(R.string.errorschool)
					, Toast.LENGTH_SHORT, false);
			return;
		}else if(admissiontime.equals("")){
			showToast(getResources().getString(R.string.erroradmissiontime)
					, Toast.LENGTH_SHORT, false);
			return;
		}else{
				sendEditCard(mShareFileUtils.getString(Constant.CLIENT_ID, ""),
						phone,email,realname,company,position,worktime,school,experience,major,admissiontime,update);
		}
		
	}

	/**
	 * 编辑名片信息请求
	 * 
	 * @param phone
	 * @param email
	 * @param company
	 * @param position
	 * @param worktime
	 * @param school
	 * @param experience
	 * @param major
	 * @param admissiontime
	 * @throws Exception
	 */

	@SuppressWarnings("unchecked")
	private void sendEditCard(String client_id,final String phone,final String email,final String realname,
			final String company,final String position,final String worktime,final String school,
			final String experience,final String major,final String admissiontime,Boolean update) {
		// TODO Auto-generated method stub
		final Intent intent = new Intent();

		RequestParams params = null;
		try {
			params = PeerParamsUtils.getCardParams(this, phone,
					email,realname, company,position,worktime,school,experience,major,admissiontime);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String url;
		if(update){
			url = HttpConfig.Card_IN_URL+client_id+"/update.json";
		}else{
			url = HttpConfig.Card_IN_URL+client_id+"/add.json";
		}

		HttpUtil.post(url, params,
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

						pLog.i("test", "card_response:"+response.toString());
//						LoginBean loginBean;

						try {
							JSONObject result = response
									.getJSONObject("success");

							String code = result.getString("code");
							if (code.equals("200")) {
//								loginBean = JsonDocHelper.toJSONObject(response
//										.getJSONObject("success").toString(),
//										LoginBean.class);
//
//								if (loginBean != null) {
////									String md5password = BussinessUtils
////											.strMd5(password);
//									mShareFileUtils.setString(
//											Constant.CLIENT_ID,
//											loginBean.user.getClient_id());
//									mShareFileUtils.setString(
//											Constant.USERNAME,
//											loginBean.user.getUsername());
//									mShareFileUtils.setString(
//											Constant.PASSWORD, password);
//
//									Intent register_tag = new Intent();
//									startActivityForLeft(
//											RegisterTagActivity.class,
//											register_tag, false);
//
//								}
								
								mShareFileUtils.setString(Constant.CARD_ADMISSIONTIME, admissiontime);
								mShareFileUtils.setString(Constant.CARD_COMPANY, company);
								mShareFileUtils.setString(Constant.CARD_REALNAME, realname);
								mShareFileUtils.setString(Constant.CARD_EMAIL, email);
								mShareFileUtils.setString(Constant.CARD_EXPERIENCE, experience);
								mShareFileUtils.setString(Constant.CARD_MAJOR, major);
								mShareFileUtils.setString(Constant.CARD_PHONE, phone);
								mShareFileUtils.setString(Constant.CARD_POSITION, position);
								mShareFileUtils.setString(Constant.CARD_SCHOOL, school);
								mShareFileUtils.setString(Constant.CARD_WORKTIME, worktime);
								
								if(pd.equals("1")){
									showToast("编辑成功！", Toast.LENGTH_SHORT, false);
									finish();
								}else if(pd.equals("2")){
									sendchangecard(
											mShareFileUtils.getString(Constant.CLIENT_ID, ""),oclient_id);
								}else{
									sendagreecard(
											mShareFileUtils.getString(Constant.CLIENT_ID, ""),oclient_id,date);
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

	@Override
	public void onNetworkOn() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onNetWorkOff() {
		// TODO Auto-generated method stub

	}
	
	
	/**
	 * 交换名片请求
	 * 
	 * @param client_id
	 * @param friend_id
	 * @throws UnsupportedEncodingException
	 */
	private void sendchangecard(String client_id, String friend_id) {
		
		RequestParams params = null;
		try {
			params = PeerParamsUtils.getdeletefriendParams(this, friend_id);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		HttpUtil.post(this, HttpConfig.CHANGE_CARD_URL + client_id +"/request/"+friend_id+ ".json",
				params, new JsonHttpResponseHandler() {

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
								showToast("请求已经发送！", Toast.LENGTH_SHORT,
										false);
								finish();
							} else if (code.equals("500")) {

							} else {
								String message = result.getString("message");
								showToast(message, Toast.LENGTH_SHORT, false);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

	}
	
	
	
	/**
	 * 同意交换名片请求
	 * 
	 * @param client_id
	 * @param friend_id
	 * @throws UnsupportedEncodingException
	 */
	private void sendagreecard(String client_id, final String friend_id,final String date) {
		
		RequestParams params = null;
		try {
			params = PeerParamsUtils.getdeletefriendParams(this, friend_id);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		HttpUtil.post(this, HttpConfig.CHANGE_CARD_URL + client_id +"/agree/"+friend_id+ ".json",
				params, new JsonHttpResponseHandler() {
			
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
						Intent intent = new Intent(EditCardActivity.this, CardActivity.class);
						intent.putExtra("other_id",friend_id);
						intent.putExtra("date", date);
						startActivity(intent);
					} else if (code.equals("500")) {
						
					} else {
						String message = result.getString("message");
						showToast(message, Toast.LENGTH_SHORT, false);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
	}
	
}
