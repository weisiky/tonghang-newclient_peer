package com.peer.activity;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.peer.R;
import com.peer.base.Constant;
import com.peer.base.CustomDialog;
import com.peer.base.pBaseActivity;
import com.peer.bean.CardBean;
import com.peer.bean.ChatRoomBean;
import com.peer.bean.FindCardBean;
import com.peer.bean.UserBean;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.utils.CardDbHelper;
import com.peer.utils.ImageLoaderUtil;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.pLog;
import com.peer.utils.pViewBox;

public class CardActivity extends pBaseActivity{
	class PageViewList {
		private RelativeLayout ll_icon;
		private ImageView personhead;
		private TextView tv_title,tv_name
				,tv_position,tv_company
				,tv_work,tv_worktime
				,tv_school,tv_major
				,tv_study,tv_time,tv_change_card
				,tv_chat_after;
		private Button btnSend;
	}

	private PageViewList pageViewaList;
	
	private CardBean card;
	private UserBean user;
	private String picserver;
	private String other_id;
	private String date;
	
	private boolean flag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card);
		findViewById();
		setListener();
		processBiz();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		pageViewaList = new PageViewList();
		pViewBox.viewBox(this, pageViewaList);
		
		Intent intent = getIntent();
		other_id = intent.getStringExtra("other_id");
		date = intent.getStringExtra("date");
		try {
			sendcard(other_id);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_icon.setOnClickListener(this);
		pageViewaList.btnSend.setOnClickListener(this);
	}

	@Override
	protected void processBiz() {
		// TODO Auto-generated method stub

	}

	// @Override
	// protected View loadTopLayout() {
	// // TODO Auto-generated method stub
	// // return getLayoutInflater().inflate(R.layout.top_layout, null);
	// return getLayoutInflater().inflate(R.layout.base_toplayout_title, null);
	// }
	//
	// @Override
	// protected View loadContentLayout() {
	// // TODO Auto-generated method stub
	// return getLayoutInflater().inflate(R.layout.activity_xieyi, null);
	// }
	//
	// @Override
	// protected View loadBottomLayout() {
	// // TODO Auto-generated method stub
	// return null;
	// }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_icon:
			flag = true;
			showselectDialog(this);
			break;
		case R.id.btnSend:
			flag = false;
			showselectDialog(this);
			
			break;

		default:
			break;
		}

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
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// 返回上个页面
			flag = true;
			showselectDialog(this);
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}
	
	
	
	/**
	 * 获取名片信息接口
	 * 
	 * @param other_id
	 * @throws UnsupportedEncodingException
	 */

	private void sendcard(String other_id)
			throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		final Intent intent = new Intent();
		RequestParams params = null;
		try {
			params = PeerParamsUtils.getCardParams(CardActivity.this,
					other_id);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpUtil.post(HttpConfig.CARD_IN_URL + other_id + "/get.json"
				, params, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				showToast(getResources().getString(R.string.config_error),
						Toast.LENGTH_SHORT, false);
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONArray errorResponse) {
				// TODO Auto-generated method stub
				showToast(getResources().getString(R.string.config_error),
						Toast.LENGTH_SHORT, false);
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				showToast(getResources().getString(R.string.config_error),
						Toast.LENGTH_SHORT, false);
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				pLog.i("test", "card_response:"+response.toString());
				try {
					JSONObject result = response.getJSONObject("success");

					String code = result.getString("code");
					if (code.equals("200")) {
						FindCardBean findcardBean = JsonDocHelper.toJSONObject(
								response.getJSONObject("success").toString(),
								FindCardBean.class);
						if (findcardBean != null) {
							picserver = findcardBean.getPic_server();
							user = findcardBean.user;
							card = findcardBean.card;
							ViewType(user,card);
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
	
	
	private void ViewType(UserBean userbean , CardBean cardbean) {
		// TODO Auto-generated method stub
		pageViewaList.tv_title
					.setText(userbean.getUsername());
		pageViewaList.tv_name
					.setText(cardbean.getRealname());
		pageViewaList.tv_position
					.setText(cardbean.getPosition());
		pageViewaList.tv_company
					.setText(cardbean.getCompanyname());
		pageViewaList.tv_work
					.setText(cardbean.getCompanyname());
		pageViewaList.tv_worktime
					.setText(cardbean.getWork_date());
		pageViewaList.tv_school
					.setText(cardbean.getSchoolname());
		pageViewaList.tv_major
					.setText(cardbean.getMajor());
		pageViewaList.tv_study
					.setText(cardbean.getDiploma());
		pageViewaList.tv_time
					.setText(cardbean.getSchool_date());
		pageViewaList.tv_change_card
					.setText(cardbean.getExchange_times());
		pageViewaList.tv_chat_after
					.setText(cardbean.getChat_times());
		
		ImageLoaderUtil.getInstance().showHttpImage(this,
				picserver+userbean.getImage(), pageViewaList.personhead,
				R.drawable.mini_avatar_shadow);
		
	}
	
	
	/**
	 * 选择框
	 */

	public void showselectDialog(final Context mcontext) {
		// TODO Auto-generated method stub

		final CustomDialog customDialog = new CustomDialog(this, R.style.dialog);
		customDialog.setContentView(R.layout.dialog_no_icon);

		((TextView) customDialog.findViewById(R.id.txtMsg))
				.setText("关闭后，名片信息将会消失，再次查看需对方同意！");

		customDialog.setTitle("温馨提示");
		Button btnSure = ((Button) customDialog.findViewById(R.id.btnSure));
		btnSure.setText("确定");
		Button btnCancel = ((Button) customDialog.findViewById(R.id.btnCancel));
		btnCancel.setText("取消");
		btnSure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String client_id = mShareFileUtils.getString(Constant.CLIENT_ID, "");
				Boolean delete_flag;
				CardDbHelper.setmContext(mcontext);
				delete_flag = CardDbHelper.delCard(other_id,client_id,date);
				if(delete_flag){
					pLog.i("test", "数据删除成功！");
				}else{
					pLog.i("test", "数据删除失败！");
				}
				
				customDialog.cancel();
				if(flag){
					finish();
				}else{
					if(user!=null){
						ChatRoomBean.getInstance().setChatroomtype(
								Constant.SINGLECHAT);
						// ChatRoomBean.getInstance().setUserBean(userbean);
						Intent intent = new Intent();
						intent.putExtra("userbean", user);
						startActivityForLeft(SingleChatRoomActivity.class, intent,
								false);
					}else{
						showToast(getResources().getString(R.string.Broken_network_prompt), Toast.LENGTH_SHORT, false);
					}
				}
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				customDialog.cancel();
			}
		});
		
		customDialog.setOnCancelListener(new OnCancelListener() {
	        
	        public void onCancel(DialogInterface dialog) {
	            // TODO Auto-generated method stub
	    
	               // Toast.makeText(getBaseContext(), "点击了back", Toast.LENGTH_SHORT).show();
	        }
	    });
		
		customDialog.show();
	}
	
	
}
