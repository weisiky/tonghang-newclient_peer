package com.peer.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.peer.R;
import com.peer.adapter.NewfriendsAdapter;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.bean.InvitationBean;
import com.peer.bean.NewFriendBean;
import com.peer.event.NewFriensEvent;
import com.peer.fragment.FriendsFragment;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.pLog;
import com.peer.utils.pViewBox;
import de.greenrobot.event.EventBus;

/**
 * ‘新朋友’页
 */
public class NewFriendsActivity extends pBaseActivity {

	private EventBus mBus;
	public List<InvitationBean> invitationbean = new ArrayList<InvitationBean>();
	private NewfriendsAdapter adapter;
	int page = 1;

	class PageViewList {
		private LinearLayout ll_back;
		private TextView tv_title;
		private ListView lv_newfriends;
	}

	private PageViewList pageViewaList;
	// 分页
	private int pageindex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newfriends);
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
				R.string.newfriends));
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);

	}

	@Override
	protected void processBiz() {
		// TODO Auto-generated method stub
		sendnewfriend(mShareFileUtils.getString(Constant.CLIENT_ID, ""));
		registEventBus();
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
		System.out.println("position:" + event.getPosition());
		invitationbean.remove(event.getPosition());
		adapter.notifyDataSetChanged();
		FriendsFragment.refreshhandle.sendEmptyMessage(Constant.REFRESHHANDLE);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mBus.unregister(this);
	}

	/**
	 * 获取新朋友请求
	 * 
	 * @param client_id
	 * @throws UnsupportedEncodingException
	 */
	private void sendnewfriend(String client_id) {
		// TODO Auto-generated method stub

		// RequestParams params = null;
		// try {
		// params = PeerParamsUtils.getNewFriendsParams(this, pageindex++);
		// } catch (Exception e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }

		HttpUtil.post(HttpConfig.FRIEND_INVITATION_URL + client_id + ".json",
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
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

						pLog.i("test", "response:" + response.toString());

						try {
							JSONObject result = response
									.getJSONObject("success");

							String code = result.getString("code");
							pLog.i("test", "code:" + code);
							if (code.equals("200")) {
								NewFriendBean newfriendbean = JsonDocHelper
										.toJSONObject(
												response.getJSONObject(
														"success").toString(),
												NewFriendBean.class);

								if (newfriendbean != null) {

									invitationbean
											.addAll(newfriendbean.invitation);
									pLog.i("test",
											"user1:"
													+ invitationbean.toString());
									if (adapter == null) {
										if (invitationbean != null
												&& invitationbean.size() > 0) {
											adapter = new NewfriendsAdapter(
													NewFriendsActivity.this,
													invitationbean,
													newfriendbean
															.getPic_server());
										}
										pageViewaList.lv_newfriends
												.setAdapter(adapter);
									}

									refresh();
								}
							} else if (code.equals("500")) {

							} else {
								String message = result.getString("message");
								showToast(message, Toast.LENGTH_SHORT, false);
							}
						} catch (Exception e1) {
							pLog.i("test", "Exception:" + e1.toString());
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						// adapter.setBaseFragment(HomeFragment.this);

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
