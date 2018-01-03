package com.linksus.entity;

import java.io.Serializable;

public class LinksusInstitution extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ����ID */
	private Long id;
	/** �������� */
	private String name;
	/** ������� */
	private String shortName;
	/** ������ҵ */
	private Integer industry;
	/** ������ַ */
	private String address;
	/** ������������ */
	private String code;
	/** �����绰 */
	private String phone;
	/** �������� */
	private String fax;
	/** �Ƿ�����
	        0Ϊ��
	        1Ϊ��
	         */
	private Integer trival;
	/** ����״̬
	        0δ����
	        1��ͨ��
	        2δͨ��
	         */
	private Integer trivalStatus;
	/** ���ÿ�ʼ���� */
	private Integer trivalStime;
	/** ���ý������� */
	private Integer trivalEtime;
	/** ����ʱ�� */
	private Integer createdTime;
	/** ������ʱ�� */
	private Integer lastUpdTime;
	/** ���˿��������� */
	private Integer sinaBusinessNumber;
	/** ��Ѷ���������� */
	private Integer qqBusinessNumber;
	/** �˻����ſ����� */
	private Integer smsNumber;
	/** �˻�����1��ͻ�2��ͨ�ͻ� */
	private Integer type;
	/** Ĭ����Ѷ��ԴĬ��Ϊ2 */
	private Long qqSourceId;
	/** Ĭ��������ԴĬ��Ϊ1 */
	private Long sinaSourceId;

	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getShortName(){
		return shortName;
	}

	public void setShortName(String shortName){
		this.shortName = shortName;
	}

	public Integer getIndustry(){
		return industry;
	}

	public void setIndustry(Integer industry){
		this.industry = industry;
	}

	public String getAddress(){
		return address;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public String getCode(){
		return code;
	}

	public void setCode(String code){
		this.code = code;
	}

	public String getPhone(){
		return phone;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getFax(){
		return fax;
	}

	public void setFax(String fax){
		this.fax = fax;
	}

	public Integer getTrival(){
		return trival;
	}

	public void setTrival(Integer trival){
		this.trival = trival;
	}

	public Integer getTrivalStatus(){
		return trivalStatus;
	}

	public void setTrivalStatus(Integer trivalStatus){
		this.trivalStatus = trivalStatus;
	}

	public Integer getTrivalStime(){
		return trivalStime;
	}

	public void setTrivalStime(Integer trivalStime){
		this.trivalStime = trivalStime;
	}

	public Integer getTrivalEtime(){
		return trivalEtime;
	}

	public void setTrivalEtime(Integer trivalEtime){
		this.trivalEtime = trivalEtime;
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

	public Integer getSinaBusinessNumber(){
		return sinaBusinessNumber;
	}

	public void setSinaBusinessNumber(Integer sinaBusinessNumber){
		this.sinaBusinessNumber = sinaBusinessNumber;
	}

	public Integer getQqBusinessNumber(){
		return qqBusinessNumber;
	}

	public void setQqBusinessNumber(Integer qqBusinessNumber){
		this.qqBusinessNumber = qqBusinessNumber;
	}

	public Integer getSmsNumber(){
		return smsNumber;
	}

	public void setSmsNumber(Integer smsNumber){
		this.smsNumber = smsNumber;
	}

	public Integer getType(){
		return type;
	}

	public void setType(Integer type){
		this.type = type;
	}

	public Long getQqSourceId(){
		return qqSourceId;
	}

	public void setQqSourceId(Long qqSourceId){
		this.qqSourceId = qqSourceId;
	}

	public Long getSinaSourceId(){
		return sinaSourceId;
	}

	public void setSinaSourceId(Long sinaSourceId){
		this.sinaSourceId = sinaSourceId;
	}
}
