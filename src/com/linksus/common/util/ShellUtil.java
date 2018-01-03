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

	// 基本路径
	//private static final String basePath = LoadConfig.getString("tempFilePath");
	protected static final Logger logger = LoggerFactory.getLogger(BaseTask.class);

	// 记录Shell执行状况的日志文件的位置(绝对路径)
	/*
	 * private static final String executeShellLogFile = basePath +
	 * "executeShell.log";
	 */

	/**
	 * 执行shell,返回Map:execFlag 0 成功,execInfo 输出信息
	 */
	public static Map executeShell(String shellCommand) throws Exception{
		Map rsMap = new HashMap();
		String success = "0";
		StringBuffer stringBuffer = new StringBuffer();
		BufferedReader bufferedReader = null;
		// 格式化日期时间，记录日志时使用
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS ");
		try{
			/*
			 * stringBuffer.append(dateFormat.format(new Date())).append(
			 * "准备执行Shell命令 ").append(shellCommand).append(" \r\n");
			 */
			logger.info("准备执行Shell命令: {}", shellCommand);
			Process pid = null;
			// 执行Shell命令
			String[] cmd = { "/bin/sh", "-c", shellCommand };
			pid = Runtime.getRuntime().exec(cmd);
			if(pid != null){
				/*
				 * stringBuffer.append("进程号：").append(pid.toString()).append(
				 * "\r\n");
				 */
				logger.info("进程号：{}", pid.toString());
				// bufferedReader用于读取Shell的输出内容 
				bufferedReader = new BufferedReader(new InputStreamReader(pid.getInputStream()), 1024);
				int exitValue = pid.waitFor();
				if(0 != exitValue){//执行出错
					success = exitValue + "";
					LogUtil.saveException(logger, new Exception("call shell failed. error code is :" + exitValue));
				}
			}else{
				//stringBuffer.append("没有pid\r\n");
				logger.info("没有pid");
			}
			stringBuffer.append(dateFormat.format(new Date())).append("Shell命令执行完毕\r\n执行结果为：\r\n");
			String line = null;
			// 读取Shell的输出内容，并添加到stringBuffer中
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
			 * 将Shell的执行情况输出到日志文件中 OutputStream outputStream = new
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
	 * URL转图片
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
