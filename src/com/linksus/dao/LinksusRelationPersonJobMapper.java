package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusRelationPersonJob;

public interface LinksusRelationPersonJobMapper{

	/** 列表查询 */
	public List<LinksusRelationPersonJob> getLinksusRelationPersonJobList();

	/** 主键查询 */
	public LinksusRelationPersonJob getLinksusRelationPersonJobById(Long pid);

	/** 新增 */
	public Integer insertLinksusRelationPersonJob(LinksusRelationPersonJob entity);

	/** 更新 */
	public Integer updateLinksusRelationPersonJob(LinksusRelationPersonJob entity);

	/** 主键删除 */
	public Integer deleteLinksusRelationPersonJobById(Long pid);

	/** 删除personId的数据 */
	public Integer deleteByPersonId(Long personId);
}
