package com.peer.activity;

import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatService;
import com.easemob.chat.EMGroupManager;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.peer.R;
import com.peer.IMimplements.easemobchatImp;
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
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		
		// 全屏显示

		AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
		animation.setDuration(4000);
		pageViewaList.welLin.startAnimation(animation);
		if (isNetworkAvailable) {
			sendSystemConfig();
		} else {
			runNextPage();
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
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

	private void runNextPage() {
		new CountDownTimer(5000, 1000) {
			public void onTick(long millisUntilFinished) {

			}

			@SuppressWarnings("static-access")
			public void onFinish() {
				Intent intent = new Intent();
				if (!mShareFileUtils.getString(Constant.CLIENT_ID, "").equals(
						"")) {
					startActivityForLeft(MainActivity.class, intent, false);
				} else {
					startActivityForLeft(LoginActivity.class, intent, false);

				}
			}

		}.start();
	}

	private void sendSystemConfig() {
		HttpEntity entity = null;
		HttpUtil.post(HttpConfig.GET_SYSTEMCONFIG,
				new JsonHttpResponseHandler() {

					@SuppressWarnings("static-access")
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						super.onSuccess(statusCode, headers, response);


						Intent intent = new Intent();

						try {
							mShareFileUtils.setBoolean(Constant.CAN_LOGIN,
									response.getJSONObject("system")
											.getBoolean("can_login"));
							mShareFileUtils
									.setBoolean(
											Constant.CAN_UPGRADE_SILENTLY,
											response.getJSONObject("system")
													.getBoolean(
															"can_upgrade_silently"));
							mShareFileUtils.setBoolean(
									Constant.CAN_REGISTER_USER, response
											.getJSONObject("system")
											.getBoolean("can_register_user"));
							mShareFileUtils.setString(
									Constant.CAN_REGISTER_USER,
									response.getJSONObject("system").getString(
											"time"));

							if (response.getJSONObject("system").getBoolean(
									"can_login")
									&& !mShareFileUtils.getString(
											Constant.CLIENT_ID, "").equals("")
											&&!mShareFileUtils.getString(
													Constant.BIRTH, "").equals("")) {
									sendRequesJpush();
									startActivityForLeft(MainActivity.class,
											intent, false);
							} else {
								startActivityForLeft(LoginActivity.class,
										intent, false);
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							startActivityForLeft(LoginActivity.class, intent,
									false);
							e.printStackTrace();
						}

					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONArray response) {
						// TODO Auto-generated method stub
						super.onSuccess(statusCode, headers, response);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String responseString) {
						// TODO Auto-generated method stub

						super.onSuccess(statusCode, headers, responseString);
					}

				});

	}

	/**
	 * 注册极光跟环信
	 */
	@SuppressWarnings("static-access")
	protected void sendRequesJpush() {
		// TODO Auto-generated method stub
		JPushInterface.setAlias(getApplication(),
				mShareFileUtils.getString(Constant.CLIENT_ID, ""),
				new TagAliasCallback() {
					@Override
					public void gotResult(int code, String arg1,
							Set<String> arg2) {
						// TODO Auto-generated method stub
						System.out.println("code" + code);
						Log.i("注册极光结果放回", String.valueOf(code));
					}
				});

		String client_id = mShareFileUtils.getString(Constant.CLIENT_ID, "");
		
		
		pLog.i("test","client_id:"+ client_id.replace("-", ""));
		
//		07-21 22:18:31.226: I/test(15270): client_id:5f7f29537bb240b9b5ec45f151e06d8d
//		07-21 22:18:31.226: I/test(15270): PASSWORD:5f7f2953-7bb2-40b9-b5ec-45f151e06d8d

		
		pLog.i("test","PASSWORD:"+ mShareFileUtils.getString(Constant.PASSWORD, ""));
		
		easemobchatImp.getInstance().login(client_id.replace("-", ""),
				mShareFileUtils.getString(Constant.PASSWORD, ""));
		easemobchatImp.getInstance().loadConversationsandGroups();
		
		pLog.i("test", "EMChatManager:"+EMChatManager.getInstance().isConnected());
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
