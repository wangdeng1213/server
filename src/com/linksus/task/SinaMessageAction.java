package com.linksus.task;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.jcs.access.exception.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.Constants;
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
import com.linksus.entity.LinksusAppaccount;
import com.linksus.entity.LinksusGovMessage;
import com.linksus.entity.LinksusGovRunning;
import com.linksus.entity.LinksusGovStructure;
import com.linksus.entity.LinksusInteractMessage;
import com.linksus.entity.LinksusRelationWeibouser;
import com.linksus.entity.LinksusTaskAttentionUser;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.entity.LinksusTaskInvalidRecord;
import com.linksus.entity.LinksusWxValidate;
import com.linksus.entity.UrlEntity;
import com.linksus.queue.TaskQueueAllSinaFans;
import com.linksus.service.LinksusAppaccountService;
import com.linksus.service.LinksusGovMessageService;
import com.linksus.service.LinksusGovRunningService;
import com.linksus.service.LinksusGovStructureService;
import com.linksus.service.LinksusInteractMessageService;
import com.linksus.service.LinksusRelationWeibouserService;
import com.linksus.service.LinksusTaskAttentionUserService;
import com.linksus.service.LinksusWxValidateService;

/**
 * sina私信读取回复
 * 
 * @author wangrui
 * 
 */

public class SinaMessageAction extends HttpServlet{

	protected static final Logger logger = LoggerFactory.getLogger(SinaMessageAction.class);

	LinksusAppaccountService linksusAppaccountService = (LinksusAppaccountService) ContextUtil
			.getBean("linksusAppaccountService");
	LinksusTaskAttentionUserService userService = (LinksusTaskAttentionUserService) ContextUtil
			.getBean("linksusTaskAttentionUserService");
	LinksusRelationWeibouserService relationWeiboUserService = (LinksusRelationWeibouserService) ContextUtil
			.getBean("linksusRelationWeibouserService");
	LinksusInteractMessageService messageService = (LinksusInteractMessageService) ContextUtil
			.getBean("linksusInteractMessageService");
	LinksusGovStructureService linksusGovStructureService = (LinksusGovStructureService) ContextUtil
			.getBean("linksusGovStructureService");
	LinksusGovRunningService linksusGovRunningService = (LinksusGovRunningService) ContextUtil
			.getBean("linksusGovRunningService");
	LinksusGovMessageService messageGovService = (LinksusGovMessageService) ContextUtil
			.getBean("linksusGovMessageService");

	CacheUtil cache = CacheUtil.getInstance();

	public void init(ServletConfig config) throws ServletException{
		super.init(config);
	}

