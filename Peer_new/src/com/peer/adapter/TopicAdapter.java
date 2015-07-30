package com.peer.adapter;

import java.util.List;
import java.util.Map;

import com.peer.activity.R;
import com.peer.base.pBaseActivity;
import com.peer.base.pBaseAdapter;
import com.peer.bean.TopicBean;
import com.peer.utils.ViewHolder;

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
/*					ChatRoomTypeUtil.getInstance().setChatroomtype(Constant.MULTICHAT);
					ChatRoomTypeUtil.getInstance().setTitle(topic.getLabel_name());
					ChatRoomTypeUtil.getInstance().setTheme(topic.getSubject());
					ChatRoomTypeUtil.getInstance().setTopicId(topic.getTopicid());
					ChatRoomTypeUtil.getInstance().setUser(topic.getUser());
					ChatRoomTypeUtil.getInstance().setHuanxingId(topic.getHuangxin_group_id());
					String ownerid=null;*/
//					try {
//						ownerid = PeerUI.getInstance().getISessionManager().getUserId();
//					} catch (RemoteException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}	
//					if(topic.getUser().getUserid().equals(ownerid)){
//						ChatRoomTypeUtil.getInstance().setIsowner(true);
//					}else{
//						ChatRoomTypeUtil.getInstance().setIsowner(false);
//					}	
//					ChatRoomTypeUtil.getInstance().setTopic(topic);
//					Intent intent=new Intent(mContext,ChatRoomActivity.class);
//					intent.putExtra("topicid",String.valueOf(topic.getTopicid()));
//					mContext.startActivity(intent);
				}else{
					Toast.makeText(mContext, mContext.getResources().getString(R.string.Broken_network_prompt), 0).show();
				}
				
			}
		});
		
		return convertView;
	}
}
