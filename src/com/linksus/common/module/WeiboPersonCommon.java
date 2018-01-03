package com.linksus.common.module;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.Constants;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.DateUtil;
import com.linksus.common.util.JsonUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.PrimaryKeyGen;
import com.linksus.data.QueueDataSave;
import com.linksus.entity.LinksusRelationPerson;
import com.linksus.entity.LinksusRelationPersonTag;
import com.linksus.entity.LinksusRelationPersonTagdef;
import com.linksus.entity.LinksusRelationWeibouser;
import com.linksus.entity.LinksusRelationWxuser;
import com.linksus.entity.LinksusTypeAreaCode;
import com.linksus.service.LinksusRelationPersonService;
import com.linksus.service.LinksusRelationPersonTagService;
import com.linksus.service.LinksusRelationPersonTagdefService;
import com.linksus.service.LinksusRelationUserTagService;
import com.linksus.service.LinksusRelationUserTagdefService;
import com.linksus.service.LinksusRelationWeibouserService;
import com.linksus.service.LinksusRelationWxuserService;
import com.linksus.service.LinksusTypeAreaCodeService;

public class WeiboPersonCommon{

	/**
	 * ����΢���û���Ϣ�ж��û��Ƿ����
	 * 
	 */
	protected static final Logger logger = LoggerFactory.getLogger(WeiboPersonCommon.class);
	LinksusRelationWeibouserService relationWeiboUserService = (LinksusRelationWeibouserService) ContextUtil
			.getBean("linksusRelationWeibouserService");
	LinksusRelationPersonService linkPersonService = (LinksusRelationPersonService) ContextUtil
			.getBean("linksusRelationPersonService");
	LinksusRelationUserTagService linksusUserTagService = (LinksusRelationUserTagService) ContextUtil
			.getBean("linksusRelationUserTagService");
	LinksusRelationUserTagdefService linksusUserTagdefService = (LinksusRelationUserTagdefService) ContextUtil
			.getBean("linksusRelationUserTagdefService");
	LinksusRelationWxuserService linksusRelationWxuserService = (LinksusRelationWxuserService) ContextUtil
			.getBean("linksusRelationWxuserService");
	LinksusRelationPersonTagdefService linksusRelationPersonTagdefService = (LinksusRelationPersonTagdefService) ContextUtil
			.getBean("linksusRelationPersonTagdefService");
	LinksusRelationPersonTagService linksusRelationPersonTagService = (LinksusRelationPersonTagService) ContextUtil
			.getBean("linksusRelationPersonTagService");
	LinksusTypeAreaCodeService linksusTypeAreaCodeService = (LinksusTypeAreaCodeService) ContextUtil
			.getBean("linksusTypeAreaCodeService");

