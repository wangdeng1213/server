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
	 * 将文件转化为字节数组字符串，并对其进行Base64编码处理
	 * 
	 * @return
	 */
	public static String getBase64StrFormFile(File imgFile){
		InputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
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
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);// 返回Base64编码过的字节数组字符串
	}

	/**
	 * 对字节数组字符串进行Base64解码并生成文件
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
			// Base64解码
			byte[] b = decoder.decodeBuffer(fileStr);
			for(int i = 0; i < b.length; ++i){
				if(b[i] < 0){// 调整异常数据
					b[i] += 256;
				}
			}
			// 生成图片
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
	 * 对字节数组生成文件
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
				if(buff[i] < 0){// 调整异常数据
					buff[i] += 256;
				}
			}
			// 生成图片
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
	 * 获取二进制文件
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
	 * 说明:单个文件的拷贝 文件属性在拷贝中丢失
	 * 
	 * @param filefrom
	 *            原文件名称
	 * @param fileto
	 *            目标文件名称
	 * @return boolean true 成功 false 失败
	 */
	public static boolean copyFile(File filefrom,File fileto){
		/** 文件缓冲区的长度 */
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
	 * 说明:单个文件的拷贝 文件属性在拷贝中丢失
	 * 
	 * @param filefrom
	 *            原文件名称
	 * @param fileto
	 *            目标文件名称
	 * @return boolean true 成功 false 失败
	 */
	public static boolean copyFile(String filefrom,String fileto){
		/** 文件缓冲区的长度 */
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
	 * 说明:单个文件的拷贝 文件属性在拷贝中丢失
	 * 
	 * @param from
	 *            原文件名称
	 * @param to
	 *            目标文件名称
	 * @return boolean true 成功 false 失败
	 */
	public static boolean copyFile(HttpServletRequest req,String to){
		/** 文件缓冲区的长度 */
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
	 * 说明:单个文件的拷贝 文件属性在拷贝中丢失
	 * 
	 * @param from
	 *            原文件名称
	 * @param to
	 *            目标文件名称
	 * @return boolean true 成功 false 失败
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
	 * 删除文件
	 * 
	 * @param filename
	 *            文件名(要包括完整的路径)
	 */
	public static boolean deleteDir(String filename){
		boolean isSucc = false;
		String message = "";
		try{
			File file = new File(filename);

			if(file.exists()){

				isSucc = deleteFile(file);
				if(isSucc){
					message = "删除成功!";
				}else{
					message = "删除失败!";

				}

			}else{

				message = filename + "此文件不存在!";

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
	 * 说明：将字符串写入一个文件中(static)
	 * 
	 * @param path
	 *            路径名称
	 * @param filename
	 *            读取的文件模版
	 * @param str
	 *            写入的字符串
	 */
	public static void writeFile(String path,String filename,String str){
		try{
			File filePath = new File(path);
			if(!filePath.exists()){
				filePath.mkdirs();
			}
			File file = new File(path, filename);

			PrintWriter pw = new PrintWriter(new FileOutputStream(file));// 写入字符串时用PrintWriter
			pw.println(str);
			pw.close();
		}catch (IOException e){
			LogUtil.saveException(logger, e);
		}
	}

	/**
	 * 说明：读取文件内容(static)
	 * 
	 * @param filename
	 *            读取的文件名
	 * @return String 文件的字符串代码
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
