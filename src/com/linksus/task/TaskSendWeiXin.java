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
 * ΢�ŵ�Ⱥ����Ϣ
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

	/**���ͷ�ʽ  ��ʱ/��ʱ */
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
		List<LinksusWeChatGroupInfo> groups = new ArrayList();//��ȡ΢�Ź��ں����б�	
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
				LogUtil.saveException(logger, new Exception("δȡ���˻�id:" + linksusWx.getAccountId() + "��������!"));
			}
		}
		if(groups.size() > 0){
			//�ϴ���Ϣid
			String mediaId = "";
			if(linksusWx.getType() == 1){//��ͼ��
				LinksusWxObjectSupply supply = linksusWxObjectSupplyService
						.getSingleLinksusWxObjectSupplyById(new Long(linksusWx.getId()));
				if(supply != null){
					mediaId = weiXinUploadNews(linksusWx.getToken(), supply, linksusWx.getType());
				}
			}else if(linksusWx.getType() == 2){//��ͼ��
				List<LinksusWxObjectSupply> supplys = linksusWxObjectSupplyService
						.getMoreLinksusWxObjectSupplyById(new Long(linksusWx.getId()));
				if(supplys != null && supplys.size() > 0){
					mediaId = weiXinUploadNews(linksusWx.getToken(), supplys, linksusWx.getType());
				}
			}
			int ifFirst = 0;
			for(int i = 0; i < groups.size(); i++){
				LinksusWeChatGroupInfo groupInfo = (LinksusWeChatGroupInfo) groups.get(i);
				//if(groupInfo.getCount() > 0 && (groupInfo.getId() == 101 || groupInfo.getId() == 102)){//�����Ա��������0ʱ
				if(groupInfo.getCount() > 0){
					ifFirst++;
					//ͨ��id��ѯ΢��Ⱥ����Ϣ����
					if(linksusWx.getType() == 0){//���ı�
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
											new Exception("΢��Ⱥ������id:" + linksusWx.getId() + ",δ��ѯ����������!"));
							break;
						}
					}else if(linksusWx.getType() == 1 || linksusWx.getType() == 2){//��ͼ�Ļ��ͼ��
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
											new Exception("΢��Ⱥ������id:" + linksusWx.getId() + ",δ��ѯ����������!"));
							break;
						}
					}
				}
			}
		}
		if(StringUtils.isBlank(resStr) || (StringUtils.isNotBlank(resStr) && !resStr.equals("success"))
				|| StringUtils.isNotBlank(resultInfo)){//�������󣬸��´�����Ϣ
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
		for(String id : args){//Ⱥ��΢�ŵķ�����Ϣid
			//��ѯ��ҪȺ��΢�ŵ��˺���Ϣ��Ⱥ��΢������
			LinksusWx linksusWx = linksusWxService.getLinksusWXUserAndInfo(new Long(id));
			if(linksusWx != null){
				if(linksusWx.getPublishStatus() == 0){//��ʱ����		
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
				}else if(linksusWx.getPublishStatus() == 1){//��ʱ����
					addMassWeChatTimer(linksusWx);
				}else{
					LinksusTaskErrorCode error = cache.getErrorCode("20032");
					if(error != null){
						resultInfo = error.getErrorMsg();
					}
					LogUtil.saveException(logger, new Exception("΢��Ⱥ������id:" + linksusWx.getId() + "," + resultInfo));
				}

			}else{
				if(StringUtils.isBlank(failId)){
					failId = id;
				}else{
					failId = failId + "," + id;
				}
				//resultInfo = "΢��Ⱥ������id:" + id + ",û������˻���Ϣ!";
				LinksusTaskErrorCode error = cache.getErrorCode("20042");
				if(error != null){
					resultInfo = error.getErrorMsg();
				}
				LinksusWx entity = new LinksusWx();
				entity.setId(new Long(id));
				entity.setStatus(2);
				entity.setErrmsg(resultInfo);
				linksusWxService.updateWeiXinTaskstatusAndErrmsg(entity);
				LogUtil.saveException(logger, new Exception("΢��Ⱥ������id:" + id + ",û������˻���Ϣ!"));
				return "20042";
			}
		}
		return resultMap;
	}

	public void sendImmediate(){
		try{
			// ����
			LinksusWx weixinTask = new LinksusWx();
			int startCount = (currentPage - 1) * pageSize;
			weixinTask.setPageSize(pageSize);
			weixinTask.setStartCount(startCount);
			weixinTask.setPublishStatus(0);//��ʱ����
			weixinTask.setStatus(1);//Ԥ����
			//��ȡ��Ҫ��΢�ŵ��б�   
			List<LinksusWx> tasks = linksusWxService.getWeiXinTaskList(weixinTask);
			if(tasks != null && tasks.size() > 0){
				for(LinksusWx weixin : tasks){
					sendMessWx(weixin);
				}
			}
			checkTaskListEnd(tasks);//�ж������Ƿ���ѯ���
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
	}

	//////////////////////////////////////��ʱ����////////////////////////////////////////////

	private void sendAtRegularTime(){
		LinksusWx weixinTask = new LinksusWx();
		int startCount = (currentPage - 1) * pageSize;
		weixinTask.setPageSize(pageSize);
		weixinTask.setStartCount(startCount);
		weixinTask.setPublishStatus(1);//��ʱ����
		weixinTask.setStatus(1);//Ԥ����
		try{
			//��ȡ��Ҫ��΢�ŵ��б�   
			List<LinksusWx> tasks = linksusWxService.getWeiXinTaskList(weixinTask);
			if(tasks != null && tasks.size() > 0){
				for(LinksusWx weixin : tasks){
					addMassWeChatTimer(weixin);
				}
			}
			checkTaskListEnd(tasks);//�ж������Ƿ���ѯ���
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
	}

	/**
	 * ֱ��΢�Ŷ�ʱ����
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

	//����Ⱥ��΢��
	public String sendMessWeiChatByGroup(LinksusWx linksusWx,Object object,LinksusWeChatGroupInfo groupInfo,int isFirst)
			throws Exception{
		String result = "";
		//Integer sendTime = DateUtil.getUnixDate(new Date());
		int status = 2;
		Map resultMap = new HashMap();
		if(linksusWx.getType() == 0){//���ı�
			LinksusWxObjectSupply supply = (LinksusWxObjectSupply) object;
			//�������ı���Ϣ
			Map map1 = new HashMap();
			Map map2 = new HashMap();
			map1.put("group_id", groupInfo.getId());
			resultMap.put("filter", map1);
			resultMap.put("msgtype", "text");
			map2.put("content", supply.getContent());
			resultMap.put("text", map2);
		}else if(linksusWx.getType() == 1 || linksusWx.getType() == 2){//1 ��ͼ��	 2 ��ͼ��		
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
			//���ͳɹ��޸�״̬����д��Ϣid
			if(StringUtils.isNotBlank(result)){
				String errcode = JsonUtil.getNodeValueByName(result, "errcode");
				if(StringUtils.isNotBlank(errcode) && errcode.equals("0")){//�ɹ�
					status = 3;
					//����΢��Ⱥ����Ϣ��
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
				}else{//ʧ��
					return result;
				}
			}
		}
		//Ϊ��һ����ʱ���޸�״̬
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

	//�ϴ���ý���ļ�,����media_id 
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
				throw new Exception("���󷵻�״̬��" + statusCode);
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			try{
				// �ͷ�����
				connection.disconnect();
				postMethod.releaseConnection();
				httpClient.getHttpConnectionManager().closeIdleConnections(0);
			}catch (Exception e2){
				e2.printStackTrace();
			}
		}
		return mediaId;
	}

	//�ϴ�ͼ����Ϣ�ز� 
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
