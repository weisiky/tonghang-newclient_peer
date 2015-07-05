package com.peer.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;

import com.peer.base.Constant;
import com.peer.bean.LoginBean;

import android.app.Activity;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
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

/**
 * 工具类
 * 
 * @author zhzhg
 * @version 1.0.0
 */
public class BussinessUtils {

	/**
	 * ��ȡ displayMetrics ����
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
	 * ����ͼƬ
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
	 * �ӱ���װ��һ��ͼƬ
	 * 
	 * @param filePath
	 *            �ļ�·��
	 * @param maxSize
	 *            ѹ���������ֽ��� Ŀǰ��λ��k
	 * @return Bitmap ѹ�����bitmap
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
	 * ����Ϊ��λ��ȡ�ļ��������ڶ������еĸ�ʽ���ļ�
	 * 
	 * @param filePath
	 *            �ļ�·��
	 * @param fileName
	 *            �ļ���
	 * @return String ����Ľ���ַ������󣬵����Ϊ��ʱ������null
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
	 * ��ȡ�豸��������ַ
	 * 
	 * @param context
	 * @return mac��ַ
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
	 * �ǲ��������ַ just if the passed email address is syntactically valid or
	 * not
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
	 * ��ȡ�쳣��Ϣ
	 * 
	 * @param ex
	 * @return
	 */
	public static String getExceptionStr(Exception ex) {
		String errorInfo = null; // ������Ϣ
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
	 * �����쳣��Ϣ
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
			netType = "û������";
		} else {
			netType = "2G��3G����";
		}
		// NewsDbHelper.insertExpLog(netType, buzType, expType, expContent);
	}

	/**
	 * ��ȡϵͳʱ��
	 * 
	 * @return
	 */
	public static String getSysTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(new java.util.Date());
		return time;
	}

	/**
	 * �Ƿ����sim��
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isExistSim(Context context) {
		TelephonyManager mTelephonyManager = (TelephonyManager) context
				.getSystemService(Service.TELEPHONY_SERVICE);
		if (mTelephonyManager.getSimState() != TelephonyManager.SIM_STATE_READY) // SIM��û�о���
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
	 * ����ͼƬ�����
	 * 
	 * @param mContext
	 *            ������
	 * @param bitmap
	 *            ͼƬ
	 * @param imageName
	 *            ͼƬ����
	 * @return ������
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
	 * �����ַ����������ֺ� �����ַ���ռ����Ļ�Ŀ��
	 * 
	 * @param content
	 *            �ַ�������
	 * @param wordSize
	 *            ���ִ�С
	 * @return �ַ���ռ����Ļ�Ŀ��
	 */
	public static float getStrWidth(String content, int wordSize) {
		Paint pFont = new Paint();
		pFont.setTextSize(wordSize);
		return pFont.measureText(content);
	}

	/**
	 * ��õ�ǰ��������
	 * 
	 * @param mContext
	 *            ������
	 */
	public static String getNetWorkName(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// ��õ�ǰ������Ϣ
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isAvailable()) {
			if (networkInfo.getExtraInfo() != null) {
				return networkInfo.getExtraInfo();
			}
		}
		return "";
	}

	/**
	 * �ж��Ƿ�Ϊ�ֻ���
	 */
	public static boolean telNumMatch(String phoneNum) {
		/*
		 * �ƶ�: 2G�Ŷ�(GSM����)��139,138,137,136,135,134,159,158,152,151,150,
		 * 3G�Ŷ�(TD-SCDMA����)��157,182,183,188,187 147���ƶ�TD������ר�úŶ�. ��ͨ:
		 * 2G�Ŷ�(GSM����)��130,131,132,155,156 3G�Ŷ�(WCDMA����)��186,185 ����:
		 * 2G�Ŷ�(CDMA����)��133,153 3G�Ŷ�(CDMA����)��189,180,181
		 */
		String YD = "^[1]{1}(([3]{1}[4-9]{1})|([5]{1}[012789]{1})|([8]{1}[2378]{1})|([4]{1}[7]{1}))[0-9]{8}$";
		String LT = "^[1]{1}(([3]{1}[0-2]{1})|([5]{1}[56]{1})|([8]{1}[56]{1}))[0-9]{8}$";
		String DX = "^[1]{1}(([3]{1}[3]{1})|([5]{1}[3]{1})|([8]{1}[0-9]{1}))[0-9]{8}$";
		// �ж��ֻ������Ƿ���11λ
		if (phoneNum.length() == 11) {
			// �ж��ֻ������Ƿ�����й��ƶ��ĺ������
			if (phoneNum.matches(YD)) {
				return true;
			}
			// �ж��ֻ������Ƿ�����й���ͨ�ĺ������
			else if (phoneNum.matches(LT)) {
				return true;
			}
			// �ж��ֻ������Ƿ�����й����ŵĺ������
			else if (phoneNum.matches(DX)) {
				return true;
			}
			// �������� δ֪
			else {
				return false;
			}
		}
		// ����11λ
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
		pShareFileUtils.setString(Constant.SYS_TIME, loginBean.getSys_time());
		pShareFileUtils.setString(Constant.ID, loginBean.user.getId());
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
		pShareFileUtils.setString(Constant.LABELS, loginBean.user.getLabels()
				.toString());

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

	}

}
