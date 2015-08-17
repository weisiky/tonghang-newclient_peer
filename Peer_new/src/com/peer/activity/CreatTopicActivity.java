package com.peer.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.peer.R;
import com.peer.IMimplements.easemobchatImp;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.bean.ChatRoomBean;
import com.peer.bean.CreateToipcBean;
import com.peer.bean.LabelBean;
import com.peer.bean.LoginBean;
import com.peer.bean.PersonpageBean;
import com.peer.bean.RecommendUserBean;
import com.peer.bean.UserBean;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.share.ShareConfig;
import com.peer.utils.AutoWrapRadioGroup;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.ManagerActivity;
import com.peer.utils.pLog;
import com.peer.utils.pViewBox;

/**
 * 创建话题
 * 
 */
public class CreatTopicActivity extends pBaseActivity {

	private AutoWrapRadioGroup tag_container;
	private boolean isselect = false;
	private String selectlabel;

	class PageViewList {
		private LinearLayout ll_back, ll_inputtopic;
		private TextView tv_title, tv_newfriends;
		private RelativeLayout rl_mytopic;
		private EditText et_topic;
		private Button bt_creattopic;
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
		pageViewaList.tv_title.setText(getResources().getString(
				R.string.createtopic));

		tag_container = (AutoWrapRadioGroup) findViewById(R.id.tag_container);
		ArrayList<String> labels = JsonDocHelper.toJSONArrary(
				mShareFileUtils.getString(Constant.LABELS, ""), String.class);		
		for (int i = 0; i < labels.size(); i++) {
			RadioButton rb = (RadioButton) getLayoutInflater().inflate(
					R.layout.skillradio, tag_container, false);
			rb.setHeight((int) getResources().getDimension(R.dimen.hight));
			rb.setText(labels.get(i));
			rb.setTextSize(18);
			tag_container.addView(rb);
		}
		

		pageViewaList.bt_creattopic.setEnabled(false);

