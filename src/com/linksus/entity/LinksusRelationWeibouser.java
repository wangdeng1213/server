package com.linksus.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class LinksusRelationWeibouser extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 主键 */
	private Long userId;
	/** 新浪腾讯微博用户标识 用于API请求  */
	private String rpsId = "";
	/** 用户类型 1:新浪 2 腾讯 */
	private Integer userType = 0;
	/** 人员ID:关联人员主表 */
	private Long personId = 0L;
	/** 用户昵称 */
	private String rpsScreenName = "";
	/** 友好显示名称|用户账户名 */
	private String rpsName = "";
	/** 用户所在国家ID */
	private String countryCode = "";
	/** 用户所在地区ID */
	private String rpsProvince = "";
	/** 用户所在城市ID */
	private String rpsCity = "";
	/** 用户所在地 */
	private String rpsLocation = "";
	/** 用户描述|个人介绍 */
	private String rpsDescription = "";
	/** 用户博客地址|个人主页 */
	private String rpsUrl = "";
	/** 用户头像地址 */
	private String rpsProfileImageUrl = "";
	/** 生日 */
	private String birthDay = "";
	/** 用户的个性化域名 */
	private String rpsDomain = "";
	/** 性别，m：男、f：女、n：未知  */
	private String rpsGender = "";
	/** 粉丝数|听众数 */
	private Integer rpsFollowersCount = 0;
	/** 关注数|收听的人数 */
	private Integer rpsFriendsCount = 0;
	/** 用户的互粉数|互听好友数 */
	private Integer rpsBiFollowersCount = 0;
	/** 微博数 */
	private Integer rpsStatusesCount = 0;
	/** 收藏数 */
	private Integer rpsFavouritesCount = 0;
	/** 创建时间 */
	private Integer rpsCreatedAt = 0;
	/** 是否允许所有人给我发私信 */
	private Integer rpsAllowAllActMsg = 0;
	/** 是否允许带有地理信息 */
	private Integer rpsGeoEnabled = 0;
	/** 是否是微博认证用户，即带V用户 */
	private Integer rpsVerified = 0;
	/** 认证种类-0 黄V,  1-7 蓝V,10  微博女郎,  200 220 微博达人 | 30 企业实名认证 31 企业未实名认证 32 个人实名认证 33 个人未实名认证 */
	private Integer rpsVerifiedType = 0;
	/**'认证种类标志-1普通用户 2 个人认证 3 企业认证'*/
	private Integer rpsVerifiedTypeFlag = 0;
	/** 是否允许所有人对我的微博进行评论 */
	private Integer rpsAllowAllComment = 0;
	/** 用户大头像地址 */
	private String rpsAvatarLarge = "";
	/** 认证原因|认证信息 */
	private String rpsVerifiedReason = "";
	/** 评论数 */
	private Integer rpsCommentNum = 0;
	/** @我数 */
	private Integer rpsAtmeNum = 0;
	/** 覆盖度 */
	private Integer rpsCoverageNum = 0;
	/** 行业ID */
	private String industryCode = "";
	/** 最后一条微博id */
	private String lastweiboid = "";
	/** 最新微博的发布时间 */
	private String lastweibotime = "";
	/** 用户标签 */
	private String tags = "";
	/** 用户质量:3高质量，2普通，1低质量，0未标记 */
	private Integer fansQuality = 0;
	/** 微博等级 */
	private Integer weiboLevel = 0;
	/** 同步时间 */
	private Integer sytime = 0;
	/** 微博创建时间 */
	private Integer weiboCreateTime = 0;
	/** 微博同步时间 */
	private Integer weiboLastSytime;
	/** 最新同步微博mid */
	private Long weiboLastMid;
	/** 微博最新同步时间戳*/
	private Integer weiboPageTime;
	/** 机构ID */
	private Long institutionId;
	/** 关注关系 */
	private Integer flagRelation;
	/** 第三方平台唯一ID */
	private String appid;
	/** 账号 */
	private Long accountId;
	/** 账号类型 */
	private int accountType;
	/** 更新粉丝列表索引标识位*/
	private Long lastUserId;

	private List tagList;
	private Set tagSet;
	//用户返回的教育信息
	private List eduList;
	//用户返回的工作信息
	private List compList;

	public List getEduList(){
		return eduList;
	}

	public void setEduList(List eduList){
		this.eduList = eduList;
	}

	public List getCompList(){
		return compList;
	}

	public void setCompList(List compList){
		this.compList = compList;
	}

	public Set getTagSet(){
		return tagSet;
	}

	public void setTagSet(Set tagSet){
		this.tagSet = tagSet;
	}

	public List getTagList(){
		return tagList;
	}

	public void setTagList(List tagList){
		this.tagList = tagList;
	}

	public Integer getRpsVerifiedTypeFlag(){
		return rpsVerifiedTypeFlag;
	}

	public void setRpsVerifiedTypeFlag(Integer rpsVerifiedTypeFlag){
		this.rpsVerifiedTypeFlag = rpsVerifiedTypeFlag;
	}

	public Long getUserId(){
		return userId;
	}

	public void setUserId(Long userId){
		this.userId = userId;
	}

	public String getRpsId(){
		return rpsId;
	}

	public void setRpsId(String rpsId){
		this.rpsId = rpsId;
	}

	public Integer getUserType(){
		return userType;
	}

	public void setUserType(Integer userType){
		this.userType = userType;
	}

	public Long getPersonId(){
		return personId;
	}

	public void setPersonId(Long personId){
		this.personId = personId;
	}

	public String getRpsScreenName(){
		return rpsScreenName;
	}

	public void setRpsScreenName(String rpsScreenName){
		this.rpsScreenName = rpsScreenName;
	}

	public String getRpsName(){
		return rpsName;
	}

	public void setRpsName(String rpsName){
		this.rpsName = rpsName;
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

	public String getRpsLocation(){
		return rpsLocation;
	}

	public void setRpsLocation(String rpsLocation){
		this.rpsLocation = rpsLocation;
	}

	public String getRpsDescription(){
		return rpsDescription;
	}

	public void setRpsDescription(String rpsDescription){
		this.rpsDescription = rpsDescription;
	}

	public String getRpsUrl(){
		return rpsUrl;
	}

	public void setRpsUrl(String rpsUrl){
		this.rpsUrl = rpsUrl;
	}

	public String getRpsProfileImageUrl(){
		return rpsProfileImageUrl;
	}

	public void setRpsProfileImageUrl(String rpsProfileImageUrl){
		this.rpsProfileImageUrl = rpsProfileImageUrl;
	}

	public String getBirthDay(){
		return birthDay;
	}

	public void setBirthDay(String birthDay){
		this.birthDay = birthDay;
	}

	public String getRpsDomain(){
		return rpsDomain;
	}

	public void setRpsDomain(String rpsDomain){
		this.rpsDomain = rpsDomain;
	}

	public String getRpsGender(){
		return rpsGender;
	}

	public void setRpsGender(String rpsGender){
		this.rpsGender = rpsGender;
	}

	public Integer getRpsFollowersCount(){
		return rpsFollowersCount;
	}

	public void setRpsFollowersCount(Integer rpsFollowersCount){
		this.rpsFollowersCount = rpsFollowersCount;
	}

	public Integer getRpsFriendsCount(){
		return rpsFriendsCount;
	}

	public void setRpsFriendsCount(Integer rpsFriendsCount){
		this.rpsFriendsCount = rpsFriendsCount;
	}

	public Integer getRpsBiFollowersCount(){
		return rpsBiFollowersCount;
	}

	public void setRpsBiFollowersCount(Integer rpsBiFollowersCount){
		this.rpsBiFollowersCount = rpsBiFollowersCount;
	}

	public Integer getRpsStatusesCount(){
		return rpsStatusesCount;
	}

	public void setRpsStatusesCount(Integer rpsStatusesCount){
		this.rpsStatusesCount = rpsStatusesCount;
	}

	public Integer getRpsFavouritesCount(){
		return rpsFavouritesCount;
	}

	public void setRpsFavouritesCount(Integer rpsFavouritesCount){
		this.rpsFavouritesCount = rpsFavouritesCount;
	}

	public Integer getRpsCreatedAt(){
		return rpsCreatedAt;
	}

	public void setRpsCreatedAt(Integer rpsCreatedAt){
		this.rpsCreatedAt = rpsCreatedAt;
	}

	public Integer getRpsAllowAllActMsg(){
		return rpsAllowAllActMsg;
	}

	public void setRpsAllowAllActMsg(Integer rpsAllowAllActMsg){
		this.rpsAllowAllActMsg = rpsAllowAllActMsg;
	}

	public Integer getRpsGeoEnabled(){
		return rpsGeoEnabled;
	}

	public void setRpsGeoEnabled(Integer rpsGeoEnabled){
		this.rpsGeoEnabled = rpsGeoEnabled;
	}

	public Integer getRpsVerified(){
		return rpsVerified;
	}

	public void setRpsVerified(Integer rpsVerified){
		this.rpsVerified = rpsVerified;
	}

	public Integer getRpsVerifiedType(){
		return rpsVerifiedType;
	}

	public void setRpsVerifiedType(Integer rpsVerifiedType){
		this.rpsVerifiedType = rpsVerifiedType;
	}

	public Integer getRpsAllowAllComment(){
		return rpsAllowAllComment;
	}

	public void setRpsAllowAllComment(Integer rpsAllowAllComment){
		this.rpsAllowAllComment = rpsAllowAllComment;
	}

	public String getRpsAvatarLarge(){
		return rpsAvatarLarge;
	}

	public void setRpsAvatarLarge(String rpsAvatarLarge){
		this.rpsAvatarLarge = rpsAvatarLarge;
	}

	public String getRpsVerifiedReason(){
		return rpsVerifiedReason;
	}

	public void setRpsVerifiedReason(String rpsVerifiedReason){
		this.rpsVerifiedReason = rpsVerifiedReason;
	}

	public Integer getRpsCommentNum(){
		return rpsCommentNum;
	}

	public void setRpsCommentNum(Integer rpsCommentNum){
		this.rpsCommentNum = rpsCommentNum;
	}

	public Integer getRpsAtmeNum(){
		return rpsAtmeNum;
	}

	public void setRpsAtmeNum(Integer rpsAtmeNum){
		this.rpsAtmeNum = rpsAtmeNum;
	}

	public Integer getRpsCoverageNum(){
		return rpsCoverageNum;
	}

	public void setRpsCoverageNum(Integer rpsCoverageNum){
		this.rpsCoverageNum = rpsCoverageNum;
	}

	public String getIndustryCode(){
		return industryCode;
	}

	public void setIndustryCode(String industryCode){
		this.industryCode = industryCode;
	}

	public String getLastweiboid(){
		return lastweiboid;
	}

	public void setLastweiboid(String lastweiboid){
		this.lastweiboid = lastweiboid;
	}

	public String getLastweibotime(){
		return lastweibotime;
	}

	public void setLastweibotime(String lastweibotime){
		this.lastweibotime = lastweibotime;
	}

	public String getTags(){
		return tags;
	}

	public void setTags(String tags){
		this.tags = tags;
	}

	public Integer getFansQuality(){
		return fansQuality;
	}

	public void setFansQuality(Integer fansQuality){
		this.fansQuality = fansQuality;
	}

	public Integer getWeiboLevel(){
		return weiboLevel;
	}

	public void setWeiboLevel(Integer weiboLevel){
		this.weiboLevel = weiboLevel;
	}

	public Integer getSytime(){
		return sytime;
	}

	public void setSytime(Integer sytime){
		this.sytime = sytime;
	}

	public Integer getWeiboCreateTime(){
		return weiboCreateTime;
	}

	public void setWeiboCreateTime(Integer weiboCreateTime){
		this.weiboCreateTime = weiboCreateTime;
	}

	public Integer getWeiboLastSytime(){
		return weiboLastSytime;
	}

	public void setWeiboLastSytime(Integer weiboLastSytime){
		this.weiboLastSytime = weiboLastSytime;
	}

	public Long getWeiboLastMid(){
		return weiboLastMid;
	}

	public void setWeiboLastMid(Long weiboLastMid){
		this.weiboLastMid = weiboLastMid;
	}

	public Integer getWeiboPageTime(){
		return weiboPageTime;
	}

	public void setWeiboPageTime(Integer weiboPageTime){
		this.weiboPageTime = weiboPageTime;
	}

	public Long getInstitutionId(){
		return institutionId;
	}

	public void setInstitutionId(Long institutionId){
		this.institutionId = institutionId;
	}

	public Integer getFlagRelation(){
		return flagRelation;
	}

	public void setFlagRelation(Integer flagRelation){
		this.flagRelation = flagRelation;
	}

	public String getAppid(){
		return appid;
	}

	public void setAppid(String appid){
		this.appid = appid;
	}

	public int getAccountType(){
		return accountType;
	}

	public void setAccountType(int accountType){
		this.accountType = accountType;
	}

	public Long getAccountId(){
		return accountId;
	}

	public void setAccountId(Long accountId){
		this.accountId = accountId;
	}

	public Long getLastUserId() {
		return lastUserId;
	}

	public void setLastUserId(Long lastUserId) {
		this.lastUserId = lastUserId;
	}
	
	

}
