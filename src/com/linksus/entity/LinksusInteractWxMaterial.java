package com.linksus.entity;

import java.io.Serializable;

public class LinksusInteractWxMaterial extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 素材ID */
	private Long materialId;
	/** 类型:1 单图文 2 多图文 3 语音 4 视频 */
	private Integer materialType;
	/** 机构ID:创建机构 */
	private Long institutionId;
	/** 状态:1 正常 2 删除(使用次数大于0时逻辑删除) */
	private Integer status;
	/** 使用次数 */
	private Integer useCount;
	/** 最近使用时间 */
	private Integer lastUseTime;
	/** 更新时间 */
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
