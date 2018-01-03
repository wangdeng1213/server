package com.linksus.task;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.linksus.common.Constants;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.JsonUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.QuartzUtil;
import com.linksus.entity.LinksusAppaccount;
import com.linksus.entity.LinksusInteractWeixin;
import com.linksus.entity.LinksusTaskInvalidRecord;
import com.linksus.service.LinksusAppaccountService;
import com.linksus.service.LinksusInteractRecordService;
import com.linksus.service.LinksusInteractWeixinService;

/**
 * 微信回复任务
 * 
 */

public class TaskReplyWeixins extends BaseTask{

	LinksusInteractWeixinService linksusInteractWeixinService = (LinksusInteractWeixinService) ContextUtil
			.getBean("linksusInteractWeixinService");
	LinksusAppaccountService linksusAppaccountService = (LinksusAppaccountService) ContextUtil
			.getBean("linksusAppaccountService");
	LinksusInteractRecordService linksusInteractRecordService = (LinksusInteractRecordService) ContextUtil
			.getBean("linksusInteractRecordService");

	/**发送方式  即时/定时 */
	private String sendType;

	public void setSendType(String sendType){
		this.sendType = sendType;
	}

	@Override
	public void cal(){
		if("0".equals(sendType)){ //0是即时
			replyWeixinImmediate();
		}else if("1".equals(sendType)){//1是定时
			replyWeixinAtRegularTime();
		}
	}

	//即时发布微信
	public void replyWeixinImmediate(){
		//获取即时发布列表
		LinksusInteractWeixin linksusWeixin = new LinksusInteractWeixin();
		int startCount = (currentPage - 1) * pageSize;
		linksusWeixin.setPageSize(pageSize);
		linksusWeixin.setStartCount(startCount);
		List<LinksusInteractWeixin> recordList = linksusInteractWeixinService.getTaskWeixinAtImmediate(linksusWeixin);
		if(recordList != null && recordList.size() > 0){
			for(LinksusInteractWeixin interactWeixin : recordList){
				//判断互动时间是否在48小时内
				Date currDate = new Date();
				if(currDate.getTime() - interactWeixin.getInteractTime() * 1000L < 48 * 3600 * 1000L){
					//发布微信
					sendWeiXinTask(interactWeixin);
				}else{
					dealExceptionInvalidRecord(linksusWeixin.getAccountId(), linksusWeixin.getPid(), new Exception(
							"微信互动超过48小时,不能被回复"));
					logger.error("微信互动:{},超过48小时,不能被回复", interactWeixin.getPid());
				}
			}
		}
		checkTaskListEnd(recordList);//判断任务是否轮询完成
	}

	//定时发布微信
	public void replyWeixinAtRegularTime(){
		//获取定时发布列表
		LinksusInteractWeixin linksusWeixin = new LinksusInteractWeixin();
		int startCount = (currentPage - 1) * pageSize;
		linksusWeixin.setPageSize(pageSize);
		linksusWeixin.setStartCount(startCount);
		List<LinksusInteractWeixin> recordList = linksusInteractWeixinService.getTaskWeixinAtRegularTime(linksusWeixin);
		if(recordList != null && recordList.size() > 0){
			for(LinksusInteractWeixin interactWeixin : recordList){
				//判断互动时间是否在48小时内
				Date currDate = new Date();
				if(currDate.getTime() - interactWeixin.getInteractTime() * 1000L < 48 * 3600 * 1000L){
					addWeixinTimer(interactWeixin);
				}else{
					dealExceptionInvalidRecord(linksusWeixin.getAccountId(), linksusWeixin.getPid(), new Exception(
							"微信互动超过48小时,不能被回复"));
					logger.error("微信互动:{},超过48小时,不能被回复", interactWeixin.getPid());
				}
			}
		}
		checkTaskListEnd(recordList);//判断任务是否轮询完成
	}

