package com.linksus.ws.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.config.LoadConfig;

public class YixinHttpClient{

	protected static final Logger logger = LoggerFactory.getLogger(YixinHttpClient.class);

	private String serviceName;

	private String xmlInfo;

	public YixinHttpClient(String serviceName, String xmlInfo) {
		this.serviceName = serviceName;
		this.xmlInfo = xmlInfo;
	}

	public boolean sendMsg() throws HttpException,IOException{
		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		buffer
				.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://ws.linksus.com/\">");
		buffer.append("<soapenv:Header/>");
		buffer.append(" <soapenv:Body>");
		buffer.append(" <ws:saveService>");
		buffer.append("<serviceName>").append(serviceName).append("</serviceName>");
		buffer.append("<xmlInfo>").append(xmlInfo).append("</xmlInfo>");
		buffer.append(" </ws:saveService>");
		buffer.append(" </soapenv:Body>");
		buffer.append(" </soapenv:Envelope>");
		PostMethod postMethod = new PostMethod(LoadConfig.getString("yixinConnection.Url"));
		// 然后把Soap请求数据添加到PostMethod中
		byte[] b = buffer.toString().getBytes("utf-8");
		InputStream is = new ByteArrayInputStream(b, 0, b.length);
		RequestEntity re = new InputStreamRequestEntity(is, b.length, "text/xml; charset=utf-8");
		postMethod.setRequestEntity(re);

		// 最后生成一个HttpClient对象，并发出postMethod请求
		HttpClient httpClient = new HttpClient();
		int statusCode = httpClient.executeMethod(postMethod);
		if(statusCode == 200){
			//String soapResponseData = postMethod.getResponseBodyAsString();
			return true;
		}else{
			logger.error("调用失败！错误码：{}", statusCode);
			return false;
		}

	}

	public static void main(String[] args) throws HttpException,IOException{
		String str = "&lt;BaseInfo&gt;&lt;uid&gt;121212&lt;/uid&gt;&lt;openid&gt;1111111111111111111111&lt;/openid&gt;&lt;type&gt;text&lt;/type&gt;&lt;content&gt;通用客户端测试消息&lt;/content&gt;&lt;/BaseInfo&gt;";
		YixinHttpClient client = new YixinHttpClient("yixinService", str);
		client.sendMsg();
	}

}
