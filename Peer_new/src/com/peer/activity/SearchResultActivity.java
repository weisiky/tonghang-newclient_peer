package com.peer.activity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.bean.SearchBean;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.pLog;
import com.peer.utils.pViewBox;

/**
 * 查询结果
 */
public class SearchResultActivity extends pBaseActivity {

	private PullToRefreshListView lv_searchresult;
	/** 分页 **/
	private int page = 1;
	/** 查询分类 **/
	String contanttype;
	
	String searchname;

	class PageViewList {
		private LinearLayout ll_back;
		private TextView tv_title;

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

		searchname = SearchBean.getInstance().getSearchname();
		String searchaccuratetarget = SearchBean.getInstance()
				.getCallbacklabel();

		lv_searchresult = (PullToRefreshListView) findViewById(R.id.lv_searchresult);// �Զ���ؼ����Լ�find
		pageViewaList.tv_title.setText(searchname);

	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);

		RefreshListner();// ����ˢ�¼���
	}

	@Override
	protected void processBiz() {
		// TODO Auto-generated method stub
		try {
			if (SearchBean.getInstance().getSearchtype()
					.equals(Constant.TOPICBYTOPIC)) {
				contanttype = Constant.TOPICBYTOPIC;
				SearchResult(searchname, page, contanttype);
			} else if (SearchBean.getInstance().getSearchtype()
					.equals(Constant.USERBYNIKE)) {
				contanttype = Constant.USERBYNIKE;
				SearchResult(searchname, page, contanttype);
			} else if (SearchBean.getInstance().getSearchtype()
					.equals(Constant.TOPICBYLABEL)) {
				contanttype = Constant.TOPICBYLABEL;
				SearchResult(searchname, page, contanttype);
			} else if (SearchBean.getInstance().getSearchtype()
					.equals(Constant.USERBYLABEL)) {
				contanttype = Constant.USERBYLABEL;
				SearchResult(searchname, page, contanttype);
			}
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
		return getLayoutInflater()
				.inflate(R.layout.activity_searchresult, null);
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

	/**
	 * ����������ˢ�·���
	 * 
	 */
	private void RefreshListner() {
		// TODO Auto-generated method stub
		lv_searchresult
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						String label = DateUtils.formatDateTime(
								SearchResultActivity.this
										.getApplicationContext(), System
										.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);
						try {
							page = 1;
							SearchResult(searchname, page, contanttype);
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						showToast("����ˢ�³ɹ�", Toast.LENGTH_SHORT, false);
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						String label = DateUtils.formatDateTime(
								SearchResultActivity.this
										.getApplicationContext(), System
										.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);
						page = +1;
						try {
							SearchResult(searchname, ++page, contanttype);
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						showToast("����ˢ�³ɹ�", Toast.LENGTH_SHORT, false);
					}
				});
	}

	/**
	 * �����ӿ�
	 * 
	 * @param label_name
	 * @param page
	 * @throws UnsupportedEncodingException
	 */

	private void SearchResult(String name, int page, String contanttype)
			throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		/** �����ַ **/
		String URL = null;

		Map<String, Object> searchParams = new HashMap<String, Object>();

		// List<BasicNameValuePair> baseParams = new
		// ArrayList<BasicNameValuePair>();
		if (contanttype.equals(Constant.USERBYLABEL)) {
			searchParams.put("label_name", name);
			// baseParams.add(new BasicNameValuePair("label_name", name));
			URL = HttpConfig.SEARCH_USER_LABEL_URL + "?page=" + page;
		} else if (contanttype.equals(Constant.USERBYNIKE)) {
			searchParams.put("username", name);
			// baseParams.add(new BasicNameValuePair("username", name));
			URL = HttpConfig.SEARCH_USER_NICK_URL + "?page=" + page;
		} else if (contanttype.equals(Constant.TOPICBYLABEL)) {
			searchParams.put("label_name", name);
			// baseParams.add(new BasicNameValuePair("label_name", name));
			URL = HttpConfig.SEARCH_TOPIC_LABEL_URL + "?page=" + page;
		} else {
			searchParams.put("Subject", name);
			// baseParams.add(new BasicNameValuePair("Subject", name));
			URL = HttpConfig.SEARCH_TOPIC_SUBJECT_URL + "?page=" + page;
		}

		pLog.i("URL", URL);

		String params = null;
		try {
			params = JsonDocHelper.toJSONString(searchParams);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpEntity entity = new StringEntity(params, "utf-8");

		HttpUtil.post(this, URL, entity, "application/json",
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub

						hideLoading();

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
						hideLoading();
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
						hideLoading();
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
							JSONArray response) {
						// TODO Auto-generated method stub
						hideLoading();
						pLog.i("test", "onSuccess+statusCode:" + statusCode
								+ "headers:" + headers.toString() + "response:"
								+ response.toString());
						super.onSuccess(statusCode, headers, response);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						hideLoading();
						pLog.i("test", "onSuccess:statusCode:" + statusCode
								+ "headers:" + headers.toString() + "response:"
								+ response.toString());
						super.onSuccess(statusCode, headers, response);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String responseString) {
						// TODO Auto-generated method stub
						hideLoading();
						pLog.i("test", "onSuccess:statusCode:" + statusCode
								+ "headers:" + headers.toString()
								+ "responseString:" + responseString.toString());
						super.onSuccess(statusCode, headers, responseString);
					}

				});
	}

	@Override
	public void onNetworkOn() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNetWorkOff() {
		// TODO Auto-generated method stub

	}
}
