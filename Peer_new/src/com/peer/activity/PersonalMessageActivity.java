package com.peer.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.peer.IMimplements.easemobchatImp;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.bean.LoginBean;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.utils.BussinessUtils;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.Tools;
import com.peer.utils.pLog;
import com.peer.utils.pShareFileUtils;
import com.peer.utils.pViewBox;

/**
 * 修改个人信息
 */
public class PersonalMessageActivity extends pBaseActivity {

	private String[] items;
	private static final int CYTYSELECT = 103;
	private static final int IMAGE_REQUEST_CODE = 100;
	private static final int CAMERA_REQUEST_CODE = 101;
	private static final int RESULT_REQUEST_CODE = 102;
	public static final int UPDATENIKENAME = 104;
	Bitmap photo;
	byte[] img;

	private static final String IMAGE_FILE_NAME = "faceImage.png";

	private int mYear;
	private int mMonth;
	private int mDay;
	private static final int SHOW_DATAPICK =0;
	private static final int DATE_DIALOG_ID = 1;

	private String email;

	private PageViewList pageViewaList;

	class PageViewList {
		private LinearLayout ll_back, ll_imfor_personmessage, ll_headpic,
				ll_updatenike, ll_setsex, ll_setbirthday_my, ll_setaddress_my;
		private TextView tv_title, et_nikename_personMSG, tv_sex,
				tv_setbirthday_my, tv_setaddress_my;
		private ImageView iv_headpic_personMSG;
		private Button bt_update;
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
		pageViewaList.tv_title.setText(getResources().getString(
				R.string.personalmessage));
		items = getResources().getStringArray(R.array.pictrue);
		pageViewaList.tv_sex.setText(mShareFileUtils
				.getString(Constant.SEX, ""));
		pageViewaList.tv_setbirthday_my.setText(mShareFileUtils.getString(
				Constant.BIRTH, ""));
		pageViewaList.tv_setaddress_my.setText(mShareFileUtils.getString(
				Constant.CITY, ""));
		pageViewaList.et_nikename_personMSG.setText(mShareFileUtils.getString(
				Constant.USERNAME, ""));
		setDateTime();
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);
		pageViewaList.ll_headpic.setOnClickListener(this);
		pageViewaList.ll_setsex.setOnClickListener(this);
		pageViewaList.ll_setbirthday_my.setOnClickListener(this);
		pageViewaList.ll_setaddress_my.setOnClickListener(this);
		pageViewaList.ll_updatenike.setOnClickListener(this);
		pageViewaList.iv_headpic_personMSG.setDrawingCacheEnabled(true);
		pageViewaList.bt_update.setOnClickListener(this);
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
		return getLayoutInflater().inflate(R.layout.activity_my_personmessage,
				null);
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
		case R.id.ll_setbirthday_my:
			ChangBirthday();
			break;
		case R.id.ll_setaddress_my:
			ChangAddress();
			break;
		case R.id.ll_headpic:
			showDialog();
			break;
		case R.id.ll_setsex:
			SexSelect();
			break;
		case R.id.ll_updatenike:
			Intent intent = new Intent(PersonalMessageActivity.this,
					UpdateNikeActivity.class);
			intent.putExtra("nikename", pageViewaList.et_nikename_personMSG
					.getText().toString());
			startActivityForResult(intent, UPDATENIKENAME);
			break;
		case R.id.bt_update:
			photo = pageViewaList.iv_headpic_personMSG.getDrawingCache();
			img = getBitmapByte(photo);

