package com.peer.fragment;

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
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.peer.IMimplements.easemobchatUser;
import com.peer.activity.NewFriendsActivity;
import com.peer.activity.R;
import com.peer.adapter.ChatHistoryAdapter;
import com.peer.adapter.FriendsAdapter;
import com.peer.base.Constant;
import com.peer.base.pBaseFragment;
import com.peer.bean.RecommendUserBean;
import com.peer.bean.User;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.pLog;
import com.readystatesoftware.viewbadger.BadgeView;

public class FriendsFragment extends pBaseFragment{
	
	private LinearLayout ll_head_come;
	private RelativeLayout rl_newfriends;
	private TextView tv_newfriends;
	private ListView lv_friends;
	
	private List<Object> list;
	
	private FriendsAdapter adapter;
	
	private BadgeView newnum;
	private int newfriendsnum;
	public static Handler refreshhandle;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_friends, container, false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		init();
		refreshhandle=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if(msg.what==Constant.REFRESHHANDLE){
					if(list!=null){
						list.clear();
					}
					try {
						sendComeMsg(mShareFileUtils.getString(Constant.CLIENT_ID, ""));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
			
	}
	
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if (!hidden) {
			if(!hidden&&checkNetworkState()){
				if(list!=null){
					list.clear();
				}
				try {
					sendComeMsg(mShareFileUtils.getString(Constant.CLIENT_ID, ""));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	
	public void setNewfriendsNum(int number){
		this.newfriendsnum=number;
		getActivity().runOnUiThread(new Runnable() {
			public void run() {
				if(newfriendsnum>0){
					newnum.setText(String.valueOf(newfriendsnum));
					newnum.show();
				}else{
					newnum.hide();
				}	
			}
		});
		
	}
	
	



	private void init() {
		// TODO Auto-generated method stub
		lv_friends=(ListView)getView().findViewById(R.id.lv_friends);				
		rl_newfriends=(RelativeLayout)getView().findViewById(R.id.rl_newfriends);
		tv_newfriends=(TextView)getView().findViewById(R.id.tv_newfriends);
		rl_newfriends.setOnClickListener(this);
		
		
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_newfriends:
			Intent newfriends = new Intent(getActivity(),NewFriendsActivity.class);
			startActivity(newfriends);
			break;

		default:
			break;
		}
	}
	
	
	/**
	 * 获取好友列表息请求
	 * 
	 * @param client_id
	 * @throws UnsupportedEncodingException
	 */
	private void sendComeMsg(String client_id)
			throws UnsupportedEncodingException {
		// TODO Auto-generated method stub

		/*HttpEntity entity = null;
		try {
			entity = PeerParamsUtils.getComMsgParams(getActivity(), client_id);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
*/
		HttpUtil.get(HttpConfig.FRIEND_GET_URL+client_id+".json",
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub

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

						pLog.i("test", "onSuccess:statusCode:" + statusCode
								+ "headers:" + headers.toString() + "response:"
								+ response.toString());
						try{
							RecommendUserBean recommenduserbean = JsonDocHelper.toJSONObject(
									response.getJSONObject("success")
									.toString(), RecommendUserBean.class);
						
							if (recommenduserbean != null) {
	
								pLog.i("test", "user1:"
										+ recommenduserbean.users.get(0).getUsername().toString());
						
						}
						for (int index = 0; index < recommenduserbean.users.size(); index++) {
							ArrayList<Object> userlist = new ArrayList<Object>();
							List<String> labelnames = new ArrayList<String>();
							Map<String, Object> userMsg = new HashMap<String, Object>();
							userMsg.put("email", recommenduserbean.users.get(index).getEmail());
							userMsg.put("sex", recommenduserbean.users.get(index).getSex());
							userMsg.put("city", recommenduserbean.users.get(index).getCity());
							userMsg.put("username",
									recommenduserbean.users.get(index).getUsername());
							userMsg.put("client_id",
									recommenduserbean.users.get(index).getClient_id());
							userMsg.put("image", recommenduserbean.users.get(index).getImage());
							userMsg.put("created_at",
									recommenduserbean.users.get(index).getCreated_at());
							userMsg.put("birth", recommenduserbean.users.get(index).getBirth());
							userMsg.put("is_friend", recommenduserbean.users.get(index).getIs_friend());
							userlist.add(userMsg);
							for (int i = 0; i <recommenduserbean.users.get(index).getLabels().size(); i++) {
								labelnames.add(recommenduserbean.users.get(index).getLabels().get(i));
							}
							userlist.add(labelnames);
							list.add(userlist);
						}

					} catch (Exception e1) {
						pLog.i("test", "Exception:" + e1.toString());
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
						


						if (adapter == null) {
							adapter = new FriendsAdapter(getActivity(), list);
							lv_friends.setAdapter(adapter);
						}

						refresh1();
						// adapter.setBaseFragment(HomeFragment.this);

						super.onSuccess(statusCode, headers, response);
					}


				});

	}
	
	private void refresh1() {

		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}

	}
	
}
