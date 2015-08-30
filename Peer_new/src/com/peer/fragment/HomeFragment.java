package com.peer.fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appems.AppemsSSP.AppemsBanner;
import com.appems.AppemsSSP.AppemsBannerAdListener;
import com.appems.AppemsSSP.AppemsErrorCode;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.peer.R;
import com.peer.activity.MainActivity;
import com.peer.activity.Recommend_topic;
import com.peer.activity.SearchUserActivity;
import com.peer.adapter.HomepageAdapter;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.base.pBaseFragment;
import com.peer.bean.RecommendUserBean;
import com.peer.bean.SearchBean;
import com.peer.bean.UserBean;
import com.peer.gps.MyLocationListener;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.titlepopwindow.ActionItem;
import com.peer.titlepopwindow.TitlePopup;
import com.peer.titlepopwindow.TitlePopup.OnItemOnClickListener;
import com.peer.utils.ImageLoaderUtil;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.pIOUitls;
import com.peer.utils.pLog;
import com.peer.webview.WebViewActivity;

/**
 * 主Fragment
 */
public class HomeFragment extends pBaseFragment {

	HomepageAdapter adapter;

	private RelativeLayout rl_search;
	public List<UserBean> usersList = new ArrayList<UserBean>();
	private PullToRefreshListView pull_refresh_homepage;
	public LinearLayout base_neterror_item,ll_downview;
	private FrameLayout banner_view;
	private TextView tv_connect_errormsg;
	private TitlePopup titlePopup;
	private AppemsBanner mBannerView;
	private ImageView self_view,cancel;
	private boolean t_page = true;
	public static boolean byDistance = false ;
	/** 分页参数 **/
	int page = 1;
	public static int pageindex;

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
					 if (!pbaseActivity.isNetworkAvailable) {
					 pbaseActivity.showToast(getActivity().getResources().getString(
					 R.string.Broken_network_prompt), Toast.LENGTH_LONG,
					 false);
					 } else {
					pbaseActivity.startActivityForLeft(Recommend_topic.class,
							intent, false);
					 }
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
						if(pbaseActivity.isNetworkAvailable){
							try {
								page = 1;
								sendRecommendTask(mShareFileUtils.getString(
										Constant.CLIENT_ID, ""), page,byDistance);
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}else{
							showToast(getResources().getString(R.string.Broken_network_prompt)
									, Toast.LENGTH_SHORT, false);
							refresh();
						}
					}

