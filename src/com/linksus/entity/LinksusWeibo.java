package com.linksus.entity;

import java.io.Serializable;

public class LinksusWeibo extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/**  */
	private Long id;
	/** �˻�id */
	private Long institutionId;
	/** �ʺ�id */
	private Long accountId;
	/** ����ʱ�� */
	private Integer createdTime;
	/** ����ʱ�� */
	private Integer sendTime;
	/** �ʺ����� */
	private String accountName;
	/** �ʺ�����
	        1����
	        2��Ѷ */
	private Integer accountType;
	/** ΢������ */
	private String content;
	/** ԭͼ��url */
	private String picOriginalUrl;
	/** ��ͼ��url */
	private String picMiddleUrl;
	/** ����ͼ��url */
	private String picThumbUrl;
	/** ��Ƶ��ַ */
	private String musicUrl;
	/** ��Ƶ��ַ */
	private String videoUrl;
	/** ������ ���� */
	private Long authorId;
	/** ������ �ַ��� */
	private String authorName;
	/** ��ǰ��������΢����״̬
	        0              �ݸ�
	        1	Ԥ����
	        2	����ʧ��
	        3	�ѷ���
	        4	����δͨ��
	        5	������
	        98	��ɾ��
	        99	��ͣ�� */
	private Integer status;
	/** ��ǰ��˵�״̬
	        00 δ����
	        10 һ���������
	        11 һ�� ������������� */
	private String applyStatus;
	/** ��ǰ������״̬
	        0Ϊ ��ʱ����
	        1Ϊ��ʱ���� */
	private Integer publishStauts;
	/** �������� 
	        0Ϊֱ��
	        1Ϊת�� */
	private Integer publishType;
	/** ��������
	        00000
	        ��һλ ͼƬ
	        �ڶ�λ ��Ƶ
	        ����λ ����
	        ����λ ����
	        ����λ ���� */
	private String contentType;
	/** ��ǰ����Ϣ�Ƿ��Ѿ��鵵
	        0Ϊ δ�鵵
	        1Ϊ �ѹ鵵 */
	private Integer toFile;
	/** ԭ΢��id */
	private Long srcid;
	/** ԭ΢����url */
	private String srcurl;
	/** ΢��id */
	private Long mid;
	/** ������  */
	private Integer repostCount;
	/** ������ */
	private Integer commentCount;
	/** ��Դid */
	private Long sourceId;
	/** ��Դ���� */
	private String sourceName;
	/** ΢����URL */
	private String currentUrl;
	/** ������Դ:0����,1�ɹ� */
	private Integer publishSource;
	/** ����Ϊ0,�ɹ�Ϊtaskid */
	private Long referId;

	/** ��ʱ�����־ */
	private boolean regularFlag = false;
	/**��ʱ�����ط����� */
	private int reSendCount;
	/** ΢��ʧ��ԭ�� */
	private String errmsg;

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

	public Long getAccountId(){
		return accountId;
	}

	public void setAccountId(Long accountId){
		this.accountId = accountId;
	}

	public Integer getCreatedTime(){
		return createdTime;
	}

	public void setCreatedTime(Integer createdTime){
		this.createdTime = createdTime;
	}

	public Integer getSendTime(){
		return sendTime;
	}

	public void setSendTime(Integer sendTime){
		this.sendTime = sendTime;
	}

	public String getAccountName(){
		return accountName;
	}

	public void setAccountName(String accountName){
		this.accountName = accountName;
	}

	public Integer getAccountType(){
		return accountType;
	}

	public void setAccountType(Integer accountType){
		this.accountType = accountType;
	}

	public String getContent(){
		return content;
	}

	public void setContent(String content){
		this.content = content;
	}

	public String getPicOriginalUrl(){
		return picOriginalUrl;
	}

	public void setPicOriginalUrl(String picOriginalUrl){
		this.picOriginalUrl = picOriginalUrl;
	}

	public String getPicMiddleUrl(){
		return picMiddleUrl;
	}

	public void setPicMiddleUrl(String picMiddleUrl){
		this.picMiddleUrl = picMiddleUrl;
	}

	public String getPicThumbUrl(){
		return picThumbUrl;
	}

	public void setPicThumbUrl(String picThumbUrl){
		this.picThumbUrl = picThumbUrl;
	}

	public String getMusicUrl(){
		return musicUrl;
	}

	public void setMusicUrl(String musicUrl){
		this.musicUrl = musicUrl;
	}

	public String getVideoUrl(){
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl){
		this.videoUrl = videoUrl;
	}

	public Long getAuthorId(){
		return authorId;
	}

	public void setAuthorId(Long authorId){
		this.authorId = authorId;
	}

	public String getAuthorName(){
		return authorName;
	}

	public void setAuthorName(String authorName){
		this.authorName = authorName;
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

	public Integer getPublishStauts(){
		return publishStauts;
	}

	public void setPublishStauts(Integer publishStauts){
		this.publishStauts = publishStauts;
	}

	public Integer getPublishType(){
		return publishType;
	}

	public void setPublishType(Integer publishType){
		this.publishType = publishType;
	}

	public String getContentType(){
		return contentType;
	}

	public void setContentType(String contentType){
		this.contentType = contentType;
	}

	public Integer getToFile(){
		return toFile;
	}

	public void setToFile(Integer toFile){
		this.toFile = toFile;
	}

	public Long getSrcid(){
		return srcid;
	}

	public void setSrcid(Long srcid){
		this.srcid = srcid;
	}

	public String getSrcurl(){
		return srcurl;
	}

	public void setSrcurl(String srcurl){
		this.srcurl = srcurl;
	}

	public Long getMid(){
		return mid;
	}

	public void setMid(Long mid){
		this.mid = mid;
	}

	public Integer getRepostCount(){
		return repostCount;
	}

	public void setRepostCount(Integer repostCount){
		this.repostCount = repostCount;
	}

	public Integer getCommentCount(){
		return commentCount;
	}

	public void setCommentCount(Integer commentCount){
		this.commentCount = commentCount;
	}

	public Long getSourceId(){
		return sourceId;
	}

	public void setSourceId(Long sourceId){
		this.sourceId = sourceId;
	}

	public String getSourceName(){
		return sourceName;
	}

	public void setSourceName(String sourceName){
		this.sourceName = sourceName;
	}

	public String getCurrentUrl(){
		return currentUrl;
	}

	public void setCurrentUrl(String currentUrl){
		this.currentUrl = currentUrl;
	}

	public Integer getPublishSource(){
		return publishSource;
	}

	public void setPublishSource(Integer publishSource){
		this.publishSource = publishSource;
	}

	public Long getReferId(){
		return referId;
	}

	public void setReferId(Long referId){
		this.referId = referId;
	}

	public int getReSendCount(){
		return reSendCount;
	}

	public void setReSendCount(int reSendCount){
		this.reSendCount = reSendCount;
	}

	public boolean isRegularFlag(){
		return regularFlag;
	}

	public void setRegularFlag(boolean regularFlag){
		this.regularFlag = regularFlag;
	}

	public String getErrmsg(){
		return errmsg;
	}

	public void setErrmsg(String errmsg){
		this.errmsg = errmsg;
	}
}