	/**
	 * @param dataResult
	 *            String ����/��Ѷ���ص��û���Ϣ
	 * @param parmMap
	 *            Map ����/��Ѷȡ�õ���Ȩ��Ϣ
	 * @param flag
	 *            flag=true ����΢���û� false ������΢���û�
	 * @return String
	 * @throws Exception 
	 */
	public String dealWeiboPersonUserList(String dataResult,Map parmMap,Boolean flag) throws Exception{
		Map commonStrMap = new HashMap();
		// �û���ͷ���ַ
		String headimgurl = "";
		// ȡ������accountType
		String accountType = (String) parmMap.get("type");
		// �����û�����ȥ�����ǩ
		Set tagsSet = new HashSet();

		// ��ȡ�û���rpsId
		String rpsId = "";
		// ΢���û��� ���ڸ��ºͲ���
		LinksusRelationWeibouser linkWeiboUser = new LinksusRelationWeibouser();
		if(StringUtils.equals(accountType, "1")){// ��������
			commonStrMap = JsonUtil.json2map(dataResult, Map.class);
			headimgurl = commonStrMap.get("profile_image_url") + "";
			rpsId = String.valueOf(commonStrMap.get("idstr"));
			Map tagParaMap = new HashMap();
			tagParaMap.put("token", parmMap.get("token"));
			tagParaMap.put("uid", rpsId);
			tagsSet = UserTagCommon.getSinaUserTagsByUrl(tagParaMap);
		}else if(StringUtils.equals(accountType, "2")){// ��Ѷ����
			String dataRealStr = JsonUtil.getNodeByName(dataResult, "data");
			commonStrMap = JsonUtil.json2map(dataRealStr, Map.class);
			headimgurl = String.valueOf(commonStrMap.get("head"));
			if(StringUtils.isBlank(headimgurl)){
				headimgurl = "http://mat1.gtimg.com/www/mb/img/p1/head_normal_180.png";
			}
			rpsId = String.valueOf(commonStrMap.get("openid"));
			List<Map> list = (List<Map>) commonStrMap.get("tag");
			tagsSet = UserTagCommon.getTencentUserTags(list);
		}
		// ����rpsId��accountType �ж��û��Ƿ����
		Map paraMap = new HashMap();
		paraMap.put("rpsId", rpsId);
		paraMap.put("userType", accountType);
		LinksusRelationWeibouser user = relationWeiboUserService.getWeibouserByrpsIdAndType(paraMap);
		if(user == null){// ������Ϣ����Ա������
			// ���뵽��Ա����
			Long personId = PrimaryKeyGen.getPrimaryKey("linksus_relation_person", "person_id");
			Long newUserId = PrimaryKeyGen.getPrimaryKey("linksus_relation_weibouser", "user_id");
			Long tagId = PrimaryKeyGen.getPrimaryKey("linksus_relation_user_tag", "tag_id");
			// ��ȡ���� �� ͷ��
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
			linkPerson.setWeixinIds("");
			linkPerson.setAddTime(DateUtil.getUnixDate(new Date()));
			//add����
			linkPerson.setSynctime(DateUtil.getUnixDate(new Date()));
			//linkPersonService.insertLinksusRelationPerson(linkPerson);
			QueueDataSave.addDataToQueue(linkPerson, Constants.OPER_TYPE_INSERT);
			linkWeiboUser.setPersonId(personId);
			linkWeiboUser.setUserId(newUserId);
			linkWeiboUser.setUserType(Integer.valueOf(accountType));
			this.setTagsForWebiUser(tagsSet, linkWeiboUser);
			//relationWeiboUserService.insertLinksusRelationWeibouser(linkWeiboUser);
			QueueDataSave.addDataToQueue(linkWeiboUser, Constants.OPER_TYPE_INSERT);
			// ���û���ǩ���в�������
			UserTagCommon.userTagInsert(tagsSet, accountType, String.valueOf(newUserId));
		}else{
			// ����΢���û�����ϸ��Ϣ
			if(flag){
				linkWeiboUser.setRpsId(rpsId);
				linkWeiboUser.setUserType(Integer.valueOf(accountType));
				Long tagUserId = user.getUserId();
				UserTagCommon.userExsitTagsData(tagsSet, accountType, String.valueOf(tagUserId));
				if(StringUtils.equals(accountType, "1")){
					CopyMapToBean.copySinaUserMapToBean(commonStrMap, linkWeiboUser);
					relationWeiboUserService.updateSinaLinksusRelationWeibouser(linkWeiboUser);
				}else if(StringUtils.equals(accountType, "2")){
					CopyMapToBean.copyTencentUserMapToBean(commonStrMap, linkWeiboUser);
					relationWeiboUserService.updateCententLinksusRelationWeibouser(linkWeiboUser);
				}
			}
		}
		return "success";
	}

