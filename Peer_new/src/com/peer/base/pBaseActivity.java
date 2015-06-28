package com.peer.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.peer.activity.LoginActivity;
import com.peer.activity.R;
import com.peer.utils.BussinessUtils;
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

public abstract class pBaseActivity extends FragmentActivity implements
		OnClickListener {

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
	/** �ײ����� **/
	private RelativeLayout bottomLayout;
	/** ��ɫ���� **/
	private LinearLayout shadeBg;
	/** ��ǰҳ������� **/
	public String currentPageName = null;
	/** �ɰ彥�䶯�� **/
	public Animation alphaAnimation;
	/** �״ν���ҳ���loadingȦȦ **/
	public RelativeLayout baseProgressBarLayout;

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
		bottomLayout = (RelativeLayout) findViewById(R.id.bottomLayout);
		shadeBg = (LinearLayout) findViewById(R.id.shadeBg);

		RelativeLayout.LayoutParams layoutParamsContent = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT);
		RelativeLayout.LayoutParams layoutParamsTop = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams layoutParamsBottom = new RelativeLayout.LayoutParams(
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
		View bottomView = loadBottomLayout();
		if (bottomView == null) {
			bottomLayout.setVisibility(View.GONE);
		} else {
			bottomLayout.setVisibility(View.VISIBLE);
			bottomLayout.addView(bottomView, layoutParamsTop);
		}

		baseProgressBarLayout = (RelativeLayout) findViewById(R.id.baseProgressBarLayout);

		alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.shade_alpha);

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
	 * ��ȡ�ײ�����
	 * 
	 * @param requestBean
	 */
	protected abstract View loadBottomLayout();

	/**
	 * ҳ����ת
	 * 
	 * @param targetActivity
	 *            Ŀ��ҳ��
	 * @param intent
	 * @param isNewActivity
	 *            �Ƿ���Ҫһ���µ�ҳ��
	 */
	public void startActivityForLeft(Class targetActivity, Intent intent,
			boolean isNewActivity) {
		if (!isNewActivity) {
			intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		}
		if (targetActivity != null) {
			intent.setClass(this, targetActivity);
			startActivity(intent);
			if (BussinessUtils.hasEclair()) {
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
			}
		}
		this.finish();
	}

	/**
	 * ҳ����ת���Ҳ����
	 * 
	 * @param targetActivity
	 *            Ŀ��ҳ��
	 * @param intent
	 * @param isNewActivity
	 *            �Ƿ���Ҫһ���µ�ҳ��
	 */
	@SuppressWarnings("rawtypes")
	public void startActivityRight(Class targetActivity, Intent intent,
			boolean isNewActivity) {
		if (!isNewActivity) {
			intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		}
		if (targetActivity != null) {
			intent.setClass(this, targetActivity);
			startActivity(intent);
			if (BussinessUtils.hasEclair()) {
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
			}
		}
		this.finish();
	}

	/**
	 * ҳ����ת(��ֱ�����ƶ�)
	 * 
	 * @param targetActivity
	 *            Ŀ��ҳ��
	 * @param intent
	 * @param isNewActivity
	 *            �Ƿ���Ҫһ���µ�ҳ��
	 */
	public void startActivityForDown(Class targetActivity, Intent intent,
			boolean isNewActivity) {
		if (!isNewActivity) {
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		}
		if (targetActivity != null) {
			intent.setClass(this, targetActivity);
			startActivity(intent);
			if (BussinessUtils.hasEclair()) {
				overridePendingTransition(R.anim.push_down_in,
						R.anim.push_down_out);
			}
		}
		this.finish();
	}

	/**
	 * ҳ����ת(��ֱ�����ƶ�)
	 * 
	 * @param targetActivity
	 *            Ŀ��ҳ��
	 * @param intent
	 * @param isNewActivity
	 *            �Ƿ���Ҫһ���µ�ҳ��
	 */
	public void startActivityForUp(Class targetActivity, Intent intent,
			boolean isNewActivity) {
		if (!isNewActivity) {
			intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		}
		if (targetActivity != null) {
			intent.setClass(this, targetActivity);
			startActivity(intent);
			if (BussinessUtils.hasEclair()) {
				overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
			}
		}

		this.finish();
	}

	/**
	 * ҳ����ת
	 * 
	 * @param targetActivity
	 *            Ŀ��ҳ��
	 * @param intent
	 * @param requestCode
	 *            ����״̬��
	 * @param isNewActivity
	 *            �Ƿ���Ҫһ���µ�ҳ��
	 */
	public void startActivityForResult(Class targetActivity, Intent intent,
			int requestCode, boolean isNewActivity) {
		if (!isNewActivity) {
			intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		}
		if (targetActivity != null) {
			intent.setClass(this, targetActivity);
			startActivityForResult(intent, requestCode);
			if (BussinessUtils.hasEclair()) {
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
			}
		}
		this.finish();
	}

	/**
	 * �����ϸ�ҳ��
	 */
	public void backPage() {
		Intent intent = new Intent();

		if (getLocalClassNameBySelf().contains("RegisterAcountActivity")) {
			startActivityRight(LoginActivity.class, intent, true);
		} else {
			exitApp();
		}

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

	/**
	 * ��ʾloadingȦȦ
	 */
	public void showProgressBar() {
		showAlphaBg();
		baseProgressBarLayout.setVisibility(View.VISIBLE);
	}

	/**
	 * ����dialog
	 */
	public void hideLoading() {
		hidAlphaBg();
		shadeBg.clearAnimation();
		baseProgressBarLayout.setVisibility(View.GONE);
	}

	/**
	 * ��ʾ���䱳��
	 */
	public void showAlphaBg() {
		shadeBg.setOnClickListener(this);
		shadeBg.setVisibility(View.VISIBLE);
		shadeBg.startAnimation(alphaAnimation);
	}

	/**
	 * ���ؽ��䱳��
	 */
	public void hidAlphaBg() {
		shadeBg.setVisibility(View.GONE);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// �����ϸ�ҳ��
			backPage();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

	/*
	 * �����������¼�
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_back:
			backPage();
			break;
		default:
			break;

		}
	}

}
