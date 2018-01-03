package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusRelationUserTagdef;

public interface LinksusRelationUserTagdefService{

	/** 查询列表 */
	public List<LinksusRelationUserTagdef> getLinksusRelationUserTagdefList();

	/** 主键查询 */
	public LinksusRelationUserTagdef getLinksusRelationUserTagdefById(Long pid);

	/** 新增 */
	public Integer insertLinksusRelationUserTagdef(LinksusRelationUserTagdef entity);

	/** 更新 */
	public Integer updateLinksusRelationUserTagdef(LinksusRelationUserTagdef entity);

	/** 主键删除 */
	public Integer deleteLinksusRelationUserTagdefById(Long pid);

	/** 查询存在list中的标签对象 */
	public List<LinksusRelationUserTagdef> getUserTagdefBySet(Map tagParaMap);

	/**通过标签 和 用户类型判断标签主表中是否存在*/

	public LinksusRelationUserTagdef checkIsExsitByTagAndaccoutType(Map paraMap);

	/** 标签名称查询 */
	public LinksusRelationUserTagdef getLinksusRelationUserTagdefByTagName(String tagname);

	/** 标签使用次数统计更新 */
	public Integer updateLinksusRelationUserTagdefUseCount();

	/** 清空用户标签统计使用的中间表 */
	public Integer clearUserTagMiddleTable();
}
