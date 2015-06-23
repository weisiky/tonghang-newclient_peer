package com.peer.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

/**
 * 文件工具类
 * 
 * @author zhzhg
 */
public class pIOUitls {

	/**
	 * 保存字节流到文件
	 * 
	 * @param data
	 *            字节数据
	 * @param path
	 *            保存路径
	 * @param fullPath
	 *            保存文件的全路径 包括文件名
	 */
	public static void saveBytes(byte[] data, String path, String fullPath) {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
			File picPath = new File(fullPath);
			BufferedOutputStream bufferedOutputStream;
			try {
				bufferedOutputStream = new BufferedOutputStream(
						new FileOutputStream(picPath));
				bufferedOutputStream.write(data);
				bufferedOutputStream.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * 保存字符串到SD卡
	 * 
	 * @param filePath
	 *            文件路径
	 * @param fileName
	 *            文件名
	 * @param isADD
	 *            是否是追加方式保存
	 * @param content
	 *            保存的内容
	 */
	public static void saveStrToSD(String filePath, String fileName,
			boolean isADD, String content) {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			try {
				File file = new File(filePath);
				if (!file.exists()) {
					file.mkdirs();
				}
				// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
				FileWriter writer = new FileWriter(filePath + fileName, isADD);
				writer.write(content);
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 删除指定文件
	 * 
	 * @param cachePath
	 *            图片url地址
	 */
	public static void deleteFile(final String cachePath) {
		File file = new File(cachePath);
		// 判断文件是否存在
		if (!file.exists()) {
			return;
		}
		// 删除指定文件
		file.delete();
	}

	/**
	 * 读取图片连接地址的文件名
	 */
	public static String getFileName(String url) {
		String fileName = url.substring(url.lastIndexOf("/") + 1);
		return fileName;
	}

	/**
	 * 删除指定目录下所有文件
	 * 
	 * @param filePath
	 *            删除文件路径
	 */
	public static void deleteAllFile(final String filePath) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					File display = new File(filePath);
					if (!display.exists()) {
						return;
					}
					File[] items = display.listFiles();
					int i = display.listFiles().length;
					for (int j = 0; j < i; j++) {
						if (items[j].isFile()) {
							items[j].delete();// 删除文件
						} else {
							// 迭代删除
							deleteAllFile(items[j].getAbsolutePath());
							// 删除目录
							items[j].delete();
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
		});
		t.start();
	}

	/**
	 * 保存缓存数据到本地
	 * 
	 * @param context
	 *            上下文
	 * @param path
	 *            目录
	 * @param filename
	 *            文件名
	 * @param content
	 *            数据
	 */
	public static void saveDataToCache(Context context, String path,
			String filename, String content) {
		try {
			File cachedir = new File(context.getCacheDir(), path);
			if (!cachedir.exists()) {
				cachedir.mkdirs();
			}
			File cachefile = new File(cachedir, filename);
			FileWriter writer = new FileWriter(cachefile, false);
			writer.write(content);
			writer.flush();
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 读取本地缓存数据
	 * 
	 * @param context
	 *            上下文
	 * @param path
	 *            目录
	 * @param filename
	 *            文件名
	 * @return string
	 */
	public static String getDataFromCache(Context context, String path,
			String filename) {
		BufferedReader reader = null;
		StringBuffer data = new StringBuffer();
		try {
			File cachedir = new File(context.getCacheDir(), path);
			File cachefile = new File(cachedir, filename);
			if (cachefile.exists()) {
				reader = new BufferedReader(new FileReader(cachefile));
				String tempString = null;
				while ((tempString = reader.readLine()) != null) {
					data.append(tempString);
				}

				reader.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e1) {
				}
			}
		}

		return data.toString().length() != 0 ? data.toString() : null;

	}

	/**
	 * 文件是否存在SD卡中
	 * 
	 * @param nfilePath
	 *            文件路径
	 * @return 存在 true 不存在 false
	 */
	public static boolean isExistFileInSD(String nfilePath) {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			File file = new File(nfilePath);
			if (file.exists()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * bytes转换成bitmap
	 * 
	 * @param data
	 *            需要转换的字节数据
	 * @param maxSize
	 *            压缩成最大的字节数 目前单位是k
	 * @return Bitmap
	 */
	public static Bitmap Bytes2Bimap(byte[] data, int maxSize) {
		if (data.length != 0) {
			int scaleSize = data.length / (maxSize * 1024);
			Options option = new Options();
			option.inSampleSize = scaleSize;
			if (scaleSize < 1) {
				option.inSampleSize = 1;
			}
			Bitmap temBmp = BitmapFactory.decodeByteArray(data, 0, data.length,
					option);
			return temBmp;
		} else {
			throw new NullPointerException();
		}
	}

	/**
	 * bytes转换成bitmap
	 * 
	 * @param data
	 *            需要转换的字节数据
	 * @param maxSize
	 *            压缩成最大的字节数 目前单位是k
	 * @return Bitmap
	 */
	public static Bitmap Bytes2Bimap(byte[] data) {
		if (data.length != 0) {
			Bitmap temBmp = BitmapFactory.decodeByteArray(data, 0, data.length,
					null);
			return temBmp;
		}
		return null;
	}

	/**
	 * Bitmap转换成bytes[]
	 * 
	 * @param bm
	 *            Bitmap对象
	 * @return byte[]
	 */
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * 设置剪切板
	 * 
	 * @param context
	 *            上下文
	 * @param content
	 *            剪切内容
	 */
	@SuppressWarnings("deprecation")
	public static void setClipboard(Context context, String content) {
		ClipboardManager clipboard = (ClipboardManager) context
				.getSystemService(Context.CLIPBOARD_SERVICE);
		clipboard.setText(content);
	}

	/**
	 * 读取剪切板内容
	 * 
	 * @param context
	 *            上下文
	 * @return 剪切内容
	 */
	@SuppressWarnings("deprecation")
	public String getClipboard(Context context) {
		ClipboardManager clipboard = (ClipboardManager) context
				.getSystemService(Context.CLIPBOARD_SERVICE);
		return clipboard.getText().toString();
	}

	/**
	 * 将null转换成""
	 * 
	 * @param content
	 *            原内容信息
	 * @return String
	 */
	public static String getEmptyString(String content) {
		if (content == null) {
			return "";
		}
		return content;
	}

	/**
	 * sd卡是否存在或者可用
	 * 
	 * @return
	 */
	public static boolean isExitSD() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}
}
