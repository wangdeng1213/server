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
 * 发微博任务 直接发送
 */
public class TaskSendWeibo extends BaseTask{

	/**发送方式  即时/定时 */
	private String sendType;

	public void setSendType(String sendType){
		this.sendType = sendType;
	}

	@Override
	public void cal(){
		if("0".equals(sendType)){//0为立即
			sendImmediate();
		}else if("1".equals(sendType)){//1为定时
			sendAtRegularTime();
		}
	}

	public void sendImmediate(){
		try{
			// 直发
			LinksusWeibo linksusWeibo = new LinksusWeibo();
			int startCount = (currentPage - 1) * pageSize;
			linksusWeibo.setPageSize(pageSize);
			linksusWeibo.setStartCount(startCount);
			linksusWeibo.setAccountType(accountType);
			List<LinksusWeibo> recordList = weiboService.getWeiboImmeSend(linksusWeibo);
			for(LinksusWeibo weibo : recordList){
				sendWeibo(weibo);
			}
			checkTaskListEnd(recordList);//判断任务是否轮询完成
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
	}

	// 前端调用接口,即时发送微博
	public String sendImmediate(Map paraMap) throws Exception{
		Long pid = (Long) paraMap.get("pid");
		String rsMsg = "";
		LinksusWeibo record = weiboService.getLinksusWeiboById(pid);
		if(record != null){
			if("1".equals(record.getStatus() + "")){
				rsMsg = sendWeibo(record);
			}else{
				//rsMsg = "该微博状态不是待发布状态,不能发布:currentstatus=" + record.getStatus();
				rsMsg = "20004";
			}
		}else{
			//rsMsg = "微博发布失败,该微博不存在:pid=" + pid;
			rsMsg = "20001";
		}
		return rsMsg;
	}

	//////////////////////////////////////定时发布////////////////////////////////////////////

	private void sendAtRegularTime(){
		LinksusWeibo linksusWeibo = new LinksusWeibo();
		int startCount = (currentPage - 1) * pageSize;
		linksusWeibo.setPageSize(pageSize);
		linksusWeibo.setStartCount(startCount);
		linksusWeibo.setAccountType(accountType);
		try{
			List<LinksusWeibo> recordList = weiboService.getWeiboRecordPrepare(linksusWeibo);
			for(LinksusWeibo record : recordList){
				//判断定时时间小于当前时间6分钟,修改为发布失败
				Integer sendTime = record.getSendTime();
				if(DateUtil.getUnixDate(new Date()) - sendTime > 6 * 60){
					String errorCode = "10132";
					LinksusTaskErrorCode error = cache.getErrorCode(errorCode);
					LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
					invalidRecord.setErrorCode(errorCode);
					invalidRecord.setInstitutionId(record.getInstitutionId());
					invalidRecord.setOperType(Constants.INVALID_RECORD_SEND_WEIBO);
					invalidRecord.setRecordId(record.getId());
					if("1".equals(record.getPublishSource().toString()) && record.getReferId() != null){//采购
						invalidRecord.setErrorMsg("采购发布微博已删除:" + error.getErrorMsg());
						weiboService.updatePurchaseWeiboFailed(record.getReferId());
						weiboService.deletePurchaseWeibo(record.getId());
					}else{
						invalidRecord.setErrorMsg(error.getErrorMsg());
						weiboService.updateSendWeiboFailed(record.getId(), errorCode);
					}
					//微博发布错误处理
					dealInvalidRecord(invalidRecord);
				}else{
					addWeiboTimer(record);
				}
			}
			checkTaskListEnd(recordList);//判断任务是否轮询完成
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
	}

	// 前端调用接口,定时发送微博
	public String sendAtRegularTime(String sendTime,String status,Long pid){
		String rsMsg = "";
		Integer time;
		try{
			time = new Integer(sendTime);// unix时间
		}catch (Exception e){
			//return "日期参数格式不正确,10位Unix时间";
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
	 * 微博定时任务
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

	//////////////////////////////////////发布逻辑////////////////////////////////////////////
	/**
	 * 发布微博
	 * @param record
	 * @throws Exception 
	 */
	public String sendWeibo(LinksusWeibo weibo) throws Exception{
		//取得用户授权
		String msg = "";
		try{
			AuthToken authToken = getAuthToken(weibo.getInstitutionId(), weibo.getAccountId(), weibo.getSourceId());
			if(authToken == null){
				dealSendError(weibo, "20006");
			}
			//判断新浪/腾讯该微博是否存在
			String rsCode = checkWeiboExists(weibo, authToken);
			if("0".equals(rsCode)){
				//发微博
				if(weibo.getPublishType().intValue() == 0){//直发
					msg = sendWeibo(weibo, authToken);
				}else if(weibo.getPublishType().intValue() == 1){//转发
					msg = sendResponseWeibo(weibo, authToken);
				}

			}else if("1".equals(rsCode)){
				dealSendError(weibo, "20007");
				return "20007";
			}else{//错误代码
				dealSendError(weibo, "20007");
				return rsCode;
			}
		}catch (Exception e){//报异常 
			msg = LogUtil.getExceptionStackMsg(e);
			LogUtil.saveException(logger, e);
			LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
			invalidRecord.setErrorCode(Constants.INVALID_RECORD_EXCEPTION);
			invalidRecord.setErrorMsg(msg);
			invalidRecord.setInstitutionId(weibo.getInstitutionId());
			invalidRecord.setOperType(Constants.INVALID_RECORD_SEND_WEIBO);
			invalidRecord.setRecordId(weibo.getId());
			if("1".equals(weibo.getPublishSource().toString()) && weibo.getReferId() != 0){//采购
				weiboService.updatePurchaseWeiboFailed(weibo.getReferId());
				weiboService.deletePurchaseWeibo(weibo.getId());
			}else{
				weiboService.updateSendWeiboFailed(weibo.getId(), Constants.INVALID_RECORD_EXCEPTION);
			}
			//微博发布错误处理
			dealInvalidRecord(invalidRecord);
			return "90000";
		}
		return msg;
	}

	private void dealSendError(LinksusWeibo weibo,String errorCode){
		if("1".equals(weibo.getPublishSource().toString()) && weibo.getReferId() != 0){//采购
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
	 * 直发微博
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
		if(weibo.getAccountType().intValue() == 1){//新浪
			try{
				//
				if(!weibo.getPicOriginalUrl().equals("0")){//带图片微博
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
			//判断返回结果 处理异常信息
			LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(rsData);
			if(!"".equals(msg) || error != null){//发布失败
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
					if(weibo.isRegularFlag() && isResendErrorCode(error.getSrcCode())){//为定时任务 判断错误代码是否为重发代码
						boolean resetFlag = resetRegularSend(weibo);
						if(resetFlag){
							dealInvalidRecord(invalidRecord);
							return "#新浪定时任务发布错误,可以重发,一分钟后继续!";
						}
					}
				}
				if("1".equals(weibo.getPublishSource().toString()) && weibo.getReferId() != 0){//采购
					weiboService.updatePurchaseWeiboFailed(weibo.getReferId());
					weiboService.deletePurchaseWeibo(weibo.getId());
				}else{
					weiboService.updateSendWeiboFailed(weibo.getId(), invalidRecord.getErrorCode());
				}
				//微博发布错误处理
				dealInvalidRecord(invalidRecord);
				return invalidRecord.getErrorCode();
			}else{
				String mid = (String) rsMap.get("mid");
				//根据微博id获取微博链接
				String link = "http://weibo.com/" + authToken.getAppId() + "/" + WeiboUtil.Id2Mid(new Long(mid));
				//更新微博信息
				LinksusWeibo updtWeibo = new LinksusWeibo();
				updtWeibo.setCurrentUrl(link);
				updtWeibo.setMid(new Long((String) rsMap.get("idstr")));
				updtWeibo.setId(weibo.getId());
				updtWeibo.setSendTime(DateUtil.getUnixDate(new Date()));
				updtWeibo.setStatus(new Integer(3));
				weiboService.updateSendWeiboSucc(updtWeibo);
				if("1".equals(weibo.getPublishSource() + "")){//采购
					updtWeibo.setReferId(weibo.getReferId());
					purchaseWeiboDeal(updtWeibo);
				}
				//判断是否置顶该微博
				/*
				 * if(weibo.getIsStick().intValue()==1){//置顶
				 * setTopStatus(mid,token); }
				 */
				return msg;
			}
		}else if(weibo.getAccountType().intValue() == 2){//腾讯
			UrlEntity strurl = LoadConfig.getUrlEntity("TCSendWeibo");
			String text = weibo.getContent();
			try{
				if(!weibo.getPicOriginalUrl().equals("0")){//带图片微博
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
			//判断返回结果 处理异常信息
			LinksusTaskErrorCode error = StringUtil.parseTencentErrorCode(rsData);
			if(!"".equals(msg) || error != null){//发布失败
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
				if("1".equals(weibo.getPublishSource().toString()) && weibo.getReferId() != 0){//采购
					weiboService.updatePurchaseWeiboFailed(weibo.getReferId());
					weiboService.deletePurchaseWeibo(weibo.getId());
				}else{
					weiboService.updateSendWeiboFailed(weibo.getId(), invalidRecord.getErrorCode());
				}
				return invalidRecord.getErrorCode();
			}else{
				Map dataMap = (Map) rsMap.get("data");
				//更新微博信息
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
				if("1".equals(weibo.getPublishSource() + "")){//采购
					updtRecord.setReferId(weibo.getReferId());
					purchaseWeiboDeal(updtRecord);
				}
			}
		}
		return msg;
	}

	/**
	 * 转发微博
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
		if(record.getAccountType().intValue() == 1){//新浪
			try{
				String text = record.getContent();
				UrlEntity strurl = LoadConfig.getUrlEntity("SendResponseWeibo");
				Map paraMap = new HashMap();
				paraMap.put("id", record.getSrcid());
				paraMap.put("access_token", token);
				paraMap.put("status", text);
				paraMap.put("is_comment", "0");
				rsData = HttpUtil.postRequest(strurl, paraMap);
				//判断返回结果 处理异常信息
				rsMap = (Map) JsonUtil.json2map(rsData, Map.class);
			}catch (Exception e){
				LogUtil.saveException(logger, e);
				msg = LogUtil.getExceptionStackMsg(e);
			}
			LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(rsData);
			if(!"".equals(msg) || error != null){//发布失败
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
					if(record.isRegularFlag() && isResendErrorCode(error.getSrcCode())){//为定时任务 判断错误代码是否为重发代码
						boolean resetFlag = resetRegularSend(record);
						if(resetFlag){
							dealInvalidRecord(invalidRecord);
							return "#新浪定时任务发布错误,可以重发,一分钟后继续!";
						}
					}
				}
				if("1".equals(record.getPublishSource().toString()) && record.getReferId() != null){//采购
					weiboService.updatePurchaseWeiboFailed(record.getReferId());
					weiboService.deletePurchaseWeibo(record.getId());
				}else{
					weiboService.updateSendWeiboFailed(record.getId(), invalidRecord.getErrorCode());
				}
				//微博发布错误处理
				invalidRecord.setSmsFlag(true);
				invalidRecord.setEmailFlag(true);
				dealInvalidRecord(invalidRecord);
				return invalidRecord.getErrorCode();
			}else{
				String responseId = (String) rsMap.get("idstr");
				//根据微博id获取微博链接
				String link = "http://weibo.com/" + authToken.getAppId() + "/" + WeiboUtil.Id2Mid(new Long(responseId));
				//更新微博信息
				LinksusWeibo updtRecord = new LinksusWeibo();
				updtRecord.setMid(new Long(responseId));
				updtRecord.setId(record.getId());
				updtRecord.setCurrentUrl(link);
				updtRecord.setStatus(new Integer("3"));
				updtRecord.setSendTime(DateUtil.getUnixDate(new Date()));
				weiboService.updateSendWeiboSucc(updtRecord);
				if("1".equals(record.getPublishSource() + "")){//采购
					updtRecord.setReferId(record.getReferId());
					purchaseWeiboDeal(updtRecord);
				}
				//判断是否置顶该微博
				/*
				 * if(record.getIsStick().intValue()==1){//置顶
				 * setTopStatus(responseId,token); }
				 */
				return msg;
			}
		}else if(record.getAccountType().intValue() == 2){//腾讯
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
			//判断返回结果 处理异常信息
			LinksusTaskErrorCode error = StringUtil.parseTencentErrorCode(rsData);
			if(!"".equals(msg) || error != null){//发布失败
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
				if("1".equals(record.getPublishSource().toString()) && record.getReferId() != null){//采购
					weiboService.updatePurchaseWeiboFailed(record.getReferId());
					weiboService.deletePurchaseWeibo(record.getId());
				}else{
					weiboService.updateSendWeiboFailed(record.getId(), invalidRecord.getErrorCode());
				}

				return invalidRecord.getErrorCode();
			}else{
				Map dataMap = (Map) rsMap.get("data");
				//更新微博信息
				String weiboId = (String) dataMap.get("id");
				LinksusWeibo updtRecord = new LinksusWeibo();
				updtRecord.setMid(new Long(weiboId));
				updtRecord.setCurrentUrl("http://t.qq.com/p/t/" + weiboId);
				updtRecord.setId(record.getId());
				updtRecord.setSendTime(DateUtil.getUnixDate(new Date()));
				updtRecord.setStatus(new Integer("3"));
				weiboService.updateSendWeiboSucc(updtRecord);
				if("1".equals(record.getPublishSource() + "")){//采购
					updtRecord.setReferId(record.getReferId());
					purchaseWeiboDeal(updtRecord);
				}
				return msg;
			}
		}
		return msg;
	}

	/**
	 * 采购微博处理
	 * @param weibo
	 * @param succFlag
	 */
	private void purchaseWeiboDeal(LinksusWeibo weibo){
		logger.info("----------------->进入微博快照");
		Map paramMap = new HashMap();
		//抓取快照
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
				LogUtil.saveException(logger, new Exception("微博快照抓取出错:"));
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
		//更新采购的表
		paramMap.put("exe_url", url);
		paramMap.put("exe_img", image);
		paramMap.put("detail_id", weibo.getReferId());
		weiboService.updatePurchaseWeiboSucc(paramMap);
	}

	/**
	 * 取得用户授权
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
	 * 发布带图微博
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
		//下载图片
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
				throw new Exception("accountType参数不正确");
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
				throw new Exception("请求返回状态：" + statusCode);
			}
		}catch (Exception e){
			throw e;
		}finally{
			try{
				// 释放链接
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
	 * 判断微博是否已存在 防止重复发送 返回0不存在,返回1存在,其他为错误代码
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
		if(sendType.intValue() == 1){//新浪
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
					//直发微博 
					Map retweeted = (Map) map.get("retweeted_status");
					if(retweeted == null){
						if(content.equals(rsContent)){
							return "1";
						}
					}
				}else{
					//转发微博 判断源微博ID
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
		}else if(sendType.intValue() == 2){//腾讯
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
			UrlEntity strurl = LoadConfig.getUrlEntity("TCWeiboList");//取出最近发送的微博
			String rsData = HttpUtil.getRequest(strurl, paraMap);
			LinksusTaskErrorCode error = StringUtil.parseTencentErrorCode(rsData);
			if(error != null){
				return error.getErrorCode().toString();
			}
			//校验data是否为空
			String data = JsonUtil.getNodeByName(rsData, "data");
			if(StringUtils.isBlank(data) || StringUtils.equals(data, "null")){
				return "0";
			}
			//校验info是否为空
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
				if(weibo.getPublishType() == 0){//直发
					if(content.equals(rsContent)){
						return "1";
					}
				}else{//转发
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

	/** 判断是否为新浪允许重发的系统错误代码 */
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
	 * 设置重发定时任务
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
			//重新定时 为当前使用1分钟之后
			Map dataMap = new HashMap();
			dataMap.put("pid", record.getId());
			dataMap.put("reSendCount", reSendCount);
			Calendar c = Calendar.getInstance();
			c.setTime(new Date()); //设置当前日期  
			c.add(Calendar.MINUTE, 1); //日期分钟加1,Calendar.DATE(天),Calendar.HOUR(小时)  
			Date date = c.getTime(); //结果  
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
