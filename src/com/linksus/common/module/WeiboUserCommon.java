package com.linksus.common.module;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.Constants;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.DateUtil;
import com.linksus.common.util.JsonUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.PrimaryKeyGen;
import com.linksus.common.util.RedisUtil;
import com.linksus.data.QueueDataSave;
import com.linksus.entity.LinksusRelationPerson;
import com.linksus.entity.LinksusRelationUserCompany;
import com.linksus.entity.LinksusRelationUserEdu;
import com.linksus.entity.LinksusRelationWeibouser;
import com.linksus.queue.QueueFactory;
import com.linksus.service.LinksusRelationUserCompanyService;
import com.linksus.service.LinksusRelationUserEduService;
import com.linksus.service.LinksusRelationWeibouserService;

public class WeiboUserCommon{

	protected static final Logger logger = LoggerFactory.getLogger(WeiboUserCommon.class);
	private LinksusRelationWeibouserService relationWeiboUserService = (LinksusRelationWeibouserService) ContextUtil
			.getBean("linksusRelationWeibouserService");
	private LinksusRelationUserEduService linksusRelationUserEduService = (LinksusRelationUserEduService) ContextUtil
			.getBean("linksusRelationUserEduService");
	private LinksusRelationUserCompanyService linksusRelationUserCompanyService = (LinksusRelationUserCompanyService) ContextUtil
			.getBean("linksusRelationUserCompanyService");

