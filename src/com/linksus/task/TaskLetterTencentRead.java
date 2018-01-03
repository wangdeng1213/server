package com.linksus.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linksus.common.Constants;
import com.linksus.common.config.LoadConfig;
import com.linksus.common.module.WeiboInteractCommon;
import com.linksus.common.module.WeiboUserCommon;
import com.linksus.common.util.CacheUtil;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.HttpUtil;
import com.linksus.common.util.JsonUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.PrimaryKeyGen;
import com.linksus.common.util.StringUtil;
import com.linksus.entity.LinksusAppaccount;
import com.linksus.entity.LinksusInteractMessage;
import com.linksus.entity.LinksusRelationWeibouser;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.entity.LinksusTaskWeiboInteract;
import com.linksus.entity.UrlEntity;
import com.linksus.service.LinksusAppaccountService;
import com.linksus.service.LinksusInteractMessageService;
import com.linksus.service.LinksusInteractWeiboService;
import com.linksus.service.LinksusRelationWeibouserService;
import com.linksus.service.LinksusTaskWeiboInteractService;

/**
 * ��Ѷ˽�Ŷ�ȡ����
 */
public class TaskLetterTencentRead extends BaseTask{

	//URL��ַ
	private UrlEntity readUrl = LoadConfig.getUrlEntity("TCLetterRead");
	//�������
	private CacheUtil cache = CacheUtil.getInstance();

	private LinksusAppaccountService accountService = (LinksusAppaccountService) ContextUtil
			.getBean("linksusAppaccountService");
	private LinksusTaskWeiboInteractService taskService = (LinksusTaskWeiboInteractService) ContextUtil
			.getBean("linksusTaskWeiboInteractService");
	private LinksusInteractMessageService messageService = (LinksusInteractMessageService) ContextUtil
			.getBean("linksusInteractMessageService");
	private LinksusRelationWeibouserService relationWeiboUserService = (LinksusRelationWeibouserService) ContextUtil
			.getBean("linksusRelationWeibouserService");
	private LinksusInteractWeiboService linksusInteractWeiboService = (LinksusInteractWeiboService) ContextUtil
			.getBean("linksusInteractWeiboService");

