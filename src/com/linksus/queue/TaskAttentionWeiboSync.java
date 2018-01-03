package com.linksus.queue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.linksus.common.Constants;
import com.linksus.common.config.LoadConfig;
import com.linksus.common.module.WeiboUserCommon;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.HttpUtil;
import com.linksus.common.util.JsonUtil;
import com.linksus.common.util.PrimaryKeyGen;
import com.linksus.common.util.RedisUtil;
import com.linksus.common.util.StringUtil;
import com.linksus.data.QueueDataSave;
import com.linksus.entity.LinksusAppaccount;
import com.linksus.entity.LinksusAttentionWeibo;
import com.linksus.entity.LinksusRelationWeibouser;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.entity.LinksusWeiboDataSync;
import com.linksus.entity.LinksusWeiboPool;
import com.linksus.entity.UrlEntity;
import com.linksus.service.LinksusAppaccountService;
import com.linksus.service.LinksusAttentionWeiboService;
import com.linksus.service.LinksusRelationWeibouserService;
import com.linksus.service.LinksusWeiboDataSyncService;

/**
 * ץȡ��ע�û���΢��
 * */

public class TaskAttentionWeiboSync extends BaseQueue{

	private String accountType;

	private LinksusAttentionWeiboService attWeiboService = (LinksusAttentionWeiboService) ContextUtil
			.getBean("linksusAttentionWeiboService");
	private LinksusWeiboDataSyncService weiboDataSyncService = (LinksusWeiboDataSyncService) ContextUtil
			.getBean("linksusWeiboDataSyncService");
	private LinksusAppaccountService linksusAppaccountService = (LinksusAppaccountService) ContextUtil
			.getBean("linksusAppaccountService");
	private LinksusRelationWeibouserService relationWeiboUserService = (LinksusRelationWeibouserService) ContextUtil
			.getBean("linksusRelationWeibouserService");

	public void setAccountType(String accountType){
		this.accountType = accountType;
	}

	protected TaskAttentionWeiboSync(String taskType) {
		super(taskType);
	}

	@Override
	protected List flushTaskQueue(){
		// �ӷ������˲��÷�ҳȡƽ̨��ע�û���΢���б�
		int currenCount = 0;
		currenCount = (currentPage - 1) * pageSize;
		// ͨ����ҳȡ���û��б�
		LinksusAppaccount platUser = new LinksusAppaccount();
		platUser.setStartCount(currenCount);
		platUser.setPageSize(pageSize);
		platUser.setType(new Integer(accountType));// 1,���� 2,��Ѷ
		List<LinksusAppaccount> appUserlist = linksusAppaccountService.getLinksusAppaccountAttentionList(platUser);
		if(appUserlist.size() < pageSize){// ����ѭ����� �´����¿�ʼ
			currentPage = 1;
			setTaskFinishFlag();
		}else{
			currentPage++;
		}
		return appUserlist;
	}

	@Override
	protected String parseClientData(Map dataMap) throws Exception{
		List<LinksusAttentionWeibo> linksusAttentionWeibolist = (List<LinksusAttentionWeibo>) dataMap
				.get("attentionList");
		List<LinksusWeiboDataSync> linksusWeiboDataSynclist = (List<LinksusWeiboDataSync>) dataMap
				.get("weiboDataSyncList");
		List<LinksusWeiboPool> linksusWeiboPoollist = (List<LinksusWeiboPool>) dataMap.get("weiboSourceList");
		updateTransferWeibo(linksusWeiboPoollist);
		updateAttentionWeibo(linksusAttentionWeibolist);
		for(int i = 0; i < linksusWeiboDataSynclist.size(); i++){
			LinksusWeiboDataSync linksusWeiboDataSync = linksusWeiboDataSynclist.get(i);
			//��ѯ�ٽ���е�����
			Integer dataSyncCount = weiboDataSyncService.getLinksusWeiboDataSyncCount(linksusWeiboDataSync);
			if(dataSyncCount > 0){
				weiboDataSyncService.updateLinksusWeiboDataSyncSingle(linksusWeiboDataSync);
			}else{
				//����
				weiboDataSyncService.insertLinksusWeiboDataSyncSingle(linksusWeiboDataSync);
			}
		}
		return "success";
	}

