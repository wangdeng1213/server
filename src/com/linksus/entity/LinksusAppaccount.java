package com.linksus.entity;

import java.io.Serializable;

public class LinksusAppaccount extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 主键ID */
	private Long id;
	/** 机构ID */
	private Long institutionId;
	/** 第三方平台唯一ID 新浪的rps_id|腾讯的open_id|微信的app_id */
	private String appid;
	/** 获取授权的token */
	private String token;
	/** 帐号类型
	        1新浪
	        2腾讯
	        3微信
	         */
	private Integer type;
	/** 合同开始日期 */
	private Integer appStime;
	/** 合同结束日期 */
	private Integer appEtime;
	/** 授权结束日期 */
	private Integer tokenEtime;
	/** 未开通   通过status判断  为0
	        未授权    通过判断token是否存在
	        授权过期 通过token_etime判断
	        使用中      通过status  为1， 
	        待续费     通过app_etime判断
	         */
	private Integer status;
	/** 创建时间 */
	private Integer createdTime;
	/** 最后更新时间 */
	private Integer lastUpdTime;
	/** 微信授权不同于微博授权此字段针对于微信 微信的appid */
	private String appKey;
	/** 微信授权不同于微博授权此字段针对于微信 微信的appsecrect */
	private String appSecrect;
	/** 微信授权不同于微博授权此字段针对于微信  平台生成的验证token */
	private String validToken;
	/** 微信服务类型  0服务号  1订阅号 */
	private Integer wxType;
	/** 新浪用户名|腾讯用户名|微信号 */
	private String accountName;
	/** 微信昵称 */
	private String nickName;
	/** 用户头像 */
	private String userProfile;

	/** 初始化关注数 */
	private Integer initial_follow_num;
	/** 上次更新后关注数 */
	private Integer last_follow_num;
	/**上次更新关注事件 */
	private Integer updt_follow_time;

	/** 用于全量粉丝中记录最后一位粉丝id*/
	private Long lastUid;

	/** 新浪私信最大msgId */
	private Long msgId;
	/** 互动类型:5 腾讯互动 6 腾讯私信 */
	private Integer interactType;
	/** 腾讯互动的lastid */
	private Long maxId;
	/** 腾讯互动本页起始时间 */
	private Long pagetime;
	/**账户userId*/
	private Long userId;
	/** 微信原始id */
	private String wxSourceId;
	/** 判断是否为政务版, 1为政务版,0为企业版.默认是企业版 */
	private Integer isGov;

	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

	public Long getInstitutionId(){
		return institutionId;
	}

	public void setInstitutionId(Long institutionId){
		this.institutionId = institutionId;
	}

	public String getAppid(){
		return appid;
	}

	public void setAppid(String appid){
		this.appid = appid;
	}

	public String getToken(){
		return token;
	}

	public void setToken(String token){
		this.token = token;
	}

	public Integer getType(){
		return type;
	}

	public void setType(Integer type){
		this.type = type;
	}

	public Integer getAppStime(){
		return appStime;
	}

	public void setAppStime(Integer appStime){
		this.appStime = appStime;
	}

	public Integer getAppEtime(){
		return appEtime;
	}

	public void setAppEtime(Integer appEtime){
		this.appEtime = appEtime;
	}

	public Integer getTokenEtime(){
		return tokenEtime;
	}

	public void setTokenEtime(Integer tokenEtime){
		this.tokenEtime = tokenEtime;
	}

	public Integer getStatus(){
		return status;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getCreatedTime(){
		return createdTime;
	}

	public void setCreatedTime(Integer createdTime){
		this.createdTime = createdTime;
	}

	public Integer getLastUpdTime(){
		return lastUpdTime;
	}

	public void setLastUpdTime(Integer lastUpdTime){
		this.lastUpdTime = lastUpdTime;
	}

	public String getAppKey(){
		return appKey;
	}

	public void setAppKey(String appKey){
		this.appKey = appKey;
	}

	public String getAppSecrect(){
		return appSecrect;
	}

	public void setAppSecrect(String appSecrect){
		this.appSecrect = appSecrect;
	}

	public String getValidToken(){
		return validToken;
	}

	public void setValidToken(String validToken){
		this.validToken = validToken;
	}

	public Integer getWxType(){
		return wxType;
	}

	public void setWxType(Integer wxType){
		this.wxType = wxType;
	}

	public String getAccountName(){
		return accountName;
	}

	public void setAccountName(String accountName){
		this.accountName = accountName;
	}

	public String getNickName(){
		return nickName;
	}

	public void setNickName(String nickName){
		this.nickName = nickName;
	}

	public String getUserProfile(){
		return userProfile;
	}

	public void setUserProfile(String userProfile){
		this.userProfile = userProfile;
	}

	public Integer getInitial_follow_num(){
		return initial_follow_num;
	}

	public void setInitial_follow_num(Integer initial_follow_num){
		this.initial_follow_num = initial_follow_num;
	}

	public Integer getLast_follow_num(){
		return last_follow_num;
	}

	public void setLast_follow_num(Integer last_follow_num){
		this.last_follow_num = last_follow_num;
	}

	public Integer getUpdt_follow_time(){
		return updt_follow_time;
	}

	public void setUpdt_follow_time(Integer updt_follow_time){
		this.updt_follow_time = updt_follow_time;
	}

	public Long getLastUid(){
		return lastUid;
	}

	public void setLastUid(Long lastUid){
		this.lastUid = lastUid;
	}

	public Long getMsgId(){
		return msgId;
	}

	public void setMsgId(Long msgId){
		this.msgId = msgId;
	}

	public Long getMaxId(){
		return maxId;
	}

	public void setMaxId(Long maxId){
		this.maxId = maxId;
	}

	public Long getPagetime(){
		return pagetime;
	}

	public void setPagetime(Long pagetime){
		this.pagetime = pagetime;
	}

	public Integer getInteractType(){
		return interactType;
	}

	public void setInteractType(Integer interactType){
		this.interactType = interactType;
	}

	public Long getUserId(){
		return userId;
	}

	public void setUserId(Long userId){
		this.userId = userId;
	}

	public String getWxSourceId(){
		return wxSourceId;
	}

	public void setWxSourceId(String wxSourceId){
		this.wxSourceId = wxSourceId;
	}

	public Integer getIsGov(){
		return isGov;
	}

	public void setIsGov(Integer isGov){
		this.isGov = isGov;
	}

}
