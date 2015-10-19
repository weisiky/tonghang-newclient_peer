package com.peer.fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import net.google.niofile.br.AdSize;
import net.google.niofile.br.AdView;
import net.google.niofile.br.AdViewListener;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.DateUtils;
import android.util.Log;
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
import com.peer.utils.BussinessUtils;
import com.peer.utils.DeleteFolder;
import com.peer.utils.ImageLoaderUtil;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.pIOUitls;
import com.peer.utils.pLog;
import com.peer.utils.zManifestInfoUtils;
import com.peer.webview.WebViewActivity;

/**
 * 主Fragment
 */
public class HomeFragment extends pBaseFragment{

	HomepageAdapter adapter;

	private RelativeLayout rl_search;
	public List<UserBean> usersList = new ArrayList<UserBean>();
	private PullToRefreshListView pull_refresh_homepage;
	public LinearLayout base_neterror_item,ll_downview;
	private FrameLayout banner_view;
	private TextView tv_connect_errormsg;
	private TitlePopup titlePopup;
	private ProgressDialog pd;
//	private AppemsBanner mBannerView;
	private ImageView self_view,cancel,iv_neterror;
//	LayoutParams params = null;
	private boolean t_page = true;
	public static boolean byDistance = false ;
	/** 控制广告的出现与消失 **/
	public static String adv = "true";
	/** 分页参数 **/
	int page = 1;
	public static int pageindex;

