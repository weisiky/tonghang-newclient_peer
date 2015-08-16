package com.peer.activity;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.peer.R;
import com.peer.adapter.ChatHistoryAdapter;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.bean.ChatRoomBean;
import com.peer.bean.LoginBean;
import com.peer.bean.PersonpageBean;
import com.peer.bean.SearchBean;
import com.peer.bean.UserBean;
import com.peer.fragment.FriendsFragment;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.titlepopwindow.ActionItem;
import com.peer.titlepopwindow.TitlePopup;
import com.peer.titlepopwindow.TitlePopup.OnItemOnClickListener;
import com.peer.utils.AutoWrapRadioGroup;
import com.peer.utils.BussinessUtils;
import com.peer.utils.ChatRoomTypeUtil;
import com.peer.utils.ImageLoaderUtil;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.pLog;
import com.peer.utils.pViewBox;

/**
 * 用户详细信息类 包括‘我的’、‘他的’、‘她的’
 * 
 */
public class PersonalPageActivity extends pBaseActivity {

	private AutoWrapRadioGroup tag_container;
	private TitlePopup titlePopup;
	private boolean page = true;
	private RadioButton skill;

	
//	private static UserBean userbean;
	final UserBean userbean = PersonpageBean.getInstance().getUser();
	
	

	class PageViewList {
		private LinearLayout ll_back, ll_personpagebottom, contentauto,ll_downview;
		private TextView tv_title, personnike, personcount, sex, city,
				tv_topic;
		private Button btnSend, addfriends;
		private ImageView personhead;
		private RelativeLayout rl_topic;
	}

	private PageViewList pageViewaList;
	private String pic_server;

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

