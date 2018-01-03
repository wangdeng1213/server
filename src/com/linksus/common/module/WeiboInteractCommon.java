package com.linksus.common.module;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.DateUtil;
import com.linksus.common.util.PrimaryKeyGen;
import com.linksus.common.util.RedisUtil;
import com.linksus.data.InteractCountSave;
import com.linksus.data.QueueDataSave;
import com.linksus.entity.LinksusInteractPerson;
import com.linksus.entity.LinksusInteractRecord;
import com.linksus.entity.LinksusInteractUser;
import com.linksus.service.LinksusInteractPersonService;
import com.linksus.service.LinksusInteractRecordService;
import com.linksus.service.LinksusInteractUserService;
import com.linksus.service.LinksusRelationUserAccountService;

public class WeiboInteractCommon{

	/**
	 * 微博互动表的关系
	 * 
	 */
	protected static final Logger logger = LoggerFactory.getLogger(WeiboInteractCommon.class);
	private LinksusInteractRecordService linksusInteractRecordService = (LinksusInteractRecordService) ContextUtil
			.getBean("linksusInteractRecordService");
	private LinksusInteractPersonService linksusInteractPersonService = (LinksusInteractPersonService) ContextUtil
			.getBean("linksusInteractPersonService");
	private LinksusInteractUserService linksusInteractUserService = (LinksusInteractUserService) ContextUtil
			.getBean("linksusInteractUserService");
	private LinksusRelationUserAccountService linksusRelationUserAccountService = (LinksusRelationUserAccountService) ContextUtil
			.getBean("linksusRelationUserAccountService");

