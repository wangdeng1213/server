package com.linksus.interfaces;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpUtil2 {
	
	public static void main(String[] args) throws Exception{
		String urlStr = "http://www.baidu.com/s";
		Map<String,String> paraMap = new HashMap<String,String>();
		paraMap.put("wd", "java");
		paraMap.put("rsv_bp", "0");
		paraMap.put("ch", "");
		paraMap.put("tn", "baidu");
		paraMap.put("bar", "");
		paraMap.put("rsv_spt", "3");
		paraMap.put("ie", "utf-8");
		paraMap.put("rsv_sug3", "2");
		paraMap.put("rsv_sug4", "215");
		paraMap.put("rsv_sug1", "2");
		paraMap.put("inputT", "896");
		System.out.println(sendPostRequest(urlStr,paraMap));
	}
	
	public static String sendPostRequest(String urlStr,Map<String,String> paraMap) throws Exception{
		HttpURLConnection connection = null;
		OutputStreamWriter writer = null;
		BufferedReader reader = null;
		try {
			URL url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");

			connection.setDoInput(true);
			connection.setDoOutput(true);
//			connection.setRequestProperty("Content-Type",
//	                "application/x-www-form-urlencoded");
			connection.connect();
			writer = new OutputStreamWriter(connection.getOutputStream(),"utf-8");
	        StringBuffer paraStr = new StringBuffer();
	        if(paraMap!=null){
	        	Set<String> keys = paraMap.keySet();
		        for(String key:keys){
		        	paraStr.append(key).append("=").append(paraMap.get(key)).append("&");
		        }
	        }
	        if(paraStr.length()>1){
	        	paraStr.deleteCharAt(paraStr.length()-1);
	        }
	        System.out.println(paraStr);
	        writer.write(paraStr.toString()); 
	        writer.flush();
	        writer.close(); // flush and close
	        
	        StringBuffer result = new StringBuffer();
	        reader = new BufferedReader(new InputStreamReader(
	                connection.getInputStream(),"utf-8"));
	        String line;
	        
	        while ((line = reader.readLine()) != null) {
	        	result.append(line);
	        }
	        return result.toString();
		} catch (Exception e) {
			throw e;
		}finally{
			if(writer!=null){
		        writer.close(); // flush and close
			}
			if(reader!=null){
				reader.close();
			}
			if (connection != null) {
				connection.disconnect();
				connection = null;
			}
		}
	}

	public static String getHttpResponse(String urlStr) throws Exception {
		HttpURLConnection connection = null;
		InputStream l_urlStream = null;
		InputStreamReader isr = null;
		BufferedReader l_reader = null;
		try {
			URL url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			connection.setDoInput(true);
			connection.setDoOutput(true);
			String sCurrentLine;
			StringBuffer sTotalString;
			sTotalString = new StringBuffer();

			l_urlStream = connection.getInputStream();

			isr = new InputStreamReader(l_urlStream, "UTF-8");
			l_reader = new BufferedReader(isr);
			while ((sCurrentLine = l_reader.readLine()) != null) {
				sTotalString.append(sCurrentLine);
			}
			return sTotalString.toString();
		} catch (Exception e) {
			throw e;
		} finally {
			if (l_reader != null) {
				l_reader.close();
				l_reader = null;
			}
			if (isr != null) {
				isr.close();
				isr = null;
			}
			if (l_urlStream != null) {
				l_urlStream.close();
				l_urlStream = null;
			}
			if (connection != null) {
				connection.disconnect();
				connection = null;
			}
		}
	}
	
}
