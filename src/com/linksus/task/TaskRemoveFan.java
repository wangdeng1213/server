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
import com.linksus.common.util.StringUtil;
import com.linksus.entity.AttentionRelationEntrity;
import com.linksus.entity.LinksusRelationOperate;
import com.linksus.entity.LinksusRelationOperateItem;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.entity.LinksusTaskInvalidRecord;
import com.linksus.entity.UrlEntity;
import com.linksus.service.LinksusRelationGroupService;
import com.linksus.service.LinksusRelationPersonGroupService;
import com.linksus.service.LinksusRelationWeibouserService;

/**
 * 增量粉丝任务
 */
public class TaskRemoveFan extends BaseTask{

	LinksusRelationWeibouserService linksusRelationWeibouserService = (LinksusRelationWeibouserService) ContextUtil
			.getBean("linksusRelationWeibouserService");
	LinksusRelationPersonGroupService linksusRelationPersonGroupService = (LinksusRelationPersonGroupService) ContextUtil
			.getBean("linksusRelationPersonGroupService");
	LinksusRelationGroupService linksusRelationGroupService = (LinksusRelationGroupService) ContextUtil
			.getBean("linksusRelationGroupService");
	TaskCancelAttention taskCancelAttention = new TaskCancelAttention();

	public static void main(String[] args){
		TaskRemoveFan t = new TaskRemoveFan();
		t.cal();
	}

	@Override
	public void cal(){
		try{
			// 移除粉丝任务
			String accountType = String.valueOf(this.accountType);
			Map paraMap = new HashMap();
			paraMap.put("appType", accountType);
			paraMap.put("taskType", "3");//任务类型 1 添加关注 2 取消关注 3 移除粉丝
			paraMap.put("pageSize", pageSize);
			int startCount = (currentPage - 1) * pageSize;
			paraMap.put("startCount", startCount);
			List<AttentionRelationEntrity> attentionList = attentionService.getAttentionRelationEntrityList(paraMap);
			if(StringUtils.equals(accountType, "1")){//新浪
				dealCancelDataFromSina(attentionList, "1");
			}
			checkTaskListEnd(attentionList);
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
	}

	// 前台接口调用方法
	public Map removeFanInterface(Map paraMap) throws Exception{
		Map rsMsg = new HashMap();
		String pid = (String) paraMap.get("pid");
		LinksusRelationOperate relaOperate = relationOperateService.checkIsExsitRelationOperate(pid);
		if(relaOperate != null){
			String accountType = relaOperate.getAccountType().toString();
			List<AttentionRelationEntrity> attentionList = attentionService.getRelationAttentionTask(paraMap);
			if(attentionList == null && attentionList.size() == 0){
				rsMsg.put("errcode", "10015");//该主键没有任务要执行
				return rsMsg;
			}
			if(StringUtils.equals(accountType, "1")){
				dealCancelDataFromSina(attentionList, "1");
			}
			LinksusRelationOperate resultOperate = relationOperateService.checkIsExsitRelationOperate(pid);
			rsMsg.put("succNum", resultOperate.getOperSuccessNum());
			rsMsg.put("failNum", resultOperate.getOperFailNum());
			rsMsg.put("countNum", attentionList.size());
		}else{
			rsMsg.put("errcode", "10110");// 该主键未通过审批或不存在
		}
		return rsMsg;
	}

	// 处理新浪类型的用户
	public String dealCancelDataFromSina(List<AttentionRelationEntrity> attentionList,String type) throws Exception{
		CacheUtil.getInstance();
		UrlEntity strUrl;
		if(type.equals("1")){//移除粉丝
			strUrl = LoadConfig.getUrlEntity("RemoveFans");
		}else{//取消关注某用户
			strUrl = LoadConfig.getUrlEntity("cancelAttention");
		}

		StringBuffer strResult = new StringBuffer("[");
		String returnData = "";
		boolean hasData = false;
		for(int i = 0; i < attentionList.size(); i++){
			String msg = "";
			AttentionRelationEntrity removeAttention = attentionList.get(i);
			String strToken = removeAttention.getToken();
			// post请求参数赋值
			String uid = removeAttention.getRpsId();
			// 通过post方式请求得到字符串
			Map paramMap = new HashMap();
			paramMap.put("access_token", strToken);
			paramMap.put("uid", uid);
			returnData = HttpUtil.postRequest(strUrl, paramMap);
			logger.error("取消关注:账号-{}#用户-{}", removeAttention.getAccountId(), removeAttention.getUserId());
			LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(returnData);
			if(error != null){
				LinksusRelationOperateItem item = new LinksusRelationOperateItem();
				item.setOperateId(removeAttention.getPid());
				item.setUserId(removeAttention.getUserId());
				item.setTaskStatus(2L);
				relationOperateItemService.updateLinksusRelationOperateItem(item);
				relationOperateService.updateLinksusRelationOperFailNum(removeAttention.getPid());
				LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
				if(!"".equals(msg)){
					invalidRecord.setErrorCode(Constants.INVALID_RECORD_EXCEPTION);
					invalidRecord.setErrorMsg(msg);
				}else{
					invalidRecord.setErrorCode(error.getErrorCode().toString());
					invalidRecord.setErrorMsg(error.getErrorMsg());
				}
				invalidRecord.setRecordId(removeAttention.getPid());
				invalidRecord.setOperType(Constants.INVALID_RECORD_REMOVE_FANS);
				invalidRecord.setAddTime(DateUtil.getUnixDate(new Date()));
				invalidRecord.setInstitutionId(removeAttention.getInstitutionId());
				dealInvalidRecord(invalidRecord);
				continue;
			}else{
				LinksusRelationOperateItem item = new LinksusRelationOperateItem();
				item.setOperateId(removeAttention.getPid());
				item.setUserId(removeAttention.getUserId());
				item.setTaskStatus(3L);
				relationOperateItemService.updateLinksusRelationOperateItem(item);
				relationOperateService.updateLinksusRelationOperSuccessNum(removeAttention.getPid());
			}
			TaskCancelAttention.dealRelationUserAccountSingle(removeAttention.getInstitutionId(), removeAttention
					.getAccountId(), removeAttention.getUserId(), "1");
		}
		return "";
	}
}
