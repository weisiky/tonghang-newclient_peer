package com.peer.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.peer.R;
import com.peer.adapter.SkillAdapter;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.bean.LoginBean;
import com.peer.bean.PersonpageBean;
import com.peer.event.SkillEvent;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.pLog;
import com.peer.utils.pViewBox;

import de.greenrobot.event.EventBus;

/**
 * 用户标签类 显示、可操作
 */
public class MySkillActivity extends pBaseActivity {

	private PageViewList pageViewaList;
	private EventBus mBus;
	private ArrayList<String> mlist;
	private int Hadtag;

	public static Handler handler;
	private TextView title;
	private SkillAdapter adapter;

	class PageViewList {
		private LinearLayout ll_back, ll_createTag_mytag;
		private TextView tv_title;
		private ListView lv_myskill;
	}

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
				R.string.myskill));

	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);
		pageViewaList.ll_createTag_mytag.setOnClickListener(this);
	}

	@Override
	protected void processBiz() {
		// TODO Auto-generated method stub
		mlist = JsonDocHelper.toJSONArrary(
				getIntent().getStringExtra("labels"), String.class);

		pLog.i("test", "初始化mlist:" + mlist);

		Hadtag = mlist.size();
		adapter = new SkillAdapter(this, mlist);
		pageViewaList.lv_myskill.setAdapter(adapter);
		registEventBus();

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
		return getLayoutInflater().inflate(R.layout.activity_my_myskill, null);
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
		case R.id.ll_createTag_mytag:
			Hadtag = mlist.size();
			if (Hadtag > 4) {
				showToast("您已经有五个标签，不能再创建了", Toast.LENGTH_SHORT, false);
				break;
			} else {
				if (isNetworkAvailable) {
					CreateTagDialog();
				} else {
					showToast(
							getResources().getString(
									R.string.Broken_network_prompt),
							Toast.LENGTH_SHORT, false);
				}

			}
			break;
		default:
			break;
		}

	}

	/**
	 * 创建标签
	 * 
	 */
	private void CreateTagDialog() {
		// TODO Auto-generated method stub
		final EditText inputServer = new EditText(MySkillActivity.this);
		inputServer.setFocusable(true);
		AlertDialog.Builder builder = new AlertDialog.Builder(
				MySkillActivity.this);
		builder.setTitle(getResources().getString(R.string.register_tag))
				.setView(inputServer)
				.setNegativeButton(getResources().getString(R.string.cancel),
						null);
		builder.setPositiveButton(getResources().getString(R.string.sure),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String inputName = inputServer.getText().toString()
								.trim();
						if (!TextUtils.isEmpty(inputName)) {
							String fomate = "^[A-Za-z]+$";
							if (inputName.matches(fomate)) {
								if (inputName.length() < 13) {
									boolean issame = false;
									for (int i = 0; i < mlist.size(); i++) {
										if (mlist.get(i).equals(inputName)) {
											issame = true;
											showToast(
													getResources()
															.getString(
																	R.string.repetskill),
													Toast.LENGTH_SHORT, false);
											break;
										}
									}
									if (!issame) {
										createLable(inputName);
									}
								} else {
									showToast(
											getResources().getString(
													R.string.skillname),
											Toast.LENGTH_SHORT, false);
								}
							} else {
								if (inputName.length() < 7) {
									boolean issame = false;
									for (int i = 0; i < mlist.size(); i++) {
										if (mlist.get(i).equals(inputName)) {
											issame = true;
											showToast(
													getResources()
															.getString(
																	R.string.repetskill),
													Toast.LENGTH_SHORT, false);
											break;
										}
									}
									if (!issame) {
										createLable(inputName);
									}
								} else {
									showToast(
											getResources().getString(
													R.string.skillname),
											Toast.LENGTH_SHORT, false);
								}
							}

						} else {
							showToast("请输入行业标签名", Toast.LENGTH_SHORT, false);
						}
					}
				});
		builder.show();
	}

	public void createLable(String label) {
		mlist.add(label);
		if (isNetworkAvailable) {
			try {
				senduserlabels(
						mShareFileUtils.getString(Constant.CLIENT_ID, ""),
						mlist);
				pLog.i("test", "mlist:" + mlist);
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			showToast(getResources().getString(R.string.Broken_network_prompt),
					Toast.LENGTH_SHORT, false);
		}

	}

	/**
	 * 修改当前用户标签请求
	 * 
	 * @param client_id
	 * @param label_name
	 * @throws UnsupportedEncodingException
	 */
	private void senduserlabels(String client_id, List<String> label_name)
			throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		final Intent intent = new Intent();
		// HttpEntity entity = null;
		// try {
		// entity = PeerParamsUtils.getUserLabelsParams(this, client_id,
		// label_name);
		// } catch (Exception e2) {
		// // TODO Auto-generated catch block
		// e2.printStackTrace();
		// }
		RequestParams params = null;
		try {
			params = PeerParamsUtils.getUserLabelsParams(this, client_id,
					label_name);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpUtil.post(this, HttpConfig.USER_UPDATE_LABEL_IN_URL + client_id
				+ ".json", params, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				hideLoading();
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONArray errorResponse) {
				// TODO Auto-generated method stub
				hideLoading();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				hideLoading();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@SuppressWarnings("static-access")
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				try {
					JSONObject result = response.getJSONObject("success");

					String code = result.getString("code");
					pLog.i("test", "code:"+code);
					if(code.equals("200")){
						LoginBean loginBean = JsonDocHelper.toJSONObject(response
								.getJSONObject("success").toString(),
								LoginBean.class);
						PersonpageBean.getInstance().setUser(loginBean.user);
						
						if (loginBean.user.getLabels() != null
								&& loginBean.user.getLabels().size() > 0) {
							mShareFileUtils.setString(Constant.LABELS,
									loginBean.user.getLabels().toString());
						}
						
						Hadtag = mlist.size();
						adapter = new SkillAdapter(MySkillActivity.this, mlist);
						pageViewaList.lv_myskill.setAdapter(adapter);
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

	private void registEventBus() {
		// TODO Auto-generated method stub
		mBus = EventBus.getDefault();
		/*
		 * Registration: three parameters are respectively, message subscriber
		 * (receiver), receiving method name, event classes
		 */
		mBus.register(this, "getSkillEvent", SkillEvent.class);
	}

	private void getSkillEvent(final SkillEvent event) {
		event.getPosition();
		event.getLabel();
		Hadtag = mlist.size();
		if (event.getIsdelete()) {
			if (Hadtag < 3) {
				showToast("至少需要保留两个行业标签", Toast.LENGTH_SHORT, false);
			} else {
				
				if (isNetworkAvailable) {
					try {
						mlist.remove(event.getPosition());
						senduserlabels(mShareFileUtils.getString(
								Constant.CLIENT_ID, ""), mlist);
						pLog.i("test", "mlist:" + mlist);
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
		} else {
			
			if (isNetworkAvailable) {
				try {
					mlist.remove(event.getPosition());
					mlist.add(event.getLabel());
					senduserlabels(
							mShareFileUtils.getString(Constant.CLIENT_ID, ""),
							mlist);
					pLog.i("test", "mlist:" + mlist);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				showToast(
						getResources()
								.getString(R.string.Broken_network_prompt),
						Toast.LENGTH_SHORT, false);
			}
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

}
