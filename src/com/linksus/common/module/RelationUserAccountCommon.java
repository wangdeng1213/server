package com.linksus.common.module;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.Constants;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.DateUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.PrimaryKeyGen;
import com.linksus.common.util.RedisUtil;
import com.linksus.data.QueueDataSave;
import com.linksus.entity.LinksusRelationGroup;
import com.linksus.entity.LinksusRelationPersonGroup;
import com.linksus.entity.LinksusRelationUserAccount;
import com.linksus.entity.LinksusTaskAttentionUser;
import com.linksus.service.LinksusRelationGroupService;
import com.linksus.service.LinksusRelationPersonGroupService;
import com.linksus.service.LinksusTaskAttentionUserService;

public class RelationUserAccountCommon{

	protected static final Logger logger = LoggerFactory.getLogger(RelationUserAccountCommon.class);
	private static LinksusRelationGroupService linksusRelationGroupService = (LinksusRelationGroupService) ContextUtil
			.getBean("linksusRelationGroupService");
	private static LinksusRelationPersonGroupService linksusRelationPersonGroupService = (LinksusRelationPersonGroupService) ContextUtil
			.getBean("linksusRelationPersonGroupService");
	private static LinksusTaskAttentionUserService userService = (LinksusTaskAttentionUserService) ContextUtil
			.getBean("linksusTaskAttentionUserService");