	@Override
	public void cal(){
		boolean firstRead = true;
		//
		int startCount = (currentPage - 1) * pageSize;
		LinksusAppaccount linksusAppaccount = new LinksusAppaccount();
		linksusAppaccount.setStartCount(startCount);
		linksusAppaccount.setPageSize(pageSize);
		linksusAppaccount.setInteractType(6);//��Ѷ˽��
		List<LinksusAppaccount> accounts = accountService.getTencentInteractAppaccount(linksusAppaccount);
		//���Կ�ʼ
		/*
		 * List<LinksusAppaccount> accounts=new ArrayList();
		 * linksusAppaccount.setId(0l);
		 * linksusAppaccount.setMaxId(366287021626918l);
		 * linksusAppaccount.setPagetime(1392644410l);
		 * linksusAppaccount.setAppid("E4DA005947F49E50E0CF9B7940F47855");
		 * linksusAppaccount.setToken("7b1fd10ef99877894f48d92e95bbe450");
		 * accounts.add(linksusAppaccount);
		 */
		//���Խ���

		Map mapPara = new HashMap();
		mapPara.put("format", "json");
		mapPara.put("pageflag", "2");
		mapPara.put("reqnum", "2");
		mapPara.put("oauth_consumer_key", cache.getAppInfo(Constants.CACHE_TENCENT_APPINFO).getAppKey());
		mapPara.put("clientip", Constants.TENCENT_CLIENT_IP);
		mapPara.put("oauth_version", "2.a");
		for(LinksusAppaccount account : accounts){
			//if(account.getId() == 110){
			if(account.getMaxId() != null && account.getPagetime() != null){//�Ѿ�����˽�Ŷ�ȡ��¼
				firstRead = false;
			}
			mapPara.put("access_token", account.getToken());
			mapPara.put("openid", account.getAppid());
			if(!firstRead){//����˽�Ŷ�ȡ��¼
				mapPara.put("lastid", account.getMaxId() + "");
				mapPara.put("pagetime", account.getPagetime() + "");
			}else{
				mapPara.put("lastid", "0");
				mapPara.put("pagetime", "0");
				mapPara.put("pageflag", "0");
			}
			boolean msgFlag = false;
			String lastid = "";
			String pagetime = "";
			LinksusTaskWeiboInteract interact = new LinksusTaskWeiboInteract();
			//������Ѷ�˺�,��ȡ˽��
			while (true){
				try{
					String rsData = HttpUtil.getRequest(readUrl, mapPara);
					LinksusTaskErrorCode error = StringUtil.parseTencentErrorCode(rsData);
					if(error != null){//���ڴ���
						LogUtil.saveException(logger, new Exception("-----��Ѷ˽�Ŷ�ȡ�쳣:" + "��Ѷopenid:" + account.getAppid()
								+ ",������Ϣ��" + rsData));
						break;
					}
					if(JsonUtil.getNodeByName(rsData, "data") == "null"){//û������
						break;
					}
					String info = JsonUtil.getNodeByName(JsonUtil.getNodeByName(rsData, "data"), "info");

					List dataList = JsonUtil.json2list(info, Map.class);
					for(int i = 0; i < dataList.size(); i++){
						Map map = (Map) dataList.get(i);
						if(!msgFlag && i == 0){//
							interact.setMaxId(new Long((String) map.get("id")));
							interact.setPagetime(new Long(map.get("timestamp") + ""));
							msgFlag = true;
						}
						if(!firstRead && i == 0){//�ǵ�һ�ζ�ȡ ���Ϸ�ҳ  ��һ����¼��Ϊ��ҳ
							mapPara.put("pageflag", "2");
							lastid = (String) map.get("id");
							pagetime = map.get("timestamp") + "";
						}else if(firstRead && i == dataList.size() - 1){//��һ�ζ�ȡ,���·�ҳ ���һ����Ϊ��ҳ
							mapPara.put("pageflag", "1");
							lastid = (String) map.get("id");
							pagetime = map.get("timestamp") + "";
						}
						Long msgId = new Long((String) map.get("id"));
						// �ж�˽���Ƿ���� ���ڲ��ٴ���
						Integer scount = messageService.getLinksusInteractMessageByMsgId(msgId);
						if(scount.intValue() != 0){
							logger.info("��˽����Ϣ�Ѵ���,��������!");
							continue;
						}
						LinksusInteractMessage message = new LinksusInteractMessage();
						Long inteWeiboId = PrimaryKeyGen.getPrimaryKey("linksus_interact_message", "pid");
						message.setPid(inteWeiboId);
						message.setAccountId(account.getId());
						message.setAccountType(2);//��Ѷ
						message.setContent((String) map.get("origtext"));
						message.setMsgId(msgId);
						message.setInteractTime((Integer) map.get("timestamp"));
						message.setInteractType(1);//�û�����
						message.setImgName("");
						message.setAttatch("");
						message.setAttatchName("");
						//����ͼƬ
						if(map.get("image") != null){
							//List tempList=new ArrayList();
							//List tempList1=new ArrayList();
							String imgs = "";
							String tempImgs = "";
							List<String> imgList = (List) map.get("image");
							int start = 0;
							for(String image : imgList){
								if(start == 0){
									imgs = image + "/160";
									tempImgs = image + "/2000";
								}else{
									imgs = imgs + "," + image + "/160";
									tempImgs = tempImgs + "," + image + "/2000";
								}
								start++;
								//tempList.add(image+"/160");
								//tempList1.add(image+"/2000");
							}
							message.setImg(tempImgs);
							message.setImgThumb(imgs);
						}else{
							message.setImg("");
							message.setImgThumb("");
						}

						//�жϷ������Ƿ����
						//long userId = new Long((String)map.get("openid"));
						//���΢���û��Ƿ����
						LinksusRelationWeibouser user = null;
						Map tencentparaMap = new HashMap();
						tencentparaMap.put("oauth_consumer_key", cache.getAppInfo(Constants.CACHE_TENCENT_APPINFO)
								.getAppKey());
						tencentparaMap.put("clientip", Constants.TENCENT_CLIENT_IP);
						tencentparaMap.put("oauth_version", "2.a");
						tencentparaMap.put("format", "json");
						tencentparaMap.put("access_token", account.getToken());
						tencentparaMap.put("openid", account.getAppid());
						tencentparaMap.put("fopenid", map.get("openid").toString());

						String strjson = HttpUtil.getRequest(LoadConfig.getUrlEntity("TCUerInfo"), tencentparaMap);
						error = StringUtil.parseTencentErrorCode(strjson);
						if(error == null){
							String userinfo = JsonUtil.getNodeByName(strjson, "data");
							Map userInfoMap = JsonUtil.json2map(userinfo, Map.class);
							if(userInfoMap.size() > 0){
								String rpsId = (String) userInfoMap.get("openid");
								//���΢���û��Ƿ����
								user = getWeiboUserInfo(strjson, account, rpsId);
							}
						}
						if(user != null){
							message.setUserId(user.getUserId());
							//�ж�˽���Ƿ���˽�Ż�����Ϣ��
							Map privateParams = new HashMap();
							privateParams.put("user_id", user.getUserId());
							privateParams.put("account_id", account.getId());
							privateParams.put("account_type", 2);
							privateParams.put("msg_id", (String) map.get("id"));
							privateParams.put("interact_type", 1);
							LinksusInteractMessage interactMessage = messageService.getMessageIsExists(privateParams);
							if(interactMessage == null){
								WeiboInteractCommon interactCommon = new WeiboInteractCommon();
								Long recordId = interactCommon.dealWeiboInteract(user.getUserId(), user.getPersonId(),
										user.getAccountId(), Integer.valueOf(2), new Long(0), "5", inteWeiboId, Integer
												.parseInt(map.get("timestamp").toString()));
								message.setRecordId(recordId);
								message.setMsgType(5);
								//����˽�Ż�����Ϣ��
								messageService.addInteractReadMessage(message);
							}
						}
					}
					if("1".equals(JsonUtil.getNodeValueByName(JsonUtil.getNodeByName(rsData, "data"), "hasnext"))){//��ȡ���
						break;
					}
					mapPara.put("lastid", lastid);
					mapPara.put("pagetime", pagetime);
				}catch (Exception e){
					LogUtil.saveException(logger, e);
				}
			}
			if(msgFlag){//��ȡ��˽��
				interact.setAccountId(account.getId());
				interact.setInteractType(6);//��Ѷ˽��
				//���»��������¼��
				if(!firstRead){//�Ѿ�����˽�Ŷ�ȡ��¼
					//����
					taskService.updateLinksusTaskWeiboInteract(interact);
				}else{
					//���� 
					taskService.insertLinksusTaskWeiboInteract(interact);
				}
			}
			//}
		}
		checkTaskListEnd(accounts);//�ж������Ƿ���ѯ���
	}

