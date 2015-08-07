package com.peer.fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMMessage;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.peer.IMimplements.easemobchatUser;
import com.peer.activity.PersonalPageActivity;
import com.peer.R;
import com.peer.adapter.ChatHistoryAdapter;
import com.peer.adapter.HomepageAdapter;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.base.pBaseFragment;
import com.peer.bean.LoginBean;
import com.peer.bean.PersonpageBean;
import com.peer.bean.RecommendUserBean;
import com.peer.bean.UserBean;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.pIOUitls;
import com.peer.utils.pLog;

public class ComeMsgFragment extends pBaseFragment {

	private ListView ListView_come;
	private boolean hidden;
	private List<EMGroup> groups;
	private ChatHistoryAdapter adapter;
	private List<EMConversation> list;
	private List<Map> easemobchatusers = new ArrayList<Map>();
	private List<UserBean> users = new ArrayList<UserBean>();

	private pBaseActivity pbaseActivity;

	private String isnumber = "^\\d+$";// 正则用来匹配纯数字

	private ListView lv_come;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_comemsg, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		init();
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		pbaseActivity = (pBaseActivity) activity;
	}

	private void init() {
		// TODO Auto-generated method stub

		list = loadConversationsWithRecentChat();
		lv_come = (ListView) getView().findViewById(R.id.lv_come);
		for (EMConversation em : loadConversationsWithRecentChat()) {
			System.out.println("环信username:"+em.getUserName());
			try {
				senduser(em.getUserName()
						,mShareFileUtils.getString(Constant.CLIENT_ID, ""));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			Map m = new HashMap<String, Object>();
//			m.put("username", em.getUserName());
//			/* 环信的群组ID为纯数字，用正则匹配来判断是不是群组 */
//			m.put("is_group", em.getUserName().matches(isnumber));
//			easemobchatusers.add(m);
		}
//		if (adapter == null) {
//			adapter = new ChatHistoryAdapter(getActivity(), users);
//		}
		// adapter.setBaseFragment(HomeFragment.this);
//		lv_come.setAdapter(adapter);
//		refresh1();
	
//		easemobchatUser users = new easemobchatUser();
//		users.setEasemobchatusers(easemobchatusers);

	}

	public void refresh() {
		if (list != null) {
			list.clear();
		}
		list.addAll(loadConversationsWithRecentChat());
		System.out.println("环信获取的会话："+list.toString());
		System.out.println("环信获取的会话条数："+list.size());
//		easemobchatusers.clear();
		if(users !=null){
			users.clear();
		}
		for (EMConversation em : loadConversationsWithRecentChat()) {
			try {
				System.out.println("em.getUserName:"+em.getUserName());
				senduser(em.getUserName()
						,mShareFileUtils.getString(Constant.CLIENT_ID, ""));
//				for(int i = 1;users.size()==i){
//					
//				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			Map m = new HashMap<String, Object>();
//			m.put("username", em.getUserName());
//			/* 环信的群组ID为纯数字，用正则匹配来判断是不是群组 */
//			m.put("is_group", em.getUserName().matches(isnumber));
//			easemobchatusers.add(m);
		}
		System.out.println("users:"+users.toString());
		adapter = new ChatHistoryAdapter(getActivity(), users);
		
		// adapter.setBaseFragment(HomeFragment.this);
		lv_come.setAdapter(adapter);
	
//		easemobchatUser users = new easemobchatUser();
//		users.setEasemobchatusers(easemobchatusers);
	}

	/**
	 * 获取所有会话
	 * 
	 * @param context
	 * @return
	 */
	private List<EMConversation> loadConversationsWithRecentChat() {
		// 获取所有会话，包括陌生人
		Hashtable<String, EMConversation> conversations = EMChatManager
				.getInstance().getAllConversations();
		List<EMConversation> list = new ArrayList<EMConversation>();
		// 过滤掉messages seize为0的conversation
		for (EMConversation conversation : conversations.values()) {
			if (conversation.getAllMessages().size() != 0)
				list.add(conversation);
		}
		// 排序
		sortConversationByLastChatTime(list);
		return list;
	}

	/**
	 * 根据最后一条消息的时间排序
	 * 
	 * @param usernames
	 */
	private void sortConversationByLastChatTime(
			List<EMConversation> conversationList) {
		Collections.sort(conversationList, new Comparator<EMConversation>() {
			@Override
			public int compare(final EMConversation con1,
					final EMConversation con2) {

				EMMessage con2LastMessage = con2.getLastMessage();
				EMMessage con1LastMessage = con1.getLastMessage();
				if (con2LastMessage.getMsgTime() == con1LastMessage
						.getMsgTime()) {
					return 0;
				} else if (con2LastMessage.getMsgTime() > con1LastMessage
						.getMsgTime()) {
					return 1;
				} else {
					return -1;
				}
			}

		});
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		this.hidden = hidden;
		if (!hidden) {
			if (pbaseActivity.isNetworkAvailable) {
				if (!hidden) {
					refresh();
				}
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (pbaseActivity.isNetworkAvailable) {
			refresh();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

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
			params = PeerParamsUtils.getUserParams(getActivity(), client_id);
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
						pLog.i("test","response:"+response.toString());
						try {
							LoginBean loginBean = JsonDocHelper.toJSONObject(
									response.getJSONObject("success")
											.toString(), LoginBean.class);
							if(loginBean!=null){
								users.add(loginBean.user);
								System.out.println("us:"+loginBean.user.getIs_friend());
								if(adapter == null){
									adapter = new ChatHistoryAdapter(getActivity(), users);
									
									// adapter.setBaseFragment(HomeFragment.this);
									lv_come.setAdapter(adapter);
								}
								refresh1();
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

	private void refresh1() {

		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}

	}

}
