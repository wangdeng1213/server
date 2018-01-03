package com.linksus.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * ZLib压缩工具 
 */
public abstract class ZLibUtil{

	protected static final Logger logger = LoggerFactory.getLogger(ZLibUtil.class);

	/** 
	 * 压缩 
	 *  
	 * @param data 
	 *            待压缩数据 
	 * @return byte[] 压缩后的数据 
	 */
	public static byte[] compress(byte[] data){
		byte[] output = new byte[0];

		Deflater compresser = new Deflater();

		compresser.reset();
		compresser.setInput(data);
		compresser.finish();
		ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
		try{
			byte[] buf = new byte[1024];
			while (!compresser.finished()){
				int i = compresser.deflate(buf);
				bos.write(buf, 0, i);
			}
			output = bos.toByteArray();
		}catch (Exception e){
			output = data;
			logger.error("compress出现异常:{}-{}", e, data);
			LogUtil.saveException(logger, e);
		}finally{
			try{
				bos.close();
			}catch (IOException e){
				e.printStackTrace();
			}
		}
		compresser.end();
		return output;
	}

	/** 
	 * 压缩 
	 *  
	 * @param data 
	 *            待压缩数据 
	 *  
	 * @param os 
	 *            输出流 
	 */
	public static void compress(byte[] data,OutputStream os){
		DeflaterOutputStream dos = new DeflaterOutputStream(os);

		try{
			dos.write(data, 0, data.length);

			dos.finish();

			dos.flush();
		}catch (IOException e){
			logger.error("compress出现异常:", e);
			LogUtil.saveException(logger, e);
		}
	}

	/** 
	 * 解压缩 
	 *  
	 * @param data 
	 *            待压缩的数据 
	 * @return byte[] 解压缩后的数据 
	 */
	public static byte[] decompress(byte[] data){
		byte[] output = new byte[0];

		Inflater decompresser = new Inflater();
		decompresser.reset();
		decompresser.setInput(data);

		ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);
		try{
			byte[] buf = new byte[1024];
			while (!decompresser.finished()){
				int i = decompresser.inflate(buf);
				o.write(buf, 0, i);
			}
			output = o.toByteArray();
		}catch (Exception e){
			output = data;
			logger.error("decompress出现异常:", e);
			LogUtil.saveException(logger, e);
		}finally{
			try{
				o.close();
			}catch (IOException e){
				e.printStackTrace();
			}
		}

		decompresser.end();
		return output;
	}

	/** 
	 * 解压缩 
	 *  
	 * @param is 
	 *            输入流 
	 * @return byte[] 解压缩后的数据 
	 */
	public static byte[] decompress(InputStream is){
		InflaterInputStream iis = new InflaterInputStream(is);
		ByteArrayOutputStream o = new ByteArrayOutputStream(1024);
		try{
			int i = 1024;
			byte[] buf = new byte[i];

			while ((i = iis.read(buf, 0, i)) > 0){
				o.write(buf, 0, i);
			}

		}catch (IOException e){
			logger.error("decompress出现异常:", e);
			LogUtil.saveException(logger, e);
		}
		return o.toByteArray();
	}

	public static Object ByteToObject(byte[] bytes){
		Object obj = null;
		try{
			ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
			ObjectInputStream oi = new ObjectInputStream(bi);
			obj = oi.readObject();
			bi.close();
			oi.close();
		}catch (Exception e){
			logger.error("ByteToObject出现异常:", e);
			LogUtil.saveException(logger, e);
		}
		return obj;
	}

	public static byte[] ObjectToByte(Object obj){
		byte[] bytes = null;
		try{
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream oo = new ObjectOutputStream(bo);
			oo.writeObject(obj);
			bytes = bo.toByteArray();
			bo.close();
			oo.close();
		}catch (Exception e){
			logger.error("ObjectToByte出现异常:{}-{}", "obj=" + obj, e);
			LogUtil.saveException(logger, e);
		}
		return bytes;
	}
}