	/**
	 *  ����΢���û�������΢���û�����Ϣ
	* @throws Exception 
	 */
	public LinksusRelationWeibouser getWeiboUserInfo(String userStr,LinksusAppaccount appaccount,String rpsId)
			throws Exception{
		//String userStr =JsonUtil.map2json(userMap);
		//��ȡ�����û�������appId ��  ҵ�������ж��û��Ƿ����΢���û�����
		WeiboUserCommon weiboUser = new WeiboUserCommon();
		Map parmMap = new HashMap();
		parmMap.put("token", appaccount.getToken());
		parmMap.put("type", "2");
		parmMap.put("AccountId", appaccount.getId() + "");
		parmMap.put("InstitutionId", appaccount.getInstitutionId());
		LinksusRelationWeibouser user = weiboUser.dealWeiboUserInfo(userStr, "2", parmMap, false, "3", 5);

		//����rpsId��accountType �ж��û��Ƿ����
		/*
		 * Map paraMap =new HashMap(); paraMap.put("rpsId",rpsId);
		 * paraMap.put("userType","2"); LinksusRelationWeibouser user =
		 * relationWeiboUserService.getWeibouserByrpsIdAndType(paraMap);
		 */
		user.setAccountId(appaccount.getId());
		user.setInstitutionId(appaccount.getInstitutionId());
		return user;
	}

	public static void main(String[] args){
		TaskLetterTencentRead read = new TaskLetterTencentRead();
		read.cal();
	}
}
