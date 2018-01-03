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

	/** 查询列表 */
	public List<LinksusInteractWxArticle> getLinksusInteractWxArticleList(){
		return linksusInteractWxArticleMapper.getLinksusInteractWxArticleList();
	}

	/** 主键查询 */
	public LinksusInteractWxArticle getLinksusInteractWxArticleById(Long pid){
		return linksusInteractWxArticleMapper.getLinksusInteractWxArticleById(pid);
	}

	/** 新增 */
	public Integer insertLinksusInteractWxArticle(LinksusInteractWxArticle entity){
		return linksusInteractWxArticleMapper.insertLinksusInteractWxArticle(entity);
	}

	/** 更新 */
	public Integer updateLinksusInteractWxArticle(LinksusInteractWxArticle entity){
		return linksusInteractWxArticleMapper.updateLinksusInteractWxArticle(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusInteractWxArticleById(Long pid){
		return linksusInteractWxArticleMapper.deleteLinksusInteractWxArticleById(pid);
	}

	/** 通过material_id获取微信图文表内容*/
	public List<LinksusInteractWxArticle> getLinksusInteractWxArticleByMaterialId(Long materialId){
		return linksusInteractWxArticleMapper.getLinksusInteractWxArticleByMaterialId(materialId);
	}
}