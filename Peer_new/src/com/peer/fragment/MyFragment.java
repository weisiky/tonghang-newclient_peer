package com.peer.fragment;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
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
import com.peer.IMimplements.easemobchatImp;
import com.peer.activity.MainActivity;
import com.peer.activity.MyAcountActivity;
import com.peer.activity.MySkillActivity;
import com.peer.activity.PersonalMessageActivity;
import com.peer.activity.PersonalPageActivity;
import com.peer.activity.R;
import com.peer.activity.SettingActivity;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.base.pBaseFragment;
import com.peer.bean.LoginBean;
import com.peer.bean.PersonpageBean;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.utils.BussinessUtils;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.pLog;
import com.peer.utils.pShareFileUtils;


/**
 *  ‘我的’fragment
 * 
 */
public class MyFragment extends pBaseFragment{
	private LinearLayout ll_top_my,linearLayout1,linearLayout2,ll_personmessage_my,ll_mytag_my,ll_setting_my;
	private RelativeLayout rl_ponseralpage,rl_myacount_my;
	private ImageView im_headpic;
	private TextView tv_nikename,tv_email;
	
	private pBaseActivity pbaseActivity;
	
	public static final int UPDATESUCESS=9;


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
			
	
		rl_ponseralpage=(RelativeLayout)getView().findViewById(R.id.rl_ponseralpage);		
		rl_myacount_my=(RelativeLayout)getView().findViewById(R.id.rl_myacount_my);
		ll_personmessage_my=(LinearLayout)getView().findViewById(R.id.ll_personmessage_my);
		ll_mytag_my=(LinearLayout)getView().findViewById(R.id.ll_mytag_my);
		ll_setting_my=(LinearLayout)getView().findViewById(R.id.ll_setting_my);
		im_headpic=(ImageView)getView().findViewById(R.id.im_headpic);
		tv_nikename=(TextView)getView().findViewById(R.id.tv_nikename);
		tv_email=(TextView)getView().findViewById(R.id.tv_email);
		
		
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
	
	
	public void getlocalMsg(){
		im_headpic.setBackgroundDrawable(getResources().getDrawable(R.drawable.mini_avatar_shadow));
		tv_nikename.setText(mShareFileUtils.getString(Constant.USERNAME,""));
		tv_email.setText(mShareFileUtils.getString(Constant.EMAIL,""));	
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {				
		case R.id.rl_myacount_my:
			Intent myacount=new Intent(getActivity(),MyAcountActivity.class);
			startActivity(myacount);
			break;
		case R.id.ll_personmessage_my:
			Intent personmessage=new Intent(getActivity(),PersonalMessageActivity.class);
			startActivityForResult(personmessage, UPDATESUCESS);
			break;
		case R.id.ll_mytag_my:
			Intent mytag=new Intent(getActivity(),MySkillActivity.class);
			startActivity(mytag);
			break;
		case R.id.ll_setting_my:
			Intent setting=new Intent(getActivity(),SettingActivity.class);
			startActivity(setting);
			break;
		case R.id.rl_ponseralpage:
			if(pbaseActivity.isNetworkAvailable){
				try {
					senduser(mShareFileUtils.getString(Constant.CLIENT_ID, ""));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				
			}

		
			break;
		default:
			break;	
		}
	}
	
	/**
	 * 获取用户信息接口
	 * 
	 * @param client_id
	 * @throws UnsupportedEncodingException
	 */

	private void senduser(String client_id)
			throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		final Intent intent = new Intent();
		HttpEntity entity = null;
		try {
			entity = PeerParamsUtils.getUserParams(getActivity(),client_id);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		HttpUtil.post(getActivity(), HttpConfig.USER_IN_URL+client_id+".json", entity,
				"application/json;charset=utf-8",
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
						try {
							LoginBean loginBean = JsonDocHelper.toJSONObject(
									response.getJSONObject("success")
											.toString(), LoginBean.class);
							if (loginBean != null) {
								BussinessUtils.saveUserData(loginBean,
										mShareFileUtils);
								PersonpageBean.getInstance().setUser(LoginBean.getInstance().user);
								pbaseActivity.startActivityForLeft(PersonalPageActivity.class, intent, false);
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
