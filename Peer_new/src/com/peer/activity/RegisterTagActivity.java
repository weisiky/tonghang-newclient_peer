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

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.bean.UserBean;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.pLog;
import com.peer.utils.pShareFileUtils;
import com.peer.utils.pViewBox;



/*
 * 注册标签类
 * */
public class RegisterTagActivity extends pBaseActivity{
	class PageViewList {
		private LinearLayout ll_back;
		private TextView tv_title;
		private EditText et_tagname_1,et_tagname_2,et_tagname_3,et_tagname_4,et_tagname_5;
		private TextView tv_remind,xieyi;
		private Button bt_registe_tag;
		
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
		pageViewaList.tv_title.setText(getResources().getString(R.string.register_tag));
		pageViewaList.et_tagname_1.addTextChangedListener(watcher);
		pageViewaList.et_tagname_2.addTextChangedListener(watcher);
		pageViewaList.bt_registe_tag.setEnabled(false);
		
		/*
		 *文字加高亮色
		 * */
		SpannableStringBuilder builder = new SpannableStringBuilder(pageViewaList.xieyi.getText().toString());
		ForegroundColorSpan colorspan = new ForegroundColorSpan(getResources().getColor(R.color.backcolornol));
		builder.setSpan(colorspan, 16, 31, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		pageViewaList.xieyi.setText(builder);

	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);
		pageViewaList.bt_registe_tag.setOnClickListener(this);
		pageViewaList.xieyi.setOnClickListener(this);
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
		return getLayoutInflater().inflate(R.layout.activity_register_tag, null);
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
		case R.id.bt_registe_tag:
			RegisteTag();
			
			break;

		case R.id.xieyi:
			Intent xieyi=new Intent(RegisterTagActivity.this,xieyiActivity.class);
			startActivity(xieyi);
			break;
		default:
			break;
		}
		
	}
	
	/**
	 * 注册标签方法
	 */
	private void RegisteTag() {
		// TODO Auto-generated method stub
		
		String t1=pageViewaList.et_tagname_1.getText().toString().trim();
		String t2=pageViewaList.et_tagname_2.getText().toString().trim();
		String t3=pageViewaList.et_tagname_3.getText().toString().trim();
		String t4=pageViewaList.et_tagname_4.getText().toString().trim();
		String t5=pageViewaList.et_tagname_5.getText().toString().trim();
		String [] arr={t1,t2,t3,t4,t5};
		ArrayList<String> list=new ArrayList<String>();
		
		//局部变量 
		boolean sameTag=true;
		boolean Tolong=true;
		boolean isbreak=true;
		for(int i=0;i<arr.length;i++){
			if(!arr[i].equals("")){
				list.add(arr[i]);									
			}			
		}
		if(!t1.equals("")&&!t2.equals("")){
			String fomate="^[A-Za-z]+$";
			for(int j=0;j<list.size();j++){	
				if(list.get(j).matches(fomate)){
					if(list.get(j).length()<13){
						Tolong=false;
					}else{
						Tolong=true;
						pageViewaList.tv_remind.setText(getResources().getString(R.string.skillname));
						break;
					}	
				}else{
					if(list.get(j).length()<7){
						Tolong=false;
					}else{
						Tolong=true;
						pageViewaList.tv_remind.setText(getResources().getString(R.string.skillname));
						break;
					}
				}
							
			}
			for(int j=0;j<list.size();j++){
				for(int k=j+1;k<list.size();k++){
					if(!list.get(j).equals(list.get(k))){
						sameTag=false;					
					}else{
						sameTag=true;
						isbreak=false;
						pageViewaList.tv_remind.setText(getResources().getString(R.string.repetskill));
						break;
					}	
				}
				if(!isbreak){
					break;
				}
			}
			
			if(!sameTag&&!Tolong){	
				pageViewaList.tv_remind.setText("");
				showProgressBar();
				try {
					sendRegisterTagRequest(pShareFileUtils.getString("email",""), 
							pShareFileUtils.getString("password",""),
							pShareFileUtils.getString("nikename",""),
							list);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Intent register_tag=new Intent();
				startActivityForLeft(RegisterCompleteActivity.class, register_tag, false);
			}
		}else{
			pageViewaList.tv_remind.setText(getResources().getString(R.string.skillnull));
			return;
		}
		
		
	}
	
	
	/**
	 * 请求登录接口
	 * 
	 * @param email
	 * @param password
	 * @throws Exception 
	 */

	private void sendRegisterTagRequest(String email, String password ,String nikename , List labels)
			throws Exception {
		// TODO Auto-generated method stub
		final Intent intent = new Intent();
		pLog.i("sendMsg", "email:" + email);
		pLog.i("sendMsg", "password:" + password);
		pLog.i("sendMsg", "nikename:" + nikename);
		pLog.i("sendMsg", "labels:" + labels);
		List<BasicNameValuePair> baseParams=new ArrayList<BasicNameValuePair>();
		baseParams.add(new BasicNameValuePair("email", email));
		baseParams.add(new BasicNameValuePair("password", password));
		baseParams.add(new BasicNameValuePair("labels", JsonDocHelper.toJSONString(labels)));
		String params=PeerParamsUtils.ParamsToJsonString(baseParams);
		
		HttpEntity entity = new StringEntity(params, "utf-8");
		 
        
		HttpUtil.post(this,Constant.REGISTTAG_IN_URL, entity, "application/json",
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
						UserBean user = new UserBean();
						try {
							Map result = (Map) response.get("user");
							String birth = (String) result.get("birth");
							user.setBirth(birth);
							pLog.i("birth:", birth);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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
	}


	/**
	 * TextWatcher监听编辑框
	 */
TextWatcher watcher=new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			String t1=pageViewaList.et_tagname_1.getText().toString().trim();
			String t2=pageViewaList.et_tagname_2.getText().toString().trim();
			if(!TextUtils.isEmpty(t1)&&!TextUtils.isEmpty(t2)){
				pageViewaList.bt_registe_tag.setEnabled(true);
			}else{
				pageViewaList.bt_registe_tag.setEnabled(false);
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
			String t1=pageViewaList.et_tagname_1.getText().toString().trim();
			String t2=pageViewaList.et_tagname_2.getText().toString().trim();
			if(!TextUtils.isEmpty(t1)&&!TextUtils.isEmpty(t2)){
				pageViewaList.bt_registe_tag.setEnabled(true);
			}else{
				pageViewaList.bt_registe_tag.setEnabled(false);
			}
		}
	};
}