	//�����ݸ��µ����ݿ����
	public void updateAttentionWeibo(List<LinksusAttentionWeibo> linksusAttentionWeibolist) throws Exception{
		//�����ݸ��µ����ݿ����
		for(int i = 0; i < linksusAttentionWeibolist.size(); i++){
			LinksusAttentionWeibo linksusAttentionWeibo = linksusAttentionWeibolist.get(i);
			//ȡ����ע΢�����û�
			String accountType = linksusAttentionWeibo.getAccountType() + "";
			Long accountId = linksusAttentionWeibo.getAccountId();
			Long institutionId = linksusAttentionWeibo.getInstitutionId();
			String dataResult = linksusAttentionWeibo.getUser();
			WeiboUserCommon weibouser = new WeiboUserCommon();
			Map paramMap = new HashMap();
			paramMap.put("AccountId", String.valueOf(accountId));
			paramMap.put("InstitutionId", institutionId);
			paramMap.put("type", accountType);
			LinksusRelationWeibouser weiboUser = weibouser.dealWeiboUserInfo(dataResult, "1", paramMap, false, "2", 0);
			//�����ע�û�����΢���û����в���
			Map commonStrMap = new HashMap();
			String screenName = "";
			String imageUrl = "";
			if("1".equals(accountType)){
				if(!StringUtils.isBlank(JsonUtil.getNodeValueByName(dataResult, "error_code"))){//���ڴ���
					continue;
				}
				commonStrMap = JsonUtil.json2map(dataResult, Map.class);
				screenName = (String) commonStrMap.get("nick") == null ? "" : (String) commonStrMap.get("nick");
				imageUrl = StringUtils.isBlank((String) commonStrMap.get("head")) ? "http://mat1.gtimg.com/www/mb/img/p1/head_normal_180.png"
						: (String) commonStrMap.get("head") + "/120";
			}else if("2".equals(accountType)){
				LinksusTaskErrorCode error = StringUtil.parseTencentErrorCode(dataResult);
				if(error != null){
					continue;
				}
				String data = JsonUtil.getNodeValueByName(dataResult, "data");
				if(StringUtils.equals(data, "null")){
					continue;
				}

				commonStrMap = JsonUtil.json2map(data, Map.class);
				screenName = commonStrMap.get("screen_name") == null ? "" : commonStrMap.get("screen_name") + "";
				imageUrl = commonStrMap.get("profile_image_url") == null ? "" : commonStrMap.get("profile_image_url")
						+ "";
			}
			Long id = PrimaryKeyGen.getPrimaryKey("linksus_attention_weibo", "id");
			linksusAttentionWeibo.setId(id);
			if(weiboUser != null){
				linksusAttentionWeibo.setUid(weiboUser.getUserId());
				linksusAttentionWeibo.setUname(screenName);
				linksusAttentionWeibo.setUprofileUrl(imageUrl);
			}
			//����weiboid �� �˺�id ���˺������жϹ�ע�û���΢���Ƿ����
			Integer countWeibo = attWeiboService.getAttentionWeiboCount(linksusAttentionWeibo);
			if(countWeibo == 0){
				attWeiboService.insertLinksusAttentionWeibo(linksusAttentionWeibo);
			}
		}
	}

