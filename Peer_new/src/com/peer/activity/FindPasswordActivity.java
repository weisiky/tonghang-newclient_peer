package com.peer.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
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

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.utils.pLog;
import com.peer.utils.pViewBox;


/*
 * ’“ªÿ√‹¬Î¿‡
 * */
public class FindPasswordActivity extends pBaseActivity{
	class PageViewList {
		private LinearLayout ll_back;
		private TextView tv_title,email_test;
		private EditText et_email_find;
		private Button bt_findpassword;
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
		pageViewaList.tv_title.setText(getResources().getString(R.string.findpassword));
		
		if(TextUtils.isEmpty(pageViewaList.et_email_find.getText().toString())){
			pageViewaList.bt_findpassword.setEnabled(false);
		}else{
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
				public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
					// TODO Auto-generated method stub
					if(TextUtils.isEmpty(pageViewaList.et_email_find.getText().toString())){
						pageViewaList.bt_findpassword.setEnabled(false);
					}else{
						pageViewaList.bt_findpassword.setEnabled(true);
					}
				}
				
				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
						int arg3) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterTextChanged(Editable arg0) {
					// TODO Auto-generated method stub
					if(TextUtils.isEmpty(pageViewaList.et_email_find.getText().toString())){
						pageViewaList.bt_findpassword.setEnabled(false);
					}else{
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
	protected View loadTopLayout() {
		// TODO Auto-generated method stub
		// return getLayoutInflater().inflate(R.layout.top_layout, null);
		return getLayoutInflater().inflate(R.layout.base_toplayout_title, null);
	}

	@Override
	protected View loadContentLayout() {
		// TODO Auto-generated method stub
		return getLayoutInflater().inflate(R.layout.activity_findpassword, null);
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
		case R.id.bt_findpassword:
			String email = pageViewaList.et_email_find.getText().toString().trim();
//			commitfindpasswd(email);
			break;

		default:
			break;
		}
		
	}

	/**
	 *Ã·Ωª’“ªÿ√‹¬Î…Í«Î 
	 * @param email
	 * 
	 */
	private void commitfindpasswd(String email) throws UnsupportedEncodingException{
		// TODO Auto-generated method stub
		showProgressBar();
		
		List<BasicNameValuePair> baseParams=new ArrayList<BasicNameValuePair>();
		baseParams.add(new BasicNameValuePair("email", email));
		String params=PeerParamsUtils.ParamsToJsonString(baseParams);
		
		HttpEntity entity = new StringEntity(params, "utf-8");
		HttpUtil.post(this,Constant.FORGET_IN_URL, entity,"application/json",
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
						pLog.i("test", "errorResponse:" + errorResponse.toString());
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
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						hideLoading();
						pLog.i("test", "onSuccess:statusCode:" + statusCode
								+ "headers:" + headers.toString() + "response:"
								+ response.toString());
						super.onSuccess(statusCode, headers, response);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String responseString) {
						// TODO Auto-generated method stub
						hideLoading();
						pLog.i("test", "onSuccess:statusCode:" + statusCode
								+ "headers:" + headers.toString()
								+ "responseString:" + responseString.toString());
						super.onSuccess(statusCode, headers, responseString);
					}

				});
		Intent intent = new Intent();
		startActivityForLeft(FindPasswordResultActivity.class, intent, false);
		
	}
	
}
