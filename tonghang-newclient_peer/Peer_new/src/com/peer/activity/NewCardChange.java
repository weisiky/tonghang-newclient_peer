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
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.peer.R;
import com.peer.IMimplements.easemobchatImp;
import com.peer.activity.NewFriendsActivity.PageViewList;
import com.peer.adapter.ChatHistoryAdapter;
import com.peer.adapter.NewCardAdapter;
import com.peer.adapter.NewfriendsAdapter;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.bean.CardChangeBean;
import com.peer.bean.InvitationBean;
import com.peer.bean.LoginBean;
import com.peer.bean.NewFriendBean;
import com.peer.bean.SqlBean;
import com.peer.bean.UserBean;
import com.peer.event.NewFriensEvent;
import com.peer.fragment.FriendsFragment;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.utils.CardDbHelper;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.ManagerActivity;
import com.peer.utils.pLog;
import com.peer.utils.pViewBox;

import de.greenrobot.event.EventBus;

public class NewCardChange extends pBaseActivity{


	private EventBus mBus;
	public List<CardChangeBean> userbean = new ArrayList<CardChangeBean>();
	private NewCardAdapter adapter;
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
				R.string.newcardchange));
		mShareFileUtils.setString(Constant.MESSAGE, "暂无新交换请求");
		pLog.i("test", "本地id:"+mShareFileUtils.getString(Constant.CLIENT_ID, ""));
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);

	}

	@Override
	protected void processBiz() {
		// TODO Auto-generated method stub
		String id = mShareFileUtils.getString(Constant.CLIENT_ID, "");
		pLog.i("test", "本地client_id:"+id);
		CardDbHelper.setmContext(this);
		ArrayList<SqlBean> sqllist = CardDbHelper.getAllCardId(id);
		pLog.i("test", "sql:"+sqllist.toString());
		for(int i=0;i<sqllist.size();i++){
			CardChangeBean bean = new CardChangeBean();
			bean.setType(sqllist.get(i).getType());
			bean.setDate(sqllist.get(i).getDate());
			bean.setName(sqllist.get(i).getName());
			bean.setPushid(sqllist.get(i).getPushid());
			bean.setImage(sqllist.get(i).getImage());
			userbean
					.add(bean);
			if (adapter == null) {
				if (userbean != null
						&& userbean.size() > 0) {
					adapter = new NewCardAdapter(
							NewCardChange.this,
							userbean);
				}
				pageViewaList.lv_newfriends
						.setAdapter(adapter);
			}

			refresh();
//			try {
//				senduser(pushid,
//						mShareFileUtils.getString(Constant.CLIENT_ID, ""),type,date);
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
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
//		sendSystemConfig();
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
		userbean.remove(event.getPosition());
		adapter.notifyDataSetChanged();
		FriendsFragment.refreshhandle.sendEmptyMessage(Constant.REFRESHHANDLE);
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
	 * 获取用户信息接口
	 * 
	 * @param client_id
	 * @throws UnsupportedEncodingException
	 */

	private void senduser(String client_id, String o_client_id, final String type , final String date)
			throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		final Intent intent = new Intent();
		RequestParams params = null;
		try {
			params = PeerParamsUtils.getUserParams(this, client_id);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpUtil.post(HttpConfig.USER_IN_URL + client_id + ".json?client_id="
				+ o_client_id, params, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				showToast(getResources().getString(R.string.config_error),
						Toast.LENGTH_SHORT, false);
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONArray errorResponse) {
				// TODO Auto-generated method stub
				showToast(getResources().getString(R.string.config_error),
						Toast.LENGTH_SHORT, false);
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				showToast(getResources().getString(R.string.config_error),
						Toast.LENGTH_SHORT, false);
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				try {
					JSONObject result = response.getJSONObject("success");

					String code = result.getString("code");
					if (code.equals("200")) {
						LoginBean loginBean = JsonDocHelper.toJSONObject(
								response.getJSONObject("success").toString(),
								LoginBean.class);
						if (loginBean != null) {
							CardChangeBean bean = new CardChangeBean();
							bean.setUserbean(loginBean.user);
							bean.setType(type);
							bean.setDate(date);
							userbean
									.add(bean);
							if (adapter == null) {
								if (userbean != null
										&& userbean.size() > 0) {
//									adapter = new NewCardAdapter(
//											NewCardChange.this,
//											userbean,
//											loginBean
//													.getPic_server());
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
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				super.onSuccess(statusCode, headers, response);

			}

		});
	}

}