	/**
	 *  @param  userId Long类型 微博用户id 
	 *  @param  personId微博用户所属人员id
	 *  @param  accountId Long类型 用户互动的账号id 
	 *  @param  accountType 账号类型 新浪 腾讯 微信
	 *  @param  weiboId   Long类型 微博id
	 *  @param  interactType String类型 互动类型 
	 *  @param  messageId Long 互动消息Id
	 *  @param  interactTime Integer 互动时间 从接口返回的互动时间 
	 *  @return String
	 * @throws Exception 
	 * */
	public Long dealWeiboInteract(Long userId,Long personId,Long accountId,Integer accountType,Long weiboId,
			String interactType,Long messageId,Integer interactTime) throws Exception{
		//判断微博互动记录表中是否存在记录 
		LinksusInteractRecord interactRecord = new LinksusInteractRecord();
		interactRecord.setUserId(userId);
		interactRecord.setAccountId(accountId);
		if(!StringUtils.equals(interactType, "5") && !StringUtils.equals(interactType, "6")){
			interactRecord.setWeiboId(weiboId);
		}else{
			interactRecord.setWeiboId(0L);
		}
		LinksusInteractRecord linksusInteractRecord = linksusInteractRecordService.checkIsExsitRecord(interactRecord);
		Integer updateTime = DateUtil.getUnixDate(new Date());//更新时间
		Long recordId = 0L;
		if(linksusInteractRecord != null){//存在更新互动记录表
			//更新微博互动记录表中的数据
			LinksusInteractRecord recordData = new LinksusInteractRecord();
			recordId = linksusInteractRecord.getRecordId();
			//logger.debug(">>>>>>>>>>>>>>>>>>>>recordId>>>>>>>>>>>:" + recordId);
			recordData.setRecordId(recordId);
			recordData.setInteractType(Integer.valueOf(interactType));
			recordData.setUpdateTime(updateTime);
			recordData.setMessageId(messageId);
			recordData.setNewMsgFlag(1);
			//linksusInteractRecordService.updateLinksusInteractRecord(recordData);
			QueueDataSave.addDataToQueue(recordData, "update");
		}else{
			//插入新的互动记录数据
			LinksusInteractRecord recordData = new LinksusInteractRecord();
			recordId = PrimaryKeyGen.getPrimaryKey("linksus_interact_record", "record_id");//互动记录表主键
			recordData.setRecordId(recordId);
			recordData.setUserId(userId);
			recordData.setAccountId(accountId);
			recordData.setAccountType(accountType);
			recordData.setInteractType(Integer.valueOf(interactType));
			recordData.setWeiboId(weiboId);
			recordData.setFakeFlag(new Integer(0));
			recordData.setNewMsgFlag(1);
			recordData.setUpdateTime(updateTime);
			recordData.setMessageId(messageId);
			linksusInteractRecordService.insertLinksusInteractRecord(recordData);
		}
		//检查互动人员表中的数据是否存在
		LinksusInteractPerson linksusInteractPerson = new LinksusInteractPerson();
		linksusInteractPerson.setPersonId(personId);
		linksusInteractPerson.setAccountId(accountId);
		LinksusInteractPerson interactPerson = linksusInteractPersonService
				.checkDataIsExsitInteractPerson(linksusInteractPerson);
		if(interactPerson == null){
			//向互动人员表中插入数据
			Long inteInteractId = PrimaryKeyGen.getPrimaryKey("linksus_interact_person", "interact_id");
			linksusInteractPerson.setInteractId(inteInteractId);
			linksusInteractPerson.setAccountType(accountType);
			linksusInteractPerson.setCount(1);//未处理互动数为1 
			linksusInteractPerson.setLastInteractTime(interactTime);//新浪评论时间
			linksusInteractPerson.setLastRecordId(recordId);//互动记录id
			linksusInteractPerson.setUpdateTime(updateTime);//更新时间
			linksusInteractPersonService.insertLinksusInteractPerson(linksusInteractPerson);
			//向互动用户表中插入数据
			LinksusInteractUser interactUser = new LinksusInteractUser();
			Long inteUserId = PrimaryKeyGen.getPrimaryKey("linksus_interact_user", "pid");
			interactUser.setPid(inteUserId);
			interactUser.setInteractId(inteInteractId);
			interactUser.setPersonId(personId);
			interactUser.setAccountId(accountId);
			interactUser.setUserId(userId);
			interactUser.setCount(1);
			interactUser.setUpdateTime(Integer.valueOf(String.valueOf(new Date().getTime() / 1000)));
			linksusInteractUserService.insertLinksusInteractUser(interactUser);
		}else{
			Long personInteractid = interactPerson.getInteractId();
			//人员和账号有互动的话  更新互动人员表中的信息 
			linksusInteractPerson.setLastInteractTime(interactTime);//新浪评论时间
			linksusInteractPerson.setLastRecordId(recordId);//互动记录id
			linksusInteractPerson.setUpdateTime(updateTime);//更新时间
			linksusInteractPerson.setInteractId(personInteractid);
			//linksusInteractPersonService.updateLinksusInteractPerson(linksusInteractPerson);
			QueueDataSave.addDataToQueue(linksusInteractPerson, "update");
			//取出人员互动id和userid查互动用户表有没有记录 有的话更新 没有的话插入
			LinksusInteractUser interactUser = new LinksusInteractUser();
			interactUser.setInteractId(personInteractid);
			interactUser.setUserId(userId);
			LinksusInteractUser linksusInteractUser = linksusInteractUserService.checkUserDataIsExsit(interactUser);
			if(linksusInteractUser != null){
				//更新互动 用户表
				LinksusInteractUser linksusInteract = new LinksusInteractUser();
				linksusInteract.setPid(linksusInteractUser.getPid());
				linksusInteract.setUpdateTime(updateTime);
				//linksusInteractUserService.updateLinksusInteractUser(linksusInteract);
				QueueDataSave.addDataToQueue(linksusInteract, "update");
			}else{
				//插入互动用户表  
				Long inteUserId = PrimaryKeyGen.getPrimaryKey("linksus_interact_user", "pid");
				LinksusInteractUser linksusInteract = new LinksusInteractUser();
				linksusInteract.setPid(inteUserId);
				linksusInteract.setInteractId(personInteractid);
				linksusInteract.setPersonId(personId);
				linksusInteract.setAccountId(accountId);
				linksusInteract.setUserId(userId);
				linksusInteract.setCount(1);
				linksusInteract.setUpdateTime(updateTime);
				linksusInteractUserService.insertLinksusInteractUser(linksusInteract);
			}
		}
		//			 //更新互动次数处理互动来源
		//			 Map valueMap= new HashMap();
		//			 valueMap.put("accountId",accountId);
		//			 valueMap.put("userId",userId);
		//			 LinksusRelationUserAccount userAccount = linksusRelationUserAccountService.getIsExsitWeiboUserAccount(valueMap);
		//判断账号用户是否存在缓存中
		String strResult = RedisUtil.getRedisHash("relation_user_account", accountId + "#" + userId);
		if(!StringUtils.isBlank(strResult)){//账号和用户有关系
			String[] str = strResult.split("#");
			String interactTypeSource = str[1];
			Integer inType = new Integer(interactType);
			if(inType > 0 && inType <= 6){//第1位 纯评论 2:纯转发 3:直接@我 4:评论并@我 5:私信过我 6:微信互动
				interactTypeSource.substring(0, inType - 1);
				interactTypeSource.substring(inType);
				//LinksusRelationUserAccount linksusUserAccount = new LinksusRelationUserAccount();
				// linksusUserAccount.setInteractType(interactTypeStr);
				//linksusUserAccount.setInteractNum(userAccount.getInteractNum()+1);//互动次数加一
				// linksusUserAccount.setAccountId(accountId);
				// linksusUserAccount.setUserId(userId);
				//linksusRelationUserAccountService.updateLinksusRelationUserAccountNum(linksusUserAccount);
				InteractCountSave.updateUserAcctInteractInfo(userId, accountId, inType);
			}
		}
		return recordId;
	}
}
