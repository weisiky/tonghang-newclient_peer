package com.peer.utils;

import android.graphics.Bitmap;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

/**
 * ImageLoader工具类
 * 
 * @author puxun
 * 
 */

public class UniversalImageLoadTool {

	private static ImageLoader imageLoader = ImageLoader.getInstance();

	public static ImageLoader getImageLoader() {
		return imageLoader;
	}

	public static boolean checkImageLoader() {
		return imageLoader.isInited();
	}

	@SuppressWarnings("deprecation")
	public static void disPlay(String uri, ImageAware imageAware,
			int default_pic) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(default_pic)
				.showImageForEmptyUri(default_pic).showImageOnFail(default_pic)
				.cacheInMemory(false).cacheOnDisc(false)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new SimpleBitmapDisplayer()).build();

		imageLoader.displayImage(uri, imageAware, options);
	}

	public static void clear() {
		imageLoader.clearMemoryCache();
		imageLoader.clearDiscCache();
	}

	public static void resume() {
		imageLoader.resume();
	}

	public static void pause() {
		imageLoader.pause();
	}

	public static void stop() {
		imageLoader.stop();
	}

	public static void destroy() {
		imageLoader.destroy();
	}
}
