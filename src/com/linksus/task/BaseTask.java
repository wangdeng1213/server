package com.linksus.task;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.Constants;
import com.linksus.common.config.LoadConfig;
import com.linksus.common.util.CacheUtil;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.DateUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.MailUtil;
import com.linksus.common.util.PrimaryKeyGen;
import com.linksus.common.util.SmsUtil;
import com.linksus.entity.Email;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.entity.LinksusTaskInvalidRecord;
import com.linksus.entity.TaskStatistic;
import com.linksus.service.AttentionRelationEntrityService;
import com.linksus.service.LinksusAppaccountService;
import com.linksus.service.LinksusPersonaccountService;
import com.linksus.service.LinksusRelationOperateItemService;
import com.linksus.service.LinksusRelationOperateService;
import com.linksus.service.LinksusRelationUserAccountService;
import com.linksus.service.LinksusSourceAppaccountService;
import com.linksus.service.LinksusTaskInvalidRecordService;
import com.linksus.service.LinksusWeiboService;

public abstract class BaseTask implements Runnable{

	protected static final Logger logger = LoggerFactory.getLogger(BaseTask.class);
	protected static LinksusWeiboService weiboService = (LinksusWeiboService) ContextUtil
			.getBean("linksusWeiboService");
	protected static LinksusAppaccountService accountService = (LinksusAppaccountService) ContextUtil
			.getBean("linksusAppaccountService");
	protected static LinksusSourceAppaccountService sourceAcctService = (LinksusSourceAppaccountService) ContextUtil
			.getBean("linksusSourceAppaccountService");
	protected static AttentionRelationEntrityService attentionService = (AttentionRelationEntrityService) ContextUtil
			.getBean("attentionRelationEntrityService");
	protected static LinksusRelationUserAccountService relationUserAccountService = (LinksusRelationUserAccountService) ContextUtil
			.getBean("linksusRelationUserAccountService");
	protected static LinksusRelationOperateItemService relationOperateItemService = (LinksusRelationOperateItemService) ContextUtil
			.getBean("linksusRelationOperateItemService");
	protected static LinksusRelationOperateService relationOperateService = (LinksusRelationOperateService) ContextUtil
			.getBean("linksusRelationOperateService");
	protected static LinksusPersonaccountService personAccountService = (LinksusPersonaccountService) ContextUtil
			.getBean("linksusPersonaccountService");
	protected static LinksusTaskInvalidRecordService recordService = (LinksusTaskInvalidRecordService) ContextUtil
			.getBean("linksusTaskInvalidRecordService");
	//缓存对象
	protected static CacheUtil cache = CacheUtil.getInstance();
	/** 账号类型 */
	protected Integer accountType;
	/** 任务间隔时间 */
	private long taskInterval = 0;
	/** 任务上次完成时间 */
	private Long taskLastStartTime;
	/** 任务完成标志 */
	private boolean finishFlag = false;
	/** 当前分页查询页数 */
	protected int currentPage = 1;
	protected int pageSize = 200;
	/** 线程终止标志:true终止,当再次循环时会判断该值 */
	private boolean exitFlag = false;
	/** 任务类型 */
	private String taskType;
	/** 任务执行统计MAP */
	private static Map taskStatisticMap = new HashMap();

	public void setTaskInterval(long taskInterval){
		this.taskInterval = taskInterval;
	}

	public void setTaskType(String taskType){
		this.taskType = taskType;
	}

	public void setPageSize(int pageSize){
		this.pageSize = pageSize;
	}

	public Integer getAccountType(){
		return accountType;
	}

	public void setAccountType(String accountType){
		this.accountType = new Integer(accountType);
	}

	public static Map getTaskStatisticMap(){
		return taskStatisticMap;
	}

	public static void resetTaskStatisticMap(){
		taskStatisticMap.clear();
	}

	private void updateTaskStatus(String taskStatus,int taskSize){
		TaskStatistic taskStatistic = (TaskStatistic) taskStatisticMap.get(taskType);
		if(taskStatistic == null){
			taskStatistic = new TaskStatistic();
			taskStatisticMap.put(taskType, taskStatistic);
		}
		taskStatistic.setTaskStatus(taskStatus);//执行状态
		taskStatistic.setTaskSize(taskStatistic.getTaskSize() + taskSize);//任务数
		if(Constants.TASK_STATUS_FINISH.equals(taskStatus)){
			taskStatistic.setTaskCount(taskStatistic.getTaskCount() + 1);//执行次数
		}
	}

	/** 处理数据 取得需要处理的数据 带分页,分页取完一次需要调用任务完成标识方法 */
	public abstract void cal();

