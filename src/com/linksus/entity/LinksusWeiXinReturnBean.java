package com.linksus.entity;

public class LinksusWeiXinReturnBean{

	//������΢�ź�
	private String toUserName;

	//���ͷ��ʺţ�һ��OpenID��
	private String fromUserName;

	//��Ϣ����ʱ�� 
	private String createTime;

	//��Ϣ����
	private String msgType;

	//�ı�����
	private String content;

	private String msgId;

	//ý��id
	private String mediaId;

	//�¼����ͣ�subscribe(����)��unsubscribe(ȡ������) 
	private String eventType;

	//������ʽ
	private String format;

	//��Ƶ��Ϣ����ͼ��ý��id
	private String thumbMediaId;

	//�¼�KEYֵ�����Զ���˵��ӿ���KEYֵ��Ӧ 
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
