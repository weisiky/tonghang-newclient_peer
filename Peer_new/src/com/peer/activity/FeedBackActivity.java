package com.peer.activity;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.json.JSONArray;
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
import com.peer.IMimplements.easemobchatImp;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.bean.LoginBean;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.utils.BussinessUtils;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.pLog;
import com.peer.utils.pShareFileUtils;
import com.peer.utils.pViewBox;

/**
 * 反馈信息activity 
 */
public class FeedBackActivity extends pBaseActivity{
	class PageViewList {
		private LinearLayout ll_back;
		private TextView tv_title;
		private EditText et_feedback_content;
		private Button commite_feedback;
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
		pageViewaList.tv_title.setText(getResources().getString(R.string.feedback));
		pageViewaList.et_feedback_content.addTextChangedListener(watcher);
		pageViewaList.commite_feedback.setEnabled(false);
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);
		pageViewaList.commite_feedback.setOnClickListener(this);
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
		return getLayoutInflater().inflate(R.layout.activity_feedback, null);
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
		case R.id.commite_feedback:
			
			if(isNetworkAvailable){
				try {
					sendfeedback(pageViewaList.et_feedback_content.getText().toString().trim(),
							mShareFileUtils.getString(Constant.CLIENT_ID, ""));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				showToast(getResources().getString(R.string.Broken_network_prompt), Toast.LENGTH_SHORT, false);
			}							
			break;

		default:
			break;
		}
		
	}
	
	
TextWatcher watcher=new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			if(!pageViewaList.et_feedback_content.getText().toString().trim().equals("")){
				pageViewaList.commite_feedback.setEnabled(true);
			}else {
				pageViewaList.commite_feedback.setEnabled(false);
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
			if(!pageViewaList.et_feedback_content.getText().toString().trim().equals("")){
				pageViewaList.commite_feedback.setEnabled(true);
			}else {
				pageViewaList.commite_feedback.setEnabled(false);
			}
		}
	};
	
	
	/**
	 * 发送反馈信息接口
	 * 
	 * @param client_id
	 * @param content
	 * @throws UnsupportedEncodingException
	 */

	private void sendfeedback(String client_id, String content)
			throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		final Intent intent = new Intent();
		HttpEntity entity = null;
		try {
			entity = PeerParamsUtils.getLoginParams(this, client_id, content);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		HttpUtil.post(this, HttpConfig.SYSTEM_FEEDBACK_URL, entity,
				"application/json;charset=utf-8",
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
							JSONObject response) {
						// TODO Auto-generated method stub
						hideLoading();
						
						showToast("已提交，感谢你的好建议！", Toast.LENGTH_SHORT, false);
						
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

}
