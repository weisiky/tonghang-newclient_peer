package com.peer.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;


/**
 * ��ȡ�ֻ�ϵͳ��sim�������Ϣ
 * 
 * @version 1.0.0
 */
@SuppressLint("NewApi")
public class pSysInfoUtils {
	private static TelephonyManager telephonyManager = null;// �绰����

	/**
	 * ��ȡ��ǰ����ϵͳ������
	 * 
	 * @return String ϵͳ����
	 */
	public static String getSysLanguage() {
		return Locale.getDefault().getLanguage();
	}

	/**
	 * ��ȡ�ֻ��ͺ�
	 * 
	 * @return String �ֻ��ͺ�
	 */
	public static String getModel() {
		return android.os.Build.MODEL;
	}

	/**
	 * ��ȡ����ϵͳ�İ汾��
	 * 
	 * @return String ϵͳ�汾��
	 */
	public static String getSysRelease() {
		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * ��ȡsim�����к�
	 */
	public static String readSimSerialNum(Context con) {
		String number = getTelephonyManager(con).getSimSerialNumber();
		return number != null ? number : "";
	}

	/**
	 * ��õ绰����ʵ������
	 * 
	 * @param con
	 *            ������
	 * @return ʵ������
	 */
	private static TelephonyManager getTelephonyManager(Context con) {
		if (telephonyManager == null) {
			telephonyManager = (TelephonyManager) con.getSystemService(Context.TELEPHONY_SERVICE);
			return telephonyManager;
		} else {
			return telephonyManager;
		}
	}

	/**
	 * ��Ψһ���豸ID
	 * 
	 * @param con
	 *            ������
	 * @return Ψһ���豸ID IMEI GSM�ֻ��� IMEI �� CDMA�ֻ��� MEID. �����ȡ��������һ��Ĭ���ַ���
	 */
	public static String readTelephoneSerialNum(Context con) {
		String telephoneSerialNumber = getTelephonyManager(con).getDeviceId();
		return telephoneSerialNumber != null ? telephoneSerialNumber : "000000000000000";
	}

	/**
	 * ��ȡϵͳ��ǰʱ�䣬��ȷ����
	 * 
	 * @return ���ص�ǰʱ���ַ���
	 */
	public static String getNowTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(Calendar.getInstance().getTime());
	}

