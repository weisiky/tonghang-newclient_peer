package com.peer.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.Deflater;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import com.peer.base.Constant;

/**
 * ѹ�����ѹ������
 * 
 * @author puxun
 * 
 */

public class ZipUtil {
	private ZipFile zipFile;
	private ZipOutputStream zipOut; // ѹ��Zip
	private int bufSize; // size of bytes
	private int fileSize = 0; // size of bytes
	private byte[] buf;

	public ZipUtil() {
		// Ҫ���캯����ȥ��ʼ�����ǵĻ�����
		this.bufSize = 1024 * 4;
		this.buf = new byte[this.bufSize];
	}

	private static final ZipUtil utils = new ZipUtil();

	public static ZipUtil getInstance() {
		return utils;
	}

	/**
	 * �Դ����Ŀ¼�������ļ�����ѹ��
	 * 
	 * @param srcFile
	 *            ��Ҫ��ѹ����Ŀ¼�����ļ�
	 * @param destFile
	 *            ��ѹ���ļ���·��
	 */
	public boolean doZip(String srcFile, String destFile) {// zipDirectoryPath:��Ҫѹ�����ļ�����
		File zipFile = new File(srcFile);
		try {
			// ����ZipOutputStream�����ѹ��������ȫ��ͨ������������������д��ѹ���ļ���ȥ
			File dFile=new File(Constant.SDCARD_DIR);
			if(!dFile.exists()){
				dFile.mkdirs();
			}
			
			this.zipOut = new ZipOutputStream(new BufferedOutputStream(
					new FileOutputStream(destFile)));
			// ����ѹ����ע��
			zipOut.setComment("comment");
			// ����ѹ���ı��룬���Ҫѹ����·���������ģ���������ı���
			zipOut.setEncoding("GBK");
			// ����ѹ��
			zipOut.setMethod(ZipOutputStream.DEFLATED);

			// ѹ������Ϊ��ǿѹ������ʱ��Ҫ���ö�һ��
			zipOut.setLevel(Deflater.BEST_SPEED);

			handleFile(zipFile, this.zipOut, "");

			// ������ɺ�ر����ǵ������
			this.zipOut.close();

			return true;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return false;
		}
	}

	/**
	 * ��doZip����,�ݹ����Ŀ¼�ļ���ȡ
	 * 
	 * @param zipFile
	 * @param zipOut
	 * @param dirName
	 *            �����Ҫ��������¼ѹ���ļ���һ��Ŀ¼��νṹ��
	 * @throws IOException
	 */
	private void handleFile(File zipFile, ZipOutputStream zipOut, String dirName)
			throws IOException {
		System.out.println("�����ļ���" + zipFile.getName());
		// �����һ��Ŀ¼�������
		if (zipFile.isDirectory()) {
			File[] files = zipFile.listFiles();

			if (files.length == 0) {// ���Ŀ¼Ϊ��,�򵥶�����֮.
				// ֻ�Ƿ����˿�Ŀ¼������
				this.zipOut.putNextEntry(new ZipEntry(dirName
						+ zipFile.getName() + File.separator));
				this.zipOut.closeEntry();
			} else {// ���Ŀ¼��Ϊ��,�����ݹ飬������һ���ļ�
				for (File file : files) {
					// ����ݹ飬������һ�����ļ�
					handleFile(file, zipOut, dirName + zipFile.getName()
							+ File.separator);
				}
			}
		}
		// ������ļ�����ֱ��ѹ��
		else {
			FileInputStream fileIn = new FileInputStream(zipFile);
			// ����һ��ZipEntry
			this.zipOut.putNextEntry(new ZipEntry(dirName + zipFile.getName()));
			int length = 0;
			// ����ѹ���ļ�����

			while ((length = fileIn.read(this.buf)) > 0) {
				this.zipOut.write(this.buf, 0, length);
			}

			// �ر�ZipEntry�����һ���ļ���ѹ��
			this.zipOut.closeEntry();
		}

	}

	/**
	 * ��ѹָ��zip�ļ�
	 * 
	 * @param unZipfile
	 *            ѹ���ļ���·��
	 * @param destFile
	 *            ��������ѹ����Ŀ¼��
	 */
	public boolean unZip(String unZipfile, String destFile) {// unZipfileName��Ҫ��ѹ��zip�ļ���
		FileOutputStream fileOut;
		File file;
		InputStream inputStream;

		try {
			// ����һ��zip���ļ�
			this.zipFile = new ZipFile(unZipfile, "GBK");

			// ����zipFile�����е�ʵ�壬�������ǽ�ѹ����
			for (@SuppressWarnings("unchecked")
			Enumeration<ZipEntry> entries = this.zipFile.getEntries(); entries
					.hasMoreElements();) {
				ZipEntry entry = entries.nextElement();
				String name = entry.getName();

				if (name.contains("\\")) {
					name = name.replaceAll("\\\\", "\\/");
				}

				// �������ǽ�ѹ���һ���ļ�
				file = new File(destFile + File.separator + name);

				if (entry.isDirectory()) {
					file.mkdirs();
				} else {
					// ���ָ���ļ���Ŀ¼������,�򴴽�֮.
					File parent = file.getParentFile();
					if (!parent.exists()) {
						parent.mkdirs();
					}
					// ��ȡ����ѹ��ʵ���������
					inputStream = zipFile.getInputStream(entry);

					fileOut = new FileOutputStream(file);

					int length = 0;
					// ��ʵ��д�������ļ���ȥ
					while ((length = inputStream.read(this.buf)) > 0) {
						fileOut.write(this.buf, 0, length);
					}
					fileOut.close();
					inputStream.close();
				}
			}
			this.zipFile.close();
			
			return true;
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return false;
		}
	}
}
