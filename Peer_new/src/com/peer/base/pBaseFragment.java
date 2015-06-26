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

	/** 共享文件工具类 **/
	public pShareFileUtils mShareFileUtils = new pShareFileUtils();
	/** 退出倒计时 **/
	private long mExitTime;
	/** 提示条 **/
	public Toast toast;
	/** 中间布局 **/
	private RelativeLayout contentLayout;
	/** 顶部布局 **/
	private RelativeLayout topLayout;
	/** 灰色布局 **/
	private LinearLayout shadeBg;
	/** 当前页面的名称 **/
	public String currentPageName = null;
	
	
	
	/**
	 * 初始化共享工具类
	 */
	public void initShareUtils() {
		mShareFileUtils.initSharePre(getActivity(),
				Constant.SHARE_NAME, 0);
	}
	
	

	
	
	/**
	 * 退出APP
	 */
	public void exitApp() {
		if ((System.currentTimeMillis() - mExitTime) > 2000) {
			showToast("再按一次退出", Toast.LENGTH_SHORT, false);
			mExitTime = System.currentTimeMillis();
		} else {
			System.exit(0);

		}
	}
	
	/**
	 * 显示toast，会判断当前是否在toast，如果正在toast，先取消，再显示最新的toast，防止toast时间过长
	 * 
	 * @param arg
	 *            toast内容
	 * @param length
	 *            toast显示长短（Toast.LENGTH_LONG,Toast.LENGTH_SHORT）
	 * @param isCenter
	 *            提示条是否要居中
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
	 * 连接网络判断类
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
