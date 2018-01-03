package com.linksus.task;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;

import com.linksus.common.Constants;
import com.linksus.common.config.LoadConfig;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.DateUtil;
import com.linksus.common.util.HttpUtil;
import com.linksus.common.util.JsonUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.PrimaryKeyGen;
import com.linksus.common.util.StringUtil;
import com.linksus.common.util.WeiboUtil;
import com.linksus.entity.AuthToken;
import com.linksus.entity.LinksusAppaccount;
import com.linksus.entity.LinksusGovWeibo;
import com.linksus.entity.LinksusSourceAppaccount;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.entity.LinksusTaskInvalidRecord;
import com.linksus.entity.UrlEntity;
import com.linksus.service.LinksusAppaccountService;
import com.linksus.service.LinksusGovWeiboService;

/**
 * ��΢������ ֱ�ӷ���
 */
public class GovTaskSendWeibo extends BaseTask{

	protected LinksusGovWeiboService govWeiboService = (LinksusGovWeiboService) ContextUtil
			.getBean("linksusGovWeiboService");
	protected LinksusAppaccountService linksusAppaccountService = (LinksusAppaccountService) ContextUtil
			.getBean("linksusAppaccountService");

	@Override
	public void cal(){
		sendImmediate();
	}

	public void sendImmediate(){
		try{
			// ֱ��
			LinksusGovWeibo linksusWeibo = new LinksusGovWeibo();
			int startCount = (currentPage - 1) * pageSize;
			linksusWeibo.setPageSize(pageSize);
			linksusWeibo.setStartCount(startCount);
			linksusWeibo.setAccountType(1);
			List<LinksusGovWeibo> recordList = govWeiboService.getWeiboImmeSend(linksusWeibo);
			for(LinksusGovWeibo weibo : recordList){
				sendWeibo(weibo);
			}
			checkTaskListEnd(recordList);//�ж������Ƿ���ѯ���
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
	}

	// ǰ�˵��ýӿ�,��ʱ����΢��
	public String sendImmediate(Map paraMap) throws Exception{
		//		Long pid=(Long)paraMap.get("pid");
		String rsMsg = "";
		//		LinksusGovWeibo record = govWeiboService.getLinksusGovWeiboById(pid);
		LinksusGovWeibo record = saveGovWeibo(paraMap);
		if(record != null){
			if("1".equals(record.getStatus() + "")){
				rsMsg = sendWeibo(record);
			}else{
				//rsMsg = "��΢��״̬���Ǵ�����״̬,���ܷ���:currentstatus=" + record.getStatus();
				rsMsg = "20004";
			}
		}else{
			//rsMsg = "΢������ʧ��,��΢��������:pid=" + pid;
			rsMsg = "20001";
		}
		return rsMsg;
	}

	/**
	 * ���湫ʾ΢����ƽ̨���ݿ���
	 * @param paraMap
	 * @throws Exception 
	 */
	public LinksusGovWeibo saveGovWeibo(Map paraMap) throws Exception{
		Long runId = (Long) dealNull(paraMap.get("runId"), Long.class);
		Long id = PrimaryKeyGen.getPrimaryKey("linksus_gov_weibo", "id");
		Long accountId = (Long) dealNull(paraMap.get("accountId"), Long.class);
		LinksusAppaccount appAccount = linksusAppaccountService.getLinksusAppaccountById(accountId);
		Long createdTime = Long.valueOf(String.valueOf(new Date().getTime() / 1000));
		String accountName = appAccount.getAccountName();
		Integer accountType = appAccount.getType();
		String content = (String) dealNull(paraMap.get("content"), String.class);
		String picOriginalUrl = (String) dealNull(paraMap.get("picOriginalUrl"), String.class);
		String picMiddleUrl = (String) dealNull(paraMap.get("picMiddleUrl"), String.class);
		String picThumbUrl = (String) dealNull(paraMap.get("picThumbUrl"), String.class);
		String musicUrl = (String) dealNull(paraMap.get("musicUrl"), String.class);
		String videoUrl = (String) dealNull(paraMap.get("videoUrl"), String.class);
		Long authorId = (Long) dealNull(paraMap.get("authorId"), Long.class);
		String authorName = (String) dealNull(paraMap.get("authorName"), String.class);
		String contentType = (String) dealNull(paraMap.get("contentType"), String.class);
		Integer toFile = (Integer) dealNull(paraMap.get("toFile"), Integer.class);
		Long srcid = (Long) dealNull(paraMap.get("srcid"), Long.class);
		paraMap.get("MID");
		String currentUrl = (String) dealNull(paraMap.get("currentUrl"), String.class);
		LinksusGovWeibo govWeibo = new LinksusGovWeibo();
		govWeibo.setRunId(runId.intValue());
		govWeibo.setId(id);
		govWeibo.setAccountId(accountId);
		govWeibo.setCreatedTime(createdTime.intValue());
		govWeibo.setSendTime(createdTime.intValue());
		govWeibo.setAccountName(accountName);
		govWeibo.setAccountType(accountType);
		govWeibo.setContent(content);
		govWeibo.setPicOriginalUrl(picOriginalUrl);
		govWeibo.setPicMiddleUrl(picMiddleUrl);
		govWeibo.setPicThumbUrl(picThumbUrl);
		govWeibo.setMusicUrl(musicUrl);
		govWeibo.setVideoUrl(videoUrl);
		govWeibo.setContentType(contentType);
		govWeibo.setAuthorId(authorId);
		govWeibo.setAuthorName(authorName);
		govWeibo.setMid(0L);
		govWeibo.setToFile(toFile);
		govWeibo.setSrcid(srcid);
		govWeibo.setStatus(1);
		govWeibo.setCurrentUrl(currentUrl);
		govWeibo.setPublishType(0);
		govWeiboService.insertLinksusGovWeibo(govWeibo);

		return govWeibo;
	}

	private Object dealNull(Object o,Class clzz){
		if(o == null){
			if(clzz.getName().equals("java.lang.Long")){
				return new Long(0);
			}
			if(clzz.getName().equals("java.lang.Integer")){
				return new Integer(0);
			}
			if(clzz.getName().equals("java.lang.String")){
				return "";
			}
		}else{
			if(clzz.getName().equals("java.lang.Long")){
				return new Long(o.toString());
			}
			if(clzz.getName().equals("java.lang.Integer")){
				return new Integer(o.toString());
			}
			if(clzz.getName().equals("java.lang.String")){
				return o.toString();
			}
		}
		return o;

	}

	//////////////////////////////////////�����߼�////////////////////////////////////////////
	/**
	 * ����΢��
	 * @param record
	 * @throws Exception 
	 */
	public String sendWeibo(LinksusGovWeibo weibo) throws Exception{
		//ȡ���û���Ȩ
		String msg = "";
		Long accountId = weibo.getAccountId();
		LinksusAppaccount appAccount = linksusAppaccountService.getLinksusAppaccountById(accountId);
		try{
			AuthToken authToken = getAuthToken(appAccount.getInstitutionId(), accountId, weibo.getSourceId());
			if(authToken == null){
				msg = "�������˺�" + weibo.getAccountId() + "û����Ӧ��Ȩ";
				weibo.setStatus(2);
				govWeiboService.updateSendWeiboStatus(weibo);
				logger.error(">>>>>>>>>{}", msg);
				return "20006";
			}
			//�ж�����/��Ѷ��΢���Ƿ����
			String rsCode = checkWeiboExists(weibo, authToken);
			if("0".equals(rsCode)){
				//��΢��
				if(weibo.getPublishType().intValue() == 0){//ֱ��
					msg = sendWeibo(weibo, authToken);
				}
			}else if("1".equals(rsCode)){//�Ѿ�����,�޸�״̬Ϊ����ʧ��
				weibo.setStatus(2);
				govWeiboService.updateSendWeiboStatus(weibo);
				msg = "��ת��΢�������Ѿ�����,�����ظ�����!" + weibo.getId();
				logger.info(">>>>>>>>>>{}", msg);
				return "20007";
			}else{

			}
		}catch (Exception e){
			msg = LogUtil.getExceptionStackMsg(e);
			LogUtil.saveException(logger, e);
			LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
			invalidRecord.setErrorCode("EXCEPTION");
			invalidRecord.setErrorMsg(msg);
			invalidRecord.setInstitutionId(appAccount.getInstitutionId());
			invalidRecord.setOperType(Constants.INVALID_RECORD_SEND_WEIBO);
			invalidRecord.setRecordId(weibo.getId());
			weibo.setStatus(2);
			govWeiboService.updateSendWeiboStatus(weibo);
			dealInvalidRecord(invalidRecord);
			return invalidRecord.getErrorCode();
		}
		return msg;
	}

	/**
	 * ֱ��΢��
	 * @param record
	 * @param token
	 * @return
	 * @throws Exception 
	 */
	private String sendWeibo(LinksusGovWeibo weibo,AuthToken authToken) throws Exception{
		String msg = "";
		String rsData = "";
		String token = authToken.getToken();
		Map rsMap = null;
		Long accountId = weibo.getAccountId();
		LinksusAppaccount appAccount = linksusAppaccountService.getLinksusAppaccountById(accountId);
		if(weibo.getAccountType().intValue() == 1){//����
			try{
				//
				if(!StringUtils.isBlank(weibo.getPicOriginalUrl())){//��ͼƬ΢��
					rsData = sendPicWeibo(weibo, token);
				}else{
					UrlEntity strurl = LoadConfig.getUrlEntity("SendWeibo");
					String text = weibo.getContent();
					Map paraMap = new HashMap();
					paraMap.put("access_token", token);
					paraMap.put("status", text);
					rsData = HttpUtil.postRequest(strurl, paraMap);
				}
				rsMap = JsonUtil.json2map(rsData, Map.class);
			}catch (Exception e){
				LogUtil.saveException(logger, e);
				msg = LogUtil.getExceptionStackMsg(e);
			}
			//�жϷ��ؽ�� �����쳣��Ϣ
			LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(rsData);
			if(!"".equals(msg) || error != null){//����ʧ��
				LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
				if(!"".equals(msg)){
					invalidRecord.setErrorCode("EXCEPTION");
					invalidRecord.setErrorMsg(msg);
				}else{
					invalidRecord.setErrorCode(error.getErrorCode().toString());
					invalidRecord.setErrorMsg(error.getErrorMsg());
				}
				invalidRecord.setInstitutionId(appAccount.getInstitutionId());
				invalidRecord.setOperType(Constants.INVALID_RECORD_SEND_WEIBO);
				invalidRecord.setRecordId(weibo.getId());
				weibo.setStatus(2);
				govWeiboService.updateSendWeiboStatus(weibo);
				//΢������������
				dealInvalidRecord(invalidRecord);
				return invalidRecord.getErrorCode();
			}else{
				String mid = (String) rsMap.get("mid");
				//����΢��id��ȡ΢������
				String link = "http://weibo.com/" + authToken.getAppId() + "/" + WeiboUtil.Id2Mid(new Long(mid));
				//����΢����Ϣ
				LinksusGovWeibo updtWeibo = new LinksusGovWeibo();
				updtWeibo.setCurrentUrl(link);
				updtWeibo.setMid(new Long((String) rsMap.get("idstr")));
				updtWeibo.setId(weibo.getId());
				updtWeibo.setSendTime(DateUtil.getUnixDate(new Date()));
				updtWeibo.setStatus(new Integer(3));
				govWeiboService.updateSendWeiboSucc(updtWeibo);
				return msg;
			}
		}
		return msg;
	}

	/** 
	 * ������ͼ΢��
	 * */
	private String sendPicWeibo(LinksusGovWeibo record,String token) throws Exception{
		String imgUrl = record.getPicOriginalUrl();
		if(!imgUrl.startsWith("http")){
			imgUrl = Constants.IMG_URL_BASE + imgUrl;
		}
		String result = "";
		PostMethod postMethod = null;
		HttpClient httpClient = null;
		InputStream is = null;
		HttpURLConnection connection = null;
		//����ͼƬ
		try{
			URL realUrl = new URL(imgUrl);
			connection = (HttpURLConnection) realUrl.openConnection();
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.connect();
			is = connection.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int len = 0;
			byte[] b = new byte[1024];
			while ((len = is.read(b, 0, b.length)) != -1){
				baos.write(b, 0, len);
			}
			byte[] buffer = baos.toByteArray();
			FilePart fp = new FilePart("pic", new ByteArrayPartSource("pic", buffer));

			UrlEntity urlEntity = LoadConfig.getUrlEntity("SendUrlImgWeibo");
			postMethod = new PostMethod(urlEntity.getUrl());
			StringPart content = new StringPart("status", record.getContent(), "UTF-8");
			StringPart accessToken = new StringPart("access_token", token);
			Part[] parts = new Part[] { content, accessToken, fp };
			postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			MultipartRequestEntity mrp = new MultipartRequestEntity(parts, postMethod.getParams());
			postMethod.setRequestEntity(mrp);
			httpClient = new HttpClient();
			int statusCode = 0;
			statusCode = httpClient.executeMethod(postMethod);
			if(statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_BAD_REQUEST){
				result = postMethod.getResponseBodyAsString();
			}else{
				throw new Exception("���󷵻�״̬��" + statusCode);
			}
		}catch (Exception e){
			throw e;
		}finally{
			try{
				// �ͷ�����
				is.close();
				connection.disconnect();
				postMethod.releaseConnection();
				httpClient.getHttpConnectionManager().closeIdleConnections(0);
			}catch (Exception e2){
				e2.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * ȡ���û���Ȩ
	 * @param sendType
	 * @param comanyId
	 * @return
	 */
	private AuthToken getAuthToken(Long institutionId,Long accountId,Long sourceId){
		LinksusSourceAppaccount account = new LinksusSourceAppaccount();
		account.setInstitutionId(institutionId);
		account.setAccountId(accountId);
		account.setSourceId(sourceId);
		LinksusSourceAppaccount token = sourceAcctService.getTokenByAccount(account);
		AuthToken authToken = new AuthToken();
		if(token == null || token.getToken() == null){
			return null;
		}
		authToken.setAppId(token.getAppid());
		authToken.setToken(token.getToken());
		return authToken;
	}

	/**
	 * �ж�΢���Ƿ��Ѵ��� ��ֹ�ظ�����
	 * @param sendType
	 * @param comanyId
	 * @param token
	 * @param content
	 * @return
	 * @throws Exception 
	 */
	private String checkWeiboExists(LinksusGovWeibo weibo,AuthToken authToken) throws Exception{
		String content = weibo.getContent();
		Integer sendType = weibo.getAccountType();
		if(content == null){
			return "0";
		}
		String token = authToken.getToken();
		String openid = authToken.getAppId();
		if(sendType.intValue() == 1){//����
			UrlEntity strurl = LoadConfig.getUrlEntity("WeiBoData");
			Map mapPara = new HashMap();
			mapPara.put("access_token", token);
			mapPara.put("uid", openid);
			mapPara.put("trim_user", 1);
			mapPara.put("count", 10);
			String rsData = HttpUtil.getRequest(strurl, mapPara);
			LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(rsData);
			if(error != null){
				return error.getErrorCode().toString();
			}
			rsData = JsonUtil.getNodeByName(rsData, "statuses");
			List weiboList = JsonUtil.json2list(rsData, Map.class);
			if(weiboList == null || weiboList.size() == 0){
				return "0";
			}
			for(int i = 0; i < weiboList.size(); i++){
				Map map = (Map) weiboList.get(i);
				String rsContent = (String) map.get("text");
				if(content.equals(rsContent)){
					return "1";
				}
			}
		}
		return "0";
	}
}