					@SuppressWarnings("static-access")
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
						if(pbaseActivity.isNetworkAvailable){
							try {
								if(page==pageindex){
									pLog.i("test", "page:"+page);
									pLog.i("test", "pageindex:"+pageindex);
									
									sendRecommendTask(mShareFileUtils.getString(
											Constant.CLIENT_ID, ""), ++page,byDistance);
								}else{
									pLog.i("test", "page:"+page);
									pLog.i("test", "pageindex:"+pageindex);
									if(pageindex == 0){
										page = 1;
									}
									sendRecommendTask(mShareFileUtils.getString(
											Constant.CLIENT_ID, ""), page,byDistance);
								}
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}else{
							showToast(getResources().getString(R.string.Broken_network_prompt)
									, Toast.LENGTH_SHORT, false);
							refresh();
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
	private void sendRecommendTask(String client_id, final int page , boolean byDistance)
			throws UnsupportedEncodingException {
		// TODO Auto-generated method stub

		// HttpEntity entity = null;
		// try {
		// entity = PeerParamsUtils.getHomeParams(pbaseActivity, client_id,
		// page);
		// } catch (Exception e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }

		RequestParams params = null;
		try {
			params = PeerParamsUtils.getHomeParams(pbaseActivity, client_id,
					page,byDistance);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		HttpUtil.post(getActivity(), HttpConfig.USER_RECOMMEND_IN_URL, params,
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub
						pbaseActivity.hideLoading();
						showToast(getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, responseString,
								throwable);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONArray errorResponse) {
						// TODO Auto-generated method stub
						pbaseActivity.hideLoading();
						showToast(getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						pbaseActivity.hideLoading();
						showToast(getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						pbaseActivity.hideLoading();
						pLog.i("test", "response:"+response.toString());
						try {
						JSONObject result = response.getJSONObject("success");

						String code = result.getString("code");
						pLog.i("test", "code:"+code);
						if(code.equals("200")){
							pIOUitls.saveStrToSD(Constant.C_FILE_CACHE_PATH,
									"home.etag", false, response.getJSONObject("success")
									.toString());
							pageindex = page;
							
							RecommendUserBean recommenduserbean = JsonDocHelper
									.toJSONObject(
											response.getJSONObject("success")
											.toString(),
											RecommendUserBean.class);
							if (recommenduserbean != null) {
								
								if (page == 1) {
									usersList.clear();
								}
								
								usersList.addAll(recommenduserbean.users);
								
								if (adapter == null) {
									adapter = new HomepageAdapter(
											getActivity(), usersList,
											recommenduserbean.getPic_server());
									pull_refresh_homepage.setAdapter(adapter);
								}
							}
						}else if(code.equals("500")){
							
						}else{
							if(pageindex == 0){
								
								pIOUitls.saveStrToSD(Constant.C_FILE_CACHE_PATH,
										"home.etag", false, "");
								usersList.clear();
							}
							if(page == 1){
								pIOUitls.saveStrToSD(Constant.C_FILE_CACHE_PATH,
										"home.etag", false, "");
								usersList.clear();
							}
							pLog.i("test", "usersList:"+usersList.toString());
							if (adapter == null) {
								adapter = new HomepageAdapter(
										getActivity(), usersList
										,mShareFileUtils.getString(Constant.PIC_SERVER, ""));
								pull_refresh_homepage.setAdapter(adapter);
							}
							
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

	}

	private void refresh() {

		if (adapter != null) {
			adapter.notifyDataSetChanged();
			pull_refresh_homepage.postDelayed(new Runnable() {

	            @Override
	            public void run() {
	            	pull_refresh_homepage.onRefreshComplete();
	            }
	        }, 1000);

		}

	}


	@SuppressWarnings("static-access")
	private void init() {
		// TODO Auto-generated method stub
		
		if(mShareFileUtils.getBoolean(
				Constant.USE_ADV,
				true)){
			banner_view =  (FrameLayout) getView().findViewById(R.id.banner_view);
			banner_view.setVisibility(View.VISIBLE);
			
			if(mShareFileUtils.getBoolean(
					Constant.THIRD_ADV,
					true)){
				pLog.i("ban","第三方广告");
				//横幅广告初始化
				// Banner广告
				mBannerView = (AppemsBanner) getView().findViewById(R.id.qbanner_view);
				mBannerView.setAccountId("");// 设置开发者的用户唯一标识, 可以选择不设置
				mBannerView.setTesting(true);// 设置是否在debug
				mBannerView.isExpand(true);// 设置Banner是否自适应
				mBannerView.setVisibility(View.VISIBLE);
				mBannerView.setBannerAdListener(new AppemsBannerAdListener() {
					
					@Override
					public void onBannerLoaded(AppemsBanner arg0) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onBannerFailed(AppemsBanner arg0, AppemsErrorCode arg1) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onBannerClicked(AppemsBanner arg0) {
						// TODO Auto-generated method stub
						
					}
				});
				mBannerView.init(getActivity(),
						"d5acfb22d3aab959c3023bc9f182a235");
				pLog.i("ban","第三方广告");
				
			}else{
				pLog.i("ban","自己的广告");
				mBannerView = (AppemsBanner) getView().findViewById(R.id.qbanner_view);
				mBannerView.setVisibility(View.GONE);
				self_view = (ImageView) getView().findViewById(R.id.self_view);
				self_view.setVisibility(View.VISIBLE);
				// ImageLoader加载图片
//				ImageLoaderUtil.getInstance().showHttpImage(
//						mShareFileUtils.getString(Constant.PIC_SERVER, "") 
//						+ mShareFileUtils.getString(Constant.SELF_IMG, ""), self_view,
//						R.drawable.spacer);
				ImageLoaderUtil.getInstance().showHttpImage(getActivity(),
					mShareFileUtils.getString(Constant.SELF_IMG, ""), self_view,
						R.drawable.spacer);
				self_view.setOnClickListener(this);
				pLog.i("ban","自己的广告");
			}
			
			cancel =  (ImageView) getView().findViewById(R.id.cancel);
			cancel.setOnClickListener(this);
			
			cancel.postDelayed(new Runnable() {

	            @Override
	            public void run() {
	            	cancel.setVisibility(View.VISIBLE);
	            }
	        }, 8*1000);
			
		}else{
			banner_view =  (FrameLayout) getView().findViewById(R.id.banner_view);
			banner_view.setVisibility(View.GONE);
		}
		
		pLog.i("type","密码："+mShareFileUtils.getString(Constant.PASSWORD, ""));
		
		ll_downview =  (LinearLayout) getView().findViewById(R.id.ll_downview);
		titlePopup = new TitlePopup(getActivity(), LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, t_page);
		if(byDistance){
			titlePopup.addAction(new ActionItem(getActivity(), 
					getResources().getString(R.string.littleleave), R.color.white));
			titlePopup.addAction(new ActionItem(getActivity(),
					getResources().getString(R.string.tonghang), R.color.red));
		}else{
			titlePopup.addAction(new ActionItem(getActivity(), 
					getResources().getString(R.string.littleleave), R.color.red));
			titlePopup.addAction(new ActionItem(getActivity(),
					getResources().getString(R.string.tonghang), R.color.white));
		}
		
		ll_downview.setOnClickListener(this);
		popupwindow();
		pull_refresh_homepage = (PullToRefreshListView) getView().findViewById(
				R.id.pull_refresh_homepage);
		base_neterror_item = (LinearLayout) getView().findViewById(
				R.id.base_neterror_item);
		tv_connect_errormsg = (TextView) base_neterror_item
				.findViewById(R.id.tv_connect_errormsg);

		rl_search = (RelativeLayout) getView().findViewById(R.id.rl_search);
		rl_search.setOnClickListener(this);
		String homeCount = pIOUitls.readFileByLines(Constant.C_FILE_CACHE_PATH,
				"home.etag");
		RecommendUserBean recommenduserbean;
		try {
			recommenduserbean = JsonDocHelper.toJSONObject(homeCount,
					RecommendUserBean.class);
			if (recommenduserbean != null) {

				usersList.addAll(recommenduserbean.users);
				if (adapter == null) {
					adapter = new HomepageAdapter(getActivity(), usersList,
							mShareFileUtils.getString(Constant.PIC_SERVER, ""));
				}
				// adapter.setBaseFragment(HomeFragment.this);
				pull_refresh_homepage.setAdapter(adapter);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		if(pbaseActivity.isNetworkAvailable){
			base_neterror_item.setVisibility(View.GONE);
			try {
				sendRecommendTask(
						mShareFileUtils.getString(Constant.CLIENT_ID, ""), page,byDistance);

			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			base_neterror_item.setVisibility(View.VISIBLE);
			
		}
//	if(pbaseActivity.isNetworkAvailable){
		RefreshListner();
//	}else{
//		showToast(getResources().getString(R.string.Broken_network_prompt)
//				, Toast.LENGTH_LONG, false);
//	}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_search:
			Intent search = new Intent(getActivity(), SearchUserActivity.class);
			startActivity(search);
			break;
		case R.id.ll_downview:
			titlePopup.showgps(v);
			break;
		case R.id.self_view:
			Intent webview = new Intent(getActivity(), WebViewActivity.class);
			startActivity(webview);
			break;
		case R.id.cancel:
			banner_view.setVisibility(View.GONE);
			break;
		default:
			break;
		}

	}
	
	
	/**
	 * 下拉框
	 */
	private void popupwindow() {
		// TODO Auto-generated method stub

		titlePopup.setItemOnClickListener(new OnItemOnClickListener() {
			@Override
			public void onItemClick(ActionItem item, int position) {
				// TODO Auto-generated method stub
				try {
				if (item.mTitle
						.equals(getResources().getString(R.string.littleleave))) {
					byDistance = true;
				    MainActivity.mLocationClient.start();
				    if(MyLocationListener.w>0&&MyLocationListener.j>0){
							
							sendgps(mShareFileUtils.getString(Constant.CLIENT_ID, "")
									,MyLocationListener.w,MyLocationListener.j);
						
						
					}
					 
				    	usersList.clear();
				    	pbaseActivity.showProgressBar();
				    	if (adapter == null) {
							adapter = new HomepageAdapter(
									getActivity(), usersList
									,mShareFileUtils.getString(Constant.PIC_SERVER, ""));
							pull_refresh_homepage.setAdapter(adapter);
						}
				    	adapter.notifyDataSetChanged();
				    	
						sendRecommendTask(mShareFileUtils.getString(Constant.CLIENT_ID, "")
								, 1, byDistance);
				} else if (item.mTitle.equals(getResources().getString(
						R.string.tonghang))) {
					byDistance = false;
					
					usersList.clear();
			    	pbaseActivity.showProgressBar();
			    	if (adapter == null) {
						adapter = new HomepageAdapter(
								getActivity(), usersList
								,mShareFileUtils.getString(Constant.PIC_SERVER, ""));
						pull_refresh_homepage.setAdapter(adapter);
					}
			    	adapter.notifyDataSetChanged();
					
					sendRecommendTask(mShareFileUtils.getString(Constant.CLIENT_ID, "")
							, 1, byDistance);
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
			params = PeerParamsUtils.getGPSParams(getActivity(), x_point,y_point);
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
