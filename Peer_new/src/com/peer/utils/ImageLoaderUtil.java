package com.peer.utils;

import android.graphics.Bitmap.Config;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.peer.activity.R;

/**
 * ClassName : ImageLoaderUtil 图片加载类，对Imageloader进行封装
 * 
 * @author zhzhg
 */
public class ImageLoaderUtil {

	private static ImageLoaderUtil instance;
	private DisplayImageOptions options;
	private ImageLoader imageLoader = ImageLoader.getInstance();

	private ImageLoaderUtil() {
		init();
	}

	public static ImageLoaderUtil getInstance() {
		if (instance == null) {
			instance = new ImageLoaderUtil();
		}
		return instance;
	}

	private void init() {
		options = new DisplayImageOptions.Builder().cacheInMemory()
				.showImageForEmptyUri(R.drawable.load_pic_faild)
				.showImageOnFail(R.drawable.load_pic_faild)
				.showStubImage(R.drawable.img_loading)
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
				.bitmapConfig(Config.RGB_565).build();
	}

	public void showImage(String filePath, ImageView imageView) {
		String iUrl = Scheme.FILE.wrap(filePath);
		imageLoader.displayImage(iUrl, imageView, options);
	}

	public void showAssetImage(String filePath, ImageView imageView) {
		String iUrl = Scheme.ASSETS.wrap(filePath);
		imageLoader.displayImage(iUrl, imageView, options);
	}

	public void clearMemoryCache() {
		imageLoader.clearMemoryCache();
	}
}