	private pBaseActivity pbaseActivity;
	
//	String Adwo_PID = "a26c321e3a19461199ccdb4b2e67fcad";
	
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
									sendRecommendTask(mShareFileUtils.getString(
											Constant.CLIENT_ID, ""), ++page,byDistance);
								}else{
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
						try {
						JSONObject result = response.getJSONObject("success");

						String code = result.getString("code");
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
								if(pd!=null){
									pd.cancel();
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
		tv_connect_errormsg = (TextView) getView()
				.findViewById(R.id.tv_connect_errormsg);
		iv_neterror =  (ImageView) getView()
				.findViewById(R.id.iv_neterror);
		base_neterror_item =  (LinearLayout) getView().findViewById(
				R.id.base_neterror_item);
		
//		qbanner_view = (RelativeLayout) getView().findViewById(R.id.qbanner_view);

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
			if(adv.equals(Constant.ADV)){
				sendSystemConfig();
			}
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

		update();//判断升级
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
				    	pd = ProgressDialog.show(getActivity(),"", "刷新中...");
				    	
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
					pd = ProgressDialog.show(getActivity(),"", "刷新中...");
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
				try {
					JSONObject result = response.getJSONObject("success");
					
					String code = result.getString("code");
					if(code.equals("200")){
						MainActivity.mLocationClient.stop();
						pLog.i("test","gps关闭");
					}else if(code.equals("500")){
						
					}else{
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
	
	/**
	 * 获取广告信息
	 * 
	 */
	private void sendSystemConfig() {
		HttpEntity entity = null;
		HttpUtil.post(HttpConfig.GET_SYSTEMCONFIG,
				new JsonHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
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
				pLog.i("test", "statusCode:"+statusCode);
				pLog.i("test", "headers:"+headers);
				pLog.i("test", "errorResponse:"+errorResponse);
				pLog.i("test", "throwable:"+throwable);
				super.onFailure(statusCode, headers, throwable,
						errorResponse);
			}

					@SuppressWarnings("static-access")
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						

						pLog.i("test","response:"+response.toString());
						Intent intent = new Intent();
						JSONObject reasult;
						try {
							reasult = response.getJSONObject("success");
						
							mShareFileUtils.setBoolean(
									Constant.USE_ADV,
									reasult.getJSONObject("system").getBoolean(
											"use_adv"));
							mShareFileUtils.setBoolean(
									Constant.THIRD_ADV,
									reasult.getJSONObject("system").getBoolean(
											"third_adv"));
							mShareFileUtils.setString(
									Constant.SELF_ADV_URL,
									reasult.getJSONObject("system").getString(
											"self_adv_url"));
							mShareFileUtils.setString(
									Constant.SELF_IMG,
									reasult.getJSONObject("system").getString(
											"self_img"));

							pLog.i("ban","USE_ADV:"+reasult.getJSONObject("system")
									.getBoolean(
											"use_adv"));
							pLog.i("ban","THIRD_ADV:"+reasult.getJSONObject("system")
									.getBoolean(
											"third_adv"));
							pLog.i("ban","SELF_ADV_URL:"+reasult.getJSONObject("system")
									.getString(
											"self_adv_url"));
							pLog.i("ban","SELF_IMG1:"+reasult.getJSONObject("system")
									.getString(
											"self_img"));
							pLog.i("ban","SELF_IMG2:"+mShareFileUtils.getString(Constant.SELF_IMG, ""));

							adv = "false";
							
							adv();
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						super.onSuccess(statusCode, headers, response);
					}


				});
	}
	
	
	/**
	 * 广告加载
	 * 
	 */
	public void adv(){
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
//				qbanner_view.setVisibility(View.VISIBLE);
				
//				params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
//				
//				// 实例化广告对象
//				AdwoAdView adView = new AdwoAdView(getActivity(), Adwo_PID, true, 20);
//				adView.setBannerMatchScreenWidth(true);
//				
//				// 设置广告监听回调
//				adView.setListener(this);
//				// 把广告条加入界面布局
//				qbanner_view.addView(adView, params);
				
				// 实例化广告条
				AdView adView = new AdView(getActivity(), AdSize.FIT_SCREEN);

				// 获取要嵌入广告条的布局
				LinearLayout adLayout=(LinearLayout)getView().findViewById(R.id.adLayout);

				// 监听广告条接口
				adView.setAdListener(new AdViewListener() {

					@Override
					public void onSwitchedAd(AdView arg0) {
						Log.i("YoumiAdDemo", "广告条切换");
					}

					@Override
					public void onReceivedAd(AdView arg0) {
						Log.i("YoumiAdDemo", "请求广告成功");
					}

					@Override
					public void onFailedToReceivedAd(AdView arg0) {
						Log.i("YoumiAdDemo", "请求广告失败");
					}
				});
				
				// 将广告条加入到布局中
				adLayout.addView(adView);
				
				
				
				// Banner广告
//				mBannerView = (AppemsBanner) getView().findViewById(R.id.qbanner_view);
//				mBannerView.setAccountId("");// 设置开发者的用户唯一标识, 可以选择不设置
//				mBannerView.setTesting(true);// 设置是否在debug
//				mBannerView.isExpand(true);// 设置Banner是否自适应
//				mBannerView.setVisibility(View.VISIBLE);
//				mBannerView.setBannerAdListener(new AppemsBannerAdListener() {
//					
//					@Override
//					public void onBannerLoaded(AppemsBanner arg0) {
//						// TODO Auto-generated method stub
//						
//					}
//					
//					@Override
//					public void onBannerFailed(AppemsBanner arg0, AppemsErrorCode arg1) {
//						// TODO Auto-generated method stub
//						
//					}
//					
//					@Override
//					public void onBannerClicked(AppemsBanner arg0) {
//						// TODO Auto-generated method stub
//						
//					}
//				});
//				mBannerView.init(getActivity(),
//						"d5acfb22d3aab959c3023bc9f182a235");
				
			}else{
				pLog.i("ban","自己的广告");
//				mBannerView = (AppemsBanner) getView().findViewById(R.id.qbanner_view);
//				mBannerView.setVisibility(View.GONE);
				self_view = (ImageView) getView().findViewById(R.id.self_view);
				self_view.setVisibility(View.VISIBLE);
				// ImageLoader加载图片
//				ImageLoaderUtil.getInstance().showHttpImage(
//						mShareFileUtils.getString(Constant.PIC_SERVER, "") 
//						+ mShareFileUtils.getString(Constant.SELF_IMG, ""), self_view,
//						R.drawable.spacer);
				pLog.i("ban","SELF_IMG3:"+mShareFileUtils.getString(Constant.SELF_IMG, ""));
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
	}
	
	
	private void update() {
		// TODO Auto-generated method stub
		
		//删除0.8版本前的本地文件夹
			pLog.i("ban", "DeleteFolder1:"+mShareFileUtils.getBoolean(Constant.tDeleteFolder, true));
				if(mShareFileUtils.getBoolean(Constant.tDeleteFolder, true)){
					DeleteFolder df = new DeleteFolder();
					df.Deletefolder(Constant.C_IMAGE_CACHE_PATH);
					mShareFileUtils.setBoolean(Constant.tDeleteFolder, false);
				}
		
				zManifestInfoUtils utils = new zManifestInfoUtils(getActivity());
				int code_now = utils.getVersionCode();
				String name_now = utils.getVersionName();		//获取当前客户端版本
				int app_code = mShareFileUtils.getInt(Constant.APP_CODE, 0);
		/** 如果没有获取到后台的app_version，则不做升级处理 **/
		if(app_code != 0){
			if(code_now < app_code){
				
				//判断强制（0）或者可选（1）
				if(mShareFileUtils.getBoolean(Constant.CAN_UPGRADE_SILENTLY, true)){
					//强制
					((pBaseActivity) getActivity()).showTipsDialog();
				}else{
					//可选
					((pBaseActivity) getActivity()).showselectDialog();
				}
			}
		}else{
			pLog.i("ban", "未获取到后台的app_version");
//			showToast(getResources()
//					.getString(R.string.Broken_network_prompt), Toast.LENGTH_SHORT, false);
		}
	}
	

}
