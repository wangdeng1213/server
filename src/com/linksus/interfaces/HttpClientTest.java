package com.linksus.interfaces;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.linksus.common.config.LoadConfig;
import com.linksus.common.util.CacheUtil;

public class HttpClientTest {
	/** 缓存对象 */
	protected CacheUtil cache = CacheUtil.getInstance();
	// 发送post请求方法
	public static String transRequest(String url, Map mapPara,String appType) {

		// 响应内容
		String result = "";
		// 定义http客户端对象--httpClient
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setParameter(
				HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		// 定义并实例化客户端链接对象-postMethod
		PostMethod postMethod = new PostMethod(url);
		try {
			// 设置http的头
			postMethod.setRequestHeader("ContentType",
					"application/x-www-form-urlencoded;charset=UTF-8");

			NameValuePair[] data =new NameValuePair[mapPara.size()];
			int i=0;
			StringBuffer paraStr = new StringBuffer();
			paraStr.append(url);
			for (Iterator itor = mapPara.entrySet().iterator(); itor.hasNext();) {
				Entry entry = (Entry) itor.next();
				data[i]=new NameValuePair(entry.getKey()
						.toString(), entry.getValue()==null?"":entry.getValue().toString());
				paraStr.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
				i++;
			}
			if(paraStr.length()>1){
	        	paraStr.deleteCharAt(paraStr.length()-1);
	        }
			System.out.println(">>>>>>>>>>>>>>>>>>>>"+paraStr);
			// 将表单的值放入postMethod中
			postMethod.setRequestBody(data);

			// 定义访问地址的链接状态
			int statusCode = 0;
			try {
				// 客户端请求url数据
				statusCode = httpClient.executeMethod(postMethod);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 请求成功状态-200
			if (statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_BAD_REQUEST) {
				try {
					result = postMethod.getResponseBodyAsString();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("error");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 释放链接
			postMethod.releaseConnection();
			httpClient.getHttpConnectionManager().closeIdleConnections(0);
		}
		return result;
	}
	
	// 发送get请求方法
	public static String getRequest(String url,String paramesContent) {
		// 响应内容
		String result = "";
		// 定义http客户端对象--httpClient
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		// 定义并实例化客户端链接对象-postMethod
		GetMethod getMethod = new UTF8GetMethod();
		httpClient.getParams().setContentCharset("UTF-8");
		//getMethod.getParams().setContentCharset("UTF-8");
		try {
			// 设置http的头
			//getMethod.addRequestHeader("Content-Type", "text/html; charset=UTF-8");
			getMethod.setRequestHeader("ContentType",
					"application/x-www-form-urlencoded;charset=UTF-8");
			getMethod.setPath(url + paramesContent);
	        System.out.println(url + paramesContent);
			// 定义访问地址的链接状态
			int statusCode = 0;
			try {
				// 客户端请求url数据
				statusCode = httpClient.executeMethod(getMethod);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 请求成功状态-200
			if (statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_BAD_REQUEST) {
				try { 
					//result = getMethod.getResponseBodyAsString();
					InputStream in = getMethod.getResponseBodyAsStream();
					BufferedReader bufferReader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
				    String line;
				    StringBuffer stringBuffer = new StringBuffer();
					while((line=bufferReader.readLine())!=null){
						stringBuffer.append(line+"\n");
				    }
					if(stringBuffer.length()>0){
						stringBuffer.deleteCharAt(stringBuffer.length()-1);
					}
				    if(bufferReader!=null)bufferReader.close();
					System.out.println("--"+stringBuffer.toString());
					result=stringBuffer.toString();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 释放链接
			getMethod.releaseConnection();
			httpClient.getHttpConnectionManager().closeIdleConnections(0);
		}
		return result;
	}
	
	public static class UTF8GetMethod extends GetMethod{
        @Override
        public String getRequestCharSet() {
            //return super.getRequestCharSet();
            return "UTF-8";
        }
    }
	public static void main(String[] args) throws UnsupportedEncodingException{
		
		
		 String strUrl ="http://sdk2.entinfo.cn/z_send.aspx?";
		 Map paraMap =new HashMap();
		 paraMap.put("sn","SDK-WSS-010-03370" );
		 paraMap.put("pwd", "fb99f8-D");
		 paraMap.put("mobile", "13240346945");
		 paraMap.put("content", "ss123");
		// paraMap.put("userJsonData", ss);
		 //System.out.println(str);
		 String str=HttpClientTest.transRequest(strUrl, paraMap, "1");
		 System.out.println(str);
	}
}
