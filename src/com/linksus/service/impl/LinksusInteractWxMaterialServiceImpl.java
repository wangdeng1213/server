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

	/** ��ѯ�б� */
	public List<LinksusInteractWxMaterial> getLinksusInteractWxMaterialList(){
		return linksusInteractWxMaterialMapper.getLinksusInteractWxMaterialList();
	}

	/** ������ѯ */
	public LinksusInteractWxMaterial getLinksusInteractWxMaterialById(Long pid){
		return linksusInteractWxMaterialMapper.getLinksusInteractWxMaterialById(pid);
	}

	/** ���� */
	public Integer insertLinksusInteractWxMaterial(LinksusInteractWxMaterial entity){
		return linksusInteractWxMaterialMapper.insertLinksusInteractWxMaterial(entity);
	}

	/** ���� */
	public Integer updateLinksusInteractWxMaterial(LinksusInteractWxMaterial entity){
		return linksusInteractWxMaterialMapper.updateLinksusInteractWxMaterial(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusInteractWxMaterialById(Long pid){
		return linksusInteractWxMaterialMapper.deleteLinksusInteractWxMaterialById(pid);
	}
}