	@Override
	protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException{
		getAction(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException{
		try{
			postAction(req, resp);
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
	}

	//post调用接口
	private void postAction(HttpServletRequest req,HttpServletResponse response) throws Exception{

		//根据url中的微信号查询账号信息 vallid_token
		String uniqueCode = req.getParameter("uniqueCode");
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>uniqueCode:{}", uniqueCode);
		uniqueCode = StringUtil.decodeBase64(uniqueCode);
		LinksusAppaccount appcount = linksusAppaccountService.getAppaccountByAppid(uniqueCode);
		if(appcount == null){
			dealExceptionInvalidRecord(0l, 0l, new Exception("新浪账号:" + uniqueCode + ",未查询到有效账号!"));
			response.getWriter().print("error");
			logger.error(">>>>>>>>>>>>>>>>>未查询到有效账号");
			return;
		}
		//根据appsecret判断信息的有效性
		String appSecret = LoadConfig.getString("sinaAppSecret");
		if(!validateSignature(appSecret, req)){//验证失败
			response.getWriter().print("error");
			logger.error(">>>>>>>>>>>>>>>>>appsecret校验失败 消息无效");
			//插入无效操作记录表
			dealExceptionInvalidRecord(appcount.getInstitutionId(), appcount.getId(), new Exception(
					"appSecret校验失败 消息无效!"));
			return;
		}
		response.setContentType("text/html;charset=UTF-8");
		ServletInputStream input = req.getInputStream();
		byte buffer[] = new byte[1024];
		StringBuffer buf = new StringBuffer();
		int i = 0;
		while ((i = input.read(buffer)) != -1){
			buf.append(new String(buffer, 0, i));
		}
		String getDataUrl = LoadConfig.getString("letterData");
		if(appcount.getIsGov() == 0){
			LetterReadDTO dto = (LetterReadDTO) JsonUtil.json2Bean(buf.toString(), LetterReadDTO.class);
			if(dto != null){
				Long rpsId = dto.getSender_id();
				dto.setToken(appcount.getToken());
				dto.setAccountId(appcount.getId());
				dto.setInstitutionId(appcount.getInstitutionId());

				//通过缓存判断该私信是否存在,使用FromUserName + CreateTime 排重
				String msgid = dto.getReceiver_id() + dto.getCreated_at();
				if(RedisUtil.getRedisSet("sina_newMsgid", msgid)){
					logger.info("该私信信息已存在,不做处理!");
					response.getWriter().print("");
				}
				RedisUtil.setRedisSet("sina_newMsgid", msgid);//消息加入缓存
				// 处理私信 判断私信类型
				if("image".equals(dto.getType()) || "text".equals(dto.getType()) || "voice".equals(dto.getType())
						|| "event".equals(dto.getType())){
					LinksusInteractMessage message = new LinksusInteractMessage();
					Long inteWeiboId = PrimaryKeyGen.getPrimaryKey("linksus_interact_message", "PID");
					Date createTime = DateUtil.parse(dto.getCreated_at(), "EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
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
							//message.setMsgId(new Long(dto.getId()));
							message.setMsgId(0l);
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
											Long pid = PrimaryKeyGen
													.getPrimaryKey("linksus_task_attention_user", "pid");
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
										TaskQueueAllSinaFans.addFensiIdInQueue45(rpsId + "", dto.getAccountId(), dto
												.getInstitutionId());
									}else if("unfollow".equals(subtype)){//取消关注
										message.setMsgType(2);
										//处理账户用户关系数据 参数:账号/用户ID
										TaskCancelAttention.dealRelationUserAccountSingle(dto.getInstitutionId(), dto
												.getAccountId(), userId, "1");
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
		}else if(appcount.getIsGov() == 1){
			LetterReadDTO dto = (LetterReadDTO) JsonUtil.json2Bean(buf.toString(), LetterReadDTO.class);
			if(dto != null){
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
				Long rpsId = dto.getSender_id();
				dto.setToken(appcount.getToken());
				dto.setAccountId(appcount.getId());
				dto.setInstitutionId(appcount.getInstitutionId());

				//通过缓存判断该私信是否存在,使用FromUserName + CreateTime 排重
				String msgid = dto.getReceiver_id() + dto.getCreated_at();
				if(RedisUtil.getRedisSet("sina_newMsgid", msgid)){
					logger.info("该私信信息已存在,不做处理!");
					response.getWriter().print("");
				}
				RedisUtil.setRedisSet("sina_newMsgid", msgid);//消息加入缓存
				// 处理私信 判断私信类型
				if("image".equals(dto.getType()) || "text".equals(dto.getType()) || "voice".equals(dto.getType())
						|| "event".equals(dto.getType())){
					LinksusGovMessage message = new LinksusGovMessage();
					Long pid = PrimaryKeyGen.getPrimaryKey("linksus_gov_message", "pid");
					Long inteWeiboId = PrimaryKeyGen.GenerateSerialNum();
					Date createTime = DateUtil.parse(dto.getCreated_at(), "EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
					Integer unixTime = Integer.parseInt(String.valueOf(createTime.getTime() / 1000));
					message.setPid(pid);
					message.setRunId(inteWeiboId);
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
						message.setMsgId(0l);
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
						messageGovService.addInteractReadMessage(message);
					}
				}
			}
		}
	}

	/**
	 * 验证appsecret
	 * @param appsecret
	 * @param req
	 * @return
	 */
	private boolean validateSignature(String appsecret,HttpServletRequest req){
		String signature = req.getParameter("signature");
		String timestamp = req.getParameter("timestamp");
		String nonce = req.getParameter("nonce");
		String echostr = req.getParameter("echostr");
		List list = new ArrayList();
		list.add(appsecret);
		list.add(timestamp);
		list.add(nonce);
		Collections.sort(list);
		StringBuffer buffer = new StringBuffer();
		for(int i = 0; i < list.size(); i++){
			buffer.append(list.get(i));
		}
		String hex = DigestUtils.shaHex(buffer.toString());
		logger.info(">>>>>>>>>>>>>>>>>hex:{}", hex);
		if(signature.equals(hex)){
			logger.info(">>>>>>>>>>>>>>>>>true");
			return true;
		}else{
			LogUtil.saveException(logger, new Exception("新浪URL校验失败!appsecret不匹配!"));
			return false;
		}
	}

	/**
	 * 外部调用接口
	 * 
	 * @param req
	 * @param response
	 * @throws IOException
	 * @throws CacheException
	 */
	private void getAction(HttpServletRequest req,HttpServletResponse response) throws IOException{
		response.setContentType("text/html;charset=UTF-8");
		try{
			LinksusWxValidateService validateService = (LinksusWxValidateService) ContextUtil
					.getBean("linksusWxValidateService");
			String uniqueCode = req.getParameter("uniqueCode");//
			logger.info(">>>>>>>>>>>>>>>>>path:" + req.getRequestURL() + "?" + req.getQueryString());
			logger.info(">>>>>>>>>>>>>>>>>uniqueCode:" + uniqueCode);
			uniqueCode = StringUtil.decodeBase64(uniqueCode);
			LinksusWxValidate val = validateService.getLinksusWxValidateByWxnum(uniqueCode);
			if(val == null){
				LogUtil.saveException(logger, new Exception("新浪URL绑定失败!平台未查询到相应用户!"));
				response.getWriter().print("error");
			}else{
				String token = val.getCode();
				if(validateSignature(token, req)){//绑定成功
					response.getWriter().print(req.getParameter("echostr"));
					val.setStatus(1);
					val.setValidTime(DateUtil.getUnixDate(new Date()));
					validateService.updateLinksusWxValidate(val);
				}
			}
			return;
		}catch (Exception e){
			e.printStackTrace();
			LogUtil.saveException(logger, e);
			response.getWriter().print(StringUtil.getHttpResultForException(e));
		}
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

	private void dealExceptionInvalidRecord(Long institutionId,Long pid,Exception exception){
		try{
			LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
			invalidRecord.setInstitutionId(institutionId);
			invalidRecord.setErrorCode("EXCEPTION");
			invalidRecord.setErrorMsg(LogUtil.getExceptionStackMsg(exception));
			invalidRecord.setOperType(Constants.INVALID_RECORD_INFO);
			invalidRecord.setRecordId(pid);
			BaseTask.dealInvalidRecord(invalidRecord);
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
	}

	private void dealErrorCodeInvalidRecord(Long institutionId,Long pid,String errcode,String rsData){
		try{
			LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
			invalidRecord.setErrorCode(errcode);
			invalidRecord.setErrorMsg(rsData);
			invalidRecord.setInstitutionId(institutionId);
			invalidRecord.setOperType(Constants.INVALID_RECORD_INTERACT_WEIXIN);
			invalidRecord.setRecordId(pid);
			BaseTask.dealInvalidRecord(invalidRecord);
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
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

	// 从新浪得到文件
	public String getSinaLetterFile(String strurl,String param,LinksusGovMessage message){
		String strResult = null;
		HttpURLConnection connection = null;
		//CacheUtil cache = CacheUtil.getInstance();
		//cache.checkConnLimitPerHour();
		try{
			UrlEntity strUrl = LoadConfig.getUrlEntity("letterData");
			logger.info("strUrl :{}", strUrl.getUrl());
			String url = strUrl.getUrl() + param;
			logger.info(">>>>>>>>>>>>>>>>>>>>>url>>>>>>>>>>>>>>>>" + url);
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

	public static void main(String[] args){
		//	StringUtil.decodeBase64(uniqueCode);
	}
}
