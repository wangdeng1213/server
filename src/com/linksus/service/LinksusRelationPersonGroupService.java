package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusRelationPersonGroup;

public interface LinksusRelationPersonGroupService{

	/** 查询列表 */
	public List<LinksusRelationPersonGroup> getLinksusRelationPersonGroupList();

	/** 主键查询 */
	public LinksusRelationPersonGroup getLinksusRelationPersonGroupById(Map paramMap);

	/** 新增 */
	public Integer insertLinksusRelationPersonGroup(LinksusRelationPersonGroup entity);

	/** 更新 */
	public Integer updateLinksusRelationPersonGroup(LinksusRelationPersonGroup entity);

	/** 主键删除 */
	public Integer deleteLinksusRelationPersonGroupById(Long pid);

	/** 删除personId的数据 */
	public Integer deleteByPersonId(Long personId);

	/**删除person_group 根据personId 和institutionId*/
	public Integer deleteByHashMap(Map map);

	/**Map查询 */
	public List<LinksusRelationPersonGroup> getLinksusRelationPersonGroupByMap(Map map);

	public LinksusRelationPersonGroup searchRelationPersonGroupByMap(Map map);

	/** 组合主键删除 */
	public Integer deleteLinksusRelationPersonGroupByMap(Map map);
}