	/**
	 * ��ȡϵͳ��ǰʱ���
	 * 
	 * @return
	 */
	public static long getNowTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}

	/**
	 * ��ȡ��Ӫ����Ϣ
	 * 
	 * @param con
	 *            ������
	 * @return String ��Ӫ����Ϣ
	 */
	public static String getCarrier(Context con) {
		TelephonyManager telManager = (TelephonyManager) con.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = telManager.getSubscriberId();
		if (imsi != null && imsi.length() > 0) {
			// ��Ϊ�ƶ�������46000�µ�IMSI�Ѿ����꣬����������һ��46002��ţ�134/159�Ŷ�ʹ���˴˱��
			if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
				return "China Mobile";
			} else if (imsi.startsWith("46001")) {
				return "China Unicom";
			} else if (imsi.startsWith("46003")) {
				return "China Telecom";
			}
		}
		return "δ��ʶ��";
	}

	/**
	 * �Ƿ����Sd��
	 * 
	 * @return true ���� ��false ������
	 */
	public static boolean getSDState() {
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			return true;
		}
		return false;
	}

	/**
	 * ��ȡSD��ʣ��ռ�Ĵ�С
	 * 
	 * @return long SD��ʣ��ռ�Ĵ�С����λ��byte��
	 */
	public static long getSDSize() {
		String str = Environment.getExternalStorageDirectory().getPath();
		StatFs localStatFs = new StatFs(str);
		long blockSize = localStatFs.getBlockSize();
		return localStatFs.getAvailableBlocks() * blockSize;
	}

	/**
	 * ��ȡSD��·��
	 * 
	 * @return
	 */
	public static String getSDPath() {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().getPath();
		}
		return "";
	}

	/**
	 * �绰״̬
	 * 
	 * @param con
	 *            ������
	 * @return 0���޻ 1������ 2������
	 */
	public static int getPhoneState(Context con) {
		return getTelephonyManager(con).getCallState();
	}

	/**
	 * ��õ绰��λ
	 * 
	 * @param con
	 *            ������
	 * @return ��λ����
	 */
	public static CellLocation getPhoneLoaction(Context con) {
		CellLocation cellLocation = getTelephonyManager(con).getCellLocation();
		return (CellLocation) (cellLocation != null ? cellLocation : "");
	}

	/**
	 * �豸������汾�ţ� the IMEI/SV(software version) for GSM phones.
	 * 
	 * @param con
	 *            ������
	 * @return ��֧�ַ��ء�not available��
	 */
	public static String getDeviceSoftVersion(Context con) {
		String version = getTelephonyManager(con).getDeviceSoftwareVersion();
		return version != null ? version : "not available";
	}

	/**
	 * ����ֻ���
	 * 
	 * @param con
	 *            ������
	 * @return �ֻ��� ��֧�־ͷ��ء�12322344345��
	 */
	public static String getPhoneNumber(Context con) {
		String number = getTelephonyManager(con).getLine1Number();
		return number != null ? number : "12322344345";
	}

	/**
	 * ���SIM���ṩ���ƶ���������ƶ�������.5��6λ��ʮ��������. SIM����״̬������
	 * SIM_STATE_READY(ʹ��getSimState()�ж�).
	 * 
	 * @param con
	 *            ������
	 * @return ����46002
	 */
	public static String getSimCode(Context con) {
		if (getTelephonyManager(con).getSimState() == 5) {
			String code = getTelephonyManager(con).getSimOperator();
			return code != null ? code : "";
		} else {
			return "";
		}
	}

	/**
	 * ���������ƣ����磺�й��ƶ�����ͨ SIM����״̬������ SIM_STATE_READY(ʹ��getSimState()�ж�).
	 * 
	 * @param con
	 *            ������
	 * @return ����������
	 */
	public static String getSimPrivatorName(Context con) {
		if (getTelephonyManager(con).getSimState() == 5) {
			String name = getTelephonyManager(con).getSimOperatorName();
			return name != null ? name : "";
		} else {
			return "";
		}
	}

	/**
	 * Ψһ���û�ID ���磺IMSI(�����ƶ��û�ʶ����) for a GSM phone. ��ҪȨ�ޣ�READ_PHONE_STATE
	 * 
	 * @param con
	 *            ������
	 * @return
	 */
	public static String getUserPhoneId(Context con) {
		return getTelephonyManager(con).getSubscriberId();
	}

	/**
	 * ��ȡ��Ļ������
	 * 
	 * @return DisplayMetrics ��Ļ�������
	 */
	public static DisplayMetrics getDisplayMetrics(Activity context) {
		DisplayMetrics displayMetrics = null;
		if (displayMetrics == null) {
			displayMetrics = new DisplayMetrics();
		}
		context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		return displayMetrics;
	}

	/**
	 * ��ȡ��Ļ�ܶ�
	 * 
	 * 
	 * @return DisplayMetrics ��Ļ�������
	 */
	public static float getScaledDensity(Activity context) {
		DisplayMetrics displayMetrics = getDisplayMetrics(context);
		// ��ȡʧ���򷵻���320x480�ֱ����ܶ�
		if (displayMetrics == null) {
			return 1.0f;
		}
		return displayMetrics.scaledDensity;
	}

	/**
	 * ��ȡ�豸��������ַ
	 * 
	 * @param context
	 * @return mac��ַ
	 */
	public static String getMacAddress(Context context) {
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		if (info != null && info.getMacAddress() != null) {
			return info.getMacAddress();
		} else {
			return "";
		}

	}

	/**
	 * ��ȡ�豸��������ַ
	 * 
	 * @return ������ַ
	 */
	@SuppressLint("NewApi")
	public static String getBluetoothAddress() {
		return BluetoothAdapter.getDefaultAdapter().getAddress();
	}


	/**
	 * �Ƿ����sim��
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isExistSim(Context context) {
		TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
		if (mTelephonyManager.getSimState() != TelephonyManager.SIM_STATE_READY) // SIM��û�о���
		{
			return false;
		}
		return true;
	}

	/**
	 * �жϵ�ǰ���л����Ƿ�Ϊģ����
	 * 
	 * @return true ��ǰ�豸Ϊģ���� false ��ǰ�豸Ϊ���
	 */
	public static boolean isEmulator() {
		return ("unknown".equals(Build.BOARD)) && ("generic".equals(Build.DEVICE)) && ("generic".equals(Build.BRAND));
	}

	/**
	 * �жϻ�ȡ��ǰ��Ļ���ܶ�
	 * 
	 * @return ��Ļ�ܶ�
	 */
	public static float getDensity(Activity activity) {
		/** ��ʼ���豸��Ļ���� */
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
		return mDisplayMetrics.density;
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
}
