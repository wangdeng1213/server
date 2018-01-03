package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusRelationGroup;

public interface LinksusRelationGroupMapper{

	/** 列表查询 */
	public List<LinksusRelationGroup> getLinksusRelationGroupList();

	/** 主键查询 */
	public LinksusRelationGroup getLinksusRelationGroupById(Long pid);

	/** 新增 */
	public Integer insertLinksusRelationGroup(LinksusRelationGroup entity);

	/** 更新 */
	public Integer updateLinksusRelationGroup(LinksusRelationGroup entity);

	/** 主键删除 */
	public Integer deleteLinksusRelationGroupById(Long pid);

	/** 单独查询列 */
	public LinksusRelationGroup getLinksusRelationGroup(LinksusRelationGroup dto);

	/** 查询该机构是否存在"未分组"组 */
	public LinksusRelationGroup getPersonGroupInfoByInstIdAndGroupType(Map paramMap);

	/** 查询该机构是否存在"***"组 */
	public LinksusRelationGroup getPersonGroupInfoType(Map map);

	/** 插入未分组信息 */
	public Integer insertNoGroupInfo(LinksusRelationGroup entity);
}
