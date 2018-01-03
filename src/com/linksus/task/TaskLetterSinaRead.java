package com.linksus.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;

import sun.misc.BASE64Encoder;

import com.linksus.common.Constants;
import com.linksus.common.config.LoadConfig;
import com.linksus.common.util.CacheUtil;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.JsonUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.entity.LetterReadDTO;
import com.linksus.entity.LinksusAppaccount;
import com.linksus.service.LinksusAppaccountService;

/**
 * ��˽����������߳�
 */
public class TaskLetterSinaRead extends BaseTask{

	//΢����ַ
	public static final String host = "m.api.weibo.com";
	//΢���ӿڷ��ʶ˿�
	public static final int port = 80;
	//URL��ַ
	public static final String path = LoadConfig.getString("readLetter");
	//�������
	private CacheUtil cache = CacheUtil.getInstance();

	private LinksusAppaccountService accountService = (LinksusAppaccountService) ContextUtil
			.getBean("linksusAppaccountService");

	@Override
	public void cal(){
		LinksusAppaccount linksusAppaccount = new LinksusAppaccount();
		linksusAppaccount.setType(1);
		//�����̴߳���sina���ص�˽��
		Thread thread = new Thread(new LetterReadDealThread());
		thread.start();
		while (true){// ����һֱִ��
			try{
				List<LinksusAppaccount> accounts = accountService.getLinksusAppaccountListWithMsgId(linksusAppaccount);
				createNioHttpClient(accounts);
				setTaskListEndTime(accounts.size());
			}catch (Exception e){
				LogUtil.saveException(logger, e);
				e.printStackTrace();
			}
		}
	}

	/**
	 * ȡ��HTTPͷ
	 * "GET /2/messages/receive.json?source=1283715620&uid=1251105081&since_id=1312210001005155 HTTP/1.1\r\n"
		"Authorization: Basic bGlua3N1czE5OTlAc2luYS5jbjoxNTEwQ2xvdWQyMDEz\r\n"
		"User-Agent: Jakarta Commons-HttpClient/3.0\r\n"
		"Host: m.api.weibo.com\r\n\r\n\r\n";
	 * @param userId
	 * @param sinceId
	 * @return
	 */
	private String getHttpHeaderStr(String userId,Object sinceId){
		String authStr = cache.getAppInfo(Constants.CACHE_SINA_APPINFO).getName() + ":"
				+ cache.getAppInfo(Constants.CACHE_SINA_APPINFO).getPsssword();
		BASE64Encoder encoder = new BASE64Encoder();
		String baseAuthKey = encoder.encode(authStr.getBytes());
		StringBuffer header = new StringBuffer("GET /2/messages/receive.json?source=");
		header.append(cache.getAppInfo(Constants.CACHE_SINA_APPINFO).getAppKey()).append("&uid=").append(userId);
		if(sinceId != null && !StringUtils.isBlank(sinceId.toString())){
			header.append("&since_id=").append(sinceId);
		}
		header.append(" HTTP/1.1\r\n");
		header.append("Authorization: Basic ").append(baseAuthKey).append("\r\n");
		header.append("User-Agent: Jakarta Commons-HttpClient/3.0\r\n");
		header.append("Host: ").append(host).append("\r\n\r\n\r\n");
		return header.toString();
	}