			if (isNetworkAvailable) {
				sendupdateusermsg(pShareFileUtils.getString("client_id", ""),
						pageViewaList.tv_setbirthday_my.getText().toString(),
						pageViewaList.tv_sex.getText().toString(),
						pageViewaList.tv_setaddress_my.getText().toString(),
						pageViewaList.et_nikename_personMSG.getText()
								.toString());
			} else {
				showToast(
						getResources()
								.getString(R.string.Broken_network_prompt),
						Toast.LENGTH_SHORT, false);
			}
			break;
		default:
			break;
		}

	}

	/**
	 * ChangBirthday类
	 */
	private void ChangBirthday() {
		// TODO Auto-generated method stub
		pLog.i("test", "ChangBirthday");
		Message msg = new Message();
		msg.what = SHOW_DATAPICK;
		dateandtimeHandler.sendMessage(msg);
	}

	Handler dateandtimeHandler = new Handler() {
		@SuppressWarnings("deprecation")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_DATAPICK:
				pLog.i("test", "handleMessage");
				showDialog(DATE_DIALOG_ID);
				break;
			}
		}
	};
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}
		return null;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case DATE_DIALOG_ID:
			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
			break;
		}
	}

	/**
	 * 初始化Calendar
	 */
	private void setDateTime() {
		final Calendar c = Calendar.getInstance();
		mYear = 1993;
		mMonth = 1;
		mDay = 1;
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDateDisplay();
		}
	};

	private void updateDateDisplay() {
		
		pLog.i("test", "handleMessage"+new StringBuilder()
		.append(mYear).append("-")
		.append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1))
		.append("-").append((mDay < 10) ? "0" + mDay : mDay));
		
		pageViewaList.tv_setbirthday_my.setText(new StringBuilder()
				.append(mYear).append("-")
				.append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1))
				.append("-").append((mDay < 10) ? "0" + mDay : mDay));
	}

	/*
	 * ChangAddress类
	 */
	private void ChangAddress() {
		// TODO Auto-generated method st
		Intent intent = new Intent(PersonalMessageActivity.this,
				GetAddressInfoActivity.class);
		startActivityForResult(intent, CYTYSELECT);
	}

	private void showDialog() {

		new AlertDialog.Builder(this)
				.setTitle(getResources().getString(R.string.picture))
				.setItems(items, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							Intent intentFromGallery = new Intent(
									Intent.ACTION_PICK, null);
							intentFromGallery
									.setDataAndType(
											MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
											"image/*");
							startActivityForResult(intentFromGallery,
									IMAGE_REQUEST_CODE);
							break;
						case 1:

							Intent intentFromCapture = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							if (Tools.hasSdcard()) {
								intentFromCapture.putExtra(
										MediaStore.EXTRA_OUTPUT,
										Uri.fromFile(new File(Environment
												.getExternalStorageDirectory(),
												IMAGE_FILE_NAME)));
							}
							startActivityForResult(intentFromCapture,
									CAMERA_REQUEST_CODE);
							break;
						}
					}
				})
				.setNegativeButton(getResources().getString(R.string.cancel),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).show();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_CANCELED) {
			switch (requestCode) {
			case CYTYSELECT:
				if (data.getStringExtra("city") == null) {
					pageViewaList.tv_setaddress_my.setText(data
							.getStringExtra("province"));
				} else {
					pageViewaList.tv_setaddress_my.setText(data
							.getStringExtra("province")
							+ "-"
							+ data.getStringExtra("city"));
				}
				break;
			case UPDATENIKENAME:
				pageViewaList.et_nikename_personMSG.setText(data
						.getStringExtra("newnikename"));
				break;
			case IMAGE_REQUEST_CODE:
				startPhotoZoom(data.getData());
				break;
			case CAMERA_REQUEST_CODE:
				if (Tools.hasSdcard()) {
					File tempFile = new File(
							Environment.getExternalStorageDirectory() + "/"
									+ IMAGE_FILE_NAME);
					startPhotoZoom(Uri.fromFile(tempFile));
				} else {

				}
				break;
			case RESULT_REQUEST_CODE:
				if (data != null) {
					Bitmap bt = getImageToView(data);

					img = getBitmapByte(bt);
				}
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void startPhotoZoom(Uri uri) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");

		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);

		intent.putExtra("outputX", 250);
		intent.putExtra("outputY", 250);

		intent.putExtra("outputFormat", "PNG");
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, RESULT_REQUEST_CODE);
	}

	public byte[] getBitmapByte(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}
		final ByteArrayOutputStream os = new ByteArrayOutputStream();

		bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
		return os.toByteArray();
	}

	public Bitmap getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			photo = extras.getParcelable("data");

			pageViewaList.iv_headpic_personMSG.setImageBitmap(photo);
		}
		return photo;
	}

	/**
	 * 选择性别
	 */
	private void SexSelect() {
		// TODO Auto-generated method stub
		final String[] items = getResources().getStringArray(R.array.sex);
		new AlertDialog.Builder(PersonalMessageActivity.this)
				.setTitle(getResources().getString(R.string.sex))
				.setItems(items, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						pageViewaList.tv_sex.setText(items[which]);
					}
				}).show();
	}

	/**
	 * 更改用户信息请求
	 * 
	 * @param client_id
	 * @param sex
	 * @param birth
	 * @param address
	 * @param username
	 * @throws Exception
	 **/

	private void sendupdateusermsg(String client_id, String tv_setbirth,
			String tv_sex, String tv_setaddress, String username) {
		// TODO Auto-generated method stub
		final Intent intent = new Intent();
		// HttpEntity entity = null;
		// try {
		// entity = PeerParamsUtils.getUpdateParams(
		// PersonalMessageActivity.this,
		// tv_setbirth,tv_sex,tv_setaddress,username);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		RequestParams params = null;
		try {
			params = PeerParamsUtils.getUpdateParams(
					PersonalMessageActivity.this, tv_setbirth, tv_sex,
					tv_setaddress, username);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		HttpUtil.post(this, HttpConfig.UPDATE_IN_URL + client_id + ".json",
				params, new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub

						hideLoading();
						super.onFailure(statusCode, headers, responseString,
								throwable);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONArray errorResponse) {
						// TODO Auto-generated method stub
						hideLoading();
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						hideLoading();
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						hideLoading();
						try {
							LoginBean loginBean = JsonDocHelper.toJSONObject(
									response.getJSONObject("success")
											.toString(), LoginBean.class);
							if (loginBean != null) {
								BussinessUtils.saveUserData(loginBean,
										mShareFileUtils);
								showToast("更新成功！", Toast.LENGTH_SHORT, false);
								finish();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						super.onSuccess(statusCode, headers, response);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String responseString) {
						// TODO Auto-generated method stub
						hideLoading();
						super.onSuccess(statusCode, headers, responseString);
						Intent login_complete = new Intent();
						startActivityForLeft(MainActivity.class,
								login_complete, false);
					}

				});
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
