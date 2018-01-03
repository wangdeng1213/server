package com.linksus.entity;

import java.util.List;

public class InteractListQueryObj extends BaseEntity{

	/** ƽ̨�˺�ID ��ά��ԱȨ���˺�*/
	private List<Long> accountIds;
	/** ����״̬:0 δ���� 1:�ѷ��� 2:���� */
	private Integer status;
	/** ��ά��ԱID:�ѷ���ʱ��ֵ */
	private Long instPersonId;
	/** ������ */
	private Long institutionId;
	/** ��ǩ����/�ǳ� */
	private String queryName;
	/** ƽ̨*/
	private List<Integer> userTypes;
	/** ����ʱ�� */
	private Integer interactTimeBegin;
	private Integer interactTimeEnd;
	/** ������Դ:1 ���� 2 ת�� 3 @ 4 ���۲�@ 5 ˽�� 6 ΢�� */
	private List<Integer> interactTypes;
	/** ���� */
	private List<Long> personGroups;
	/** ��˿��|������ */
	private Integer followersCountMin;
	private Integer followersCountMax;
	/**'��֤�����־-1��ͨ�û� 2 ������֤ 3 ��ҵ��֤'*/
	private List<Integer> verifiedTypeFlags;
	private List<Integer> sinaVerifiedTypes;
	private List<Integer> tencentVerifiedTypes;
	/** �Ա�m���С�f��Ů��n��δ֪  */
	private List<String> rpsGenders;
	/** �û����ڹ���ID */
	private String countryCode;
	/** �û����ڵ���ID */
	private String rpsProvince;
	/** �û����ڳ���ID */
	private String rpsCity;
	/** �ѷ��������ID */
	private List<Long> dealPersonIds;

	private Integer page;

	public Integer getStatus(){
		return status;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Long getInstPersonId(){
		return instPersonId;
	}

	public void setInstPersonId(Long instPersonId){
		this.instPersonId = instPersonId;
	}

	public List<Integer> getUserTypes(){
		return userTypes;
	}

	public void setUserTypes(List<Integer> userTypes){
		this.userTypes = userTypes;
	}

	public Integer getInteractTimeBegin(){
		return interactTimeBegin;
	}

	public void setInteractTimeBegin(Integer interactTimeBegin){
		this.interactTimeBegin = interactTimeBegin;
	}

	public Integer getInteractTimeEnd(){
		return interactTimeEnd;
	}

	public void setInteractTimeEnd(Integer interactTimeEnd){
		this.interactTimeEnd = interactTimeEnd;
	}

	public List<Integer> getInteractTypes(){
		return interactTypes;
	}

	public void setInteractTypes(List<Integer> interactTypes){
		this.interactTypes = interactTypes;
	}

	public List<Long> getPersonGroups(){
		return personGroups;
	}

	public void setPersonGroups(List<Long> personGroups){
		this.personGroups = personGroups;
	}

	public Integer getFollowersCountMin(){
		return followersCountMin;
	}

	public void setFollowersCountMin(Integer followersCountMin){
		this.followersCountMin = followersCountMin;
	}

	public Integer getFollowersCountMax(){
		return followersCountMax;
	}

	public void setFollowersCountMax(Integer followersCountMax){
		this.followersCountMax = followersCountMax;
	}

	public List<Integer> getVerifiedTypeFlags(){
		return verifiedTypeFlags;
	}

	public void setVerifiedTypeFlags(List<Integer> verifiedTypeFlags){
		this.verifiedTypeFlags = verifiedTypeFlags;
	}

	public List<String> getRpsGenders(){
		return rpsGenders;
	}

	public void setRpsGenders(List<String> rpsGenders){
		this.rpsGenders = rpsGenders;
	}

	public String getCountryCode(){
		return countryCode;
	}

	public void setCountryCode(String countryCode){
		this.countryCode = countryCode;
	}

	public String getRpsProvince(){
		return rpsProvince;
	}

	public void setRpsProvince(String rpsProvince){
		this.rpsProvince = rpsProvince;
	}

	public String getRpsCity(){
		return rpsCity;
	}

	public void setRpsCity(String rpsCity){
		this.rpsCity = rpsCity;
	}

	public List<Long> getAccountIds(){
		return accountIds;
	}

	public void setAccountIds(List<Long> accountIds){
		this.accountIds = accountIds;
	}

	public String getQueryName(){
		return queryName;
	}

	public void setQueryName(String queryName){
		this.queryName = queryName;
	}

	public Long getInstitutionId(){
		return institutionId;
	}

	public void setInstitutionId(Long institutionId){
		this.institutionId = institutionId;
	}

	public Integer getPage(){
		return page;
	}

	public void setPage(Integer page){
		this.page = page;
	}

	public List<Integer> getSinaVerifiedTypes(){
		return sinaVerifiedTypes;
	}

	public void setSinaVerifiedTypes(List<Integer> sinaVerifiedTypes){
		this.sinaVerifiedTypes = sinaVerifiedTypes;
	}

	public List<Integer> getTencentVerifiedTypes(){
		return tencentVerifiedTypes;
	}

	public void setTencentVerifiedTypes(List<Integer> tencentVerifiedTypes){
		this.tencentVerifiedTypes = tencentVerifiedTypes;
	}

	public List<Long> getDealPersonIds(){
		return dealPersonIds;
	}

	public void setDealPersonIds(List<Long> dealPersonIds){
		this.dealPersonIds = dealPersonIds;
	}

}
