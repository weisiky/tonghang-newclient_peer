package com.peer.base;

import com.peer.utils.pShareFileUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public abstract class pBaseFragment extends Fragment implements OnClickListener{

	/** �����ļ������� **/
	public pShareFileUtils mShareFileUtils = new pShareFileUtils();
	/** �˳�����ʱ **/
	private long mExitTime;
	/** ��ʾ�� **/
	public Toast toast;
	/** �м䲼�� **/
	private RelativeLayout contentLayout;
	/** �������� **/
	private RelativeLayout topLayout;
	/** ��ɫ���� **/
	private LinearLayout shadeBg;
	/** ��ǰҳ������� **/
	public String currentPageName = null;
	
	
	
	/**
	 * ��ʼ����������
	 */
	public void initShareUtils() {
		mShareFileUtils.initSharePre(getActivity(),
				Constant.SHARE_NAME, 0);
	}
	
	

	
	
	/**
	 * �˳�APP
	 */
	public void exitApp() {
		if ((System.currentTimeMillis() - mExitTime) > 2000) {
			showToast("�ٰ�һ���˳�", Toast.LENGTH_SHORT, false);
			mExitTime = System.currentTimeMillis();
		} else {
			System.exit(0);

		}
	}
	
	/**
	 * ��ʾtoast�����жϵ�ǰ�Ƿ���toast���������toast����ȡ��������ʾ���µ�toast����ֹtoastʱ�����
	 * 
	 * @param arg
	 *            toast����
	 * @param length
	 *            toast��ʾ���̣�Toast.LENGTH_LONG,Toast.LENGTH_SHORT��
	 * @param isCenter
	 *            ��ʾ���Ƿ�Ҫ����
	 */
	public void showToast(String arg, int length, boolean isCenter) {
		if (toast == null) {
			toast = Toast.makeText(getActivity(), arg, length);

		} else {
			toast.setText(arg);
		}
		if (isCenter) {
			toast.setGravity(Gravity.CENTER, 0, 0);
		} else {
			toast.setGravity(Gravity.BOTTOM, 0, 0);
		}
		toast.show();
	}
	
	/*
	 * ���������ж���
	 * */
	public boolean checkNetworkState() {
		boolean flag = false;		
		ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);		
		if (manager.getActiveNetworkInfo() != null) {
			flag = manager.getActiveNetworkInfo().isAvailable();
		}
		return flag;
	}
}