	/**
	 *  @param  LinksusRelationWeibouser weibouser 新浪/腾讯返回的用户信息
	 *  @param  parmMap Map 新浪/腾讯取得的账号id 和 机构id
	 *  @param  flag=true 更新微博用户  false 不更新微博用户
	 *  @param  relationType String 更新用户账户关系  0不更新账户用户表,1为更新粉丝 2为更新我的关注 3为互动
	 *  @param int interactBit 0：非互动 1： 纯评论 2:纯转发 3:直接@我 4:评论并@我 5:私信过我'6:微信互动
	 * @throws Exception 
	 * */
	public LinksusRelationWeibouser paserWeiboUserInfo(LinksusRelationWeibouser weibouser,Map parmMap,Boolean flag,
			String relationType,int interactBit) throws Exception{
		LinksusRelationWeibouser user = null;
		//用户的头像地址
		String headimgurl = weibouser.getRpsProfileImageUrl();
		//获取用户的rpsId
		String rpsId = weibouser.getRpsId();
		Integer weiboType = weibouser.getUserType();
		//微博用户类 用于更新和插入
		String strResult = RedisUtil.getRedisHash("relation_weibouser", rpsId);
		if(StringUtils.isBlank(strResult)){//复制信息到人员主表中
			//插入到人员表中
			Long personId = PrimaryKeyGen.getPrimaryKey("linksus_relation_person", "person_id");
			Long newUserId = PrimaryKeyGen.getPrimaryKey("linksus_relation_weibouser", "user_id");
			//获取主键 和 头像
			LinksusRelationPerson linkPerson = new LinksusRelationPerson();
			linkPerson.setHeadimgurl(headimgurl);
			linkPerson.setPersonId(personId);
			if(weiboType == 1){
				linkPerson.setSinaIds(String.valueOf(newUserId));
				linkPerson.setTencentIds("");
			}else if(weiboType == 2){
				linkPerson.setSinaIds("");
				linkPerson.setTencentIds(String.valueOf(newUserId));
			}
			linkPerson.setPersonName(weibouser.getRpsScreenName());
			linkPerson.setGender(weibouser.getRpsGender());
			linkPerson.setBirthDay(weibouser.getBirthDay());
			linkPerson.setLocation(weibouser.getRpsLocation());
			linkPerson.setCountryCode(weibouser.getCountryCode());
			linkPerson.setStateCode(weibouser.getRpsProvince());
			linkPerson.setCityCode(weibouser.getRpsCity());
			linkPerson.setWeixinIds("");
			linkPerson.setAddTime(Integer.valueOf(String.valueOf(new Date().getTime() / 1000)));
			linkPerson.setSynctime(Integer.valueOf(String.valueOf(new Date().getTime() / 1000)));
			QueueDataSave.addDataToQueue(linkPerson, Constants.OPER_TYPE_INSERT);
			//linkPersonService.insertLinksusRelationPerson(linkPerson);
			weibouser.setPersonId(personId);
			weibouser.setUserId(newUserId);
			QueueDataSave.addDataToQueue(weibouser, Constants.OPER_TYPE_INSERT);
			//relationWeiboUserService.insertLinksusRelationWeibouser(linkWeiboUser);
			user = weibouser;
			if(!relationType.equals("0")){
				//向账户用户关系表中插入数据
				Long accountId = new Long(parmMap.get("AccountId").toString());
				Long institutionId = new Long(parmMap.get("InstitutionId").toString());
				RelationUserAccountCommon relationUserAccountCommon = new RelationUserAccountCommon();
				relationUserAccountCommon.dealRelationUserAccount(accountId, newUserId, personId, institutionId,
						weiboType.toString(), relationType, interactBit);
			}
			//用户不存在将用户信息放到队列中进行更新用户标签表
			if(weiboType == 1){
				LinksusRelationWeibouser newUser = new LinksusRelationWeibouser();
				newUser.setUserId(user.getUserId());
				newUser.setRpsId(rpsId);
				try{
					QueueFactory.addQueueTaskData("Queue015", newUser);
				}catch (Exception e){
					LogUtil.saveException(logger, e);
				}
			}else if(weiboType == 2){//腾讯的标签不走缓存
				List tagsList = (List) weibouser.getTagList();
				Set tagsSet = new HashSet();
				if(tagsList != null && tagsList.size() > 0){
					for(int i = 0; i < tagsList.size(); i++){
						tagsSet.add(tagsList.get(i));
					}
				}
				UserTagCommon.userTagInsert(tagsSet, weiboType.toString(), String.valueOf(newUserId));
				insertEduInfoCompInfo(user);
			}
		}else{
			//更新微博用户的详细信息
			String[] str = strResult.split("#");
			String userId = str[0];
			String personId = str[1];
			weibouser.setUserId(new Long(userId));
			weibouser.setPersonId(new Long(personId));
			user = weibouser;
			if(flag){
				Set tagsSet = weibouser.getTagSet();
				if(tagsSet != null && tagsSet.size() > 0){
					UserTagCommon.userExsitTagsData(tagsSet, weiboType.toString(), userId);
				}
				logger.info("update user before>>>>>>>>>>>>>>>userId:{}>>>>>>>>>>>>>>>>person_id:{}", user.getUserId(),
						user.getPersonId());
				if(weiboType == 1){
					QueueDataSave.addDataToQueue(weibouser, "updateSinaWeiboUser");
					// relationWeiboUserService.updateSinaLinksusRelationWeibouser(weibouser);
				}else if(weiboType == 2){
					QueueDataSave.addDataToQueue(weibouser, "updateCententWeiboUser");
					// relationWeiboUserService.updateCententLinksusRelationWeibouser(weibouser);
					updateEduInfoCompInfo(weibouser);
				}
			}
			if(!relationType.equals("0")){
				//向账户用户关系表中插入数据
				Long accountId = new Long(parmMap.get("AccountId").toString());
				Long institutionId = new Long(parmMap.get("InstitutionId").toString());
				RelationUserAccountCommon relationUserAccountCommon = new RelationUserAccountCommon();
				relationUserAccountCommon.dealRelationUserAccount(accountId, new Long(userId), new Long(personId),
						institutionId, weiboType.toString(), relationType, interactBit);
			}
		}
		return user;
	}

