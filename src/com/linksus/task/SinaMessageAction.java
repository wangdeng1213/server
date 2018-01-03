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
 * sina˽�Ŷ�ȡ�ظ�
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

	//post���ýӿ�
	private void postAction(HttpServletRequest req,HttpServletResponse response) throws Exception{

		//����url�е�΢�źŲ�ѯ�˺���Ϣ vallid_token
		String uniqueCode = req.getParameter("uniqueCode");
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>uniqueCode:{}", uniqueCode);
		uniqueCode = StringUtil.decodeBase64(uniqueCode);
		LinksusAppaccount appcount = linksusAppaccountService.getAppaccountByAppid(uniqueCode);
		if(appcount == null){
			dealExceptionInvalidRecord(0l, 0l, new Exception("�����˺�:" + uniqueCode + ",δ��ѯ����Ч�˺�!"));
			response.getWriter().print("error");
			logger.error(">>>>>>>>>>>>>>>>>δ��ѯ����Ч�˺�");
			return;
		}
		//����appsecret�ж���Ϣ����Ч��
		String appSecret = LoadConfig.getString("sinaAppSecret");
		if(!validateSignature(appSecret, req)){//��֤ʧ��
			response.getWriter().print("error");
			logger.error(">>>>>>>>>>>>>>>>>appsecretУ��ʧ�� ��Ϣ��Ч");
			//������Ч������¼��
			dealExceptionInvalidRecord(appcount.getInstitutionId(), appcount.getId(), new Exception(
					"appSecretУ��ʧ�� ��Ϣ��Ч!"));
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

				//ͨ�������жϸ�˽���Ƿ����,ʹ��FromUserName + CreateTime ����
				String msgid = dto.getReceiver_id() + dto.getCreated_at();
				if(RedisUtil.getRedisSet("sina_newMsgid", msgid)){
					logger.info("��˽����Ϣ�Ѵ���,��������!");
					response.getWriter().print("");
				}
				RedisUtil.setRedisSet("sina_newMsgid", msgid);//��Ϣ���뻺��
				// ����˽�� �ж�˽������
				if("image".equals(dto.getType()) || "text".equals(dto.getType()) || "voice".equals(dto.getType())
						|| "event".equals(dto.getType())){
					LinksusInteractMessage message = new LinksusInteractMessage();
					Long inteWeiboId = PrimaryKeyGen.getPrimaryKey("linksus_interact_message", "PID");
					Date createTime = DateUtil.parse(dto.getCreated_at(), "EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
					Integer unixTime = Integer.parseInt(String.valueOf(createTime.getTime() / 1000));
					message.setPid(inteWeiboId);
					//�жϷ������Ƿ����
					//long userId = new Long((String)map.get("openid"));
					//���΢���û��Ƿ����
					LinksusRelationWeibouser relationWeibouser = null;
					Map sinaparaMap = new HashMap();
					sinaparaMap.put("access_token", dto.getToken());
					sinaparaMap.put("uid", rpsId);

					String strjson = HttpUtil.getRequest(LoadConfig.getUrlEntity("fensiinfo"), sinaparaMap);
					if(StringUtils.isNotBlank(strjson)){
						LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(strjson);
						if(StringUtils.isNotBlank(strjson) && error == null){//���ط�˿��Ϣ
							//���΢���û��Ƿ����
							String relationType = "3";
							if("event".equals(dto.getType())){//�¼�
								String subtype = dto.getData().getSubtype();
								if("follow".equals(subtype)){//��ӹ�ע
									relationType = "1";
								}
							}
							relationWeibouser = getWeiboUserInfo(strjson, dto, rpsId + "", relationType, 4);
						}
						if(relationWeibouser != null){
							//�������û���������Ա��
							WeiboInteractCommon interactCommon = new WeiboInteractCommon();
							Long recordId = interactCommon.dealWeiboInteract(relationWeibouser.getUserId(),
									relationWeibouser.getPersonId(), relationWeibouser.getAccountId(), Integer
											.valueOf(1), new Long(0), "5", inteWeiboId, unixTime);
							Long userId = relationWeibouser.getUserId();
							message.setUserId(userId);
							message.setRecordId(recordId);
							//����˽�Ż�����Ϣ��
							message.setAccountId(dto.getAccountId());
							message.setContent(dto.getText());
							//message.setMsgId(new Long(dto.getId()));
							message.setMsgId(0l);
							message.setInteractTime(unixTime);
							message.setInteractType(1);//�û�����
							message.setAccountType(1);//����
							if("image".equals(dto.getType())){
								//����ͼƬ/������
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
								message.setMsgType(5);//˽��
								if("event".equals(dto.getType())){//�¼�
									String subtype = dto.getData().getSubtype();
									if("follow".equals(subtype)){//��ӹ�ע
										message.setMsgType(1);
										//�ж��·�˿��������Ƿ����Զ���ע����
										LinksusTaskAttentionUser isTaskAttentionUser = new LinksusTaskAttentionUser();
										isTaskAttentionUser.setAccountId(dto.getAccountId());
										isTaskAttentionUser.setUserId(userId);
										Integer countUsers = userService
												.countLinksusTaskAttentionUser(isTaskAttentionUser);
										if(countUsers == 0){
											//����·�˿�����
											LinksusTaskAttentionUser user = new LinksusTaskAttentionUser();
											Long pid = PrimaryKeyGen
													.getPrimaryKey("linksus_task_attention_user", "pid");
											user.setPid(pid);
											user.setAccountId(dto.getAccountId());
											user.setUserId(userId);
											user.setAccountType(1);//����
											user.setStatus(1);
											user.setInteractTime(unixTime);
											user.setCreateTime(DateUtil.getUnixDate(new Date()));
											userService.insertLinksusTaskAttentionUser(user);
										}
										//����ȫ���������
										TaskQueueAllSinaFans.addFensiIdInQueue45(rpsId + "", dto.getAccountId(), dto
												.getInstitutionId());
									}else if("unfollow".equals(subtype)){//ȡ����ע
										message.setMsgType(2);
										//�����˻��û���ϵ���� ����:�˺�/�û�ID
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
						LogUtil.saveException(logger, new Exception("-----��֯�����������쳣:" + "��֯����gid:" + org.getGid()));
						continue;
					}
					orgMap.put(org.getAccountId(), org);
				}
				Long rpsId = dto.getSender_id();
				dto.setToken(appcount.getToken());
				dto.setAccountId(appcount.getId());
				dto.setInstitutionId(appcount.getInstitutionId());

				//ͨ�������жϸ�˽���Ƿ����,ʹ��FromUserName + CreateTime ����
				String msgid = dto.getReceiver_id() + dto.getCreated_at();
				if(RedisUtil.getRedisSet("sina_newMsgid", msgid)){
					logger.info("��˽����Ϣ�Ѵ���,��������!");
					response.getWriter().print("");
				}
				RedisUtil.setRedisSet("sina_newMsgid", msgid);//��Ϣ���뻺��
				// ����˽�� �ж�˽������
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
					//���΢���û��Ƿ����
					LinksusRelationWeibouser relationWeibouser = null;
					Map sinaparaMap = new HashMap();
					sinaparaMap.put("access_token", dto.getToken());
					sinaparaMap.put("uid", rpsId);

					String strjson = HttpUtil.getRequest(LoadConfig.getUrlEntity("fensiinfo"), sinaparaMap);
					LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(strjson);
					if(StringUtils.isNotBlank(strjson) && error == null){
						//���΢���û��Ƿ����
						relationWeibouser = getWeiboUserInfo(strjson, dto, rpsId + "");
					}
					if(relationWeibouser != null){
						Long userId = relationWeibouser.getUserId();
						Integer gid = orgMap.get(new Integer(dto.getAccountId().intValue())).getGid();
						Integer orgId = orgMap.get(new Integer(dto.getAccountId().intValue())).getOrgId();
						saveGovRunning(inteWeiboId, gid, orgId, userId);

						//����˽�Ż�����Ϣ��
						message.setAccountId(dto.getAccountId());
						message.setContent(dto.getText());
						message.setMsgId(0l);
						message.setInteractTime(unixTime);
						message.setInteractType(1);//�û�����
						if("image".equals(dto.getType())){
							//����ͼƬ/������
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
	 * ��֤appsecret
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
			LogUtil.saveException(logger, new Exception("����URLУ��ʧ��!appsecret��ƥ��!"));
			return false;
		}
	}

	/**
	 * �ⲿ���ýӿ�
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
				LogUtil.saveException(logger, new Exception("����URL��ʧ��!ƽ̨δ��ѯ����Ӧ�û�!"));
				response.getWriter().print("error");
			}else{
				String token = val.getCode();
				if(validateSignature(token, req)){//�󶨳ɹ�
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
	  *  ����΢���û�������΢���û�����Ϣ
	 * @throws Exception 
	  */
	public LinksusRelationWeibouser getWeiboUserInfo(String userStr,LetterReadDTO dto,String rpsId,String relationType,
			int interactBit) throws Exception{
		//String userStr =JsonUtil.map2json(userMap);
		//��ȡ�����û�������appId ��  ҵ�������ж��û��Ƿ����΢���û�����
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

	// �����˵õ��ļ�
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
			// �򿪺�URL֮�������
			connection = (HttpURLConnection) realUrl.openConnection();
			// ����ͨ�õ���������
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// ����ʵ�ʵ�����
			connection.connect();
			// ��ȡ������Ӧͷ�ֶ�
			Map<String, List<String>> map = connection.getHeaderFields();
			String disp = map.get("Content-Disposition").get(0);
			String fileName = disp.substring(disp.indexOf("\"") + 1, disp.lastIndexOf("\""));
			String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
			if(StringUtils.isNotBlank(fileType)){//�����������ļ�ʱ�������˶�ȡ�ļ�
				// �������е���Ӧͷ�ֶ�
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
		// ʹ��finally�����ر�������
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
		govRunning.setIsMultiterm(0);//Ĭ����0 ����Ϊ1
		//��������:1 ���� 2 ת�� 3 @ 4 ���۲�@  5���۲�ת�� 6 ˽�� 7 ƽ̨�˻��ظ�
		govRunning.setInteractType(6);
		govRunning.setInteractMode(2);//������ʽ�� 1 ΢�� 2 ˽��
		govRunning.setUserId(userId);
		govRunning.setUpdateTime(DateUtil.getUnixDate(new Date()));
		linksusGovRunningService.insertLinksusGovRunning(govRunning);
	}

	/**
	  *  ����΢���û�������΢���û�����Ϣ
	 * @throws Exception 
	  */
	public LinksusRelationWeibouser getWeiboUserInfo(String userStr,LetterReadDTO dto,String rpsId) throws Exception{
		//String userStr =JsonUtil.map2json(userMap);
		//��ȡ�����û�������appId ��  ҵ�������ж��û��Ƿ����΢���û�����
		WeiboUserCommon weiboUser = new WeiboUserCommon();
		Map parmMap = new HashMap();
		parmMap.put("token", dto.getToken());
		parmMap.put("type", "1");
		parmMap.put("AccountId", dto.getAccountId() + "");
		parmMap.put("InstitutionId", dto.getInstitutionId());
		weiboUser.dealWeiboUserInfo(userStr, "1", parmMap, false, "3", 6);

		//����rpsId��accountType �ж��û��Ƿ����
		Map paraMap = new HashMap();
		paraMap.put("rpsId", rpsId);
		paraMap.put("userType", "1");
		LinksusRelationWeibouser user = relationWeiboUserService.getWeibouserByrpsIdAndType(paraMap);
		user.setAccountId(dto.getAccountId());
		user.setInstitutionId(dto.getInstitutionId());
		return user;
	}

	// �����˵õ��ļ�
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
			// �򿪺�URL֮�������
			connection = (HttpURLConnection) realUrl.openConnection();
			// ����ͨ�õ���������
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// ����ʵ�ʵ�����
			connection.connect();
			// ��ȡ������Ӧͷ�ֶ�
			Map<String, List<String>> map = connection.getHeaderFields();
			String disp = map.get("Content-Disposition").get(0);
			String fileName = disp.substring(disp.indexOf("\"") + 1, disp.lastIndexOf("\""));
			String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
			// �������е���Ӧͷ�ֶ�
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
		// ʹ��finally�����ر�������
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
