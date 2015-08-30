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

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.peer.R;
import com.peer.adapter.Recommend_topicAdapter;
import com.peer.adapter.SeachResultAdapter;
import com.peer.adapter.SearchTopicAdapter;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.bean.RecommendTopicBean;
import com.peer.bean.RecommendUserBean;
import com.peer.bean.SearchBean;
import com.peer.bean.TopicBean;
import com.peer.bean.UserBean;
import com.peer.fragment.HomeFragment;
import com.peer.gps.MyLocationListener;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.titlepopwindow.ActionItem;
import com.peer.titlepopwindow.TitlePopup;
import com.peer.titlepopwindow.TitlePopup.OnItemOnClickListener;
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
	List<UserBean> userbean = new ArrayList<UserBean>();
	List<TopicBean> topicbean = new ArrayList<TopicBean>();
	private TitlePopup titlePopup;
	
	/** 分页 **/
	private int page = 1;
	/** 查询分类 **/
	String contanttype;

	String searchname;

	class PageViewList {
		private LinearLayout ll_back,ll_GPSdownview;
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
				pageViewaList.ll_GPSdownview.setVisibility(View.GONE);
				contanttype = Constant.TOPICBYTOPIC;
				sendSearchResult(searchname, page, contanttype,HomeFragment.byDistance);
			} else if (SearchBean.getInstance().getSearchtype()
					.equals(Constant.USERBYNIKE)) {
				titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT, true);
				if(HomeFragment.byDistance){
					titlePopup.addAction(new ActionItem(this, 
							getResources().getString(R.string.littleleave), R.color.white));
					titlePopup.addAction(new ActionItem(this,
							getResources().getString(R.string.tonghang), R.color.gray));
				}else{
					titlePopup.addAction(new ActionItem(this, 
							getResources().getString(R.string.littleleave), R.color.gray));
					titlePopup.addAction(new ActionItem(this,
							getResources().getString(R.string.tonghang), R.color.white));
				}
				gpspopupwindow();
				pageViewaList.ll_GPSdownview.setVisibility(View.VISIBLE);
				pageViewaList.ll_GPSdownview.setOnClickListener(this);
				contanttype = Constant.USERBYNIKE;
				sendSearchResult(searchname, page, contanttype,HomeFragment.byDistance);
			} else if (SearchBean.getInstance().getSearchtype()
					.equals(Constant.TOPICBYLABEL)) {
				pageViewaList.ll_GPSdownview.setVisibility(View.GONE);
				contanttype = Constant.TOPICBYLABEL;
				sendSearchResult(searchname, page, contanttype,HomeFragment.byDistance);
			} else if (SearchBean.getInstance().getSearchtype()
					.equals(Constant.USERBYLABEL)) {
				titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT, true);
				if(HomeFragment.byDistance){
					titlePopup.addAction(new ActionItem(this, 
							getResources().getString(R.string.littleleave), R.color.white));
					titlePopup.addAction(new ActionItem(this,
							getResources().getString(R.string.tonghang), R.color.gray));
				}else{
					titlePopup.addAction(new ActionItem(this, 
							getResources().getString(R.string.littleleave), R.color.gray));
					titlePopup.addAction(new ActionItem(this,
							getResources().getString(R.string.tonghang), R.color.white));
				}
				gpspopupwindow();
				pageViewaList.ll_GPSdownview.setVisibility(View.VISIBLE);
				pageViewaList.ll_GPSdownview.setOnClickListener(this);
				contanttype = Constant.USERBYLABEL;
				sendSearchResult(searchname, page, contanttype,HomeFragment.byDistance);
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
		switch (v.getId()) {
			case R.id.ll_GPSdownview:
				titlePopup.showgps(v);
				break;
			default:
				break;
		}
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
						if(isNetworkAvailable){
							try {
								page = 1;
								sendSearchResult(searchname, page, contanttype,HomeFragment.byDistance);
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}else{
							showToast(getResources().getString(R.string.Broken_network_prompt)
									, Toast.LENGTH_SHORT, false);
							lv_searchresult.onRefreshComplete();
						}
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
						if(isNetworkAvailable){
							try {
								sendSearchResult(searchname, ++page, contanttype,HomeFragment.byDistance);
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}else{
							showToast(getResources().getString(R.string.Broken_network_prompt)
									, Toast.LENGTH_SHORT, false);
							lv_searchresult.onRefreshComplete();
						}
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

	private void sendSearchResult(String name, final int page, String contanttype,boolean byDistance)
			throws UnsupportedEncodingException {
		// TODO Auto-generated method stub

		String URL = null;

		Map<String, Object> searchParams = new HashMap<String, Object>();

		if (contanttype.equals(Constant.USERBYLABEL)) {
			searchParams.put("label_name", name);
			searchParams.put("pageindex", page);
			try {
				
				RequestParams params = null;
				try {
					params = PeerParamsUtils.getSearchUserByLabelParams(this, name, page,mShareFileUtils.getString(Constant.CLIENT_ID, ""),byDistance);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				

				HttpUtil.post(this, HttpConfig.SEARCH_USER_LABEL_URL, params,
						 new JsonHttpResponseHandler() {

							@Override
							public void onFailure(int statusCode,
									Header[] headers, String responseString,
									Throwable throwable) {
								// TODO Auto-generated method stub

								hideLoading();
								showToast(getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
								super.onFailure(statusCode, headers,
										responseString, throwable);
							}

							@Override
							public void onFailure(int statusCode,
									Header[] headers, Throwable throwable,
									JSONArray errorResponse) {
								// TODO Auto-generated method stub
								hideLoading();
								showToast(getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
								super.onFailure(statusCode, headers, throwable,
										errorResponse);
							}

							@Override
							public void onFailure(int statusCode,
									Header[] headers, Throwable throwable,
									JSONObject errorResponse) {
								// TODO Auto-generated method stub
								hideLoading();
								showToast(getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
								super.onFailure(statusCode, headers, throwable,
										errorResponse);
							}

							@Override
							public void onSuccess(int statusCode,
									Header[] headers, JSONObject response) {
								// TODO Auto-generated method stub
								hideLoading();
								try {
									JSONObject result = response.getJSONObject("success");

									String code = result.getString("code");
									pLog.i("test", "code:"+code);
									if(code.equals("200")){
										RecommendUserBean recommenduserbean = JsonDocHelper
												.toJSONObject(response
														.getJSONObject("success")
														.toString(),
														RecommendUserBean.class);
										if (recommenduserbean != null) {
											if (page == 1) {
												userbean.clear();
											}
											
											userbean.addAll(recommenduserbean.users);
											if (adapter == null) {
												adapter = new SeachResultAdapter(
														SearchResultActivity.this, userbean
														,recommenduserbean.getPic_server());
												lv_searchresult.setAdapter(adapter);
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
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (contanttype.equals(Constant.USERBYNIKE)) {
			searchParams.put("username", name);
			searchParams.put("pageindex", page);

			try {
				RequestParams params = null;
				try {
					params = PeerParamsUtils.getSearchUserByNikeParams(this, name, page,mShareFileUtils.getString(Constant.CLIENT_ID, ""),byDistance);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}


				HttpUtil.post(this, HttpConfig.SEARCH_USER_NICK_URL, params,
						 new JsonHttpResponseHandler() {

							@Override
							public void onFailure(int statusCode,
									Header[] headers, String responseString,
									Throwable throwable) {
								// TODO Auto-generated method stub

								hideLoading();
								showToast(getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
								super.onFailure(statusCode, headers,
										responseString, throwable);
							}

							@Override
							public void onFailure(int statusCode,
									Header[] headers, Throwable throwable,
									JSONArray errorResponse) {
								// TODO Auto-generated method stub
								hideLoading();
								showToast(getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
								super.onFailure(statusCode, headers, throwable,
										errorResponse);
							}

							@Override
							public void onFailure(int statusCode,
									Header[] headers, Throwable throwable,
									JSONObject errorResponse) {
								// TODO Auto-generated method stub
								hideLoading();
								showToast(getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
								super.onFailure(statusCode, headers, throwable,
										errorResponse);
							}

							@Override
							public void onSuccess(int statusCode,
									Header[] headers, JSONObject response) {
								// TODO Auto-generated method stub
								hideLoading();

								try {
									JSONObject result = response.getJSONObject("success");

									String code = result.getString("code");
									pLog.i("test", "code:"+code);
									if(code.equals("200")){
										RecommendUserBean recommenduserbean = JsonDocHelper
												.toJSONObject(response
														.getJSONObject("success")
														.toString(),
														RecommendUserBean.class);
										if (recommenduserbean != null) {
											if (page == 1) {
												userbean.clear();
											}
											pLog.i("test", "user1:"
													+ recommenduserbean.users
													.get(0).getUsername()
													.toString());
											userbean.addAll(recommenduserbean.users);
											if (adapter == null) {
												adapter = new SeachResultAdapter(
														SearchResultActivity.this, userbean
														,recommenduserbean.getPic_server());
												lv_searchresult.setAdapter(adapter);
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
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (contanttype.equals(Constant.TOPICBYLABEL)) {
			searchParams.put("label_name", name);
			searchParams.put("pageindex", page);

			try {
				RequestParams params = null;
				try {
					params = PeerParamsUtils.getSearchTopicByLabelParams(this, name, page,mShareFileUtils.getString(Constant.CLIENT_ID, ""));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				HttpUtil.post(this, HttpConfig.SEARCH_TOPIC_LABEL_URL, params,
						 new JsonHttpResponseHandler() {

							@Override
							public void onFailure(int statusCode,
									Header[] headers, String responseString,
									Throwable throwable) {
								// TODO Auto-generated method stub
								hideLoading();
								showToast(getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
								super.onFailure(statusCode, headers,
										responseString, throwable);
							}

							@Override
							public void onFailure(int statusCode,
									Header[] headers, Throwable throwable,
									JSONArray errorResponse) {
								// TODO Auto-generated method stub
								showToast(getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
								super.onFailure(statusCode, headers, throwable,
										errorResponse);
							}

							@Override
							public void onFailure(int statusCode,
									Header[] headers, Throwable throwable,
									JSONObject errorResponse) {
								// TODO Auto-generated method stub
								hideLoading();
								showToast(getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
								super.onFailure(statusCode, headers, throwable,
										errorResponse);
							}

							@Override
							public void onSuccess(int statusCode,
									Header[] headers, JSONObject response) {
								// TODO Auto-generated method stub
								try {
									JSONObject result = response.getJSONObject("success");

									String code = result.getString("code");
									pLog.i("test", "code:"+code);
									if(code.equals("200")){
										RecommendTopicBean recommendtopicbean = JsonDocHelper
												.toJSONObject(response
														.getJSONObject("success")
														.toString(),
														RecommendTopicBean.class);
										if (recommendtopicbean != null) {
											if (page == 1) {
												topicbean.clear();
											}
											topicbean.addAll(recommendtopicbean.topics);
											if (adapter1 == null) {
												adapter1 = new SearchTopicAdapter(
														SearchResultActivity.this, topicbean
														,recommendtopicbean.getPic_server());
												lv_searchresult.setAdapter(adapter1);
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
				
				RequestParams params = null;
				try {
					params = PeerParamsUtils.getSearchTopicBySubjectParams(this, name, page,mShareFileUtils.getString(Constant.CLIENT_ID, ""));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				HttpUtil.post(this, HttpConfig.SEARCH_TOPIC_SUBJECT_URL,
						params, new JsonHttpResponseHandler() {

							@Override
							public void onFailure(int statusCode,
									Header[] headers, String responseString,
									Throwable throwable) {
								// TODO Auto-generated method stub
								hideLoading();
								showToast(getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
								super.onFailure(statusCode, headers,
										responseString, throwable);
							}

							@Override
							public void onFailure(int statusCode,
									Header[] headers, Throwable throwable,
									JSONArray errorResponse) {
								// TODO Auto-generated method stub
								hideLoading();
								showToast(getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
								super.onFailure(statusCode, headers, throwable,
										errorResponse);
							}

							@Override
							public void onFailure(int statusCode,
									Header[] headers, Throwable throwable,
									JSONObject errorResponse) {
								// TODO Auto-generated method stub
								hideLoading();
								showToast(getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
								super.onFailure(statusCode, headers, throwable,
										errorResponse);
							}

							@Override
							public void onSuccess(int statusCode,
									Header[] headers, JSONObject response) {
								// TODO Auto-generated method stub
								hideLoading();
								try {
									JSONObject result = response.getJSONObject("success");

									String code = result.getString("code");
									pLog.i("test", "code:"+code);
									if(code.equals("200")){
										RecommendTopicBean recommendtopicbean = JsonDocHelper
												.toJSONObject(response
														.getJSONObject("success")
														.toString(),
														RecommendTopicBean.class);
										if (recommendtopicbean != null) {
											if (page == 1) {
												topicbean.clear();
											}
											pLog.i("test", "user1:"
													+ recommendtopicbean.topics
													.get(0).getSubject()
													.toString());
											topicbean.addAll(recommendtopicbean.topics);
											if (adapter1 == null) {
												adapter1 = new SearchTopicAdapter(
														SearchResultActivity.this, topicbean
														,recommendtopicbean.getPic_server());
												lv_searchresult.setAdapter(adapter1);
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
			lv_searchresult.postDelayed(new Runnable() {

	            @Override
	            public void run() {
	            	lv_searchresult.onRefreshComplete();
	            }
	        }, 1000);

		} else if (adapter1 != null) {
			adapter1.notifyDataSetChanged();
			lv_searchresult.postDelayed(new Runnable() {

	            @Override
	            public void run() {
	            	lv_searchresult.onRefreshComplete();
	            }
	        }, 1000);
		}

	}
	
	
	/**
	 * 下拉框
	 */
	private void gpspopupwindow() {
		// TODO Auto-generated method stub

		titlePopup.setItemOnClickListener(new OnItemOnClickListener() {
			@Override
			public void onItemClick(ActionItem item, int position) {
				// TODO Auto-generated method stub
				try {
				if (item.mTitle
						.equals(getResources().getString(R.string.littleleave))) {
						HomeFragment.byDistance = true;
					    MainActivity.mLocationClient.start();
					    if(MyLocationListener.w>0&&MyLocationListener.j>0){
								sendgps(mShareFileUtils.getString(Constant.CLIENT_ID, "")
										,MyLocationListener.w,MyLocationListener.j);
							
						}
						sendSearchResult(searchname, page, contanttype, HomeFragment.byDistance);
				} else if (item.mTitle.equals(getResources().getString(
						R.string.tonghang))) {
					HomeFragment.byDistance = false;
					sendSearchResult(searchname, page, contanttype, HomeFragment.byDistance);
				}
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

			}
		});
	}
	
	
	
	/**
	 * 获取用户信息接口
	 * 
	 * @param client_id
	 * @param x_point
	 * @param y_point
	 * @throws UnsupportedEncodingException
	 */
	private void sendgps(String client_id,Double x_point, Double y_point) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		final Intent intent = new Intent();
		RequestParams params = null;
		try {
			params = PeerParamsUtils.getGPSParams(SearchResultActivity.this, x_point,y_point);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpUtil.post(HttpConfig.LOGIN_GPS_URL + client_id + ".json", params,
				new JsonHttpResponseHandler() {
			
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
				pLog.i("test", "response:"+response.toString());
				try {
					JSONObject result = response.getJSONObject("success");
					
					String code = result.getString("code");
					pLog.i("test", "code:"+code);
					if(code.equals("200")){
						MainActivity.mLocationClient.stop();
						pLog.i("test","gps关闭");
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
				
				super.onSuccess(statusCode, headers, response);
				
			}
			
		});
	}
	
	
}
