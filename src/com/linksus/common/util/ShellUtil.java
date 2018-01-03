package com.linksus.common.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.task.BaseTask;

public class ShellUtil{

	// ����·��
	//private static final String basePath = LoadConfig.getString("tempFilePath");
	protected static final Logger logger = LoggerFactory.getLogger(BaseTask.class);

	// ��¼Shellִ��״������־�ļ���λ��(����·��)
	/*
	 * private static final String executeShellLogFile = basePath +
	 * "executeShell.log";
	 */

	/**
	 * ִ��shell,����Map:execFlag 0 �ɹ�,execInfo �����Ϣ
	 */
	public static Map executeShell(String shellCommand) throws Exception{
		Map rsMap = new HashMap();
		String success = "0";
		StringBuffer stringBuffer = new StringBuffer();
		BufferedReader bufferedReader = null;
		// ��ʽ������ʱ�䣬��¼��־ʱʹ��
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS ");
		try{
			/*
			 * stringBuffer.append(dateFormat.format(new Date())).append(
			 * "׼��ִ��Shell���� ").append(shellCommand).append(" \r\n");
			 */
			logger.info("׼��ִ��Shell����: {}", shellCommand);
			Process pid = null;
			// ִ��Shell����
			String[] cmd = { "/bin/sh", "-c", shellCommand };
			pid = Runtime.getRuntime().exec(cmd);
			if(pid != null){
				/*
				 * stringBuffer.append("���̺ţ�").append(pid.toString()).append(
				 * "\r\n");
				 */
				logger.info("���̺ţ�{}", pid.toString());
				// bufferedReader���ڶ�ȡShell��������� 
				bufferedReader = new BufferedReader(new InputStreamReader(pid.getInputStream()), 1024);
				int exitValue = pid.waitFor();
				if(0 != exitValue){//ִ�г���
					success = exitValue + "";
					LogUtil.saveException(logger, new Exception("call shell failed. error code is :" + exitValue));
				}
			}else{
				//stringBuffer.append("û��pid\r\n");
				logger.info("û��pid");
			}
			stringBuffer.append(dateFormat.format(new Date())).append("Shell����ִ�����\r\nִ�н��Ϊ��\r\n");
			String line = null;
			// ��ȡShell��������ݣ�����ӵ�stringBuffer��
			while (bufferedReader != null && (line = bufferedReader.readLine()) != null){
				stringBuffer.append(line).append("\r\n");
			}
			logger.info("--------------->shell info:{}", stringBuffer.toString());
			rsMap.put("execInfo", stringBuffer.toString());
			bufferedReader.close();
		}catch (Exception ioe){
			throw ioe;
		}finally{
			/*
			 * if (bufferedReader != null) { OutputStreamWriter
			 * outputStreamWriter = null; try { bufferedReader.close(); //
			 * ��Shell��ִ������������־�ļ��� OutputStream outputStream = new
			 * FileOutputStream( executeShellLogFile); outputStreamWriter = new
			 * OutputStreamWriter(outputStream, "UTF-8");
			 * outputStreamWriter.write(stringBuffer.toString()); } catch
			 * (Exception e) { e.printStackTrace(); } finally {
			 * outputStreamWriter.close(); } }
			 */
		}
		rsMap.put("execFlag", success);
		return rsMap;
	}

	/**
	 * URLתͼƬ
	 * @param urlStr
	 * @param tmpFilePath
	 * @return
	 * @throws Exception
	 */
	public static Map urlToImage(String urlStr,String tmpFilePath) throws Exception{
		StringBuffer buff = new StringBuffer("DISPLAY=:0 /usr/local/CutyCapt/CutyCapt --url=\"");
		buff.append(urlStr).append("\" --out=\"").append(tmpFilePath).append("\"");
		String cookie = HttpUtil.getCookie(urlStr);
		if(!StringUtils.isBlank(cookie)){
			buff.append(" --header=Cookie:\"");
			buff.append(cookie).append("\"");
		}
		return ShellUtil.executeShell(buff.toString());
	}
}
