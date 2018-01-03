package com.linksus.entity;

import java.util.List;

public class InteractListResultObj{

	private static final long serialVersionUID = 1L;

	/** 互动ID 逗号分隔 */
	private String interactIds;
	/** 人员ID */
	private Long personId;
	/** 平台账号ID */
	private Long accountId;
	/** 最近互动平台类型:1 新浪 2 腾讯 3 微信 */
	private Integer accountType;
	/** 分配状态:0 未分配 1:已分配 2:忽略 */
	private Integer status;
	/** 未处理互动数 */
	private Integer count;
	/** 运维人员ID:已分配时有值 */
	private Long instPersonId;
	/** 置顶时间:置顶时有值 */
	private Integer topTime;
	/** 最近互动时间 */
	private Integer lastInteractTime;
	/** 最近互动ID */
	private Long lastRecordId;
	/** 最近互动类型:1 评论 2 转发 3 @ 4 评论并@ 5 私信 6 微信 */
	private Integer interactType;
	/** 微博ID:互动类型为1-4的有值 */
	private Long weiboId;
	/** 微博ID:互动类型为1-4的有值 ——字符串格式*/
	private String weiboMid;
	/** 互动信息ID:用户最新互动消息ID */
	private Long messageId;
	/** 内容:文本内容 */
	private String content;
	/** 用户头像 */
	private List headImages;
	/** 人员名称*/
	private String personName;
	/** 运维人员ID*/
	private String instPersonIds;
	/** 运维人员名称 */
	private String instPersonNames;
	/** 平台账号名称 */
	private String accountName;
	/** 最新互动人员名称  */
	private String interactUserName;
	/** 最新互动人员头像  */
	private String interactUserHeadImage;
	/** 事件类型  */
	private String eventType;
	/** 私信图片路径  */
	private String messageImage;

	public List getHeadImages(){
		return headImages;
	}

	public String getContent(){
		return content;
	}

	public String getInstPersonIds(){
		return instPersonIds;
	}

	public void setInstPersonIds(String instPersonIds){
		this.instPersonIds = instPersonIds;
	}

	public String getInstPersonNames(){
		return instPersonNames;
	}

	public void setInstPersonNames(String instPersonNames){
		this.instPersonNames = instPersonNames;
	}

	public String getPersonName(){
		return personName;
	}

	public void setPersonName(String personName){
		this.personName = personName;
	}

	public void setContent(String content){
		this.content = content;
	}

	public void setHeadImages(List headImages){
		this.headImages = headImages;
	}

	public Long getPersonId(){
		return personId;
	}

	public void setPersonId(Long personId){
		this.personId = personId;
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

	public Integer getStatus(){
		return status;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getCount(){
		return count;
	}

	public void setCount(Integer count){
		this.count = count;
	}

	public Long getInstPersonId(){
		return instPersonId;
	}

	public void setInstPersonId(Long instPersonId){
		this.instPersonId = instPersonId;
	}

	public Integer getTopTime(){
		return topTime;
	}

	public void setTopTime(Integer topTime){
		this.topTime = topTime;
	}

	public Integer getLastInteractTime(){
		return lastInteractTime;
	}

	public void setLastInteractTime(Integer lastInteractTime){
		this.lastInteractTime = lastInteractTime;
	}

	public Long getLastRecordId(){
		return lastRecordId;
	}

	public void setLastRecordId(Long lastRecordId){
		this.lastRecordId = lastRecordId;
	}

	public Integer getInteractType(){
		return interactType;
	}

	public void setInteractType(Integer interactType){
		this.interactType = interactType;
	}

	public Long getWeiboId(){
		return weiboId;
	}

	public void setWeiboId(Long weiboId){
		this.weiboId = weiboId;
	}

	public Long getMessageId(){
		return messageId;
	}

	public void setMessageId(Long messageId){
		this.messageId = messageId;
	}

	public String getInteractIds(){
		return interactIds;
	}

	public void setInteractIds(String interactIds){
		this.interactIds = interactIds;
	}

	public String getAccountName(){
		return accountName;
	}

	public void setAccountName(String accountName){
		this.accountName = accountName;
	}

	public String getInteractUserName(){
		return interactUserName;
	}

	public void setInteractUserName(String interactUserName){
		this.interactUserName = interactUserName;
	}

	public String getInteractUserHeadImage(){
		return interactUserHeadImage;
	}

	public void setInteractUserHeadImage(String interactUserHeadImage){
		this.interactUserHeadImage = interactUserHeadImage;
	}

	public String getEventType(){
		return eventType;
	}

	public void setEventType(String eventType){
		this.eventType = eventType;
	}

	public String getMessageImage(){
		return messageImage;
	}

	public void setMessageImage(String messageImage){
		this.messageImage = messageImage;
	}

	public String getWeiboMid(){
		return weiboMid;
	}

	public void setWeiboMid(String weiboMid){
		this.weiboMid = weiboMid;
	}

}
