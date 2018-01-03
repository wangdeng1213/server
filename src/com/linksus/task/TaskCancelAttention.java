package com.linksus.task;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.linksus.common.Constants;
import com.linksus.common.config.LoadConfig;
import com.linksus.common.util.CacheUtil;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.DateUtil;
import com.linksus.common.util.HttpUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.RedisUtil;
import com.linksus.common.util.StringUtil;
import com.linksus.data.QueueDataSave;
import com.linksus.entity.AttentionRelationEntrity;
import com.linksus.entity.LinksusRelationOperate;
import com.linksus.entity.LinksusRelationOperateItem;
import com.linksus.entity.LinksusRelationUserAccount;
import com.linksus.entity.LinksusRelationWeibouser;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.entity.LinksusTaskInvalidRecord;
import com.linksus.entity.UrlEntity;
import com.linksus.service.LinksusRelationPersonGroupService;
import com.linksus.service.LinksusRelationWeibouserService;

/**
 * 增量粉丝任务
 */
public class TaskCancelAttention extends BaseTask{

	private static LinksusRelationWeibouserService linksusRelationWeibouserService = (LinksusRelationWeibouserService) ContextUtil
			.getBean("linksusRelationWeibouserService");
	private static LinksusRelationPersonGroupService linksusRelationPersonGroupService = (LinksusRelationPersonGroupService) ContextUtil
			.getBean("linksusRelationPersonGroupService");

	public static void main(String[] args){
		TaskCancelAttention tt = new TaskCancelAttention();
		tt.cal();
	}