	/**
	 * @param dataResult
	 *            String ����/��Ѷ���ص��û���Ϣ
	 * @param parmMap
	 *            Map ����/��Ѷȡ�õ���Ȩ��Ϣ
	 * @param flag
	 *            flag=true ����΢���û� false ������΢���û�
	 * @return String
	 * @throws Exception 
	 */
	public Map dealWeiboPersonUser(String dataResult,Map parmMap,Boolean flag) throws Exception{
		Map commonStrMap = new HashMap();
		Map rsMap = new HashMap();
		// �û���ͷ���ַ
		String headimgurl = "";
		// ȡ������accountType
		String accountType = (String) parmMap.get("type");
		// �����û�����ȥ�����ǩ
		Set tagsSet = new HashSet();
		// ��ȡ�û���rpsId
		String rpsId = "";
		String rs = "1";
		// ΢���û��� ���ڸ��ºͲ���
		LinksusRelationWeibouser linkWeiboUser = new LinksusRelationWeibouser();
		if(StringUtils.equals(accountType, "1")){// ��������
			commonStrMap = JsonUtil.json2map(dataResult, Map.class);
			headimgurl = commonStrMap.get("profile_image_url") + "";
			rpsId = String.valueOf(commonStrMap.get("idstr"));
			Map tagParaMap = new HashMap();
			tagParaMap.put("token", parmMap.get("access_token"));
			tagParaMap.put("uid", rpsId);
			tagsSet = UserTagCommon.getSinaUserTagsByUrl(tagParaMap);
		}else if(StringUtils.equals(accountType, "2")){// ��Ѷ����
			String dataRealStr = JsonUtil.getNodeByName(dataResult, "data");
			commonStrMap = JsonUtil.json2map(dataRealStr, Map.class);
			headimgurl = String.valueOf(commonStrMap.get("head"));
			if(StringUtils.isBlank(headimgurl)){
				headimgurl = "http://mat1.gtimg.com/www/mb/img/p1/head_normal_180.png";
			}
			rpsId = String.valueOf(commonStrMap.get("openid"));
			List<Map> list = (List<Map>) commonStrMap.get("tag");
			tagsSet = UserTagCommon.getTencentUserTags(list);
		}
		// ����rpsId��accountType �ж��û��Ƿ����
		Map paraMap = new HashMap();
		paraMap.put("rpsId", rpsId);
		paraMap.put("userType", accountType);
		rsMap.put("uid", rpsId);
		LinksusRelationWeibouser user = relationWeiboUserService.getWeibouserByrpsIdAndType(paraMap);
		if(user == null){// ������Ϣ����Ա������
			// ���뵽��Ա����
			// Long personId =
			// PrimaryKeyGen.getPrimaryKey("linksus_relation_person",
			// "person_id");
			Long userId = PrimaryKeyGen.getPrimaryKey("linksus_relation_weibouser", "user_id");
			Long tagId = PrimaryKeyGen.getPrimaryKey("linksus_relation_user_tag", "tag_id");
			// ��ȡ���� �� ͷ��
			// LinksusRelationPerson linkPerson=new LinksusRelationPerson();
			// linkPerson.setHeadimgurl(headimgurl);
			// linkPerson.setPersonId(personId);
			// if(StringUtils.equals(accountType, "1")){
			// linkPerson.setSinaIds(String.valueOf(newUserId));
			// linkPerson.setTencentIds("0");
			// CopyMapToBean.copySinaMapToBean(commonStrMap, linkWeiboUser);
			// }else if(StringUtils.equals(accountType, "2")){
			// linkPerson.setSinaIds("0");
			// linkPerson.setTencentIds(String.valueOf(newUserId));
			// CopyMapToBean.copyCententMapToBean(commonStrMap, linkWeiboUser);
			// }
			// linkPerson.setWeixinIds("0");
			// linkPerson.setAddTime(Integer.valueOf(String.valueOf(new
			// Date().getTime()/1000)));
			// linkPersonService.insertLinksusRelationPerson(linkPerson);
			// linkWeiboUser.setPersonId(personId);
			linkWeiboUser.setPersonId(Long.parseLong("0"));
			linkWeiboUser.setUserId(userId);
			linkWeiboUser.setUserType(Integer.valueOf(accountType));
			this.setTagsForWebiUser(tagsSet, linkWeiboUser);
			//relationWeiboUserService.insertLinksusRelationWeibouser(linkWeiboUser);
			QueueDataSave.addDataToQueue(linkWeiboUser, Constants.OPER_TYPE_INSERT);
			// ���û���ǩ���в�������
			UserTagCommon.userTagInsert(tagsSet, accountType, String.valueOf(userId));
			// rs = "1";
			rsMap.put("exist", "1");
		}else{
			// ����΢���û�����ϸ��Ϣ
			if(flag){
				linkWeiboUser.setRpsId(rpsId);
				linkWeiboUser.setUserType(Integer.valueOf(accountType));
				Long tagUserId = user.getUserId();
				UserTagCommon.userExsitTagsData(tagsSet, accountType, String.valueOf(tagUserId));
				if(StringUtils.equals(accountType, "1")){
					CopyMapToBean.copySinaUserMapToBean(commonStrMap, linkWeiboUser);
					relationWeiboUserService.updateSinaLinksusRelationWeibouser(linkWeiboUser);
				}else if(StringUtils.equals(accountType, "2")){
					CopyMapToBean.copyTencentUserMapToBean(commonStrMap, linkWeiboUser);
					relationWeiboUserService.updateCententLinksusRelationWeibouser(linkWeiboUser);
				}
			}
			// rs = "2";
			rsMap.put("exist", "2");
		}
		rsMap.put("linkWeiboUser", linkWeiboUser);
		return rsMap;
	}

