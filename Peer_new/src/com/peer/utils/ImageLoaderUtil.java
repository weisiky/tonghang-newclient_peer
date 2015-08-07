package com.peer.utils;

import android.graphics.Bitmap.Config;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.peer.R;

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
	}

	public static ImageLoaderUtil getInstance() {
		if (instance == null) {
			instance = new ImageLoaderUtil();
		}
		return instance;
	}

	private void init(int defaultImage) {
		options = new DisplayImageOptions.Builder().cacheInMemory()
				.showImageForEmptyUri(defaultImage)
				.showImageOnFail(defaultImage).showStubImage(defaultImage)
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
				.bitmapConfig(Config.RGB_565).build();
	}

	public void showImage(String filePath, ImageView imageView, int defaultImage) {
		String iUrl = Scheme.FILE.wrap(filePath);
		init(defaultImage);
		imageLoader.displayImage(iUrl, imageView, options);
	}

	public void showAssetImage(String filePath, ImageView imageView,
			int defaultImage) {
		String iUrl = Scheme.ASSETS.wrap(filePath);
		init(defaultImage);
		imageLoader.displayImage(iUrl, imageView, options);
	}

	public void showHttpImage(String filePath, ImageView imageView,
			int defaultImage) {
//		String iUrl = Scheme.HTTP.wrap(filePath);
		System.out.println("传图片路径："+filePath);
//		System.out.println("图片地址:"+iUrl);
		init(defaultImage);
		imageLoader.displayImage(filePath, imageView, options);
	}

	public void showHttpsImage(String filePath, ImageView imageView,
			int defaultImage) {
		String iUrl = Scheme.HTTPS.wrap(filePath);
		init(defaultImage);
		imageLoader.displayImage(iUrl, imageView, options);
	}

	public void clearMemoryCache() {
		imageLoader.clearMemoryCache();
	}
}
