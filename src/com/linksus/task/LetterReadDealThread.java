package com.linksus.task;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.config.LoadConfig;
import com.linksus.common.module.WeiboInteractCommon;
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
import com.linksus.common.util.RedisUtil;
import com.linksus.common.util.StringUtil;
import com.linksus.entity.LetterReadDTO;
import com.linksus.entity.LinksusInteractMessage;
import com.linksus.entity.LinksusRelationWeibouser;
import com.linksus.entity.LinksusTaskAttentionUser;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.entity.UrlEntity;
import com.linksus.queue.TaskQueueAllSinaFans;
import com.linksus.service.LinksusInteractMessageService;
import com.linksus.service.LinksusRelationWeibouserService;
import com.linksus.service.LinksusTaskAttentionUserService;

/**
 * sina返回私信数据后的处理线程，防止达到总接口限制后阻塞私信读取
 * 
 * @author zhangew
 * 
 */
public class LetterReadDealThread extends BaseTask implements Runnable{

	protected static final Logger logger = LoggerFactory.getLogger(LetterReadDealThread.class);
	/** 缓存对象 */
	CacheUtil cache = CacheUtil.getInstance();
	/** 任务队列 */
	private static Queue letterQueue = new ConcurrentLinkedQueue();

	LinksusInteractMessageService messageService = (LinksusInteractMessageService) ContextUtil
			.getBean("linksusInteractMessageService");
	LinksusTaskAttentionUserService userService = (LinksusTaskAttentionUserService) ContextUtil
			.getBean("linksusTaskAttentionUserService");
	LinksusRelationWeibouserService relationWeiboUserService = (LinksusRelationWeibouserService) ContextUtil
			.getBean("linksusRelationWeibouserService");

	/** 将私信存入队列 */
	public static void putLetterQueue(LetterReadDTO dto){
		letterQueue.offer(dto);
	}

