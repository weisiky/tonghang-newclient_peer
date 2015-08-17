package com.peer.adapter;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.peer.IMimplements.easemobchatImp;
import com.peer.activity.MultiChatRoomActivity;
import com.peer.R;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.base.pBaseAdapter;
import com.peer.bean.ChatRoomBean;
import com.peer.bean.TopicBean;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.utils.ManagerActivity;
import com.peer.utils.ViewHolder;
import com.peer.utils.pLog;
import com.tencent.mm.sdk.modelmsg.ShowMessageFromWX;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class TopicAdapter extends pBaseAdapter {
	private Context mContext;
	private List<TopicBean> mlist;
	public TopicAdapter(Context mContext,List<TopicBean> list){
		super(mContext);
		this.mContext=mContext;
		this.mlist=list;
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
	public View getView(final int position, View convertView, ViewGroup parentgroup) {
		// TODO Auto-generated method stub
			convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_home_listtopic,null,false);
			TextView tv_time=ViewHolder.get(convertView, R.id.tv_time);
			tv_time.setVisibility(View.VISIBLE);
			TextView tv_skill=ViewHolder.get(convertView, R.id.tv_skill);			
			TextView tv_topic=ViewHolder.get(convertView, R.id.tv_topic);
			LinearLayout ll_clike=ViewHolder.get(convertView, R.id.ll_clike);
		final TopicBean topic= mlist.get(position);
		tv_time.setText(topic.getCreated_at());
		tv_skill.setText(topic.getLabel_name());
		tv_topic.setText(topic.getSubject());
		ll_clike.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub				
				if(((pBaseActivity)mContext).isNetworkAvailable){
					ChatRoomBean.getInstance().setChatroomtype(Constant.MULTICHAT);
					ChatRoomBean.getInstance().setTopicBean(topic);
					String ownerid=null;
					ownerid =((pBaseActivity)mContext).mShareFileUtils.getString(Constant.CLIENT_ID, "") ;	
					if(topic.getUser_id().equals(ownerid)){
						ChatRoomBean.getInstance().setIsowner(true);
					}else{
						ChatRoomBean.getInstance().setIsowner(false);
					}
					try {
						if (EMChatManager.getInstance().isConnected()) {
							
						}else{
							easemobchatImp.getInstance().login(
									ownerid.replace("-", ""),
									((pBaseActivity)mContext).mShareFileUtils.getString(
											Constant.PASSWORD, ""));
						}
						//从环信服务器获取自己加入的和创建的群聊列表
						//需异步处理
						List<EMGroup> grouplist = EMGroupManager.getInstance().getGroupsFromServer();
						for(int i= 0 ;i<grouplist.size();i++ ){
							pLog.i("test","grouplist.get(0):"+grouplist.get(i));
							pLog.i("test","getGroupId():"+grouplist.get(i).getGroupId());
							pLog.i("test","getOwner():"+grouplist.get(i).getOwner());
							if(ownerid.equals(grouplist.get(i).getOwner())){
								sendleavegroup(ownerid
										, grouplist.get(i).getGroupId()
										,true);
							}else{
								sendleavegroup(ownerid
										 , grouplist.get(i).getGroupId()
										 ,false);
								easemobchatImp.getInstance().exitgroup(grouplist.get(i).getGroupId());
							}
						}
						ManagerActivity.getAppManager().finishMulActivity();
						Intent intent=new Intent();
						((pBaseActivity)mContext).startActivityForLeft(MultiChatRoomActivity.class, intent, false);
					} catch (EaseMobException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					((pBaseActivity)mContext).showToast(mContext.getResources()
							.getString(R.string.Broken_network_prompt), Toast.LENGTH_SHORT, false);
				}
				
			}
		});
		
		return convertView;
	}
	
	/**
	 * 退出话题请求
	 * 
	 * @param client_id
	 * @param topic_id
	 * @exception UnsupportedEncodingException
	 */
	private void sendleavegroup(String client_id, String topic_id , boolean isOwner) {
		// TODO Auto-generated method stub
		final Intent intent = new Intent();
		RequestParams params = null;
		try {
			params = PeerParamsUtils.getJoinParams(mContext,
					client_id, topic_id,isOwner);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpUtil.post(HttpConfig.LEAVE_TOPIC_IN_URL, params,
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub

						super.onFailure(statusCode, headers, responseString,
								throwable);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONArray errorResponse) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
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
							String code = String.valueOf(result.get("code"));
							pLog.i("test", "code:"+code);
							if (code.equals("200")) {
								((pBaseActivity)mContext).showToast("正在进入新话题...", Toast.LENGTH_SHORT, false);
							}else if(code.equals("500")){
								
							}else{
								String message = result.getString("message");
								((pBaseActivity)mContext).showToast(message, Toast.LENGTH_SHORT, false);
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
