package com.linksus.task;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.Constants;
import com.linksus.common.config.LoadConfig;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.DateUtil;
import com.linksus.common.util.FileUtil;
import com.linksus.common.util.HttpUtil;
import com.linksus.common.util.JsonUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.PrimaryKeyGen;
import com.linksus.common.util.QuartzUtil;
import com.linksus.entity.LinksusMassArticle;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.entity.LinksusWeChatGroupInfo;
import com.linksus.entity.LinksusWx;
import com.linksus.entity.LinksusWxMassGroup;
import com.linksus.entity.LinksusWxObjectSupply;
import com.linksus.entity.UrlEntity;
import com.linksus.service.LinksusAppaccountService;
import com.linksus.service.LinksusWxMassGroupService;
import com.linksus.service.LinksusWxObjectSupplyService;
import com.linksus.service.LinksusWxService;

/**
 * 微信的群发信息
 * 
 */

public class TaskSendWeiXin extends BaseTask{

	protected static final Logger logger = LoggerFactory.getLogger(TaskSendWeiXin.class);

	LinksusWxService linksusWxService = (LinksusWxService) ContextUtil.getBean("linksusWxService");
	LinksusAppaccountService linksusAppaccountService = (LinksusAppaccountService) ContextUtil
			.getBean("linksusAppaccountService");
	LinksusWxObjectSupplyService linksusWxObjectSupplyService = (LinksusWxObjectSupplyService) ContextUtil
			.getBean("linksusWxObjectSupplyService");
	LinksusWxMassGroupService massService = (LinksusWxMassGroupService) ContextUtil
			.getBean("linksusWxMassGroupService");

	/**发送方式  即时/定时 */
	private String sendType;

	public void setSendType(String sendType){
		this.sendType = sendType;
	}

	@Override
	public void cal(){
		if("0".equals(sendType)){
			sendImmediate();
		}else if("1".equals(sendType)){
			sendAtRegularTime();
		}
	}

	public String sendMessWx(LinksusWx linksusWx) throws Exception{
		String resultInfo = "";
		String resStr = "";
		List<LinksusWeChatGroupInfo> groups = new ArrayList();//获取微信公众号组列表	
		Map parms = new HashMap();
		parms.put("access_token", linksusWx.getToken());
		String resGroupIds = HttpUtil.getRequest(LoadConfig.getUrlEntity("WeiXinGroupInfo"), parms);
		if(StringUtils.isNotBlank(resGroupIds)){
			if(JsonUtil.getNodeByName(resGroupIds, "errcode") == null){
				groups = (List<LinksusWeChatGroupInfo>) JsonUtil.json2list(JsonUtil
						.getNodeByName(resGroupIds, "groups"), LinksusWeChatGroupInfo.class);
			}else{
				String errcode = JsonUtil.getNodeValueByName(resGroupIds, "errcode");
				LinksusTaskErrorCode error = cache.getErrorCode(errcode);
				if(error != null){
					resultInfo = error.getErrorMsg();
				}
				LogUtil.saveException(logger, new Exception("未取得账户id:" + linksusWx.getAccountId() + "分组数据!"));
			}
		}
		if(groups.size() > 0){
			//上传消息id
			String mediaId = "";
			if(linksusWx.getType() == 1){//单图文
				LinksusWxObjectSupply supply = linksusWxObjectSupplyService
						.getSingleLinksusWxObjectSupplyById(new Long(linksusWx.getId()));
				if(supply != null){
					mediaId = weiXinUploadNews(linksusWx.getToken(), supply, linksusWx.getType());
				}
			}else if(linksusWx.getType() == 2){//多图文
				List<LinksusWxObjectSupply> supplys = linksusWxObjectSupplyService
						.getMoreLinksusWxObjectSupplyById(new Long(linksusWx.getId()));
				if(supplys != null && supplys.size() > 0){
					mediaId = weiXinUploadNews(linksusWx.getToken(), supplys, linksusWx.getType());
				}
			}
			int ifFirst = 0;
			for(int i = 0; i < groups.size(); i++){
				LinksusWeChatGroupInfo groupInfo = (LinksusWeChatGroupInfo) groups.get(i);
				//if(groupInfo.getCount() > 0 && (groupInfo.getId() == 101 || groupInfo.getId() == 102)){//当组成员人数大于0时
				if(groupInfo.getCount() > 0){
					ifFirst++;
					//通过id查询微信群发消息内容
					if(linksusWx.getType() == 0){//纯文本
						LinksusWxObjectSupply supply = linksusWxObjectSupplyService
								.getSingleLinksusWxObjectSupplyById(new Long(linksusWx.getId()));
						if(supply != null){
							resStr = sendMessWeiChatByGroup(linksusWx, supply, groupInfo, ifFirst);
							if(StringUtils.isNotBlank(resStr) && !resStr.equals("success")){
								break;
							}
						}else{
							LinksusTaskErrorCode error = cache.getErrorCode("10203");
							if(error != null){
								resultInfo = error.getErrorMsg();
							}
							LogUtil
									.saveException(logger,
											new Exception("微信群发任务id:" + linksusWx.getId() + ",未查询到发送内容!"));
							break;
						}
					}else if(linksusWx.getType() == 1 || linksusWx.getType() == 2){//单图文或多图文
						if(StringUtils.isNotBlank(mediaId)){
							resStr = sendMessWeiChatByGroup(linksusWx, mediaId, groupInfo, ifFirst);
							if(StringUtils.isNotBlank(resStr) && !resStr.equals("success")){
								break;
							}
						}else{
							LinksusTaskErrorCode error = cache.getErrorCode("10203");
							if(error != null){
								resultInfo = error.getErrorMsg();
							}
							LogUtil
									.saveException(logger,
											new Exception("微信群发任务id:" + linksusWx.getId() + ",未查询到发送内容!"));
							break;
						}
					}
				}
			}
		}
		if(StringUtils.isBlank(resStr) || (StringUtils.isNotBlank(resStr) && !resStr.equals("success"))
				|| StringUtils.isNotBlank(resultInfo)){//发布错误，更新错误信息
			LinksusWx entity = new LinksusWx();
			entity.setId(linksusWx.getId());
			//entity.setStatus(2);
			String errmsg = "";
			if(StringUtils.isNotBlank(resStr) && !resStr.equals("success")){
				LinksusTaskErrorCode error = cache.getErrorCode(JsonUtil.getNodeValueByName(errmsg, "errcode"));
				if(error != null){
					errmsg = error.getErrorMsg();
				}
			}
			entity.setErrmsg(errmsg);
			linksusWxService.updateWeiXinTaskErrmsg(entity);
		}
		return resultInfo;
	}