	/**
	 *  @param  dataResult String 新浪/腾讯返回的用户信息
	 *  @param  serviceType  String  服务类型 1为客户端 2为服务端
	 *  @param  parmMap Map 新浪/腾讯取得的授权信息
	 *  @param  flag flag=true 更新微博用户  false 不更新微博用户
	 *  @param  relationType String 更新用户账户关系  0不更新账户用户表,1为更新粉丝 2为更新我的关注 3为互动
	 *  @param int interactBit 0：非互动 1： 纯评论 2:纯转发 3:直接@我 4:评论并@我 5:私信过我'6:微信互动
	 *  @return String
	 * @throws Exception 
	 * */
	public LinksusRelationWeibouser dealWeiboUserInfo(String dataResult,String serviceType,Map parmMap,Boolean flag,
			String relationType,int interactBit) throws Exception{
		LinksusRelationWeibouser user = null;
		Map commonStrMap = new HashMap();
		//用户的头像地址
		String headimgurl = "";
		//取出参数accountType 
		String accountType = parmMap.get("type").toString();
		//新浪用户单独去处理标签
		Set tagsSet = null;
		//获取用户的rpsId
		String rpsId = "";
		//微博用户类 用于更新和插入
		LinksusRelationWeibouser linkWeiboUser = new LinksusRelationWeibouser();
		if(StringUtils.equals(accountType, "1")){//新浪数据
			commonStrMap = JsonUtil.json2map(dataResult, Map.class);
			headimgurl = commonStrMap.get("profile_image_url") + "";
			rpsId = String.valueOf(commonStrMap.get("idstr"));
			if(StringUtils.equals(serviceType, "1")){
				tagsSet = (Set) commonStrMap.get("tagsSet");
			}else{
				Map tagParaMap = new HashMap();
				tagParaMap.put("token", parmMap.get("token"));
				tagParaMap.put("uid", rpsId);
				//tagsSet = UserTagCommon.getSinaUserTagsByUrl(tagParaMap);
			}
		}else if(StringUtils.equals(accountType, "2")){//腾讯数据
			String dataRealStr = JsonUtil.getNodeByName(dataResult, "data");
			if(dataRealStr != null){
				commonStrMap = JsonUtil.json2map(dataRealStr, Map.class);
			}else{
				commonStrMap = JsonUtil.json2map(dataResult, Map.class);
			}
			headimgurl = commonStrMap.get("head") == null ? "" : commonStrMap.get("head") + "";
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
						LogUtil.saveException(logger, new Exception("-----腾讯用户标签读取异常:" + "腾讯tagList:" + list + ",返回信息："
								+ commonStrMap));
					}
				}
			}
		}
		//判断用户是否存在缓存中
		String strResult = RedisUtil.getRedisHash("relation_weibouser", rpsId);
		if(StringUtils.isBlank(strResult)){//复制信息到人员主表中
			//插入到人员表中
			Long personId = PrimaryKeyGen.getPrimaryKey("linksus_relation_person", "person_id");
			Long newUserId = PrimaryKeyGen.getPrimaryKey("linksus_relation_weibouser", "user_id");
			//获取主键 和 头像
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
			linkPerson.setSynctime(DateUtil.getUnixDate(new Date()));
			QueueDataSave.addDataToQueue(linkPerson, Constants.OPER_TYPE_INSERT);
			//linkPersonService.insertLinksusRelationPerson(linkPerson);
			linkWeiboUser.setPersonId(personId);
			linkWeiboUser.setUserId(newUserId);
			linkWeiboUser.setUserType(Integer.valueOf(accountType));
			QueueDataSave.addDataToQueue(linkWeiboUser, Constants.OPER_TYPE_INSERT);
			//relationWeiboUserService.insertLinksusRelationWeibouser(linkWeiboUser);
			user = linkWeiboUser;
			if(!relationType.equals("0")){
				//向账户用户关系表中插入数据
				Long accountId = new Long(parmMap.get("AccountId").toString());
				Long institutionId = new Long(parmMap.get("InstitutionId").toString());
				RelationUserAccountCommon relationUserAccountCommon = new RelationUserAccountCommon();
				relationUserAccountCommon.dealRelationUserAccount(accountId, newUserId, personId, institutionId,
						accountType, relationType, interactBit);
			}
			//用户不存在将用户信息放到队列中进行更新用户标签表
			if(StringUtils.equals(accountType, "1") && StringUtils.equals(serviceType, "1")){
				LinksusRelationWeibouser newUser = new LinksusRelationWeibouser();
				newUser.setUserId(user.getUserId());
				newUser.setRpsId(rpsId);
				//加入增量粉丝标签任务队列
				if(newUser != null){
					try{
						QueueFactory.addQueueTaskData("Queue015", newUser);
					}catch (Exception e){
						LogUtil.saveException(logger, e);
					}
				}
			}else{//腾讯的标签不走缓存
				UserTagCommon.userTagInsert(tagsSet, accountType, String.valueOf(newUserId));
				insertEduInfoCompInfo(user);
			}
		}else{
			//更新微博用户的详细信息
			String[] str = strResult.split("#");
			String userId = str[0];
			String personId = str[1];
			user = new LinksusRelationWeibouser();
			user.setUserId(new Long(userId));
			if(StringUtils.equals(accountType, "1")){
				CopyMapToBean.copySinaUserMapToBean(commonStrMap, user);
				user.setUserType(1);
				user.setPersonId(new Long(personId));
			}else if(StringUtils.equals(accountType, "2")){
				CopyMapToBean.copyTencentUserMapToBean(commonStrMap, user);
				user.setUserType(2);
				user.setPersonId(new Long(personId));
			}
			if(flag){
				/*
				 * linkWeiboUser.setRpsId(rpsId);
				 * linkWeiboUser.setUserType(Integer.valueOf(accountType));
				 */
				UserTagCommon.userExsitTagsData(tagsSet, accountType, userId);
				logger.info("update user before>>>>>>>>>>>>>>>userId:{}>>>>>>>>>>>>>>>>person_id:{}", user.getUserId(),
						user.getPersonId());
				if(StringUtils.equals(accountType, "1")){
					relationWeiboUserService.updateSinaLinksusRelationWeibouser(user);
				}else if(StringUtils.equals(accountType, "2")){
					relationWeiboUserService.updateCententLinksusRelationWeibouser(user);
					updateEduInfoCompInfo(user);
				}
			}
			if(!relationType.equals("0")){
				//向账户用户关系表中插入数据
				Long accountId = new Long(parmMap.get("AccountId").toString());
				Long institutionId = new Long(parmMap.get("InstitutionId").toString());
				RelationUserAccountCommon relationUserAccountCommon = new RelationUserAccountCommon();
				relationUserAccountCommon.dealRelationUserAccount(accountId, new Long(userId), new Long(personId),
						institutionId, accountType, relationType, interactBit);
			}
		}
		return user;
	}

	/**
	 * 更新教育信息和教育信息
	 * @param dataResult
	 * @param user
	 * @throws Exception
	 */
	public void updateEduInfoCompInfo(LinksusRelationWeibouser user) throws Exception{
		linksusRelationUserCompanyService.deleteLinksusRelationUserCompanyByUserId(user.getUserId());
		linksusRelationUserEduService.deleteLinksusRelationUserEduByUserId(user.getUserId());
		insertEduInfoCompInfo(user);
	}

	/**
	 * 腾讯新增职业信息和教育信息
	 * @param dataResult 
	 * @param user
	 * @throws Exception
	 */
	public void insertEduInfoCompInfo(LinksusRelationWeibouser user) throws Exception{
		//add by hb
		List eduList = user.getEduList();
		List compList = user.getCompList();
		if(eduList != null && eduList.size() > 0){
			for(int i = 0; i < eduList.size(); i++){
				Map eduMap = (Map) eduList.get(i);
				Long eduPid = PrimaryKeyGen.getPrimaryKey("linksus_relation_user_edu", "pid");
				Integer departmentid = eduMap.get("departmentid") == null ? 0 : Integer.valueOf(eduMap.get(
						"departmentid").toString());
				String id = eduMap.get("id") == null ? "0" : eduMap.get("id").toString();
				String level = eduMap.get("level") == null ? "0" : eduMap.get("level").toString();
				Integer schoolid = eduMap.get("schoolid") == null ? 0 : Integer.valueOf(eduMap.get("schoolid")
						.toString());
				Integer year = eduMap.get("year") == null ? 0 : Integer.valueOf(eduMap.get("year").toString());
				StringBuffer str = new StringBuffer();
				if(departmentid != 0 && schoolid != 0){
					str.append(schoolid).append("_").append(departmentid);
				}
				LinksusRelationUserEdu eduInfo = new LinksusRelationUserEdu();
				eduInfo.setPid(eduPid);
				eduInfo.setOpenid(user.getRpsId());
				eduInfo.setDepartmentid(str.toString());
				eduInfo.setId(id);
				eduInfo.setLevel(level);
				eduInfo.setSchoolid(schoolid);
				eduInfo.setYear(year);
				eduInfo.setAddTime(DateUtil.getUnixDate(new Date()));
				eduInfo.setUserId(user.getUserId());
				QueueDataSave.addDataToQueue(eduInfo, Constants.OPER_TYPE_INSERT);
				//					linksusRelationUserEduService.insertLinksusRelationUserEdu(eduInfo);	
			}

		}
		if(compList != null && compList.size() > 0){
			for(int i = 0; i < compList.size(); i++){
				Map compMap = (Map) compList.get(i);
				Long companyPid = PrimaryKeyGen.getPrimaryKey("linksus_relation_user_company", "pid");
				Integer beginYear = compMap.get("begin_year") == null ? 0 : Integer.valueOf(compMap.get("begin_year")
						.toString());
				Integer endYear = compMap.get("end_year") == null ? 0 : Integer.valueOf(compMap.get("end_year")
						.toString());
				String companyName = compMap.get("company_name") == null ? "0" : compMap.get("company_name").toString();
				String departmentName = compMap.get("department_name") == null ? "0" : compMap.get("department_name")
						.toString();
				String companyId = compMap.get("id") == null ? "0" : compMap.get("id").toString();
				LinksusRelationUserCompany companyInfo = new LinksusRelationUserCompany();
				companyInfo.setPid(companyPid);
				companyInfo.setOpenid(user.getRpsId());
				companyInfo.setBeginYear(beginYear);
				companyInfo.setEndYear(endYear);
				companyInfo.setCompanyName(companyName);
				companyInfo.setDepartmentName(departmentName);
				companyInfo.setCompanyId(companyId);
				companyInfo.setAddTime(DateUtil.getUnixDate(new Date()));
				companyInfo.setUserId(user.getUserId());
				QueueDataSave.addDataToQueue(companyInfo, Constants.OPER_TYPE_INSERT);
				//					linksusRelationUserCompanyService.insertLinksusRelationUserCompany(companyInfo);	
			}

		}
	}

	public static void main(String[] args) throws HttpException,IOException{
		//		String ssw ="[{\"id\":1212323,\"weight\":123123},{\"id\":1805091337,\"weight\":123}]";
		//		List list =JsonUtil.json2list(ssw, Map.class);
		//		String value="";
		//		List userTagsList =new ArrayList();
		//		for(int i=0;i<list.size();i++){
		//			Map map =(Map)list.get(i);
		//			 Set keySet= map.keySet();
		//	           Iterator iterator = keySet.iterator();
		//	           while(iterator.hasNext()) {
		//	                   String key = (String)iterator.next();
		//	                   if(!StringUtils.equals(key, "weight")){
		//	                	   value = map.get(key)+"";
		//	                   }
		//	           }
		//	           userTagsList.add(value);
		//		}
		HttpClient client = new HttpClient();
		HttpMethod get = new GetMethod(
				"https://open.t.qq.com/api/user/other_info?name=x521xnn&format=json&access_token=4a5b2dcb27324f93883e13bcc9a68b2b&oauth_consumer_key=801058005&openid=D3FC4A3E387A09313DB451A6CF65C28D&oauth_version=2.a&clientip=219.142.25.154&scope=all");
		client.executeMethod(get);
		String dataResult = get.getResponseBodyAsString();
		//String dataResult=FileUtil.readFile("c://myweibo.json");
		WeiboUserCommon common = new WeiboUserCommon();
		Map map = new HashMap();
		map.put("token", "ab8c358c6f9ad3b0998a3d04d051c5a9");
		map.put("type", 2);
		map.put("status", 1);
		map.put("InstitutionId", 1017);
		try{
			//	common.dealWeiboUserInfo(dataResult, "2", map, true, "0");
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
