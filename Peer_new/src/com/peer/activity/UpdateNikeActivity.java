package com.peer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.peer.base.pBaseActivity;
import com.peer.utils.pViewBox;


/**
 * 更改昵称
 */
public class UpdateNikeActivity extends pBaseActivity{
	class PageViewList {
		private LinearLayout ll_back;
		private TextView tv_title;
		private EditText et_updatenike;
		private Button bt_nikename;
	
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
		pageViewaList.tv_title.setText(getResources().getString(R.string.updatenike));
		Intent t=getIntent();
		pageViewaList.et_updatenike.setText(t.getStringExtra("nikename"));
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);
		pageViewaList.bt_nikename.setOnClickListener(this);
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
		return getLayoutInflater().inflate(R.layout.activity_updatenike, null);
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
		case R.id.bt_nikename:
			if(pageViewaList.et_updatenike.getText().toString().trim().equals("")){
				showToast(getResources().getString(R.string.inputnike), Toast.LENGTH_LONG, false);
			}else{
				Intent t=getIntent();
				t.putExtra("newnikename", pageViewaList.et_updatenike.getText().toString());
				setResult(PersonalMessageActivity.UPDATENIKENAME, t);
				finish();
			}
			break;

		default:
			break;
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
