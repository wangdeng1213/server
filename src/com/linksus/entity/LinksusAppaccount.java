package com.linksus.entity;

import java.io.Serializable;

public class LinksusAppaccount extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ����ID */
	private Long id;
	/** ����ID */
	private Long institutionId;
	/** ������ƽ̨ΨһID ���˵�rps_id|��Ѷ��open_id|΢�ŵ�app_id */
	private String appid;
	/** ��ȡ��Ȩ��token */
	private String token;
	/** �ʺ�����
	        1����
	        2��Ѷ
	        3΢��
	         */
	private Integer type;
	/** ��ͬ��ʼ���� */
	private Integer appStime;
	/** ��ͬ�������� */
	private Integer appEtime;
	/** ��Ȩ�������� */
	private Integer tokenEtime;
	/** δ��ͨ   ͨ��status�ж�  Ϊ0
	        δ��Ȩ    ͨ���ж�token�Ƿ����
	        ��Ȩ���� ͨ��token_etime�ж�
	        ʹ����      ͨ��status  Ϊ1�� 
	        ������     ͨ��app_etime�ж�
	         */
	private Integer status;
	/** ����ʱ�� */
	private Integer createdTime;
	/** ������ʱ�� */
	private Integer lastUpdTime;
	/** ΢����Ȩ��ͬ��΢����Ȩ���ֶ������΢�� ΢�ŵ�appid */
	private String appKey;
	/** ΢����Ȩ��ͬ��΢����Ȩ���ֶ������΢�� ΢�ŵ�appsecrect */
	private String appSecrect;
	/** ΢����Ȩ��ͬ��΢����Ȩ���ֶ������΢��  ƽ̨���ɵ���֤token */
	private String validToken;
	/** ΢�ŷ�������  0�����  1���ĺ� */
	private Integer wxType;
	/** �����û���|��Ѷ�û���|΢�ź� */
	private String accountName;
	/** ΢���ǳ� */
	private String nickName;
	/** �û�ͷ�� */
	private String userProfile;

	/** ��ʼ����ע�� */
	private Integer initial_follow_num;
	/** �ϴθ��º��ע�� */
	private Integer last_follow_num;
	/**�ϴθ��¹�ע�¼� */
	private Integer updt_follow_time;

	/** ����ȫ����˿�м�¼���һλ��˿id*/
	private Long lastUid;

	/** ����˽�����msgId */
	private Long msgId;
	/** ��������:5 ��Ѷ���� 6 ��Ѷ˽�� */
	private Integer interactType;
	/** ��Ѷ������lastid */
	private Long maxId;
	/** ��Ѷ������ҳ��ʼʱ�� */
	private Long pagetime;
	/**�˻�userId*/
	private Long userId;
	/** ΢��ԭʼid */
	private String wxSourceId;
	/** �ж��Ƿ�Ϊ�����, 1Ϊ�����,0Ϊ��ҵ��.Ĭ������ҵ�� */
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
