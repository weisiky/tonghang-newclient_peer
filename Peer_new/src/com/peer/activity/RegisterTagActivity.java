package com.peer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.peer.base.pBaseActivity;
import com.peer.utils.pViewBox;



/*
 * ×¢²á±êÇ©Àà
 * */
public class RegisterTagActivity extends pBaseActivity{
	class PageViewList {
		private LinearLayout ll_back;
		private TextView tv_title;
		private EditText et_tagname_1,et_tagname_2,et_tagname_3,et_tagname_4,et_tagname_5;
		private TextView tv_remind,xieyi;
		private Button bt_registe_tag;
		
	}

	private PageViewList pageViewaList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Intent intent=new Intent(this,LoginActivity.class);
		startActivity(intent);
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		pageViewaList = new PageViewList();
		pViewBox.viewBox(this, pageViewaList);
		pageViewaList.tv_title.setText(getResources().getString(R.string.register_tag));
		pageViewaList.et_tagname_1.addTextChangedListener(watcher);
		pageViewaList.et_tagname_2.addTextChangedListener(watcher);
		pageViewaList.bt_registe_tag.setEnabled(false);
		
		/*
		 *ÎÄ×Ö¼Ó¸ßÁÁÉ«
		 * */
		SpannableStringBuilder builder = new SpannableStringBuilder(pageViewaList.xieyi.getText().toString());
		ForegroundColorSpan colorspan = new ForegroundColorSpan(getResources().getColor(R.color.backcolornol));
		builder.setSpan(colorspan, 16, 31, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		pageViewaList.xieyi.setText(builder);

	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);
		pageViewaList.bt_registe_tag.setOnClickListener(this);
		pageViewaList.xieyi.setOnClickListener(this);
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
		return getLayoutInflater().inflate(R.layout.activity_register_tag, null);
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
		case R.id.bt_registe_tag:
			Intent register_tag=new Intent(RegisterTagActivity.this,RegisterCompleteActivity.class);
			startActivity(register_tag);
			break;

		case R.id.xieyi:
			Intent xieyi=new Intent(RegisterTagActivity.this,xieyiActivity.class);
			startActivity(xieyi);
			break;
		default:
			break;
		}
		
	}
	
	
	/*
	 * TextWatcher¼àÌý±à¼­¿ò
	 * */
TextWatcher watcher=new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			String t1=pageViewaList.et_tagname_1.getText().toString().trim();
			String t2=pageViewaList.et_tagname_2.getText().toString().trim();
			if(!TextUtils.isEmpty(t1)&&!TextUtils.isEmpty(t2)){
				pageViewaList.bt_registe_tag.setEnabled(true);
			}else{
				pageViewaList.bt_registe_tag.setEnabled(false);
			}
		}
		
		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			String t1=pageViewaList.et_tagname_1.getText().toString().trim();
			String t2=pageViewaList.et_tagname_2.getText().toString().trim();
			if(!TextUtils.isEmpty(t1)&&!TextUtils.isEmpty(t2)){
				pageViewaList.bt_registe_tag.setEnabled(true);
			}else{
				pageViewaList.bt_registe_tag.setEnabled(false);
			}
		}
	};
}
