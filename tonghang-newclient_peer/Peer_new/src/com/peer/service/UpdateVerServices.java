package com.peer.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.Header;

import com.loopj.android.http.BinaryHttpResponseHandler;
import com.peer.R;
import com.peer.activity.WelComeActivity;
import com.peer.base.Constant;
import com.peer.net.HttpUtil;
import com.peer.utils.pLog;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.backup.FileBackupHelper;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

/**
 * 版本更新服务
 * 
 * @author zzg
 * @version 1.0.0
 */

public class UpdateVerServices extends Service {

	/** 通知管理器 **/
	private NotificationManager mNotificationManager;
	/** 通知 **/
	private Notification mNotification;
	/** apk缓存目录 **/
	private File updateDir;

	private final String TAG = "UpdateVerServices";
	/** 下载apk包的临时路径 */
	private String currentTempFilePath = "";
	/** 下载url的地址 **/
	private String apkUrl = "";
	/** notification唯一标识 **/
	private final int notificationId = 11111111;
	/** 上次进度条的大小 **/
	private int downloadCount = 0;
	/** 服务是否正在运行中 **/
	private boolean isRuning = false;
	
	/** 下载至文件夹名 **/
	public String DOWNLOAD_FOLDER_NAME = "peerdownload";

	/** 下载文件名 **/
	 public String DOWNLOAD_FILE_NAME   = "peer.apk";
	 
	 /** 存储下载的id **/
	 public static long downloadid;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		//在service中启动广播监听，便于监听下载完成调用安装。
		CompleteReceiver completeReceiver = new CompleteReceiver();
		/** register download success broadcast **/
		registerReceiver(completeReceiver,
						 new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
		
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (!isRuning) {
			isRuning = true;
			// 创建目录和文件
			File folder = Environment.getExternalStoragePublicDirectory(DOWNLOAD_FOLDER_NAME);
            if (!folder.exists() || !folder.isDirectory()) {
                folder.mkdirs();
            }
			apkUrl = intent.getStringExtra("apkUrl");
			
			DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);  
	          
		    Uri uri = Uri.parse(apkUrl);  
		    Request request = new Request(uri);  
		  
		    //设置允许使用的网络类型，这里是移动网络和wifi都可以    
		    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE|DownloadManager.Request.NETWORK_WIFI);    
		  
		    //禁止发出通知，既后台下载，如果要使用这一句必须声明一个权限：android.permission.DOWNLOAD_WITHOUT_NOTIFICATION    
		    //request.setShowRunningNotification(false);    
		  