		tag_container = (AutoWrapRadioGroup) findViewById(R.id.tag_container);
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);
		pageViewaList.rl_topic.setOnClickListener(this);
		pageViewaList.btnSend.setOnClickListener(this);
		pageViewaList.addfriends.setOnClickListener(this);
		pageViewaList.ll_downview.setOnClickListener(this);
		tag_container.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				RadioButton tempButton = (RadioButton) findViewById(checkedId);
				String selectlabel = tempButton.getText().toString();
				if(isNetworkAvailable){
					SearchBean.getInstance().setSearchname(selectlabel);
					SearchBean.getInstance().setSearchtype(Constant.USERBYLABEL);
					Intent intent = new Intent();
					startActivityForLeft(SearchResultActivity.class, intent, false);
				}else{
					showToast(getResources().getString(R.string.Broken_network_prompt)
							, Toast.LENGTH_SHORT, false);
				}
			}
		});

	}

	@Override
	protected void processBiz() {
		// TODO Auto-generated method stub
		pic_server = mShareFileUtils.getString(Constant.PIC_SERVER, "");
		ViewType();

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
				.inflate(R.layout.activity_personalpage, null);
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
		case R.id.rl_topic:
			if (isNetworkAvailable) {
				Intent intent = new Intent(PersonalPageActivity.this,
						TopicActivity.class);
				intent.putExtra("bean", userbean);
				intent.putExtra("client_id", userbean.getClient_id());
				intent.putExtra("image", userbean.getImage());
				intent.putExtra("nike", userbean.getUsername());
				intent.putExtra("email", userbean.getEmail());
				startActivity(intent);
			} else {
				showToast(
						getResources()
								.getString(R.string.Broken_network_prompt),
						Toast.LENGTH_SHORT, false);
			}
			break;
		case R.id.ll_downview:
			titlePopup.show(v);
			break;
		default:
			break;
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

	private void ViewType() {
		// TODO Auto-generated method stub
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.weight = 1;
		params.gravity = Gravity.CENTER_VERTICAL;
		String userclient_id = mShareFileUtils
				.getString(Constant.CLIENT_ID, "");

		if (userclient_id.equals(PersonpageBean.getInstance().getUser()
				.getClient_id())) {
			MypersonPage();
		} else if (PersonpageBean.getInstance().getUser().getIs_friend()) {
			friendsPersonPage();
		} else {
			unfriendsPersonPage();
		}

	}

	/**
	 * 当前用户的个人信息展示页
	 */
	public void MypersonPage() {
		pageViewaList.tv_title.setText(getResources().getString(
				R.string.personalpage_own));
		pageViewaList.tv_topic.setText(getResources().getString(
				R.string.topic_owen));
		pageViewaList.ll_personpagebottom.setVisibility(View.INVISIBLE);

		
			
//		File file = new File(Constant.C_IMAGE_CACHE_PATH + "head.png");// 将要保存图片的路径
//		if (file.exists()) {
			pageViewaList.personhead.setImageBitmap(BussinessUtils.decodeFile(
					Constant.C_IMAGE_CACHE_PATH + "head.png", 100));
			ImageLoaderUtil.getInstance().showHttpImage(
					pic_server + mShareFileUtils.getString(Constant.IMAGE,"")
					, pageViewaList.personhead,
					R.drawable.mini_avatar_shadow);

//		} else {
//			// ImageLoader加载图片
//			ImageLoaderUtil.getInstance().showHttpImage(
//					pic_server + userbean.getImage(), pageViewaList.personhead,
//					R.drawable.mini_avatar_shadow);
//		}
		

		pageViewaList.personnike.setText(userbean.getUsername());
//		pageViewaList.personcount.setText(userbean.getEmail());
		pageViewaList.city.setText(userbean.getCity());
		pageViewaList.sex.setText(userbean.getSex());
		ArrayList<String> lables = userbean.getLabels();
		
		for (int i = 0; i < lables.size(); i++) {
			String tag = lables.get(i);
			pLog.i("test", "tag:"+tag);
			skill = (RadioButton) getLayoutInflater().inflate(R.layout.skill,
					tag_container, false);
			skill.setHeight((int) getResources().getDimension(R.dimen.hight));
			skill.setTextSize(20);
			skill.setTextColor(getResources().getColor(R.color.white));
			int pading = (int) getResources().getDimension(R.dimen.pading);
			skill.setText(tag);
			skill.setTag("" + i);
			tag_container.addView(skill);
		}
	}

	/**
	 * 
	 * 与当前用户非好友的用户展示
	 * 
	 */
	private void unfriendsPersonPage() {

		if (userbean.getSex() == null
				|| userbean.getSex().toString().equals("男")) {
			pageViewaList.tv_topic.setText(getResources().getString(
					R.string.topic_other));
			pageViewaList.tv_title.setText(getResources().getString(
					R.string.personalpage_other));
		} else {
			pageViewaList.tv_topic.setText(getResources().getString(
					R.string.topic_nvother));
			pageViewaList.tv_title.setText(getResources().getString(
					R.string.personalpage_nvother));
		}

		pageViewaList.personnike.setText(userbean.getUsername());
//		pageViewaList.personcount.setText(userbean.getEmail());
		pageViewaList.city.setText(userbean.getCity());
		pageViewaList.sex.setText(userbean.getSex());
		ImageLoaderUtil.getInstance().showHttpImage(
				pic_server + userbean.getImage(), pageViewaList.personhead,
				R.drawable.mini_avatar_shadow);
		ArrayList<String> lables = userbean.getLabels();
		
		for (int i = 0; i < lables.size(); i++) {
			String tag = lables.get(i);
			pLog.i("test", "tag:"+tag);
			skill = (RadioButton) getLayoutInflater().inflate(R.layout.skill,
					tag_container, false);
			skill.setHeight((int) getResources().getDimension(R.dimen.hight));
			skill.setTextSize(20);
			skill.setTextColor(getResources().getColor(R.color.white));
			int pading = (int) getResources().getDimension(R.dimen.pading);
			skill.setText(tag);
			skill.setTag("" + i);
			tag_container.addView(skill);
		}
		if(userbean.getHas_invitation()){
			pageViewaList.addfriends.setText("等待中..");
			pageViewaList.addfriends.setEnabled(false);
		}else{
			pageViewaList.addfriends.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
						Intent intent = new Intent();
						intent.putExtra("user_client_id", userbean.getClient_id());
						intent.putExtra("image", userbean.getImage());
						intent.putExtra("nike", userbean.getUsername());
						intent.putExtra("email", userbean.getEmail());
						startActivityForLeft(AddFriendsActivity.class,
								intent, false);
				}
			});
		}
		pageViewaList.btnSend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				if(isNetworkAvailable){
					ChatRoomBean.getInstance().setChatroomtype(
							Constant.SINGLECHAT);
//				ChatRoomBean.getInstance().setUserBean(userbean);
					Intent intent = new Intent(PersonalPageActivity.this,
							SingleChatRoomActivity.class);
					intent.putExtra("userbean", userbean);
					startActivity(intent);
//				}else{
//					showToast(getResources().getString(R.string.Broken_network_prompt)
//							, Toast.LENGTH_SHORT, false);
//				}
			}
		});
		
	}

	/**
	 * 显示好友的个人主页
	 * 
	 */
	private void friendsPersonPage() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.weight = 1;
		params.gravity = Gravity.CENTER_VERTICAL;
		String userid = null;

		pageViewaList.ll_downview.setVisibility(View.VISIBLE);
		pageViewaList.btnSend.setVisibility(View.GONE);
		pageViewaList.addfriends.setVisibility(View.GONE);
		pageViewaList.personnike.setText(userbean.getUsername());
