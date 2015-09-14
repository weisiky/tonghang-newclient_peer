package com.peer.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.peer.R;
import com.peer.base.pBaseActivity;
import com.peer.utils.pViewBox;

/**
 * 协议类
 */
public class xieyiActivity extends pBaseActivity {
	class PageViewList {
		private LinearLayout ll_back;
		private TextView tv_title;
		private LinearLayout back;
	}

	private PageViewList pageViewaList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xieyi);
		findViewById();
		setListener();
		processBiz();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		pageViewaList = new PageViewList();
		pViewBox.viewBox(this, pageViewaList);
		pageViewaList.tv_title
				.setText(getResources().getString(R.string.xieyi));
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);
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
		super.onClick(v);

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
