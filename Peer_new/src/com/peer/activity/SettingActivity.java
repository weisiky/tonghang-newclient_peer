package com.peer.activity;

import java.io.File;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.peer.R;
import com.peer.IMimplements.easemobchatImp;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.service.FxService;
import com.peer.utils.BussinessUtils;
import com.peer.utils.ManagerActivity;
import com.peer.utils.pLog;
import com.peer.utils.pViewBox;
import com.thoughtworks.xstream.core.util.FastField;
import com.umeng.update.UmengUpdateAgent;

/**
 * ‘设置’页
 */
public class SettingActivity extends pBaseActivity {
	class PageViewList {
		private LinearLayout ll_back, ll_head_my, ll_setting_set,
				ll_newfunction_set, ll_feedback_set, ll_newversion_set,
				ll_clearcash_set, ll_blacklist_set;
		private TextView tv_title, xieyi;
		private Button bt_relogin, bt_todesk;
	}

	private PageViewList pageViewaList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_setting);
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
				R.string.setting));
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);
		pageViewaList.bt_todesk.setOnClickListener(this);
		pageViewaList.bt_relogin.setOnClickListener(this);
		pageViewaList.ll_clearcash_set.setOnClickListener(this);
		pageViewaList.ll_newversion_set.setOnClickListener(this);
		pageViewaList.ll_setting_set.setOnClickListener(this);
		pageViewaList.ll_newfunction_set.setOnClickListener(this);
		pageViewaList.ll_feedback_set.setOnClickListener(this);
		pageViewaList.ll_blacklist_set.setOnClickListener(this);
		pageViewaList.xieyi.setOnClickListener(this);
	}

	@Override
	protected void processBiz() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ll_setting_set:
			startActivityForLeft(MessageNotifyActivity.class, intent, false);
			break;
		case R.id.ll_newfunction_set:
			startActivityForLeft(NewFunctionActivity.class, intent, false);
			break;
		case R.id.ll_feedback_set:
			startActivityForLeft(FeedBackActivity.class, intent, false);
			break;
		case R.id.ll_newversion_set: // 新版本更新
			// if(checkNetworkState()){
			UmengUpdateAgent.forceUpdate(SettingActivity.this);
			// }else{
			// ShowMessage(getResources().getString(R.string.Broken_network_prompt));
			// }

			break;
		case R.id.ll_clearcash_set:
			Clean();
			break;
		case R.id.ll_blacklist_set:
			startActivityForLeft(BlackListActivity.class, intent, false);
			break;
		case R.id.bt_relogin:
			Relogin();
			break;
		case R.id.bt_todesk:
			Todesk();
			break;
		case R.id.xieyi:
			Intent xieyi = new Intent(SettingActivity.this, xieyiActivity.class);
			startActivity(xieyi);
			break;
		default:
			break;
		}

	}

	/**
	 * 清楚缓存
	 */
	private void Clean() {
		new AlertDialog.Builder(this)
				.setTitle(getResources().getString(R.string.clean))
				.setMessage(getResources().getString(R.string.clearcash))
				.setNegativeButton(getResources().getString(R.string.cancel),
						null)
				.setPositiveButton(getResources().getString(R.string.sure),
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface, int i) {
								deleteFilesByDirectory(getExternalCacheDir());
								showToast(
										getResources().getString(
												R.string.cleancash),
										Toast.LENGTH_LONG, false);
							}
						}).show();
	}

	private static void deleteFilesByDirectory(File directory) {
		if (directory != null && directory.exists() && directory.isDirectory()) {
			for (File item : directory.listFiles()) {
				item.delete();
			}
		}
	}

	/**
	 * 重新登入
	 */
	private void Relogin() {
		new AlertDialog.Builder(this)
				.setTitle(getResources().getString(R.string.exitlogin))
				.setMessage(getResources().getString(R.string.relogin))
				.setNegativeButton(getResources().getString(R.string.cancel),
						null)
				.setPositiveButton(getResources().getString(R.string.sure),
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface, int i) {
								BussinessUtils.clearUserData(mShareFileUtils);
								// if(mShareFileUtils.getBoolean(Constant.ISFLOAT,
								// false)){
								// pLog.i("test", "退出时，把悬浮头像隐藏");
								// Intent intent = new
								// Intent(SettingActivity.this,
								// FxService.class);//悬浮头像停止运行
								// stopService(intent);
								// mShareFileUtils.setBoolean(Constant.ISFLOAT,
								// false);
								// }
								ManagerActivity.getAppManager().restart(
										SettingActivity.this);
								easemobchatImp.getInstance().logout();
							}
						}).show();
	}

	/**
	 * 退出
	 */
	private void Todesk() {

		new AlertDialog.Builder(this)
				.setTitle(getResources().getString(R.string.close))
				.setMessage(getResources().getString(R.string.todesk))
				.setNegativeButton(getResources().getString(R.string.cancel),
						null)
				.setPositiveButton(getResources().getString(R.string.sure),
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface, int i) {
								ManagerActivity.getAppManager().AppExit(
										SettingActivity.this);
							}
						}).show();

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
