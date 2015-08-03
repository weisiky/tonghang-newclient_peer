package com.peer.activity;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.RemoteException;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.peer.adapter.FriendsAdapter;
import com.peer.adapter.NewfriendsAdapter;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.bean.NewFriendBean;
import com.peer.bean.RecommendUserBean;
import com.peer.fragment.ComeMsgFragment;
import com.peer.fragment.FriendsFragment;
import com.peer.fragment.HomeFragment;
import com.peer.fragment.MyFragment;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.pIOUitls;
import com.peer.utils.pLog;
import com.peer.utils.pViewBox;
import com.readystatesoftware.viewbadger.BadgeView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

/**
 * mainactivity
 */
public class MainActivity extends pBaseActivity {

	/**
	 * fragment定义
	 */
	private HomeFragment homefragment;
	private ComeMsgFragment comemsgfragment;
	private FriendsFragment friendsfragment;
	private MyFragment myfragment;
	private Fragment[] fragments;

	private int index;
	private int currentTabIndex;
	private int intnewfriendsnum;
	private BadgeView unredmsg,bdnewfriendsnum;

	private PageViewList pageViewaList;

	private String mPageName = "MainActivity";

	class PageViewList {
		/* bottom layout */
		private LinearLayout find, come, my, friends;
		private TextView tv_find, tv_come, tv_my, tv_friends, tv_newfriendsnum,
				showmessgenum;
		private ImageView iv_backfind, iv_backcome, iv_backfriends, iv_backmy;

		public LinearLayout ll_find, ll_come, ll_friends, ll_my;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		init();

		// 考虑到用户流量的限制，目前我们默认在Wi-Fi接入情况下才进行自动提醒。如需要在任意网络环境下都进行更新自动提醒，
		// 则请在update调用之前添加以下代码：
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.update(this);

		// v2.4版本以后的SDK中默认开启了集成检测功能，在调用任意的更新接口后，我们将替您自动检查上述集成过程中2、3两个步骤是否被正确完成。
		// 如果正确完成不会出现任何提示，否则会以如下的toast提示您。
		// 你可以通过调用UmengUpdateAgent.setUpdateCheckConfig(false)来禁用此功能。
//		UmengUpdateAgent.setUpdateCheckConfig(false);

	}

