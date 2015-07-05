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

	
	public pShareFileUtils mShareFileUtils = new pShareFileUtils();
	
	private long mExitTime;
	
	public Toast toast;
	
	private RelativeLayout contentLayout;
	
	private RelativeLayout topLayout;
	
	private LinearLayout shadeBg;
	
	public String currentPageName = null;
	
	
	
	public void initShareUtils() {
		mShareFileUtils.initSharePre(getActivity(),
				Constant.SHARE_NAME, 0);
	}
	
	

	
	
	
	public void exitApp() {
		if ((System.currentTimeMillis() - mExitTime) > 2000) {
			showToast("再按一次退出", Toast.LENGTH_SHORT, false);
			mExitTime = System.currentTimeMillis();
		} else {
			System.exit(0);
		}
	}
	
	
	public  void showToast(String arg, int length, boolean isCenter) {
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
	
	
	public boolean checkNetworkState() {
		boolean flag = false;		
		ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);		
		if (manager.getActiveNetworkInfo() != null) {
			flag = manager.getActiveNetworkInfo().isAvailable();
		}
		return flag;
	}
}