	/**
	 * @param dataResult
	 *            String ΢�ŷ��ص��û���Ϣ
	 * @param parmMap
	 *            Map ΢��ȡ�õ���Ȩ��Ϣ
	 * @param flag
	 *            flag=true ����΢���û� false ������΢���û�
	 * @return String
	 * @throws Exception 
	 */
	public Map dealWeixinPersonUser(String dataResult,Map parmMap,Boolean flag) throws Exception{
		// Map commonStrMap = new HashMap();
		Map rsMap = new HashMap();
		// �û���ͷ���ַ
		String headimgurl = "";
		// ȡ������accountType
		String accountType = (String) parmMap.get("type");

		// ΢���û��� ���ڸ��ºͲ���
		LinksusRelationWeibouser linkWeiboUser = new LinksusRelationWeibouser();
		LinksusRelationWxuser entity = new LinksusRelationWxuser();

		Map datamap = this.getWeixinSections(dataResult);
		Map areamap = new HashMap();

		areamap.put("area_name", datamap.get("country").toString());

		String country_code = this.getAreaCode(areamap);

		areamap.put("area_name", datamap.get("province").toString());
		areamap.put("parent_code", country_code);

		String state_code = this.getAreaCode(areamap);

		areamap.put("area_name", datamap.get("city").toString());
		areamap.put("parent_code", state_code);

		String city_code = this.getAreaCode(areamap);

		String nickname = datamap.get("nickname").toString();

		String sex = datamap.get("sex").toString().equals("��") ? "m" : "n";

		int last_updt_time = Integer.valueOf(String.valueOf(new Date().getTime() / 1000));

		datamap.put("sex", sex);

		datamap.put("country", country_code);

		datamap.put("province", state_code);

		datamap.put("city", city_code);

		LinksusRelationWxuser weixinuser = linksusRelationWxuserService.getLinksusRelationWxuserByMap(datamap);
		if(weixinuser == null){// ������ ������

			Long userId = PrimaryKeyGen.getPrimaryKey("linksus_relation_wxuser", "user_id");

			entity.setPersonId(Long.parseLong("0"));
			entity.setUserId(userId);
			entity.setNickname(nickname);
			entity.setSex(sex);
			entity.setCountry(country_code);
			entity.setProvince(state_code);
			entity.setCity(city_code);
			entity.setLanguage("0");
			entity.setLastUpdtTime(last_updt_time);

			linksusRelationWxuserService.insertLinksusRelationWxuser(entity);

			linkWeiboUser.setUserId(userId);
			linkWeiboUser.setUserType(Integer.parseInt(accountType));
			linkWeiboUser.setRpsName(nickname);
			rsMap.put("exist", "1");
		}else{
			// ����΢���û�����ϸ��Ϣ
			if(flag){
				weixinuser.setNickname(nickname);
				weixinuser.setSex(sex);
				weixinuser.setCountry(country_code);
				weixinuser.setProvince(state_code);
				weixinuser.setCity(city_code);
				weixinuser.setLastUpdtTime(last_updt_time);

				linksusRelationWxuserService.updateLinksusRelationWxuser(weixinuser);

				linkWeiboUser.setUserId(weixinuser.getUserId());
				linkWeiboUser.setUserType(Integer.parseInt(accountType));
				linkWeiboUser.setRpsName(nickname);
			}

			rsMap.put("exist", "2");
		}
		rsMap.put("linkWeiboUser", linkWeiboUser);
		return rsMap;
	}

