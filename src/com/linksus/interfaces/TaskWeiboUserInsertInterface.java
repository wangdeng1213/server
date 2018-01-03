package com.linksus.interfaces;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.Constants;
import com.linksus.common.module.CopyMapToBean;
import com.linksus.common.module.RelationUserAccountCommon;
import com.linksus.common.module.UserTagCommon;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.DateUtil;
import com.linksus.common.util.JsonUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.PrimaryKeyGen;
import com.linksus.common.util.RedisUtil;
import com.linksus.common.util.StringUtil;
import com.linksus.data.QueueDataSave;
import com.linksus.entity.LinksusRelationPerson;
import com.linksus.entity.LinksusRelationWeibouser;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.service.LinksusRelationWeibouserService;

public class TaskWeiboUserInsertInterface extends BaseInterface{

	/** ������� */
	protected static final Logger logger = LoggerFactory.getLogger(TaskWeiboUserInsertInterface.class);
	private LinksusRelationWeibouserService relationWeiboUserService = (LinksusRelationWeibouserService) ContextUtil
			.getBean("linksusRelationWeibouserService");

	public Map cal(Map paramsMap) throws Exception{
		Map rpsMap = new HashMap();
		TaskWeiboUserInsertInterface userInserttask = new TaskWeiboUserInsertInterface();
		String dataResult = StringUtil.utf8CharFilter((String) paramsMap.get("userJsonData"));
		String userType = (String) paramsMap.get("userType");
		String tagStr = StringUtil.utf8CharFilter((String) paramsMap.get("tagStr"));
		String institutionId = (String) paramsMap.get("institutionId");
		if(StringUtils.equals(userType, "1") && StringUtils.isBlank(tagStr)){
			rpsMap.put("errcode", "20021");
			return rpsMap;
		}
		if("1".equals(userType)){//����
			LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(dataResult);
			if(error != null){
				rpsMap.put("errcode", error.getErrorCode());
				return rpsMap;
			}
		}else{//��Ѷ
			LinksusTaskErrorCode error = StringUtil.parseTencentErrorCode(dataResult);
			if(error != null){
				rpsMap.put("errcode", error.getErrorCode());
				return rpsMap;
			}
		}
		LinksusRelationWeibouser user = userInserttask.dealWeiboUserInfo(dataResult, userType, tagStr);
		if(user != null){
			rpsMap.put("userId", user.getUserId());
			rpsMap.put("personId", user.getPersonId());
		}
		if(institutionId != null){
			//����δ����,�ҵ�����
			Long id = new Long(institutionId);
			RelationUserAccountCommon.dealPersonGroup(user.getPersonId(), id, "2");
			RelationUserAccountCommon.dealPersonGroup(user.getPersonId(), id, "5");
		}
		return rpsMap;
	}