	public Object sendMassInfo(Map paramsMap) throws Exception{
		Map resultMap = new HashMap();
		String resultInfo = "";
		String sucessId = "";
		String failId = "";
		String resStr = "";
		String ids = (String) paramsMap.get("ids");
		String[] args = ids.split(",");
		for(String id : args){//群发微信的发布信息id
			//查询需要群发微信的账号信息及群发微信内容
			LinksusWx linksusWx = linksusWxService.getLinksusWXUserAndInfo(new Long(id));
			if(linksusWx != null){
				if(linksusWx.getPublishStatus() == 0){//即时发布		
					String error = sendMessWx(linksusWx);
					if(StringUtils.isNotBlank(error) && error.equals("success")){
						if(StringUtils.isBlank(sucessId)){
							sucessId = id;
						}else{
							sucessId = sucessId + "," + id;
						}
					}else{
						if(StringUtils.isBlank(failId)){
							failId = id;
						}else{
							failId = failId + "," + id;
						}
					}
				}else if(linksusWx.getPublishStatus() == 1){//定时发布
					addMassWeChatTimer(linksusWx);
				}else{
					LinksusTaskErrorCode error = cache.getErrorCode("20032");
					if(error != null){
						resultInfo = error.getErrorMsg();
					}
					LogUtil.saveException(logger, new Exception("微信群发任务id:" + linksusWx.getId() + "," + resultInfo));
				}

			}else{
				if(StringUtils.isBlank(failId)){
					failId = id;
				}else{
					failId = failId + "," + id;
				}
				//resultInfo = "微信群发任务id:" + id + ",没有相关账户信息!";
				LinksusTaskErrorCode error = cache.getErrorCode("20042");
				if(error != null){
					resultInfo = error.getErrorMsg();
				}
				LinksusWx entity = new LinksusWx();
				entity.setId(new Long(id));
				entity.setStatus(2);
				entity.setErrmsg(resultInfo);
				linksusWxService.updateWeiXinTaskstatusAndErrmsg(entity);
				LogUtil.saveException(logger, new Exception("微信群发任务id:" + id + ",没有相关账户信息!"));
				return "20042";
			}
		}
		return resultMap;
	}

