package com.linksus.entity;

public class LinksusWeiXinReturnBean{

	//开发者微信号
	private String toUserName;

	//发送方帐号（一个OpenID）
	private String fromUserName;

	//消息创建时间 
	private String createTime;

	//消息类型
	private String msgType;

	//文本内容
	private String content;

	private String msgId;

	//媒体id
	private String mediaId;

	//事件类型，subscribe(订阅)、unsubscribe(取消订阅) 
	private String eventType;

	//语音格式
	private String format;

	//视频消息缩略图的媒体id
	private String thumbMediaId;

	//事件KEY值，与自定义菜单接口中KEY值对应 
	private String eventKey;

	private String status;

	private String totalCount;

	private String sentCount;

	private String filterCount;

	private String errorCount;

	public String getToUserName(){
		return toUserName;
	}

	public void setToUserName(String toUserName){
		this.toUserName = toUserName;
	}

	public String getFromUserName(){
		return fromUserName;
	}

	public void setFromUserName(String fromUserName){
		this.fromUserName = fromUserName;
	}

	public String getCreateTime(){
		return createTime;
	}

	public void setCreateTime(String createTime){
		this.createTime = createTime;
	}

	public String getMsgType(){
		return msgType;
	}

	public void setMsgType(String msgType){
		this.msgType = msgType;
	}

	public String getContent(){
		return content;
	}

	public void setContent(String content){
		this.content = content;
	}

	public String getMsgId(){
		return msgId;
	}

	public void setMsgId(String msgId){
		this.msgId = msgId;
	}

	public String getEventType(){
		return eventType;
	}

	public void setEventType(String eventType){
		this.eventType = eventType;
	}

	public String getMediaId(){
		return mediaId;
	}

	public void setMediaId(String mediaId){
		this.mediaId = mediaId;
	}

	public String getFormat(){
		return format;
	}

	public void setFormat(String format){
		this.format = format;
	}

	public String getThumbMediaId(){
		return thumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId){
		this.thumbMediaId = thumbMediaId;
	}

	public String getEventKey(){
		return eventKey;
	}

	public void setEventKey(String eventKey){
		this.eventKey = eventKey;
	}

	public String getStatus(){
		return status;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getTotalCount(){
		return totalCount;
	}

	public void setTotalCount(String totalCount){
		this.totalCount = totalCount;
	}

	public String getSentCount(){
		return sentCount;
	}

	public void setSentCount(String sentCount){
		this.sentCount = sentCount;
	}

	public String getFilterCount(){
		return filterCount;
	}

	public void setFilterCount(String filterCount){
		this.filterCount = filterCount;
	}

	public String getErrorCount(){
		return errorCount;
	}

	public void setErrorCount(String errorCount){
		this.errorCount = errorCount;
	}

}
