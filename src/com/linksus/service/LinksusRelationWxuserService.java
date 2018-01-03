package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusRelationWxuser;

public interface LinksusRelationWxuserService{

	/** 查询列表 */
	public List<LinksusRelationWxuser> getLinksusRelationWxuserList();

	/** 主键查询 */
	public LinksusRelationWxuser getLinksusRelationWxuserById(Long pid);

	/** 新增 */
	public Integer insertLinksusRelationWxuser(LinksusRelationWxuser entity);

	/** 更新 */
	public Integer updateLinksusRelationWxuser(LinksusRelationWxuser entity);

	/** 主键删除 */
	public Integer deleteLinksusRelationWxuserById(Long pid);

	/**  根据主键查询微信用户信息*/
	public LinksusRelationWxuser getLinksusRelationWxuserInfo(long userId);

	/** 多条件查询单个 */
	public LinksusRelationWxuser getLinksusRelationWxuserByMap(Map map);

	/** 更新PersonId */
	public Integer updatePersonId(LinksusRelationWxuser entity);
}