	public void run(){
		while (!exitFlag){//线程开始执行
			//1 判断刷新时间间隔
			try{//防止异常中断
				long time = getTaskIntervalTime();
				if(time >= 0 && taskInterval > time){//队列刷新频率太快任务刷新时间
					try{
						updateTaskStatus(Constants.TASK_STATUS_SLEEP, 0);
						if(taskInterval - time > 60000){
							logger.debug(">>>>>>>>{}任务间隔时间太短,休眠1分钟", taskType);
							Thread.sleep(60000);
						}else{
							logger.debug(">>>>>>>>{}任务间隔时间太短,休眠{}秒", taskType, (taskInterval - time) / 1000);
							Thread.sleep(taskInterval - time);
						}
					}catch (InterruptedException e){
						ServerTaskControl.removeTaskThread(taskType);
						return;
					}
					continue;
				}else{
					if(taskLastStartTime == null){
						taskLastStartTime = new Date().getTime();
					}else if(finishFlag){//再次开始计算
						finishFlag = false;
						taskLastStartTime = new Date().getTime();
					}
				}
				updateTaskStatus(Constants.TASK_STATUS_EXEC, 0);
				cal();
			}catch (Exception e){
				LogUtil.saveException(logger, e);
			}
		}
		ServerTaskControl.removeTaskThread(taskType);
	}

	/**
	 * 根据任务list的size判断是否轮询完一遍 设置任务完成时间
	 * @param dataList
	 */
	protected void checkTaskListEnd(List dataList){
		if(dataList == null || dataList.size() < pageSize){
			finishFlag = true;
			currentPage = 1;
			int taskSize = 0;
			if(dataList != null){
				taskSize = dataList.size();
			}
			updateTaskStatus(Constants.TASK_STATUS_FINISH, taskSize);
		}else{
			currentPage++;
			updateTaskStatus(Constants.TASK_STATUS_EXEC, dataList.size());
		}
	}

	/**
	 * 直接设置任务轮询完成时间
	 */
	protected void setTaskListEndTime(int taskSize){
		finishFlag = true;
		updateTaskStatus(Constants.TASK_STATUS_FINISH, taskSize);
		currentPage = 1;
	}

	/**
	 * 取得本次任务执行间隔
	 * @return
	 */
	private long getTaskIntervalTime(){
		 //任务最后执行时间为空 任务执行间隔为0 返回-1 说明是第一次执行
		if(taskLastStartTime == null || taskInterval == 0){
			return -1;
		}else{
			if(finishFlag){//任务完成 返回任务执行间隔
				return new Date().getTime() - taskLastStartTime;
			}//没有完成返回-1
			return -1;
		}
	}

	public void setExitFlag(boolean exitFlag){
		this.exitFlag = exitFlag;
	}

