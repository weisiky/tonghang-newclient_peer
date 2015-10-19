package com.peer.activity;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.peer.R;
import com.peer.IMimplements.easemobchatImp;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.utils.BussinessUtils;
import com.peer.utils.ManagerActivity;
import com.peer.utils.pLog;
import com.peer.utils.pViewBox;

/**
 * 消息提醒方式类
 */
public class MessageNotifyActivity extends pBaseActivity {

	// private final int START=1,END=2;
	// int starth=22,startm=30,endh=8,endm=30;

	private PageViewList pageViewaList;

	class PageViewList {
		private LinearLayout ll_back;
		private TextView tv_title;
		private CheckBox cb_sound, cb_vibrate;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_notify);
		findViewById();
		setListener();
		processBiz();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		pageViewaList = new PageViewList();
		pViewBox.viewBox(this, pageViewaList);
		pageViewaList.tv_title.setText(getResources()
				.getString(R.string.notify));
		pageViewaList.cb_sound.setChecked(true);
		pageViewaList.cb_vibrate.setChecked(true);

		if (mShareFileUtils.getBoolean("sound", true)) {
			pageViewaList.cb_sound.setChecked(true);
		} else {
			pageViewaList.cb_sound.setChecked(false);
		}
		if (mShareFileUtils.getBoolean("vibrate", true)) {
			pageViewaList.cb_vibrate.setChecked(true);
		} else {
			pageViewaList.cb_vibrate.setChecked(false);
		}
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);
		// pageViewaList.start.setOnClickListener(this);
		// pageViewaList.end.setOnClickListener(this);

		pageViewaList.cb_sound
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							mShareFileUtils.setBoolean("sound", true);
						} else {
							mShareFileUtils.setBoolean("sound", false);
						}
					}
				});
		pageViewaList.cb_vibrate
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							mShareFileUtils.setBoolean("vibrate", true);
						} else {
							mShareFileUtils.setBoolean("vibrate", false);
						}
					}
				});
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
		// case R.id.start:
		// showDialog(START);
		// break;
		// case R.id.end:
		// showDialog(END);
		// break;

		default:
			break;
		}

	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		// case START:
		// return new TimePickerDialog(this, mtimesetListener1, starth, startm,
		// true);
		// case END:
		// return new TimePickerDialog(this, mtimesetListener2, endh, endm,
		// true);
		default:
			break;
		}

		return null;
	}

	private TimePickerDialog.OnTimeSetListener mtimesetListener1 = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			// starth = hourOfDay;
			// startm = minute;
			// mShareFileUtils.setInt("starth", starth);
			// mShareFileUtils.setInt("startm", startm);
			// pageViewaList.start.setText(new
			// StringBuilder().append(starth).append(":").append(startm));
			// updateDisplay();
		}
	};
	private TimePickerDialog.OnTimeSetListener mtimesetListener2 = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			// endh = hourOfDay;
			// endm = minute;
			// mShareFileUtils.setInt("endh", endh);
			// mShareFileUtils.setInt("endm", endm);
			// pageViewaList.end.setText(new
			// StringBuilder().append(endh).append(":").append(endm));
			// updateDisplay();
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
	
	
}
