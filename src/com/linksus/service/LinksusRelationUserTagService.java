package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusRelationUserTag;

public interface LinksusRelationUserTagService{

	/** 查询列表 */
	public List<LinksusRelationUserTag> getLinksusRelationUserTagList();

	/** 主键查询 */
	public LinksusRelationUserTag getLinksusRelationUserTagById(Long pid);

	/** 新增 */
	public Integer insertLinksusRelationUserTag(LinksusRelationUserTag entity);

	/** 更新 */
	public Integer updateLinksusRelationUserTag(LinksusRelationUserTag entity);

	/** 主键删除 */
	public Integer deleteLinksusRelationUserTagById(Long pid);

	//批量删除
	public Integer deleteUserTagBySet(Map str);

	/**查询userId所有的标签数据*/
	public List<LinksusRelationUserTag> getTagsByUserId(Long userId);

	/** 主键删除 */
	public Integer deleteLinksusRelationUserTagByUserId(Long pid);

	/**判断userId 和 tagId 是否存在*/
	public Integer getCountByUserIdAndTagId(Long userId,Long tagId);

	/** 将标签使用记录数插入中间表*/
	public Integer insertTagCountIntoMiddleTable();

}
