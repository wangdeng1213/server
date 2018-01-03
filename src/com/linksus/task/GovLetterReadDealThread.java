package com.linksus.task;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.config.LoadConfig;
import com.linksus.common.module.WeiboUserCommon;
import com.linksus.common.util.CacheUtil;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.DateUtil;
import com.linksus.common.util.FileUtil;
import com.linksus.common.util.HttpUtil;
import com.linksus.common.util.ImageUtil;
import com.linksus.common.util.JsonUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.PrimaryKeyGen;
import com.linksus.common.util.StringUtil;
import com.linksus.entity.LetterReadDTO;
import com.linksus.entity.LinksusGovMessage;
import com.linksus.entity.LinksusGovRunning;
import com.linksus.entity.LinksusGovStructure;
import com.linksus.entity.LinksusRelationWeibouser;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.service.LinksusGovMessageService;
import com.linksus.service.LinksusGovRunningService;
import com.linksus.service.LinksusGovStructureService;
import com.linksus.service.LinksusRelationWeibouserService;
import com.linksus.service.LinksusTaskAttentionUserService;

/**
 * sina返回私信数据后的处理线程，防止达到总接口限制后阻塞私信读取
 * 
 * @author zhangew
 * 
 */
public class GovLetterReadDealThread extends BaseTask implements Runnable{

	protected static final Logger logger = LoggerFactory.getLogger(GovLetterReadDealThread.class);
	/** 缓存对象 */
	CacheUtil cache = CacheUtil.getInstance();
	/** 任务队列 */
	private static Queue letterQueue = new ConcurrentLinkedQueue();

	LinksusGovMessageService messageService = (LinksusGovMessageService) ContextUtil
			.getBean("linksusGovMessageService");
	LinksusTaskAttentionUserService userService = (LinksusTaskAttentionUserService) ContextUtil
			.getBean("linksusTaskAttentionUserService");
	LinksusRelationWeibouserService relationWeiboUserService = (LinksusRelationWeibouserService) ContextUtil
			.getBean("linksusRelationWeibouserService");
	private LinksusGovRunningService linksusGovRunningService = (LinksusGovRunningService) ContextUtil
			.getBean("linksusGovRunningService");
	private LinksusGovStructureService linksusGovStructureService = (LinksusGovStructureService) ContextUtil
			.getBean("linksusGovStructureService");

	/** 将私信存入队列 */
	public static void putLetterQueue(LetterReadDTO dto){
		letterQueue.offer(dto);
	}

