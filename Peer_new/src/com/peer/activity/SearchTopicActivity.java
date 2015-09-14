package com.peer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.peer.R;
import com.peer.activity.SearchUserActivity.PageViewList;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.bean.SearchBean;
import com.peer.titlepopwindow.ActionItem;
import com.peer.titlepopwindow.TitlePopup;
import com.peer.titlepopwindow.TitlePopup.OnItemOnClickListener;
import com.peer.utils.pViewBox;

/**
 * 搜索话题类
 */
public class SearchTopicActivity extends pBaseActivity {

	public static int TOPIC_LABEL = 1;
	public static int TOPIC_TOPICKEY = 2;
	private int searchtype = TOPIC_LABEL; // 默认按标签搜
	private TitlePopup tagPopup;
	private InputMethodManager imm;
	boolean page = false;

	class PageViewList {
		private LinearLayout ll_back, layout_clear_search_text,
				search_downview;
		private TextView tv_title;
		private EditText et_contentsearch;
		private Button btn_clear_search_text;
		private ImageView im_search_search;
	}

	private PageViewList pageViewaList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		findViewById();
		setListener();
		processBiz();
		popupwindow();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		switch (searchtype) {
		case 1:
			SearchBean.getInstance().setSearchtype(Constant.TOPICBYLABEL);
			break;
		case 2:
			SearchBean.getInstance().setSearchtype(Constant.TOPICBYTOPIC);
			break;
		default:
			break;
		}
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		pageViewaList = new PageViewList();
		pViewBox.viewBox(this, pageViewaList);
		pageViewaList.tv_title.setText(getResources().getString(
				R.string.searchtopic));
		SearchBean.getInstance().setSearchtype(Constant.TOPICBYLABEL);

		pageViewaList.et_contentsearch.setHint(getResources().getString(
				R.string.bytag));
		pageViewaList.et_contentsearch.addTextChangedListener(watcher);
		pageViewaList.et_contentsearch.setFocusable(true);
		pageViewaList.et_contentsearch.setFocusableInTouchMode(true);
		pageViewaList.et_contentsearch.requestFocus();

		imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
		imm.showSoftInput(pageViewaList.et_contentsearch,
				InputMethodManager.RESULT_SHOWN);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
				InputMethodManager.HIDE_IMPLICIT_ONLY);

		tagPopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, page);
		tagPopup.addAction(new ActionItem(this, getResources().getString(
				R.string.bytag), R.color.white));
		tagPopup.addAction(new ActionItem(this, getResources().getString(
				R.string.bytopic), R.color.white));

	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);
		pageViewaList.btn_clear_search_text.setOnClickListener(this);
		pageViewaList.im_search_search.setOnClickListener(this);
		pageViewaList.search_downview.setOnClickListener(this);
	}

	@Override
	protected void processBiz() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_clear_search_text:
			pageViewaList.et_contentsearch.setText("");
			pageViewaList.layout_clear_search_text.setVisibility(View.GONE);
			break;
		case R.id.search_downview:
			tagPopup.showonserchuser(v);
			break;
		case R.id.im_search_search:
			// if(checkNetworkState()){
			String searchtaget = pageViewaList.et_contentsearch.getText()
					.toString().trim();
			if (TextUtils.isEmpty(searchtaget)) {
				showToast("搜索框不能为空", Toast.LENGTH_SHORT, false);
			} else {
				Search(searchtaget);
			}
			// }else{
			// ShowMessage(getResources().getString(R.string.Broken_network_prompt));
			// }
			break;
		default:
			break;
		}

	}

	private void Search(String tagetname) {
		// TODO Auto-generated method stub
		imm.hideSoftInputFromWindow(
				pageViewaList.et_contentsearch.getWindowToken(), 0);
		SearchBean.getInstance().setSearchname(tagetname);
		// if(SearchUtil.getInstance().equals("USERBYNIKE")){
		SearchBean.getInstance().setCallbacklabel(tagetname);
		// }
		Intent intent = new Intent(SearchTopicActivity.this,
				SearchResultActivity.class);
		startActivity(intent);
	}

	private void popupwindow() {

		tagPopup.setItemOnClickListener(new OnItemOnClickListener() {
			@Override
			public void onItemClick(ActionItem item, int position) {
				// TODO Auto-generated method stub
				if (item.mTitle
						.equals(getResources().getString(R.string.bytag))) {
					pageViewaList.et_contentsearch.setHint(item.mTitle);
					SearchBean.getInstance().setSearchtype(
							Constant.TOPICBYLABEL);
					searchtype = TOPIC_LABEL;
				} else if (item.mTitle.equals(getResources().getString(
						R.string.bytopic))) {
					pageViewaList.et_contentsearch.setHint(item.mTitle);
					SearchBean.getInstance().setSearchtype(
							Constant.TOPICBYTOPIC);
					searchtype = TOPIC_TOPICKEY;
				}
			}
		});
	}

	TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			int textLength = pageViewaList.et_contentsearch.getText().length();
			if (textLength > 0) {
				pageViewaList.layout_clear_search_text
						.setVisibility(View.VISIBLE);
			} else {
				pageViewaList.layout_clear_search_text.setVisibility(View.GONE);
			}
		}
	};

	@Override
	public void onNetworkOn() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNetWorkOff() {
		// TODO Auto-generated method stub

	}
}