	@Override
	public void run(){
		String getDataUrl = LoadConfig.getString("letterData");
		boolean sleepFlag = false;
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
						dto.getReceiver_id();
						Long rpsId = dto.getSender_id();
						// 判断私信是否存在 存在不再处理
						if(dto.getId() == null){
							continue;
						}
						//通过缓存判断该私信是否存在
						if(RedisUtil.getRedisSet("sina_msgid", dto.getId())){
							logger.info("该私信信息已存在,不做处理!");
							continue;
						}
						RedisUtil.setRedisSet("sina_msgid", dto.getId());//消息加入缓存
						//						Integer scount = messageService.getLinksusInteractMessageByMsgId(new Long(dto.getId()));
						//						if(scount.intValue() != 0){
						//							logger.info("该私信信息已存在,不做处理!");
						//							continue;
						//						}
						// 处理私信 判断私信类型
						if("image".equals(dto.getType()) || "text".equals(dto.getType())
								|| "voice".equals(dto.getType()) || "event".equals(dto.getType())){
							LinksusInteractMessage message = new LinksusInteractMessage();
							Long inteWeiboId = PrimaryKeyGen.getPrimaryKey("linksus_interact_message", "PID");
							Date createTime = DateUtil.parse(dto.getCreated_at(), "EEE MMM dd HH:mm:ss zzz yyyy",
									Locale.US);
							Integer unixTime = Integer.parseInt(String.valueOf(createTime.getTime() / 1000));
							message.setPid(inteWeiboId);
							//判断发信人是否存在
							//long userId = new Long((String)map.get("openid"));
							//检查微博用户是否存在
							LinksusRelationWeibouser relationWeibouser = null;
							Map sinaparaMap = new HashMap();
							sinaparaMap.put("access_token", dto.getToken());
							sinaparaMap.put("uid", rpsId);

							String strjson = HttpUtil.getRequest(LoadConfig.getUrlEntity("fensiinfo"), sinaparaMap);
							if(StringUtils.isNotBlank(strjson)){
								LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(strjson);
								if(StringUtils.isNotBlank(strjson) && error == null){//返回粉丝信息
									//检查微博用户是否存在
									String relationType = "3";
									if("event".equals(dto.getType())){//事件
										String subtype = dto.getData().getSubtype();
										if("follow".equals(subtype)){//添加关注
											relationType = "1";
										}
									}
									relationWeibouser = getWeiboUserInfo(strjson, dto, rpsId + "", relationType, 4);
								}
								if(relationWeibouser != null){
									//处理互动用户表、互动人员表
									WeiboInteractCommon interactCommon = new WeiboInteractCommon();
									Long recordId = interactCommon.dealWeiboInteract(relationWeibouser.getUserId(),
											relationWeibouser.getPersonId(), relationWeibouser.getAccountId(), Integer
													.valueOf(1), new Long(0), "5", inteWeiboId, unixTime);
									Long userId = relationWeibouser.getUserId();
									message.setUserId(userId);
									message.setRecordId(recordId);
									//插入私信互动信息表
									message.setAccountId(dto.getAccountId());
									message.setContent(dto.getText());
									message.setMsgId(new Long(dto.getId()));
									message.setInteractTime(unixTime);
									message.setInteractType(1);//用户发布
									message.setAccountType(1);//新浪
									if("image".equals(dto.getType())){
										//存在图片/附件的
										String tovfid = dto.getData().getTovfid();
										if(!StringUtils.isBlank(tovfid)){
											String param = "access_token=" + dto.getToken() + "&fid=" + tovfid;
											getSinaLetterFile(getDataUrl, param, message);
										}else{
											message.setImg("");
											message.setImgThumb("");
											message.setImgName("");
											message.setAttatch("");
											message.setAttatchName("");
											message.setMsgType(5);
										}
									}else{
										message.setMsgType(5);//私信
										if("event".equals(dto.getType())){//事件
											String subtype = dto.getData().getSubtype();
											if("follow".equals(subtype)){//添加关注
												message.setMsgType(1);
												//判断新粉丝任务表中是否有自动关注任务
												LinksusTaskAttentionUser isTaskAttentionUser = new LinksusTaskAttentionUser();
												isTaskAttentionUser.setAccountId(dto.getAccountId());
												isTaskAttentionUser.setUserId(userId);
												Integer countUsers = userService
														.countLinksusTaskAttentionUser(isTaskAttentionUser);
												if(countUsers == 0){
													//添加新粉丝任务表
													LinksusTaskAttentionUser user = new LinksusTaskAttentionUser();
													Long pid = PrimaryKeyGen.getPrimaryKey(
															"linksus_task_attention_user", "pid");
													user.setPid(pid);
													user.setAccountId(dto.getAccountId());
													user.setUserId(userId);
													user.setAccountType(1);//新浪
													user.setStatus(1);
													user.setInteractTime(unixTime);
													user.setCreateTime(DateUtil.getUnixDate(new Date()));
													userService.insertLinksusTaskAttentionUser(user);
												}
												//加入全量任务队列
												TaskQueueAllSinaFans.addFensiIdInQueue45(rpsId + "",
														dto.getAccountId(), dto.getInstitutionId());
											}else if("unfollow".equals(subtype)){//取消关注
												message.setMsgType(2);
												//处理账户用户关系数据 参数:账号/用户ID
												TaskCancelAttention.dealRelationUserAccountSingle(dto
														.getInstitutionId(), dto.getAccountId(), userId, "1");
											}
										}
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
	public String getSinaLetterFile(String strurl,String param,LinksusInteractMessage message){
		String strResult = null;
		HttpURLConnection connection = null;
		//CacheUtil cache = CacheUtil.getInstance();
		//cache.checkConnLimitPerHour();
		try{
			UrlEntity strUrl = LoadConfig.getUrlEntity("letterData");
			logger.info("strUrl :{}", strUrl.getUrl());
			String url = strUrl.getUrl() + param;
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
			if(StringUtils.isNotBlank(fileType)){//返回有类型文件时，从新浪读取文件
				// 遍历所有的响应头字段
				//			
				//			  for (String key : map.keySet()) { 
				//				  System.out.println(key + "--->"
				//			  + map.get(key));
				//				  }

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
					logger.debug("-----after zoom");
					paramMap.put("thumb." + fileType, FileUtil.getBase64StrFormFile(zoonFile));
					paramMap.put("original." + fileType, FileUtil.getBase64StrFormFile(tempFile));
					rsStr = HttpUtil.sendToDataServer(paramMap);
					String image = JsonUtil.getNodeValueByName(JsonUtil.getNodeByName(rsStr, "data"), "original_"
							+ fileType);
					String thumb = JsonUtil.getNodeValueByName(JsonUtil.getNodeByName(rsStr, "data"), "thumb_"
							+ fileType);
					message.setImg(image);
					message.setImgThumb(thumb);
					message.setImgName(fileDecodeName);
					message.setAttatch("");
					message.setAttatchName("");
					message.setMsgType(5);
				}else{
					paramMap.put("original." + fileType, FileUtil.getBase64StrFormFile(tempFile));
					rsStr = HttpUtil.sendToDataServer(paramMap);
					String doc = JsonUtil.getNodeValueByName(JsonUtil.getNodeByName(rsStr, "data"), "original_"
							+ fileType);
					message.setMsgType(5);
					message.setImg("");
					message.setImgThumb("");
					message.setImgName("");
					message.setAttatch(JsonUtil.array2json(new String[] { doc }).replaceAll("\\\\/", "/"));
					message.setAttatchName(JsonUtil.array2json(new String[] { fileDecodeName })
							.replaceAll("\\\\/", "/"));
				}
			}else{
				message.setMsgType(5);
				message.setImg("");
				message.setImgThumb("");
				message.setImgName("");
				message.setAttatch("");
				message.setAttatchName("");
			}
		}catch (Exception e){
			message.setMsgType(5);
			message.setImg("");
			message.setImgThumb("");
			message.setImgName("");
			message.setAttatch("");
			message.setAttatchName("");
			LogUtil.saveException(logger, e);
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
	public LinksusRelationWeibouser getWeiboUserInfo(String userStr,LetterReadDTO dto,String rpsId,String relationType,
			int interactBit) throws Exception{
		//String userStr =JsonUtil.map2json(userMap);
		//获取评论用户的新浪appId 和  业务类型判断用户是否存在微博用户表中
		WeiboUserCommon weiboUser = new WeiboUserCommon();
		Map parmMap = new HashMap();
		parmMap.put("token", dto.getToken());
		parmMap.put("type", "1");
		parmMap.put("AccountId", dto.getAccountId() + "");
		parmMap.put("InstitutionId", dto.getInstitutionId());
		LinksusRelationWeibouser user = weiboUser.dealWeiboUserInfo(userStr, "1", parmMap, false, relationType,
				interactBit);
		user.setAccountId(dto.getAccountId());
		user.setInstitutionId(dto.getInstitutionId());
		return user;
	}

	public static void main(String[] args){
		String data = "{\"id\":1312240001024632,\"type\":\"image\",\"receiver_id\":1251105081,\"sender_id\":2916398037,\"created_at\":\"Tue Dec 24 16:53:25 +0800 2013\",\"text\":\"测试图片\",\"data\":{\"vfid\":1148918212,\"tovfid\":1148918215}}";
		LetterReadDTO dto = (LetterReadDTO) JsonUtil.json2Bean(data, LetterReadDTO.class);
		//dto.setToken(account.getToken());
		//dto.setAccountId(account.getId());
		LetterReadDealThread.putLetterQueue(dto);
		LetterReadDealThread thread = new LetterReadDealThread();
		thread.run();
	}

	@Override
	public void cal(){
	}
}
