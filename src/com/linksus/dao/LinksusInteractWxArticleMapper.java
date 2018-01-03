package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusInteractWxArticle;

public interface LinksusInteractWxArticleMapper{

	/** 列表查询 */
	public List<LinksusInteractWxArticle> getLinksusInteractWxArticleList();

	/** 主键查询 */
	public LinksusInteractWxArticle getLinksusInteractWxArticleById(Long pid);

	/** 新增 */
	public Integer insertLinksusInteractWxArticle(LinksusInteractWxArticle entity);

	/** 更新 */
	public Integer updateLinksusInteractWxArticle(LinksusInteractWxArticle entity);

	/** 主键删除 */
	public Integer deleteLinksusInteractWxArticleById(Long pid);

	/** 通过material_id获取微信图文表内容*/
	public List<LinksusInteractWxArticle> getLinksusInteractWxArticleByMaterialId(Long materialId);

}
