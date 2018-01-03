package com.linksus.common.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
public class WebSms {
	public  boolean send(String mobile,String content) {
		try{
			content+="【金马威软件】";
			int result=9999;
			URL url=new URL("http://sdk2.entinfo.cn/z_send.aspx");
			HttpURLConnection connection=(HttpURLConnection)url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.connect();
			DataOutputStream dos=new DataOutputStream(connection.getOutputStream());
			String data = "sn=SDK-WSS-010-03370&pwd=fb99f8-D&mobile=" + mobile + "&content=" + URLEncoder.encode(content, "gb2312");
			dos.writeBytes(data);
			dos.flush();
			dos.close();
			
			InputStream input=connection.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(  
					input));
			result = Integer.parseInt(reader.readLine());
			reader.close();
			input.close();
			
			connection.disconnect();
			
			if(result==1){
				return true;
			}else{
				return false;
			}
		}
		catch(Exception ex){
			//LogHelper.Error("给" + mobile + "发送短信失败\n"+LogHelper.getExceptionTrace(ex));
			return false;
		}
	}
	public static void main(String[] args)throws Exception
	{
		WebSms sms =new WebSms();
		sms.send("13240346945","wangdeng hello!");
	}
}