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

import com.loopj.android.http.JsonHttpResponseHandler;
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
	
	private List<Object> list=new ArrayList<Object>();
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

		HttpEntity entity = null;
		try {
			entity = PeerParamsUtils.getUserNumberParams(ChatRoomListnikeActivity.this, groupid,
					page,client_id);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		HttpUtil.post(this, HttpConfig.NUMBER_IN_URL, entity,
				"application/json", new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, responseString,
								throwable);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONArray errorResponse) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub

						try {
							RecommendUserBean recommenduserbean = JsonDocHelper
									.toJSONObject(
											response.getJSONObject("success")
													.toString(),
											RecommendUserBean.class);
							if (recommenduserbean != null) {

								if (page == 1) {
									list.clear();
								}

								for (int index = 0; index < recommenduserbean.users
										.size(); index++) {
									ArrayList<UserBean> userlist = new ArrayList<UserBean>();
									List<String> labelnames = new ArrayList<String>();
									Map<String, Object> userMsg = new HashMap<String, Object>();
									userMsg.put("email",
											recommenduserbean.users.get(index)
													.getEmail());
									userMsg.put("sex", recommenduserbean.users
											.get(index).getSex());
									userMsg.put("city", recommenduserbean.users
											.get(index).getCity());
									userMsg.put("username",
											recommenduserbean.users.get(index)
													.getUsername());
									userMsg.put("client_id",
											recommenduserbean.users.get(index)
													.getClient_id());
									userMsg.put("image",
											recommenduserbean.users.get(index)
													.getImage());
									userMsg.put("created_at",
											recommenduserbean.users.get(index)
													.getCreated_at());
									userMsg.put("birth",
											recommenduserbean.users.get(index)
													.getBirth());
									userMsg.put("is_friend",
											recommenduserbean.users.get(index)
													.getIs_friend());
									userlist.add((UserBean) userMsg);
									for (int i = 0; i < recommenduserbean.users
											.get(index).getLabels().size(); i++) {
										labelnames.add(recommenduserbean.users
												.get(index).getLabels().get(i));
									}
									userlist.add((UserBean) labelnames);
									list.addAll(userlist);
								}

								if (adapter == null) {
									adapter = new FriendsAdapter(
											ChatRoomListnikeActivity.this, list);
									pageViewaList.lv_listnike_chatroom.setAdapter(adapter);
								}

								refresh();

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
	
	
	private void refresh() {

		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}

	}
}
