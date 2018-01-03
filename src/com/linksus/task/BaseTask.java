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
	//�������
	protected static CacheUtil cache = CacheUtil.getInstance();
	/** �˺����� */
	protected Integer accountType;
	/** ������ʱ�� */
	private long taskInterval = 0;
	/** �����ϴ����ʱ�� */
	private Long taskLastStartTime;
	/** ������ɱ�־ */
	private boolean finishFlag = false;
	/** ��ǰ��ҳ��ѯҳ�� */
	protected int currentPage = 1;
	protected int pageSize = 200;
	/** �߳���ֹ��־:true��ֹ,���ٴ�ѭ��ʱ���жϸ�ֵ */
	private boolean exitFlag = false;
	/** �������� */
	private String taskType;
	/** ����ִ��ͳ��MAP */
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
		taskStatistic.setTaskStatus(taskStatus);//ִ��״̬
		taskStatistic.setTaskSize(taskStatistic.getTaskSize() + taskSize);//������
		if(Constants.TASK_STATUS_FINISH.equals(taskStatus)){
			taskStatistic.setTaskCount(taskStatistic.getTaskCount() + 1);//ִ�д���
		}
	}

	/** �������� ȡ����Ҫ��������� ����ҳ,��ҳȡ��һ����Ҫ����������ɱ�ʶ���� */
	public abstract void cal();

	public void run(){
		while (!exitFlag){//�߳̿�ʼִ��
			//1 �ж�ˢ��ʱ����
			try{//��ֹ�쳣�ж�
				long time = getTaskIntervalTime();
				if(time >= 0 && taskInterval > time){//����ˢ��Ƶ��̫������ˢ��ʱ��
					try{
						updateTaskStatus(Constants.TASK_STATUS_SLEEP, 0);
						if(taskInterval - time > 60000){
							logger.debug(">>>>>>>>{}������ʱ��̫��,����1����", taskType);
							Thread.sleep(60000);
						}else{
							logger.debug(">>>>>>>>{}������ʱ��̫��,����{}��", taskType, (taskInterval - time) / 1000);
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
					}else if(finishFlag){//�ٴο�ʼ����
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
	 * ��������list��size�ж��Ƿ���ѯ��һ�� �����������ʱ��
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
	 * ֱ������������ѯ���ʱ��
	 */
	protected void setTaskListEndTime(int taskSize){
		finishFlag = true;
		updateTaskStatus(Constants.TASK_STATUS_FINISH, taskSize);
		currentPage = 1;
	}

	/**
	 * ȡ�ñ�������ִ�м��
	 * @return
	 */
	private long getTaskIntervalTime(){
		 //�������ִ��ʱ��Ϊ�� ����ִ�м��Ϊ0 ����-1 ˵���ǵ�һ��ִ��
		if(taskLastStartTime == null || taskInterval == 0){
			return -1;
		}else{
			if(finishFlag){//������� ��������ִ�м��
				return new Date().getTime() - taskLastStartTime;
			}//û����ɷ���-1
			return -1;
		}
	}

	public void setExitFlag(boolean exitFlag){
		this.exitFlag = exitFlag;
	}

	/**
	 * ������Ч����:΢������/����/Ӫ����
	 * @param invalidRecord
	 * @return
	 * @throws Exception 
	 */
	public static void dealInvalidRecord(LinksusTaskInvalidRecord invalidRecord){
		try{
			MailUtil mailUtils = (MailUtil) ContextUtil.getBean("mail"); //��ȡ�ʼ�����ʵ������
			int type = invalidRecord.getOperType().intValue();
			String subject = "";
			String content = "";
			String errorMsg = "";
			String sendType = "";//�жϷ�����Ա�ͷ�������
			if(Constants.INVALID_RECORD_EXCEPTION.equals(invalidRecord.getErrorCode())){
				errorMsg = "[90000-" + "ϵͳ�쳣]";
			}else{
				LinksusTaskErrorCode error = cache.getErrorCode(invalidRecord.getErrorCode());
				if(error.getDisplayType().intValue() == 0){
					errorMsg = "[" + invalidRecord.getErrorCode() + "-" + invalidRecord.getErrorMsg() + "]";
				}else{
					errorMsg = "[90000-" + "ϵͳ����]";
				}
			}
			if(type == Constants.INVALID_RECORD_SEND_WEIBO){
				sendType = LoadConfig.getString("invalid_1");
				subject = "΢�������쳣";
				content = "���ã�������1510cloud������΢������" + errorMsg + "����ʧ�ܡ����¼ƽ̨���·��͡��������ʣ��벦��绰��400-9909-111����1510cloud��";
			}else if(type == Constants.INVALID_RECORD_INTERACT_WEIBO){
				sendType = LoadConfig.getString("invalid_4");
				subject = "΢���������ݷ����쳣";
				content = "���ã�������1510cloud������΢���������ݣ���" + errorMsg
						+ "����ʧ�ܡ����¼ƽ̨���·��͡��������ʣ��벦��绰��400-9909-111����1510cloud��";
			}else if(type == Constants.INVALID_RECORD_MARKETING_AT
					|| type == Constants.INVALID_RECORD_MARKETING_COMMENT){
				sendType = LoadConfig.getString("invalid_2");
				subject = "΢��Ӫ�����ݷ����쳣";
				content = "���ã�΢��Ӫ�����ݷ����쳣����" + errorMsg + "����ʧ�ܡ�";
			}else if(type == Constants.INVALID_RECORD_INTERACT_MESSAGE){
				sendType = LoadConfig.getString("invalid_5");
				subject = "˽�����ݷ����쳣";
				content = "���ã�˽�����ݷ����쳣����" + errorMsg + "����ʧ�ܡ�";
			}else if(type == Constants.INVALID_RECORD_INTERACT_WEIXIN){
				sendType = LoadConfig.getString("invalid_6");
				subject = "΢�����ݷ����쳣";
				content = "���ã�΢�����ݷ����쳣����" + errorMsg + "����ʧ�ܡ�";
			}else if(type == Constants.INVALID_RECORD_ADD_ATTENTION){
				sendType = LoadConfig.getString("invalid_7");
				subject = "���ӹ�ע�����쳣";
				content = "���ã����ӹ�ע�����쳣����" + errorMsg + "����ʧ�ܡ�";
			}else if(type == Constants.INVALID_RECORD_CANCLE_ATTENTION){
				sendType = LoadConfig.getString("invalid_8");
				subject = "ȡ����ע�����쳣";
				content = "���ã�ȡ����ע�����쳣����" + errorMsg + "����ʧ�ܡ�";
			}else if(type == Constants.INVALID_RECORD_REMOVE_FANS){
				sendType = LoadConfig.getString("invalid_9");
				subject = "�Ƴ���˿�����쳣";
				content = "���ã��Ƴ���˿�����쳣����" + errorMsg + "����ʧ�ܡ�";
			}else if(type == Constants.INVALID_RECORD_INFO){
				sendType = LoadConfig.getString("invalid_10");
				subject = "�����쳣";
				content = "�����쳣����" + errorMsg + "����ʧ�ܡ�";
			}

			invalidRecord.setPid(PrimaryKeyGen.getPrimaryKey("linksus_task_invalid_record", "pid"));
			invalidRecord.setSourceIp((String) cache.getCache(Constants.CACHE_LOCAL_IP_ADDR));
			invalidRecord.setAddTime(DateUtil.getUnixDate(new Date()));
			invalidRecord.setEmailInstPersons("");
			invalidRecord.setSmsInstPersons("");

			//���ݻ�����ѯ��ά��Ա�ʼ�/������ϵ��ʽ
			List personAccountList = personAccountService.getPersonaccountByInstitutionId(invalidRecord
					.getInstitutionId());
			StringBuffer emailBuffer = new StringBuffer();
			StringBuffer smsBuffer = new StringBuffer();
			for(int i = 0; i < personAccountList.size(); i++){
				Map map = (Map) personAccountList.get(i);
				//���ʼ�
				if("1".equals(sendType.substring(0, 1)) && map.get("mail") != null){
					Email email = new Email();
					email.setAddress((String) map.get("mail"));
					email.setHtmlFlag(false);
					email.setSubject(subject);
					email.setContent(content);
					mailUtils.sendEmail(email, null);
					emailBuffer.append(map.get("mail")).append(",");
				}
				//������
				if("1".equals(sendType.substring(1, 2)) && map.get("tele") != null){
					SmsUtil.sendSms(map.get("tele") + "", content);
					smsBuffer.append(map.get("tele")).append(",");
				}
			}
			if(emailBuffer.length() > 0){//�ѷ����ʼ���ά��Ա
				emailBuffer.deleteCharAt(emailBuffer.length() - 1);
				invalidRecord.setEmailInstPersons(emailBuffer.toString());
			}
			if(smsBuffer.length() > 0){//�ѷ��Ͷ�����ά��Ա
				smsBuffer.deleteCharAt(smsBuffer.length() - 1);
				invalidRecord.setSmsInstPersons(smsBuffer.toString());
			}
			//��ƽ̨��ά��Ա�ʼ�
			if("1".equals(sendType.substring(2, 3))){
				Email email = new Email();
				String emailStr = LoadConfig.getString("TaskManagerEmail");
				email.setAddress(emailStr);
				email.setHtmlFlag(false);
				email.setSubject(subject);
				email.setContent("������Ϣ����" + cache.getCache(Constants.CACHE_LOCAL_IP_ADDR) + ":"
						+ invalidRecord.getErrorMsg());
				mailUtils.sendEmail(email, null);
			}
			//��ƽ̨��ά��Ա����
			if("1".equals(sendType.substring(3, 4))){
				String teleStr = LoadConfig.getString("TaskManagerTel");
				if(!StringUtils.isBlank(teleStr)){
					SmsUtil.sendSms(teleStr, content);
				}
			}
			//�����ݿ�
			recordService.insertLinksusTaskInvalidRecord(invalidRecord);
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
	}

}
