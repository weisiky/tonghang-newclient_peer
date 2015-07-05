package com.peer.adapter;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


import com.peer.activity.R;
import com.peer.base.pBaseAdapter;
import com.peer.utils.ViewHolder;


/*
 * 标签adapter
 * */
public class SkillAdapter extends pBaseAdapter{
	private Context mContext;
	private List<String> mlist;
	public SkillAdapter(Context mContext,List<String> mlist){
		super(mContext);
		this.mContext=mContext;
		this.mlist=mlist;
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

		if(convertView==null){

			convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_skill,null,false);
			TextView tv_skill = ViewHolder.get(convertView,R.id.tv_skill);
			TextView tv_delete = ViewHolder.get(convertView,R.id.tv_delete);
			TextView tv_update = ViewHolder.get(convertView,R.id.tv_update);	
			tv_skill.setText(mlist.get(position));
			tv_delete.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
//					if(checkNetworkState()){
						deleteSkill(position);
//					}else{
//						Toast.makeText(mContext, mContext.getResources().getString(R.string.Broken_network_prompt), 0).show();
//					}
					
				}
			});
			tv_update.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
//					if(checkNetworkState()){
						updateSkill(position);
//					}else{
//						Toast.makeText(mContext, mContext.getResources().getString(R.string.Broken_network_prompt), 0).show();
//					}
					
				}
			});							
		}else{
			convertView.getTag();
		}	
		
		return convertView;
	}
	private void deleteSkill(final int position){
		new AlertDialog.Builder(mContext).setTitle(mContext.getResources().getString(R.string.deleteskill))  
		.setMessage(mContext.getResources().getString(R.string.deletethisskill)) .setNegativeButton(mContext.getResources().getString(R.string.cancel), null) 
		 .setPositiveButton(mContext.getResources().getString(R.string.sure), new DialogInterface.OnClickListener(){
             public void onClick(DialogInterface dialoginterface, int i){ 
//            	 EventBus.getDefault().post(new SkillEvent(position,mlist.get(position),true));           	      	        	 
             }
		 })
		 .show();  
	}
	private void updateSkill(final int position) {
		// TODO Auto-generated method stub
		final EditText inputServer = new EditText(mContext);
        inputServer.setFocusable(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getResources().getString(R.string.updateskill)).setView(inputServer).setNegativeButton(
        		mContext.getResources().getString(R.string.cancel), null);
        builder.setPositiveButton(mContext.getResources().getString(R.string.sure),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final String inputName = inputServer.getText().toString().trim();
                        boolean issamelabel=false;
                        if(!TextUtils.isEmpty(inputName)){                    	   
                    	   for(int i=0;i<mlist.size();i++){
                    		  if( inputName.equals( mlist.get(i))){
                    			  issamelabel=true;
                    			  break;
                    			  }                    		   
                    	   }
                    	   if(!issamelabel){
//                    		   EventBus.getDefault().post(new SkillEvent(position,inputName,false));                  		  
                    	   }                   	
                       }      
                    }
                });
        builder.show();			
	}

}
