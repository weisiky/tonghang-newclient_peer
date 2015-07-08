package com.peer.share;

import java.util.HashMap;

import android.content.Context;
import android.os.Handler.Callback;
import android.os.Message;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.renren.Renren;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.mob.tools.utils.UIHandler;

/**
 * 分享工具配置类
 * 
 * @author zhangzg
 * 
 */
public class ShareConfig implements PlatformActionListener, Callback {

	public ShareConfig config;
	public Context context;

	public ShareConfig(Context context) {
		this.context = context;
	}

	/**
	 * 分享到朋友圈
	 */
	public void shareCircleFriend(String share_title, String share_url,
			String share_text, String share_image, String mShareImagePath) {
		Platform circle = ShareSDK.getPlatform(context, WechatMoments.NAME);

		cn.sharesdk.wechat.moments.WechatMoments.ShareParams sp = new cn.sharesdk.wechat.moments.WechatMoments.ShareParams();
		sp.setShareType(Platform.SHARE_WEBPAGE);// 一定要设置分享属性
		sp.setTitle(share_title);
		sp.setText(share_text);
		sp.setImageUrl(share_image);
		sp.setUrl(share_url);
		sp.setImagePath(mShareImagePath);
		circle.setPlatformActionListener(this); // 设置分享事件回调
		// 执行图文分享
		circle.share(sp);
	}

	/**
	 * 分享到微信好友
	 */
	public void shareWxFriend(String share_title, String share_url,
			String share_text, String share_image, String mShareImagePath) {
		Platform circle = ShareSDK.getPlatform(context, Wechat.NAME);

		cn.sharesdk.wechat.friends.Wechat.ShareParams sp = new cn.sharesdk.wechat.friends.Wechat.ShareParams();
		sp.setShareType(Platform.SHARE_WEBPAGE);// 一定要设置分享属性
		sp.setTitle(share_title);
		sp.setText(share_text);
		sp.setImageUrl(share_image);
		sp.setImagePath(mShareImagePath);
		sp.setUrl(share_url);

		circle.setPlatformActionListener(this); // 设置分享事件回调
		// 执行图文分享
		circle.share(sp);
	}

	/**
	 * 分享到QQ空间
	 */
	public void shareQzone(String share_title, String share_url,
			String share_text, String share_image, String mShareImagePath) {

		cn.sharesdk.tencent.qzone.QZone.ShareParams sp = new cn.sharesdk.tencent.qzone.QZone.ShareParams();
		sp.setShareType(Platform.SHARE_WEBPAGE);// 一定要设置分享属性
		sp.setTitle(share_title);
		sp.setText(share_text);
		sp.setTitleUrl(share_url);
		if (share_image.equals("") || share_image == null) {
			sp.setImagePath(mShareImagePath);
		} else if (mShareImagePath.equals("") || mShareImagePath == null) {
			sp.setImageUrl(share_image);
		}
		Platform qzone = ShareSDK.getPlatform(context, QZone.NAME);
		qzone.setPlatformActionListener(this); // 设置分享事件回调
		// 执行图文分享
		qzone.share(sp);
	}

	/**
	 * 分享到QQ好友
	 */
	public void shareQQFriend(String share_title, String share_url,
			String share_text, String share_image, String mShareImagePath) {
		Platform circle = ShareSDK.getPlatform(context, QQ.NAME);

		cn.sharesdk.tencent.qq.QQ.ShareParams sp = new cn.sharesdk.tencent.qq.QQ.ShareParams();
		sp.setShareType(Platform.SHARE_WEBPAGE);// 一定要设置分享属性
		sp.setTitle(share_title);
		sp.setUrl(share_url);
		sp.setText(share_text);
		if (share_image.equals("") || share_image == null) {
			sp.setImagePath(mShareImagePath);
		} else if (mShareImagePath.equals("") || mShareImagePath == null) {
			sp.setImageUrl(share_image);
		}
		sp.setTitleUrl(share_url);
		sp.setImagePath(mShareImagePath);

		circle.SSOSetting(false);
		circle.setPlatformActionListener(this); // 设置分享事件回调
		// 执行图文分享
		circle.share(sp);
	}

