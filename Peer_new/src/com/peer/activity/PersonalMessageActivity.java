package com.peer.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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

import com.peer.base.pBaseActivity;
import com.peer.utils.Tools;
import com.peer.utils.pViewBox;

/*
 * 修改用户基本信息类
 **/
public class PersonalMessageActivity extends pBaseActivity{
	
	private String[] items;
	private static final int CYTYSELECT=3;
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	public static final int UPDATENIKENAME=4;
	Bitmap photo;
	byte[] img;
	
	private static final String IMAGE_FILE_NAME = "faceImage.png";
	
	private int mYear;  						
	private int mMonth;
	private int mDay; 
	private static final int SHOW_DATAPICK = 0; 
	private static final int DATE_DIALOG_ID = 1;
	
	private String email;
	
	private PageViewList pageViewaList;
	
	class PageViewList {
		private LinearLayout ll_back,ll_imfor_personmessage,ll_headpic,ll_updatenike,ll_setsex,ll_setbirthday_my,ll_setaddress_my;
		private TextView tv_title,et_nikename_personMSG,tv_sex,tv_setbirthday_my,tv_setaddress_my;
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
		pageViewaList.tv_title.setText(getResources().getString(R.string.personalmessage));
		items = getResources().getStringArray(R.array.pictrue);
		pageViewaList.tv_sex.setText("男");
		pageViewaList.tv_setbirthday_my.setText("1993-01-18");
		pageViewaList.tv_setaddress_my.setText("天津");
		pageViewaList.et_nikename_personMSG.setText("weisiky");
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
		return getLayoutInflater().inflate(R.layout.activity_my_personmessage, null);
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
			Intent intent=new Intent(PersonalMessageActivity.this,UpdateNikeActivity.class);
			intent.putExtra("nikename", pageViewaList.et_nikename_personMSG.getText().toString());
			startActivityForResult(intent, UPDATENIKENAME);
			break;
		case R.id.bt_update:
			photo=pageViewaList.iv_headpic_personMSG.getDrawingCache();
			img=getBitmapByte(photo);
//			if(checkNetworkState()){
				CommiteToServer();
//			}else{
//				ShowMessage(getResources().getString(R.string.Broken_network_prompt));
//			}			
			break;
		default:
			break;
		}
		
	}
	
	/*
	 * ChangBirthday类
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
	     * 初始化Calendar控件类
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
	    	 pageViewaList.tv_setbirthday_my.setText(new StringBuilder().append(mYear).append("-")
	           .append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("-")
	                 .append((mDay < 10) ? "0" + mDay : mDay)); 
	   }
	    
	    /*
	     * ChangAddress类
	     * */
	    private void ChangAddress() {
			// TODO Auto-generated method st
			Intent intent=new Intent(PersonalMessageActivity.this, GetAddressInfoActivity.class);
			startActivityForResult(intent,CYTYSELECT);
		}
	    
	    
	    /*
	     * 更换头像类
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
	     * 事件处理完成，返回更新UI类
	     * */
	    @Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			if (resultCode != RESULT_CANCELED) {
				switch (requestCode) {
				case CYTYSELECT:
					if(data.getStringExtra("city")==null){
						pageViewaList.tv_setaddress_my.setText(data.getStringExtra("province"));
					}else{
						pageViewaList.tv_setaddress_my.setText(data.getStringExtra("province") + "-" + data.getStringExtra("city"));
					}				
					break;
				case UPDATENIKENAME:
					pageViewaList.et_nikename_personMSG.setText(data.getStringExtra("newnikename"));
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
	    
	    
	    /*
	     * 更改性别类
	     * */
	    private void SexSelect() {
			// TODO Auto-generated method stub
			 final String[] items = getResources().getStringArray(  
	                 R.array.sex);  
	         new AlertDialog.Builder(PersonalMessageActivity.this)  
	                 .setTitle(getResources().getString(R.string.sex))  
	                 .setItems(items, new DialogInterface.OnClickListener() {  

	                     public void onClick(DialogInterface dialog,  
	                             int which) {  
	                    	 pageViewaList.tv_sex.setText(items[which]);  
	                     }  
	                 }).show();  
		}
	    
	    
	    /*
	     * CommiteToServer类
	     * 模拟完成时，跳转至myfragment
	     * */
	    private void CommiteToServer() {
			// TODO Auto-generated method stub
			finish();	
		}
}
