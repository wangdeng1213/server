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
	 * �ж��˻��û���ϵ�����Ƿ���ڼ�¼�������޸Ĺ�ϵ״̬���������������,������δ������Ϣ
	 *  @param  accountId String ƽ̨�ʺ�id
	 *  @param  userId  Long  �û�id
	 *  @param  personId Long ��Աid
	 *  @param  institutionId Long ����id
	 *  @param  appType String ���� 1Ϊ���ˣ�2Ϊ��Ѷ
	 *  @param  relationType String ���� 1Ϊ��˿��2Ϊ�ҵĹ�ע ��3Ϊ����
	 *  @return int interactBit  0���ǻ��� 1�� ������ 2:��ת�� 3:ֱ��@�� 4:���۲�@�� 5:˽�Ź���'6:΢�Ż���
	 * @throws Exception 
	 * */
	public void dealRelationUserAccount(long accountId,long userId,long personId,long institutionId,String appType,
			String relationType,int interactBit) throws Exception{
		boolean updateFlag=true;
		if(StringUtils.isNotBlank(relationType)){
			//�жϸ��û����˺��Ƿ��й�ϵ ��ִ���޸Ĳ��� û��ִ�в���accountId 
			//			  Map valueMap= new HashMap();
			//			  valueMap.put("accountId",accountId );
			//			  valueMap.put("userId",userId);
			//		      LinksusRelationUserAccount userAccount = linksusUserAccountService.getIsExsitWeiboUserAccount(valueMap);
			//�ж��˺��û��Ƿ���ڻ�����
			String strResult = RedisUtil.getRedisHash("relation_user_account", accountId + "#" + userId);
			LinksusRelationUserAccount linksusUserAccount = new LinksusRelationUserAccount();
			if(StringUtils.isBlank(strResult)){//�˺ź��û�û�й�ϵ
				Long pid = PrimaryKeyGen.getPrimaryKey("linksus_relation_user_account", "pid");
				linksusUserAccount.setPid(pid);
				linksusUserAccount.setAccountId(accountId);
				linksusUserAccount.setAccountType(Integer.valueOf(appType));
				linksusUserAccount.setUserId(userId);
				linksusUserAccount.setInstitutionId(institutionId);
				linksusUserAccount.setPersonId(personId);
				if(relationType.equals("1")){//������˿��ϵ
					linksusUserAccount.setFlagRelation(1);
					//���üӷ�˿ʱ��
					linksusUserAccount.setFansTime(DateUtil.getUnixDate(new Date()));
					if(StringUtils.equals(appType, "2")){
						LinksusTaskAttentionUser user = new LinksusTaskAttentionUser();
						user.setPid(PrimaryKeyGen.getPrimaryKey("linksus_task_attention_user", "pid"));
						user.setAccountId(accountId);
						user.setUserId(userId);
						user.setAccountType(Integer.valueOf(appType));//����
						user.setStatus(1);
						user.setInteractTime(0);
						user.setCreateTime(DateUtil.getUnixDate(new Date()));
						//�ж��·�˿�����Ƿ����
						Integer countUser = userService.countLinksusTaskAttentionUser(user);
						if(countUser == 0){
							userService.insertLinksusTaskAttentionUser(user);
						}
					}
				}else if(relationType.equals("2")){//�����ҵĹ�ע
					linksusUserAccount.setFlagRelation(2);
					//���ù�עʱ��
					linksusUserAccount.setAttentionTime(DateUtil.getUnixDate(new Date()));
				}else if(relationType.equals("3")){//����ΪǱ�ڹ�ϵ
					linksusUserAccount.setFlagRelation(4);
					//������󻥶�ʱ��
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
				//�жϹ�ϵ״̬ 
				//����΢���û�����ϸ��Ϣ
				String[] str = strResult.split("#");
				String flagRe = "0";
				String interactType = "000000";
				if(str != null && str.length >= 2){
					flagRe = str[0]; //�û��˺�������ϵ
					interactType = str[1];
				}
				Integer flagRelation = 0;//�������
				if(StringUtils.equals(relationType, "1")){//���·�˿
					//���üӷ�˿ʱ��
					linksusUserAccount.setFansTime(DateUtil.getUnixDate(new Date()));
					if("0".equals(flagRe) || "4".equals(flagRe)){//��ϵ������0��4�Ļ�
						flagRelation = 1;
						//�����·�˿��
						if(StringUtils.equals(appType, "2")){
							LinksusTaskAttentionUser user = new LinksusTaskAttentionUser();
							user.setPid(PrimaryKeyGen.getPrimaryKey("linksus_task_attention_user", "pid"));
							user.setAccountId(accountId);
							user.setUserId(userId);
							user.setAccountType(Integer.valueOf(appType));//����
							user.setStatus(1);
							user.setInteractTime(0);
							user.setCreateTime(DateUtil.getUnixDate(new Date()));
							//�ж��·�˿�����Ƿ����
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
							user.setAccountType(Integer.valueOf(appType));//����
							user.setStatus(1);
							user.setInteractTime(0);
							user.setCreateTime(DateUtil.getUnixDate(new Date()));
							//�ж��·�˿�����Ƿ����
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
				}else if(StringUtils.equals(relationType, "2")){//�����ҵĹ�ע
					//���ù�עʱ��
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
					//������󻥶�ʱ��
					//linksusUserAccount.setInteractTime(DateUtil.getUnixDate(new Date()));
					updateFlag=false;
				}
				if(updateFlag){
					if(interactBit > 0){
						String start = interactType.substring(0, interactBit - 1);
						String end = interactType.substring(interactBit);
						interactType = start + "1" + end;
					}
					//ִ�и��²���
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
		//�жϸû����ڷ�����Ϣ�����Ƿ���ڡ�δ���顱 "�ҵ�����"
		String hashKey = "rel_groupdef";
		String fieldKey = institutionId + "#" + groupType;
		String groupIdStr = RedisUtil.getRedisHash(hashKey, fieldKey);
		Long groupId = null;

		//���治���ڵ��Ȳ�ѯ���ݿ�
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
				groupName = "δ����";
			}else if("4".equals(groupType)){
				groupName = "����ӵ�";
			}else if("5".equals(groupType)){
				groupName = "�ҵ�����";
			}
			groupId = PrimaryKeyGen.getPrimaryKey("linksus_relation_group", "group_id");
			//����"δ����"��Ϣ
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
		//�ж���Ա������Ƿ����
		Map paramMap = new HashMap();
		paramMap.put("groupId", groupId);
		paramMap.put("personId", personId);
		LinksusRelationPersonGroup rersonGroup = linksusRelationPersonGroupService
				.getLinksusRelationPersonGroupById(paramMap);
		//������Ա�����
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