		pageViewaList.et_topic.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				if (isselect
						&& TextUtils.isEmpty(pageViewaList.et_topic.getText()
								.toString().trim())) {
					pageViewaList.bt_creattopic.setEnabled(false);
				} else {
					pageViewaList.bt_creattopic.setEnabled(true);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				if (isselect
						&& !TextUtils.isEmpty(pageViewaList.et_topic.getText()
								.toString().trim())) {
					pageViewaList.bt_creattopic.setEnabled(true);
				} else {
					pageViewaList.bt_creattopic.setEnabled(false);
				}
			}
		});
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);
		pageViewaList.rl_mytopic.setOnClickListener(this);
		pageViewaList.bt_creattopic.setOnClickListener(this);

		tag_container.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				RadioButton tempButton = (RadioButton) findViewById(checkedId);
				selectlabel = tempButton.getText().toString();
				isselect = true;
				pageViewaList.ll_inputtopic.setVisibility(View.VISIBLE);
				if (isselect
						&& !TextUtils.isEmpty(pageViewaList.et_topic.getText()
								.toString().trim())) {
					pageViewaList.bt_creattopic.setEnabled(true);
				} else {
					pageViewaList.bt_creattopic.setEnabled(false);
				}

			}
		});
	}

	@Override
	protected void processBiz() {
		// TODO Auto-generated method stub

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
		return getLayoutInflater().inflate(R.layout.activity_creattopic, null);
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
		case R.id.bt_creattopic:
			if (isNetworkAvailable) {
				ShareDialog();
			} else {
				showToast(
						getResources()
								.getString(R.string.Broken_network_prompt),
						Toast.LENGTH_SHORT, false);
			}
			break;
		case R.id.rl_mytopic:
//			PersonpageBean.getInstance().setUser(new UserBean());
//			PersonpageBean.getInstance().getUser().setClient_id(mShareFileUtils.getString(Constant.CLIENT_ID, ""));
//			System.out.println("NICKNAME："+mShareFileUtils.getString(Constant.NICKNAME, ""));
			UserBean userbean = new UserBean();
			userbean.setClient_id(mShareFileUtils.getString(Constant.CLIENT_ID, ""));
			pLog.i("test","传过去的client_id"+userbean.getClient_id());
			Intent mytopic = new Intent(CreatTopicActivity.this,
					TopicActivity.class);
			mytopic.putExtra("bean", userbean);
			mytopic.putExtra("image", mShareFileUtils.getString(Constant.IMAGE, ""));
			mytopic.putExtra("nike", mShareFileUtils.getString(Constant.NICKNAME, ""));
			mytopic.putExtra("email", mShareFileUtils.getString(Constant.EMAIL, ""));
			startActivity(mytopic);
			break;
		default:
			break;
		}

	}

	/*
	 * 创建话题时，分享Dialog
	 */
	public void ShareDialog() {
		new AlertDialog.Builder(this)
				.setTitle(getResources().getString(R.string.sharedailog))
				.setMessage(getResources().getString(R.string.isshare))
				.setNegativeButton(getResources().getString(R.string.notshare),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(
									DialogInterface dialoginterface, int i) {
								// TODO Auto-generated method stub
								try {
									sendCreateTopic(
											mShareFileUtils.getString(Constant.CLIENT_ID, ""),
											pageViewaList.et_topic.getText()
													.toString().trim(),
											selectlabel);
								} catch (UnsupportedEncodingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						})
				.setPositiveButton(
						getResources().getString(R.string.sharesure),
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface, int i) {
								ShareConfig shareConfig = new ShareConfig(
										CreatTopicActivity.this);
								shareConfig.shareCircleFriend("", "","同行话题#"+
										pageViewaList.et_topic.getText()
										.toString().trim()+"#", "", "");
								try {
									sendCreateTopic(
											mShareFileUtils.getString(Constant.CLIENT_ID, ""),
											pageViewaList.et_topic.getText()
													.toString().trim(),
											selectlabel);
								} catch (UnsupportedEncodingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							}
						}).show();
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
	 * 创建话题请求
	 * 
	 * @param client_id
	 * @param subject
	 * @param label_name
	 * @throws UnsupportedEncodingException
	 */
	private void sendCreateTopic(final String client_id, String subject,
			String label_name) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub

		// HttpEntity entity = null;
		// try {
		// entity = PeerParamsUtils.getcreatetopicParams(this, client_id,
		// subject, label_name);
		// } catch (Exception e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }

		RequestParams params = null;
		try {
			params = PeerParamsUtils.getcreatetopicParams(this, client_id,
					subject, label_name);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		HttpUtil.post(this,HttpConfig.TOPIC_CREATE_URL,params, new JsonHttpResponseHandler() {

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
						pLog.i("test", "response:"+response.toString());

						try {
							JSONObject result = response.getJSONObject("success");

							String code = result.getString("code");
							pLog.i("test", "code:"+code);
							if(code.equals("200")){
								CreateToipcBean createtopicbean = JsonDocHelper
										.toJSONObject(
												response.getJSONObject("success")
												.toString(),
												CreateToipcBean.class);
								if (createtopicbean != null) {
									if (EMChatManager.getInstance().isConnected()) {
										
									}else{
										easemobchatImp.getInstance().login(
												mShareFileUtils.getString(Constant.CLIENT_ID, "")
												.replace("-", ""),
												mShareFileUtils.getString(
														Constant.PASSWORD, ""));
									}
									//从环信服务器获取自己加入的和创建的群聊列表
									//需异步处理
									List<EMGroup> grouplist = EMGroupManager.getInstance().getGroupsFromServer();
									for(int i= 0 ;i<grouplist.size();i++ ){
										pLog.i("test","grouplist.get(0):"+grouplist.get(i));
										pLog.i("test","getGroupId():"+grouplist.get(i).getGroupId());
										pLog.i("test","getOwner():"+grouplist.get(i).getOwner());
										if(client_id.equals(grouplist.get(i).getOwner())){
											sendleavegroup(client_id
													, grouplist.get(i).getGroupId()
													,true);
										}else{
											sendleavegroup(client_id
													 , grouplist.get(i).getGroupId()
													 ,false);
											easemobchatImp.getInstance().exitgroup(grouplist.get(i).getGroupId());
										}
									}
										ChatRoomBean.getInstance().setChatroomtype(Constant.MULTICHAT);
										ChatRoomBean.getInstance().setIsowner(true);
										ChatRoomBean.getInstance().setTopicBean(
												createtopicbean.getTopic());
										Intent intent = new Intent();
										startActivityForLeft(MultiChatRoomActivity.class,
												intent, false);
										ManagerActivity.getAppManager().finishActivity(
												CreatTopicActivity.this);
										pageViewaList.et_topic.setText("");
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
	
	
	/**
	 * 退出话题请求
	 * 
	 * @param client_id
	 * @param topic_id
	 * @exception UnsupportedEncodingException
	 */
	private void sendleavegroup(String client_id, String topic_id , boolean isOwner) {
		// TODO Auto-generated method stub
		final Intent intent = new Intent();
		RequestParams params = null;
		try {
			params = PeerParamsUtils.getJoinParams(CreatTopicActivity.this,
					client_id, topic_id,isOwner);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpUtil.post(HttpConfig.LEAVE_TOPIC_IN_URL, params,
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub

						super.onFailure(statusCode, headers, responseString,
								throwable);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONArray errorResponse) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
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
							String code = String.valueOf(result.get("code"));
							pLog.i("test", "code:"+code);
							if (code.equals("200")) {
								showToast("正在进入新话题...", Toast.LENGTH_SHORT, false);
							}else if(code.equals("500")){
								
							}else{
								String message = result.getString("message");
								showToast(message, Toast.LENGTH_SHORT, false);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						super.onSuccess(statusCode, headers, response);

					}

				});
	}

}
