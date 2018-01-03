package com.linksus.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class LinksusRelationWeibouser extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ���� */
	private Long userId;
	/** ������Ѷ΢���û���ʶ ����API����  */
	private String rpsId = "";
	/** �û����� 1:���� 2 ��Ѷ */
	private Integer userType = 0;
	/** ��ԱID:������Ա���� */
	private Long personId = 0L;
	/** �û��ǳ� */
	private String rpsScreenName = "";
	/** �Ѻ���ʾ����|�û��˻��� */
	private String rpsName = "";
	/** �û����ڹ���ID */
	private String countryCode = "";
	/** �û����ڵ���ID */
	private String rpsProvince = "";
	/** �û����ڳ���ID */
	private String rpsCity = "";
	/** �û����ڵ� */
	private String rpsLocation = "";
	/** �û�����|���˽��� */
	private String rpsDescription = "";
	/** �û����͵�ַ|������ҳ */
	private String rpsUrl = "";
	/** �û�ͷ���ַ */
	private String rpsProfileImageUrl = "";
	/** ���� */
	private String birthDay = "";
	/** �û��ĸ��Ի����� */
	private String rpsDomain = "";
	/** �Ա�m���С�f��Ů��n��δ֪  */
	private String rpsGender = "";
	/** ��˿��|������ */
	private Integer rpsFollowersCount = 0;
	/** ��ע��|���������� */
	private Integer rpsFriendsCount = 0;
	/** �û��Ļ�����|���������� */
	private Integer rpsBiFollowersCount = 0;
	/** ΢���� */
	private Integer rpsStatusesCount = 0;
	/** �ղ��� */
	private Integer rpsFavouritesCount = 0;
	/** ����ʱ�� */
	private Integer rpsCreatedAt = 0;
	/** �Ƿ����������˸��ҷ�˽�� */
	private Integer rpsAllowAllActMsg = 0;
	/** �Ƿ�������е�����Ϣ */
	private Integer rpsGeoEnabled = 0;
	/** �Ƿ���΢����֤�û�������V�û� */
	private Integer rpsVerified = 0;
	/** ��֤����-0 ��V,  1-7 ��V,10  ΢��Ů��,  200 220 ΢������ | 30 ��ҵʵ����֤ 31 ��ҵδʵ����֤ 32 ����ʵ����֤ 33 ����δʵ����֤ */
	private Integer rpsVerifiedType = 0;
	/**'��֤�����־-1��ͨ�û� 2 ������֤ 3 ��ҵ��֤'*/
	private Integer rpsVerifiedTypeFlag = 0;
	/** �Ƿ����������˶��ҵ�΢���������� */
	private Integer rpsAllowAllComment = 0;
	/** �û���ͷ���ַ */
	private String rpsAvatarLarge = "";
	/** ��֤ԭ��|��֤��Ϣ */
	private String rpsVerifiedReason = "";
	/** ������ */
	private Integer rpsCommentNum = 0;
	/** @���� */
	private Integer rpsAtmeNum = 0;
	/** ���Ƕ� */
	private Integer rpsCoverageNum = 0;
	/** ��ҵID */
	private String industryCode = "";
	/** ���һ��΢��id */
	private String lastweiboid = "";
	/** ����΢���ķ���ʱ�� */
	private String lastweibotime = "";
	/** �û���ǩ */
	private String tags = "";
	/** �û�����:3��������2��ͨ��1��������0δ��� */
	private Integer fansQuality = 0;
	/** ΢���ȼ� */
	private Integer weiboLevel = 0;
	/** ͬ��ʱ�� */
	private Integer sytime = 0;
	/** ΢������ʱ�� */
	private Integer weiboCreateTime = 0;
	/** ΢��ͬ��ʱ�� */
	private Integer weiboLastSytime;
	/** ����ͬ��΢��mid */
	private Long weiboLastMid;
	/** ΢������ͬ��ʱ���*/
	private Integer weiboPageTime;
	/** ����ID */
	private Long institutionId;
	/** ��ע��ϵ */
	private Integer flagRelation;
	/** ������ƽ̨ΨһID */
	private String appid;
	/** �˺� */
	private Long accountId;
	/** �˺����� */
	private int accountType;
	/** ���·�˿�б�������ʶλ*/
	private Long lastUserId;

	private List tagList;
	private Set tagSet;
	//�û����صĽ�����Ϣ
	private List eduList;
	//�û����صĹ�����Ϣ
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
