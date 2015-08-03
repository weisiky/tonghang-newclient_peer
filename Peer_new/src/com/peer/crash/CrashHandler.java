package com.peer.crash;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.peer.base.pBaseActivity;
import com.peer.utils.BussinessUtils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;

/**
 * 记录异常崩溃信息
 * 
 * @author zhangzg
 * 
 */

@SuppressLint("SimpleDateFormat")
public class CrashHandler implements UncaughtExceptionHandler {

	private static CrashHandler crashHandler;
	private Context context;
	private SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private UncaughtExceptionHandler defaultUncaughtExceptionHandler;
	/** 256字节 */
	private static final int FILE_MAX_SIZE = 256;

	private CrashHandler(Context context) {
		this.context = context;
	}

	// 单例
	public static CrashHandler instance(Context context) {
		if (crashHandler == null) {
			crashHandler = new CrashHandler(context);
		}
		return crashHandler;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// TODO Auto-generated method stub
		try {

			String fileName = Environment.getExternalStorageDirectory()
					.getPath() + "/crash.txt";
			File file = new File(fileName);
			if (file.exists() && file.length() > 1024 * FILE_MAX_SIZE
					&& file.canWrite()) {
				file.delete();
			}

			// 将crash log写入文件
			FileOutputStream fileOutputStream = new FileOutputStream(
					Environment.getExternalStorageDirectory().getPath()
							+ "/crash.txt", true);
			PrintStream printStream = new PrintStream(fileOutputStream);

			String time = dateFormat.format(new Date());

			printStream.append("time : " + time + "\n");
			ex.printStackTrace(printStream);
			printStream.flush();
			printStream.close();
			fileOutputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			BussinessUtils.clearUserData(((pBaseActivity)context).mShareFileUtils);
			exit();
			defaultUncaughtExceptionHandler.uncaughtException(thread, ex);
		}
	}

	// 设置默认处理器
	public void init() {
		defaultUncaughtExceptionHandler = Thread
				.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	private void exit() {
		android.os.Process.killProcess(android.os.Process.myPid());
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		am.killBackgroundProcesses(context.getPackageName());
		System.exit(0);
	}
}
