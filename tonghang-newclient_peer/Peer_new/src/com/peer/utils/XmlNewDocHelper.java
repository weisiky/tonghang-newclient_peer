package com.peer.utils;

import java.io.FileWriter;
import java.io.InputStream;
import java.util.List;

import com.thoughtworks.xstream.XStream;

/**
 * XML������
 * 
 * @author puxun
 * 
 */

public class XmlNewDocHelper {

	private static XStream xStream;

	// ��ȡ���xml�б����
	@SuppressWarnings("unchecked")
	public static <T> List<T> getXmlList(String aliaName, Class<T> cls,
			InputStream input) throws Exception {
		try {
			if (xStream == null) {
				xStream = new XStream();
			}
			xStream.alias(aliaName, cls);
			return (List<T>) xStream.fromXML(input);
		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception("XML ����ʧ��");
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T getXmlObject(Class<T> cls, InputStream input)
			throws Exception {
		try {
			if (xStream == null) {
				xStream = new XStream();
			}
			Object obj = xStream.fromXML(input, cls);
			return (T) obj;
		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception("XML ����ʧ��");
		}
	}

	/**
	 * Objectд��xml�ļ�
	 * 
	 * @param cls
	 *            ����
	 * @param filePath
	 *            ·��
	 * @param fileName
	 *            xml�ļ�����
	 * @param isADD
	 *            �Ƿ�׷��
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public <T> void ObjectrToXml(Object obj, String filePath,
			String fileName, boolean isADD) throws Exception {
		try {
			FileWriter writer = new FileWriter(filePath + fileName, isADD);
			if (xStream == null) {
				xStream = new XStream();
			}
			xStream.toXML(obj, writer);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception("XML д��ʧ��");
		}
	}

}