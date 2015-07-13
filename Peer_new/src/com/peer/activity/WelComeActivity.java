package com.peer.activity;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;

import cn.jpush.android.api.JPushInterface;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.utils.pLog;
import com.peer.utils.pViewBox;

/**
 * 欢迎Activity
 * 
 * @author zhangzg
 * 
 */

public class WelComeActivity extends pBaseActivity {

	class PageViewList {

		public LinearLayout welLin;
	}

	private PageViewList pageViewaList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
		animation.setDuration(4000);
		pageViewaList.welLin.startAnimation(animation);
		sendSystemConfig();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		new CountDownTimer(5000, 1000) {
			public void onTick(long millisUntilFinished) {

			}

			@SuppressWarnings("static-access")
			public void onFinish() {
				Intent intent = new Intent();
				if (!mShareFileUtils.getString(Constant.CLIENT_ID, "")
						.equals("")) {
					startActivityForLeft(MainActivity.class, intent, false);
				} else {
					startActivityForLeft(LoginActivity.class, intent, false);

				}
			}

		}.start();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		JPushInterface.onPause(this);
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
	
	private void sendSystemConfig(){
		HttpEntity entity = null;
		HttpUtil.post(HttpConfig.LONIN_IN_URL, new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						super.onSuccess(statusCode, headers, response);
						
						pLog.i("test","response:"+ response.toString());
						
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONArray response) {
						// TODO Auto-generated method stub
						pLog.i("test","JSONArray:"+ response.toString());
						super.onSuccess(statusCode, headers, response);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String responseString) {
						// TODO Auto-generated method stub
						pLog.i("test","responseString:"+ responseString);
						super.onSuccess(statusCode, headers, responseString);
					}
					
			
			
		});

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
		return getLayoutInflater().inflate(R.layout.activity_welcome, null);
	}

	@Override
	protected View loadBottomLayout() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

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
