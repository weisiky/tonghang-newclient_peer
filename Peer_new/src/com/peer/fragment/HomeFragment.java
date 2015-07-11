package com.peer.fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.peer.activity.MainActivity;
import com.peer.activity.PersonalPageActivity;
import com.peer.activity.R;
import com.peer.activity.Recommend_topic;
import com.peer.activity.SearchUserActivity;
import com.peer.adapter.HomepageAdapter;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.base.pBaseFragment;
import com.peer.bean.LoginBean;
import com.peer.bean.RecommendUserBean;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.utils.BussinessUtils;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.pIOUitls;
import com.peer.utils.pLog;

/**
 * 主Fragment
 */
public class HomeFragment extends pBaseFragment {

	HomepageAdapter adapter;

	private LinearLayout ll_search;
	List<Object> list = new ArrayList<Object>();
	private PullToRefreshListView pull_refresh_homepage;
	public LinearLayout base_neterror_item;
	private TextView tv_connect_errormsg;
	/** 分页参数 **/
	int page = 1;

	private pBaseActivity pbaseActivity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_home, container, false);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		pbaseActivity = (pBaseActivity) activity;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		init();
		setListener();
		try {
			sendRecommendTask(
					mShareFileUtils.getString(Constant.CLIENT_ID, ""), page);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setListener() {
		// TODO Auto-generated method stub
		pull_refresh_homepage.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();

				if (position == 0) {
					// if (!pbaseActivity.isNetworkAvailable) {
					// pbaseActivity.showToast(getActivity().getResources().getString(
					// R.string.Broken_network_prompt), Toast.LENGTH_LONG,
					// false);
					// } else {
					pbaseActivity.startActivityForLeft(Recommend_topic.class,
							intent, false);
					// }
				} else {

				}

			}
		});
	}

	private void RefreshListner() {
		// TODO Auto-generated method stub
		pull_refresh_homepage
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						String label = DateUtils.formatDateTime(getActivity()
								.getApplicationContext(), System
								.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);
						try {
							page = 1;
							sendRecommendTask(mShareFileUtils.getString(
									Constant.CLIENT_ID, ""), page);
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						String label = DateUtils.formatDateTime(getActivity()
								.getApplicationContext(), System
								.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);
						try {
							sendRecommendTask(mShareFileUtils.getString(
									Constant.CLIENT_ID, ""), ++page);
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	/**
	 * 推荐用户请求
	 * 
	 * @param client_id
	 * @param page
	 * @throws UnsupportedEncodingException
	 */
	private void sendRecommendTask(String client_id, final int page)
			throws UnsupportedEncodingException {
		// TODO Auto-generated method stub

		HttpEntity entity = null;
		try {
			entity = PeerParamsUtils.getHomeParams(pbaseActivity, client_id,
					page);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		HttpUtil.post(getActivity(), HttpConfig.USER_RECOMMEND_IN_URL, entity,
				"application/json", new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub
						pbaseActivity.hideLoading();
						super.onFailure(statusCode, headers, responseString,
								throwable);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONArray errorResponse) {
						// TODO Auto-generated method stub
						pbaseActivity.hideLoading();
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						pbaseActivity.hideLoading();
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						pbaseActivity.hideLoading();

						pIOUitls.saveStrToSD(Constant.C_FILE_CACHE_PATH,
								"home.etag", false, response.toString());

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
									ArrayList<Object> userlist = new ArrayList<Object>();
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
									userlist.add(userMsg);
									for (int i = 0; i < recommenduserbean.users
											.get(index).getLabels().size(); i++) {
										labelnames.add(recommenduserbean.users
												.get(index).getLabels().get(i));
									}
									userlist.add(labelnames);
									list.add(userlist);
								}

								if (adapter == null) {
									adapter = new HomepageAdapter(
											getActivity(), list);
									pull_refresh_homepage.setAdapter(adapter);
								}

								refresh();

							}

						} catch (Exception e1) {
							pLog.i("test", "Exception:" + e1.toString());
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						/*
						 * try { JSONObject success = response
						 * .getJSONObject("success"); JSONArray user =
						 * (JSONArray) success.get("user"); for (int index = 0;
						 * index < user.length(); index++) { ArrayList<Object>
						 * userlist = new ArrayList<Object>(); JSONObject person
						 * = user.getJSONObject(index); List<String> labelnames
						 * = new ArrayList<String>(); Map<String, Object>
						 * userMsg = new HashMap<String, Object>();
						 * userMsg.put("email", person.getString("email"));
						 * userMsg.put("sex", person.getString("sex"));
						 * userMsg.put("city", person.getString("city"));
						 * userMsg.put("username",
						 * person.getString("username"));
						 * userMsg.put("client_id",
						 * person.getString("client_id")); userMsg.put("image",
						 * person.getString("image")); userMsg.put("created_at",
						 * person.getString("created_at")); userMsg.put("birth",
						 * person.getString("birth")); userlist.add(userMsg);
						 * JSONArray lab = (JSONArray) person .get("labels");
						 * for (int i = 0; i < lab.length(); i++) {
						 * labelnames.add(lab.getString(i)); }
						 * userlist.add(labelnames); list.add(userlist); } }
						 * catch (JSONException e) { // TODO Auto-generated
						 * catch block e.printStackTrace(); }
						 */
						// adapter.setBaseFragment(HomeFragment.this);

						super.onSuccess(statusCode, headers, response);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String responseString) {
						// TODO Auto-generated method stub
						super.onSuccess(statusCode, headers, responseString);
					}

				});

	}

	private void refresh() {

		if (adapter != null) {
			adapter.notifyDataSetChanged();
			pull_refresh_homepage.onRefreshComplete();
		}

	}

	private void init() {
		// TODO Auto-generated method stub
		pull_refresh_homepage = (PullToRefreshListView) getView().findViewById(
				R.id.pull_refresh_homepage);
		base_neterror_item = (LinearLayout) getView().findViewById(
				R.id.base_neterror_item);
		tv_connect_errormsg = (TextView) base_neterror_item
				.findViewById(R.id.tv_connect_errormsg);

		ll_search = (LinearLayout) getView().findViewById(R.id.ll_search);
		ll_search.setOnClickListener(this);

		String homeCount = pIOUitls.readFileByLines(Constant.C_FILE_CACHE_PATH,
				"home.etag");

		RecommendUserBean recommenduserbean;
		try {
			recommenduserbean = JsonDocHelper.toJSONObject(homeCount,
					RecommendUserBean.class);

			for (int index = 0; index < recommenduserbean.users.size(); index++) {
				ArrayList<Object> userlist = new ArrayList<Object>();
				List<String> labelnames = new ArrayList<String>();
				Map<String, Object> userMsg = new HashMap<String, Object>();
				userMsg.put("email", recommenduserbean.users.get(index)
						.getEmail());
				userMsg.put("sex", recommenduserbean.users.get(index).getSex());
				userMsg.put("city", recommenduserbean.users.get(index)
						.getCity());
				userMsg.put("username", recommenduserbean.users.get(index)
						.getUsername());
				userMsg.put("client_id", recommenduserbean.users.get(index)
						.getClient_id());
				userMsg.put("image", recommenduserbean.users.get(index)
						.getImage());
				userMsg.put("created_at", recommenduserbean.users.get(index)
						.getCreated_at());
				userMsg.put("birth", recommenduserbean.users.get(index)
						.getBirth());
				userlist.add(userMsg);
				for (int i = 0; i < recommenduserbean.users.get(index)
						.getLabels().size(); i++) {
					labelnames.add(recommenduserbean.users.get(index)
							.getLabels().get(i));
				}
				userlist.add(labelnames);
				list.add(userlist);
			}

			if (homeCount != null) {

				if (adapter == null) {
					adapter = new HomepageAdapter(getActivity(), list);
				}
				// adapter.setBaseFragment(HomeFragment.this);
				pull_refresh_homepage.setAdapter(adapter);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		refresh();

		RefreshListner();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_search:
			Intent search = new Intent(getActivity(), SearchUserActivity.class);
			startActivity(search);
			break;

		case R.id.im_headpic:

			break;
		default:
			break;
		}

	}

}
