package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusInteractWxMaterialMapper;
import com.linksus.entity.LinksusInteractWxMaterial;
import com.linksus.service.LinksusInteractWxMaterialService;

@Service("linksusInteractWxMaterialService")
public class LinksusInteractWxMaterialServiceImpl implements LinksusInteractWxMaterialService{

	@Autowired
	private LinksusInteractWxMaterialMapper linksusInteractWxMaterialMapper;

	/** 查询列表 */
	public List<LinksusInteractWxMaterial> getLinksusInteractWxMaterialList(){
		return linksusInteractWxMaterialMapper.getLinksusInteractWxMaterialList();
	}

	/** 主键查询 */
	public LinksusInteractWxMaterial getLinksusInteractWxMaterialById(Long pid){
		return linksusInteractWxMaterialMapper.getLinksusInteractWxMaterialById(pid);
	}

	/** 新增 */
	public Integer insertLinksusInteractWxMaterial(LinksusInteractWxMaterial entity){
		return linksusInteractWxMaterialMapper.insertLinksusInteractWxMaterial(entity);
	}

	/** 更新 */
	public Integer updateLinksusInteractWxMaterial(LinksusInteractWxMaterial entity){
		return linksusInteractWxMaterialMapper.updateLinksusInteractWxMaterial(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusInteractWxMaterialById(Long pid){
		return linksusInteractWxMaterialMapper.deleteLinksusInteractWxMaterialById(pid);
	}
}