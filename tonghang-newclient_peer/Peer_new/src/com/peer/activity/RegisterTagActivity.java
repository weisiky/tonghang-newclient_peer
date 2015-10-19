package com.peer.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.peer.R;
import com.peer.IMimplements.easemobchatImp;
import com.peer.adapter.SkillAdapter;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.bean.LoginBean;
import com.peer.bean.PersonpageBean;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.utils.BussinessUtils;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.ManagerActivity;
import com.peer.utils.pLog;
import com.peer.utils.pViewBox;

/**
 * 注册第二部标签页面
 * 
 * @author zhangzg
 * 
 */
public class RegisterTagActivity extends pBaseActivity {
	class PageViewList {
		private LinearLayout ll_back;
		private TextView tv_title;
		private EditText et_tagname_1, et_tagname_2, et_tagname_3,
				et_tagname_4, et_tagname_5;
		private TextView tv_remind, xieyi;
		private Button bt_registe_tag;

	}

	private PageViewList pageViewaList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_tag);
		findViewById();
		setListener();
		processBiz();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		pageViewaList = new PageViewList();
		pViewBox.viewBox(this, pageViewaList);
		pageViewaList.tv_title.setText(getResources().getString(
				R.string.register_tag));
		pageViewaList.et_tagname_1.addTextChangedListener(watcher);
		pageViewaList.et_tagname_2.addTextChangedListener(watcher);
		pageViewaList.bt_registe_tag.setEnabled(false);

	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);
		pageViewaList.bt_registe_tag.setOnClickListener(this);
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
		case R.id.bt_registe_tag:
			RegisteTag();

			break;

		default:
			break;
		}

	}

	private void RegisteTag() {
		// TODO Auto-generated method stub

		String t1 = pageViewaList.et_tagname_1.getText().toString().trim();
		String t2 = pageViewaList.et_tagname_2.getText().toString().trim();
		String t3 = pageViewaList.et_tagname_3.getText().toString().trim();
		String t4 = pageViewaList.et_tagname_4.getText().toString().trim();
		String t5 = pageViewaList.et_tagname_5.getText().toString().trim();
		String[] arr = { t1, t2, t3, t4, t5 };
		ArrayList<String> list = new ArrayList<String>();

		boolean sameTag = true;
		boolean Tolong = true;
		boolean isbreak = true;
		for (int i = 0; i < arr.length; i++) {
			if (!arr[i].equals("")) {
				list.add(arr[i]);
			}
		}
		if (!t1.equals("") && !t2.equals("")) {
			String fomate = "^[A-Za-z]+$";
			for (int j = 0; j < list.size(); j++) {
				if (list.get(j).matches(fomate)) {
					if (list.get(j).length() < 13) {
						Tolong = false;
					} else {
						Tolong = true;
						pageViewaList.tv_remind.setText(getResources()
								.getString(R.string.skillname));
						break;
					}
				} else {
					if (list.get(j).length() < 7) {
						Tolong = false;
					} else {
						Tolong = true;
						pageViewaList.tv_remind.setText(getResources()
								.getString(R.string.skillname));
						break;
					}
				}

			}
			for (int j = 0; j < list.size(); j++) {
				for (int k = j + 1; k < list.size(); k++) {
					if (!list.get(j).equals(list.get(k))) {
						sameTag = false;
					} else {
						sameTag = true;
						isbreak = false;
						pageViewaList.tv_remind.setText(getResources()
								.getString(R.string.repetskill));
						break;
					}
				}
				if (!isbreak) {
					break;
				}
			}

			if (!sameTag && !Tolong) {
				pageViewaList.tv_remind.setText("");
				try {
					senduserlabels(
							mShareFileUtils.getString(Constant.CLIENT_ID, ""),
							list);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		} else {
			pageViewaList.tv_remind.setText(getResources().getString(
					R.string.skillnull));
			return;
		}

	}

	/**
	 * 修改当前用户标签请求
	 * 
	 * @param client_id
	 * @param label_name
	 * @throws UnsupportedEncodingException
	 */
	private void senduserlabels(String client_id, List<String> label_name)
			throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		final Intent intent = new Intent();
		// HttpEntity entity = null;
		// try {
		// entity = PeerParamsUtils.getUserLabelsParams(this, client_id,
		// label_name);
		// } catch (Exception e2) {
		// // TODO Auto-generated catch block
		// e2.printStackTrace();
		// }
		RequestParams params = null;
		try {
			params = PeerParamsUtils.getUserLabelsParams(this, client_id,
					label_name);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpUtil.post(this, HttpConfig.USER_UPDATE_LABEL_IN_URL + client_id
				+ ".json", params, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub

				showToast(getResources().getString(R.string.config_error),
						Toast.LENGTH_SHORT, false);
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONArray errorResponse) {
				// TODO Auto-generated method stub

				showToast(getResources().getString(R.string.config_error),
						Toast.LENGTH_SHORT, false);
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub

				showToast(getResources().getString(R.string.config_error),
						Toast.LENGTH_SHORT, false);
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@SuppressWarnings("static-access")
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				try {
					JSONObject result = response.getJSONObject("success");

					String code = result.getString("code");
					if (code.equals("200")) {
						LoginBean loginBean = JsonDocHelper.toJSONObject(
								response.getJSONObject("success").toString(),
								LoginBean.class);
						sendRequesJpush(mShareFileUtils.getString(
								Constant.CLIENT_ID, ""));
						if (loginBean.user.getLabels() != null
								&& loginBean.user.getLabels().size() > 0) {
							mShareFileUtils.setString(Constant.LABELS,
									loginBean.user.getLabels().toString());
							Intent register_com = new Intent();
							startActivityForLeft(
									RegisterCompleteActivity.class,
									register_com, false);

						}

					} else if (code.equals("500")) {

					} else {
						String message = result.getString("message");
						showToast(message, Toast.LENGTH_SHORT, false);
					}

				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				super.onSuccess(statusCode, headers, response);

			}

		});
	}

	TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			String t1 = pageViewaList.et_tagname_1.getText().toString().trim();
			String t2 = pageViewaList.et_tagname_2.getText().toString().trim();
			if (!TextUtils.isEmpty(t1) && !TextUtils.isEmpty(t2)) {
				pageViewaList.bt_registe_tag.setEnabled(true);
			} else {
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
			String t1 = pageViewaList.et_tagname_1.getText().toString().trim();
			String t2 = pageViewaList.et_tagname_2.getText().toString().trim();
			if (!TextUtils.isEmpty(t1) && !TextUtils.isEmpty(t2)) {
				pageViewaList.bt_registe_tag.setEnabled(true);
			} else {
				pageViewaList.bt_registe_tag.setEnabled(false);
			}
		}
	};

	@Override
	public void onNetworkOn() {
		// TODO Auto-generated method stub
//		sendSystemConfig();
	}

	@Override
	public void onNetWorkOff() {
		// TODO Auto-generated method stub

	}

	/**
	 * 注册极光跟环信
	 */
	@SuppressWarnings("static-access")
	protected void sendRequesJpush(String client_id) {
		// TODO Auto-generated method stub
		JPushInterface.setAlias(getApplication(), client_id,
				new TagAliasCallback() {
					@Override
					public void gotResult(int code, String arg1,
							Set<String> arg2) {
						// TODO Auto-generated method stub
					}
				});

		easemobchatImp.getInstance().login(client_id.replace("-", ""),
				mShareFileUtils.getString(Constant.PASSWORD, ""));
		easemobchatImp.getInstance().loadConversationsandGroups();

	}
}
