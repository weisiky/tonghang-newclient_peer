package com.peer.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.peer.base.pBaseActivity;
import com.peer.utils.pViewBox;


/*
 * 更改密码（发请求）
 * */
public class UpdatePasswordActivity extends pBaseActivity {
	
	private TextWatcher textwatcher;
	private PageViewList pageViewaList;

	class PageViewList {
		private LinearLayout ll_back;
		private TextView tv_title,updatepasw_remind;
        private EditText et_oldpasw,et_newpasw,et_repasw;
        private Button bt_changesubmite;
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
		pageViewaList.tv_title.setText(getResources().getString(R.string.updatepassword));
		pageViewaList.bt_changesubmite.setEnabled(false);
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);
		CreateTextwatcher();
		pageViewaList.et_oldpasw.addTextChangedListener(textwatcher);
		pageViewaList.et_newpasw.addTextChangedListener(textwatcher);
		pageViewaList.et_repasw.addTextChangedListener(textwatcher);
		
		pageViewaList.bt_changesubmite.setOnClickListener(this);
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
		return getLayoutInflater().inflate(R.layout.activity_changpassword, null);
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
		case R.id.bt_changesubmite:
			UpdatePassword();
			
			break;

		default:
			break;
		}
		
	}
	
	
	private void UpdatePassword() {
		// TODO Auto-generated method stub		
		String old=pageViewaList.et_oldpasw.getText().toString().trim();
		final String newpasws=pageViewaList.et_newpasw.getText().toString().trim();
		String testnew=pageViewaList.et_repasw.getText().toString().trim();
		
		/*
		final String email=LocalStorage.getString(UpdatePasswordActivity.this, Constant.EMAIL);
		final UserDao udao=new UserDao(UpdatePasswordActivity.this);		
		String password=udao.getPassord(email);
		if(!old.equals(password)){
			remind.setText(getResources().getString(R.string.erroroldpsw));
			return;
		}else */
		if(!newpasws.matches("^[a-zA-Z0-9_]{5,17}$")){
			pageViewaList.updatepasw_remind.setText(getResources().getString(R.string.errorpswformat));
			return;
		}else if(!newpasws.equals(testnew)){
			pageViewaList.updatepasw_remind.setText(getResources().getString(R.string.oldnewnot));
			return;
		}else{
			showToast("模拟完成更改", Toast.LENGTH_SHORT, false);
			finish();
		}
	}
	
	
	
	
	private void CreateTextwatcher() {
		// TODO Auto-generated method stub
		textwatcher=new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				TestNull();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				TestNull();
			}			
		};
		
	}
	
	private void TestNull() {
		// TODO Auto-generated method stub
		String old=pageViewaList.et_oldpasw.getText().toString();
		String newpasws=pageViewaList.et_newpasw.getText().toString();
		String testnew=pageViewaList.et_repasw.getText().toString();
		if(!old.equals("")&&!newpasws.equals("")&&!testnew.equals("")){
			pageViewaList.bt_changesubmite.setEnabled(true);
		}else{
			pageViewaList.bt_changesubmite.setEnabled(false);
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
