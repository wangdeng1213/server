package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusInteractWxMaterial;

public interface LinksusInteractWxMaterialMapper{

	/** �б��ѯ */
	public List<LinksusInteractWxMaterial> getLinksusInteractWxMaterialList();

	/** ������ѯ */
	public LinksusInteractWxMaterial getLinksusInteractWxMaterialById(Long pid);

	/** ���� */
	public Integer insertLinksusInteractWxMaterial(LinksusInteractWxMaterial entity);

	/** ���� */
	public Integer updateLinksusInteractWxMaterial(LinksusInteractWxMaterial entity);

	/** ����ɾ�� */
	public Integer deleteLinksusInteractWxMaterialById(Long pid);

}
