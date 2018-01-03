package com.linksus.entity;

import java.util.List;

public class InteractListQueryObj extends BaseEntity{

	/** 平台账号ID 运维人员权限账号*/
	private List<Long> accountIds;
	/** 分配状态:0 未分配 1:已分配 2:忽略 */
	private Integer status;
	/** 运维人员ID:已分配时有值 */
	private Long instPersonId;
	/** 机构号 */
	private Long institutionId;
	/** 标签名称/昵称 */
	private String queryName;
	/** 平台*/
	private List<Integer> userTypes;
	/** 互动时间 */
	private Integer interactTimeBegin;
	private Integer interactTimeEnd;
	/** 互动来源:1 评论 2 转发 3 @ 4 评论并@ 5 私信 6 微信 */
	private List<Integer> interactTypes;
	/** 分组 */
	private List<Long> personGroups;
	/** 粉丝数|听众数 */
	private Integer followersCountMin;
	private Integer followersCountMax;
	/**'认证种类标志-1普通用户 2 个人认证 3 企业认证'*/
	private List<Integer> verifiedTypeFlags;
	private List<Integer> sinaVerifiedTypes;
	private List<Integer> tencentVerifiedTypes;
	/** 性别，m：男、f：女、n：未知  */
	private List<String> rpsGenders;
	/** 用户所在国家ID */
	private String countryCode;
	/** 用户所在地区ID */
	private String rpsProvince;
	/** 用户所在城市ID */
	private String rpsCity;
	/** 已分配操作人ID */
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