	/**
	 * 判断账户用户关系表中是否存在记录，存在修改关系状态，否则插入新数据,并加入未分组信息
	 *  @param  accountId String 平台帐号id
	 *  @param  userId  Long  用户id
	 *  @param  personId Long 人员id
	 *  @param  institutionId Long 机构id
	 *  @param  appType String 类型 1为新浪，2为腾讯
	 *  @param  relationType String 类型 1为粉丝，2为我的关注 ，3为互动
	 *  @return int interactBit  0：非互动 1： 纯评论 2:纯转发 3:直接@我 4:评论并@我 5:私信过我'6:微信互动
	 * @throws Exception 
	 * */
	public void dealRelationUserAccount(long accountId,long userId,long personId,long institutionId,String appType,
			String relationType,int interactBit) throws Exception{
		boolean updateFlag=true;
		if(StringUtils.isNotBlank(relationType)){
			//判断该用户和账号是否有关系 有执行修改操作 没有执行插入accountId 
			//			  Map valueMap= new HashMap();
			//			  valueMap.put("accountId",accountId );
			//			  valueMap.put("userId",userId);
			//		      LinksusRelationUserAccount userAccount = linksusUserAccountService.getIsExsitWeiboUserAccount(valueMap);
			//判断账号用户是否存在缓存中
			String strResult = RedisUtil.getRedisHash("relation_user_account", accountId + "#" + userId);
			LinksusRelationUserAccount linksusUserAccount = new LinksusRelationUserAccount();
			if(StringUtils.isBlank(strResult)){//账号和用户没有关系
				Long pid = PrimaryKeyGen.getPrimaryKey("linksus_relation_user_account", "pid");
				linksusUserAccount.setPid(pid);
				linksusUserAccount.setAccountId(accountId);
				linksusUserAccount.setAccountType(Integer.valueOf(appType));
				linksusUserAccount.setUserId(userId);
				linksusUserAccount.setInstitutionId(institutionId);
				linksusUserAccount.setPersonId(personId);
				if(relationType.equals("1")){//新增粉丝关系
					linksusUserAccount.setFlagRelation(1);
					//设置加粉丝时间
					linksusUserAccount.setFansTime(DateUtil.getUnixDate(new Date()));
					if(StringUtils.equals(appType, "2")){
						LinksusTaskAttentionUser user = new LinksusTaskAttentionUser();
						user.setPid(PrimaryKeyGen.getPrimaryKey("linksus_task_attention_user", "pid"));
						user.setAccountId(accountId);
						user.setUserId(userId);
						user.setAccountType(Integer.valueOf(appType));//新浪
						user.setStatus(1);
						user.setInteractTime(0);
						user.setCreateTime(DateUtil.getUnixDate(new Date()));
						//判断新粉丝表中是否存在
						Integer countUser = userService.countLinksusTaskAttentionUser(user);
						if(countUser == 0){
							userService.insertLinksusTaskAttentionUser(user);
						}
					}
				}else if(relationType.equals("2")){//更新我的关注
					linksusUserAccount.setFlagRelation(2);
					//设置关注时间
					linksusUserAccount.setAttentionTime(DateUtil.getUnixDate(new Date()));
				}else if(relationType.equals("3")){//互动为潜在关系
					linksusUserAccount.setFlagRelation(4);
					//设置最后互动时间
					linksusUserAccount.setInteractTime(DateUtil.getUnixDate(new Date()));
				}else{
					linksusUserAccount.setFlagRelation(0);
				}
				String interactType = "000000";
				if(interactBit > 0){
					String start = interactType.substring(0, interactBit - 1);
					String end = interactType.substring(interactBit);
					interactType = start + "1" + end;
				}
				linksusUserAccount.setInteractType(interactType);
				// linksusUserAccountService.insertLinksusRelationUserAccount(linksusUserAccount);
				QueueDataSave.addDataToQueue(linksusUserAccount, Constants.OPER_TYPE_INSERT);
				dealPersonGroup(personId, institutionId, "2");
			}else{
				//判断关系状态 
				//更新微博用户的详细信息
				String[] str = strResult.split("#");
				String flagRe = "0";
				String interactType = "000000";
				if(str != null && str.length >= 2){
					flagRe = str[0]; //用户账号所属关系
					interactType = str[1];
				}
				Integer flagRelation = 0;//定义变量
				if(StringUtils.equals(relationType, "1")){//更新粉丝
					//设置加粉丝时间
					linksusUserAccount.setFansTime(DateUtil.getUnixDate(new Date()));
					if("0".equals(flagRe) || "4".equals(flagRe)){//关系类型是0和4的话
						flagRelation = 1;
						//插入新粉丝表
						if(StringUtils.equals(appType, "2")){
							LinksusTaskAttentionUser user = new LinksusTaskAttentionUser();
							user.setPid(PrimaryKeyGen.getPrimaryKey("linksus_task_attention_user", "pid"));
							user.setAccountId(accountId);
							user.setUserId(userId);
							user.setAccountType(Integer.valueOf(appType));//新浪
							user.setStatus(1);
							user.setInteractTime(0);
							user.setCreateTime(DateUtil.getUnixDate(new Date()));
							//判断新粉丝表中是否存在
							Integer countUser = userService.countLinksusTaskAttentionUser(user);
							if(countUser == 0){
								userService.insertLinksusTaskAttentionUser(user);
							}
						}
					}else if("2".equals(flagRe)){
						if(StringUtils.equals(appType, "2")){
							LinksusTaskAttentionUser user = new LinksusTaskAttentionUser();
							user.setPid(PrimaryKeyGen.getPrimaryKey("linksus_task_attention_user", "pid"));
							user.setAccountId(accountId);
							user.setUserId(userId);
							user.setAccountType(Integer.valueOf(appType));//新浪
							user.setStatus(1);
							user.setInteractTime(0);
							user.setCreateTime(DateUtil.getUnixDate(new Date()));
							//判断新粉丝表中是否存在
							Integer countUser = userService.countLinksusTaskAttentionUser(user);
							if(countUser == 0){
								userService.insertLinksusTaskAttentionUser(user);
							}
						}
						flagRelation = 3;
					}else{
						updateFlag=false;
						//flagRelation = Integer.valueOf(flagRe);
					}
				}else if(StringUtils.equals(relationType, "2")){//更新我的关注
					//设置关注时间
					linksusUserAccount.setAttentionTime(DateUtil.getUnixDate(new Date()));
					if("0".equals(flagRe) || "4".equals(flagRe)){
						flagRelation = 2;
					}else if("1".equals(flagRe)){
						flagRelation = 3;
					}else{
						updateFlag=false;
						//flagRelation = 2;
					}
				}else{
					//设置最后互动时间
					//linksusUserAccount.setInteractTime(DateUtil.getUnixDate(new Date()));
					updateFlag=false;
				}
				if(updateFlag){
					if(interactBit > 0){
						String start = interactType.substring(0, interactBit - 1);
						String end = interactType.substring(interactBit);
						interactType = start + "1" + end;
					}
					//执行更新操作
					linksusUserAccount.setAccountId(accountId);
					linksusUserAccount.setUserId(userId);
					linksusUserAccount.setFlagRelation(flagRelation);
					linksusUserAccount.setInteractType(interactType);
					QueueDataSave.addDataToQueue(linksusUserAccount, Constants.OPER_TYPE_UPDATE);
					// linksusUserAccountService.updateFlagRelationByPid(linksusUserAccount);
				}
			}
		}
	}

