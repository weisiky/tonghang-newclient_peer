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
 * ����Activity
 * 
 * @author zhangzg
 * 
 */

public abstract class pBaseActivity extends Activity implements OnClickListener {

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.base);
		// ��ǰҳ������
		currentPageName = getLocalClassNameBySelf();
		// ��ȡ�ֻ���Ļ��ȣ�ͼƬ����ʹ��
		Constant.S_SCREEN_WIDHT_VALUE = pSysInfoUtils.getDisplayMetrics(this).widthPixels
				+ "";
		findGlobalViewById();
		// ��ʼ��ShareUtiles
		initShareUtils();
		// ����ͳ�� ���Ͳ���
		MobclickAgent.updateOnlineConfig(this);
		// ����ͳ�� ���ݼ���
		AnalyticsConfig.enableEncrypt(true);

		this.findViewById();
		this.setListener();
		this.processBiz();

	}

	/**
	 * ��ʼ����������
	 */
	public void initShareUtils() {
		mShareFileUtils.initSharePre(getApplicationContext(),
				Constant.SHARE_NAME, 0);
	}

	/**
	 * ��ȡȫ��ҳ��ؼ�����
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
	 * ��ȡҳ��ؼ�����
	 */
	protected abstract void findViewById();

	/**
	 * �󶨼����¼�
	 */
	protected abstract void setListener();

	/**
	 * ����ҵ��
	 */
	protected abstract void processBiz();

	/**
	 * ��ȡ��������
	 */
	protected abstract View loadTopLayout();

	/**
	 * ��ȡ���ݲ���
	 * 
	 * @param requestBean
	 */
	protected abstract View loadContentLayout();

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
	 * ���ص�ǰ����
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
