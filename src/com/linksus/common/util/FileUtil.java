package com.linksus.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class FileUtil{

	protected static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

	/**
	 * ���ļ�ת��Ϊ�ֽ������ַ��������������Base64���봦��
	 * 
	 * @return
	 */
	public static String getBase64StrFormFile(File imgFile){
		InputStream in = null;
		byte[] data = null;
		// ��ȡͼƬ�ֽ�����
		try{
			in = new FileInputStream(imgFile);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		}catch (IOException e){
			e.printStackTrace();
			LogUtil.saveException(logger, e);
			return null;
		}
		// ���ֽ�����Base64����
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);// ����Base64��������ֽ������ַ���
	}

	/**
	 * ���ֽ������ַ�������Base64���벢�����ļ�
	 * 
	 * @param imgStr
	 * @param filePath
	 * @return
	 */
	public static File getFileFromBase64Str(String fileStr,String filePath){
		if(StringUtils.isBlank(fileStr)){
			return null;
		}
		BASE64Decoder decoder = new BASE64Decoder();
		try{
			// Base64����
			byte[] b = decoder.decodeBuffer(fileStr);
			for(int i = 0; i < b.length; ++i){
				if(b[i] < 0){// �����쳣����
					b[i] += 256;
				}
			}
			// ����ͼƬ
			File file = new File(filePath);
			OutputStream out = new FileOutputStream(file);
			out.write(b);
			out.flush();
			out.close();
			return file;
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * ���ֽ����������ļ�
	 * 
	 * @param imgStr
	 * @param filePath
	 * @return
	 */
	public static File getFileFromByte(byte[] buff,String filePath){
		if(buff == null || buff.length == 0){
			return null;
		}
		try{
			for(int i = 0; i < buff.length; ++i){
				if(buff[i] < 0){// �����쳣����
					buff[i] += 256;
				}
			}
			// ����ͼƬ
			File file = new File(filePath);
			OutputStream out = new FileOutputStream(file);
			out.write(buff);
			out.flush();
			out.close();
			return file;
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * ��ȡ�������ļ�
	 * 
	 * @param file_name
	 * @param is
	 * @return
	 */
	public static File filePutContents(String file_name,InputStream is){
		File file = new File(file_name);
		OutputStream os = null;
		int i = 0;
		try{
			os = new FileOutputStream(file);
			byte buffer[] = new byte[1024];
			while ((i = is.read(buffer)) != -1){
				os.write(buffer, 0, i);
			}
			os.flush();
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			try{
				os.close();
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * ˵��:�����ļ��Ŀ��� �ļ������ڿ����ж�ʧ
	 * 
	 * @param filefrom
	 *            ԭ�ļ�����
	 * @param fileto
	 *            Ŀ���ļ�����
	 * @return boolean true �ɹ� false ʧ��
	 */
	public static boolean copyFile(File filefrom,File fileto){
		/** �ļ��������ĳ��� */
		int buffersize = 1024;
		FileInputStream input;
		FileOutputStream output;
		try{
			input = new FileInputStream(filefrom);
			output = new FileOutputStream(fileto);
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
		byte[] buffer = new byte[buffersize];
		int len;
		try{
			while ((len = input.read(buffer, 0, buffersize)) > 0){
				output.write(buffer, 0, len);
			}
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}finally{
			try{
				input.close();
				output.close();
			}catch (Exception e){
				e.printStackTrace();
				return false;
			}
		}
	}

	/**
	 * ˵��:�����ļ��Ŀ��� �ļ������ڿ����ж�ʧ
	 * 
	 * @param filefrom
	 *            ԭ�ļ�����
	 * @param fileto
	 *            Ŀ���ļ�����
	 * @return boolean true �ɹ� false ʧ��
	 */
	public static boolean copyFile(String filefrom,String fileto){
		/** �ļ��������ĳ��� */
		int buffersize = 1024;
		FileInputStream input;
		FileOutputStream output;
		try{
			input = new FileInputStream(new File(filefrom));
			output = new FileOutputStream(new File(fileto));
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
		byte[] buffer = new byte[buffersize];
		int len;
		try{
			while ((len = input.read(buffer, 0, buffersize)) > 0){
				output.write(buffer, 0, len);
			}
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}finally{
			try{
				input.close();
				output.close();
			}catch (Exception e){
				e.printStackTrace();
				return false;
			}
		}
	}

	/**
	 * ˵��:�����ļ��Ŀ��� �ļ������ڿ����ж�ʧ
	 * 
	 * @param from
	 *            ԭ�ļ�����
	 * @param to
	 *            Ŀ���ļ�����
	 * @return boolean true �ɹ� false ʧ��
	 */
	public static boolean copyFile(HttpServletRequest req,String to){
		/** �ļ��������ĳ��� */
		File uploadFile = new File(to);
		FileOutputStream outputStream = null;
		BufferedInputStream bufferedInputStream = null;
		BufferedOutputStream bufferedOutputStream = null;
		try{
			outputStream = new FileOutputStream(uploadFile);
			bufferedInputStream = new BufferedInputStream(req.getInputStream());
			bufferedOutputStream = new BufferedOutputStream(outputStream);
			final byte temp[] = new byte[1024];
			int readBytes = 0;
			while ((readBytes = bufferedInputStream.read(temp)) != -1){
				bufferedOutputStream.write(temp, 0, readBytes);
			}
			bufferedOutputStream.flush();
			bufferedInputStream.close();
			bufferedOutputStream.close();
		}catch (Exception e){
			System.err.println("XXX");
			e.printStackTrace();
		}finally{

		}
		return false;
	}

	/**
	 * ˵��:�����ļ��Ŀ��� �ļ������ڿ����ж�ʧ
	 * 
	 * @param from
	 *            ԭ�ļ�����
	 * @param to
	 *            Ŀ���ļ�����
	 * @return boolean true �ɹ� false ʧ��
	 */
	public static boolean copyFile(BufferedInputStream bufinput,String to){
		File uploadFile = new File(to);
		FileOutputStream outputStream = null;
		BufferedInputStream bufferedInputStream = null;
		BufferedOutputStream bufferedOutputStream = null;
		try{
			outputStream = new FileOutputStream(uploadFile);
			bufferedInputStream = bufinput;
			bufferedOutputStream = new BufferedOutputStream(outputStream);
			final byte temp[] = new byte[1024];
			int readBytes = 0;
			while ((readBytes = bufferedInputStream.read(temp)) != -1){
				bufferedOutputStream.write(temp, 0, readBytes);
			}
			bufferedOutputStream.flush();
			bufferedInputStream.close();
			bufferedOutputStream.close();
		}catch (Exception e){
			System.err.println("XXX");
			e.printStackTrace();
		}finally{

		}
		return false;
	}

	/**
	 * ɾ���ļ�
	 * 
	 * @param filename
	 *            �ļ���(Ҫ����������·��)
	 */
	public static boolean deleteDir(String filename){
		boolean isSucc = false;
		String message = "";
		try{
			File file = new File(filename);

			if(file.exists()){

				isSucc = deleteFile(file);
				if(isSucc){
					message = "ɾ���ɹ�!";
				}else{
					message = "ɾ��ʧ��!";

				}

			}else{

				message = filename + "���ļ�������!";

			}
		}catch (SecurityException e){
			LogUtil.saveException(logger, e);
		}finally{
			logger.debug(message);
		}
		return isSucc;
	}

	public static boolean deleteFile(File file){
		boolean isSucc = false;
		try{
			if(file.isDirectory()){
				File[] files = file.listFiles();
				for(int i = 0; i < files.length; i++){
					deleteFile(files[i]);
				}
				isSucc = file.delete();
			}else{
				isSucc = file.delete();
			}
		}catch (SecurityException e){
			LogUtil.saveException(logger, e);
		}
		return isSucc;
	}

	/**
	 * ˵�������ַ���д��һ���ļ���(static)
	 * 
	 * @param path
	 *            ·������
	 * @param filename
	 *            ��ȡ���ļ�ģ��
	 * @param str
	 *            д����ַ���
	 */
	public static void writeFile(String path,String filename,String str){
		try{
			File filePath = new File(path);
			if(!filePath.exists()){
				filePath.mkdirs();
			}
			File file = new File(path, filename);

			PrintWriter pw = new PrintWriter(new FileOutputStream(file));// д���ַ���ʱ��PrintWriter
			pw.println(str);
			pw.close();
		}catch (IOException e){
			LogUtil.saveException(logger, e);
		}
	}

	/**
	 * ˵������ȡ�ļ�����(static)
	 * 
	 * @param filename
	 *            ��ȡ���ļ���
	 * @return String �ļ����ַ�������
	 */
	public static String readFile(String filename){
		String return_str = "";
		//File file = null;
		try{
			// file = new File(filename);
			FileReader fr = new FileReader(new File(filename));
			LineNumberReader lr = new LineNumberReader(fr, 512);
			while (true){
				String str = lr.readLine();
				if(str == null){
					break;
				}
				return_str += str + "\n";

			}
			lr.close();
		}catch (FileNotFoundException e){
			LogUtil.saveException(logger, e);
			return_str = "File not found!";
		}catch (IOException e){
			LogUtil.saveException(logger, e);
		}
		return return_str;
	}
}
