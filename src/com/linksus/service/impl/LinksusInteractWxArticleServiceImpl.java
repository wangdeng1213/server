package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusInteractWxArticleMapper;
import com.linksus.entity.LinksusInteractWxArticle;
import com.linksus.service.LinksusInteractWxArticleService;

@Service("linksusInteractWxArticleService")
public class LinksusInteractWxArticleServiceImpl implements LinksusInteractWxArticleService{

	@Autowired
	private LinksusInteractWxArticleMapper linksusInteractWxArticleMapper;

	/** ��ѯ�б� */
	public List<LinksusInteractWxArticle> getLinksusInteractWxArticleList(){
		return linksusInteractWxArticleMapper.getLinksusInteractWxArticleList();
	}

	/** ������ѯ */
	public LinksusInteractWxArticle getLinksusInteractWxArticleById(Long pid){
		return linksusInteractWxArticleMapper.getLinksusInteractWxArticleById(pid);
	}

	/** ���� */
	public Integer insertLinksusInteractWxArticle(LinksusInteractWxArticle entity){
		return linksusInteractWxArticleMapper.insertLinksusInteractWxArticle(entity);
	}

	/** ���� */
	public Integer updateLinksusInteractWxArticle(LinksusInteractWxArticle entity){
		return linksusInteractWxArticleMapper.updateLinksusInteractWxArticle(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusInteractWxArticleById(Long pid){
		return linksusInteractWxArticleMapper.deleteLinksusInteractWxArticleById(pid);
	}

	/** ͨ��material_id��ȡ΢��ͼ�ı�����*/
	public List<LinksusInteractWxArticle> getLinksusInteractWxArticleByMaterialId(Long materialId){
		return linksusInteractWxArticleMapper.getLinksusInteractWxArticleByMaterialId(materialId);
	}
}