	/**
	 * 处理无效操作:微博发布/评论/营销等
	 * @param invalidRecord
	 * @return
	 * @throws Exception 
	 */
	public static void dealInvalidRecord(LinksusTaskInvalidRecord invalidRecord){
		try{
			MailUtil mailUtils = (MailUtil) ContextUtil.getBean("mail"); //获取邮件发送实例对象
			int type = invalidRecord.getOperType().intValue();
			String subject = "";
			String content = "";
			String errorMsg = "";
			String sendType = "";//判断发送人员和发送类型
			if(Constants.INVALID_RECORD_EXCEPTION.equals(invalidRecord.getErrorCode())){
				errorMsg = "[90000-" + "系统异常]";
			}else{
				LinksusTaskErrorCode error = cache.getErrorCode(invalidRecord.getErrorCode());
				if(error.getDisplayType().intValue() == 0){
					errorMsg = "[" + invalidRecord.getErrorCode() + "-" + invalidRecord.getErrorMsg() + "]";
				}else{
					errorMsg = "[90000-" + "系统错误]";
				}
			}
			if(type == Constants.INVALID_RECORD_SEND_WEIBO){
				sendType = LoadConfig.getString("invalid_1");
				subject = "微博发布异常";
				content = "您好，您于在1510cloud发布的微博，因" + errorMsg + "发布失败。请登录平台重新发送。如有疑问，请拨打电话：400-9909-111。【1510cloud】";
			}else if(type == Constants.INVALID_RECORD_INTERACT_WEIBO){
				sendType = LoadConfig.getString("invalid_4");
				subject = "微博互动数据发布异常";
				content = "您好，您于在1510cloud发布的微博互动数据，因" + errorMsg
						+ "发布失败。请登录平台重新发送。如有疑问，请拨打电话：400-9909-111。【1510cloud】";
			}else if(type == Constants.INVALID_RECORD_MARKETING_AT
					|| type == Constants.INVALID_RECORD_MARKETING_COMMENT){
				sendType = LoadConfig.getString("invalid_2");
				subject = "微博营销数据发布异常";
				content = "您好，微博营销数据发布异常，因" + errorMsg + "发布失败。";
			}else if(type == Constants.INVALID_RECORD_INTERACT_MESSAGE){
				sendType = LoadConfig.getString("invalid_5");
				subject = "私信数据发布异常";
				content = "您好，私信数据发布异常，因" + errorMsg + "发布失败。";
			}else if(type == Constants.INVALID_RECORD_INTERACT_WEIXIN){
				sendType = LoadConfig.getString("invalid_6");
				subject = "微信数据发布异常";
				content = "您好，微信数据发布异常，因" + errorMsg + "发布失败。";
			}else if(type == Constants.INVALID_RECORD_ADD_ATTENTION){
				sendType = LoadConfig.getString("invalid_7");
				subject = "增加关注操作异常";
				content = "您好，增加关注操作异常，因" + errorMsg + "操作失败。";
			}else if(type == Constants.INVALID_RECORD_CANCLE_ATTENTION){
				sendType = LoadConfig.getString("invalid_8");
				subject = "取消关注操作异常";
				content = "您好，取消关注发布异常，因" + errorMsg + "操作失败。";
			}else if(type == Constants.INVALID_RECORD_REMOVE_FANS){
				sendType = LoadConfig.getString("invalid_9");
				subject = "移除粉丝操作异常";
				content = "您好，移除粉丝操作异常，因" + errorMsg + "操作失败。";
			}else if(type == Constants.INVALID_RECORD_INFO){
				sendType = LoadConfig.getString("invalid_10");
				subject = "操作异常";
				content = "操作异常，因" + errorMsg + "操作失败。";
			}

			invalidRecord.setPid(PrimaryKeyGen.getPrimaryKey("linksus_task_invalid_record", "pid"));
			invalidRecord.setSourceIp((String) cache.getCache(Constants.CACHE_LOCAL_IP_ADDR));
			invalidRecord.setAddTime(DateUtil.getUnixDate(new Date()));
			invalidRecord.setEmailInstPersons("");
			invalidRecord.setSmsInstPersons("");

			//根据机构查询运维人员邮件/短信联系方式
			List personAccountList = personAccountService.getPersonaccountByInstitutionId(invalidRecord
					.getInstitutionId());
			StringBuffer emailBuffer = new StringBuffer();
			StringBuffer smsBuffer = new StringBuffer();
			for(int i = 0; i < personAccountList.size(); i++){
				Map map = (Map) personAccountList.get(i);
				//发邮件
				if("1".equals(sendType.substring(0, 1)) && map.get("mail") != null){
					Email email = new Email();
					email.setAddress((String) map.get("mail"));
					email.setHtmlFlag(false);
					email.setSubject(subject);
					email.setContent(content);
					mailUtils.sendEmail(email, null);
					emailBuffer.append(map.get("mail")).append(",");
				}
				//发短信
				if("1".equals(sendType.substring(1, 2)) && map.get("tele") != null){
					SmsUtil.sendSms(map.get("tele") + "", content);
					smsBuffer.append(map.get("tele")).append(",");
				}
			}
			if(emailBuffer.length() > 0){//已发送邮件运维人员
				emailBuffer.deleteCharAt(emailBuffer.length() - 1);
				invalidRecord.setEmailInstPersons(emailBuffer.toString());
			}
			if(smsBuffer.length() > 0){//已发送短信运维人员
				smsBuffer.deleteCharAt(smsBuffer.length() - 1);
				invalidRecord.setSmsInstPersons(smsBuffer.toString());
			}
			//发平台运维人员邮件
			if("1".equals(sendType.substring(2, 3))){
				Email email = new Email();
				String emailStr = LoadConfig.getString("TaskManagerEmail");
				email.setAddress(emailStr);
				email.setHtmlFlag(false);
				email.setSubject(subject);
				email.setContent("错误信息来自" + cache.getCache(Constants.CACHE_LOCAL_IP_ADDR) + ":"
						+ invalidRecord.getErrorMsg());
				mailUtils.sendEmail(email, null);
			}
			//发平台运维人员短信
			if("1".equals(sendType.substring(3, 4))){
				String teleStr = LoadConfig.getString("TaskManagerTel");
				if(!StringUtils.isBlank(teleStr)){
					SmsUtil.sendSms(teleStr, content);
				}
			}
			//存数据库
			recordService.insertLinksusTaskInvalidRecord(invalidRecord);
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
	}

}
