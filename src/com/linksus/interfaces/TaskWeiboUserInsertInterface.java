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

	/** 缓存对象 */
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
		if("1".equals(userType)){//新浪
			LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(dataResult);
			if(error != null){
				rpsMap.put("errcode", error.getErrorCode());
				return rpsMap;
			}
		}else{//腾讯
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
			//加入未分组,我的舆情
			Long id = new Long(institutionId);
			RelationUserAccountCommon.dealPersonGroup(user.getPersonId(), id, "2");
			RelationUserAccountCommon.dealPersonGroup(user.getPersonId(), id, "5");
		}
		return rpsMap;
	}

	/**
	 *  @param  dataResult String 新浪/腾讯返回的用户信息
	 *  @param  serviceType  String  服务类型 1为客户端 2为服务端
	 *  @param  parmMap Map 新浪/腾讯取得的授权信息
	 *  @param  flag flag=true 更新微博用户  false 不更新微博用户
	 *  @param  relationType String 更新用户账户关系  0不更新账户用户表,1为更新粉丝 2为更新我的关注 3为互动
	 *  @return String
	 * @throws Exception 
	 * */
	public LinksusRelationWeibouser dealWeiboUserInfo(String dataResult,String accountType,String tagStr)
			throws Exception{
		LinksusRelationWeibouser user = null;
		Map commonStrMap = new HashMap();
		//用户的头像地址
		String headimgurl = "";
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
			if(StringUtils.equals(accountType, "1")){
				tagsSet = UserTagCommon.getSinaUserTags(tagStr);
			}
		}else if(StringUtils.equals(accountType, "2")){//腾讯数据
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
						LogUtil.saveException(logger, new Exception("-----腾讯标签读取异常:" + "腾讯tagList:" + list + ",返回信息："
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
			//add邢泽江
			linkPerson.setSynctime(DateUtil.getUnixDate(new Date()));
			QueueDataSave.addDataToQueue(linkPerson, Constants.OPER_TYPE_INSERT);
			//linkPersonService.insertLinksusRelationPerson(linkPerson);
			linkWeiboUser.setPersonId(personId);
			linkWeiboUser.setUserId(newUserId);
			linkWeiboUser.setUserType(Integer.valueOf(accountType));
			QueueDataSave.addDataToQueue(linkWeiboUser, Constants.OPER_TYPE_INSERT);
			//relationWeiboUserService.insertLinksusRelationWeibouser(linkWeiboUser);
			user = linkWeiboUser;
			//向用户标签表中插入数据
			if(tagsSet != null && !tagsSet.isEmpty()){
				//直接向用户标签表中插入数据 查询出标签id 
				UserTagCommon.userTagInsert(tagsSet, accountType, String.valueOf(newUserId));
			}
		}else{
			//更新微博用户的详细信息
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
