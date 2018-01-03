package com.linksus.common.util;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 短信工具类
 * @author dxliud
 *
 */
public abstract class SmsUtil{

	protected static final Logger logger = LoggerFactory.getLogger(SmsUtil.class);

	/**
	 * 发送短信
	 * @param targetUserTel	发送号码
	 * @param msgContext	发送内容 
	 * @throws IOException 
	 * @throws HttpException 
	 */
	public static void sendSms(String targetUserTel,String msgContent) throws HttpException,IOException{
		String corp_id = "3cc7003"; //用户名W
		String user_id = "3cc7003"; //企业名
		String MD5_td_code = "b45f58443fa97b57f7d859337959e78a"; //密码
		String url = "http://218.241.153.202:8888/post_sms.do"; //接口

		HttpClient client = new HttpClient(); //实例化协议
		PostMethod post = new PostMethod(url); //post调用接口
		//post转码 (中文乱码)
		post.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=gbk");
		//短信发送设置
		NameValuePair[] data = { new NameValuePair("corp_id", corp_id), new NameValuePair("user_id", user_id),
				new NameValuePair("MD5_td_code", MD5_td_code), new NameValuePair("mobile", targetUserTel),
				new NameValuePair("msg_content", msgContent)
		//new NameValuePair("msg_content", URLEncoder.encode(msgContent,"GBK"))
		};
		post.setRequestBody(data);
		client.executeMethod(post); //执行post
		post.releaseConnection(); //释放资源
	}

	public static void main(String[] args) throws HttpException,IOException{
		//只支持电信手机号码
		SmsUtil.sendSms("13240346945", "短信测试");
	}
}
