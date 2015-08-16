package com.peer.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.peer.R;
import com.peer.adapter.HomepageAdapter;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.base.pBaseApplication;
import com.peer.bean.PersonpageBean;
import com.peer.bean.RecommendUserBean;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.utils.ImageLoaderUtil;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.pIOUitls;
import com.peer.utils.pLog;
import com.peer.utils.pViewBox;

/**
 * 添加好友
 */
public class AddFriendsActivity extends pBaseActivity {

	String user_client_id;

	class PageViewList {
		private LinearLayout ll_back;
		private TextView tv_title, personnike, email;
		private ImageView personhead;
		private EditText add_reson;
		private Button bt_send;
	}

	private PageViewList pageViewaList;
	private String pic_server;

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
				R.string.checkfriends));
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);
		pageViewaList.bt_send.setOnClickListener(this);
	}

	@SuppressWarnings("static-access")
	@Override
	protected void processBiz() {
		// TODO Auto-generated method stub

		Intent t = getIntent();
		user_client_id = t.getStringExtra("user_client_id");
		String image = t.getStringExtra("image");
		String nike = t.getStringExtra("nike");
		String email = t.getStringExtra("email");

		pLog.i("test", "image:" + image);
		pLog.i("test", "nike:" + nike);
		pLog.i("test", "email:" + email);

		pic_server = mShareFileUtils.getString(Constant.PIC_SERVER, "");

		ImageLoaderUtil.getInstance().showHttpImage(pic_server + image,
				pageViewaList.personhead, R.drawable.mini_avatar_shadow);

		pageViewaList.personnike.setText(nike);
		pageViewaList.email.setText(email);

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
		return getLayoutInflater().inflate(R.layout.activity_addfriends, null);
	}

	@Override
	protected View loadBottomLayout() {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("static-access")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.bt_send:

			if (isNetworkAvailable) {
				String reason = pageViewaList.add_reson.getText().toString()
						.trim();
				try {
					sendaddfriend(user_client_id, reason,
							mShareFileUtils.getString(Constant.CLIENT_ID, ""));
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

	@Override
	public void onNetworkOn() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNetWorkOff() {
		// TODO Auto-generated method stub

	}

	/**
	 * 添加好友请求
	 * 
	 * @param invitee_id
	 * @param reason
	 * @param client_id
	 * @throws UnsupportedEncodingException
	 */
	private void sendaddfriend(final String invitee_id, String reason,
			String client_id) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		RequestParams params = null;
		try {
			params = PeerParamsUtils.getaddfriendParams(this, invitee_id,
					reason);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		HttpUtil.post(HttpConfig.FRIEND_ADD_URL + client_id + ".json", params,
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub
						hideLoading();
						pLog.i("test","statusCode:"+statusCode);
						pLog.i("test","headers:"+headers);
						pLog.i("test","responseString:"+responseString);
						pLog.i("test","throwable:"+throwable);
						showToast(getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, responseString,
								throwable);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONArray errorResponse) {
						// TODO Auto-generated method stub
						hideLoading();
						pLog.i("test","statusCode:"+statusCode);
						pLog.i("test","headers:"+headers);
						pLog.i("test","errorResponse:"+errorResponse);
						pLog.i("test","throwable:"+throwable);
						showToast(getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						hideLoading();
						pLog.i("test","statusCode:"+statusCode);
						pLog.i("test","headers:"+headers);
						pLog.i("test","errorResponse:"+errorResponse);
						pLog.i("test","throwable:"+throwable);
						showToast(getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						JSONObject result;
						try {
							result = response.getJSONObject("success");
							String code = result.getString("code");
							pLog.i("test", "code:"+code);
							if (code.equals("200")) {
								showToast("请求已送达！", Toast.LENGTH_SHORT, false);
//								PersonpageBean.getInstance().getUser().setHas_invitation(true);
								Intent intent = new Intent();
								intent.putExtra("client_id", invitee_id);
								startActivityForLeft(OtherPageActivity.class, intent, false);
							}else if(code.equals("500")){
								
							}else{
								String message = result.getString("message");
								showToast(message, Toast.LENGTH_SHORT, false);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						super.onSuccess(statusCode, headers, response);
					}

				});
	}
}
