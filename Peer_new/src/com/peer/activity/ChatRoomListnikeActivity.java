package com.peer.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.peer.R;
import com.peer.adapter.FriendsAdapter;
import com.peer.adapter.HomepageAdapter;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.bean.RecommendUserBean;
import com.peer.bean.User;
import com.peer.bean.UserBean;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.pIOUitls;
import com.peer.utils.pLog;
import com.peer.utils.pViewBox;


/**
 * 聊天室成员activity
 */
public class ChatRoomListnikeActivity extends pBaseActivity{
	
	private List<UserBean> userlist=new ArrayList<UserBean>();
	private FriendsAdapter adapter;
	private int page = 1;
	
	class PageViewList {
		private LinearLayout ll_back;
		private TextView tv_title;
		private ListView lv_listnike_chatroom;
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
		pageViewaList.tv_title.setText(getResources().getString(R.string.chatroommember));
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);
	}

	@Override
	protected void processBiz() {
		// TODO Auto-generated method stub
		
		Intent intent = getIntent();
		try {
			sendUserNumber(intent.getStringExtra("groupId"),page,intent.getStringExtra("client_id"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
		return getLayoutInflater().inflate(R.layout.activity_chatroom_listnike, null);
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
	 * 获取参与话题用户请求
	 * 
	 * @param groupid
	 * @param page
	 * @param client_id
	 * @throws UnsupportedEncodingException
	 */
	private void sendUserNumber(String groupid, final int page , String client_id)
			throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		
		RequestParams params = null;
		try {
			params =PeerParamsUtils.getUserNumberParams(ChatRoomListnikeActivity.this, groupid,
					page,client_id);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		HttpUtil.post(HttpConfig.NUMBER_IN_URL, params, new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub
						showToast(getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, responseString,
								throwable);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONArray errorResponse) {
						// TODO Auto-generated method stub
						showToast(getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						showToast(getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub

						try {
							JSONObject result = response.getJSONObject("success");

							String code = result.getString("code");
							pLog.i("test", "code:"+code);
							if(code.equals("200")){
								RecommendUserBean recommenduserbean = JsonDocHelper
										.toJSONObject(
												response.getJSONObject("success")
												.toString(),
												RecommendUserBean.class);
								if (recommenduserbean != null) {
									
									if (page == 1) {
										userlist.clear();
									}
									userlist.addAll(recommenduserbean.users);
									
									
									if (adapter == null) {
										adapter = new FriendsAdapter(
												ChatRoomListnikeActivity.this, userlist
												,recommenduserbean.getPic_server());
										pageViewaList.lv_listnike_chatroom.setAdapter(adapter);
									}
								}
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
						refresh();


						super.onSuccess(statusCode, headers, response);
					}


				});

	}
	
	
	private void refresh() {

		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}

	}
}
