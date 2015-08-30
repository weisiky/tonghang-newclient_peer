package com.peer.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.peer.R;
import com.peer.activity.AddFriendsActivity.PageViewList;
import com.peer.adapter.BlacklistAdapter;
import com.peer.adapter.HomepageAdapter;
import com.peer.adapter.NewfriendsAdapter;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.bean.InvitationBean;
import com.peer.bean.NewFriendBean;
import com.peer.bean.RecommendUserBean;
import com.peer.bean.UserBean;
import com.peer.event.NewFriensEvent;
import com.peer.fragment.FriendsFragment;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.utils.ImageLoaderUtil;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.pLog;
import com.peer.utils.pViewBox;

import de.greenrobot.event.EventBus;

/**
 * 黑名单功能类
 * @author weisiky
 *
 */
public class BlackListActivity extends pBaseActivity{
	private EventBus mBus;
	public List<UserBean> blacklistbean = new ArrayList<UserBean>();
	private BlacklistAdapter adapter;
	int page=1;

	class PageViewList {
		private LinearLayout ll_back;
		private TextView tv_title;
		private ListView lv_blacklist;
	}

	private PageViewList pageViewaList;
	// 分页
	private int pageindex;

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
				R.string.blacklist));
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);
		
	}

	@Override
	protected void processBiz() {
		// TODO Auto-generated method stub
		sendblacklist(mShareFileUtils.getString(Constant.CLIENT_ID, ""));
		registEventBus();
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
		return getLayoutInflater().inflate(R.layout.activity_blacklist, null);
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

	private void registEventBus() {
		// TODO Auto-generated method stub
		mBus = EventBus.getDefault();
		/*
		 * Registration: three parameters are respectively, message subscriber
		 * (receiver), receiving method name, event classes
		 */
		mBus.register(this, "getEvent", NewFriensEvent.class);
	}

	private void getEvent(NewFriensEvent event) {
		System.out.println("position:"+event.getPosition());
		blacklistbean.remove(event.getPosition());
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mBus.unregister(this);
	}


	private void refresh() {

		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}

	}
	
	
	/**
	 * 获取用户黑名单请求
	 * 
	 * @param client_id
	 * @throws UnsupportedEncodingException
	 */
	private void sendblacklist(String client_id) {
		RequestParams params = null;
		try {
			params = PeerParamsUtils.getblacklistParams(this);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		HttpUtil.post(this, HttpConfig.BLACKLIST_GET_URL + client_id + "/blacklist.json",
				params, new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub
						hideLoading();
						showToast(getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, responseString,
								throwable);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONArray errorResponse) {
						// TODO Auto-generated method stub
						hideLoading();
						showToast(getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						hideLoading();
						showToast(getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
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
							pLog.i("test", "code:"+code);
							if (code.equals("200")) {
								RecommendUserBean recommenduserbean = JsonDocHelper
										.toJSONObject(
												response.getJSONObject("success")
												.toString(),
												RecommendUserBean.class);
								if (recommenduserbean != null) {
									blacklistbean.addAll(recommenduserbean.users);
									
									if (adapter == null) {
										adapter = new BlacklistAdapter(
												BlackListActivity.this, blacklistbean
												,mShareFileUtils.getString(Constant.PIC_SERVER, ""));
										pageViewaList.lv_blacklist.setAdapter(adapter);
									}
								}
							}else if(code.equals("500")){
								
							}else{
								String message = result.getString("message");
								showToast(message, Toast.LENGTH_SHORT, false);
							}

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

	}

}
