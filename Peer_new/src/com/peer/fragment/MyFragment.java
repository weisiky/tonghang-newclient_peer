package com.peer.fragment;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.peer.activity.ChatRoomListnikeActivity;
import com.peer.activity.MyAcountActivity;
import com.peer.activity.MySkillActivity;
import com.peer.activity.PersonalMessageActivity;
import com.peer.activity.PersonalPageActivity;
import com.peer.R;
import com.peer.activity.SettingActivity;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.base.pBaseFragment;
import com.peer.bean.LoginBean;
import com.peer.bean.PersonpageBean;
import com.peer.bean.UserBean;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.utils.BussinessUtils;
import com.peer.utils.ImageLoaderUtil;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.pLog;

/**
 * ‘我的’fragment
 * 
 */
public class MyFragment extends pBaseFragment {
	private LinearLayout ll_top_my, linearLayout1, linearLayout2,
			ll_personmessage_my, ll_mytag_my, ll_setting_my;
	private RelativeLayout rl_ponseralpage, rl_myacount_my;
	private ImageView im_headpic;
	private TextView tv_nikename, tv_email;

	private pBaseActivity pbaseActivity;

	public static final int UPDATESUCESS = 9;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_my, container, false);
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

		rl_ponseralpage = (RelativeLayout) getView().findViewById(
				R.id.rl_ponseralpage);
		rl_myacount_my = (RelativeLayout) getView().findViewById(
				R.id.rl_myacount_my);
		ll_personmessage_my = (LinearLayout) getView().findViewById(
				R.id.ll_personmessage_my);
		ll_mytag_my = (LinearLayout) getView().findViewById(R.id.ll_mytag_my);
		ll_setting_my = (LinearLayout) getView().findViewById(
				R.id.ll_setting_my);
		im_headpic = (ImageView) getView().findViewById(R.id.im_headpic);
		tv_nikename = (TextView) getView().findViewById(R.id.tv_nikename);
		tv_email = (TextView) getView().findViewById(R.id.tv_email);

		rl_ponseralpage.setOnClickListener(this);
		rl_myacount_my.setOnClickListener(this);
		ll_personmessage_my.setOnClickListener(this);
		ll_mytag_my.setOnClickListener(this);
		ll_setting_my.setOnClickListener(this);

		getlocalMsg();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getlocalMsg();

	}

	public void getlocalMsg() {
		// ImageLoader加载图片
		File file = new File(Constant.C_IMAGE_CACHE_PATH + "head.png");// 将要保存图片的路径
		if (file.exists()) {
			im_headpic.setImageBitmap(BussinessUtils.decodeFile(
					Constant.C_IMAGE_CACHE_PATH + "head.png", 100));
			ImageLoaderUtil.getInstance().showHttpImage(getActivity(),
					mShareFileUtils.getString(Constant.PIC_SERVER, "")
							+ mShareFileUtils.getString(Constant.IMAGE, ""),
					im_headpic, R.drawable.mini_avatar_shadow);

		} else {
			ImageLoaderUtil.getInstance().showHttpImage(getActivity(),
					mShareFileUtils.getString(Constant.PIC_SERVER, "")
							+ mShareFileUtils.getString(Constant.IMAGE, ""),
					im_headpic, R.drawable.mini_avatar_shadow);
		}
		tv_nikename.setText(mShareFileUtils.getString(Constant.USERNAME, ""));
		tv_email.setText(mShareFileUtils.getString(Constant.EMAIL, ""));
		// if (LoginBean.getInstance().user == null) {
		// try {
		// senduser(mShareFileUtils.getString(Constant.CLIENT_ID, ""));
		// } catch (UnsupportedEncodingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.rl_myacount_my:
			Intent myacount = new Intent(getActivity(), MyAcountActivity.class);
			startActivity(myacount);
			break;
		case R.id.ll_personmessage_my:
			Intent personmessage = new Intent(getActivity(),
					PersonalMessageActivity.class);
			startActivityForResult(personmessage, UPDATESUCESS);
			break;
		case R.id.ll_mytag_my:
			// PersonpageBean.getInstance().setUser(LoginBean.getInstance().user);
			Intent skillting = new Intent(getActivity(), MySkillActivity.class);
			skillting.putExtra("labels",
					mShareFileUtils.getString(Constant.LABELS, ""));
			startActivity(skillting);
			break;
		case R.id.ll_setting_my:
			Intent setting = new Intent(getActivity(), SettingActivity.class);
			startActivity(setting);
			break;
		case R.id.rl_ponseralpage:
				PersonpageBean.getInstance().setUser(initUserBean());
				Intent myting = new Intent(getActivity(),
						PersonalPageActivity.class);
				startActivity(myting);
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("static-access")
	private UserBean initUserBean() {
		// TODO Auto-generated method stub
		UserBean userBean = new UserBean();

		userBean.setBirth(mShareFileUtils.getString(Constant.BIRTH, ""));
		userBean.setCity(mShareFileUtils.getString(Constant.CITY, ""));
		userBean.setClient_id(mShareFileUtils.getString(Constant.CLIENT_ID, ""));
		userBean.setCreated_at(mShareFileUtils.getString(Constant.CREATED_AT,
				""));
		userBean.setEmail(mShareFileUtils.getString(Constant.EMAIL, ""));
		userBean.setImage(mShareFileUtils.getString(Constant.IMAGE, ""));
		userBean.setIs_friend(mShareFileUtils.getBoolean(Constant.IS_FRIEND,
				true));
		ArrayList<String> labels = JsonDocHelper.toJSONArrary(
				mShareFileUtils.getString(Constant.LABELS, ""), String.class);
		userBean.setLabels(labels);
		userBean.setPhone(mShareFileUtils.getString(Constant.PHONE, ""));
		userBean.setSex(mShareFileUtils.getString(Constant.SEX, ""));
		userBean.setUsername(mShareFileUtils.getString(Constant.USERNAME, ""));
		return userBean;
	}

	/**
	 * 获取用户信息接口
	 * 
	 * @param client_id
	 * @throws UnsupportedEncodingException
	 */

	private void senduser(String client_id) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		final Intent intent = new Intent();
		// HttpEntity entity = null;
		// try {
		// entity = PeerParamsUtils.getUserParams(getActivity(),client_id);
		// } catch (Exception e2) {
		// // TODO Auto-generated catch block
		// e2.printStackTrace();
		// }

		RequestParams params = null;
		try {
			params = PeerParamsUtils.getUserParams(getActivity(), client_id);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		HttpUtil.post(getActivity(), HttpConfig.USER_IN_URL + client_id
				+ ".json", params, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				pbaseActivity.hideLoading();
				showToast(getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONArray errorResponse) {
				// TODO Auto-generated method stub
				pbaseActivity.hideLoading();
				showToast(getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				pbaseActivity.hideLoading();
				showToast(getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

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
						if (loginBean != null) {
							BussinessUtils.saveUserData(loginBean, mShareFileUtils);
							
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
}