	public static void dealPersonGroup(Long personId,Long institutionId,String groupType) throws Exception{
		//判断该机构在分组信息表中是否存在“未分组” "我的舆情"
		String hashKey = "rel_groupdef";
		String fieldKey = institutionId + "#" + groupType;
		String groupIdStr = RedisUtil.getRedisHash(hashKey, fieldKey);
		Long groupId = null;

		//缓存不存在的先查询数据库
		if(StringUtils.isBlank(groupIdStr)){
			Map paramMap = new HashMap();
			paramMap.put("institutionId", institutionId);
			paramMap.put("groupType", groupType);
			LinksusRelationGroup relationGroup = linksusRelationGroupService
					.getPersonGroupInfoByInstIdAndGroupType(paramMap);
			if(relationGroup != null){
				groupId = relationGroup.getGroupId();
				RedisUtil.setRedisHash(hashKey, fieldKey, groupId.toString());
			}
		}else{
			groupId = new Long(groupIdStr);
		}

		if(groupId == null){
			String groupName = null;
			if("2".equals(groupType)){
				groupName = "未分组";
			}else if("4".equals(groupType)){
				groupName = "我添加的";
			}else if("5".equals(groupType)){
				groupName = "我的舆情";
			}
			groupId = PrimaryKeyGen.getPrimaryKey("linksus_relation_group", "group_id");
			//插入"未分组"信息
			LinksusRelationGroup group = new LinksusRelationGroup();
			Calendar cal = Calendar.getInstance();
			String lastUpdateTime = String.valueOf(cal.getTime().getTime() / 1000);
			Integer etime = Integer.parseInt(lastUpdateTime);
			group.setGroupId(groupId);
			group.setInstitutionId(institutionId);
			group.setGroupName(groupName);
			group.setGroupType(new Integer(groupType));
			group.setStatus(2);
			group.setLastUpdateTime(etime);
			try{
				RedisUtil.setRedisHash(hashKey, fieldKey, groupId.toString());
				linksusRelationGroupService.insertNoGroupInfo(group);
			}catch (Exception e){
				RedisUtil.delFieldKey(hashKey, fieldKey);
				LogUtil.saveException(logger, new Exception(group.getGroupName() + "++++++++++++++groupId-----:"
						+ groupId + "personId>>>>>>>>>>>" + personId + ">>>>>>>>>>>institutionId" + institutionId));
			}
		}
		//判断人员分组表是否存在
		Map paramMap = new HashMap();
		paramMap.put("groupId", groupId);
		paramMap.put("personId", personId);
		LinksusRelationPersonGroup rersonGroup = linksusRelationPersonGroupService
				.getLinksusRelationPersonGroupById(paramMap);
		//插入人员分组表
		if(rersonGroup == null){
			LinksusRelationPersonGroup relationPersonGroup = new LinksusRelationPersonGroup();
			Calendar cal = Calendar.getInstance();
			String lastUpdateTime = String.valueOf(cal.getTime().getTime() / 1000);
			Integer etime = Integer.parseInt(lastUpdateTime);
			relationPersonGroup.setGroupId(groupId);
			relationPersonGroup.setGroupSource(2);
			relationPersonGroup.setPersonId(personId);
			relationPersonGroup.setLastUpdtTime(etime);
			linksusRelationPersonGroupService.insertLinksusRelationPersonGroup(relationPersonGroup);
		}
	}

	public static void main(String[] args) throws Exception{
		RelationUserAccountCommon.dealPersonGroup(121l, 10222l, "2");
	}
}
