package com.peer.webview;

import com.peer.R;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.utils.pViewBox;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * app 现实网页内容页面
 * 
 * @author lingxin
 */
public class WebViewActivity extends pBaseActivity {
	
	private PageViewList pageViewaList;
	
	class PageViewList {
	// webview布局
	private WebView webView;
	private LinearLayout ll_back;
	
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_view);
	}

//	@Override
//	protected View loadContentLayout() {
//		return getLayoutInflater().inflate(R.layout.web_view, null);
//	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void findViewById() {
		pageViewaList = new PageViewList();
		pViewBox.viewBox(this, pageViewaList);
		 //设置WebView属性，能够执行Javascript脚本  
		pageViewaList.webView.getSettings().setJavaScriptEnabled(true);  
	}

	@Override
	protected void setListener() {
		
		pageViewaList.ll_back.setOnClickListener(this);
		
		pageViewaList.webView.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				if (newProgress == 100) {
//					hideLoading(); // 隐藏进度条
				} else if (newProgress == 10) {
//					showProgressBar();
				}
			}

		});
		pageViewaList.webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 获取网页中的点击事件，加载网页地址
				view.loadUrl(url);
				return true;
			}
		});

	}

	@Override
	protected void processBiz() {
//		showProgressBar();
		pageViewaList.webView.loadUrl("http://"+mShareFileUtils.getString(Constant.SELF_ADV_URL, ""));
//		pageViewaList.webView.loadUrl("http://baidu.com");
		 //设置Web视图  
		pageViewaList.webView.setWebViewClient(new WebViewClient ());
	}


//	@Override
//	protected View loadTopLayout() {
//		return getLayoutInflater().inflate(R.layout.base_toplayout_title, null);
//	}
//
//	@Override
//	protected View loadBottomLayout() {
//		return null;
//	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (pageViewaList.webView.canGoBack()) {
			pageViewaList.webView.goBack();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	public void onNetworkOn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNetWorkOff() {
		// TODO Auto-generated method stub
		
	}
}
