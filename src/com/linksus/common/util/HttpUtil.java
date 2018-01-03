package com.linksus.common.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.Constants;
import com.linksus.common.config.LoadConfig;
import com.linksus.entity.UrlEntity;
import com.linksus.service.impl.MySecureProtocolSocketFactory;

public class HttpUtil{

	protected static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);
	//�������
	protected static CacheUtil cache = CacheUtil.getInstance();
	//����
	private static Map ioSessionMap = new HashMap();

	// ����post���󷽷�
	public static String postRequest(UrlEntity urlEntity,Map mapPara){
		if(urlEntity.getLimitType() == 1){//����������
			cache.checkConnLimitPerHour(Constants.LIMIT_SINA_TOTAL_PER_HOUR);
		}else if(urlEntity.getLimitType() == 2){//��Ѷ������
			cache.checkConnLimitPerHour(Constants.LIMIT_TENCENT_READ_PER_HOUR);
		}else if(urlEntity.getLimitType() == 3){//��Ѷд����
			cache.checkConnLimitPerHour(Constants.LIMIT_TENCENT_WRITE_PER_HOUR);
		}
		urlEntity.addCurrHourLinkTime();//���ӵ�ǰСʱ���ô���
		String result = "";
		try{
			result = postRequest(urlEntity.getUrl(), mapPara);
		}catch (Exception e){
			urlEntity.addCurrHourErrorTime();
			LogUtil.saveException(logger, e);
		}
		return result;
	}

	// ����get���󷽷�
	public static String getRequest(UrlEntity urlEntity,Map mapPara){
		if(urlEntity.getLimitType() == 1){//����������
			cache.checkConnLimitPerHour(Constants.LIMIT_SINA_TOTAL_PER_HOUR);
		}else if(urlEntity.getLimitType() == 2){//��Ѷ������
			cache.checkConnLimitPerHour(Constants.LIMIT_TENCENT_READ_PER_HOUR);
		}else if(urlEntity.getLimitType() == 3){//��Ѷд����
			cache.checkConnLimitPerHour(Constants.LIMIT_TENCENT_WRITE_PER_HOUR);
		}
		urlEntity.addCurrHourLinkTime();//���ӵ�ǰСʱ���ô���
		if(urlEntity.getUrlType() == 3){//΢��
			ProtocolSocketFactory fcty = new MySecureProtocolSocketFactory();
			Protocol.registerProtocol("https", new Protocol("https", fcty, 443));
		}
		// ��Ӧ����
		String result = "";
		// ����http�ͻ��˶���--httpClient
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		// ���岢ʵ�����ͻ������Ӷ���-postMethod
		GetMethod getMethod = new GetMethod();
		try{
			// ����http��ͷ
			getMethod.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=UTF-8");
			String paramesContent = "";
			for(Iterator itor = mapPara.entrySet().iterator(); itor.hasNext();){
				Entry entry = (Entry) itor.next();
				paramesContent = paramesContent + entry.getKey().toString() + "="
						+ URLEncoder.encode(entry.getValue().toString(), "UTF-8") + "&";
			}
			getMethod.setPath(urlEntity.getUrl() + paramesContent);
			httpClient.executeMethod(getMethod);
			// ����ɹ�״̬-200
			//if (statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_BAD_REQUEST) {
			//result = getMethod.getResponseBodyAsString();
			InputStream in = getMethod.getResponseBodyAsStream();
			BufferedReader bufferReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String line;
			StringBuffer stringBuffer = new StringBuffer();
			while ((line = bufferReader.readLine()) != null){
				stringBuffer.append(line + "\n");
			}
			if(stringBuffer.length() > 0){
				stringBuffer.deleteCharAt(stringBuffer.length() - 1);
			}
			if(bufferReader != null){
				bufferReader.close();
			}
			result = stringBuffer.toString();
			/*
			 * } else { logger.error("���󷵻�״̬��{}" , statusCode); }
			 */
		}catch (Exception e){
			urlEntity.addCurrHourErrorTime();
			LogUtil.saveException(logger, e);
		}finally{
			// �ͷ�����
			getMethod.releaseConnection();
			httpClient.getHttpConnectionManager().closeIdleConnections(0);
		}
		return StringUtil.utf8CharFilter(result);
	}

	// ����post���󷽷�,�����ַ���
	public static String postBodyRequest(UrlEntity urlEntity,String urlParam,String content){
		if(urlEntity.getLimitType() == 1){//����������
			cache.checkConnLimitPerHour(Constants.LIMIT_SINA_TOTAL_PER_HOUR);
		}else if(urlEntity.getLimitType() == 2){//��Ѷ������
			cache.checkConnLimitPerHour(Constants.LIMIT_TENCENT_READ_PER_HOUR);
		}else if(urlEntity.getLimitType() == 3){//��Ѷд����
			cache.checkConnLimitPerHour(Constants.LIMIT_TENCENT_WRITE_PER_HOUR);
		}
		urlEntity.addCurrHourLinkTime();//���ӵ�ǰСʱ���ô���
		// ��Ӧ����
		String result = "";
		// ����http�ͻ��˶���--httpClient
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		// ���岢ʵ�����ͻ������Ӷ���-postMethod
		PostMethod postMethod = new PostMethod(urlEntity.getUrl() + urlParam);
		try{
			// ����http��ͷ
			postMethod.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=UTF-8");

			// ���ַ������ݷ���postMethod��
			RequestEntity requestEntity = new StringRequestEntity(content);
			postMethod.setRequestEntity(requestEntity);

			httpClient.executeMethod(postMethod);
			// ����ɹ�״̬-200
			//if (statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_BAD_REQUEST) {
			//result = postMethod.getResponseBodyAsString();
			InputStream in = postMethod.getResponseBodyAsStream();
			BufferedReader bufferReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String line;
			StringBuffer stringBuffer = new StringBuffer();
			while ((line = bufferReader.readLine()) != null){
				stringBuffer.append(line + "\n");
			}
			if(stringBuffer.length() > 0){
				stringBuffer.deleteCharAt(stringBuffer.length() - 1);
			}
			if(bufferReader != null){
				bufferReader.close();
			}
			result = stringBuffer.toString();
			/*
			 * } else { logger.error("���󷵻�״̬��{}" , statusCode); }
			 */
		}catch (Exception e){
			urlEntity.addCurrHourErrorTime();
			LogUtil.saveException(logger, e);
		}finally{
			// �ͷ�����
			postMethod.releaseConnection();
			httpClient.getHttpConnectionManager().closeIdleConnections(0);
		}
		return StringUtil.utf8CharFilter(result);
	}

	// ����get���󷽷� ȡͼƬ����Ƶ����Ƶ
	public static byte[] getByteRequest(UrlEntity urlEntity,Map mapPara,String ContentType){
		byte[] buff = new byte[1024];
		if(urlEntity.getLimitType() == 1){//����������
			cache.checkConnLimitPerHour(Constants.LIMIT_SINA_TOTAL_PER_HOUR);
		}else if(urlEntity.getLimitType() == 2){//��Ѷ������
			cache.checkConnLimitPerHour(Constants.LIMIT_TENCENT_READ_PER_HOUR);
		}else if(urlEntity.getLimitType() == 3){//��Ѷд����
			cache.checkConnLimitPerHour(Constants.LIMIT_TENCENT_WRITE_PER_HOUR);
		}
		urlEntity.addCurrHourLinkTime();//���ӵ�ǰСʱ���ô���
		if(urlEntity.getUrlType() == 3){//΢��
			ProtocolSocketFactory fcty = new MySecureProtocolSocketFactory();
			Protocol.registerProtocol("https", new Protocol("https", fcty, 443));
		}
		// ����http�ͻ��˶���--httpClient
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		// ���岢ʵ�����ͻ������Ӷ���-postMethod
		GetMethod getMethod = new GetMethod();
		try{
			// ����http��ͷ
			getMethod.setRequestHeader("ContentType", ContentType);
			//		"image/jpeg");
			String paramesContent = "";
			int i = 0;
			for(Iterator itor = mapPara.entrySet().iterator(); itor.hasNext();){
				Entry entry = (Entry) itor.next();
				paramesContent = paramesContent + entry.getKey().toString() + "=" + entry.getValue().toString() + "&";
				i++;
			}
			getMethod.setPath(urlEntity.getUrl() + paramesContent);
			httpClient.executeMethod(getMethod);
			// ����ɹ�״̬-200
			//if (statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_BAD_REQUEST) {
			//buff = getMethod.getResponseBodyAsString();
			buff = getMethod.getResponseBody();
			/*
			 * } else { logger.error("���󷵻�״̬��{}" , statusCode); }
			 */
		}catch (Exception e){
			urlEntity.addCurrHourErrorTime();
			LogUtil.saveException(logger, e);
		}finally{
			// �ͷ�����
			getMethod.releaseConnection();
			httpClient.getHttpConnectionManager().closeIdleConnections(0);
		}
		return buff;
	}

	private static String postRequest(String url,Map mapPara) throws Exception{
		// ��Ӧ����
		String result = "";
		// ����http�ͻ��˶���--httpClient
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		// ���岢ʵ�����ͻ������Ӷ���-postMethod
		PostMethod postMethod = new PostMethod(url);
		try{
			// ����http��ͷ
			postMethod.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=UTF-8");
			postMethod.setRequestHeader("Accept-Language", "zh-cn");
			postMethod.setRequestHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)");
			NameValuePair[] data = new NameValuePair[mapPara.size()];
			int i = 0;
			for(Iterator itor = mapPara.entrySet().iterator(); itor.hasNext();){
				Entry entry = (Entry) itor.next();
				data[i] = new NameValuePair(entry.getKey().toString(), entry.getValue() == null ? "" : entry.getValue()
						.toString());
				i++;
			}

			// ������ֵ����postMethod��
			postMethod.setRequestBody(data);

			httpClient.executeMethod(postMethod);

			// ����ɹ�״̬-200
			//if (statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_BAD_REQUEST) {
			//result = postMethod.getResponseBodyAsString();
			InputStream in = postMethod.getResponseBodyAsStream();
			BufferedReader bufferReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String line;
			StringBuffer stringBuffer = new StringBuffer();
			while ((line = bufferReader.readLine()) != null){
				stringBuffer.append(line + "\n");
			}
			if(stringBuffer.length() > 0){
				stringBuffer.deleteCharAt(stringBuffer.length() - 1);
			}
			if(bufferReader != null){
				bufferReader.close();
			}
			result = stringBuffer.toString();
			/*
			 * } else { logger.error("���󷵻�״̬��{}" , statusCode); }
			 */
		}catch (Exception e){
			throw e;
		}finally{
			// �ͷ�����
			postMethod.releaseConnection();
			httpClient.getHttpConnectionManager().closeIdleConnections(0);
		}
		return StringUtil.utf8CharFilter(result);
	}

	public static String sendToDataServer(Map paraMap){
		//String serverUrl=LoadConfig.getString("imageServerUrl");
		UrlEntity strUrl = LoadConfig.getUrlEntity("imageServerUrl");
		String serverUrl = strUrl.getUrl();
		// ��Ӧ����
		String result = "";
		// ����http�ͻ��˶���--httpClient
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		// ���岢ʵ�����ͻ������Ӷ���-postMethod
		PostMethod postMethod = new PostMethod(serverUrl);
		try{
			// ����http��ͷ
			postMethod.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=UTF-8");

			// ������������ֵ
			NameValuePair[] data = new NameValuePair[paraMap.size()];
			int i = 0;
			for(Iterator itor = paraMap.entrySet().iterator(); itor.hasNext();){
				Entry entry = (Entry) itor.next();
				data[i] = new NameValuePair(entry.getKey().toString(), entry.getValue().toString());
				i++;
			}
			// ������ֵ����postMethod��
			postMethod.setRequestBody(data);
			httpClient.executeMethod(postMethod);
			// ����ɹ�״̬-200
			//if (statusCode == HttpStatus.SC_OK) {
			//result = postMethod.getResponseBodyAsString();
			InputStream in = postMethod.getResponseBodyAsStream();
			BufferedReader bufferReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String line;
			StringBuffer stringBuffer = new StringBuffer();
			while ((line = bufferReader.readLine()) != null){
				stringBuffer.append(line + "\n");
			}
			if(stringBuffer.length() > 0){
				stringBuffer.deleteCharAt(stringBuffer.length() - 1);
			}
			if(bufferReader != null){
				bufferReader.close();
			}
			result = stringBuffer.toString();
			/*
			 * } else { logger.error("���󷵻�״̬��{}" , statusCode); }
			 */
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}finally{
			// �ͷ�����
			postMethod.releaseConnection();
			httpClient.getHttpConnectionManager().closeIdleConnections(0);
		}
		return result;
	}

	/**
	 * ���������ȡIP��ַ
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request){
		String ip = request.getHeader(" x-forwarded-for ");
		if(ip == null || ip.length() == 0 || " unknown ".equalsIgnoreCase(ip)){
			ip = request.getHeader(" Proxy-Client-IP ");
		}
		if(ip == null || ip.length() == 0 || " unknown ".equalsIgnoreCase(ip)){
			ip = request.getHeader(" WL-Proxy-Client-IP ");
		}
		if(ip == null || ip.length() == 0 || " unknown ".equalsIgnoreCase(ip)){
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * ȡ��cookie,���˵��ж��Ƿ���Ч
	 * @return
	 * @throws Exception 
	 * @throws Exception 
	 */
	public static String getCookie(String urlStr) throws Exception{
		//�ӻ���ȡcookie
		Map cookieMap = (Map) cache.getCache(Constants.CACHE_COOKIE_MAP);
		String mainUrl = urlStr.substring(urlStr.indexOf("/") + 2);
		if(mainUrl.indexOf("/") > 0){
			mainUrl = mainUrl.substring(0, mainUrl.indexOf("/"));
		}
		String cookie = (String) cookieMap.get(mainUrl);
		//return cookie;
		if(!mainUrl.equals("weibo.com")){
			return cookie;
		}else{//��Ҫ��֤cookie
			int i = 0;//�������
			while (true){
				String checkUrl = "http://weibo.com/aj/mblog/info/big";
				String result = "";
				//HttpClient�������302����,δ���,��ʹ��curl�ж���Ч��
				//HttpClient httpClient = new HttpClient();
				//GetMethod getMethod = new GetMethod();
				try{
					logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>cookie={}", cookie);
					/*
					 * getMethod.setRequestHeader("Cookie",cookie);
					 * getMethod.setRequestHeader("Accept-Language",
					 * "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
					 * getMethod.setRequestHeader("Accept",
					 * "text/html,application/xhtml+xml,application/xml;");
					 * getMethod.setRequestHeader("Accept-Encoding",
					 * "gzip, deflate");
					 * getMethod.setRequestHeader("Connection", "keep-alive");
					 * getMethod.setRequestHeader("Host", "weibo.com");
					 * getMethod.setRequestHeader("User-Agent",
					 * "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:28.0)");
					 * getMethod.setPath(checkUrl);
					 * httpClient.executeMethod(getMethod);
					 * result=getMethod.getResponseBodyAsString();
					 */
					StringBuffer buff = new StringBuffer("curl -H \"Cookie:");
					buff.append(cookie).append("\" ").append(checkUrl);
					Map map = ShellUtil.executeShell(buff.toString());
					result = (String) map.get("execInfo");
					if(result.indexOf("\"code\":\"100000\"") > 0){//cookie��Ч
						logger.info("sina cookie��֤��Ч");
						return cookie;
					}else{
						logger.info("�����ȡsina cookie");
						if(i > 0){//ֻ��php��ȡһ��
							throw new Exception("����cookie��ȡʧ��");
						}
						//����phpȡ����cookie
						String url = LoadConfig.getString("sinaCookieUrl");
						Map paramMap = new HashMap();
						paramMap.put("code", "linksus");
						cookie = postRequest(url, paramMap);
						cookieMap.put("weibo.com", cookie);//���뻺��
						logger.info("���»�ȡsina cookie");
						i++;
					}
				}catch (Exception e){
					throw e;
				}finally{
					// �ͷ�����
					//getMethod.releaseConnection();
					//httpClient.getHttpConnectionManager().closeIdleConnections(0);
				}
			}
		}
	}

	public static IoSession getIoSession(String type){
		IoSession ioSession = (IoSession) ioSessionMap.get(type);
		if(ioSession != null && !ioSession.isClosing()){
			return ioSession;
		}
		while (true){
			NioSocketConnector ioConnectorWithSSL = null;
			try{
				String ipAddr = LoadConfig.getString("serverIPAddr");
				int port = Integer.parseInt(LoadConfig.getString("serverPort"));
				//����Զ������
				ioConnectorWithSSL = (NioSocketConnector) ContextUtil.getMinaClientBean("ioConnectorWithSSL");
				// ��������
				ConnectFuture future = ioConnectorWithSSL.connect(new InetSocketAddress(ipAddr, port));
				// �ȴ����Ӵ������
				future.awaitUninterruptibly();
				// ��ȡ���ӻỰ
				ioSession = future.getSession();
				ioSessionMap.put(type, ioSession);
			}catch (Exception e){
				logger.error(LogUtil.getExceptionStackMsg(e));//���Ӵ��󲻷��ͻط����
				if(ioConnectorWithSSL != null){
					ioConnectorWithSSL.dispose();
				}
			}
			if(ioSession != null && !ioSession.isClosing()){
				return ioSession;
			}else{
				logger.error(">>>>>>>>>>>>>>>>>>>>>>>>>taskType:{}δ��ȷ�������,1���Ӻ�����!", type);
				//�������1����
				try{
					Thread.sleep(60000);
				}catch (InterruptedException e){
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) throws Exception{
		/*
		 * String url=LoadConfig.getString("sinaCookieUrl"); Map paramMap=new
		 * HashMap(); paramMap.put("code", "linksus"); String
		 * cookie=postRequest("http://10.10.2.126/api/sinacookie", paramMap);
		 * System.out.println(".........>"+cookie);
		 */
		String checkUrl = "http://weibo.com/aj/mblog/info/big";
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod();
		String cookie = "SUE=es%3Da54e54aa1b309dea209ddffe556ef1d3%26ev%3Dv1%26es2%3De47c65b15f7388e092cf62aec9bf3642%26rs0%3Do0uYLcoOmDlWdtxWx6xV2pN1dTbHwUai6cUDnbTiD8EWtkTiVmnMoxgjb343NIzcUY7%252BIhYn3bq3t9CMYlYQKYXISZRYlb9P5eMRG7GetDIbtkKQxbVDSlPuMnVtXkQJGFVgCIAdWqjDk9Y9Z1VRTrfY9dcVXd%252Btr7mi0%252BItV0U%253D%26rv%3D0;SUP=cv%3D1%26bt%3D1396437741%26et%3D1396524141%26d%3Dc909%26i%3D8e61%26us%3D1%26vf%3D0%26vt%3D0%26ac%3D2%26st%3D0%26uid%3D3980764134%26name%3Dfishcat335%2540163.com%26nick%3Dfishcat335fishcat335fishcat335%26fmp%3D%26lcp%3D;";
		getMethod.addRequestHeader("Cookie", cookie);
		getMethod.addRequestHeader("Accept-Language", "zh-cn");
		getMethod.setPath(checkUrl);
		httpClient.executeMethod(getMethod);
		getMethod.getResponseBodyAsString();

		/*
		 * String url=LoadConfig.getString("sinaCookieUrl"); Map paramMap=new
		 * HashMap(); paramMap.put("code", "linksus"); String
		 * cookie=postRequest(url, paramMap);
		 * System.out.println(">>>>>new cookie:"+cookie);
		 * logger.info("���»�ȡsina cookie");
		 */
	}
}