//		pageViewaList.personcount.setText(userbean.getEmail());
		pageViewaList.city.setText(userbean.getCity());
		pageViewaList.sex.setText(userbean.getSex());
		ImageLoaderUtil.getInstance().showHttpImage(
				pic_server + userbean.getImage(), pageViewaList.personhead,
				R.drawable.mini_avatar_shadow);

		ArrayList<String> lables = userbean.getLabels();
		
		for (int i = 0; i < lables.size(); i++) {
			String tag = lables.get(i);
			pLog.i("test", "tag:"+tag);
			skill = (RadioButton) getLayoutInflater().inflate(R.layout.skill,
					tag_container, false);
			skill.setHeight((int) getResources().getDimension(R.dimen.hight));
			skill.setTextSize(20);
			skill.setTextColor(getResources().getColor(R.color.white));
			int pading = (int) getResources().getDimension(R.dimen.pading);
			skill.setText(tag);
			skill.setTag("" + i);
			tag_container.addView(skill);
		}
		titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT ,page);	
		
		titlePopup.addAction(new ActionItem(this, getResources().getString(
				R.string.deletefriends), R.color.white));
		popupwindow();
		if (userbean.getSex().equals("男")) {
			pageViewaList.tv_topic.setText(getResources().getString(
					R.string.topic_other));
			pageViewaList.tv_title.setText(getResources().getString(
					R.string.personalpage_other));
		} else {
			pageViewaList.tv_topic.setText(getResources().getString(
					R.string.topic_nvother));
			pageViewaList.tv_title.setText(getResources().getString(
					R.string.personalpage_nvother));
		}
		Button bt3 = new Button(this);
		bt3.setText(getResources().getString(R.string.sendmsg));
		bt3.setTextColor(getResources().getColor(R.color.white));
		bt3.setBackgroundResource(R.drawable.select_personal);
		bt3.setLayoutParams(params);
		pageViewaList.ll_personpagebottom.addView(bt3);
		bt3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ChatRoomBean.getInstance().setChatroomtype(
						Constant.SINGLECHAT);
//				ChatRoomBean.getInstance().setUserBean(userbean);
				Intent intent = new Intent(PersonalPageActivity.this,
						SingleChatRoomActivity.class);
				intent.putExtra("userbean", userbean);
				startActivity(intent);
			}
		});
	}

	private void popupwindow() {
		// TODO Auto-generated method stub

		titlePopup.setItemOnClickListener(new OnItemOnClickListener() {

			@Override
			public void onItemClick(ActionItem item, int position) {
				// TODO Auto-generated method stub
				if (item.mTitle.equals(getResources().getString(
						R.string.deletefriends))) {
					if(isNetworkAvailable){
						String client_id = mShareFileUtils.getString(
								Constant.CLIENT_ID, "");
						String friend_id = userbean.getClient_id();
						
						senddeletefriend(client_id, friend_id);
					}else{
						showToast(getResources().getString(R.id.baseProgressBarLayout)
								, Toast.LENGTH_SHORT, false);
					}
				}
			}
		});
	}

	/**
	 * 删除好友请求
	 * 
	 * @param client_id
	 * @param friend_id
	 * @throws UnsupportedEncodingException
	 */
	private void senddeletefriend(String client_id, String friend_id) {
		// HttpEntity entity = null;
		// try {
		// entity = PeerParamsUtils.getdeletefriendParams(this, friend_id);
		// } catch (Exception e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		RequestParams params = null;
		try {
			params = PeerParamsUtils.getdeletefriendParams(this, friend_id);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		HttpUtil.post(this, HttpConfig.FRIEND_DELETE_URL + client_id + ".json",
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
								showToast("好友关系已经移除！", Toast.LENGTH_SHORT,
										false);
								FriendsFragment.refreshhandle.sendEmptyMessage(Constant.REFRESHHANDLE);
								finish();
							}else if(code.equals("500")){
								
							}else{
								String message = result.getString("message");
								showToast(message, Toast.LENGTH_SHORT, false);
							}

						} catch (JSONException e) {
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
	 * @throws UnsupportedEncodingException
	 */

	private void senduser(String client_id,String o_client_id) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		final Intent intent = new Intent();
		RequestParams params = null;
		try {
			params = PeerParamsUtils.getUserParams(PersonalPageActivity.this, client_id);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpUtil.post(HttpConfig.USER_IN_URL + client_id + ".json?client_id="+o_client_id, params,
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
						pLog.i("test","response:"+response.toString());
						try {
							JSONObject result = response.getJSONObject("success");

							String code = result.getString("code");
							pLog.i("test", "code:"+code);
							if(code.equals("200")){
								LoginBean loginBean = JsonDocHelper.toJSONObject(
										response.getJSONObject("success")
										.toString(), LoginBean.class);
								if(loginBean!=null){
//								userbean = loginBean.user;
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

						super.onSuccess(statusCode, headers, response);

					}

				});
	}

}
