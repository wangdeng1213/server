package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationMarketing extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 主键 */
	private Long pid;
	/** 平台账号ID */
	private Long accountId;
	/** 帐号类型:1 新浪 2 腾讯 3 微信 */
	private Integer accountType;
	/** 机构ID */
	private Long institutionId;
	/** 营销对象 0我的粉丝,1潜在粉丝,2我的关注,3推荐关注 */
	private Integer marketingObject;
	/** 营销类型(1@,2评论,3短信,4邮件) */
	private Integer marketingType;
	/** 营销微博ID[1@的微博ID] */
	private Long weiboId;
	/** 营销标题[4邮件的标题] */
	private String marketingTitle;
	/** 营销内容 */
	private String marketingContent;
	/** 营销成功人数 */
	private Integer marketingSuccessNum;
	/** 营销失败人数 */
	private Integer marketingFailNum;
	/** 1 未审批  2 审批未通过  3  审批通过 */
	private Integer status;
	/** 当前审核的状态 00 未审批 10 一级审批完成 11 一级 二级审批均完成 处理过程在linksus_apply_operation */
	private String applyStatus;
	/** 创建时间 */
	private Long createTime;
	/** 更新时间 */
	private Integer updateTime;

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

	public Integer getAccountType(){
		return accountType;
	}

	public void setAccountType(Integer accountType){
		this.accountType = accountType;
	}

	public Long getInstitutionId(){
		return institutionId;
	}

	public void setInstitutionId(Long institutionId){
		this.institutionId = institutionId;
	}

	public Integer getMarketingObject(){
		return marketingObject;
	}

	public void setMarketingObject(Integer marketingObject){
		this.marketingObject = marketingObject;
	}

	public Integer getMarketingType(){
		return marketingType;
	}

	public void setMarketingType(Integer marketingType){
		this.marketingType = marketingType;
	}

	public Long getWeiboId(){
		return weiboId;
	}

	public void setWeiboId(Long weiboId){
		this.weiboId = weiboId;
	}

	public String getMarketingTitle(){
		return marketingTitle;
	}

	public void setMarketingTitle(String marketingTitle){
		this.marketingTitle = marketingTitle;
	}

	public String getMarketingContent(){
		return marketingContent;
	}

	public void setMarketingContent(String marketingContent){
		this.marketingContent = marketingContent;
	}

	public Integer getMarketingSuccessNum(){
		return marketingSuccessNum;
	}

	public void setMarketingSuccessNum(Integer marketingSuccessNum){
		this.marketingSuccessNum = marketingSuccessNum;
	}

	public Integer getMarketingFailNum(){
		return marketingFailNum;
	}

	public void setMarketingFailNum(Integer marketingFailNum){
		this.marketingFailNum = marketingFailNum;
	}

	public Integer getStatus(){
		return status;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public String getApplyStatus(){
		return applyStatus;
	}

	public void setApplyStatus(String applyStatus){
		this.applyStatus = applyStatus;
	}

	public Long getCreateTime(){
		return createTime;
	}

	public void setCreateTime(Long createTime){
		this.createTime = createTime;
	}

	public Integer getUpdateTime(){
		return updateTime;
	}

	public void setUpdateTime(Integer updateTime){
		this.updateTime = updateTime;
	}
}