	@Override
	public void cal(){
		try{
			// 取消关注任务
			String accountType = String.valueOf(this.accountType);
			Map paraMap = new HashMap();
			paraMap.put("appType", accountType);
			paraMap.put("taskType", "2");
			paraMap.put("pageSize", pageSize);
			int startCount = (currentPage - 1) * pageSize;
			paraMap.put("startCount", startCount);
			List<AttentionRelationEntrity> attentionList = attentionService.getAttentionRelationEntrityList(paraMap);
			if(StringUtils.equals(accountType, "1")){
				dealCancelDataFromSina(attentionList);
			}else if(StringUtils.equals(accountType, "2")){
				dealCancelDataFromTentent(attentionList);
			}
			checkTaskListEnd(attentionList);
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
	}

	// 前台接口调用方法
	public Map cancelAttentionInterface(Map paraMap) throws Exception{
		Map rsMsg = new HashMap();
		String pid = (String) paraMap.get("pid");
		LinksusRelationOperate relaOperate = relationOperateService.checkIsExsitRelationOperate(pid);
		if(relaOperate != null){
			String accountType = relaOperate.getAccountType().toString();
			List<AttentionRelationEntrity> attentionList = attentionService.getRelationAttentionTask(paraMap);
			if(attentionList.size() == 0){
				rsMsg.put("errcode", "10015");//该主键没有任务要执行
				return rsMsg;
			}
			if(StringUtils.equals(accountType, "1")){
				dealCancelDataFromSina(attentionList);
			}else if(StringUtils.equals(accountType, "2")){
				dealCancelDataFromTentent(attentionList);
			}
			//查询pid的成功次数和失败次数
			LinksusRelationOperate resultOperate = relationOperateService.checkIsExsitRelationOperate(pid);
			rsMsg.put("succNum", resultOperate.getOperSuccessNum());
			rsMsg.put("failNum", resultOperate.getOperFailNum());
			rsMsg.put("countNum", attentionList.size());
		}else{
			rsMsg.put("errcode", "10110");// 该主键未通过审批或不存在
		}
		return rsMsg;
	}

	// 处理腾讯类型的用户
	public String dealCancelDataFromTentent(List<AttentionRelationEntrity> attentionList) throws Exception{
		CacheUtil cache = CacheUtil.getInstance();
		UrlEntity strUrl = LoadConfig.getUrlEntity("TCcancelAttention");
		String returnData = "";
		Map paraMap = new HashMap();
		paraMap.put("oauth_consumer_key", cache.getAppInfo(Constants.CACHE_TENCENT_APPINFO).getAppKey());
		paraMap.put("clientip", Constants.TENCENT_CLIENT_IP);
		paraMap.put("oauth_version", "2.a");
		paraMap.put("format", "json");
		for(int i = 0; i < attentionList.size(); i++){
			AttentionRelationEntrity cancleAttention = (AttentionRelationEntrity) attentionList.get(i);
			try{
				String strToken = cancleAttention.getToken();
				String openid = cancleAttention.getOpenId();
				// post请求参数赋值
				String access_token = strToken;
				String rpsId = cancleAttention.getRpsId();
				// 通过post方式请求得到字符串
				paraMap.put("openid", openid);
				paraMap.put("access_token", strToken);
				paraMap.put("fopenid", rpsId);
				returnData = HttpUtil.postRequest(strUrl, paraMap);

				LinksusTaskErrorCode error = StringUtil.parseTencentErrorCode(returnData);
				if(error != null && error.getErrorCode() != 50370){
					LinksusRelationOperateItem item = new LinksusRelationOperateItem();
					item.setOperateId(cancleAttention.getPid());
					item.setUserId(cancleAttention.getUserId());
					item.setTaskStatus(2L);
					relationOperateItemService.updateLinksusRelationOperateItem(item);
					relationOperateService.updateLinksusRelationOperFailNum(cancleAttention.getPid());
					//插入无效记录表
					LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
					invalidRecord.setErrorCode(error.getErrorCode().toString());
					invalidRecord.setErrorMsg(error.getErrorMsg());
					invalidRecord.setRecordId(cancleAttention.getPid());
					invalidRecord.setOperType(Constants.INVALID_RECORD_CANCLE_ATTENTION);
					invalidRecord.setAddTime(DateUtil.getUnixDate(new Date()));
					invalidRecord.setInstitutionId(cancleAttention.getInstitutionId());
					dealInvalidRecord(invalidRecord);
					continue;
				}else{
					LinksusRelationOperateItem item = new LinksusRelationOperateItem();
					item.setOperateId(cancleAttention.getPid());
					item.setUserId(cancleAttention.getUserId());
					item.setTaskStatus(3L);
					relationOperateItemService.updateLinksusRelationOperateItem(item);
					relationOperateService.updateLinksusRelationOperSuccessNum(cancleAttention.getPid());
				}
				dealRelationUserAccountSingle(cancleAttention.getInstitutionId(), cancleAttention.getAccountId(),
						cancleAttention.getUserId(), "2");
			}catch (Exception e){
				LogUtil.saveException(logger, e);
				LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
				invalidRecord.setErrorCode(Constants.INVALID_RECORD_EXCEPTION);
				invalidRecord.setErrorMsg(LogUtil.getExceptionStackMsg(e));
				invalidRecord.setRecordId(cancleAttention.getPid());
				invalidRecord.setOperType(Constants.INVALID_RECORD_CANCLE_ATTENTION);
				invalidRecord.setAddTime(DateUtil.getUnixDate(new Date()));
				invalidRecord.setInstitutionId(cancleAttention.getInstitutionId());
				dealInvalidRecord(invalidRecord);
			}
		}
		return "";
	}

	// 处理新浪类型的用户
	public void dealCancelDataFromSina(List<AttentionRelationEntrity> attentionList) throws Exception{
		UrlEntity strUrl = LoadConfig.getUrlEntity("cancelAttention");
		String returnData = "";
		for(int i = 0; i < attentionList.size(); i++){
			AttentionRelationEntrity cancelAttention = (AttentionRelationEntrity) attentionList.get(i);
			try{
				String strToken = cancelAttention.getToken();
				String access_token = strToken;
				String uid = cancelAttention.getRpsId();
				Map paramMap = new HashMap();
				paramMap.put("access_token", access_token);
				paramMap.put("uid", uid);
				returnData = HttpUtil.postRequest(strUrl, paramMap);
				LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(returnData);
				if(error != null && !StringUtils.equals(error.getSrcCode(), "20522")){
					LinksusRelationOperateItem item = new LinksusRelationOperateItem();
					item.setOperateId(cancelAttention.getPid());
					item.setUserId(cancelAttention.getUserId());
					item.setTaskStatus(2L);
					relationOperateItemService.updateLinksusRelationOperateItem(item);
					relationOperateService.updateLinksusRelationOperFailNum(cancelAttention.getPid());
					//插入无效记录表
					LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
					invalidRecord.setErrorCode(error.getErrorCode().toString());
					invalidRecord.setErrorMsg(error.getErrorMsg());
					invalidRecord.setRecordId(cancelAttention.getPid());
					invalidRecord.setOperType(Constants.INVALID_RECORD_CANCLE_ATTENTION);
					invalidRecord.setAddTime(DateUtil.getUnixDate(new Date()));
					invalidRecord.setInstitutionId(cancelAttention.getInstitutionId());
					dealInvalidRecord(invalidRecord);
					continue;
				}else{
					LinksusRelationOperateItem operateItem = new LinksusRelationOperateItem();
					operateItem.setOperateId(cancelAttention.getPid());
					operateItem.setUserId(cancelAttention.getUserId());
					operateItem.setTaskStatus(new Long("3"));
					relationOperateItemService.updateLinksusRelationOperateItem(operateItem);
					relationOperateService.updateLinksusRelationOperSuccessNum(cancelAttention.getPid());
				}
				dealRelationUserAccountSingle(cancelAttention.getInstitutionId(), cancelAttention.getAccountId(),
						cancelAttention.getUserId(), "2");
			}catch (Exception e){
				LogUtil.saveException(logger, e);
				LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
				invalidRecord.setErrorCode(Constants.INVALID_RECORD_EXCEPTION);
				invalidRecord.setErrorMsg(LogUtil.getExceptionStackMsg(e));
				invalidRecord.setRecordId(cancelAttention.getPid());
				invalidRecord.setOperType(Constants.INVALID_RECORD_CANCLE_ATTENTION);
				invalidRecord.setAddTime(DateUtil.getUnixDate(new Date()));
				invalidRecord.setInstitutionId(cancelAttention.getInstitutionId());
				dealInvalidRecord(invalidRecord);
			}
		}
	}

	/**
	 * 处理用户账号关系  dealtype 1 去除粉丝 2 取消关注
	 * @param institutionId 
	 * @param accountId
	 * @param userId
	 * @param dealtype
	 * @return
	 */
	public static String dealRelationUserAccountSingle(Long institutionId,Long accountId,Long userId,String dealtype){
		String relationValue = RedisUtil.getRedisHash("relation_user_account", accountId + "#" + userId);
		logger.error("调试信息:账号-{}#用户-{},关系{}", accountId, userId, relationValue);
		if(StringUtils.isBlank(relationValue)){
			return "fail";
		}else{
			String[] str = relationValue.split("#");
			String flagRelation = "0";
			String interactType = "000000";
			if(str != null && str.length >= 2){
				flagRelation = str[0];
				interactType = str[1];
			}
			LinksusRelationUserAccount linksusUserAccount = new LinksusRelationUserAccount();
			linksusUserAccount.setAccountId(accountId);
			linksusUserAccount.setUserId(userId);
			if("3".equals(flagRelation)){// 假如为互粉的关系
				// 修改这条数据
				if(dealtype.equals("1")){// 移除粉丝
					flagRelation = "2";
				}
				if(dealtype.equals("2")){// 取消关注某用户
					flagRelation = "1";
				}
				//更新缓存中的数据
				linksusUserAccount.setFlagRelation(Integer.parseInt(flagRelation));
				linksusUserAccount.setInteractType(interactType);
				QueueDataSave.addDataToQueue(linksusUserAccount, Constants.OPER_TYPE_UPDATE);
			}else if("1".equals(flagRelation) || "2".equals(flagRelation)){// 假如不是互粉的关系
				// 修改这条数据
				if(!"000000".equals(interactType)){// 存在互动关系
					flagRelation = "4";// 潜在关系
					linksusUserAccount.setFlagRelation(Integer.parseInt(flagRelation));
					linksusUserAccount.setInteractType(interactType);
					QueueDataSave.addDataToQueue(linksusUserAccount, Constants.OPER_TYPE_UPDATE);
				}else{
					//根据userId查询微博用户表中的personId
					LinksusRelationWeibouser linksusRelationWeibouser = linksusRelationWeibouserService
							.getRelationWeibouserByUserId(userId);
					Long personId = linksusRelationWeibouser.getPersonId();
					//删除缓存中的这条记录
					RedisUtil.delFieldKey("relation_user_account", accountId + "#" + userId);
					Map paraMap = new HashMap();
					paraMap.put("accountId", accountId);
					paraMap.put("userId", userId);
					logger.error("调试信息:账号-{}#用户-{},关系{}-->删除用户关系", accountId, userId, flagRelation);
					relationUserAccountService.deleteLinksusRelationUserAccountByKey(paraMap);
					//判断用户机构关系
					LinksusRelationUserAccount linkAccount = new LinksusRelationUserAccount();
					linkAccount.setUserId(userId);
					linkAccount.setInstitutionId(institutionId);
					Integer countAccount = relationUserAccountService.getCountLinksusRelationUserAccount(linkAccount);
					if(countAccount == 0){//该用户和该机构下别的账号没有关系 删除人员分组
						Map pasMap = new HashMap();
						pasMap.put("personId", personId);
						pasMap.put("institutionId", institutionId);
						linksusRelationPersonGroupService.deleteByHashMap(pasMap);
					}
				}
			}
		}
		return "success";
	}
}
