package com.peer.adapter;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.peer.R;
import com.peer.activity.CardActivity;
import com.peer.activity.EditCardActivity;
import com.peer.activity.OtherPageActivity;
import com.peer.activity.PersonalMessageActivity;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.base.pBaseAdapter;
import com.peer.bean.CardChangeBean;
import com.peer.bean.FindCardBean;
import com.peer.bean.InvitationBean;
import com.peer.bean.PersonpageBean;
import com.peer.bean.UserBean;
import com.peer.event.NewFriensEvent;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.utils.CardDbHelper;
import com.peer.utils.ImageLoaderUtil;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.pLog;

import de.greenrobot.event.EventBus;

public class NewCardAdapter extends pBaseAdapter {
	private Context mContext;
	private List<CardChangeBean> mlist;
//	private String pic_server;

	public NewCardAdapter(Context mContext, List<CardChangeBean> mlist) {
		super(mContext);
		this.mContext = mContext;
		this.mlist = mlist;
//		this.pic_server = pic_server;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mlist.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView,
			ViewGroup parentgroup) {
		// TODO Auto-generated method stub
		convertView = LayoutInflater.from(mContext).inflate(
				R.layout.adapter_list_card, null, false);
		ImageView im_headpic = (ImageView) convertView
				.findViewById(R.id.im_headpic);
		TextView tv_nikename = (TextView) convertView
				.findViewById(R.id.tv_nikename);
		TextView tv_date = (TextView) convertView
				.findViewById(R.id.tv_date);
		TextView tv_descripe = (TextView) convertView
				.findViewById(R.id.tv_descripe);
		RelativeLayout ll_clike = (RelativeLayout) convertView
				.findViewById(R.id.ll_clike);
		TextView tv_access = (TextView) convertView
				.findViewById(R.id.tv_access);

		final CardChangeBean user = mlist.get(position);
		tv_nikename.setText(user.getName());
		tv_date.setText(user.getDate().split(" ")[0]);
		if(user.getType().equals("1")){        //type==1 对应推送7
			tv_access.setText("同意");   
			tv_descripe.setText("请求与您交换名片");
		}else{						//type==2对应推送8
			tv_access.setText("查看");	
			tv_descripe.setText("同意交换名片");
		}
		// ImageLoader加载图片
		ImageLoaderUtil.getInstance().showHttpImage(mContext,
				((pBaseActivity)mContext).mShareFileUtils.getString(Constant.PIC_SERVER, "")
				+ user.getImage(), im_headpic,
				R.drawable.mini_avatar_shadow);

		tv_access.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if(user.getType().equals("1")){
					
					try {
						sendcard(
								((pBaseActivity)mContext).mShareFileUtils
								.getString(Constant.CLIENT_ID, "")
								,user.getPushid()
								,position
								,user.getDate());
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}else{
					EventBus.getDefault().post(
							new NewFriensEvent(position));
					Intent intent = new Intent(mContext, CardActivity.class);
					intent.putExtra("other_id",user.getPushid());
					intent.putExtra("date", user.getDate());
					mContext.startActivity(intent);
				}

			}
		});
		
		ll_clike.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				// TODO Auto-generated method stub
				final String[] items = {"删除该选项"};
				new AlertDialog.Builder(mContext)
				.setTitle(((pBaseActivity)mContext).getResources().getString(R.string.sex))
				.setItems(items, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						String client_id = ((pBaseActivity)mContext).mShareFileUtils
						.getString(Constant.CLIENT_ID, "");
						pLog.i("test", "本地id:"+client_id);
						Boolean delete_flag;
						CardDbHelper.setmContext(mContext);
						delete_flag = CardDbHelper.delCard(user.getPushid()
								,client_id,user.getDate());
						if(delete_flag){
							pLog.i("test", "数据删除成功！");
						}else{
							pLog.i("test", "数据删除失败！");
						}
						EventBus.getDefault().post(
								new NewFriensEvent(position));
					}
				}).show();
				return false;
			}
		});
		/*ll_clike.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (((pBaseActivity) mContext).isNetworkAvailable) {

					PersonpageBean.getInstance().setUser(
							invitation.getUserbean());
					Intent intent = new Intent(mContext,
							OtherPageActivity.class);
					intent.putExtra("client_id", invitation.getUserbean().getClient_id());
					mContext.startActivity(intent);
				} else {
					((pBaseActivity) mContext).showToast(
							mContext.getResources().getString(
									R.string.Broken_network_prompt),
							Toast.LENGTH_SHORT, false);
				}
			}
		});*/
		return convertView;
	}

	
	
	
	/**
	 * 同意交换名片请求
	 * 
	 * @param client_id
	 * @param friend_id
	 * @throws UnsupportedEncodingException
	 */
	private void sendagreecard(String client_id, final String friend_id ,final int position,final String date) {
		
		RequestParams params = null;
		try {
			params = PeerParamsUtils.getdeletefriendParams(mContext, friend_id);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		HttpUtil.post(mContext, HttpConfig.CHANGE_CARD_URL + client_id +"/agree/"+friend_id+ ".json",
				params, new JsonHttpResponseHandler() {
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				((pBaseActivity)mContext).showToast(
						((pBaseActivity)mContext).getResources().getString(R.string.config_error),
						Toast.LENGTH_SHORT, false);
				super.onFailure(statusCode, headers, responseString,
						throwable);
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONArray errorResponse) {
				// TODO Auto-generated method stub
				((pBaseActivity)mContext).showToast(
						((pBaseActivity)mContext).getResources().getString(R.string.config_error),
						Toast.LENGTH_SHORT, false);
				super.onFailure(statusCode, headers, throwable,
						errorResponse);
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				((pBaseActivity)mContext).showToast(
						((pBaseActivity)mContext).getResources().getString(R.string.config_error),
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
						EventBus.getDefault().post(
								new NewFriensEvent(position));
						Intent intent = new Intent(mContext, CardActivity.class);
						intent.putExtra("other_id",friend_id);
						intent.putExtra("date",date);
						mContext.startActivity(intent);
					} else if (code.equals("500")) {
						
					} else {
						String message = result.getString("message");
						((pBaseActivity)mContext).showToast(message, Toast.LENGTH_SHORT, false);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
	}
	
	
	
	/**
	 * 获取名片信息接口
	 * 
	 * @param other_id
	 * @throws UnsupportedEncodingException
	 */

	private void sendcard(final String client_id,final String other_id ,final int position,final String date)
			throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		final Intent intent = new Intent();
		RequestParams params = null;
		try {
			params = PeerParamsUtils.getCardParams(mContext,
					client_id);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpUtil.post(HttpConfig.CARD_IN_URL + client_id + "/get.json"
				, params, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				((pBaseActivity)mContext).showToast(((pBaseActivity)mContext).getResources().getString(R.string.config_error),
						Toast.LENGTH_SHORT, false);
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONArray errorResponse) {
				// TODO Auto-generated method stub
				((pBaseActivity)mContext).showToast(((pBaseActivity)mContext).getResources().getString(R.string.config_error),
						Toast.LENGTH_SHORT, false);
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				((pBaseActivity)mContext).showToast(((pBaseActivity)mContext).getResources().getString(R.string.config_error),
						Toast.LENGTH_SHORT, false);
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				try {
					JSONObject result = response.getJSONObject("success");

					String code = result.getString("code");
					if (code.equals("200")) {
						FindCardBean findcardBean = JsonDocHelper.toJSONObject(
								response.getJSONObject("success").toString(),
								FindCardBean.class);
						if (findcardBean != null) {
							((pBaseActivity)mContext).mShareFileUtils.setString(Constant.CARD_ADMISSIONTIME, findcardBean.card.getSchool_date());
							((pBaseActivity)mContext).	mShareFileUtils.setString(Constant.CARD_COMPANY, findcardBean.card.getCompanyname());
							((pBaseActivity)mContext).mShareFileUtils.setString(Constant.CARD_REALNAME, findcardBean.card.getRealname());
							((pBaseActivity)mContext).mShareFileUtils.setString(Constant.CARD_EMAIL, findcardBean.card.getEmail());
							((pBaseActivity)mContext).	mShareFileUtils.setString(Constant.CARD_EXPERIENCE, findcardBean.card.getDiploma());
							((pBaseActivity)mContext).mShareFileUtils.setString(Constant.CARD_MAJOR, findcardBean.card.getMajor());
							((pBaseActivity)mContext).mShareFileUtils.setString(Constant.CARD_PHONE, findcardBean.card.getPhone());
							((pBaseActivity)mContext).mShareFileUtils.setString(Constant.CARD_POSITION, findcardBean.card.getPosition());
							((pBaseActivity)mContext).mShareFileUtils.setString(Constant.CARD_SCHOOL, findcardBean.card.getSchoolname());
							((pBaseActivity)mContext).mShareFileUtils.setString(Constant.CARD_WORKTIME, findcardBean.card.getWork_date());
							
							if(((pBaseActivity)mContext).mShareFileUtils.getString(Constant.CARD_COMPANY, "").equals("")){
								
								//传1代表为用户是从个人主页到编辑名片页面
								//传2代表用户是从交换名片进入编辑名片页面
								//传3代表用户从同意交换名片进入编辑名片页面
								Intent intent = new Intent(mContext, EditCardActivity.class);
								intent.putExtra("flag", "3");
								intent.putExtra("client_id",other_id);
								intent.putExtra("date", date);
								mContext.startActivity(intent);
							}else{
								//交换名片
								if(((pBaseActivity)mContext).isNetworkAvailable){
									sendagreecard(
											((pBaseActivity)mContext).mShareFileUtils.getString(Constant.CLIENT_ID, ""),other_id,position,date);
//									Intent intent = new Intent(OtherPageActivity.this, CardActivity.class);
//									intent.putExtra("other_id",mShareFileUtils.getString(Constant.CLIENT_ID, ""));
//									startActivity(intent);
								}else{
									((pBaseActivity)mContext).showToast(
											((pBaseActivity)mContext).
											getResources().getString(R.string.Broken_network_prompt)
											, Toast.LENGTH_SHORT, false);
								}
							}
						}
					} else if (code.equals("500")) {

					} else {
						String message = result.getString("message");
						((pBaseActivity)mContext).showToast(message, Toast.LENGTH_SHORT, false);
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				super.onSuccess(statusCode, headers, response);

			}

		});
	}
}
