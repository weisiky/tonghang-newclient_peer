package com.peer.utils;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.peer.base.Constant;

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

	@SuppressWarnings("deprecation")
	private void init(Context context, int defaultImage) {
		options = new DisplayImageOptions.Builder().cacheInMemory()
				.showImageForEmptyUri(defaultImage)
				.showImageOnFail(defaultImage).showStubImage(defaultImage)
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
				.bitmapConfig(Config.RGB_565).cacheInMemory(true)
				.cacheOnDisc(true).build();

		// 图片缓存路径
		File cacheDir = new File(Constant.C_IMAGE_CACHE_PATH);

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				.memoryCacheExtraOptions(480, 800)
				// maxwidth, max height，即保存的每个缓存文件的最大长宽
				.threadPoolSize(3)
				// 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
				// You can pass your own memory cache
				// implementation/你可以通过自己的内存缓存实现
				.memoryCacheSize(2 * 1024 * 1024)
				.discCacheSize(50 * 1024 * 1024)
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.discCacheFileCount(100)
				// 缓存的文件数量
				.discCache(new UnlimitedDiscCache(cacheDir))
				// 自定义缓存路径
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.imageDownloader(
						new BaseImageDownloader(context, 5 * 1000, 30 * 1000))
				.writeDebugLogs().build();// 开始构建
		imageLoader.getInstance().init(config);
	}

	public void showImage(Context context, String filePath,
			ImageView imageView, int defaultImage) {
		String iUrl = Scheme.FILE.wrap(filePath);
		init(context, defaultImage);
		imageLoader.displayImage(iUrl, imageView, options);
	}

	public void showAssetImage(Context context, String filePath,
			ImageView imageView, int defaultImage) {
		String iUrl = Scheme.ASSETS.wrap(filePath);
		init(context, defaultImage);
		imageLoader.displayImage(iUrl, imageView, options);
	}

	public void showHttpImage(Context context, String filePath,
			ImageView imageView, int defaultImage) {
		// String iUrl = Scheme.HTTP.wrap(filePath);
		// System.out.println("图片地址:"+iUrl);

		pLog.i("test", "filePath:" + filePath);

		init(context, defaultImage);
		imageLoader.displayImage(filePath, imageView, options);
	}

	public void showHttpsImage(Context context, String filePath,
			ImageView imageView, int defaultImage) {
		String iUrl = Scheme.HTTPS.wrap(filePath);
		init(context, defaultImage);
		imageLoader.displayImage(iUrl, imageView, options);
	}

	public void clearMemoryCache() {
		imageLoader.clearMemoryCache();
	}
}
