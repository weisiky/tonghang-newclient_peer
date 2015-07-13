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
import com.peer.adapter.HomepageAdapter;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.bean.RecommendUserBean;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.pIOUitls;
import com.peer.utils.pLog;
import com.peer.utils.pViewBox;


/**
 * 添加好友
 */
public class AddFriendsActivity extends pBaseActivity{
	
	String user_client_id;
	
	class PageViewList {
		private LinearLayout ll_back;
		private TextView tv_title,personnike,email;
		private ImageView personhead;
		private EditText add_reson;
		private Button bt_send;
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
		pageViewaList.tv_title.setText(getResources().getString(R.string.checkfriends));
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);
		pageViewaList.bt_send.setOnClickListener(this);
	}

	@Override
	protected void processBiz() {
		// TODO Auto-generated method stub
		
		Intent t = new Intent();
		user_client_id = t.getStringExtra("user_client_id");
		String image = t.getStringExtra("image");
		String nike = t.getStringExtra("nike");
		String email = t.getStringExtra("email");

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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.bt_send:
			
			
			if(isNetworkAvailable){
				String reason = pageViewaList.add_reson.getText().toString().trim();
				try {
					sendaddfriend(user_client_id,reason,mShareFileUtils.getString(Constant.CLIENT_ID, ""));
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
	private void sendaddfriend(String invitee_id, String reason , String client_id)
			throws UnsupportedEncodingException {
		// TODO Auto-generated method stub

		HttpEntity entity = null;
		try {
			entity = PeerParamsUtils.getaddfriendParams(this, invitee_id,
					reason);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		HttpUtil.post(this, HttpConfig.FRIEND_ADD_URL+client_id+".json"
				, entity, "application/json",
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
						JSONObject result;
						try {
							result = response.getJSONObject("success");
							
							pLog.i("test", "result:"+result);
						
						String code = result.getString("code");
						if (code.equals("ok")){
							showToast("请求已送达！", Toast.LENGTH_SHORT, false);
							finish();
						}else{
							showToast("网络繁忙，请稍后在试！", Toast.LENGTH_SHORT, false);
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
