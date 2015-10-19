package com.peer.activity;

import java.util.Set;

import net.google.niofile.AdManager;

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
import com.peer.utils.BussinessUtils;
import com.peer.utils.ManagerActivity;
import com.peer.utils.pLog;
import com.peer.utils.pViewBox;
import com.peer.utils.zManifestInfoUtils;

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
		setContentView(R.layout.activity_welcome);
//		AdManager.getInstance(this).init("55b6776ef7488d73", "7fd430361ac0d3f0", true);
		findViewById();
		setListener();
		processBiz();
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
//		AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
//		animation.setDuration(4000);
//		pageViewaList.welLin.startAnimation(animation);
		if (isNetworkAvailable) {
			pLog.i("test", "连网自动登录中。。。");
			zManifestInfoUtils utils = new zManifestInfoUtils(WelComeActivity.this);
			int code_now = utils.getVersionCode();
			String name_now = utils.getVersionName();
			sendSystemConfig(name_now);
		} else {
			runNextPage();
		}
	}

	private void runNextPage() {
		new CountDownTimer(3000, 1000) {
			public void onTick(long millisUntilFinished) {

			}

			@SuppressWarnings("static-access")
			public void onFinish() {
				Intent intent = new Intent();
				if (!mShareFileUtils.getString(Constant.CLIENT_ID, "").equals(
						"")
						&& !mShareFileUtils.getString(Constant.BIRTH, "")
								.equals("")
						&& !mShareFileUtils.getString(Constant.LABELS, "")
								.equals("")) {
					startActivityForLeft(MainActivity.class, intent, false);
				} else {
					startActivityForLeft(LoginActivity.class, intent, false);

				}
			}

		}.start();
	}

	private void sendSystemConfig(final String version) {
		HttpEntity entity = null;
		HttpUtil.post(HttpConfig.GET_SYSTEMCONFIG,
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

					@SuppressWarnings("static-access")
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						
						pLog.i("test", "res:"+response.toString());

						Intent intent = new Intent();
						JSONObject reasult;
						try {
							reasult = response.getJSONObject("success");

							mShareFileUtils.setBoolean(
									Constant.CAN_LOGIN,
									reasult.getJSONObject("system").getBoolean(
											"can_login"));
							//判断强制（0）或者可选（1）
							mShareFileUtils
									.setBoolean(
											Constant.CAN_UPGRADE_SILENTLY,
											reasult.getJSONObject("system")
													.getBoolean(
															"can_upgrade_silently"));
							//判断升级开关是否打开
							mShareFileUtils
							.setBoolean(
									Constant.CAN_UPGRADE,
									reasult.getJSONObject("system")
									.getBoolean(
											"can_upgrade"));
							//最新app版本号
							mShareFileUtils
							.setString(
									Constant.APP_VERSION,
									reasult.getJSONObject("system")
											.getString(
													"app_version"));
							
							mShareFileUtils.setInt(Constant.APP_CODE, reasult.getJSONObject("system")
									.getInt(
											"app_code"));
							
							//app下载链接
							mShareFileUtils
							.setString(
									Constant.APP_LINK,
									reasult.getJSONObject("system")
											.getString(
													"app_link"));
							
							
							
							
							mShareFileUtils.setBoolean(
									Constant.CAN_REGISTER_USER, reasult
											.getJSONObject("system")
											.getBoolean("can_register_user"));
							mShareFileUtils.setString(
									Constant.CAN_REGISTER_USER,
									reasult.getJSONObject("system").getString(
											"time"));
							mShareFileUtils.setBoolean(
									Constant.USE_ADV,
									reasult.getJSONObject("system").getBoolean(
											"use_adv"));
							mShareFileUtils.setBoolean(
									Constant.THIRD_ADV,
									reasult.getJSONObject("system").getBoolean(
											"third_adv"));
							mShareFileUtils.setString(
									Constant.SELF_ADV_URL,
									reasult.getJSONObject("system").getString(
											"self_adv_url"));
							mShareFileUtils.setString(
									Constant.SELF_IMG,
									reasult.getJSONObject("system").getString(
											"self_img"));
							
							if (reasult.getJSONObject("system").getBoolean(
									"can_login")
									&& !mShareFileUtils.getString(
											Constant.CLIENT_ID, "").equals("")
									&& !mShareFileUtils.getString(
											Constant.LABELS, "").equals("")
									&& !mShareFileUtils.getString(
											Constant.BIRTH, "").equals("")
								    ) {
								sendRequesJpush();
//								startActivityForLeft(MainActivity.class,
//										intent, false);
							} else {
//								startActivityForLeft(LoginActivity.class,
//										intent, false);
							}
							
							
							runNextPage();

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							startActivityForLeft(LoginActivity.class, intent,
									false);
							e.printStackTrace();
						}
						super.onSuccess(statusCode, headers, response);
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

		// 07-21 22:18:31.226: I/test(15270):
		// client_id:5f7f29537bb240b9b5ec45f151e06d8d
		// 07-21 22:18:31.226: I/test(15270):
		// PASSWORD:5f7f2953-7bb2-40b9-b5ec-45f151e06d8d

		easemobchatImp.getInstance().login(client_id.replace("-", ""),
				mShareFileUtils.getString(Constant.PASSWORD, ""));
		easemobchatImp.getInstance().loadConversationsandGroups();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

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

}