	@Override
	public void run(){
		String getDataUrl = LoadConfig.getString("letterData");
		boolean sleepFlag = false;
		List<LinksusGovStructure> orgList = linksusGovStructureService.getLinksusGovStructureByOrgId();
		Map<Integer, LinksusGovStructure> orgMap = new HashMap<Integer, LinksusGovStructure>();
		for(Iterator<LinksusGovStructure> iter = orgList.iterator(); iter.hasNext();){
			LinksusGovStructure org = iter.next();
			if(org.getGid() == null){
				LogUtil.saveException(logger, new Exception("-----组织机构表主键异常:" + "组织机构gid:" + org.getGid()));
				continue;
			}
			orgMap.put(org.getAccountId(), org);
		}
		while (true){
			try{
				if(sleepFlag){
					Thread.sleep(2000);
				}
				if(letterQueue.size() == 0){
					sleepFlag = true;
					continue;
				}else{
					for(int i = 0; i < letterQueue.size(); i++){
						LetterReadDTO dto = (LetterReadDTO) letterQueue.poll();
						Long rpsId = dto.getSender_id();
						// 判断私信是否存在 存在不再处理
						Integer scount = messageService.getLinksusInteractMessageCountByMsgId(new Long(dto.getId()));
						if(scount.intValue() != 0){
							logger.info("该私信信息已存在,不做处理!");
							continue;
						}
						// 处理私信 判断私信类型
						if("image".equals(dto.getType()) || "text".equals(dto.getType())
								|| "voice".equals(dto.getType()) || "event".equals(dto.getType())){
							LinksusGovMessage message = new LinksusGovMessage();
							Long inteWeiboId = PrimaryKeyGen.GenerateSerialNum();
							Date createTime = DateUtil.parse(dto.getCreated_at(), "EEE MMM dd HH:mm:ss zzz yyyy",
									Locale.US);
							Integer unixTime = Integer.parseInt(String.valueOf(createTime.getTime() / 1000));
							message.setRunId(inteWeiboId);
							logger.info(">>>>>>>>>>>dto.getSender_id()>>>>>>>>>>>" + dto.getSender_id());
							message.setUserId(dto.getSender_id());
							//检查微博用户是否存在
							LinksusRelationWeibouser relationWeibouser = null;
							Map sinaparaMap = new HashMap();
							sinaparaMap.put("access_token", dto.getToken());
							sinaparaMap.put("uid", rpsId);

							String strjson = HttpUtil.getRequest(LoadConfig.getUrlEntity("fensiinfo"), sinaparaMap);
							LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(strjson);
							if(StringUtils.isNotBlank(strjson) && error == null){
								//检查微博用户是否存在
								relationWeibouser = getWeiboUserInfo(strjson, dto, rpsId + "");
							}
							if(relationWeibouser != null){
								Long userId = relationWeibouser.getUserId();
								Integer gid = orgMap.get(new Integer(dto.getAccountId().intValue())).getGid();
								Integer orgId = orgMap.get(new Integer(dto.getAccountId().intValue())).getOrgId();
								saveGovRunning(inteWeiboId, gid, orgId, userId);

								//插入私信互动信息表
								message.setAccountId(dto.getAccountId());
								message.setContent(dto.getText());
								message.setMsgId(new Long(dto.getId()));
								message.setInteractTime(unixTime);
								message.setInteractType(1);//用户发布
								if("image".equals(dto.getType())){
									//存在图片/附件的
									String tovfid = dto.getData().getTovfid();
									if(!StringUtils.isBlank(tovfid)){
										String param = "access_token=" + dto.getToken() + "&fid=" + tovfid;
										getSinaLetterFile(getDataUrl, param, message);
									}
								}else{
									message.setImg("");
									message.setImgThumb("");
									message.setImgName("");
									message.setAttatch("");
									message.setAttatchName("");
								}
								messageService.addInteractReadMessage(message);
							}
						}
					}
					sleepFlag = false;
				}
			}catch (Exception e){
				LogUtil.saveException(logger, e);
				e.printStackTrace();
			}
		}
	}

