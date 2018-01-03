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
 * sina����˽�����ݺ�Ĵ����̣߳���ֹ�ﵽ�ܽӿ����ƺ�����˽�Ŷ�ȡ
 * 
 * @author zhangew
 * 
 */
public class LetterReadDealThread extends BaseTask implements Runnable{

	protected static final Logger logger = LoggerFactory.getLogger(LetterReadDealThread.class);
	/** ������� */
	CacheUtil cache = CacheUtil.getInstance();
	/** ������� */
	private static Queue letterQueue = new ConcurrentLinkedQueue();

	LinksusInteractMessageService messageService = (LinksusInteractMessageService) ContextUtil
			.getBean("linksusInteractMessageService");
	LinksusTaskAttentionUserService userService = (LinksusTaskAttentionUserService) ContextUtil
			.getBean("linksusTaskAttentionUserService");
	LinksusRelationWeibouserService relationWeiboUserService = (LinksusRelationWeibouserService) ContextUtil
			.getBean("linksusRelationWeibouserService");

	/** ��˽�Ŵ������ */
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
						// �ж�˽���Ƿ���� ���ڲ��ٴ���
						if(dto.getId() == null){
							continue;
						}
						//ͨ�������жϸ�˽���Ƿ����
						if(RedisUtil.getRedisSet("sina_msgid", dto.getId())){
							logger.info("��˽����Ϣ�Ѵ���,��������!");
							continue;
						}
						RedisUtil.setRedisSet("sina_msgid", dto.getId());//��Ϣ���뻺��
						//						Integer scount = messageService.getLinksusInteractMessageByMsgId(new Long(dto.getId()));
						//						if(scount.intValue() != 0){
						//							logger.info("��˽����Ϣ�Ѵ���,��������!");
						//							continue;
						//						}
						// ����˽�� �ж�˽������
						if("image".equals(dto.getType()) || "text".equals(dto.getType())
								|| "voice".equals(dto.getType()) || "event".equals(dto.getType())){
							LinksusInteractMessage message = new LinksusInteractMessage();
							Long inteWeiboId = PrimaryKeyGen.getPrimaryKey("linksus_interact_message", "PID");
							Date createTime = DateUtil.parse(dto.getCreated_at(), "EEE MMM dd HH:mm:ss zzz yyyy",
									Locale.US);
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
									message.setMsgId(new Long(dto.getId()));
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
													Long pid = PrimaryKeyGen.getPrimaryKey(
															"linksus_task_attention_user", "pid");
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
												TaskQueueAllSinaFans.addFensiIdInQueue45(rpsId + "",
														dto.getAccountId(), dto.getInstitutionId());
											}else if("unfollow".equals(subtype)){//ȡ����ע
												message.setMsgType(2);
												//�����˻��û���ϵ���� ����:�˺�/�û�ID
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

	public static void main(String[] args){
		String data = "{\"id\":1312240001024632,\"type\":\"image\",\"receiver_id\":1251105081,\"sender_id\":2916398037,\"created_at\":\"Tue Dec 24 16:53:25 +0800 2013\",\"text\":\"����ͼƬ\",\"data\":{\"vfid\":1148918212,\"tovfid\":1148918215}}";
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
