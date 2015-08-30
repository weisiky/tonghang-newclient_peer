package com.peer.adapter;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.peer.R;
import com.peer.activity.PersonalPageActivity;
import com.peer.base.pBaseActivity;
import com.peer.base.pBaseAdapter;
import com.peer.bean.InvitationBean;
import com.peer.bean.PersonpageBean;
import com.peer.bean.User;
import com.peer.bean.UserBean;
import com.peer.event.NewFriensEvent;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.utils.ImageLoaderUtil;
import com.peer.utils.pLog;

import de.greenrobot.event.EventBus;

public class BlacklistAdapter extends pBaseAdapter{
	private Context mContext;
	private List<UserBean> mlist;
	private boolean status;
	private String pic_server;

	public BlacklistAdapter(Context mContext, List<UserBean> mlist,String pic_server) {
		super(mContext);
		this.mContext = mContext;
		this.mlist = mlist;
		this.pic_server = pic_server;
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
				R.layout.adapter_listnike_friends, null, false);
		ImageView im_headpic = (ImageView) convertView
				.findViewById(R.id.im_headpic);
		TextView tv_nikename = (TextView) convertView
				.findViewById(R.id.tv_nikename);
		TextView tv_descripe = (TextView) convertView
				.findViewById(R.id.tv_descripe);
		LinearLayout ll_clike = (LinearLayout) convertView
				.findViewById(R.id.ll_clike);
		
		TextView tv_recovery = (TextView) convertView
				.findViewById(R.id.tv_recovery);
		tv_recovery.setVisibility(View.VISIBLE);

		final UserBean invitation = mlist.get(position);
		tv_nikename.setText(invitation.getUsername());
		// ImageLoader加载图片
		ImageLoaderUtil.getInstance().showHttpImage(mContext,
				pic_server + invitation.getImage(), im_headpic,
				R.drawable.mini_avatar_shadow);

		
		tv_recovery.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				status = true;
				if(((pBaseActivity) mContext).isNetworkAvailable){
				try {
					sendrecovery(((pBaseActivity) mContext).mShareFileUtils
									.getString("client_id", ""),invitation.getClient_id(),position);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}else{
					((pBaseActivity)mContext).showToast("您的网络未连接，请稍后再试！",Toast.LENGTH_SHORT,false);
				}

			}
		});
		ll_clike.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (((pBaseActivity) mContext).isNetworkAvailable) {

					PersonpageBean.getInstance().setUser(
							invitation);
					Intent intent = new Intent(mContext,
							PersonalPageActivity.class);
					mContext.startActivity(intent);
				} else {
					((pBaseActivity) mContext).showToast(
							mContext.getResources().getString(
									R.string.Broken_network_prompt),
							Toast.LENGTH_SHORT, false);
				}
			}
		});
		return convertView;
	}

	/**
	 * 还原黑名单用户请求
	 * 
	 * @param client_id
	 * @param blocker_id
	 * @throws UnsupportedEncodingException
	 */
	private void sendrecovery(String client_id,String blocker_id,final int position
			) throws UnsupportedEncodingException {
		
		// TODO Auto-generated method stub
		RequestParams params = null;
		try {
			params = PeerParamsUtils.getblacklistParams(mContext);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpUtil.post(HttpConfig.DROPBLOCK_GET_URL + client_id + "/deblocks/" + blocker_id
				+ ".json",params, new JsonHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				pLog.i("test", "statusCode:" + statusCode);
				pLog.i("test", "headers:" + headers);
				pLog.i("test", "throwable:" + throwable);
				pLog.i("test", "errorResponse:" + errorResponse);
				((pBaseActivity)mContext).showToast(((pBaseActivity)mContext).getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub

				pLog.i("test", "statusCode:" + statusCode);
				pLog.i("test", "headers:" + headers);
				pLog.i("test", "responseString:" + responseString);
				pLog.i("test", "throwable:" + throwable);
				((pBaseActivity)mContext).showToast(((pBaseActivity)mContext).getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONArray errorResponse) {
				// TODO Auto-generated method stub

				pLog.i("test", "statusCode:" + statusCode);
				pLog.i("test", "headers:" + headers);
				pLog.i("test", "throwable:" + throwable);
				pLog.i("test", "errorResponse:" + errorResponse);
				((pBaseActivity)mContext).showToast(((pBaseActivity)mContext).getResources().getString(R.string.config_error), Toast.LENGTH_SHORT, false);
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
					if (code.equals("200")) {

							// TODO Auto-generated method stub
							EventBus.getDefault().post(
									new NewFriensEvent(position));
							((pBaseActivity) mContext).showToast("还原该好友",
									Toast.LENGTH_SHORT, false);

					}else if(code.equals("500")){
						
					}else{
						String message = result.getString("message");
						((pBaseActivity) mContext).showToast(message
								, Toast.LENGTH_SHORT, false);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				super.onSuccess(statusCode, headers, response);
			}

		});

	}
}
