package com.peer.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;

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

import com.peer.base.pBaseActivity;
import com.peer.utils.Tools;
import com.peer.utils.pViewBox;


/*
 * 注册信息完善类。
 * 
 * */
public class RegisterCompleteActivity extends pBaseActivity{
	
	private String[] items;
	
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	Bitmap photo;
	byte[] img;	
	private static final String IMAGE_FILE_NAME = "faceImage.png";
	int width,height;

	private int mYear;  						
	private int mMonth;
	private int mDay; 
	
	private static final int CYTYSELECT=3;
	private static final int SHOW_DATAPICK = 0; 
	private static final int DATE_DIALOG_ID = 1;
	
	
	private PageViewList pageViewaList;
	
	class PageViewList {
		private LinearLayout ll_back,ll_uploadepic,back,ll_setsex,ll_setbirthday,ll_setaddress;
		private TextView tv_title;
		private ImageView iv_uploadepic_complete;
		private TextView tv_sex,tv_setbirth,tv_setaddress,tv_remind;
		private Button bt_login_complete;
		
	}

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		items = getResources().getStringArray(R.array.pictrue);
		pageViewaList.iv_uploadepic_complete.setDrawingCacheEnabled(true);
		setDateTime();	
		
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		pageViewaList = new PageViewList();
		pViewBox.viewBox(this, pageViewaList);
		pageViewaList.tv_title.setText(getResources().getString(R.string.complete));
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);
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
	protected View loadTopLayout() {
		// TODO Auto-generated method stub
		// return getLayoutInflater().inflate(R.layout.top_layout, null);
		return getLayoutInflater().inflate(R.layout.base_toplayout_title, null);
	}

	@Override
	protected View loadContentLayout() {
		// TODO Auto-generated method stub
		return getLayoutInflater().inflate(R.layout.activity_register_complete, null);
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
			photo=pageViewaList.iv_uploadepic_complete.getDrawingCache();
			img=getBitmapByte(photo);
			if(TextUtils.isEmpty(pageViewaList.tv_sex.getText().toString())){
				pageViewaList.tv_remind.setText(getResources().getString(R.string.selectsex));
				return;
			}else if(TextUtils.isEmpty(pageViewaList.tv_setbirth.getText().toString())){
				pageViewaList.tv_remind.setText(getResources().getString(R.string.selectbirthday));
				return;
			}else{
			Intent login_complete = new Intent(RegisterCompleteActivity.this,MainActivity.class);
			startActivity(login_complete);
			finish();
			}
			break;
		}
		
	}
	
	/*
	 * 存bitmap
	 * */
	public byte[] getBitmapByte(Bitmap bitmap) {
		if (bitmap == null) {  
		     return null;  
		  }  
		  final ByteArrayOutputStream os = new ByteArrayOutputStream();  
		  
		  bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);  
		  return os.toByteArray();
	}
	
	
	/*
	 * 复选框，选择性别
	 * */
	private void SexSelect() {
		// TODO Auto-generated method stub
		 final String[] items = getResources().getStringArray(  
                 R.array.sex);  
         new AlertDialog.Builder(RegisterCompleteActivity.this)  
                 .setTitle(getResources().getString(R.string.sex))  
                 .setItems(items, new DialogInterface.OnClickListener() {  

                     public void onClick(DialogInterface dialog,  
                             int which) {  
                    	 pageViewaList.tv_sex.setText(items[which]);  
                     }  
                 }).show();  
	}
	
	
	/*
	 * 图片存储
	 * */
	private void showDialog() {

		new AlertDialog.Builder(this)
				.setTitle(getResources().getString(R.string.picture))
				.setItems(items, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							Intent intentFromGallery = new Intent(Intent.ACTION_PICK,null);
							intentFromGallery.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");  
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
				.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();

	}
	
	/*
	 * 生日
	 * */
	private void ChangBirthday() {
		// TODO Auto-generated method stub
		 Message msg = new Message(); 
         
         msg.what = SHOW_DATAPICK;  
          
         dateandtimeHandler.sendMessage(msg);
         }
	
	Handler dateandtimeHandler = new Handler(){
	       @Override  
	       public void handleMessage(Message msg) {  
	           switch (msg.what) {  
	           case SHOW_DATAPICK:  
	               showDialog(DATE_DIALOG_ID);  
	               break; 
	           }
	       }
	    };
	    
	    /*
	     * 默认出生年月
	     * */
	    private void setDateTime(){
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
	     
	     private void updateDateDisplay(){
	    	 pageViewaList.tv_setbirth.setText(new StringBuilder().append(mYear).append("-")
	           .append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("-")
	                 .append((mDay < 10) ? "0" + mDay : mDay)); 
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
	 		Intent intent=new Intent(RegisterCompleteActivity.this, GetAddressInfoActivity.class);
	 		startActivityForResult(intent,CYTYSELECT);
	 	}
	     
	     
	     @Override
	 	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	 		if (resultCode != RESULT_CANCELED) {
	 			switch (requestCode) {
	 			case CYTYSELECT:
	 				if(data.getStringExtra("city")==null){
	 					pageViewaList.tv_setaddress.setText(data.getStringExtra("province"));
	 				}else{
	 					pageViewaList.tv_setaddress.setText(data.getStringExtra("province") + "-" + data.getStringExtra("city"));
	 				}				
	 				break;
	 			case IMAGE_REQUEST_CODE:
	 				startPhotoZoom(data.getData());
	 				break;
	 			case CAMERA_REQUEST_CODE:
	 				if (Tools.hasSdcard()) {
	 					File tempFile = new File(
	 							Environment.getExternalStorageDirectory()
	 									+"/"+ IMAGE_FILE_NAME);
	 					startPhotoZoom(Uri.fromFile(tempFile));
	 				} else {
	 					showToast(getResources().getString(R.string.sdcard), Toast.LENGTH_LONG, false);
	 				}
	 				break;
	 			case RESULT_REQUEST_CODE:
	 				if (data != null) {
	 					Bitmap bt=getImageToView(data);
	 					img=getBitmapByte(bt);
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
	 				
	 				pageViewaList.iv_uploadepic_complete.setImageBitmap(photo);
	 			}
	 			return photo;

	 		}
	 		
	 		
}
