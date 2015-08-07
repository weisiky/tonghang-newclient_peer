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

import android.app.Activity;
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
import com.loopj.android.http.RequestParams;
import com.peer.IMimplements.easemobchatUser;
import com.peer.activity.NewFriendsActivity;
import com.peer.R;
import com.peer.adapter.ChatHistoryAdapter;
import com.peer.adapter.FriendsAdapter;
import com.peer.adapter.HomepageAdapter;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.base.pBaseFragment;
import com.peer.bean.InvitationBean;
import com.peer.bean.RecommendUserBean;
import com.peer.bean.User;
import com.peer.bean.UserBean;
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
	
	private List<UserBean> list= new ArrayList<UserBean>();
	
	private FriendsAdapter adapter;
	
	private BadgeView newnum;
	private int newfriendsnum;
	private pBaseActivity pbaseActivity;
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
						pLog.i("test","发送sendComeMsg请求");
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
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		pbaseActivity=(pBaseActivity) activity;
		super.onAttach(activity);
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
				System.out.println("newfriendsnum:"+newfriendsnum);
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
		newnum=new BadgeView(getActivity(),tv_newfriends);
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
	private void sendComeMsg(String client_id )
			throws UnsupportedEncodingException {
		// TODO Auto-generated method stub


		HttpUtil.post(HttpConfig.FRIEND_GET_URL+client_id+".json",
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub
						pbaseActivity.hideLoading();
						pLog.i("test", "statusCode:"+statusCode);
						pLog.i("test", "headers:"+headers);
						pLog.i("test", "responseString:"+responseString);
						pLog.i("test", "throwable:"+throwable);
						super.onFailure(statusCode, headers, responseString,
								throwable);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONArray errorResponse) {
						// TODO Auto-generated method stub
						pbaseActivity.hideLoading();
						pLog.i("test", "statusCode:"+statusCode);
						pLog.i("test", "headers:"+headers);
						pLog.i("test", "errorResponse:"+errorResponse);
						pLog.i("test", "throwable:"+throwable);
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						pbaseActivity.hideLoading();
						pLog.i("test", "statusCode:"+statusCode);
						pLog.i("test", "headers:"+headers);
						pLog.i("test", "errorResponse:"+errorResponse);
						pLog.i("test", "throwable:"+throwable);
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}


					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						pbaseActivity.hideLoading();
						pLog.i("test", "response:"+response.toString());
						try{
							RecommendUserBean recommenduserbean = JsonDocHelper.toJSONObject(
									response.getJSONObject("success")
									.toString(), RecommendUserBean.class);
						
							if (recommenduserbean != null) {
								System.out.println("recommenduserbean:"+recommenduserbean.users);
								list.addAll(recommenduserbean.users);

								if (adapter == null) {
									adapter = new FriendsAdapter(getActivity(), list
											,recommenduserbean.getPic_server());
									lv_friends.setAdapter(adapter);
								}

								refresh1();

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
	
	private void refresh1() {

		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}

	}
	
}
