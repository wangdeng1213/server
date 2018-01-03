package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusRelationOperate;

public interface LinksusRelationOperateMapper{

	/** 列表查询 */
	public List<LinksusRelationOperate> getLinksusRelationOperateList();

	/** 主键查询 */
	public LinksusRelationOperate getLinksusRelationOperateById(Long pid);

	/** 新增 */
	public Integer insertLinksusRelationOperate(LinksusRelationOperate entity);

	/** 更新 */
	public Integer updateLinksusRelationOperate(LinksusRelationOperate entity);

	/** 主键删除 */
	public Integer deleteLinksusRelationOperateById(Long pid);

	//主键更新操作表失败次数
	public Integer updateLinksusRelationOperFailNum(Long pid);

	//主键更新操作成功次数
	public Integer updateLinksusRelationOperSuccessNum(Long pid);

	public LinksusRelationOperate checkIsExsitRelationOperate(String paraStr);

}
