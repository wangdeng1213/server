package com.linksus.entity;

public class AttentionRelationEntrity{

	private Long pid;
	private Long accountId;
	private Long userId;
	private Integer accountType;
	private Long institutionId;
	private String token;
	private String openId;
	private String rpsId;
	private Long personId;

	public Long getPersonId(){
		return personId;
	}

	public void setPersonId(Long personId){
		this.personId = personId;
	}

	public String getRpsId(){
		return rpsId;
	}

	public void setRpsId(String rpsId){
		this.rpsId = rpsId;
	}

	public Long getInstitutionId(){
		return institutionId;
	}

	public void setInstitutionId(Long institutionId){
		this.institutionId = institutionId;
	}

	public Long getPid(){
		return pid;
	}

	public void setPid(Long pid){
		this.pid = pid;
	}

	public Long getAccountId(){
		return accountId;
	}

	public void setAccountId(Long accountId){
		this.accountId = accountId;
	}

	public Long getUserId(){
		return userId;
	}

	public void setUserId(Long userId){
		this.userId = userId;
	}

	public Integer getAccountType(){
		return accountType;
	}

	public void setAccountType(Integer accountType){
		this.accountType = accountType;
	}

	public String getToken(){
		return token;
	}

	public void setToken(String token){
		this.token = token;
	}

	public String getOpenId(){
		return openId;
	}

	public void setOpenId(String openId){
		this.openId = openId;
	}

}
