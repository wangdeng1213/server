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
 * ΢�Żظ�����
 * 
 */

public class TaskReplyWeixins extends BaseTask{

	LinksusInteractWeixinService linksusInteractWeixinService = (LinksusInteractWeixinService) ContextUtil
			.getBean("linksusInteractWeixinService");
	LinksusAppaccountService linksusAppaccountService = (LinksusAppaccountService) ContextUtil
			.getBean("linksusAppaccountService");
	LinksusInteractRecordService linksusInteractRecordService = (LinksusInteractRecordService) ContextUtil
			.getBean("linksusInteractRecordService");

	/**���ͷ�ʽ  ��ʱ/��ʱ */
	private String sendType;

	public void setSendType(String sendType){
		this.sendType = sendType;
	}

	@Override
	public void cal(){
		if("0".equals(sendType)){ //0�Ǽ�ʱ
			replyWeixinImmediate();
		}else if("1".equals(sendType)){//1�Ƕ�ʱ
			replyWeixinAtRegularTime();
		}
	}

	//��ʱ����΢��
	public void replyWeixinImmediate(){
		//��ȡ��ʱ�����б�
		LinksusInteractWeixin linksusWeixin = new LinksusInteractWeixin();
		int startCount = (currentPage - 1) * pageSize;
		linksusWeixin.setPageSize(pageSize);
		linksusWeixin.setStartCount(startCount);
		List<LinksusInteractWeixin> recordList = linksusInteractWeixinService.getTaskWeixinAtImmediate(linksusWeixin);
		if(recordList != null && recordList.size() > 0){
			for(LinksusInteractWeixin interactWeixin : recordList){
				//�жϻ���ʱ���Ƿ���48Сʱ��
				Date currDate = new Date();
				if(currDate.getTime() - interactWeixin.getInteractTime() * 1000L < 48 * 3600 * 1000L){
					//����΢��
					sendWeiXinTask(interactWeixin);
				}else{
					dealExceptionInvalidRecord(linksusWeixin.getAccountId(), linksusWeixin.getPid(), new Exception(
							"΢�Ż�������48Сʱ,���ܱ��ظ�"));
					logger.error("΢�Ż���:{},����48Сʱ,���ܱ��ظ�", interactWeixin.getPid());
				}
			}
		}
		checkTaskListEnd(recordList);//�ж������Ƿ���ѯ���
	}

	//��ʱ����΢��
	public void replyWeixinAtRegularTime(){
		//��ȡ��ʱ�����б�
		LinksusInteractWeixin linksusWeixin = new LinksusInteractWeixin();
		int startCount = (currentPage - 1) * pageSize;
		linksusWeixin.setPageSize(pageSize);
		linksusWeixin.setStartCount(startCount);
		List<LinksusInteractWeixin> recordList = linksusInteractWeixinService.getTaskWeixinAtRegularTime(linksusWeixin);
		if(recordList != null && recordList.size() > 0){
			for(LinksusInteractWeixin interactWeixin : recordList){
				//�жϻ���ʱ���Ƿ���48Сʱ��
				Date currDate = new Date();
				if(currDate.getTime() - interactWeixin.getInteractTime() * 1000L < 48 * 3600 * 1000L){
					addWeixinTimer(interactWeixin);
				}else{
					dealExceptionInvalidRecord(linksusWeixin.getAccountId(), linksusWeixin.getPid(), new Exception(
							"΢�Ż�������48Сʱ,���ܱ��ظ�"));
					logger.error("΢�Ż���:{},����48Сʱ,���ܱ��ظ�", interactWeixin.getPid());
				}
			}
		}
		checkTaskListEnd(recordList);//�ж������Ƿ���ѯ���
	}

	//����΢��
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
				//���»�����Ϣ��
				Integer currentTime = Integer.parseInt(String.valueOf(new Date().getTime() / 1000));
				if(StringUtils.isNotBlank(resStr) && resStr.equals("sucess")){//�ɹ�
					interactWeixin.setStatus(2);
					interactWeixin.setInteractTime(currentTime);
					linksusInteractWeixinService.updateLinksusInteractWeixinStatus(interactWeixin);
					return "#�ظ�΢�ŷ����ɹ�:" + resStr;
				}else{//ʧ��
					String errorContent = "";
					String errcode = "";
					if(Constants.WECHATNOTEXISTENT.equals(resStr)){
						resStr = "20035";
						errcode = resStr;
						errorContent = "��ǰ�û�δ��ע���˻�,���ܱ��ظ�!";
						interactWeixin.setStatus(4);
					}else{
						interactWeixin.setStatus(3);
						errorContent = resStr;
						errcode = JsonUtil.getNodeValueByName(resStr, "errcode");
						resStr = "#�ظ�΢�ŷ���ʧ��:" + resStr;
					}
					dealErrorCodeInvalidRecord(interactWeixin.getAccountId(), interactWeixin.getPid(), errcode,
							errorContent);
					interactWeixin.setInteractTime(currentTime);
					linksusInteractWeixinService.updateLinksusInteractWeixinStatus(interactWeixin);
					return resStr;
				}
			}else{
				logger.info("--------------tokenȡֵ����------------");
			}
		}else{
			dealExceptionInvalidRecord(interactWeixin.getAccountId(), interactWeixin.getPid(), new Exception(
					"΢�Ż�������48Сʱ,���ܱ��ظ�"));
			resStr = "20029";
		}
		return resStr;
	}

	/**
	 * ΢�Ŷ�ʱ����
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

	//ǰ�˵��ã���ʱ����ʱ�ظ�΢��
	public String sendReplyWeixin(LinksusInteractWeixin record){
		String rsMsg = null;
		if(record != null){
			if(record.getSendType() == 0){
				//����΢��
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
