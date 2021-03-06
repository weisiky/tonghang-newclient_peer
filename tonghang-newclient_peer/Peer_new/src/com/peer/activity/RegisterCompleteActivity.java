package com.peer.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.ObjectOutputStream.PutField;
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
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.peer.R;
import com.peer.IMimplements.easemobchatImp;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.bean.LoginBean;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.net.PeerParamsUtils;
import com.peer.utils.BussinessUtils;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.ManagerActivity;
import com.peer.utils.Tools;
import com.peer.utils.pLog;
import com.peer.utils.pViewBox;

/**
 * 注册第三部 完善信息
 * 
 */
public class RegisterCompleteActivity extends pBaseActivity {

	private String[] items;

	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	Bitmap photo;
	byte[] img;
	private static final String IMAGE_FILE_NAME = "faceImage.png";
	int width, height;

	private int mYear;
	private int mMonth;
	private int mDay;

	private static final int CYTYSELECT = 3;
	private static final int SHOW_DATAPICK = 0;
	private static final int DATE_DIALOG_ID = 1;

	private PageViewList pageViewaList;

	class PageViewList {
		private LinearLayout ll_back, ll_uploadepic, back, ll_setsex,
				ll_setbirthday, ll_setaddress;
		private TextView tv_title;
		private ImageView iv_uploadepic_complete;
		private TextView tv_sex, tv_setbirth, tv_setaddress, tv_remind;
		private Button bt_login_complete;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_complete);
		findViewById();
		setListener();
		processBiz();
		items = getResources().getStringArray(R.array.pictrue);
		pageViewaList.iv_uploadepic_complete.setDrawingCacheEnabled(true);
		setDateTime();

	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		pageViewaList = new PageViewList();
		pViewBox.viewBox(this, pageViewaList);
		pageViewaList.tv_title.setText(getResources().getString(
				R.string.complete));
		pageViewaList.ll_back.setVisibility(View.GONE);
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_uploadepic.setOnClickListener(this);
		pageViewaList.ll_setsex.setOnClickListener(this);
		pageViewaList.bt_login_complete.setOnClickListener(this);
		pageViewaList.ll_setbirthday.setOnClickListener(this);
		pageViewaList.ll_setaddress.setOnClickListener(this);

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
		case R.id.ll_uploadepic:
			showDialog();
			break;
		case R.id.ll_setbirthday:
			ChangBirthday();
			break;
		case R.id.ll_setaddress:
			ChangAddress();
			break;
		case R.id.ll_setsex:
			SexSelect();
			break;
		case R.id.bt_login_complete:
			photo = pageViewaList.iv_uploadepic_complete.getDrawingCache();
			img = getBitmapByte(photo);
			if (TextUtils.isEmpty(pageViewaList.tv_sex.getText().toString())) {
				pageViewaList.tv_remind.setText(getResources().getString(
						R.string.selectsex));
				return;
			} else if (TextUtils.isEmpty(pageViewaList.tv_setbirth.getText()
					.toString())) {
				pageViewaList.tv_remind.setText(getResources().getString(
						R.string.selectbirthday));
				return;
			} else {
				CommiteToServer();
			}
			break;
		}

	}

	/**
	 * 
	 * commit
	 */
	private void CommiteToServer() {
		// LoginBean loginBean = new LoginBean();
		sendUpdateRequest(mShareFileUtils.getString(Constant.CLIENT_ID, ""),
				pageViewaList.tv_setbirth.getText().toString().trim(),
				pageViewaList.tv_sex.getText().toString().trim(),
				pageViewaList.tv_setaddress.getText().toString().trim(),
				mShareFileUtils.getString(Constant.USERNAME, ""));

	}

	/**
	 * 更改用户信息请求
	 * 
	 * @param client_id
	 * @param tv_setbirth
	 * @param tv_sex
	 * @param tv_setaddress
	 * @param username
	 * @throws Exception
	 **/

	private void sendUpdateRequest(String client_id, String tv_setbirth,
			String tv_sex, String tv_setaddress, String username) {
		// TODO Auto-generated method stub
		final Intent intent = new Intent();

		RequestParams params = null;
		try {
			params = PeerParamsUtils.getUpdateParams(
					RegisterCompleteActivity.this, client_id, tv_setbirth,
					tv_sex, tv_setaddress, username);
			File file = new File(Constant.C_IMAGE_CACHE_PATH + "head.png");// 将要保存图片的路径
			if (file.exists()) {
				params.put("image", file);
			} else {
				params.put("image", file);
			}
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

						showToast(
								getResources().getString(R.string.config_login),
								Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, responseString,
								throwable);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONArray errorResponse) {
						// TODO Auto-generated method stub

						showToast(
								getResources().getString(R.string.config_login),
								Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub

						showToast(
								getResources().getString(R.string.config_login),
								Toast.LENGTH_SHORT, false);
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub

						try {
							JSONObject result = response
									.getJSONObject("success");

							String code = result.getString("code");
							if (code.equals("200")) {
								LoginBean loginBean = JsonDocHelper
										.toJSONObject(
												response.getJSONObject(
														"success").toString(),
												LoginBean.class);
								if (loginBean != null) {
									BussinessUtils.saveUserData(loginBean,
											mShareFileUtils);
									if (EMChatManager.getInstance()
											.isConnected()) {
										pLog.i("huanxin", "环信已登录");
									} else {
										
										easemobchatImp.getInstance().login(
												mShareFileUtils.getString(
														Constant.CLIENT_ID, ""),
												mShareFileUtils.getString(
														Constant.PASSWORD, ""));
										easemobchatImp.getInstance()
												.loadConversationsandGroups();
									}

									/** 向已注册用户推荐我 **/
									sendpushme(mShareFileUtils.getString(
											Constant.CLIENT_ID, ""));

									startActivityForLeft(MainActivity.class,
											intent, false);
								} else if (code.equals("500")) {

								} else {
									String message = result
											.getString("message");
									showToast(message, Toast.LENGTH_SHORT,
											false);
								}

							} else if (code.equals("500")) {

							} else {
								String message = result.getString("message");
								showToast(message, Toast.LENGTH_SHORT, false);
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

					// @Override
					// public void onSuccess(int statusCode, Header[] headers,
					// String responseString) {
					// // TODO Auto-generated method stub
					//
					// super.onSuccess(statusCode, headers, responseString);
					// Intent login_complete = new Intent();
					// startActivityForLeft(MainActivity.class,
					// login_complete, false);
					// }
					//
				});
	}

	/**
	 * 向已注册用户推荐我请求
	 * 
	 * @param client_id
	 * @throws Exception
	 **/

	private void sendpushme(String client_id) {
		// TODO Auto-generated method stub
		final Intent intent = new Intent();

		RequestParams params = null;
		try {
			params = PeerParamsUtils
					.getblacklistParams(RegisterCompleteActivity.this);
			File file = new File(Constant.C_IMAGE_CACHE_PATH + "head.png");// 将要保存图片的路径
			if (file.exists()) {
				params.put("image", file);
			} else {
				params.put("image", file);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		HttpUtil.post(this, HttpConfig.PUSH_IN_URL + client_id + "/push.json",
				params, new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						// TODO Auto-generated method stub

						super.onFailure(statusCode, headers, responseString,
								throwable);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONArray errorResponse) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub

						try {
							JSONObject result = response
									.getJSONObject("success");

							String code = result.getString("code");
							if (code.equals("200")) {
								LoginBean loginBean = JsonDocHelper
										.toJSONObject(
												response.getJSONObject(
														"success").toString(),
												LoginBean.class);
								if (loginBean != null) {
								} else if (code.equals("500")) {

								} else {
									String message = result
											.getString("message");
									showToast(message, Toast.LENGTH_SHORT,
											false);
								}

							} else if (code.equals("500")) {

							} else {
								String message = result.getString("message");
								showToast(message, Toast.LENGTH_SHORT, false);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						super.onSuccess(statusCode, headers, response);
					}

				});
	}

	public byte[] getBitmapByte(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}
		final ByteArrayOutputStream os = new ByteArrayOutputStream();

		bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
		return os.toByteArray();
	}

	private void SexSelect() {
		// TODO Auto-generated method stub
		final String[] items = getResources().getStringArray(R.array.sex);
		new AlertDialog.Builder(RegisterCompleteActivity.this)
				.setTitle(getResources().getString(R.string.sex))
				.setItems(items, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						pageViewaList.tv_sex.setText(items[which]);
					}
				}).show();
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

	private void ChangBirthday() {
		// TODO Auto-generated method stub
		Message msg = new Message();

		msg.what = SHOW_DATAPICK;

		dateandtimeHandler.sendMessage(msg);
	}

	Handler dateandtimeHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_DATAPICK:
				showDialog(DATE_DIALOG_ID);
				break;
			}
		}
	};

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
		pageViewaList.tv_setbirth.setText(new StringBuilder().append(mYear)
				.append("-")
				.append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1))
				.append("-").append((mDay < 10) ? "0" + mDay : mDay));
	}

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

	private void ChangAddress() {
		// TODO Auto-generated method st
		Intent intent = new Intent(RegisterCompleteActivity.this,
				GetAddressInfoActivity.class);
		startActivityForResult(intent, CYTYSELECT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_CANCELED) {
			switch (requestCode) {
			case CYTYSELECT:
				if (data.getStringExtra("city") == null) {
					pageViewaList.tv_setaddress.setText(data
							.getStringExtra("province"));
				} else {
					pageViewaList.tv_setaddress.setText(data
							.getStringExtra("province")
							+ "-"
							+ data.getStringExtra("city"));
				}
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
					showToast(getResources().getString(R.string.sdcard),
							Toast.LENGTH_LONG, false);
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

	public Bitmap getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			photo = extras.getParcelable("data");
			BussinessUtils.saveBitmapFile(photo);
			pageViewaList.iv_uploadepic_complete.setImageBitmap(photo);
		}
		return photo;

	}

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
