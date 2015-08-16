package com.peer.service;

/**
 * 悬浮头像触发逻辑：
 * 1.首次启动app不触发。
 * 2.app启动后，进入自己创建的房间，（在不人为退出房间的时候）后退到主页面时，悬浮头像启动。
 * 3.悬浮头像只保留最后一次退出时的房间信息。
 * 4.退出后，悬浮头像跟随app到任何地方。
 * 5.app暂停至后台，悬浮头像消失。app恢复——头像恢复。
 * 6.用户退出该账户登入状态，悬浮头像消失。
 * 7.app异常退出，或者用户杀死该进程，悬浮头像也被杀死。
 */

import com.peer.activity.MultiChatRoomActivity;
import com.peer.R;
import com.peer.base.Constant;
import com.peer.bean.ChatRoomBean;
import com.peer.utils.ImageLoaderUtil;
import com.peer.utils.RoundImageView;
import com.peer.utils.pLog;
import com.peer.utils.pShareFileUtils;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;


public class FxService extends Service 
{	
	private String roomaddress,ownernike,theme,fromfloat,
	tagname,image,userId,topicid;
	int xdown;
	int ydown;
	int xup;
	int yup;
    LinearLayout mFloatLayout;
    WindowManager.LayoutParams wmParams;
    
	WindowManager mWindowManager;
	
	RoundImageView mFloatView;
	
	private static final String TAG = "FxService";
	
	/** 共享文件工具类 **/
	public pShareFileUtils mShareFileUtils = new pShareFileUtils();
	
	@Override
	public void onCreate() 
	{
		// TODO Auto-generated method stub
		super.onCreate();
		pLog.i("test", "oncreat");
        //Toast.makeText(FxService.this, "create FxService", Toast.LENGTH_LONG);		
	}
	@Override
	public IBinder onBind(Intent intent)
	{			
		return null;
	}
	@SuppressWarnings("static-access")
	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
//		image=intent.getStringExtra(Constant.F_IMAGE);
//		ownernike=intent.getStringExtra(Constant.F_OWNERNIKE);
//		theme=intent.getStringExtra(Constant.F_THEME);
//		tagname=intent.getStringExtra(Constant.F_TAGNAME);
//		userId=intent.getStringExtra(Constant.F_USERID);
//		topicid=intent.getStringExtra(Constant.F_TOPICID);
//		roomaddress=intent.getStringExtra(Constant.F_ROOMID);
		fromfloat = intent.getStringExtra(Constant.FROMFLOAT);
		mShareFileUtils.initSharePre(getApplicationContext(),
				Constant.SHARE_NAME, 0);
		
//		mShareFileUtils.setString(Constant.F_IMAGE, image);
//		mShareFileUtils.setString(Constant.F_OWNERNIKE, ownernike);
//		mShareFileUtils.setString(Constant.F_THEME, theme);
//		mShareFileUtils.setString(Constant.F_TAGNAME, tagname);
//		mShareFileUtils.setString(Constant.F_USERID, userId);
//		mShareFileUtils.setString(Constant.F_TOPICID, topicid);
//		mShareFileUtils.setString(Constant.F_ROOMID, roomaddress);
		mShareFileUtils.setString(Constant.FROMFLOAT, fromfloat);
		
		createFloatView();
		super.onStart(intent, startId);
	}
	private void createFloatView()
	{
		wmParams = new WindowManager.LayoutParams();
		
		mWindowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);
		
		wmParams.type = LayoutParams.TYPE_PHONE; 
		
        wmParams.format = PixelFormat.RGBA_8888; 
       wmParams.flags =  LayoutParams.FLAG_NOT_FOCUSABLE;
        
        wmParams.gravity = Gravity.TOP | Gravity.LEFT; 
       
        wmParams.x =mWindowManager.getDefaultDisplay().getWidth();
        wmParams.y =320;

        
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        
        LayoutInflater inflater = LayoutInflater.from(getApplication());
        
        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_layout, null);
      
        mWindowManager.addView(mFloatLayout, wmParams);

        mFloatView = (RoundImageView)mFloatLayout.findViewById(R.id.float_image);
        
        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        	
        System.out.println("泡泡："+image);
     // ImageLoader加载图片
     		ImageLoaderUtil.getInstance().showHttpImage(
     				mShareFileUtils.getString(Constant.PIC_SERVER, "") 
     				+ mShareFileUtils.getString(Constant.IMAGE, "")
     				, mFloatView,
     				R.drawable.mini_avatar_shadow);
       
		mFloatView.setOnTouchListener(new OnTouchListener() 
        {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{									
	            switch (event.getAction()) {
	            case MotionEvent.ACTION_DOWN:    //点击，不松开
	            	xdown=(int) event.getRawX();
	            	ydown=(int) event.getRawY();
	            	break;
	            case MotionEvent.ACTION_MOVE:    //移动后
	            	wmParams.x = (int) event.getRawX() - mFloatView.getMeasuredWidth()/2;
					
		            wmParams.y = (int) event.getRawY() - mFloatView.getMeasuredHeight()/2-25;
		          
		            mWindowManager.updateViewLayout(mFloatLayout, wmParams);
		            break;
	            case MotionEvent.ACTION_UP:     //点击后，松开
	            	xup=(int) event.getRawX();
	            	yup=(int) event.getRawY();
//	            	if(){
	            		if(-mFloatView.getMeasuredWidth()/2<xdown-xup&&xdown-xup<mFloatView.getMeasuredWidth()/2
	            				&&-(mFloatView.getMeasuredHeight()/2-25)<ydown-yup&&ydown-yup<(mFloatView.getMeasuredHeight()/2-25)){
	            			ChatRoomBean.getInstance().setChatroomtype(Constant.MULTICHAT);
	            			Intent intent=new Intent(FxService.this,MultiChatRoomActivity.class);
	            			intent.putExtra(Constant.F_THEME, mShareFileUtils.getString(Constant.F_THEME, ""));			
	            			intent.putExtra(Constant.F_TAGNAME, mShareFileUtils.getString(Constant.F_TAGNAME, ""));
//	            			intent.putExtra(Constant.F_USERID, userId);
	            			intent.putExtra(Constant.F_ROOMID, mShareFileUtils.getString(Constant.F_ROOMID, ""));
	            			intent.putExtra(Constant.F_TOPICID, mShareFileUtils.getString(Constant.F_TOPICID, ""));
	            			intent.putExtra(Constant.FROMFLOAT, fromfloat);
	            			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	            			startActivity(intent);
	            		}
//	            	}
					if(320<(int) event.getRawX()){
						 wmParams.x = 720;
						 wmParams.y = (int) event.getRawY()-128;
						 mWindowManager.updateViewLayout(mFloatLayout, wmParams);
						 
					}else{
						 wmParams.x = 0;
						 wmParams.y = (int) event.getRawY()-128;
						 mWindowManager.updateViewLayout(mFloatLayout, wmParams);						 
					}
					break;
				}
				return false;
			}
		});	
	}
	
	@Override
	public void onDestroy() 
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mFloatLayout != null)
		{
			mWindowManager.removeView(mFloatLayout);
		}
	}
	
}
