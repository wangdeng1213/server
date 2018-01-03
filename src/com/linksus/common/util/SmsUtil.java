package com.linksus.common.util;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ���Ź�����
 * @author dxliud
 *
 */
public abstract class SmsUtil{

	protected static final Logger logger = LoggerFactory.getLogger(SmsUtil.class);

	/**
	 * ���Ͷ���
	 * @param targetUserTel	���ͺ���
	 * @param msgContext	�������� 
	 * @throws IOException 
	 * @throws HttpException 
	 */
	public static void sendSms(String targetUserTel,String msgContent) throws HttpException,IOException{
		String corp_id = "3cc7003"; //�û���W
		String user_id = "3cc7003"; //��ҵ��
		String MD5_td_code = "b45f58443fa97b57f7d859337959e78a"; //����
		String url = "http://218.241.153.202:8888/post_sms.do"; //�ӿ�

		HttpClient client = new HttpClient(); //ʵ����Э��
		PostMethod post = new PostMethod(url); //post���ýӿ�
		//postת�� (��������)
		post.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=gbk");
		//���ŷ�������
		NameValuePair[] data = { new NameValuePair("corp_id", corp_id), new NameValuePair("user_id", user_id),
				new NameValuePair("MD5_td_code", MD5_td_code), new NameValuePair("mobile", targetUserTel),
				new NameValuePair("msg_content", msgContent)
		//new NameValuePair("msg_content", URLEncoder.encode(msgContent,"GBK"))
		};
		post.setRequestBody(data);
		client.executeMethod(post); //ִ��post
		post.releaseConnection(); //�ͷ���Դ
	}

	public static void main(String[] args) throws HttpException,IOException{
		//ֻ֧�ֵ����ֻ�����
		SmsUtil.sendSms("13240346945", "���Ų���");
	}
}
