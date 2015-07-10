package com.peer.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.peer.adapter.Recommend_topicAdapter;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.bean.RecommendTopicBean;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.pLog;
import com.peer.utils.pViewBox;


/**
 * 话题推荐
 * 
 */
public class Recommend_topic extends pBaseActivity{
	
	private PullToRefreshListView pull_refresh_topic;
	private List<Map> list = new ArrayList<Map>(); 
	private int page = 1;
    Recommend_topicAdapter adapter;
	
	class PageViewList {
		private LinearLayout ll_back,ll_topic;
		private TextView tv_title;
		private ImageView im_search;
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
		pageViewaList.tv_title.setText("话题");
		pull_refresh_topic = (PullToRefreshListView) findViewById(R.id.pull_refresh_topic);
		pageViewaList.ll_topic.setVisibility(View.VISIBLE);
		pageViewaList.im_search.setVisibility(View.GONE);
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);
		pageViewaList.ll_topic.setOnClickListener(this);
		pageViewaList.im_search.setOnClickListener(this);
		
		pull_refresh_topic.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				
				if(!isNetworkAvailable){
					showToast(getResources().getString(R.string.Broken_network_prompt), Toast.LENGTH_SHORT, false);
				}else{
					/*ChatRoomTypeUtil.getInstance().setChatroomtype(Constant.MULTICHAT);	
					
					ChatRoomTypeUtil.getInstance().setHuanxingId(topic.getHuangxin_group_id());
					ChatRoomTypeUtil.getInstance().setTitle(topic.getLabel_name());
					ChatRoomTypeUtil.getInstance().setTheme(topic.getSubject());
					ChatRoomTypeUtil.getInstance().setTopicId(topic.getTopicid());
					
					User user=(User)mList.get(position).get(Constant.USER);
					String ownerid=null;
					try {
						ownerid = PeerUI.getInstance().getISessionManager().getUserId();
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}					
					if(user.getUserid().equals(ownerid)){
						ChatRoomTypeUtil.getInstance().setIsowner(true);
					}else{
						ChatRoomTypeUtil.getInstance().setIsowner(false);
					}
					topic.setUser(user);
					ChatRoomTypeUtil.getInstance().setTopic(topic);	
					Intent intent=new Intent(mContext,ChatRoomActivity.class);
					mContext.startActivity(intent);*/	
				}	

			}
		});
	}

	@Override
	protected void processBiz() {
		// TODO Auto-generated method stub
		try {
			sendRecommendtopic(mShareFileUtils.getString(
					Constant.CLIENT_ID, ""), page);
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
		return getLayoutInflater().inflate(R.layout.activity_recommend_topic, null);
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
		case R.id.im_search:
			Intent searchtopic = new Intent(this,SearchTopicActivity.class);
			startActivity(searchtopic);
			break;
		case R.id.ll_topic:
			Intent createtopic = new Intent(this,CreatTopicActivity.class);
			startActivity(createtopic);
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
	
	
	
	
	
	
	/**
	 * 推荐话题请求
	 * 
	 * @param client_id
	 * @param page
	 * @throws UnsupportedEncodingException
	 */
	private void sendRecommendtopic(String client_id, int page)
			throws UnsupportedEncodingException {
		// TODO Auto-generated method stub

		HttpEntity entity = null;
		try {
			entity = PeerParamsUtils.getRemTopicParams(this, client_id,
					page);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		HttpUtil.post(this, HttpConfig.TOPIC_RECOMMEND_IN_URL
				, entity, "application/json",
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub

						pLog.i("test", "onFailure+statusCode:" + statusCode
								+ "headers:" + headers.toString()
								+ "responseString:" + responseString);

						super.onFailure(statusCode, headers, responseString,
								throwable);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONArray errorResponse) {
						// TODO Auto-generated method stub

						pLog.i("test", "onFailure+statusCode:" + statusCode
								+ "headers:" + headers.toString()
								+ "errorResponse:" + errorResponse.toString());
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub

						pLog.i("test", "onFailure:statusCode:" + statusCode);
						pLog.i("test", "throwable:" + throwable.toString());
						pLog.i("test", "headers:" + headers.toString());
						pLog.i("test",
								"errorResponse:" + errorResponse.toString());
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}


					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub

						pLog.i("test", "onSuccess:statusCode:" + statusCode
								+ "headers:" + headers.toString() + "response:"
								+ response.toString());			
						try {
							RecommendTopicBean recommendtopicbean = JsonDocHelper.toJSONObject(
									response.getJSONObject("success")
											.toString(), RecommendTopicBean.class);
							if (recommendtopicbean != null) {
								pLog.i("test", "user1:"
										+ recommendtopicbean.topics.get(0).getSubject().toString());
							}
							
							for (int index = 0; index < recommendtopicbean.topics.size(); index++) {
								Map<String, Object> topicMsg = new HashMap<String, Object>();
								topicMsg.put("label_name", recommendtopicbean.topics.get(index).getLabel_name().toString());
								topicMsg.put("subject", recommendtopicbean.topics.get(index).getSubject().toString());
								topicMsg.put("user_id", recommendtopicbean.topics.get(index).getUser_id().toString());
								topicMsg.put("topic_id",
										recommendtopicbean.topics.get(index).getTopic_id().toString());
								topicMsg.put("sys_time",
										recommendtopicbean.getSys_time());
								topicMsg.put("created_at",
										recommendtopicbean.topics.get(index).getCreated_at().toString());
								list.add(topicMsg);
							}

						} catch (Exception e1) {
							pLog.i("test", "Exception:" + e1.toString());
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						if (adapter == null) {
							adapter = new Recommend_topicAdapter(Recommend_topic.this, list);
							pull_refresh_topic.setAdapter(adapter);
						}

						refresh();
						// adapter.setBaseFragment(HomeFragment.this);

						super.onSuccess(statusCode, headers, response);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String responseString) {
						// TODO Auto-generated method stub

						pLog.i("test", "onSuccess:statusCode:" + statusCode
								+ "headers:" + headers.toString()
								+ "responseString:" + responseString.toString());
						super.onSuccess(statusCode, headers, responseString);
					}

				});

	}
	
	private void refresh() {

		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}

	}

}