	/**
	 * 分享到新浪微博
	 */
	public void shareSinaWeibo(String share_title, String share_url,
			String share_text, String share_image, String mShareImagePath) {

		cn.sharesdk.sina.weibo.SinaWeibo.ShareParams sp = new cn.sharesdk.sina.weibo.SinaWeibo.ShareParams();
		sp.setShareType(Platform.SHARE_WEBPAGE);// 一定要设置分享属性
		sp.setText(share_text);
		sp.setUrl(share_url);
		if (share_image.equals("") || share_image == null) {
			sp.setImagePath(mShareImagePath);
		} else if (mShareImagePath.equals("") || mShareImagePath == null) {
			sp.setImageUrl(share_image);
		}
		Platform weibo = ShareSDK.getPlatform(context, SinaWeibo.NAME);
		weibo.setPlatformActionListener(this); // 设置分享事件回调
		// 执行图文分享
		weibo.share(sp);
	}

	/**
	 * 分享到人人网
	 */
	public void shareRenRen(String share_title, String share_url,
			String share_text, String share_image, String mShareImagePath) {

		cn.sharesdk.renren.Renren.ShareParams sp = new cn.sharesdk.renren.Renren.ShareParams();
		sp.setUrl(share_url);
		sp.setText(share_text);
		sp.setComment(share_text);
		if (share_image.equals("")) {
			sp.setImagePath(mShareImagePath);
		} else if (mShareImagePath.equals("")) {
			sp.setImageUrl(share_image);
		}
		Platform renren = ShareSDK.getPlatform(context, Renren.NAME);
		renren.setPlatformActionListener(this); // 设置分享事件回调
		// 执行图文分享
		renren.share(sp);

	}

	public boolean handleMessage(Message msg) {
		String text = actionToString(msg.arg2);

		switch (msg.arg1) {
		case 1:
			// 成功
			text = "分享成功";
			// InfoMessage.showMessage(context, text);
			break;
		case 2:
			// 失败
			text = "分享失败";
			break;
		case 3:
			// 取消
			text = "分享已取消";
			break;
		}
		// InfoMessage.showMessage(context, text);
		return false;
	}

	/** 将action转换为String */
	public static String actionToString(int action) {
		switch (action) {
		case Platform.ACTION_AUTHORIZING:
			return "ACTION_AUTHORIZING";
		case Platform.ACTION_GETTING_FRIEND_LIST:
			return "ACTION_GETTING_FRIEND_LIST";
		case Platform.ACTION_FOLLOWING_USER:
			return "ACTION_FOLLOWING_USER";
		case Platform.ACTION_SENDING_DIRECT_MESSAGE:
			return "ACTION_SENDING_DIRECT_MESSAGE";
		case Platform.ACTION_TIMELINE:
			return "ACTION_TIMELINE";
		case Platform.ACTION_USER_INFOR:
			return "ACTION_USER_INFOR";
		case Platform.ACTION_SHARE:
			return "ACTION_SHARE";
		default: {
			return "UNKNOWN";
		}
		}
	}

	@Override
	public void onCancel(Platform arg0, int arg1) {
		// TODO Auto-generated method stub
		// Message msg = new Message();
		// msg.arg1 = 3;
		// msg.arg2 = action;
		// msg.obj = palt;
		// UIHandler.sendMessage(msg, this);
	}

	@Override
	public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
		// TODO Auto-generated method stub
		// Message msg = new Message();
		// msg.arg1 = 1;
		// msg.arg2 = action;
		// msg.obj = plat;
		// UIHandler.sendMessage(msg, this);
	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		// TODO Auto-generated method stub
		// t.printStackTrace();
		// Message msg = new Message();
		// msg.arg1 = 2;
		// msg.arg2 = action;
		// UIHandler.sendMessage(msg, this);
	}

}