	//ת��΢�����ݸ��µ�΢������
	public void updateTransferWeibo(List<LinksusWeiboPool> linksusWeiboPoollist) throws Exception{
		//ת��΢�����ݸ��µ�΢������
		for(int k = 0; k < linksusWeiboPoollist.size(); k++){
			LinksusWeiboPool weiboPool = linksusWeiboPoollist.get(k);
			String userInfo = "";
			String openId = "";
			String screenName = "";
			String imageUrl = "";
			Long weiboid = weiboPool.getMid();
			Integer weiboType = weiboPool.getWeiboType();
			weiboPool.setPublishType(0);
			Map tokenMap = getAccountTokenMap(accountType);
			if(weiboType == 2){
				openId = weiboPool.getOpenId();
				userInfo = getWeiboUserInfoFromCentent(openId, tokenMap);
				LinksusTaskErrorCode error = StringUtil.parseTencentErrorCode(userInfo);
				if(error != null){
					continue;
				}
				String data = JsonUtil.getNodeValueByName(userInfo, "data");
				if(StringUtils.equals(data, "null")){
					continue;
				}
				Map commonStrMap = JsonUtil.json2map(data, Map.class);
				screenName = commonStrMap.get("screen_name") == null ? "" : commonStrMap.get("screen_name") + "";
				imageUrl = commonStrMap.get("profile_image_url") == null ? "" : commonStrMap.get("profile_image_url")
						+ "";
			}else if(weiboType == 1){
				openId = weiboPool.getOpenId();
				userInfo = weiboPool.getSourceUser();
				if(!StringUtils.isBlank(JsonUtil.getNodeValueByName(userInfo, "error_code"))){//���ڴ���
					continue;
				}
				Map commonStrMap = JsonUtil.json2map(userInfo, Map.class);
				screenName = (String) commonStrMap.get("nick") == null ? "" : (String) commonStrMap.get("nick");
				imageUrl = StringUtils.isBlank((String) commonStrMap.get("head")) ? "http://mat1.gtimg.com/www/mb/img/p1/head_normal_180.png"
						: (String) commonStrMap.get("head") + "/120";
			}
			//���ԭ΢���Ƿ���΢������
			//			Map paraMap =new HashMap();
			//			paraMap.put("mid",weiboid);
			//			paraMap.put("weiboType", weiboType);
			//�߻���
			String weiboRedis = RedisUtil.getRedisHash("weibo_pool", weiboid + "#" + weiboType);
			//LinksusWeiboPool count =linksusWeiboPoolService.checkWeiboPoolIsExsit(paraMap);
			//if(count == null){
			if(StringUtils.isBlank(weiboRedis)){
				//LinksusRelationWeibouser user =checkUpdateWeiboUser(openId,accountType);
				WeiboUserCommon weiboUserObj = new WeiboUserCommon();
				LinksusRelationWeibouser weibouser = weiboUserObj.dealWeiboUserInfo(userInfo, "2", tokenMap, false,
						"0", 0);
				if(weibouser != null){
					//�û�id
					weiboPool.setUid(weibouser.getUserId());
					//�û�����
					weiboPool.setUname(screenName);
					//�û�ͷ��
					weiboPool.setUprofileUrl(imageUrl);
					weiboPool.setWeiboType(Integer.valueOf(accountType));
					QueueDataSave.addDataToQueue(weiboPool, Constants.OPER_TYPE_INSERT);
					// linksusWeiboPoolService.insertLinksusWeiboPool(weiboPool);
				}
			}
		}
	}

	//��ȡ�û��Ļ�����Ϣ ����Ѷ
	public String getWeiboUserInfoFromCentent(String rpsId,Map tokenMap){
		//ͨ�������ļ�����Ѷ�ӿ�ȡ���µ�΢������
		UrlEntity strTencentUrl = LoadConfig.getUrlEntity("TCUerInfo");
		String strjson = "";
		Map paraMap = new HashMap();
		paraMap.put("oauth_consumer_key", cache.getAppInfo(Constants.CACHE_TENCENT_APPINFO).getAppKey());
		paraMap.put("clientip", Constants.TENCENT_CLIENT_IP);
		paraMap.put("oauth_version", "2.a");
		paraMap.put("format", "json");
		paraMap.put("openid", tokenMap.get("openid"));
		paraMap.put("access_token", tokenMap.get("token"));
		paraMap.put("fopenid", rpsId);
		strjson = HttpUtil.getRequest(strTencentUrl, paraMap);
		return strjson;
	}

	//����rpsId �� appType �ж��û��Ƿ� ����΢���û�����
	public LinksusRelationWeibouser checkUpdateWeiboUser(String rpsId,String appType){
		Map paraMap = new HashMap();
		paraMap.put("rpsId", rpsId);
		paraMap.put("userType", appType);
		//��ѯuserid��ͬ��ʱ��sysTime ����һ������
		LinksusRelationWeibouser user = relationWeiboUserService.getWeibouserByrpsIdAndType(paraMap);
		return user;
	}

}
