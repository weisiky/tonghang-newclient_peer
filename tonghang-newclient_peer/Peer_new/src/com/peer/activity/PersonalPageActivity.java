package com.peer.activity;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
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
import com.peer.IMimplements.easemobchatImp;
import com.peer.adapter.ChatHistoryAdapter;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.bean.ChatRoomBean;
import com.peer.bean.FindCardBean;
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
import com.peer.utils.ManagerActivity;
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

	// private static UserBean userbean;
	final UserBean userbean = PersonpageBean.getInstance().getUser();

	class PageViewList {
		private LinearLayout ll_back, ll_personpagebottom, ll_contentauto,
				ll_downview;
		private TextView tv_title, personnike, personcount, city,tv_change_card;
		private Button btnSend, addfriends;
		private ImageView personhead,img_sex_b,img_sex_w;
//		private View view_line;
//		private RelativeLayout rl_topic;
	}

	private PageViewList pageViewaList;
	private String pic_server;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personalpage_othernew);
		findViewById();
		setListener();
		processBiz();
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
//		pageViewaList.rl_topic.setOnClickListener(this);
		pageViewaList.btnSend.setOnClickListener(this);
		pageViewaList.addfriends.setOnClickListener(this);
		pageViewaList.ll_downview.setOnClickListener(this);
		pageViewaList.ll_contentauto.setOnClickListener(this);
		tag_container.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				RadioButton tempButton = (RadioButton) findViewById(checkedId);
				String selectlabel = tempButton.getText().toString();
				if (isNetworkAvailable) {
					SearchBean.getInstance().setSearchname(selectlabel);
					SearchBean.getInstance()
							.setSearchtype(Constant.USERBYLABEL);
					Intent intent = new Intent();
					startActivityForLeft(SearchResultActivity.class, intent,
							false);
				} else {
					showToast(
							getResources().getString(
									R.string.Broken_network_prompt),
							Toast.LENGTH_SHORT, false);
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
//		case R.id.rl_topic:
//			if (isNetworkAvailable) {
//				Intent intent = new Intent(PersonalPageActivity.this,
//						TopicActivity.class);
//				intent.putExtra("bean", userbean);
//				intent.putExtra("client_id", userbean.getClient_id());
//				intent.putExtra("image", userbean.getImage());
//				intent.putExtra("nike", userbean.getUsername());
//				intent.putExtra("email", userbean.getEmail());
//				startActivity(intent);
//			} else {
//				showToast(
//						getResources()
//								.getString(R.string.Broken_network_prompt),
//						Toast.LENGTH_SHORT, false);
//			}
//			break;
		case R.id.ll_downview:
			titlePopup.show(v);
			break;
		case R.id.ll_contentauto:
			try {
				sendcard(mShareFileUtils.getString(Constant.CLIENT_ID, ""));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			break;
		default:
			break;
		}

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

	private void ViewType() {
		// TODO Auto-generated method stub
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.weight = 1;
		params.gravity = Gravity.CENTER_VERTICAL;
		titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, page);
		String userclient_id = mShareFileUtils
				.getString(Constant.CLIENT_ID, "");

		if (userclient_id.equals(PersonpageBean.getInstance().getUser()
				.getClient_id())) {
			pLog.i("test", "111");
			MypersonPage();
		} else if (PersonpageBean.getInstance().getUser().getIs_friend()) {
			pLog.i("test", "222");
			friendsPersonPage();
		} else {
			pLog.i("test", "333");
			unfriendsPersonPage();
		}

	}

	/**
	 * 当前用户的个人信息展示页
	 */
	public void MypersonPage() {
		pageViewaList.tv_title.setText(getResources().getString(
				R.string.personalpage_own));
//		pageViewaList.tv_topic.setText(getResources().getString(
//				R.string.topic_owen));
		pageViewaList.ll_personpagebottom.setVisibility(View.GONE);
//		pageViewaList.view_line.setVisibility(View.GONE);

		// File file = new File(Constant.C_IMAGE_CACHE_PATH + "head.png");//
		// 将要保存图片的路径
		// if (file.exists()) {
		pageViewaList.personhead.setImageBitmap(BussinessUtils.decodeFile(
				Constant.C_IMAGE_CACHE_PATH + "head.png", 100));
		ImageLoaderUtil.getInstance().showHttpImage(this,
				pic_server + mShareFileUtils.getString(Constant.IMAGE, ""),
				pageViewaList.personhead, R.drawable.mini_avatar_shadow);

		// } else {
		// // ImageLoader加载图片
		// ImageLoaderUtil.getInstance().showHttpImage(
		// pic_server + userbean.getImage(), pageViewaList.personhead,
		// R.drawable.mini_avatar_shadow);
		// }

		pageViewaList.personnike.setText(userbean.getUsername());
		pageViewaList.tv_change_card.setText("查看名片");
		// pageViewaList.personcount.setText(userbean.getEmail());
		pageViewaList.city.setText(userbean.getCity());
//		pageViewaList.sex.setText(userbean.getSex());
		String sex = userbean.getSex();
		if(sex.equals("男")){
			pageViewaList.img_sex_b.setVisibility(View.VISIBLE);
		}else{
			pageViewaList.img_sex_w.setVisibility(View.VISIBLE);
		}
		
		ArrayList<String> lables = userbean.getLabels();

		for (int i = 0; i < lables.size(); i++) {
			String tag = lables.get(i).trim();
			skill = (RadioButton) getLayoutInflater().inflate(R.layout.skill,
					tag_container, false);
			skill.setHeight((int) getResources().getDimension(R.dimen.hight));
//			skill.setTextSize(20);
//			skill.setTextColor(getResources().getColor(R.color.white));
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

	//		if (userbean.getSex() == null
	//		|| userbean.getSex().toString().equals("男")) {
	//	pageViewaList.tv_topic.setText(getResources().getString(
	//			R.string.topic_other));
		pageViewaList.tv_title.setText(getResources().getString(
				R.string.personalpage));
	//} else {
	//	pageViewaList.tv_topic.setText(getResources().getString(
	//			R.string.topic_nvother));
	//	pageViewaList.tv_title.setText(getResources().getString(
	//			R.string.personalpage_nvother));
	//}

		pageViewaList.ll_downview.setVisibility(View.VISIBLE);
		pageViewaList.personnike.setText(userbean.getUsername());
		// pageViewaList.personcount.setText(userbean.getEmail());
		pageViewaList.city.setText(userbean.getCity());
		pageViewaList.tv_change_card.setText("换名片");
//		pageViewaList.sex.setText(userbean.getSex());
		String sex = userbean.getSex();
		if(sex.equals("男")){
			pageViewaList.img_sex_b.setVisibility(View.VISIBLE);
		}else{
			pageViewaList.img_sex_w.setVisibility(View.VISIBLE);
		}
		
		titlePopup.addAction(new ActionItem(this, getResources().getString(
				R.string.blacklist), R.color.white));
		popupwindow();
		ImageLoaderUtil.getInstance().showHttpImage(this,
				pic_server + userbean.getImage(), pageViewaList.personhead,
				R.drawable.mini_avatar_shadow);
		ArrayList<String> lables = userbean.getLabels();

		for (int i = 0; i < lables.size(); i++) {
			String tag = lables.get(i).trim();
			skill = (RadioButton) getLayoutInflater().inflate(R.layout.skill,
					tag_container, false);
			skill.setHeight((int) getResources().getDimension(R.dimen.hight));
//			skill.setTextSize(20);
//			skill.setTextColor(getResources().getColor(R.color.white));
			int pading = (int) getResources().getDimension(R.dimen.pading);
			skill.setText(tag);
			skill.setTag("" + i);
			tag_container.addView(skill);
		}
		if (userbean.getHas_invitation()) {
			pageViewaList.addfriends.setText("等待中..");
			pageViewaList.addfriends.setEnabled(false);
		} else {
			pageViewaList.addfriends
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Intent intent = new Intent();
							intent.putExtra("user_client_id",
									userbean.getClient_id());
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
				// if(isNetworkAvailable){
				ChatRoomBean.getInstance().setChatroomtype(Constant.SINGLECHAT);
				// ChatRoomBean.getInstance().setUserBean(userbean);
				Intent intent = new Intent();
				intent.putExtra("userbean", userbean);
				startActivityForLeft(SingleChatRoomActivity.class, intent,
						false);
				// }else{
				// showToast(getResources().getString(R.string.Broken_network_prompt)
				// , Toast.LENGTH_SHORT, false);
				// }
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
		pageViewaList.tv_change_card.setText("换名片");
		pageViewaList.personnike.setText(userbean.getUsername());
		// pageViewaList.personcount.setText(userbean.getEmail());
		pageViewaList.city.setText(userbean.getCity());
//		pageViewaList.sex.setText(userbean.getSex());
		String sex = userbean.getSex();
		if(sex.equals("男")){
			pageViewaList.img_sex_b.setVisibility(View.VISIBLE);
		}else{
			pageViewaList.img_sex_w.setVisibility(View.VISIBLE);
		}
		
		ImageLoaderUtil.getInstance().showHttpImage(this,
				pic_server + userbean.getImage(), pageViewaList.personhead,
				R.drawable.mini_avatar_shadow);

		ArrayList<String> lables = userbean.getLabels();

		for (int i = 0; i < lables.size(); i++) {
			String tag = lables.get(i).trim();
			skill = (RadioButton) getLayoutInflater().inflate(R.layout.skill,
					tag_container, false);
			skill.setHeight((int) getResources().getDimension(R.dimen.hight));
//			skill.setTextSize(20);
//			skill.setTextColor(getResources().getColor(R.color.white));
			int pading = (int) getResources().getDimension(R.dimen.pading);
			skill.setText(tag);
			skill.setTag("" + i);
			tag_container.addView(skill);
		}

		titlePopup.addAction(new ActionItem(this, getResources().getString(
				R.string.deletefriends), R.color.white));
		titlePopup.addAction(new ActionItem(this, getResources().getString(
				R.string.blacklist), R.color.white));
		popupwindow();
//		if (userbean.getSex().equals("男")) {
//			pageViewaList.tv_topic.setText(getResources().getString(
//					R.string.topic_other));
			pageViewaList.tv_title.setText(getResources().getString(
					R.string.personalpage));
//		} else {
//			pageViewaList.tv_topic.setText(getResources().getString(
//					R.string.topic_nvother));
//			pageViewaList.tv_title.setText(getResources().getString(
//					R.string.personalpage_nvother));
//		}
		Button bt3 = new Button(this);
		bt3.setText(getResources().getString(R.string.sendmsg));
		bt3.setTextColor(getResources().getColor(R.color.new_text));
		bt3.setBackgroundResource(R.drawable.select_personal);
		bt3.setLayoutParams(params);
		pageViewaList.ll_personpagebottom.addView(bt3);
		bt3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ChatRoomBean.getInstance().setChatroomtype(Constant.SINGLECHAT);
				// ChatRoomBean.getInstance().setUserBean(userbean);
				Intent intent = new Intent();
				intent.putExtra("userbean", userbean);
				startActivityForLeft(SingleChatRoomActivity.class, intent,
						false);
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
					if (isNetworkAvailable) {
						String client_id = mShareFileUtils.getString(
								Constant.CLIENT_ID, "");
						String friend_id = userbean.getClient_id();

						senddeletefriend(client_id, friend_id);
					} else {
						showToast(
								getResources().getString(
										R.id.baseProgressBarLayout),
								Toast.LENGTH_SHORT, false);
					}
				} else if (item.mTitle.equals(getResources().getString(
						R.string.blacklist))) {
					if (isNetworkAvailable) {
						try {
							sendaddblacklist(mShareFileUtils.getString(
									Constant.CLIENT_ID, ""), userbean
									.getClient_id());
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						showToast(
								getResources().getString(
										R.string.Broken_network_prompt),
								Toast.LENGTH_SHORT, false);
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
						try {
							JSONObject result = response
									.getJSONObject("success");
							String code = result.getString("code");
							if (code.equals("200")) {
								showToast("好友关系已经移除！", Toast.LENGTH_SHORT,
										false);
								FriendsFragment.refreshhandle
										.sendEmptyMessage(Constant.REFRESHHANDLE);
								finish();
							} else if (code.equals("500")) {

							} else {
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

	private void senduser(String client_id, String o_client_id)
			throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		final Intent intent = new Intent();
		RequestParams params = null;
		try {
			params = PeerParamsUtils.getUserParams(PersonalPageActivity.this,
					client_id);
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
							// userbean = loginBean.user;
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

	/**
	 * 加入黑名单接口
	 * 
	 * @param client_id
	 * @param blocker_id
	 * @throws UnsupportedEncodingException
	 */

	private void sendaddblacklist(String client_id, String blocker_id)
			throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		final Intent intent = new Intent();
		RequestParams params = null;
		try {
			params = PeerParamsUtils
					.getblacklistParams(PersonalPageActivity.this);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpUtil.post(HttpConfig.ADDBLOCK_GET_URL + client_id + "/blocks/"
				+ blocker_id + ".json", params, new JsonHttpResponseHandler() {

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
						showToast("已添加入黑名单", Toast.LENGTH_SHORT, false);
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
	
	
	/**
	 * 获取名片信息接口
	 * 
	 * @param other_id
	 * @throws UnsupportedEncodingException
	 */

	private void sendcard(String other_id)
			throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		final Intent intent = new Intent();
		RequestParams params = null;
		try {
			params = PeerParamsUtils.getCardParams(PersonalPageActivity.this,
					other_id);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpUtil.post(HttpConfig.CARD_IN_URL + other_id + "/get.json"
				, params, new JsonHttpResponseHandler() {

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
						FindCardBean findcardBean = JsonDocHelper.toJSONObject(
								response.getJSONObject("success").toString(),
								FindCardBean.class);
						if (findcardBean != null) {
							mShareFileUtils.setString(Constant.CARD_ADMISSIONTIME, findcardBean.card.getSchool_date());
							mShareFileUtils.setString(Constant.CARD_COMPANY, findcardBean.card.getCompanyname());
							mShareFileUtils.setString(Constant.CARD_REALNAME, findcardBean.card.getRealname());
							mShareFileUtils.setString(Constant.CARD_EMAIL, findcardBean.card.getEmail());
							mShareFileUtils.setString(Constant.CARD_EXPERIENCE, findcardBean.card.getDiploma());
							mShareFileUtils.setString(Constant.CARD_MAJOR, findcardBean.card.getMajor());
							mShareFileUtils.setString(Constant.CARD_PHONE, findcardBean.card.getPhone());
							mShareFileUtils.setString(Constant.CARD_POSITION, findcardBean.card.getPosition());
							mShareFileUtils.setString(Constant.CARD_SCHOOL, findcardBean.card.getSchoolname());
							mShareFileUtils.setString(Constant.CARD_WORKTIME, findcardBean.card.getWork_date());
							
							//传1代表为用户是从个人主页到编辑名片页面
							//传2代表用户是从交换名片进入编辑名片页面
								Intent intent = new Intent(PersonalPageActivity.this, EditCardActivity.class);
								intent.putExtra("flag", "1");
								startActivity(intent);
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
