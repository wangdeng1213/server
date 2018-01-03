package com.linksus.entity;

import java.io.Serializable;

public class LinksusInteractWxMaterial extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** �ز�ID */
	private Long materialId;
	/** ����:1 ��ͼ�� 2 ��ͼ�� 3 ���� 4 ��Ƶ */
	private Integer materialType;
	/** ����ID:�������� */
	private Long institutionId;
	/** ״̬:1 ���� 2 ɾ��(ʹ�ô�������0ʱ�߼�ɾ��) */
	private Integer status;
	/** ʹ�ô��� */
	private Integer useCount;
	/** ���ʹ��ʱ�� */
	private Integer lastUseTime;
	/** ����ʱ�� */
	private Integer updateTime;

	public Long getMaterialId(){
		return materialId;
	}

	public void setMaterialId(Long materialId){
		this.materialId = materialId;
	}

	public Integer getMaterialType(){
		return materialType;
	}

	public void setMaterialType(Integer materialType){
		this.materialType = materialType;
	}

	public Long getInstitutionId(){
		return institutionId;
	}

	public void setInstitutionId(Long institutionId){
		this.institutionId = institutionId;
	}

	public Integer getStatus(){
		return status;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getUseCount(){
		return useCount;
	}

	public void setUseCount(Integer useCount){
		this.useCount = useCount;
	}

	public Integer getLastUseTime(){
		return lastUseTime;
	}

	public void setLastUseTime(Integer lastUseTime){
		this.lastUseTime = lastUseTime;
	}

	public Integer getUpdateTime(){
		return updateTime;
	}

	public void setUpdateTime(Integer updateTime){
		this.updateTime = updateTime;
	}
}
