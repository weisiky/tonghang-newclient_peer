package com.peer.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.peer.adapter.Recommend_topicAdapter;
import com.peer.adapter.SeachResultAdapter;
import com.peer.adapter.SearchTopicAdapter;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.bean.RecommendTopicBean;
import com.peer.bean.RecommendUserBean;
import com.peer.bean.SearchBean;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.pLog;
import com.peer.utils.pViewBox;

/**
 * 查询结果
 */
public class SearchResultActivity extends pBaseActivity {

	SeachResultAdapter adapter;
	SearchTopicAdapter adapter1;
	private PullToRefreshListView lv_searchresult;
	List<Object> list = new ArrayList<Object>();
	
	/** 分页 **/
	private int page = 1;
	/** 查询分类 **/
	String contanttype;
	
	String searchname;

	class PageViewList {
		private LinearLayout ll_back;
		private TextView tv_title;

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

		searchname = SearchBean.getInstance().getSearchname();
		String searchaccuratetarget = SearchBean.getInstance()
				.getCallbacklabel();

		lv_searchresult = (PullToRefreshListView) findViewById(R.id.lv_searchresult);
		pageViewaList.tv_title.setText(searchname);

	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);
		RefreshListner();
	}

	@Override
	protected void processBiz() {
		// TODO Auto-generated method stub
		try {
			if (SearchBean.getInstance().getSearchtype()
					.equals(Constant.TOPICBYTOPIC)) {
				contanttype = Constant.TOPICBYTOPIC;
				sendSearchResult(searchname, page, contanttype);
			} else if (SearchBean.getInstance().getSearchtype()
					.equals(Constant.USERBYNIKE)) {
				contanttype = Constant.USERBYNIKE;
				sendSearchResult(searchname, page, contanttype);
			} else if (SearchBean.getInstance().getSearchtype()
					.equals(Constant.TOPICBYLABEL)) {
				contanttype = Constant.TOPICBYLABEL;
				sendSearchResult(searchname, page, contanttype);
			} else if (SearchBean.getInstance().getSearchtype()
					.equals(Constant.USERBYLABEL)) {
				contanttype = Constant.USERBYLABEL;
				sendSearchResult(searchname, page, contanttype);
			}
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
		return getLayoutInflater()
				.inflate(R.layout.activity_searchresult, null);
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

	
	private void RefreshListner() {
		// TODO Auto-generated method stub
		lv_searchresult
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						String label = DateUtils.formatDateTime(
								SearchResultActivity.this
										.getApplicationContext(), System
										.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);
						try {
							page = 1;
							sendSearchResult(searchname, page, contanttype);
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						showToast("下拉", Toast.LENGTH_SHORT, false);
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						String label = DateUtils.formatDateTime(
								SearchResultActivity.this
										.getApplicationContext(), System
										.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);
						page = +1;
						try {
							sendSearchResult(searchname, ++page, contanttype);
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						showToast("上拉", Toast.LENGTH_SHORT, false);
					}
				});
	}

	/**
	 * 搜索请求
	 * 
	 * @param label_name
	 * @param page
	 * @throws UnsupportedEncodingException
	 */

	private void sendSearchResult(String name, int page, String contanttype)
			throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		
		String URL = null;

		Map<String, Object> searchParams = new HashMap<String, Object>();

		if (contanttype.equals(Constant.USERBYLABEL)) {
			searchParams.put("label_name", name);
			searchParams.put("pageindex", page);
			try {
				String	params = JsonDocHelper.toJSONString(searchParams);
			

			HttpEntity entity = new StringEntity(params, "utf-8");

			HttpUtil.post(this, HttpConfig.SEARCH_USER_LABEL_URL, entity, "application/json",
					new JsonHttpResponseHandler() {

						@Override
						public void onFailure(int statusCode, Header[] headers,
								String responseString, Throwable throwable) {
							// TODO Auto-generated method stub

							hideLoading();

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
							hideLoading();
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
							hideLoading();
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
							hideLoading();
							pLog.i("test", "onSuccess:statusCode:" + statusCode
									+ "headers:" + headers.toString() + "response:"
									+ response.toString());
							
							try {
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
								adapter = new SeachResultAdapter(SearchResultActivity.this, list);
								lv_searchresult.setAdapter(adapter);
							}
							refresh();
							
							super.onSuccess(statusCode, headers, response);
						}

					});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (contanttype.equals(Constant.USERBYNIKE)) {
			searchParams.put("username", name);
			searchParams.put("pageindex", page);
		
			
			try {
				String	params = JsonDocHelper.toJSONString(searchParams);
			

			HttpEntity entity = new StringEntity(params, "utf-8");

			HttpUtil.post(this, HttpConfig.SEARCH_USER_NICK_URL, entity, "application/json",
					new JsonHttpResponseHandler() {

						@Override
						public void onFailure(int statusCode, Header[] headers,
								String responseString, Throwable throwable) {
							// TODO Auto-generated method stub

							hideLoading();

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
							hideLoading();
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
							hideLoading();
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
							hideLoading();
							pLog.i("test", "onSuccess:statusCode:" + statusCode
									+ "headers:" + headers.toString() + "response:"
									+ response.toString());
							
							try {
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
								adapter = new SeachResultAdapter(SearchResultActivity.this, list);
								lv_searchresult.setAdapter(adapter);
							}
							refresh();
							
							super.onSuccess(statusCode, headers, response);
						}

					});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (contanttype.equals(Constant.TOPICBYLABEL)) {
			searchParams.put("label_name", name);
			searchParams.put("pageindex", page);
			
			try {
				String	params = JsonDocHelper.toJSONString(searchParams);
			

			HttpEntity entity = new StringEntity(params, "utf-8");

			HttpUtil.post(this, HttpConfig.SEARCH_TOPIC_LABEL_URL
					, entity, "application/json",
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
							try {
								RecommendTopicBean recommendtopicbean = JsonDocHelper.toJSONObject(
										response.getJSONObject("success")
												.toString(), RecommendTopicBean.class);
								if (recommendtopicbean != null) {
									pLog.i("test", "user1:"
											+ recommendtopicbean.topics.get(0).getSubject().toString());
								}
								
								for (int index = 0; index < recommendtopicbean.topics.size(); index++) {
									Map<String, Object> topicMsg = new HashMap<String, Object>();
									topicMsg.put("label_name", recommendtopicbean.topics.get(index).getLabel_name().toString());
									topicMsg.put("subject", recommendtopicbean.topics.get(index).getSubject().toString());
									topicMsg.put("user_id", recommendtopicbean.topics.get(index).getUser_id().toString());
									topicMsg.put("topic_id",
											recommendtopicbean.topics.get(index).getTopic_id().toString());
									topicMsg.put("sys_time",
											recommendtopicbean.getSys_time());
									topicMsg.put("image",
											recommendtopicbean.topics.get(index).getImage().toString());
									list.add(topicMsg);
								}

							} catch (Exception e1) {
								pLog.i("test", "Exception:" + e1.toString());
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							if (adapter1 == null) {
								adapter1 = new SearchTopicAdapter(SearchResultActivity.this, list);
								lv_searchresult.setAdapter(adapter1);
							}

							refresh();
							// adapter.setBaseFragment(HomeFragment.this);

							super.onSuccess(statusCode, headers, response);
						}


					});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else {
			searchParams.put("subject", name);
			searchParams.put("pageindex", page);
		try {
			String	params = JsonDocHelper.toJSONString(searchParams);
		

		HttpEntity entity = new StringEntity(params, "utf-8");

		HttpUtil.post(this, HttpConfig.SEARCH_TOPIC_SUBJECT_URL
				, entity, "application/json",
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
						try {
							RecommendTopicBean recommendtopicbean = JsonDocHelper.toJSONObject(
									response.getJSONObject("success")
											.toString(), RecommendTopicBean.class);
							if (recommendtopicbean != null) {
								pLog.i("test", "user1:"
										+ recommendtopicbean.topics.get(0).getSubject().toString());
							}
							
							for (int index = 0; index < recommendtopicbean.topics.size(); index++) {
								Map<String, Object> topicMsg = new HashMap<String, Object>();
								topicMsg.put("label_name", recommendtopicbean.topics.get(index).getLabel_name().toString());
								topicMsg.put("subject", recommendtopicbean.topics.get(index).getSubject().toString());
								topicMsg.put("user_id", recommendtopicbean.topics.get(index).getUser_id().toString());
								topicMsg.put("topic_id",
										recommendtopicbean.topics.get(index).getTopic_id().toString());
								topicMsg.put("sys_time",
										recommendtopicbean.getSys_time());
								topicMsg.put("image",
										recommendtopicbean.topics.get(index).getImage().toString());
								list.add(topicMsg);
							}

						} catch (Exception e1) {
							pLog.i("test", "Exception:" + e1.toString());
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						if (adapter1 == null) {
							adapter1 = new SearchTopicAdapter(SearchResultActivity.this, list);
							lv_searchresult.setAdapter(adapter1);
						}

						refresh();
						// adapter.setBaseFragment(HomeFragment.this);

						super.onSuccess(statusCode, headers, response);
					}


				});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	private void refresh() {

		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}else if(adapter1 != null){
			adapter1.notifyDataSetChanged();
		}

	}
}
