package com.peer.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.peer.activity.R;
import com.peer.utils.pShareFileUtils;
import com.peer.utils.pSysInfoUtils;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

/**
 * 基础Activity
 * 
 * @author zhangzg
 * 
 */

public abstract class pBaseActivity extends Activity implements OnClickListener {

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.base);
		// 当前页面名称
		currentPageName = getLocalClassNameBySelf();
		// 获取手机屏幕跨度，图片适配使用
		Constant.S_SCREEN_WIDHT_VALUE = pSysInfoUtils.getDisplayMetrics(this).widthPixels
				+ "";
		findGlobalViewById();
		// 初始化ShareUtiles
		initShareUtils();
		// 友盟统计 发送策略
		MobclickAgent.updateOnlineConfig(this);
		// 友盟统计 数据加密
		AnalyticsConfig.enableEncrypt(true);

		this.findViewById();
		this.setListener();
		this.processBiz();

	}

	/**
	 * 初始化共享工具类
	 */
	public void initShareUtils() {
		mShareFileUtils.initSharePre(getApplicationContext(),
				Constant.SHARE_NAME, 0);
	}

	/**
	 * 获取全局页面控件对象
	 */
	@SuppressWarnings("deprecation")
	protected void findGlobalViewById() {
		topLayout = (RelativeLayout) findViewById(R.id.topLayout);
		contentLayout = (RelativeLayout) findViewById(R.id.contentLayout);
		shadeBg = (LinearLayout) findViewById(R.id.shadeBg);

		RelativeLayout.LayoutParams layoutParamsContent = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT);
		RelativeLayout.LayoutParams layoutParamsTop = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		View topView = loadTopLayout();
		if (topView == null) {
			topLayout.setVisibility(View.GONE);
		} else {
			topLayout.setVisibility(View.VISIBLE);
			topLayout.addView(topView, layoutParamsTop);
		}
		View contentView = loadContentLayout();
		if (contentView == null) {
			contentLayout.setVisibility(View.GONE);
		} else {
			contentLayout.setVisibility(View.VISIBLE);
			contentLayout.addView(contentView, layoutParamsContent);
		}
	}

	/**
	 * 获取页面控件对象
	 */
	protected abstract void findViewById();

	/**
	 * 绑定监听事件
	 */
	protected abstract void setListener();

	/**
	 * 处理业务
	 */
	protected abstract void processBiz();

	/**
	 * 获取顶部布局
	 */
	protected abstract View loadTopLayout();

	/**
	 * 获取内容布局
	 * 
	 * @param requestBean
	 */
	protected abstract View loadContentLayout();

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
			toast = Toast.makeText(getApplicationContext(), arg, length);

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

	/**
	 * 返回当前类名
	 * 
	 * @return
	 */
	public String getLocalClassNameBySelf() {
		String lClssName = getLocalClassName();
		if (lClssName.contains(".")) {
			lClssName = lClssName.replace(".", "@@");
			String lClassArray[] = lClssName.split("@@");
			return lClassArray[lClassArray.length - 1];
		}
		return lClssName;
	}

}
