package com.linksus.interfaces;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.jcs.access.exception.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.Constants;
import com.linksus.common.config.LoadConfig;
import com.linksus.common.module.RelationUserAccountCommon;
import com.linksus.common.module.WeiboUserCommon;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.HttpUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.StringUtil;
import com.linksus.entity.LinksusAppaccount;
import com.linksus.entity.LinksusRelationWeibouser;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.entity.UrlEntity;
import com.linksus.service.LinksusRelationGroupService;
import com.linksus.service.LinksusRelationPersonGroupService;
import com.linksus.service.LinksusRelationWeibouserService;

/**
 * �������û����˺ŵĹ�ϵ�ӿ�
 */
public class TaskSearchUserAddInterface extends BaseInterface{

	protected static final Logger logger = LoggerFactory.getLogger(TaskSearchUserAddInterface.class);
	/** ������� */
	private LinksusRelationWeibouserService relationWeiboUserService = (LinksusRelationWeibouserService) ContextUtil
			.getBean("linksusRelationWeibouserService");
	private LinksusRelationGroupService linksusRelationGroupService = (LinksusRelationGroupService) ContextUtil
			.getBean("linksusRelationGroupService");
	private LinksusRelationPersonGroupService linksusRelationPersonGroupService = (LinksusRelationPersonGroupService) ContextUtil
			.getBean("linksusRelationPersonGroupService");

	public Map cal(Map paramsMap) throws Exception{
		TaskSearchUserAddInterface task = new TaskSearchUserAddInterface();
		Map rsMap = task.searchUserDealInterface(paramsMap);
		return rsMap;
	}

	// ǰ̨�ӿڵ��÷���
	public Map searchUserDealInterface(Map paraMap) throws Exception{
		Map rsMap = new HashMap();
		String rpsId = (String) paraMap.get("rpsId");
		String userType = (String) paraMap.get("userType");
		String institutionId = (String) paraMap.get("institutionId");
		// ��ѯ΢���û�
		Map tokenMap = getAccountTokenMap(userType);
		// ����rpsId��userType �ж��û��Ƿ����
		Map parasMap = new HashMap();
		parasMap.put("rpsId", rpsId);
		parasMap.put("userType", userType);
		LinksusRelationWeibouser user = relationWeiboUserService.getWeibouserByrpsIdAndType(parasMap);
		if(StringUtils.equals(userType, "1")){// �û�����1������ȡ���û���Ϣ
			if(user == null){// �û������� ��ƽ̨��ѯ�û�����ϸ��Ϣ
				String strData = getWeiboUserDataFromSina(paraMap, tokenMap);// ���˷��ؽ��
				LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(strData);
				if(error != null){
					rsMap.put("errcode", error.getErrorCode().toString());
				}else{
					WeiboUserCommon weiboUserCommon = new WeiboUserCommon();
					user = weiboUserCommon.dealWeiboUserInfo(strData, "2", tokenMap, false, "0", 0);
					dealGroupInfo(user, institutionId);
				}
			}else{
				dealGroupInfo(user, institutionId);
			}
		}else if(StringUtils.equals(userType, "2")){// �û����͵���2����Ѷȡ������
			if(user == null){// �û������� ��ƽ̨��ѯ�û�����ϸ��Ϣ
				String strData = getWeiboUserDataFromTencent(paraMap, tokenMap);
				LinksusTaskErrorCode error = StringUtil.parseTencentErrorCode(strData);
				if(error != null){
					rsMap.put("errcode", error.getErrorCode());
				}else{
					WeiboUserCommon weiboUserCommon = new WeiboUserCommon();
					user = weiboUserCommon.dealWeiboUserInfo(strData, "2", tokenMap, false, "0", 0);
					dealGroupInfo(user, institutionId);
				}
			}else{
				dealGroupInfo(user, institutionId);
			}
		}
		if(user != null){
			rsMap.put("person_id", user.getPersonId());
		}
		return rsMap;
	}

	protected Map getAccountTokenMap(String accountType){
		Map tokenMap = null;
		try{
			Map accountMap = cache.getAccountTokenMap();
			LinksusAppaccount tokenObj = (LinksusAppaccount) accountMap.get(accountType);
			if(tokenObj != null){// ����Ӧ��Ȩ
				tokenMap = new HashMap();
				tokenMap.put("token", tokenObj.getToken());
				tokenMap.put("openid", tokenObj.getAppid());
				tokenMap.put("type", accountType);
				tokenMap.put("appkey", tokenObj.getAppKey());
			}
		}catch (CacheException e){
			LogUtil.saveException(logger, e);
		}
		return tokenMap;
	}

	// ���ݻ��� ΢���û���������ӵķ�����Ϣ
	private void dealGroupInfo(LinksusRelationWeibouser user,String institutionId) throws Exception{
		if(user != null){
			Long id = new Long(institutionId);
			RelationUserAccountCommon.dealPersonGroup(user.getPersonId(), id, "2");//����δ����
			RelationUserAccountCommon.dealPersonGroup(user.getPersonId(), id, "4");//�����ҵ����
		}
	}

	// ��ȡ�û��Ļ�����Ϣ ����Ѷ
	public String getWeiboUserDataFromTencent(Map parmMap,Map tokenMap){
		// ͨ�������ļ�����Ѷ�ӿ�ȡ���µ�΢������
		UrlEntity strTencentUrl = LoadConfig.getUrlEntity("TCUerInfo");
		String strjson = "";
		Map paraMap = new HashMap();
		paraMap.put("oauth_consumer_key", cache.getAppInfo(Constants.CACHE_TENCENT_APPINFO).getAppKey());
		paraMap.put("clientip", Constants.TENCENT_CLIENT_IP);
		paraMap.put("oauth_version", "2.a");
		paraMap.put("format", "json");
		paraMap.put("openid", tokenMap.get("openid"));
		paraMap.put("access_token", tokenMap.get("token"));
		if(!StringUtils.isBlank((String) parmMap.get("rpsId"))){
			paraMap.put("fopenid", parmMap.get("rpsId"));
		}
		if(!StringUtils.isBlank((String) parmMap.get("name"))){
			paraMap.put("name", parmMap.get("name"));
		}
		strjson = HttpUtil.getRequest(strTencentUrl, paraMap);
		return strjson;
	}

	// ��ȡ�û��Ļ�����Ϣ ������
	public String getWeiboUserDataFromSina(Map parmMap,Map tokenMap){
		String strjson = "";
		UrlEntity strUrl = LoadConfig.getUrlEntity("fensiinfo");
		parmMap.get("rpsId");
		Map paraMap = new HashMap();
		paraMap.put("access_token", tokenMap.get("token"));
		paraMap.put("uid", parmMap.get("rpsId"));
		strjson = HttpUtil.getRequest(strUrl, paraMap);
		return strjson;
	}
}