	/**
	 *  @param  dataResult String ����/��Ѷ���ص��û���Ϣ
	 *  @param  serviceType  String  �������� 1Ϊ�ͻ��� 2Ϊ�����
	 *  @param  parmMap Map ����/��Ѷȡ�õ���Ȩ��Ϣ
	 *  @param  flag flag=true ����΢���û�  false ������΢���û�
	 *  @param  relationType String �����û��˻���ϵ  0�������˻��û���,1Ϊ���·�˿ 2Ϊ�����ҵĹ�ע 3Ϊ����
	 *  @return String
	 * @throws Exception 
	 * */
	public LinksusRelationWeibouser dealWeiboUserInfo(String dataResult,String accountType,String tagStr)
			throws Exception{
		LinksusRelationWeibouser user = null;
		Map commonStrMap = new HashMap();
		//�û���ͷ���ַ
		String headimgurl = "";
		//�����û�����ȥ�����ǩ
		Set tagsSet = null;
		//��ȡ�û���rpsId
		String rpsId = "";
		//΢���û��� ���ڸ��ºͲ���
		LinksusRelationWeibouser linkWeiboUser = new LinksusRelationWeibouser();
		if(StringUtils.equals(accountType, "1")){//��������
			commonStrMap = JsonUtil.json2map(dataResult, Map.class);
			headimgurl = commonStrMap.get("profile_image_url") + "";
			rpsId = String.valueOf(commonStrMap.get("idstr"));
			if(StringUtils.equals(accountType, "1")){
				tagsSet = UserTagCommon.getSinaUserTags(tagStr);
			}
		}else if(StringUtils.equals(accountType, "2")){//��Ѷ����
			String dataRealStr = JsonUtil.getNodeByName(dataResult, "data");
			if(!StringUtils.isBlank(dataRealStr)){
				commonStrMap = JsonUtil.json2map(dataRealStr, Map.class);
			}else{
				commonStrMap = JsonUtil.json2map(dataResult, Map.class);
			}
			try{
				headimgurl = commonStrMap.get("head") == null ? "" : String.valueOf(commonStrMap.get("head"));
			}catch (Exception e){
				LogUtil.saveException(logger, new Exception("headimgurl>>>>>>>>>" + headimgurl + "commonStrMap>>>>>>>>"
						+ commonStrMap));
			}
			if(!StringUtils.isBlank(headimgurl)){
				headimgurl = headimgurl + "/120";
			}else{
				headimgurl = "http://mat1.gtimg.com/www/mb/img/p1/head_normal_180.png";
			}
			rpsId = String.valueOf(commonStrMap.get("openid"));
			Object tagObj = commonStrMap.get("tag");
			if(tagObj instanceof List){
				if(commonStrMap.get("tag") != null){
					List<Map> list = null;
					try{
						list = (List<Map>) commonStrMap.get("tag");
						tagsSet = UserTagCommon.getTencentUserTags(list);
					}catch (Exception e){
						LogUtil.saveException(logger, new Exception("-----��Ѷ��ǩ��ȡ�쳣:" + "��ѶtagList:" + list + ",������Ϣ��"
								+ commonStrMap));
					}
				}
			}
		}
		//�ж��û��Ƿ���ڻ�����
		String strResult = RedisUtil.getRedisHash("relation_weibouser", rpsId);
		if(StringUtils.isBlank(strResult)){//������Ϣ����Ա������
			//���뵽��Ա����
			Long personId = PrimaryKeyGen.getPrimaryKey("linksus_relation_person", "person_id");
			Long newUserId = PrimaryKeyGen.getPrimaryKey("linksus_relation_weibouser", "user_id");
			//��ȡ���� �� ͷ��
			LinksusRelationPerson linkPerson = new LinksusRelationPerson();
			linkPerson.setHeadimgurl(headimgurl);
			linkPerson.setPersonId(personId);
			if(StringUtils.equals(accountType, "1")){
				linkPerson.setSinaIds(String.valueOf(newUserId));
				linkPerson.setTencentIds("");
				CopyMapToBean.copySinaUserMapToBean(commonStrMap, linkWeiboUser);
			}else if(StringUtils.equals(accountType, "2")){
				linkPerson.setSinaIds("");
				linkPerson.setTencentIds(String.valueOf(newUserId));
				CopyMapToBean.copyTencentUserMapToBean(commonStrMap, linkWeiboUser);
			}
			linkPerson.setPersonName(linkWeiboUser.getRpsScreenName());
			linkPerson.setGender(linkWeiboUser.getRpsGender());
			linkPerson.setBirthDay(linkWeiboUser.getBirthDay());
			linkPerson.setLocation(linkWeiboUser.getRpsLocation());
			linkPerson.setCountryCode(linkWeiboUser.getCountryCode());
			linkPerson.setStateCode(linkWeiboUser.getRpsProvince());
			linkPerson.setCityCode(linkWeiboUser.getRpsCity());
			linkPerson.setWeixinIds("");
			linkPerson.setAddTime(DateUtil.getUnixDate(new Date()));
			//add����
			linkPerson.setSynctime(DateUtil.getUnixDate(new Date()));
			QueueDataSave.addDataToQueue(linkPerson, Constants.OPER_TYPE_INSERT);
			//linkPersonService.insertLinksusRelationPerson(linkPerson);
			linkWeiboUser.setPersonId(personId);
			linkWeiboUser.setUserId(newUserId);
			linkWeiboUser.setUserType(Integer.valueOf(accountType));
			QueueDataSave.addDataToQueue(linkWeiboUser, Constants.OPER_TYPE_INSERT);
			//relationWeiboUserService.insertLinksusRelationWeibouser(linkWeiboUser);
			user = linkWeiboUser;
			//���û���ǩ���в�������
			if(tagsSet != null && !tagsSet.isEmpty()){
				//ֱ�����û���ǩ���в������� ��ѯ����ǩid 
				UserTagCommon.userTagInsert(tagsSet, accountType, String.valueOf(newUserId));
			}
		}else{
			//����΢���û�����ϸ��Ϣ
			String[] str = strResult.split("#");
			String userId = str[0];
			String personId = str[1];
			user = new LinksusRelationWeibouser();
			UserTagCommon.userExsitTagsData(tagsSet, accountType, userId);
			if(StringUtils.equals(accountType, "1")){
				CopyMapToBean.copySinaUserMapToBean(commonStrMap, user);
				user.setUserId(new Long(userId));
				user.setPersonId(new Long(personId));
				user.setUserType(1);
				relationWeiboUserService.updateSinaLinksusRelationWeibouser(user);
			}else if(StringUtils.equals(accountType, "2")){
				CopyMapToBean.copyTencentUserMapToBean(commonStrMap, user);
				user.setUserId(new Long(userId));
				user.setPersonId(new Long(personId));
				user.setUserType(2);
				relationWeiboUserService.updateCententLinksusRelationWeibouser(user);
			}
		}
		return user;
	}
}
