package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusRelationPersonEdu;

public interface LinksusRelationPersonEduMapper{

	/** 列表查询 */
	public List<LinksusRelationPersonEdu> getLinksusRelationPersonEduList();

	/** 主键查询 */
	public LinksusRelationPersonEdu getLinksusRelationPersonEduById(Long pid);

	/** 新增 */
	public Integer insertLinksusRelationPersonEdu(LinksusRelationPersonEdu entity);

	/** 更新 */
	public Integer updateLinksusRelationPersonEdu(LinksusRelationPersonEdu entity);

	/** 主键删除 */
	public Integer deleteLinksusRelationPersonEduById(Long pid);

	/** 删除personId的数据 */
	public Integer deleteByPersonId(Long personId);
}