	public void sendImmediate(){
		try{
			// 立即
			LinksusWx weixinTask = new LinksusWx();
			int startCount = (currentPage - 1) * pageSize;
			weixinTask.setPageSize(pageSize);
			weixinTask.setStartCount(startCount);
			weixinTask.setPublishStatus(0);//即时发布
			weixinTask.setStatus(1);//预发布
			//获取需要发微信的列表   
			List<LinksusWx> tasks = linksusWxService.getWeiXinTaskList(weixinTask);
			if(tasks != null && tasks.size() > 0){
				for(LinksusWx weixin : tasks){
					sendMessWx(weixin);
				}
			}
			checkTaskListEnd(tasks);//判断任务是否轮询完成
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
	}

	//////////////////////////////////////定时发布////////////////////////////////////////////

	private void sendAtRegularTime(){
		LinksusWx weixinTask = new LinksusWx();
		int startCount = (currentPage - 1) * pageSize;
		weixinTask.setPageSize(pageSize);
		weixinTask.setStartCount(startCount);
		weixinTask.setPublishStatus(1);//定时发布
		weixinTask.setStatus(1);//预发布
		try{
			//获取需要发微信的列表   
			List<LinksusWx> tasks = linksusWxService.getWeiXinTaskList(weixinTask);
			if(tasks != null && tasks.size() > 0){
				for(LinksusWx weixin : tasks){
					addMassWeChatTimer(weixin);
				}
			}
			checkTaskListEnd(tasks);//判断任务是否轮询完成
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
	}

	/**
	 * 直发微信定时任务
	 * 
	 * @param record
	 */
	private void addMassWeChatTimer(LinksusWx record){
		if(Constants.STATUS_DELETE.equals(record.getStatus() + "")
				|| Constants.STATUS_PAUSE.equals(record.getStatus() + "")){
			QuartzUtil.removeJob("RECORD_" + record.getId().toString(), Constants.RECORD_GROUP_NAME);
		}else{
			Map dataMap = new HashMap();
			dataMap.put("pid", record.getId());
			//dataMap.put("groupId", groupId);
			Date sendTime = new Date(Long.parseLong(record.getSendTime().toString()) * 1000);
			QuartzUtil.addSimpleJob("RECORD_" + record.getId().toString(), Constants.RECORD_GROUP_NAME, dataMap,
					sendTime, SendMassWeChatTimer.class);
		}
	}

	//发送群发微信
	public String sendMessWeiChatByGroup(LinksusWx linksusWx,Object object,LinksusWeChatGroupInfo groupInfo,int isFirst)
			throws Exception{
		String result = "";
		//Integer sendTime = DateUtil.getUnixDate(new Date());
		int status = 2;
		Map resultMap = new HashMap();
		if(linksusWx.getType() == 0){//纯文本
			LinksusWxObjectSupply supply = (LinksusWxObjectSupply) object;
			//发布纯文本消息
			Map map1 = new HashMap();
			Map map2 = new HashMap();
			map1.put("group_id", groupInfo.getId());
			resultMap.put("filter", map1);
			resultMap.put("msgtype", "text");
			map2.put("content", supply.getContent());
			resultMap.put("text", map2);
		}else if(linksusWx.getType() == 1 || linksusWx.getType() == 2){//1 单图文	 2 多图文		
			//String mediaId = weiXinUploadNews(linksusWx.getToken(), object, linksusWx.getType());
			String mediaId = (String) object;
			if(StringUtils.isNotBlank(mediaId)){
				Map map1 = new HashMap();
				Map map2 = new HashMap();
				Map map3 = new HashMap();
				map1.put("group_id", groupInfo.getId());
				resultMap.put("filter", map1);
				resultMap.put("msgtype", "mpnews");
				map3.put("media_id", mediaId);
				resultMap.put("mpnews", map3);
			}
		}
		if(resultMap.size() > 0){
			result = HttpUtil.postBodyRequest(LoadConfig.getUrlEntity("WeiXinSendAll"), "access_token="
					+ linksusWx.getToken(), JsonUtil.map2json(resultMap));
			//logger.info(">>>>>>>>>>>>>result>>>>>>>>>>>:" + result);
			//result = "{\"errcode\":10,\"errmsg\":\"ok\",\"msg_id\":50322" + isFirst + "}";
			//发送成功修改状态及回写消息id
			if(StringUtils.isNotBlank(result)){
				String errcode = JsonUtil.getNodeValueByName(result, "errcode");
				if(StringUtils.isNotBlank(errcode) && errcode.equals("0")){//成功
					status = 3;
					//插入微信群发信息表
					LinksusWxMassGroup wxGroup = new LinksusWxMassGroup();
					Long groupPid = PrimaryKeyGen.getPrimaryKey("linksus_wx_mass_group", "pid");
					wxGroup.setPid(groupPid);
					wxGroup.setAccountId(linksusWx.getAccountId());
					String msgId = JsonUtil.getNodeValueByName(result, "msg_id");
					wxGroup.setMsgId(new Long(msgId));
					wxGroup.setWxId(linksusWx.getId());
					wxGroup.setGroupId(groupInfo.getId());
					wxGroup.setGroupName(groupInfo.getName());
					wxGroup.setGroupCount(groupInfo.getCount());
					wxGroup.setCreateTime(DateUtil.getUnixDate(new Date()));
					wxGroup.setFilterCount(0);
					wxGroup.setSentCount(0);
					wxGroup.setErrorCount(0);
					wxGroup.setLastTime(0);
					massService.insertLinksusWxMassGroup(wxGroup);
					result = "success";
				}else{//失败
					return result;
				}
			}
		}
		//为第一个组时，修改状态
		if(isFirst == 1){
			LinksusWx wx = new LinksusWx();
			wx.setId(linksusWx.getId());
			wx.setStatus(status);
			linksusWxService.updateWeiXinTaskList(wx);
		}
		return result;
	}

	public static void main(String[] args){
		Integer sendTime = DateUtil.getUnixDate(new Date());
		System.out.println(sendTime);
		//Map paramsMap = new HashMap();
		//paramsMap.put("ids", "337");
		//paramsMap.put(key, value);
		//new TaskSendWeiXin().sendImmediateMassInfo(paramsMap);
		/*
		 * List<LinksusWeChatGroupInfo> groups = new ArrayList(); Map parms =
		 * new HashMap(); parms .put( "access_token",
		 * "11U6nRr49qjoPlRF8IDuAlOrIPNXUPCmzTxh2JbFViNE9osZu8xq6sKDlaAmWcNk2dkTfJ5Fsd2niMuvICVxzLC115tSmc2OlxxmTAD1nrr31kV3NS3wnUNdZlkz-F-mZBQR8GwKGNNIh-6SOScN7w"
		 * ); String resGroupIds =
		 * HttpUtil.getRequest(LoadConfig.getUrlEntity("WeiXinGroupInfo"),
		 * parms); if(StringUtils.isNotBlank(resGroupIds)){ groups =
		 * (List<LinksusWeChatGroupInfo>)
		 * JsonUtil.json2list(JsonUtil.getNodeByName(resGroupIds, "groups"),
		 * LinksusWeChatGroupInfo.class); System.out.println(groups.size());
		 * for(LinksusWeChatGroupInfo info : groups){
		 * System.out.println(info.getId() + ":" + info.getName() + ":" +
		 * info.getCount()); } }
		 */
		/*
		 * String token =
		 * "ZBjcqGUgyqVjcUjqJJssg2YNf1ITVeTz4Iy9yABuPyf0gd1Te2X12zeKOsLabtqgatuA8HPYWqacjK8yqt5yWOGvxkSmnAIzDRrXG5_aYCKQt6hnc5BhalKzlZ7HwXe81BwwzgoSZCB_hcptfoUncQ"
		 * ; Map mapFirst = new HashMap(); Map map1 = new HashMap(); Map map2 =
		 * new HashMap(); map1.put("group_id", "101"); mapFirst.put("filter",
		 * map1); mapFirst.put("msgtype", "text"); map2.put("content",
		 * "hello world!"); mapFirst.put("text", map2);
		 * System.out.println(JsonUtil.map2json(mapFirst));
		 */
		//		String ss = "{\"errcode\":0,\"errmsg\":\"ok\",\"msg_id\":50322}";
		//		System.out.println(">>>>>>>>>>>value>>>>>>>>>:" + JsonUtil.getNodeValueByName(ss, "errcode"));
		//new TaskSendWeiXin().sendMessWeiChatByGroup(token, mapFirst);
		//Map map = new HashMap();
		//map.put("ids", "374");
		//		new TaskSendWeiXin()
		//				.uploadWxUserMedia(
		//						"Ww_dBKl083nQFMrXGMz9j0aYOB7YPaVhSDNz6yjOXYwevLASwsSPrYHw9ROnamRDrZe_jOYdPFQ7FYzkAAxHaqpYgiHujnMRp4Te_ZqN6qlgm2lO5DxRXBbJyalT_W9dm8sOsbVyO8-1tbPisPTMCQ",
		//						"http://gb.cri.cn/mmsource/images/2014/04/17/0ebc5d0e898f4bf8aa8914895155e782.jpg");
	}

	//上传多媒体文件,返回media_id 
	public String uploadWxUserMedia(String token,String picPath){
		String mediaId = "";
		String result = "";
		UrlEntity urlEntity = LoadConfig.getUrlEntity("uploadWxUserMedia");
		PostMethod postMethod = new PostMethod(urlEntity.getUrl() + "access_token=" + token + "&type=image");
		HttpClient httpClient = null;
		HttpURLConnection connection = null;
		try{
			URL realUrl = new URL(picPath);
			connection = (HttpURLConnection) realUrl.openConnection();
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.connect();
			String fileType = "jpg";
			String filePath = LoadConfig.getString("tempFilePath");
			String dtStr = new Date().getTime() + "";
			String tmpFileName = filePath + File.separator + "tmp_" + dtStr + ".jpg";
			File tempFile = FileUtil.filePutContents(tmpFileName, connection.getInputStream());

			FilePart fp = new FilePart("media", tempFile);

			Part[] parts = new Part[] { fp };
			postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			MultipartRequestEntity mrp = new MultipartRequestEntity(parts, postMethod.getParams());
			postMethod.setRequestEntity(mrp);
			httpClient = new HttpClient();
			int statusCode = 0;
			statusCode = httpClient.executeMethod(postMethod);
			if(statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_BAD_REQUEST){
				result = postMethod.getResponseBodyAsString();
				if(StringUtils.isNotBlank(result)){
					mediaId = JsonUtil.getNodeValueByName(result, "media_id");
				}else{
					return null;
				}
			}else{
				throw new Exception("请求返回状态：" + statusCode);
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			try{
				// 释放链接
				connection.disconnect();
				postMethod.releaseConnection();
				httpClient.getHttpConnectionManager().closeIdleConnections(0);
			}catch (Exception e2){
				e2.printStackTrace();
			}
		}
		return mediaId;
	}

	//上传图文消息素材 
	public String weiXinUploadNews(String token,Object object,int type){
		String error = "";
		List<LinksusMassArticle> articles = new ArrayList<LinksusMassArticle>();
		Map map = new HashMap();
		if(type == 1){
			LinksusWxObjectSupply supply = (LinksusWxObjectSupply) object;
			String fileMediaId = uploadWxUserMedia(token, supply.getPicOriginalUrl());
			if(StringUtils.isNotBlank(fileMediaId)){
				LinksusMassArticle article = new LinksusMassArticle();
				article.setAuthor(supply.getAutherName());
				article.setThumb_media_id(fileMediaId);
				article.setTitle(supply.getTitle());
				article.setContent(supply.getContent());
				article.setContent_source_url(supply.getConentUrl());
				article.setDigest(supply.getSummary());
				articles.add(article);
			}
		}else if(type == 2){
			List<LinksusWxObjectSupply> supplys = (List<LinksusWxObjectSupply>) object;
			if(supplys.size() > 0){
				for(LinksusWxObjectSupply supply : supplys){
					String fileMediaId = uploadWxUserMedia(token, supply.getPicOriginalUrl());
					if(StringUtils.isNotBlank(fileMediaId)){
						LinksusMassArticle article = new LinksusMassArticle();
						article.setAuthor(supply.getAutherName());
						article.setThumb_media_id(fileMediaId);
						article.setTitle(supply.getTitle());
						article.setContent(supply.getContent());
						article.setContent_source_url(supply.getConentUrl());
						article.setDigest(supply.getSummary());
						articles.add(article);
					}
				}
			}
		}
		map.put("articles", articles);
		String weiXinData = HttpUtil.postBodyRequest(LoadConfig.getUrlEntity("WeiXinUploadnews"), "access_token="
				+ token, JsonUtil.map2json(map));
		if(StringUtils.isNotBlank(weiXinData)){
			String returnType = JsonUtil.getNodeValueByName(weiXinData, "type");
			if(StringUtils.isNotBlank(returnType) && returnType.equals("news")){
				error = JsonUtil.getNodeValueByName(weiXinData, "media_id");
			}
		}
		return error;
	}
}
