package com.peer.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.peer.adapter.TopicAdapter;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.bean.PersonpageBean;
import com.peer.bean.RecommendTopicBean;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.pLog;
import com.peer.utils.pViewBox;

/**
 * 用户话题类 某用户的全部话题
 */
public class TopicActivity extends pBaseActivity {

	int page = 1;
	List<Map> list = new ArrayList<Map>();
	TopicAdapter adapter;

	class PageViewList {
		private LinearLayout ll_back;
		private TextView tv_title, email, personnike;
		private ImageView personhead;
		private ListView lv_topichistory;
	}

	private PageViewList pageViewaList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@SuppressWarnings("static-access")
	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		pageViewaList = new PageViewList();
		pViewBox.viewBox(this, pageViewaList);
		if (PersonpageBean.getInstance().user.getClient_id().equals(
				mShareFileUtils.getString(Constant.CLIENT_ID, ""))) {
			pageViewaList.tv_title.setText(getResources().getString(
					R.string.topic_owen));
		} else if (PersonpageBean.getInstance().user.getSex().equals("男")) {
			pageViewaList.tv_title.setText(getResources().getString(
					R.string.topic_other));
		} else {
			pageViewaList.tv_title.setText(getResources().getString(
					R.string.topic_nvother));
		}

	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);
	}

	@Override
	protected void processBiz() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		pageViewaList.personnike.setText(intent.getStringExtra("nike"));
		pageViewaList.email.setText(intent.getStringExtra("email"));

		try {
			sendUserTopic(PersonpageBean.getInstance().user.getClient_id(),
					page);
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
		return getLayoutInflater().inflate(R.layout.activity_topic, null);
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

	@Override
	public void onNetworkOn() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNetWorkOff() {
		// TODO Auto-generated method stub

	}

	/**
	 * 查看指定用户的话题请求
	 * 
	 * @param client_id
	 * @param page
	 * @throws UnsupportedEncodingException
	 */
	private void sendUserTopic(String client_id, int page)
			throws UnsupportedEncodingException {
		// TODO Auto-generated method stub

		// HttpEntity entity = null;
		// try {
		// entity = PeerParamsUtils.getRemTopicParams(this, client_id, page);
		// } catch (Exception e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }

		RequestParams params = null;
		try {
			params = PeerParamsUtils.getRemTopicParams(this, client_id, page);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		HttpUtil.post(this, HttpConfig.USER_TOPIC_IN_URL, params,
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub
						hideLoading();
						super.onFailure(statusCode, headers, responseString,
								throwable);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONArray errorResponse) {
						// TODO Auto-generated method stub
						hideLoading();
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						hideLoading();
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						hideLoading();

						pLog.i("test", "TopicActivity:" + response.toString());

						try {
							RecommendTopicBean recommendtopicbean = JsonDocHelper
									.toJSONObject(
											response.getJSONObject("success")
													.toString(),
											RecommendTopicBean.class);
							if (recommendtopicbean != null) {
								pLog.i("test", "user1:"
										+ recommendtopicbean.topics.get(0)
												.getSubject().toString());
							}

							for (int index = 0; index < recommendtopicbean.topics
									.size(); index++) {
								Map<String, Object> topicMsg = new HashMap<String, Object>();
								topicMsg.put("label_name",
										recommendtopicbean.topics.get(index)
												.getLabel_name().toString());
								topicMsg.put("subject",
										recommendtopicbean.topics.get(index)
												.getSubject().toString());
								topicMsg.put("user_id",
										recommendtopicbean.topics.get(index)
												.getUser_id().toString());
								topicMsg.put("topic_id",
										recommendtopicbean.topics.get(index)
												.getTopic_id().toString());
								topicMsg.put("sys_time",
										recommendtopicbean.getSys_time());
								topicMsg.put("created_at",
										recommendtopicbean.topics.get(index)
												.getCreated_at().toString());
								list.add(topicMsg);
							}

						} catch (Exception e1) {
							pLog.i("test", "Exception:" + e1.toString());
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						if (adapter == null) {
							adapter = new TopicAdapter(TopicActivity.this, list);
							pageViewaList.lv_topichistory.setAdapter(adapter);
						}

						refresh();

						super.onSuccess(statusCode, headers, response);
					}

				});

	}

	private void refresh() {

		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}

	}
}
