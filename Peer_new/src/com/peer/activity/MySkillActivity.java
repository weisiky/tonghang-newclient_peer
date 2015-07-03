package com.peer.activity;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.peer.adapter.SkillAdapter;
import com.peer.base.pBaseActivity;
import com.peer.utils.pViewBox;
import com.umeng.analytics.f;


/*
 * �鿴�ҵı�ǩ��
 * �ɶԱ�ǩ������Ӻ�ɾ��
 * */
public class MySkillActivity extends pBaseActivity{
	
	private PageViewList pageViewaList;
	
	private List<String> mlist;  //���ڴ洢��ǰ�û���ǩ����
	private int Hadtag;		//�����������жϵ�ǰ�û���ǩ����
	
	
	
	private SkillAdapter adapter;
	
	class PageViewList {
		private LinearLayout ll_back,ll_createTag_mytag;
		private TextView tv_title;
		private ListView lv_myskill;
	}

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		pageViewaList = new PageViewList();
		pViewBox.viewBox(this, pageViewaList);
		pageViewaList.tv_title.setText(getResources().getString(R.string.myskill));
		
		adapter=new SkillAdapter(this,mlist);
		pageViewaList.lv_myskill.setAdapter(adapter);
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);
		pageViewaList.ll_createTag_mytag.setOnClickListener(this);
	}

	@Override
	protected void processBiz() {
		// TODO Auto-generated method stub
		

	}

	@Override
	protected View loadTopLayout() {
		// TODO Auto-generated method stub
		// return getLayoutInflater().inflate(R.layout.top_layout, null);
		return getLayoutInflater().inflate(R.layout.base_toplayout_title, null);
	}

	@Override
	protected View loadContentLayout() {
		// TODO Auto-generated method stub
		return getLayoutInflater().inflate(R.layout.activity_my_myskill, null);
	}
	
	@Override
	protected View loadBottomLayout() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ll_createTag_mytag:
			Hadtag=mlist.size();
			if(Hadtag>4){
				showToast("���Ѿ��������ǩ�������ٴ�����", Toast.LENGTH_LONG, false);
				break;
			}else{
				CreateTagDialog();					
			}		
			break;
		default:
			break;
		}
		
	}

	
	
	/*
	 * ��ӱ�ǩ��
	 * 
	 * */
	private void CreateTagDialog() {
		// TODO Auto-generated method stub		
		//�����༭��Dialog
		final EditText inputServer = new EditText(MySkillActivity.this);
		inputServer.setFocusable(true);
		AlertDialog.Builder builder = new AlertDialog.Builder(MySkillActivity.this);
        builder.setTitle(getResources().getString(R.string.register_tag)).setView(inputServer).
        setNegativeButton(getResources().getString(R.string.cancel), null);
        builder.setPositiveButton(getResources().getString(R.string.sure),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String inputName = inputServer.getText().toString().trim();
                       if(!TextUtils.isEmpty(inputName)){
                    	   String fomate="^[A-Za-z]+$";
                    	   if(inputName.matches(fomate)){
                    		   if(inputName.length()<13){
                        		   boolean issame=false;
    	                      		 for(int i=0;i<mlist.size();i++){ 
    	                      			 if(mlist.get(i).equals(inputName)){
    	                      				issame=true;
    	                      				 showToast(getResources().getString(R.string.repetskill), Toast.LENGTH_LONG,false);
    	                      				 break;	                      				
    	                      			 }
    	                      		 }
    	                      		 if(!issame){
    	                      			createLable(inputName);
    	                      		 }	                      		
    	                      	}else{
    	                      		showToast(getResources().getString(R.string.skillname), Toast.LENGTH_LONG,false);
    	                      		
    	                      	}       
                    	   }else{
                    		   if(inputName.length()<7){
                        		   boolean issame=false;
    	                      		 for(int i=0;i<mlist.size();i++){ 
    	                      			 if(mlist.get(i).equals(inputName)){
    	                      				issame=true;
    	                      				showToast(getResources().getString(R.string.repetskill), Toast.LENGTH_LONG,false);
    	                      				
    	                      				 break;	                      				
    	                      			 }
    	                      		 }
    	                      		 if(!issame){
    	                      			createLable(inputName);
    	                      		 }	                      		
    	                      	}else{
    	                      		showToast(getResources().getString(R.string.skillname), Toast.LENGTH_LONG,false);
    	                      		
    	                      	}     
                    	   }
                    	                     
                       }else{
                    	   showToast("��������ҵ��ǩ��",Toast.LENGTH_LONG ,false);
                       }                       
                    }
                });
        builder.show();	  		
	}
	
	
	public void createLable(String label){
		mlist.add(label);
		changLabelsTask task=new changLabelsTask();
		task.execute();				
	}
	
	
	/*
	 * ���̸߳���UI
	 * �����ɹ�ʱ����������ӵ�adapter��
	 * 
	 * */
	public class changLabelsTask extends  AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			Hadtag=mlist.size();
			adapter=new SkillAdapter(MySkillActivity.this,mlist);
			pageViewaList.lv_myskill.setAdapter(adapter);
			return null;
		}
				
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
