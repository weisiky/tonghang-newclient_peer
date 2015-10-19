package com.peer.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.peer.R;
import com.peer.IMimplements.easemobchatImp;
import com.peer.adapter.TopicAdapter;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.bean.PersonpageBean;
import com.peer.bean.RecommendTopicBean;
import com.peer.bean.TopicBean;
import com.peer.bean.UserBean;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.utils.BussinessUtils;
import com.peer.utils.ImageLoaderUtil;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.ManagerActivity;
import com.peer.utils.pLog;
import com.peer.utils.pViewBox;

/**
 * 用户话题类 某用户的全部话题
 */
public class TopicActivity extends pBaseActivity {

	int page = 1;
	List<TopicBean> list = new ArrayList<TopicBean>();
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
		setContentView(R.layout.activity_topic);
		findViewById();
		setListener();
		processBiz();
	}

	@SuppressWarnings("static-access")
	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		pageViewaList = new PageViewList();
		pViewBox.viewBox(this, pageViewaList);

	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);
	}

	@Override
	protected void processBiz() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		UserBean userbean = (UserBean) intent.getSerializableExtra("bean");
		if (userbean.getClient_id().equals(
				mShareFileUtils.getString(Constant.CLIENT_ID, ""))) {
			pageViewaList.tv_title.setText(getResources().getString(
					R.string.topic_owen));
		} else if (userbean.getSex().equals("男")) {
			pageViewaList.tv_title.setText(getResources().getString(
					R.string.topic_other));
		} else {
			pageViewaList.tv_title.setText(getResources().getString(
					R.string.topic_nvother));
		}
		String name = intent.getStringExtra("nike");
		String email = intent.getStringExtra("email");
		String image = intent.getStringExtra("image");
		String client_id = userbean.getClient_id();
		System.out.println("name:" + name);
		System.out.println("email:" + email);
		pageViewaList.personnike.setText(intent.getStringExtra("nike"));
		pageViewaList.email.setText(intent.getStringExtra("email"));
		// ImageLoader加载图片
		ImageLoaderUtil.getInstance().showHttpImage(this,
				mShareFileUtils.getString(Constant.PIC_SERVER, "") + image,
				pageViewaList.personhead, R.drawable.mini_avatar_shadow);

		try {
			sendUserTopic(client_id, page);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);

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
								RecommendTopicBean recommendtopicbean = JsonDocHelper
										.toJSONObject(
												response.getJSONObject(
														"success").toString(),
												RecommendTopicBean.class);
								if (recommendtopicbean != null) {
									list.addAll(recommendtopicbean.topics);
									if (adapter == null) {
										adapter = new TopicAdapter(
												TopicActivity.this, list);
										pageViewaList.lv_topichistory
												.setAdapter(adapter);
									}

									refresh();
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

	private void refresh() {

		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}

	}
	
}
