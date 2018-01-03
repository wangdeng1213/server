package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationGroup extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ����ID */
	private Long groupId;
	/** ����ID */
	private Long institutionId;
	/** �������� */
	private String groupName;
	/** �Ƿ��������:1 ��,0 �� */
	private Integer groupType;
	/** �˺����� */
	private Integer accountType;
	/** �˺�ID ����linksus_appaccount���� */
	private Long accountId;
	/** ��ӹ�ע������:��Χ����|�ָ� */
	private String addDate;
	/** ��˿��:��Χ����|�ָ� */
	private String fansNum;
	/** ��Ѷ���:�����|�ָ� */
	private String accountIdQq;
	/** �������:�����|�ָ� */
	private String accountIdSina;
	/** �û��Ա� �����|�ָ� */
	private String rpsGender;
	/** �û����� �����|�ָ� */
	private String fansQuality;
	/** ������ʽ:���۵� �����|�ָ� */
	private String relationType;
	/** ��ϵ��Դ:�������� �����|�ָ� */
	private String relationSource;
	/** �������:��ѡ */
	private Long areaCode;
	/** ִ��״̬:1 δִ�� 3 ִ���� 2  ��ִ��(���Զ��������,��������ֻ��2����) */
	private Integer status;
	/** ����ʱ�� */
	private Integer lastUpdateTime;

	public Long getGroupId(){
		return groupId;
	}

	public void setGroupId(Long groupId){
		this.groupId = groupId;
	}

	public Long getInstitutionId(){
		return institutionId;
	}

	public void setInstitutionId(Long institutionId){
		this.institutionId = institutionId;
	}

	public String getGroupName(){
		return groupName;
	}

	public void setGroupName(String groupName){
		this.groupName = groupName;
	}

	public Integer getAccountType(){
		return accountType;
	}

	public void setAccountType(Integer accountType){
		this.accountType = accountType;
	}

	public Long getAccountId(){
		return accountId;
	}

	public void setAccountId(Long accountId){
		this.accountId = accountId;
	}

	public String getAddDate(){
		return addDate;
	}

	public void setAddDate(String addDate){
		this.addDate = addDate;
	}

	public String getFansNum(){
		return fansNum;
	}

	public void setFansNum(String fansNum){
		this.fansNum = fansNum;
	}

	public String getRpsGender(){
		return rpsGender;
	}

	public void setRpsGender(String rpsGender){
		this.rpsGender = rpsGender;
	}

	public String getFansQuality(){
		return fansQuality;
	}

	public void setFansQuality(String fansQuality){
		this.fansQuality = fansQuality;
	}

	public String getRelationType(){
		return relationType;
	}

	public void setRelationType(String relationType){
		this.relationType = relationType;
	}

	public String getRelationSource(){
		return relationSource;
	}

	public void setRelationSource(String relationSource){
		this.relationSource = relationSource;
	}

	public Integer getStatus(){
		return status;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getLastUpdateTime(){
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Integer lastUpdateTime){
		this.lastUpdateTime = lastUpdateTime;
	}

	public Integer getGroupType(){
		return groupType;
	}

	public void setGroupType(Integer groupType){
		this.groupType = groupType;
	}

	public String getAccountIdQq(){
		return accountIdQq;
	}

	public void setAccountIdQq(String accountIdQq){
		this.accountIdQq = accountIdQq;
	}

	public String getAccountIdSina(){
		return accountIdSina;
	}

	public void setAccountIdSina(String accountIdSina){
		this.accountIdSina = accountIdSina;
	}

	public Long getAreaCode(){
		return areaCode;
	}

	public void setAreaCode(Long areaCode){
		this.areaCode = areaCode;
	}

}
