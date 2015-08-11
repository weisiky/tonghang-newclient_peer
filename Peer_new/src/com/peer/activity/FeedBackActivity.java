package com.peer.activity;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
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
import com.loopj.android.http.RequestParams;
import com.peer.R;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.utils.pLog;
import com.peer.utils.pViewBox;

/**
 * 反馈信息activity
 */
public class FeedBackActivity extends pBaseActivity {
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
		pageViewaList.tv_title.setText(getResources().getString(
				R.string.feedback));
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

			if (isNetworkAvailable) {
				pLog.i("test", "CLIENT_ID:"+mShareFileUtils.getString(Constant.CLIENT_ID, ""));
				try {
					
					sendfeedback(mShareFileUtils.getString(Constant.CLIENT_ID, ""),pageViewaList.et_feedback_content.getText()
							.toString().trim());
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

		default:
			break;
		}

	}

	TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			if (!pageViewaList.et_feedback_content.getText().toString().trim()
					.equals("")) {
				pageViewaList.commite_feedback.setEnabled(true);
			} else {
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
			if (!pageViewaList.et_feedback_content.getText().toString().trim()
					.equals("")) {
				pageViewaList.commite_feedback.setEnabled(true);
			} else {
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
		// HttpEntity entity = null;
		// try {
		// entity = PeerParamsUtils.getLoginParams(this, client_id, content);
		// } catch (Exception e2) {
		// // TODO Auto-generated catch block
		// e2.printStackTrace();
		// }

		RequestParams params = null;
		try {
			params = PeerParamsUtils.getFeedParams(FeedBackActivity.this, client_id, content);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpUtil.post(this, HttpConfig.SYSTEM_FEEDBACK_URL, params,
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
							JSONObject result = response.getJSONObject("success");

							String code = result.getString("code");
							if(code.equals("200")){
								showToast(getResources().getString(R.string.commit)
										, Toast.LENGTH_SHORT, false);
								finish();
							}else if(code.equals("500")){
								
							}else{
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
	public void onNetworkOn() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNetWorkOff() {
		// TODO Auto-generated method stub

	}

}
