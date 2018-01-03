package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusInteractWxMaterial;

public interface LinksusInteractWxMaterialMapper{

	/** 列表查询 */
	public List<LinksusInteractWxMaterial> getLinksusInteractWxMaterialList();

	/** 主键查询 */
	public LinksusInteractWxMaterial getLinksusInteractWxMaterialById(Long pid);

	/** 新增 */
	public Integer insertLinksusInteractWxMaterial(LinksusInteractWxMaterial entity);

	/** 更新 */
	public Integer updateLinksusInteractWxMaterial(LinksusInteractWxMaterial entity);

	/** 主键删除 */
	public Integer deleteLinksusInteractWxMaterialById(Long pid);

}