		    //不显示下载界面    
		    request.setVisibleInDownloadsUi(true);  
		    /*设置下载后文件存放的位置,如果sdcard不可用，那么设置这个将报错，因此最好不设置
		     * 如果sdcard可用，下载后的文件在/mnt/sdcard/Android/data/packageName/files目录下面，
		     * 如果sdcard不可用,设置了下面这个将报错，不设置，下载后的文件在/cache这个  目录下面*/  
//			request.setDestinationInExternalFilesDir(this, null, "tar.apk");  
		    request.setDestinationInExternalPublicDir(DOWNLOAD_FOLDER_NAME, DOWNLOAD_FILE_NAME);
			long id = downloadManager.enqueue(request);  
			//TODO 把id保存好，在接收者里面要用，最好保存在Preferences里面  
			downloadid = id;

//			pLog.i("zzg", "updateDir:" + updateDir);
//
//			apkUrl = intent.getStringExtra("apkUrl");
//			pLog.i("zzg", "apkUrl:" + apkUrl);
//			if (apkUrl.contains("http")) {
//				String cacheFileName = apkUrl.hashCode() + "";
//				currentTempFilePath = updateDir.getPath() + "/" + cacheFileName
//						+ ".apk";
//
//				pLog.i("zzg", "cacheFileName:" + cacheFileName);
//
//				mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//				mNotification = new Notification(R.drawable.logo,
//						"同行安装中，请稍候...", System.currentTimeMillis());
//				mNotification.contentView = new RemoteViews(getPackageName(),
//						R.layout.updata_nitification);
//				// 设置通知栏显示内容
//				// 设置下载过程中，点击通知栏，回到主界面
//				Intent updateIntent = new Intent(this, WelComeActivity.class);
//				PendingIntent updatePendingIntent = PendingIntent.getActivity(
//						this, 0, updateIntent, 0);
//				// 设置通知栏显示内容
//				mNotification.contentIntent = updatePendingIntent;
//				mNotification.contentView.setProgressBar(R.id.progressBar, 100,
//						0, false);
//				mNotification.contentView.setTextViewText(R.id.txtProgress,
//						"0%");
//				// 发出通知
//				mNotificationManager.notify(notificationId, mNotification);
//				// delFile();
//
//				doDownloadTheFile(apkUrl, updateDir.getPath(),
//						currentTempFilePath);
//			} else {
//				stopSelf();
//			}
		}
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 下载apk文件
	 * 
	 * @param strPath
	 *            新apk的网络url
	 * @throws Exception
	 *             异常对象
	 * 暂无使用
	 */
	private void doDownloadTheFile(final String strPath,
			final String updatePath, final String savePathString) {
		
		
		String[] allowedContentTypes = new String[] { "Accept-Ranges/bytes",
				"text/html;charset=utf-8" };
		HttpUtil.get(this, strPath, new BinaryHttpResponseHandler(
				allowedContentTypes) {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] binaryData) {
				// TODO Auto-generated method stub

				int fileLength = 100;

				for (Header header : headers) {
					// 09-20 23:01:54.208: I/zzg(23569): Content-Length /
					// 8423880
					Log.i("zzg", header.getName() + " / " + header.getValue());
					if (header.getName().equals("Content-Length")) {
						fileLength = Integer.valueOf(header.getValue());
					}
				}
				Log.i("zzg", "strPath:" + strPath);
				Log.i("zzg", "statusCode:" + statusCode);
				if (statusCode == 200 && binaryData != null
						&& binaryData.length > 0) {
					Log.i("zzg", "binaryData:" + binaryData.length);
					boolean b = saveBytes(binaryData, updatePath,
							savePathString);
					if (b) {
						mNotification.defaults = Notification.DEFAULT_SOUND;// 铃声提醒
						mNotification.contentView.setViewVisibility(
								R.id.progressBarLayout, View.GONE);
						mNotification.contentView.setTextViewText(
								R.id.progressBar, "下载完成");
						mNotificationManager.cancel(notificationId);
						openFile(new File(currentTempFilePath));
					} else {
						int tCurrentCount = (int) (binaryData.length / fileLength);
						mNotification.contentView.setViewVisibility(
								R.id.progressBarLayout, View.VISIBLE);
						mNotification.contentView.setProgressBar(
								R.id.progressBar, 100, tCurrentCount, false);
						mNotification.contentView.setTextViewText(
								R.id.txtProgress, tCurrentCount + "%");
						if (downloadCount == 0 || downloadCount % 10 == 0) {
							mNotificationManager.notify(notificationId,
									mNotification);
						}
					}
				}

			}

			@Override
			public void onFailure(int arg0, Header[] headers, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				Log.i("zzg", "onFailure:" + arg3.toString());
				for (Header header : headers) {
					Log.i("zzg", header.getName() + " / " + header.getValue());
				}
			}
		});

	}

	/**
	 * 保存字节流到文件
	 * 
	 * @param data
	 *            字节数据
	 * @param path
	 *            保存路径
	 * @param fullPath
	 *            保存文件的全路径 包括文件名
	 * 暂无使用
	 */
	public boolean saveBytes(byte[] data, String path, String fullPath) {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
			File picPath = new File(fullPath);
			BufferedOutputStream bufferedOutputStream;
			try {
				if (file.createNewFile()) {
					bufferedOutputStream = new BufferedOutputStream(
							new FileOutputStream(picPath));
					bufferedOutputStream.write(data);
					bufferedOutputStream.close();
					return true;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 暂无使用
	 * 独立成工具类在BussinessUtils中
	 * @param weisiky
	 */
	private void openFile(File f) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		String type = getMIMEType(f);
		intent.setDataAndType(Uri.fromFile(f), type);
		startActivity(intent);
	}

	/**
	 * 暂无使用
	 * @param weisiky
	 */
	public void delFile() {
		File myFile = new File(currentTempFilePath);
		if (myFile.exists()) {
			myFile.delete();
		}
	}

	/**
	 * 暂无使用
	 * @param weisiky
	 */
	private String getMIMEType(File f) {
		return "application/vnd.android.package-archive";
	}

	/**
	 * 暂无使用
	 * @param weisiky
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		isRuning = false;
	}

}
