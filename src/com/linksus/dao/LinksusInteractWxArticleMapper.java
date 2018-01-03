package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusInteractWxArticle;

public interface LinksusInteractWxArticleMapper{

	/** �б��ѯ */
	public List<LinksusInteractWxArticle> getLinksusInteractWxArticleList();

	/** ������ѯ */
	public LinksusInteractWxArticle getLinksusInteractWxArticleById(Long pid);

	/** ���� */
	public Integer insertLinksusInteractWxArticle(LinksusInteractWxArticle entity);

	/** ���� */
	public Integer updateLinksusInteractWxArticle(LinksusInteractWxArticle entity);

	/** ����ɾ�� */
	public Integer deleteLinksusInteractWxArticleById(Long pid);

	/** ͨ��material_id��ȡ΢��ͼ�ı�����*/
	public List<LinksusInteractWxArticle> getLinksusInteractWxArticleByMaterialId(Long materialId);

}