	private void init() {
		// TODO Auto-generated method stub
		/* init fragment */
		homefragment = new HomeFragment();
		comemsgfragment = new ComeMsgFragment();
		friendsfragment = new FriendsFragment();
		myfragment = new MyFragment();
		fragments = new Fragment[] { homefragment, comemsgfragment,
				friendsfragment, myfragment };
		getSupportFragmentManager().beginTransaction()
				.add(R.id.fragment_container, homefragment)
				.add(R.id.fragment_container, comemsgfragment)
				.hide(comemsgfragment)
				.add(R.id.fragment_container, friendsfragment)
				.hide(friendsfragment).add(R.id.fragment_container, myfragment)
				.hide(myfragment).show(homefragment).commit();

		index = 0;
		pageViewaList.tv_find.setTextColor(getResources().getColor(
				R.color.bottomtextblue));
		pageViewaList.iv_backfind.setImageResource(R.drawable.peer_press);
		
//		unredmsg=new BadgeView(this,pageViewaList.showmessgenum);
		bdnewfriendsnum=new BadgeView(this,pageViewaList.tv_newfriendsnum);

	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		pageViewaList = new PageViewList();
		pViewBox.viewBox(this, pageViewaList);

	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processBiz() {
		// TODO Auto-generated method stub

	}

	@Override
	protected View loadTopLayout() {
		// TODO Auto-generated method stub
		// return getLayoutInflater().inflate(R.layout.top_layout, null);
		return null;
	}

	@Override
	protected View loadContentLayout() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected View loadBottomLayout() {
		// TODO Auto-generated method stub
		return getLayoutInflater().inflate(R.layout.activity_frabottom, null);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	public void onTabClicked(View v) {
		// TODO Auto-generated method stub
		pageViewaList.tv_find.setTextColor(getResources().getColor(
				R.color.bottomtextgray));
		pageViewaList.tv_come.setTextColor(getResources().getColor(
				R.color.bottomtextgray));
		pageViewaList.tv_friends.setTextColor(getResources().getColor(
				R.color.bottomtextgray));
		pageViewaList.tv_my.setTextColor(getResources().getColor(
				R.color.bottomtextgray));

		pageViewaList.iv_backfind.setImageResource(R.drawable.peer_nol);
		pageViewaList.iv_backcome.setImageResource(R.drawable.come_mess_nol);
		pageViewaList.iv_backfriends
				.setImageResource(R.drawable.find_label_nol);
		pageViewaList.iv_backmy.setImageResource(R.drawable.mysetting_nol);

		switch (v.getId()) {
		case R.id.ll_find:
			index = 0;
			pageViewaList.tv_find.setTextColor(getResources().getColor(
					R.color.bottomtextblue));
			pageViewaList.iv_backfind.setImageResource(R.drawable.peer_press);
			break;
		case R.id.ll_come:
			index = 1;
			pageViewaList.tv_come.setTextColor(getResources().getColor(
					R.color.bottomtextblue));
			pageViewaList.iv_backcome
					.setImageResource(R.drawable.come_mess_press);
			break;
		case R.id.ll_friends:
			index = 2;
			pageViewaList.tv_friends.setTextColor(getResources().getColor(
					R.color.bottomtextblue));
			pageViewaList.iv_backfriends
					.setImageResource(R.drawable.find_label_press);
			break;
		case R.id.ll_my:
			index = 3;
			pageViewaList.tv_my.setTextColor(getResources().getColor(
					R.color.bottomtextblue));
			pageViewaList.iv_backmy
					.setImageResource(R.drawable.mysetting_press);
			break;
		default:
			break;
		}
		if (currentTabIndex != index) {
			FragmentTransaction trx = getSupportFragmentManager()
					.beginTransaction();
			trx.hide(fragments[currentTabIndex]);
			if (!fragments[index].isAdded()) {
				trx.add(R.id.fragment_container, fragments[index]);
			}
			trx.show(fragments[index]).commit();
		}
		currentTabIndex = index;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(mPageName);
		
		System.out.println("进来了吗？");
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
				// TODO Auto-generated method stub
				try {
					sendnewfriend(mShareFileUtils.getString(Constant.CLIENT_ID, ""));
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
//			}
//		}).start();

	}

	@Override
	public void onNetworkOn() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNetWorkOff() {
		// TODO Auto-generated method stub

	}
	
	
	/**
	 * 获取新朋友请求
	 * 
	 * @param client_id
	 * @throws UnsupportedEncodingException
	 */
	private void sendnewfriend(String client_id) {
		// TODO Auto-generated method stub

//		 RequestParams params = null;
//		 try {
//		 params = PeerParamsUtils.getNewFriendsParams(this, pageindex++);
//		 } catch (Exception e1) {
//		 // TODO Auto-generated catch block
//		 e1.printStackTrace();
//		 }
		
		HttpUtil.post(HttpConfig.FRIEND_INVITATION_URL + client_id + ".json",
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub
						hideLoading();
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
						hideLoading();
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
						hideLoading();
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

						pLog.i("test", "response:" + response.toString());

						try {
							NewFriendBean newfriendbean = JsonDocHelper
									.toJSONObject(
											response.getJSONObject("success")
													.toString(),
											NewFriendBean.class);

							if (newfriendbean != null) {

								pIOUitls.saveStrToSD(Constant.C_FILE_CACHE_PATH,
										"newfriends.etag", false, newfriendbean.getInvitationbean().toString());
								intnewfriendsnum=newfriendbean.getInvitationbean().size();
								System.out.println("intnewfriendsnum:"+intnewfriendsnum);
								friendsfragment.setNewfriendsNum(intnewfriendsnum);
								runOnUiThread(new Runnable() {
									public void run() {
										updatenewfriends();	
									}
								});
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
	
	public void updatenewfriends(){
		if(intnewfriendsnum>0){
			bdnewfriendsnum.setText(String.valueOf(intnewfriendsnum)); 
//			bdnewfriendsnum.setTextSize(11);
//			bdnewfriendsnum.setBadgeMargin(1,1);
			bdnewfriendsnum.show();
		}else{
			bdnewfriendsnum.hide();
		}
	}

}
