package com.peer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.peer.base.pBaseActivity;
import com.peer.utils.pViewBox;


/*
 * ���ҵ��˻���ҳactivity
 * */
public class MyAcountActivity extends pBaseActivity {
	
	class PageViewList {
		private LinearLayout ll_back,ll_changepassword;
		private TextView tv_title,myeamil;
		
	}

	private PageViewList pageViewaList;

	
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
		pageViewaList.tv_title.setText(getResources().getString(R.string.mycount));
		/*
		 * ������ʾ��ǰ�û�������
		 * ��ʱ��̬Ĭ��
		 * */
		pageViewaList.myeamil.setText("395296797@qq.com");
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);
		pageViewaList.ll_changepassword.setOnClickListener(this);
		
	}

	@Override
	protected void processBiz() {
		// TODO Auto-generated method stub

	}

	@Override
	protected View loadTopLayout() {
		// TODO Auto-generated method stub
		return getLayoutInflater().inflate(R.layout.base_toplayout_title, null);
	}

	@Override
	protected View loadContentLayout() {
		// TODO Auto-generated method stub
		return getLayoutInflater().inflate(R.layout.activity_my_myacount, null);
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
		case R.id.ll_changepassword:
			Intent changepassword=new Intent(MyAcountActivity.this,UpdatePasswordActivity.class);				
			startActivity(changepassword);
			break;

		default:
			break;
		}
	}

}