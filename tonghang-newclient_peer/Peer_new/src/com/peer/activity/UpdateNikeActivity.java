package com.peer.activity;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
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
import com.peer.bean.LoginBean;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.utils.BussinessUtils;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.ManagerActivity;
import com.peer.utils.pLog;
import com.peer.utils.pViewBox;

/**
 * 更改昵称
 */
public class UpdateNikeActivity extends pBaseActivity {
	class PageViewList {
		private LinearLayout ll_back;
		private TextView tv_title;
		private EditText et_updatenike;
		private Button bt_nikename;

	}

	private PageViewList pageViewaList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_updatenike);
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
				R.string.updatenike));
		Intent t = getIntent();
		pageViewaList.et_updatenike.setText(t.getStringExtra("nikename"));
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);
		pageViewaList.bt_nikename.setOnClickListener(this);
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
		case R.id.bt_nikename:
			if (pageViewaList.et_updatenike.getText().toString().trim()
					.equals("")) {
				showToast(getResources().getString(R.string.inputnike),
						Toast.LENGTH_LONG, false);
			} else {
				sendUpdateNike(
						mShareFileUtils.getString(Constant.CLIENT_ID, ""),
						pageViewaList.et_updatenike.getText().toString());

			}
			break;

		default:
			break;
		}

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

	/**
	 * 更改用户生日信息请求
	 * 
	 * @param client_id
	 * @param birth
	 * @throws Exception
	 **/

	private void sendUpdateNike(String client_id, String username) {
		// TODO Auto-generated method stub
		final Intent intent = new Intent();

		RequestParams params = null;
		try {
			params = PeerParamsUtils.getUpdateUsernameParams(
					UpdateNikeActivity.this, client_id, username);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		HttpUtil.post(this, HttpConfig.UPDATE_IN_URL + client_id + ".json",
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
								LoginBean loginBean = JsonDocHelper
										.toJSONObject(
												response.getJSONObject(
														"success").toString(),
												LoginBean.class);
								if (loginBean != null) {
									BussinessUtils.saveUserData(loginBean,
											mShareFileUtils);
									showToast(
											getResources().getString(
													R.string.updatemsgsuccess),
											Toast.LENGTH_SHORT, false);
									Intent t = getIntent();
									t.putExtra("newnikename",
											pageViewaList.et_updatenike
													.getText().toString());
									setResult(
											PersonalMessageActivity.UPDATENIKENAME,
											t);
									finish();
								}
							} else if (code.equals("500")) {

							} else {
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

				});
	}
	
}
