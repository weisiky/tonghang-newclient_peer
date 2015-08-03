package com.peer.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.peer.base.Constant;
import com.peer.bean.LoginBean;

/**
 * 工具类
 * 
 * @author zhzhg
 * @version 1.0.0
 */
public class BussinessUtils {

	/**
	 * 读取 displayMetrics 对象
	 * 
	 * @param context
	 * @return
	 */
	public static DisplayMetrics getDisplayMetrics(Activity context) {
		DisplayMetrics displayMetrics = context.getResources()
				.getDisplayMetrics();
		return displayMetrics;
	}

	/**
	 * 缩放图片
	 * 
	 * @param bitmap
	 * @param w
	 * @param h
	 * @return
	 */
	public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {

		Bitmap BitmapOrg = bitmap;

		int width = BitmapOrg.getWidth();
		int height = BitmapOrg.getHeight();
		int newWidth = w;
		int newHeight = h;
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);

		Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
				height, matrix, true);
		return resizedBitmap;

	}

	/**
	 * 从本地装饰一张图片
	 * 
	 * @param filePath
	 *            文件路径
	 * @param maxSize
	 *            压缩成最大的字节数 目前单位是k
	 * @return Bitmap 压缩后的bitmap
	 */
	public static Bitmap decodeFile(String filePath, int maxSize) {
		if (filePath != null && filePath.length() > 0) {
			File file = new File(filePath);
			if (file.exists()) {
				long fileSize = file.length();
				long scaleSize = fileSize / (maxSize * 1024);
				Options option = new Options();
				option.inSampleSize = (int) scaleSize;
				if (scaleSize < 1) {
					option.inSampleSize = 1;
				}
				return BitmapFactory.decodeFile(filePath, option);

			}
		}
		return null;
	}

	/**
	 * 以行为单位读取文件，常用于读面向行的格式化文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @param fileName
	 *            文件名
	 * @return String 处理的结果字符串对象，当结果为空时，返回null
	 */
	public static String readFileByLines(String filePath, String fileName) {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			BufferedReader reader = null;
			StringBuffer data = new StringBuffer();
			try {
				File file = new File(filePath + fileName);
				if (file.exists()) {
					reader = new BufferedReader(new FileReader(filePath
							+ fileName));
					String tempString = null;
					while ((tempString = reader.readLine()) != null) {
						data.append(tempString);
					}
					reader.close();
					return data.toString().trim();
				} else {
					return null;
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e1) {
					}
				}
			}
		}
		return null;
	}

	/**
	 * 读取设备的网卡地址
	 * 
	 * @param context
	 * @return mac地址
	 */
	public static String getMacAddress(Context context) {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		if (info != null) {
			return info.getMacAddress();
		} else {
			return "";
		}

	}

	/**
	 * 是不是邮箱地址 just if the passed email address is syntactically valid or not
	 * 
	 * @param email
	 * @return
	 */
	private static final String emailAddrReg = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}"
			+ "\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\"
			+ ".)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

	public static boolean isEmailAddr(String email) {
		if (TextUtils.isEmpty(email)) {
			return false;
		}
		if (email.matches(emailAddrReg)) {
			return true;
		}
		return false;
	}

	/**
	 * 读取异常信息
	 * 
	 * @param ex
	 * @return
	 */
	public static String getExceptionStr(Exception ex) {
		String errorInfo = null; // 错误信息
		ByteArrayOutputStream baos = null;
		PrintStream printStream = null;
		try {
			baos = new ByteArrayOutputStream();
			printStream = new PrintStream(baos);
			ex.printStackTrace(printStream);
			byte[] data = baos.toByteArray();
			errorInfo = new String(data);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (printStream != null) {
					printStream.close();
				}
				if (baos != null) {
					baos.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return errorInfo;
	}

	/**
	 * 保存异常信息
	 * 
	 * @param context
	 * @param buzType
	 * @param expType
	 * @param expContent
	 */
	public static void saveLog(Context context, String buzType, String expType,
			String expContent) {
		String netType = "";
		int netIntType = pNetUitls.getNetWorkType(context);
		if (netIntType == pNetUitls.TYPE_WIFI) {
			netType = "wifi";
		} else if (netIntType == pNetUitls.TYPE_NO) {
			netType = "没有网络";
		} else {
			netType = "2G或3G网络";
		}
		// NewsDbHelper.insertExpLog(netType, buzType, expType, expContent);
	}

	/**
	 * 获取系统时间
	 * 
	 * @return
	 */
	public static String getSysTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(new java.util.Date());
		return time;
	}

	/**
	 * 是否存在sim卡
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isExistSim(Context context) {
		TelephonyManager mTelephonyManager = (TelephonyManager) context
				.getSystemService(Service.TELEPHONY_SERVICE);
		if (mTelephonyManager.getSimState() != TelephonyManager.SIM_STATE_READY) // SIM卡没有就绪
		{
			return false;
		}
		return true;
	}

	public static boolean hasEclair() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR;
	}

	public static boolean hasFroyo() {

		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	public static boolean hasGingerbread() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	}

	/**
	 * 保存图片到相册
	 * 
	 * @param mContext
	 *            上下文
	 * @param bitmap
	 *            图片
	 * @param imageName
	 *            图片名称
	 * @return 保存结果
	 */
	public static Uri saveImageToMediaStore(Context mContext, Bitmap bitmap,
			String imageName) {
		Uri uri = null;
		try {
			ContentResolver cr = mContext.getContentResolver();
			String url = MediaStore.Images.Media.insertImage(cr, bitmap,
					imageName, imageName);
			uri = Uri.parse(url);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return uri;
	}

	/**
	 * 根据字符串和文字字号 计算字符串占据屏幕的宽度
	 * 
	 * @param content
	 *            字符串内容
	 * @param wordSize
	 *            文字大小
	 * @return 字符串占据屏幕的宽度
	 */
	public static float getStrWidth(String content, int wordSize) {
		Paint pFont = new Paint();
		pFont.setTextSize(wordSize);
		return pFont.measureText(content);
	}

	/**
	 * 获得当前网络名称
	 * 
	 * @param mContext
	 *            上下文
	 */
	public static String getNetWorkName(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// 获得当前网络信息
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isAvailable()) {
			if (networkInfo.getExtraInfo() != null) {
				return networkInfo.getExtraInfo();
			}
		}
		return "";
	}

	/**
	 * 判断是否为手机号
	 */
	public static boolean telNumMatch(String phoneNum) {
		/*
		 * 移动: 2G号段(GSM网络)有139,138,137,136,135,134,159,158,152,151,150,
		 * 3G号段(TD-SCDMA网络)有157,182,183,188,187 147是移动TD上网卡专用号段. 联通:
		 * 2G号段(GSM网络)有130,131,132,155,156 3G号段(WCDMA网络)有186,185 电信:
		 * 2G号段(CDMA网络)有133,153 3G号段(CDMA网络)有189,180,181
		 */
		String YD = "^[1]{1}(([3]{1}[4-9]{1})|([5]{1}[012789]{1})|([8]{1}[2378]{1})|([4]{1}[7]{1}))[0-9]{8}$";
		String LT = "^[1]{1}(([3]{1}[0-2]{1})|([5]{1}[56]{1})|([8]{1}[56]{1}))[0-9]{8}$";
		String DX = "^[1]{1}(([3]{1}[3]{1})|([5]{1}[3]{1})|([8]{1}[0-9]{1}))[0-9]{8}$";
		// 判断手机号码是否是11位
		if (phoneNum.length() == 11) {
			// 判断手机号码是否符合中国移动的号码规则
			if (phoneNum.matches(YD)) {
				return true;
			}
			// 判断手机号码是否符合中国联通的号码规则
			else if (phoneNum.matches(LT)) {
				return true;
			}
			// 判断手机号码是否符合中国电信的号码规则
			else if (phoneNum.matches(DX)) {
				return true;
			}
			// 都不合适 未知
			else {
				return false;
			}
		}
		// 不是11位
		else {
			return false;
		}
	}

	public static String strMd5(String str) {
		StringBuffer buf = new StringBuffer("");
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte b[] = md.digest();

			int i;

			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			str = buf.toString();
			System.out.println("result: " + buf.toString());// 32位加密
			System.out.println("result: " + buf.toString().substring(8, 24));// 16位加密

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return buf.toString();
	}

	/**
	 * 保存用户数据
	 */
	public static void saveUserData(LoginBean loginBean,
			pShareFileUtils mShareFileUtils) {
		pShareFileUtils.setString(Constant.PIC_SERVER,
				loginBean.getPic_server());
		pShareFileUtils.setString(Constant.EMAIL,
				loginBean.user.getEmail());
		pShareFileUtils.setString(Constant.SYS_TIME, loginBean.getSys_time());
		pShareFileUtils.setString(Constant.SEX, loginBean.user.getSex());
		pShareFileUtils.setString(Constant.USERNAME,
				loginBean.user.getUsername());
		pShareFileUtils.setString(Constant.PHONE, loginBean.user.getPhone());
		pShareFileUtils.setString(Constant.BIRTH, loginBean.user.getBirth());
		pShareFileUtils.setString(Constant.USER_IMAGE,
				loginBean.user.getImage());
		pShareFileUtils.setString(Constant.CREATED_AT,
				loginBean.user.getCreated_at());
		pShareFileUtils.setString(Constant.CITY, loginBean.user.getCity());
		pShareFileUtils.setString(Constant.CLIENT_ID,
				loginBean.user.getClient_id());
		pShareFileUtils.setString(Constant.LABELS, loginBean.user.getLabels().toString());
		pShareFileUtils.setBoolean(Constant.IS_FRIEND, loginBean.user.getIs_friend());
	
	}

	/**
	 * 清楚用户数据
	 */
	public static void clearUserData(pShareFileUtils mShareFileUtils) {
		pShareFileUtils.setString(Constant.PIC_SERVER, "");
		pShareFileUtils.setString(Constant.SYS_TIME, "");
		pShareFileUtils.setString(Constant.ID, "");
		pShareFileUtils.setString(Constant.SEX, "");
		pShareFileUtils.setString(Constant.USERNAME, "");
		pShareFileUtils.setString(Constant.PHONE, "");
		pShareFileUtils.setString(Constant.BIRTH, "");
		pShareFileUtils.setString(Constant.USER_IMAGE, "");
		pShareFileUtils.setString(Constant.CREATED_AT, "");
		pShareFileUtils.setString(Constant.CITY, "");
		pShareFileUtils.setString(Constant.CLIENT_ID, "");
		pShareFileUtils.setString(Constant.LABELS, "");
		pShareFileUtils.setString(Constant.EMAIL, "");
	}

}
