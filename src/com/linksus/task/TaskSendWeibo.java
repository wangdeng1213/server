package com.linksus.task;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
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
import com.linksus.common.util.DateUtil;
import com.linksus.common.util.FileUtil;
import com.linksus.common.util.HttpUtil;
import com.linksus.common.util.JsonUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.QuartzUtil;
import com.linksus.common.util.ShellUtil;
import com.linksus.common.util.StringUtil;
import com.linksus.common.util.WeiboUtil;
import com.linksus.entity.AuthToken;
import com.linksus.entity.LinksusSourceAppaccount;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.entity.LinksusTaskInvalidRecord;
import com.linksus.entity.LinksusWeibo;
import com.linksus.entity.UrlEntity;

/**
 * ��΢������ ֱ�ӷ���
 */
public class TaskSendWeibo extends BaseTask{

	/**���ͷ�ʽ  ��ʱ/��ʱ */
	private String sendType;

	public void setSendType(String sendType){
		this.sendType = sendType;
	}

	@Override
	public void cal(){
		if("0".equals(sendType)){//0Ϊ����
			sendImmediate();
		}else if("1".equals(sendType)){//1Ϊ��ʱ
			sendAtRegularTime();
		}
	}

	public void sendImmediate(){
		try{
			// ֱ��
			LinksusWeibo linksusWeibo = new LinksusWeibo();
			int startCount = (currentPage - 1) * pageSize;
			linksusWeibo.setPageSize(pageSize);
			linksusWeibo.setStartCount(startCount);
			linksusWeibo.setAccountType(accountType);
			List<LinksusWeibo> recordList = weiboService.getWeiboImmeSend(linksusWeibo);
			for(LinksusWeibo weibo : recordList){
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
		Long pid = (Long) paraMap.get("pid");
		String rsMsg = "";
		LinksusWeibo record = weiboService.getLinksusWeiboById(pid);
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

	//////////////////////////////////////��ʱ����////////////////////////////////////////////

	private void sendAtRegularTime(){
		LinksusWeibo linksusWeibo = new LinksusWeibo();
		int startCount = (currentPage - 1) * pageSize;
		linksusWeibo.setPageSize(pageSize);
		linksusWeibo.setStartCount(startCount);
		linksusWeibo.setAccountType(accountType);
		try{
			List<LinksusWeibo> recordList = weiboService.getWeiboRecordPrepare(linksusWeibo);
			for(LinksusWeibo record : recordList){
				//�ж϶�ʱʱ��С�ڵ�ǰʱ��6����,�޸�Ϊ����ʧ��
				Integer sendTime = record.getSendTime();
				if(DateUtil.getUnixDate(new Date()) - sendTime > 6 * 60){
					String errorCode = "10132";
					LinksusTaskErrorCode error = cache.getErrorCode(errorCode);
					LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
					invalidRecord.setErrorCode(errorCode);
					invalidRecord.setInstitutionId(record.getInstitutionId());
					invalidRecord.setOperType(Constants.INVALID_RECORD_SEND_WEIBO);
					invalidRecord.setRecordId(record.getId());
					if("1".equals(record.getPublishSource().toString()) && record.getReferId() != null){//�ɹ�
						invalidRecord.setErrorMsg("�ɹ�����΢����ɾ��:" + error.getErrorMsg());
						weiboService.updatePurchaseWeiboFailed(record.getReferId());
						weiboService.deletePurchaseWeibo(record.getId());
					}else{
						invalidRecord.setErrorMsg(error.getErrorMsg());
						weiboService.updateSendWeiboFailed(record.getId(), errorCode);
					}
					//΢������������
					dealInvalidRecord(invalidRecord);
				}else{
					addWeiboTimer(record);
				}
			}
			checkTaskListEnd(recordList);//�ж������Ƿ���ѯ���
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
	}

	// ǰ�˵��ýӿ�,��ʱ����΢��
	public String sendAtRegularTime(String sendTime,String status,Long pid){
		String rsMsg = "";
		Integer time;
		try{
			time = new Integer(sendTime);// unixʱ��
		}catch (Exception e){
			//return "���ڲ�����ʽ����ȷ,10λUnixʱ��";
			return "20005";
		}
		LinksusWeibo record = new LinksusWeibo();
		record.setId(pid);
		record.setSendTime(time);
		record.setStatus(new Integer(status));
		addWeiboTimer(record);
		return rsMsg;
	}

	/**
	 * ΢����ʱ����
	 * 
	 * @param record
	 */
	private void addWeiboTimer(LinksusWeibo record){
		if(Constants.STATUS_DELETE.equals(record.getStatus() + "")
				|| Constants.STATUS_PAUSE.equals(record.getStatus() + "")){
			QuartzUtil.removeJob("RECORD_" + record.getId().toString(), Constants.RECORD_GROUP_NAME);
		}else{
			Map dataMap = new HashMap();
			dataMap.put("pid", record.getId());
			dataMap.put("reSendCount", new Integer(0));
			Date sendTime = new Date(Long.parseLong(record.getSendTime().toString()) * 1000);
			QuartzUtil.addSimpleJob("RECORD_" + record.getId().toString(), Constants.RECORD_GROUP_NAME, dataMap,
					sendTime, SendRecordTimer.class);
		}
	}

	//////////////////////////////////////�����߼�////////////////////////////////////////////
	/**
	 * ����΢��
	 * @param record
	 * @throws Exception 
	 */
	public String sendWeibo(LinksusWeibo weibo) throws Exception{
		//ȡ���û���Ȩ
		String msg = "";
		try{
			AuthToken authToken = getAuthToken(weibo.getInstitutionId(), weibo.getAccountId(), weibo.getSourceId());
			if(authToken == null){
				dealSendError(weibo, "20006");
			}
			//�ж�����/��Ѷ��΢���Ƿ����
			String rsCode = checkWeiboExists(weibo, authToken);
			if("0".equals(rsCode)){
				//��΢��
				if(weibo.getPublishType().intValue() == 0){//ֱ��
					msg = sendWeibo(weibo, authToken);
				}else if(weibo.getPublishType().intValue() == 1){//ת��
					msg = sendResponseWeibo(weibo, authToken);
				}

			}else if("1".equals(rsCode)){
				dealSendError(weibo, "20007");
				return "20007";
			}else{//�������
				dealSendError(weibo, "20007");
				return rsCode;
			}
		}catch (Exception e){//���쳣 
			msg = LogUtil.getExceptionStackMsg(e);
			LogUtil.saveException(logger, e);
			LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
			invalidRecord.setErrorCode(Constants.INVALID_RECORD_EXCEPTION);
			invalidRecord.setErrorMsg(msg);
			invalidRecord.setInstitutionId(weibo.getInstitutionId());
			invalidRecord.setOperType(Constants.INVALID_RECORD_SEND_WEIBO);
			invalidRecord.setRecordId(weibo.getId());
			if("1".equals(weibo.getPublishSource().toString()) && weibo.getReferId() != 0){//�ɹ�
				weiboService.updatePurchaseWeiboFailed(weibo.getReferId());
				weiboService.deletePurchaseWeibo(weibo.getId());
			}else{
				weiboService.updateSendWeiboFailed(weibo.getId(), Constants.INVALID_RECORD_EXCEPTION);
			}
			//΢������������
			dealInvalidRecord(invalidRecord);
			return "90000";
		}
		return msg;
	}

	private void dealSendError(LinksusWeibo weibo,String errorCode){
		if("1".equals(weibo.getPublishSource().toString()) && weibo.getReferId() != 0){//�ɹ�
			weiboService.updatePurchaseWeiboFailed(weibo.getReferId());
			weiboService.deletePurchaseWeibo(weibo.getId());
		}else{
			weiboService.updateSendWeiboFailed(weibo.getId(), errorCode);
		}
		LinksusTaskErrorCode error = cache.getErrorCode(errorCode);
		LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
		invalidRecord.setErrorCode(errorCode);
		invalidRecord.setErrorMsg(error.getErrorMsg());
		invalidRecord.setInstitutionId(weibo.getInstitutionId());
		invalidRecord.setOperType(Constants.INVALID_RECORD_SEND_WEIBO);
		invalidRecord.setRecordId(weibo.getId());
		dealInvalidRecord(invalidRecord);
	}

	/**
	 * ֱ��΢��
	 * @param record
	 * @param token
	 * @return
	 * @throws Exception 
	 */
	private String sendWeibo(LinksusWeibo weibo,AuthToken authToken) throws Exception{
		String msg = "";
		String rsData = "";
		Map rsMap = null;
		String token = authToken.getToken();
		String appId = authToken.getAppId();
		if(weibo.getAccountType().intValue() == 1){//����
			try{
				//
				if(!weibo.getPicOriginalUrl().equals("0")){//��ͼƬ΢��
					rsData = sendPicWeibo(weibo, authToken, Constants.APP_TYPE_SINA);
				}else{
					UrlEntity strurl = LoadConfig.getUrlEntity("SendWeibo");
					String text = weibo.getContent();
					Map paraMap = new HashMap();
					paraMap.put("access_token", token);
					paraMap.put("status", text);
					rsData = HttpUtil.postRequest(strurl, paraMap);
				}
				rsMap = (Map) JsonUtil.json2map(rsData, Map.class);
			}catch (Exception e){
				LogUtil.saveException(logger, e);
				msg = LogUtil.getExceptionStackMsg(e);
			}
			//�жϷ��ؽ�� �����쳣��Ϣ
			LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(rsData);
			if(!"".equals(msg) || error != null){//����ʧ��
				LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
				if(!"".equals(msg)){
					invalidRecord.setErrorCode(Constants.INVALID_RECORD_EXCEPTION);
					invalidRecord.setErrorMsg(msg);
				}else{
					invalidRecord.setErrorCode(error.getErrorCode().toString());
					invalidRecord.setErrorMsg(error.getErrorMsg());
				}
				invalidRecord.setInstitutionId(weibo.getInstitutionId());
				invalidRecord.setOperType(Constants.INVALID_RECORD_SEND_WEIBO);
				invalidRecord.setRecordId(weibo.getId());
				if(error != null){
					if(weibo.isRegularFlag() && isResendErrorCode(error.getSrcCode())){//Ϊ��ʱ���� �жϴ�������Ƿ�Ϊ�ط�����
						boolean resetFlag = resetRegularSend(weibo);
						if(resetFlag){
							dealInvalidRecord(invalidRecord);
							return "#���˶�ʱ���񷢲�����,�����ط�,һ���Ӻ����!";
						}
					}
				}
				if("1".equals(weibo.getPublishSource().toString()) && weibo.getReferId() != 0){//�ɹ�
					weiboService.updatePurchaseWeiboFailed(weibo.getReferId());
					weiboService.deletePurchaseWeibo(weibo.getId());
				}else{
					weiboService.updateSendWeiboFailed(weibo.getId(), invalidRecord.getErrorCode());
				}
				//΢������������
				dealInvalidRecord(invalidRecord);
				return invalidRecord.getErrorCode();
			}else{
				String mid = (String) rsMap.get("mid");
				//����΢��id��ȡ΢������
				String link = "http://weibo.com/" + authToken.getAppId() + "/" + WeiboUtil.Id2Mid(new Long(mid));
				//����΢����Ϣ
				LinksusWeibo updtWeibo = new LinksusWeibo();
				updtWeibo.setCurrentUrl(link);
				updtWeibo.setMid(new Long((String) rsMap.get("idstr")));
				updtWeibo.setId(weibo.getId());
				updtWeibo.setSendTime(DateUtil.getUnixDate(new Date()));
				updtWeibo.setStatus(new Integer(3));
				weiboService.updateSendWeiboSucc(updtWeibo);
				if("1".equals(weibo.getPublishSource() + "")){//�ɹ�
					updtWeibo.setReferId(weibo.getReferId());
					purchaseWeiboDeal(updtWeibo);
				}
				//�ж��Ƿ��ö���΢��
				/*
				 * if(weibo.getIsStick().intValue()==1){//�ö�
				 * setTopStatus(mid,token); }
				 */
				return msg;
			}
		}else if(weibo.getAccountType().intValue() == 2){//��Ѷ
			UrlEntity strurl = LoadConfig.getUrlEntity("TCSendWeibo");
			String text = weibo.getContent();
			try{
				if(!weibo.getPicOriginalUrl().equals("0")){//��ͼƬ΢��
					rsData = sendPicWeibo(weibo, authToken, Constants.APP_TYPE_TENCENT);
				}else{
					Map paraMap = new HashMap();
					paraMap.put("access_token", token);
					paraMap.put("oauth_consumer_key", cache.getAppInfo(Constants.CACHE_TENCENT_APPINFO).getAppKey());
					paraMap.put("openid", appId);
					paraMap.put("clientip", Constants.TENCENT_CLIENT_IP);
					paraMap.put("oauth_version", "2.a");
					paraMap.put("format", "json");
					paraMap.put("content", text);
					paraMap.put("compatibleflag", "32");
					rsData = HttpUtil.postRequest(strurl, paraMap);
				}
				rsMap = (Map) JsonUtil.json2map(rsData, Map.class);
			}catch (Exception e){
				LogUtil.saveException(logger, e);
				msg = LogUtil.getExceptionStackMsg(e);
			}
			//�жϷ��ؽ�� �����쳣��Ϣ
			LinksusTaskErrorCode error = StringUtil.parseTencentErrorCode(rsData);
			if(!"".equals(msg) || error != null){//����ʧ��
				LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
				if(!"".equals(msg)){
					invalidRecord.setErrorCode(Constants.INVALID_RECORD_EXCEPTION);
					invalidRecord.setErrorMsg(msg);
				}else{
					invalidRecord.setErrorCode(error.getErrorCode().toString());
					invalidRecord.setErrorMsg(error.getErrorMsg());
				}
				invalidRecord.setInstitutionId(weibo.getInstitutionId());
				invalidRecord.setOperType(Constants.INVALID_RECORD_SEND_WEIBO);
				invalidRecord.setRecordId(weibo.getId());
				dealInvalidRecord(invalidRecord);
				if("1".equals(weibo.getPublishSource().toString()) && weibo.getReferId() != 0){//�ɹ�
					weiboService.updatePurchaseWeiboFailed(weibo.getReferId());
					weiboService.deletePurchaseWeibo(weibo.getId());
				}else{
					weiboService.updateSendWeiboFailed(weibo.getId(), invalidRecord.getErrorCode());
				}
				return invalidRecord.getErrorCode();
			}else{
				Map dataMap = (Map) rsMap.get("data");
				//����΢����Ϣ
				Long sendTime = new Long((Integer) dataMap.get("time"));
				String timeStr = DateUtil.formatDate(sendTime);
				String weiboId = (String) dataMap.get("id");
				LinksusWeibo updtRecord = new LinksusWeibo();
				updtRecord.setMid(new Long(weiboId));
				updtRecord.setCurrentUrl("http://t.qq.com/p/t/" + weiboId);
				updtRecord.setId(weibo.getId());
				updtRecord.setSendTime(DateUtil.getUnixDate(new Date()));
				updtRecord.setStatus(new Integer("3"));
				weiboService.updateSendWeiboSucc(updtRecord);
				if("1".equals(weibo.getPublishSource() + "")){//�ɹ�
					updtRecord.setReferId(weibo.getReferId());
					purchaseWeiboDeal(updtRecord);
				}
			}
		}
		return msg;
	}

	/**
	 * ת��΢��
	 * @param record
	 * @param token
	 * @return
	 * @throws Exception 
	 */
	private String sendResponseWeibo(LinksusWeibo record,AuthToken authToken) throws Exception{
		String msg = "";
		String rsData = "";
		Map rsMap = null;
		String token = authToken.getToken();
		String appId = authToken.getAppId();
		if(record.getAccountType().intValue() == 1){//����
			try{
				String text = record.getContent();
				UrlEntity strurl = LoadConfig.getUrlEntity("SendResponseWeibo");
				Map paraMap = new HashMap();
				paraMap.put("id", record.getSrcid());
				paraMap.put("access_token", token);
				paraMap.put("status", text);
				paraMap.put("is_comment", "0");
				rsData = HttpUtil.postRequest(strurl, paraMap);
				//�жϷ��ؽ�� �����쳣��Ϣ
				rsMap = (Map) JsonUtil.json2map(rsData, Map.class);
			}catch (Exception e){
				LogUtil.saveException(logger, e);
				msg = LogUtil.getExceptionStackMsg(e);
			}
			LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(rsData);
			if(!"".equals(msg) || error != null){//����ʧ��
				LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
				if(!"".equals(msg)){
					invalidRecord.setErrorCode(Constants.INVALID_RECORD_EXCEPTION);
					invalidRecord.setErrorMsg(msg);
				}else{
					invalidRecord.setErrorCode(error.getErrorCode().toString());
					invalidRecord.setErrorMsg(error.getErrorMsg());
				}
				invalidRecord.setInstitutionId(record.getInstitutionId());
				invalidRecord.setOperType(Constants.INVALID_RECORD_SEND_WEIBO);
				invalidRecord.setRecordId(record.getId());
				if(error != null){
					if(record.isRegularFlag() && isResendErrorCode(error.getSrcCode())){//Ϊ��ʱ���� �жϴ�������Ƿ�Ϊ�ط�����
						boolean resetFlag = resetRegularSend(record);
						if(resetFlag){
							dealInvalidRecord(invalidRecord);
							return "#���˶�ʱ���񷢲�����,�����ط�,һ���Ӻ����!";
						}
					}
				}
				if("1".equals(record.getPublishSource().toString()) && record.getReferId() != null){//�ɹ�
					weiboService.updatePurchaseWeiboFailed(record.getReferId());
					weiboService.deletePurchaseWeibo(record.getId());
				}else{
					weiboService.updateSendWeiboFailed(record.getId(), invalidRecord.getErrorCode());
				}
				//΢������������
				invalidRecord.setSmsFlag(true);
				invalidRecord.setEmailFlag(true);
				dealInvalidRecord(invalidRecord);
				return invalidRecord.getErrorCode();
			}else{
				String responseId = (String) rsMap.get("idstr");
				//����΢��id��ȡ΢������
				String link = "http://weibo.com/" + authToken.getAppId() + "/" + WeiboUtil.Id2Mid(new Long(responseId));
				//����΢����Ϣ
				LinksusWeibo updtRecord = new LinksusWeibo();
				updtRecord.setMid(new Long(responseId));
				updtRecord.setId(record.getId());
				updtRecord.setCurrentUrl(link);
				updtRecord.setStatus(new Integer("3"));
				updtRecord.setSendTime(DateUtil.getUnixDate(new Date()));
				weiboService.updateSendWeiboSucc(updtRecord);
				if("1".equals(record.getPublishSource() + "")){//�ɹ�
					updtRecord.setReferId(record.getReferId());
					purchaseWeiboDeal(updtRecord);
				}
				//�ж��Ƿ��ö���΢��
				/*
				 * if(record.getIsStick().intValue()==1){//�ö�
				 * setTopStatus(responseId,token); }
				 */
				return msg;
			}
		}else if(record.getAccountType().intValue() == 2){//��Ѷ
			try{
				UrlEntity strurl = LoadConfig.getUrlEntity("TCResponseWeibo");
				Map paraMap = new HashMap();
				paraMap.put("format", "json");
				paraMap.put("access_token", token);
				paraMap.put("content", record.getContent());
				paraMap.put("clientip", Constants.TENCENT_CLIENT_IP);
				paraMap.put("reid", record.getSrcid());
				paraMap.put("oauth_consumer_key", cache.getAppInfo(Constants.CACHE_TENCENT_APPINFO).getAppKey());
				paraMap.put("openid", appId);
				paraMap.put("oauth_version", "2.a");
				paraMap.put("compatibleflag", "32");
				rsData = HttpUtil.postRequest(strurl, paraMap);
				rsMap = (Map) JsonUtil.json2map(rsData, Map.class);
			}catch (Exception e){
				LogUtil.saveException(logger, e);
				msg = LogUtil.getExceptionStackMsg(e);
			}
			//�жϷ��ؽ�� �����쳣��Ϣ
			LinksusTaskErrorCode error = StringUtil.parseTencentErrorCode(rsData);
			if(!"".equals(msg) || error != null){//����ʧ��
				LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
				if(!"".equals(msg)){
					invalidRecord.setErrorCode(Constants.INVALID_RECORD_EXCEPTION);
					invalidRecord.setErrorMsg(msg);
				}else{
					invalidRecord.setErrorCode(error.getErrorCode().toString());
					invalidRecord.setErrorMsg(error.getErrorMsg());
				}
				invalidRecord.setInstitutionId(record.getInstitutionId());
				invalidRecord.setOperType(Constants.INVALID_RECORD_SEND_WEIBO);
				invalidRecord.setRecordId(record.getId());
				invalidRecord.setSmsFlag(true);
				invalidRecord.setEmailFlag(true);
				dealInvalidRecord(invalidRecord);
				if("1".equals(record.getPublishSource().toString()) && record.getReferId() != null){//�ɹ�
					weiboService.updatePurchaseWeiboFailed(record.getReferId());
					weiboService.deletePurchaseWeibo(record.getId());
				}else{
					weiboService.updateSendWeiboFailed(record.getId(), invalidRecord.getErrorCode());
				}

				return invalidRecord.getErrorCode();
			}else{
				Map dataMap = (Map) rsMap.get("data");
				//����΢����Ϣ
				String weiboId = (String) dataMap.get("id");
				LinksusWeibo updtRecord = new LinksusWeibo();
				updtRecord.setMid(new Long(weiboId));
				updtRecord.setCurrentUrl("http://t.qq.com/p/t/" + weiboId);
				updtRecord.setId(record.getId());
				updtRecord.setSendTime(DateUtil.getUnixDate(new Date()));
				updtRecord.setStatus(new Integer("3"));
				weiboService.updateSendWeiboSucc(updtRecord);
				if("1".equals(record.getPublishSource() + "")){//�ɹ�
					updtRecord.setReferId(record.getReferId());
					purchaseWeiboDeal(updtRecord);
				}
				return msg;
			}
		}
		return msg;
	}

	/**
	 * �ɹ�΢������
	 * @param weibo
	 * @param succFlag
	 */
	private void purchaseWeiboDeal(LinksusWeibo weibo){
		logger.info("----------------->����΢������");
		Map paramMap = new HashMap();
		//ץȡ����
		String url = weibo.getCurrentUrl();
		String filePath = LoadConfig.getString("tempFilePath");
		String fileName = StringUtil.randomString(5) + ".jpg";
		String tmpFileName = filePath + File.separator + "weibo_" + fileName;
		String image = "";
		try{
			Map tmpMap = ShellUtil.urlToImage(url, tmpFileName);
			File tempFile = new File(tmpFileName);
			paramMap.put("original.jpg", FileUtil.getBase64StrFormFile(tempFile));
			logger.info("----------------->before send to server");
			String rsStr = HttpUtil.sendToDataServer(paramMap);
			image = JsonUtil.getNodeValueByName(JsonUtil.getNodeByName(rsStr, "data"), "original_jpg");
			logger.info("----------------->after send to server:{}", rsStr);
			//image=JsonUtil.object2json(image).replaceAll("\\\\/", "/");
			String execFlag = (String) tmpMap.get("execFlag");
			if(!"0".equals(execFlag)){
				LogUtil.saveException(logger, new Exception("΢������ץȡ����:"));
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
		//���²ɹ��ı�
		paramMap.put("exe_url", url);
		paramMap.put("exe_img", image);
		paramMap.put("detail_id", weibo.getReferId());
		weiboService.updatePurchaseWeiboSucc(paramMap);
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
	 * ������ͼ΢��
	 * */
	private String sendPicWeibo(LinksusWeibo record,AuthToken authToken,String accountType) throws Exception{
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

			Part[] parts = null;
			if(accountType.equals(Constants.APP_TYPE_SINA)){
				UrlEntity urlEntity = LoadConfig.getUrlEntity("SendUrlImgWeibo");
				postMethod = new PostMethod(urlEntity.getUrl());
				StringPart content = new StringPart("status", record.getContent(), "UTF-8");
				StringPart accessToken = new StringPart("access_token", authToken.getToken());
				parts = new Part[] { content, accessToken, fp };
			}else if(accountType.equals(Constants.APP_TYPE_TENCENT)){
				UrlEntity urlEntity = LoadConfig.getUrlEntity("TCSendImgWeibo");
				postMethod = new PostMethod(urlEntity.getUrl());
				StringPart format = new StringPart("format", "json");
				StringPart content = new StringPart("content", record.getContent(), "UTF-8");
				StringPart clientip = new StringPart("clientip", Constants.TENCENT_CLIENT_IP);
				StringPart accessToken = new StringPart("access_token", authToken.getToken());
				StringPart appKey = new StringPart("oauth_consumer_key", cache.getAppInfo(
						Constants.CACHE_TENCENT_APPINFO).getAppKey());
				StringPart openid = new StringPart("openid", (String) authToken.getAppId());
				StringPart version = new StringPart("oauth_version", "2.a");
				StringPart scope = new StringPart("scope", "all");
				StringPart compatibleflag = new StringPart("compatibleflag", "46");
				parts = new Part[] { format, content, clientip, accessToken, appKey, openid, version, scope,
						compatibleflag, fp };
			}else{
				throw new Exception("accountType��������ȷ");
			}
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
	 * �ж�΢���Ƿ��Ѵ��� ��ֹ�ظ����� ����0������,����1����,����Ϊ�������
	 * @param sendType
	 * @param comanyId
	 * @param token
	 * @param content
	 * @return
	 * @throws Exception 
	 */
	private String checkWeiboExists(LinksusWeibo weibo,AuthToken authToken) throws Exception{
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
				if(weibo.getPublishType() == 0){
					//ֱ��΢�� 
					Map retweeted = (Map) map.get("retweeted_status");
					if(retweeted == null){
						if(content.equals(rsContent)){
							return "1";
						}
					}
				}else{
					//ת��΢�� �ж�Դ΢��ID
					Map retweeted = (Map) map.get("retweeted_status");
					if(retweeted != null){
						String mid = retweeted.get("mid").toString();
						if(weibo.getSrcid().toString().equals(mid)){
							if(content.equals(rsContent)){
								return "1";
							}
						}
					}
				}
			}
		}else if(sendType.intValue() == 2){//��Ѷ
			Map paraMap = new HashMap();
			paraMap.put("access_token", token);
			paraMap.put("oauth_consumer_key", cache.getAppInfo(Constants.CACHE_TENCENT_APPINFO).getAppKey());
			paraMap.put("openid", openid);
			paraMap.put("clientip", Constants.TENCENT_CLIENT_IP);
			paraMap.put("oauth_version", "2.a");
			paraMap.put("format", "json");
			paraMap.put("pageflag", "0");
			paraMap.put("pagetime", "0");
			paraMap.put("reqnum", "10");
			paraMap.put("lastid", "0");
			paraMap.put("type", "3");
			paraMap.put("contenttype", "0");
			UrlEntity strurl = LoadConfig.getUrlEntity("TCWeiboList");//ȡ��������͵�΢��
			String rsData = HttpUtil.getRequest(strurl, paraMap);
			LinksusTaskErrorCode error = StringUtil.parseTencentErrorCode(rsData);
			if(error != null){
				return error.getErrorCode().toString();
			}
			//У��data�Ƿ�Ϊ��
			String data = JsonUtil.getNodeByName(rsData, "data");
			if(StringUtils.isBlank(data) || StringUtils.equals(data, "null")){
				return "0";
			}
			//У��info�Ƿ�Ϊ��
			String infoData = JsonUtil.getNodeByName(data, "info");
			if(StringUtils.isBlank(infoData) || StringUtils.equals(infoData, "null")){
				return "0";
			}
			List weiboList = JsonUtil.json2list(infoData, Map.class);
			if(weiboList == null || weiboList.size() == 0){
				return "0";
			}
			for(int i = 0; i < weiboList.size(); i++){
				Map map = (Map) weiboList.get(i);
				String rsContent = (String) map.get("origtext");
				if(weibo.getPublishType() == 0){//ֱ��
					if(content.equals(rsContent)){
						return "1";
					}
				}else{//ת��
					String type = map.get("type").toString();
					if(type.equals("2")){
						Map source = (Map) map.get("source");
						if(weibo.getSrcid().toString().equals(source.get("id"))){
							if(content.equals(rsContent)){
								return "1";
							}
						}
					}
				}
			}
		}
		return "0";
	}

	/** �ж��Ƿ�Ϊ���������ط���ϵͳ������� */
	private boolean isResendErrorCode(String errorCode){
		errorCode = errorCode.substring(0, errorCode.indexOf("#"));
		String reSendErrorCode = LoadConfig.getString("reSendErrorCode");
		String[] codes = reSendErrorCode.split(",");
		for(int i = 0; i < codes.length; i++){
			String code = codes[i];
			if(errorCode.equals(code)){
				return true;
			}
		}
		return false;
	}

	/**
	 * �����ط���ʱ����
	 * @param record
	 * @return
	 */
	private boolean resetRegularSend(LinksusWeibo record){
		Integer reSendCount = record.getReSendCount();
		boolean reSend = false;
		if(reSendCount == null){
			reSendCount = 1;
			reSend = true;
		}else{
			if(reSendCount < Integer.parseInt(LoadConfig.getString("reSendCount"))){
				reSend = true;
				reSendCount = reSendCount + 1;
			}
		}
		if(reSend){
			//���¶�ʱ Ϊ��ǰʹ��1����֮��
			Map dataMap = new HashMap();
			dataMap.put("pid", record.getId());
			dataMap.put("reSendCount", reSendCount);
			Calendar c = Calendar.getInstance();
			c.setTime(new Date()); //���õ�ǰ����  
			c.add(Calendar.MINUTE, 1); //���ڷ��Ӽ�1,Calendar.DATE(��),Calendar.HOUR(Сʱ)  
			Date date = c.getTime(); //���  
			QuartzUtil.addSimpleJob("RECORD_" + record.getId().toString(), Constants.RECORD_GROUP_NAME, dataMap, date,
					SendRecordTimer.class);
		}
		return reSend;
	}

	public static void main(String[] args){
		TaskSendWeibo ss = new TaskSendWeibo();
		LinksusWeibo record = new LinksusWeibo();
		record.setPicOriginalUrl("http://ww2.sinaimg.cn/large/88ca8540jw1e774c9qmdqj20fk06raas.jpg");
		try{
			ss.sendPicWeibo(record, null, "1213");
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
