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
	 * ΢��������Ĺ�ϵ
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
	 *  @param  userId Long���� ΢���û�id 
	 *  @param  personId΢���û�������Աid
	 *  @param  accountId Long���� �û��������˺�id 
	 *  @param  accountType �˺����� ���� ��Ѷ ΢��
	 *  @param  weiboId   Long���� ΢��id
	 *  @param  interactType String���� �������� 
	 *  @param  messageId Long ������ϢId
	 *  @param  interactTime Integer ����ʱ�� �ӽӿڷ��صĻ���ʱ�� 
	 *  @return String
	 * @throws Exception 
	 * */
	public Long dealWeiboInteract(Long userId,Long personId,Long accountId,Integer accountType,Long weiboId,
			String interactType,Long messageId,Integer interactTime) throws Exception{
		//�ж�΢��������¼�����Ƿ���ڼ�¼ 
		LinksusInteractRecord interactRecord = new LinksusInteractRecord();
		interactRecord.setUserId(userId);
		interactRecord.setAccountId(accountId);
		if(!StringUtils.equals(interactType, "5") && !StringUtils.equals(interactType, "6")){
			interactRecord.setWeiboId(weiboId);
		}else{
			interactRecord.setWeiboId(0L);
		}
		LinksusInteractRecord linksusInteractRecord = linksusInteractRecordService.checkIsExsitRecord(interactRecord);
		Integer updateTime = DateUtil.getUnixDate(new Date());//����ʱ��
		Long recordId = 0L;
		if(linksusInteractRecord != null){//���ڸ��»�����¼��
			//����΢��������¼���е�����
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
			//�����µĻ�����¼����
			LinksusInteractRecord recordData = new LinksusInteractRecord();
			recordId = PrimaryKeyGen.getPrimaryKey("linksus_interact_record", "record_id");//������¼������
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
		//��黥����Ա���е������Ƿ����
		LinksusInteractPerson linksusInteractPerson = new LinksusInteractPerson();
		linksusInteractPerson.setPersonId(personId);
		linksusInteractPerson.setAccountId(accountId);
		LinksusInteractPerson interactPerson = linksusInteractPersonService
				.checkDataIsExsitInteractPerson(linksusInteractPerson);
		if(interactPerson == null){
			//�򻥶���Ա���в�������
			Long inteInteractId = PrimaryKeyGen.getPrimaryKey("linksus_interact_person", "interact_id");
			linksusInteractPerson.setInteractId(inteInteractId);
			linksusInteractPerson.setAccountType(accountType);
			linksusInteractPerson.setCount(1);//δ��������Ϊ1 
			linksusInteractPerson.setLastInteractTime(interactTime);//��������ʱ��
			linksusInteractPerson.setLastRecordId(recordId);//������¼id
			linksusInteractPerson.setUpdateTime(updateTime);//����ʱ��
			linksusInteractPersonService.insertLinksusInteractPerson(linksusInteractPerson);
			//�򻥶��û����в�������
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
			//��Ա���˺��л����Ļ�  ���»�����Ա���е���Ϣ 
			linksusInteractPerson.setLastInteractTime(interactTime);//��������ʱ��
			linksusInteractPerson.setLastRecordId(recordId);//������¼id
			linksusInteractPerson.setUpdateTime(updateTime);//����ʱ��
			linksusInteractPerson.setInteractId(personInteractid);
			//linksusInteractPersonService.updateLinksusInteractPerson(linksusInteractPerson);
			QueueDataSave.addDataToQueue(linksusInteractPerson, "update");
			//ȡ����Ա����id��userid�黥���û�����û�м�¼ �еĻ����� û�еĻ�����
			LinksusInteractUser interactUser = new LinksusInteractUser();
			interactUser.setInteractId(personInteractid);
			interactUser.setUserId(userId);
			LinksusInteractUser linksusInteractUser = linksusInteractUserService.checkUserDataIsExsit(interactUser);
			if(linksusInteractUser != null){
				//���»��� �û���
				LinksusInteractUser linksusInteract = new LinksusInteractUser();
				linksusInteract.setPid(linksusInteractUser.getPid());
				linksusInteract.setUpdateTime(updateTime);
				//linksusInteractUserService.updateLinksusInteractUser(linksusInteract);
				QueueDataSave.addDataToQueue(linksusInteract, "update");
			}else{
				//���뻥���û���  
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
		//			 //���»���������������Դ
		//			 Map valueMap= new HashMap();
		//			 valueMap.put("accountId",accountId);
		//			 valueMap.put("userId",userId);
		//			 LinksusRelationUserAccount userAccount = linksusRelationUserAccountService.getIsExsitWeiboUserAccount(valueMap);
		//�ж��˺��û��Ƿ���ڻ�����
		String strResult = RedisUtil.getRedisHash("relation_user_account", accountId + "#" + userId);
		if(!StringUtils.isBlank(strResult)){//�˺ź��û��й�ϵ
			String[] str = strResult.split("#");
			String interactTypeSource = str[1];
			Integer inType = new Integer(interactType);
			if(inType > 0 && inType <= 6){//��1λ ������ 2:��ת�� 3:ֱ��@�� 4:���۲�@�� 5:˽�Ź��� 6:΢�Ż���
				interactTypeSource.substring(0, inType - 1);
				interactTypeSource.substring(inType);
				//LinksusRelationUserAccount linksusUserAccount = new LinksusRelationUserAccount();
				// linksusUserAccount.setInteractType(interactTypeStr);
				//linksusUserAccount.setInteractNum(userAccount.getInteractNum()+1);//����������һ
				// linksusUserAccount.setAccountId(accountId);
				// linksusUserAccount.setUserId(userId);
				//linksusRelationUserAccountService.updateLinksusRelationUserAccountNum(linksusUserAccount);
				InteractCountSave.updateUserAcctInteractInfo(userId, accountId, inType);
			}
		}
		return recordId;
	}
}
