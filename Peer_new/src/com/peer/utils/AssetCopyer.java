package com.peer.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;

import com.peer.base.Constant;

/**
 * AssetCopyer�� ʵ�ֽ�assets�µ��ļ���Ŀ¼�ṹ������sdcard��
 * 
 * @author ticktick
 * @Email lujun.hust@gmail.com
 */
public class AssetCopyer {

	private static final String ASSET_LIST_FILENAME = "assets.lst";

	private final Context mContext;
	private final AssetManager mAssetManager;
	private File mdestDirectory;

	public AssetCopyer(Context context) {
		mContext = context;
		mAssetManager = context.getAssets();
	}

	/**
	 * ��assetsĿ¼��ָ�����ļ�������sdcard��
	 * 
	 * @return �Ƿ񿽱��ɹ�,true �ɹ���false ʧ��
	 * @throws IOException
	 */
	public boolean copyFiles(String destPath) throws IOException {

		List<String> srcFiles = new ArrayList<String>();

		// ��ȡϵͳ��SDCard��Ϊapp�����Ŀ¼��eg:/sdcard/Android/data/$(app's package)
		// ��Ŀ¼���app��صĸ����ļ�(��cache�������ļ���)��unstall app���Ŀ¼Ҳ����֮ɾ��
		// mAppDirectory = mContext.getExternalFilesDir(null);
		// if (null == mAppDirectory) {
		// return false;
		// }

		mdestDirectory = new File(destPath);

		// ��ȡassets/$(subDirectory)Ŀ¼�µ�assets.lst�ļ����õ���Ҫcopy���ļ��б�
		List<String> assets = getAssetsList();
		for (String asset : assets) {
			// ��������ڣ�����ӵ�copy�б�
			if (!new File(destPath, asset).exists()) {
				srcFiles.add(asset);
			}
		}

		// ���ο�����App�İ�װĿ¼��
		for (String file : srcFiles) {
			copy(file);
		}

		return true;
	}

	/**
	 * ��ȡ��Ҫ�������ļ��б���¼��assets/assets.lst�ļ��У�
	 * 
	 * @return �ļ��б�
	 * @throws IOException
	 */
	protected List<String> getAssetsList() throws IOException {

		List<String> files = new ArrayList<String>();

		InputStream listFile = mAssetManager.open(new File(ASSET_LIST_FILENAME)
				.getPath());
		BufferedReader br = new BufferedReader(new InputStreamReader(listFile));
		String path;
		while (null != (path = br.readLine())) {
			files.add(path);
		}

		return files;
	}

	/**
	 * ִ�п�������
	 * 
	 * @param asset
	 *            ��Ҫ������assets�ļ�·��
	 * @return �����ɹ����Ŀ���ļ����
	 * @throws IOException
	 */
	protected File copy(String asset) throws IOException {

		File asFile = new File(asset);

		String prefix = asFile.getName().substring(
				asFile.getName().lastIndexOf(".") + 1);

		InputStream source = mAssetManager.open(asFile.getPath());

		File destinationFile = null;
		if (prefix.equals("db")) {
			destinationFile = new File(mdestDirectory + "/Offlinedata/", asset);
		} else if (prefix.equals("PFX")) {
			destinationFile = new File(Constant.DEFAULT_MAIN_DIRECTORY, asset);
		} else {
			destinationFile = new File(mdestDirectory, asset);
		}

		destinationFile.getParentFile().mkdirs();
		OutputStream destination = new FileOutputStream(destinationFile);
		byte[] buffer = new byte[1024];
		int nread;

		while ((nread = source.read(buffer)) != -1) {
			if (nread == 0) {
				nread = source.read();
				if (nread < 0)
					break;
				destination.write(nread);
				continue;
			}
			destination.write(buffer, 0, nread);
		}
		destination.flush();
		source.close();
		destination.close();

		return destinationFile;
	}
}

// ע�⣬�����������ʵ��Ҫ��assetsĿ¼�±�����һ��assets.lst�ļ����г���Ҫ��������sdcard���ļ��б���������ʾ�����̵Ĵ��룬���̽ṹ��ͼ��ʾ��
// ���У�assets.lst �ļ��������£�
// map/china.txt
// map/france.txt