	//发布微信
	public String sendWeiXinTask(LinksusInteractWeixin interactWeixin){
		String resStr = "";
		WeixinAction weixinAction = new WeixinAction();
		Date currDate = new Date();
		if(currDate.getTime() - interactWeixin.getInteractTime() * 1000L < 48 * 3600 * 1000L){
			LinksusAppaccount appcount = linksusAppaccountService.getLinksusAppaccountById(interactWeixin
					.getAccountId());
			if(appcount != null){
				resStr = weixinAction.sendWeixinContent(appcount.getId(), appcount.getToken(), interactWeixin
						.getUserId(), interactWeixin.getMaterialId(), interactWeixin.getMsgType(), interactWeixin
						.getContent(), interactWeixin.getOpenid());
				//更新互动信息表
				Integer currentTime = Integer.parseInt(String.valueOf(new Date().getTime() / 1000));
				if(StringUtils.isNotBlank(resStr) && resStr.equals("sucess")){//成功
					interactWeixin.setStatus(2);
					interactWeixin.setInteractTime(currentTime);
					linksusInteractWeixinService.updateLinksusInteractWeixinStatus(interactWeixin);
					return "#回复微信发布成功:" + resStr;
				}else{//失败
					String errorContent = "";
					String errcode = "";
					if(Constants.WECHATNOTEXISTENT.equals(resStr)){
						resStr = "20035";
						errcode = resStr;
						errorContent = "当前用户未关注此账户,不能被回复!";
						interactWeixin.setStatus(4);
					}else{
						interactWeixin.setStatus(3);
						errorContent = resStr;
						errcode = JsonUtil.getNodeValueByName(resStr, "errcode");
						resStr = "#回复微信发布失败:" + resStr;
					}
					dealErrorCodeInvalidRecord(interactWeixin.getAccountId(), interactWeixin.getPid(), errcode,
							errorContent);
					interactWeixin.setInteractTime(currentTime);
					linksusInteractWeixinService.updateLinksusInteractWeixinStatus(interactWeixin);
					return resStr;
				}
			}else{
				logger.info("--------------token取值错误------------");
			}
		}else{
			dealExceptionInvalidRecord(interactWeixin.getAccountId(), interactWeixin.getPid(), new Exception(
					"微信互动超过48小时,不能被回复"));
			resStr = "20029";
		}
		return resStr;
	}

	/**
	 * 微信定时任务
	 * 
	 * @param record
	 */
	private void addWeixinTimer(LinksusInteractWeixin record){
		if(Constants.STATUS_DELETE.equals(record.getStatus() + "")
				|| Constants.STATUS_PAUSE.equals(record.getStatus() + "")){
			QuartzUtil.removeJob("RECORD_" + record.getPid().toString(), Constants.RECORD_GROUP_NAME);
		}else{
			Map dataMap = new HashMap();
			dataMap.put("pid", record.getPid());
			Date sendTime = new Date(Long.parseLong(record.getSendTime().toString()) * 1000);
			QuartzUtil.addSimpleJob("RECORD_" + record.getPid().toString(), Constants.RECORD_GROUP_NAME, dataMap,
					sendTime, SendWeiXinTimer.class);
		}
	}

	//前端调用，即时、定时回复微信
	public String sendReplyWeixin(LinksusInteractWeixin record){
		String rsMsg = null;
		if(record != null){
			if(record.getSendType() == 0){
				//发布微信
				rsMsg = sendWeiXinTask(record);
			}else if(record.getSendType() == 1){
				rsMsg = "add";
				addWeixinTimer(record);
			}
		}
		return rsMsg;
	}

	private void dealExceptionInvalidRecord(Long institutionId,Long pid,Exception exception){
		try{
			LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
			invalidRecord.setInstitutionId(institutionId);
			invalidRecord.setErrorCode("EXCEPTION");
			invalidRecord.setErrorMsg(LogUtil.getExceptionStackMsg(exception));
			invalidRecord.setOperType(Constants.INVALID_RECORD_INTERACT_WEIXIN);
			invalidRecord.setRecordId(pid);
			dealInvalidRecord(invalidRecord);
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
	}

	private void dealErrorCodeInvalidRecord(Long institutionId,Long pid,String errcode,String rsData){
		try{
			LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
			invalidRecord.setErrorCode(errcode);
			invalidRecord.setErrorMsg(rsData);
			invalidRecord.setInstitutionId(institutionId);
			invalidRecord.setOperType(Constants.INVALID_RECORD_INTERACT_WEIXIN);
			invalidRecord.setRecordId(pid);
			dealInvalidRecord(invalidRecord);
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
	}

}