	/**
	 * ���ҵ�����Ϣ
	 * 
	 * @param commonCache
	 * @return
	 */
	public String getAreaCode(Map map){
		String areacode = "";
		try{
			LinksusTypeAreaCode linksusTypeAreaCode = linksusTypeAreaCodeService.getLinksusTypeAreaCode(map);
			areacode = linksusTypeAreaCode.getAreaCode().toString();

		}catch (Exception e){
			LogUtil.saveException(logger, e);

		}
		return areacode;
	}

	// ���߷ָ��ַ����Ľ���
	public Map getWeixinSections(String strs){
		// List lstObject = new ArrayList();
		Map map = new HashMap();
		String[] sts = strs.split("\\|");
		if(null != sts[0]){
			map.put("nickname", sts[0].trim());
		}
		if(null != sts[1]){
			map.put("sex", sts[1].trim());
		}
		if(null != sts[2]){
			map.put("country", sts[2].trim());
		}
		if(null != sts[3]){
			map.put("province", sts[3].trim());
		}
		if(null != sts[4]){
			map.put("city", sts[4].trim());
		}

		return map;
	}

	// ��Ա��ǩ����
	public void actionPersonTagsData(List tagsList,String personId) throws Exception{
		if(tagsList != null && tagsList.size() > 0){
			// ��ѯ��ǩ������tagsList�е�ֵ
			Map tagParaMap = new HashMap();
			tagParaMap.put("tagsList", tagsList);
			// tagParaMap.put("accountType", accountType);
			// �ӱ�ǩ������ȡ����List�еĶ���
			List<LinksusRelationPersonTagdef> personTagdefList = linksusRelationPersonTagdefService
					.getLinksusRelationPersonTagdefList();
			Map tagdefMap = new HashMap();
			Set tagSet = new HashSet();
			for(int i = 0; i < personTagdefList.size(); i++){
				LinksusRelationPersonTagdef personTagdef = new LinksusRelationPersonTagdef();
				tagdefMap.put(personTagdef.getTagName(), personTagdef);
				tagSet.add(personTagdef.getTagName());
			}
			// ɾ��
			// linksusRelationPersonTagService.deleteLinksusRelationPersonTagById(Long.parseLong(""));
			for(int i = 0; i < tagsList.size(); i++){
				String tagStr = (String) tagsList.get(i);
				if(tagSet.contains(tagStr)){
					/** �жϱ�ǩ�Ƿ���� */
					LinksusRelationPersonTagdef personTagdeg = (LinksusRelationPersonTagdef) tagdefMap.get(tagStr);
					// �����û���ǩ��
					LinksusRelationPersonTag personTag = new LinksusRelationPersonTag();
					personTag.setTagId(personTagdeg.getPid());
					personTag.setPersonId(Long.parseLong(personId));
					// personTag.sett(tagStr);
					linksusRelationPersonTagService.insertLinksusRelationPersonTag(personTag);
				}else{
					// �����û���ǩ����ͬʱ�����ǩ��
					Long tagId = PrimaryKeyGen.getPrimaryKey("linksus_relation_person_tagdef", "pid");
					LinksusRelationPersonTagdef personTagdef = new LinksusRelationPersonTagdef();
					personTagdef.setPid(tagId);
					personTagdef.setTagName(tagStr);
					personTagdef.setUseCount(1);
					linksusRelationPersonTagdefService.insertLinksusRelationPersonTagdef(personTagdef);
					LinksusRelationPersonTag personTag = new LinksusRelationPersonTag();
					personTag.setTagId(tagId);
					personTag.setPersonId(Long.parseLong(personId));
					linksusRelationPersonTagService.insertLinksusRelationPersonTag(personTag);
				}
			}
		}
	}
	
	// �û���ǩ����
	public void setTagsForWebiUser(Set tagsList,LinksusRelationWeibouser linkWeiboUser) {
		StringBuffer sBuffer = new StringBuffer();
		if(tagsList != null && tagsList.size() >0) {
			int count = 0;
			for (Object obj: tagsList) {
			      if(obj instanceof String){
			    	  String tag = (String)obj;
			    	  sBuffer.append(tag);
			    	  if(count < tagsList.size()-1) {
			    		  sBuffer.append("|");  
			    	  }
			      } 
			      count++;
			}
			linkWeiboUser.setTags(sBuffer.toString());
		}
	}
}
