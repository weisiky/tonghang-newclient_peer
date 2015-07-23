package com.peer.activity;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.peer.adapter.NewfriendsAdapter;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.bean.NewFriendBean;
import com.peer.event.NewFriensEvent;
import com.peer.fragment.FriendsFragment;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.pLog;
import com.peer.utils.pViewBox;

import de.greenrobot.event.EventBus;

/**
 * ‘新朋友’页
 */
public class NewFriendsActivity extends pBaseActivity {

	private EventBus mBus;
	private List<Object> mlist, list;
	private NewfriendsAdapter adapter;

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
		registEventBus();
		sendnewfriend(mShareFileUtils.getString(Constant.CLIENT_ID, ""));

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
		return getLayoutInflater().inflate(R.layout.activity_newfriends, null);
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

		mlist.remove(event.getPosition());
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

						pLog.i("test", "response:" + response.toString());

						try {
							NewFriendBean newfriendbean = JsonDocHelper
									.toJSONObject(
											response.getJSONObject("success")
													.toString(),
											NewFriendBean.class);

							if (newfriendbean != null) {

								pLog.i("test", "user1:"
										+ newfriendbean.getInvitationbean()
												.get(0).getUserbean()
												.getUsername().toString());

							}
							for (int index = 0; index < NewFriendBean
									.getInstance().getInvitationbean().size(); index++) {
								list.add(NewFriendBean.getInstance()
										.getInvitationbean().get(index));
							}

						} catch (Exception e1) {
							pLog.i("test", "Exception:" + e1.toString());
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						if (adapter == null) {
							if (list!=null&&list.size() > 0) {
								adapter = new NewfriendsAdapter(
										NewFriendsActivity.this, list);
							}
							pageViewaList.lv_newfriends.setAdapter(adapter);
						}

						refresh();
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
