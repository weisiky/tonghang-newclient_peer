package com.peer.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.peer.R;
import com.peer.IMimplements.easemobchatImp;
import com.peer.activity.LoginActivity;
import com.peer.activity.MainActivity;
import com.peer.activity.NewFriendsActivity;
import com.peer.activity.OtherPageActivity;
import com.peer.activity.SettingActivity;
import com.peer.activity.SingleChatRoomActivity;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.bean.JpushBean;
import com.peer.utils.JsonDocHelper;
import com.peer.utils.ManagerActivity;
import com.peer.utils.pLog;
import com.peer.utils.pShareFileUtils;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "push";

	public static final String NOTIFICATION_SERVICE = "notification";

	/** 共享文件工具类 **/
	public pShareFileUtils mShareFileUtils = new pShareFileUtils();

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction()
				+ ", extras: " + printBundle(bundle));
		
		mShareFileUtils.initSharePre(context,
				Constant.SHARE_NAME, 0);
		
		
		
		pLog.i("test", "jpush:" + bundle.getString("cn.jpush.android.MESSAGE"));
		JpushBean jpushBean = new JpushBean();

		try {
			jpushBean = JsonDocHelper.toJSONObject(
					bundle.getString("cn.jpush.android.MESSAGE"),
					JpushBean.class);

			if (jpushBean != null) {
				pLog.i("test", "jpushBean:" + jpushBean.getType());

				if (jpushBean.getType().equals("4")) {
					
					// 封号
					exitlogin(context,mShareFileUtils);
					

				} else if(jpushBean.getType().equals("6")){
					//删除话题
//					deletetopic(context,mShareFileUtils);
				}else{
					showNotification(context, jpushBean,
							(int) Math.random() * 100, mShareFileUtils);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void deletetopic(Context context, pShareFileUtils mShareFileUtils) {
		// TODO Auto-generated method stub
		Toast.makeText(context, "话题已被删除", Toast.LENGTH_SHORT);
		Intent intent = new Intent();
		((pBaseActivity)context).startActivityForLeft(MainActivity.class, intent, false);
	}

	private void exitlogin(Context context,pShareFileUtils mShareFileUtils) {
		// TODO Auto-generated method stub
//		mShareFileUtils.setString(Constant.CLIENT_ID, "");
		pLog.i("test","fenghao");
		((pBaseActivity)context).showToast("您已经被封号", Toast.LENGTH_SHORT, false);
		System.exit(0);
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	/**
	 * 展示推送通知
	 * 
	 * @param2：打开客户端进入首页
	 * @param3：资讯详情页面
	 * @param4：活动详情页面
	 * @param5：互动详情页面
	 * @param6：服务列表页面
	 * @param7：速递服务详情页面
	 * @param 8：速取服务详情也没有
	 * @param pushItemBean
	 */
	@SuppressWarnings({ "deprecation", "static-access" })
	public void showNotification(Context context, JpushBean jpushBean,
			int notifyId, pShareFileUtils mShareFileUtils) {
		mShareFileUtils.initSharePre(context, Constant.SHARE_NAME, 0);
		Notification notification = new Notification();
		notification.icon = R.drawable.logo; // 设置显示在手机最上边的状态栏的图标
		notification.flags |= Notification.FLAG_AUTO_CANCEL;// 点击通知后自动消失
		
		pLog.i("test","sound:"+ mShareFileUtils.getBoolean("sound", true));
		pLog.i("test","vibrate:"+ mShareFileUtils.getBoolean("vibrate", true));

		if (mShareFileUtils.getBoolean("sound", true)
				&& mShareFileUtils.getBoolean("vibrate", true)) {
			notification.defaults = Notification.DEFAULT_SOUND
					| Notification.DEFAULT_VIBRATE;
		} else {
			if (mShareFileUtils.getBoolean("sound", true)) {
				notification.defaults = Notification.DEFAULT_SOUND;// 声音默认
			} else if (mShareFileUtils.getBoolean("vibrate", true)) {
				notification.defaults = Notification.DEFAULT_VIBRATE;
			}
		}

		Intent intent = new Intent();
//		if (mShareFileUtils.getString(Constant.CLIENT_ID, "").length() > 0) {

			if (jpushBean.getType().equals("0")) {
				// 请求加好友
				intent.setClass(context, NewFriendsActivity.class);
			} else if (jpushBean.getType().equals("1")) {
				// 同意加好友
			} else if (jpushBean.getType().equals("2")) {
				// 拒绝加好友
			} else if (jpushBean.getType().equals("3")) {
				// 推荐新人--跳转到个人主页。
				pLog.i("test", "333");
				intent.putExtra("client_id", jpushBean.getId());
				intent.setClass(context, OtherPageActivity.class);
			} else if (jpushBean.getType().equals("5")) {
				// 解封
				intent.setClass(context, LoginActivity.class);
			}

//		} else {
//			// 没有注册跳转注册页面
//			pLog.i("test","no");
//			intent.setClass(context, LoginActivity.class);
//		}

		notification.tickerText = jpushBean.getMessage(); // 当前的notification被放到状态栏上的时候，提示内容
		PendingIntent pt = PendingIntent.getActivity(context, notifyId, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(context, "同行", jpushBean.getMessage(),
				pt);
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(NOTIFICATION_SERVICE);
		notificationManager.notify(notifyId, notification);

	}

}