	/**
	 * ʹ��NIO�����Socket IO����
	 * @param accountList
	 */
	public void createNioHttpClient(List accountList){
		Selector selector = null;
		ByteBuffer buffer = ByteBuffer.allocate(2048);
		SocketChannel channel[] = new SocketChannel[accountList.size()];
		try{
			selector = SelectorProvider.provider().openSelector();
			Thread thread = new Thread(new LetterReadControlThread(selector));
			thread.start();
			for(int i = 0; i < accountList.size(); i++){

				buffer.clear();
				LinksusAppaccount account = (LinksusAppaccount) accountList.get(i);
				channel[i] = createSocketChannel(host, port);
				if(account.getMsgId() != null){
					account.getMsgId();
				}
				String httpHeader = getHttpHeaderStr(account.getAppid() + "", account.getMsgId());
				logger.debug("-----------header:{}", httpHeader);
				buffer.put(httpHeader.getBytes("UTF-8"));
				buffer.flip();
				channel[i].write(buffer);
				SelectionKey sk = channel[i].register(selector, SelectionKey.OP_READ);
				sk.attach(account);
			}
		}catch (IOException e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
		while (selector != null && selector.isOpen()){
			try{
				selector.select();
			}catch (IOException e){
				LogUtil.saveException(logger, e);
				e.printStackTrace();
			}
			if(!selector.isOpen()){
				return;
			}
			Iterator itor = selector.selectedKeys().iterator();
			while (itor.hasNext()){
				SelectionKey sk = (SelectionKey) itor.next();
				LinksusAppaccount account = (LinksusAppaccount) sk.attachment();
				itor.remove();
				SocketChannel selChannel = (SocketChannel) sk.channel();
				buffer.clear();
				try{
					//�ж��Ƿ�ȫ���Ͽ�����
					if(selChannel.read(buffer) == -1){
						//�˴ο����жϵ�һ�����ӶϿ�ʱ�������Ͽ��������¿�ʼ
						//selector.close();
						//rentur;
						logger.debug("���ӶϿ���δ�ɹ����ӣ�");
						sk.cancel();
						selChannel.close();
					}
				}catch (IOException e){
					LogUtil.saveException(logger, e);
					e.printStackTrace();
					itor.remove();
					sk.cancel();
					try{
						selChannel.close();
					}catch (IOException e1){
						LogUtil.saveException(logger, e);
						e1.printStackTrace();
					}
				}
				buffer.flip();
				if(buffer.limit() == 0){
					continue;
				}
				byte bytes[] = new byte[buffer.limit()];
				buffer.get(bytes);
				try{
					parseResultMsg(account, new String(bytes, 0, bytes.length - 1, "UTF-8"));
				}catch (Exception e){
					LogUtil.saveException(logger, e);
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * ����sina���ص���Ϣ
	 * @param rs
	 */
	private void parseResultMsg(LinksusAppaccount account,String rs){
		try{
			if(rs.indexOf("{") > 0 && rs.endsWith("}")){
				logger.error(">>>>>>>>>>>>>>>���Ӵ���:{}", rs.substring(rs.indexOf("{")));
			}else{
				while (rs.indexOf("{") >= 0 && rs.endsWith("\r\n\r")){
					rs = rs.substring(rs.indexOf("{"));
					String data = rs.substring(rs.indexOf("{"), rs.indexOf("\r\n"));
					rs = rs.substring(rs.indexOf("\r\n"));
					//�����ݲ�������У��������̴߳���
					logger.debug(">>>>>>>>>>>>>>>�յ�˽��:{}", data);
					LetterReadDTO dto = (LetterReadDTO) JsonUtil.json2Bean(data, LetterReadDTO.class);
					dto.setToken(account.getToken());
					dto.setAccountId(account.getId());
					dto.setInstitutionId(account.getInstitutionId());
					LetterReadDealThread.putLetterQueue(dto);
				}
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
	}

	/**
	 * ����NIO Channel
	 * @param host
	 * @param port
	 * @return
	 * @throws IOException
	 */
	public static SocketChannel createSocketChannel(String host,int port) throws IOException{
		SocketChannel channel = SocketChannel.open(new InetSocketAddress(host, port));
		channel.configureBlocking(false);
		return channel;
	}

	/**
	 * ȡ���������� HttpClient��ʽ���� ��ʹ��
	 * @throws Exception
	 */
	private void getLetterFromSina() throws Exception{
		String url = path + "source=1283715620&uid=1251105081";
		// ����HttpClient��ʵ��
		HttpClient httpClient = new HttpClient();
		// �������ӳ�ʱΪ300��
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
		httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		// ����GET������ʵ��
		GetMethod getMethod = new GetMethod(url);
		// ����get����ʱΪ300��
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 100000);
		String authStr = cache.getAppInfo(Constants.CACHE_SINA_APPINFO).getName() + ":"
				+ cache.getAppInfo(Constants.CACHE_SINA_APPINFO).getPsssword();
		BASE64Encoder encoder = new BASE64Encoder();
		String headerKey = "Authorization";
		String headerValue = "Basic " + encoder.encode(authStr.getBytes());
		getMethod.setRequestHeader(headerKey, headerValue);
		int statusCode = 0;
		try{
			// �ͻ�������url����
			statusCode = httpClient.executeMethod(getMethod);
			Header[] headers = getMethod.getResponseHeaders();
			for(Header header : headers){
				logger.debug("{}-------{}", header.getName(), header.getValue());
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		// ����ɹ�״̬-200
		if(statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_BAD_REQUEST){
			try{
				InputStream inputStream = getMethod.getResponseBodyAsStream();
				String str;
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
				while ((str = reader.readLine()) != null){
					logger.debug(str);
				}
			}catch (IOException e){
				e.printStackTrace();
			}
		}else{
			logger.error("���󷵻�״̬��{}", statusCode);
		}
	}

	public static void main(String[] args) throws Exception{
		TaskLetterSinaRead letter = new TaskLetterSinaRead();
		Thread thread = new Thread(new LetterReadDealThread());
		thread.start();
		List accountList = new ArrayList();
		LinksusAppaccount account = new LinksusAppaccount();
		account.setId(1041L);
		account.setInstitutionId(1017L);
		account.setAppid("1251105081");
		account.setToken("2.00NaVf3BeE2s5B2f1113d612012bdT");
		//account.setMessageId(new Long("1312210001005155"));
		accountList.add(account);
		letter.createNioHttpClient(accountList);
		//letter.cal();
	}

}
