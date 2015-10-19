package com.peer.service;

import java.io.File;

import com.peer.utils.BussinessUtils;

import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

/**
 * 监听升级下载情况
 * 下载完成时调用系统安装
 * @author weisiky
 *
 */
public class CompleteReceiver extends BroadcastReceiver {  
	  
    private DownloadManager downloadManager;  
    
    /** 下载至文件夹名 **/
	public String DOWNLOAD_FOLDER_NAME = "peerdownload";

	/** 下载文件名 **/
	 public String DOWNLOAD_FILE_NAME   = "peer.apk";
  
    @Override  
    public void onReceive(Context context, Intent intent) {  
          
        String action = intent.getAction();  
        if(action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {  
            Toast.makeText(context, "下载完成了....", Toast.LENGTH_LONG).show();  
              
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);                                                                                      //TODO 判断这个id与之前的id是否相等，如果相等说明是之前的那个要下载的文件  
            Query query = new Query();  
            query.setFilterById(id);  
            downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);  
            Cursor cursor = downloadManager.query(query);  
              
            int columnCount = cursor.getColumnCount();  
            String path = null;                                                                                                                                       //TODO 这里把所有的列都打印一下，有什么需求，就怎么处理,文件的本地路径就是path  
            while(cursor.moveToNext()) {  
                for (int j = 0; j < columnCount; j++) {  
                    String columnName = cursor.getColumnName(j);  
                    String string = cursor.getString(j);  
                    if(columnName.equals("local_uri")) {  
                        path = string;  
                    }  
                    if(string != null) {  
                        System.out.println("string:"+columnName+": "+ string);  
                    }else {  
                        System.out.println("string:"+columnName+": null");  
                    }  
                }  
            }  
            cursor.close();  
        //如果sdcard不可用时下载下来的文件，那么这里将是一个内容提供者的路径，这里打印出来，有什么需求就怎么样处理                                                 
            if(path.startsWith("content:")) {  
                               cursor = context.getContentResolver().query(Uri.parse(path), null, null, null, null);  
                               columnCount = cursor.getColumnCount();  
                               while(cursor.moveToNext()) {  
                                    for (int j = 0; j < columnCount; j++) {  
                                                String columnName = cursor.getColumnName(j);  
                                                String string = cursor.getString(j);  
                                                if(string != null) {  
                                                     System.out.println("string:"+columnName+": "+ string);  
                        }else {  
                            System.out.println("string:"+columnName+": null");  
                        }  
                    }  
                }
                  
                cursor.close();  
            }
            
            //apkFilePath为下载时指定的本地路径。
            String apkFilePath = new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath())
            .append(File.separator).append(DOWNLOAD_FOLDER_NAME).append(File.separator)
            .append(DOWNLOAD_FILE_NAME).toString();
            
            //安装
            BussinessUtils.openFile(context, new File(apkFilePath));
            
        }else if(action.equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {  
       
        }  
    } 
    
    
}  