	// 从新浪得到文件
	public String getSinaLetterFile(String strurl,String param,LinksusGovMessage message){
		String strResult = null;
		HttpURLConnection connection = null;
		//CacheUtil cache = CacheUtil.getInstance();
		//cache.checkConnLimitPerHour();
		try{
			String url = strurl + param;
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			connection = (HttpURLConnection) realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			String disp = map.get("Content-Disposition").get(0);
			String fileName = disp.substring(disp.indexOf("\"") + 1, disp.lastIndexOf("\""));
			String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
			// 遍历所有的响应头字段
			//String filePath=LoadConfig.getString("letterDataPath");
			String filePath = LoadConfig.getString("tempFilePath");
			String dtStr = new Date().getTime() + "";
			String tmpFileName = filePath + File.separator + "tmp_" + dtStr;
			String tmpZoonName = filePath + File.separator + "zoon_" + dtStr;
			File tempFile = FileUtil.filePutContents(tmpFileName, connection.getInputStream());
			File zoonFile = new File(tmpZoonName);
			Map paramMap = new HashMap();
			String rsStr = "";
			String fileDecodeName = URLDecoder.decode(fileName, "UTF-8");
			if("jpg".equals(fileType) || "png".equals(fileType) || "gif".equals(fileType) || "bmp".equals(fileType)){
				ImageUtil.zoomImage(tempFile, zoonFile, fileType, 200, 200);
				paramMap.put("thumb." + fileType, FileUtil.getBase64StrFormFile(zoonFile));
				paramMap.put("original." + fileType, FileUtil.getBase64StrFormFile(tempFile));
				rsStr = HttpUtil.sendToDataServer(paramMap);
				String image = JsonUtil.getNodeValueByName(JsonUtil.getNodeByName(rsStr, "data"), "original_"
						+ fileType);
				String thumb = JsonUtil.getNodeValueByName(JsonUtil.getNodeByName(rsStr, "data"), "thumb_" + fileType);
				message.setImg(image);
				message.setImgThumb(thumb);
				message.setImgName(fileDecodeName);
				message.setAttatch("");
				message.setAttatchName("");
			}else{
				paramMap.put("original." + fileType, FileUtil.getBase64StrFormFile(tempFile));
				rsStr = HttpUtil.sendToDataServer(paramMap);
				String doc = JsonUtil.getNodeValueByName(JsonUtil.getNodeByName(rsStr, "data"), "original_" + fileType);
				//				message.setMsgType(5);
				message.setImg("");
				message.setImgThumb("");
				message.setImgName("");
				message.setAttatch(JsonUtil.array2json(new String[] { doc }).replaceAll("\\\\/", "/"));
				message.setAttatchName(JsonUtil.array2json(new String[] { fileDecodeName }).replaceAll("\\\\/", "/"));
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally{
			if(connection != null){
				connection.disconnect();
			}
		}
		return strResult;
	}

	/**
	  *  处理微博用户并返回微博用户的信息
	 * @throws Exception 
	  */
	public LinksusRelationWeibouser getWeiboUserInfo(String userStr,LetterReadDTO dto,String rpsId) throws Exception{
		//String userStr =JsonUtil.map2json(userMap);
		//获取评论用户的新浪appId 和  业务类型判断用户是否存在微博用户表中
		WeiboUserCommon weiboUser = new WeiboUserCommon();
		Map parmMap = new HashMap();
		parmMap.put("token", dto.getToken());
		parmMap.put("type", "1");
		parmMap.put("AccountId", dto.getAccountId() + "");
		parmMap.put("InstitutionId", dto.getInstitutionId());
		weiboUser.dealWeiboUserInfo(userStr, "1", parmMap, false, "3", 6);

		//根据rpsId和accountType 判断用户是否存在
		Map paraMap = new HashMap();
		paraMap.put("rpsId", rpsId);
		paraMap.put("userType", "1");
		LinksusRelationWeibouser user = relationWeiboUserService.getWeibouserByrpsIdAndType(paraMap);
		user.setAccountId(dto.getAccountId());
		user.setInstitutionId(dto.getInstitutionId());
		return user;
	}

	private void saveGovRunning(Long runId,Integer gid,Integer orgId,Long userId){
		LinksusGovRunning govRunning = new LinksusGovRunning();
		govRunning.setRunId(runId);
		govRunning.setGid(gid);
		govRunning.setCreateTime(DateUtil.getUnixDate(new Date()));
		govRunning.setIsFinish(0);
		govRunning.setOrgId(orgId);
		govRunning.setIsMultiterm(0);//默认是0 多问为1
		//互动类型:1 评论 2 转发 3 @ 4 评论并@  5评论并转发 6 私信 7 平台账户回复
		govRunning.setInteractType(6);
		govRunning.setInteractMode(2);//互动方式： 1 微博 2 私信
		govRunning.setUserId(userId);
		govRunning.setUpdateTime(DateUtil.getUnixDate(new Date()));
		linksusGovRunningService.insertLinksusGovRunning(govRunning);
	}

	public static void main(String[] args){
		//230	1248	20581	1041	1	1403110000839222	1	5	午饭结束[江南style]						0	0	0	0	0	0	1394513880
		String data = "{\"id\":1403110000839223,\"type\":\"image\",\"receiver_id\":1251105081,\"sender_id\":2916398037,\"created_at\":\"Tue Dec 24 16:53:25 +0800 2014\",\"text\":\"午饭结束[江南style]\",\"data\":{\"vfid\":1148918212,\"tovfid\":1394513880}}";
		LetterReadDTO dto = (LetterReadDTO) JsonUtil.json2Bean(data, LetterReadDTO.class);
		dto.setToken("2.00NaVf3BeE2s5B2f1113d612012bdT");
		dto.setAccountId(10000L);
		dto.setInstitutionId(10000L);
		GovLetterReadDealThread.putLetterQueue(dto);
		GovLetterReadDealThread thread = new GovLetterReadDealThread();
		thread.run();
	}

	@Override
	public void cal(){
	}
}
