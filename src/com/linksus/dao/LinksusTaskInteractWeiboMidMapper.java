package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusTaskInteractWeiboMid;

public interface LinksusTaskInteractWeiboMidMapper{

	/** 列表查询 */
	public List<LinksusTaskInteractWeiboMid> getLinksusTaskInteractWeiboMidList();

	/** 主键查询 */
	public LinksusTaskInteractWeiboMid getLinksusTaskInteractWeiboMidById(Long pid);

	/** 新增 */
	public Integer insertLinksusTaskInteractWeiboMid(LinksusTaskInteractWeiboMid entity);

	/** 更新 */
	public Integer updateLinksusTaskInteractWeiboMid(LinksusTaskInteractWeiboMid entity);

	/** 主键删除 */
	public Integer deleteLinksusTaskInteractWeiboMidById(Long pid);

	public List<LinksusTaskInteractWeiboMid> getWeiboMidTempListByMap(Map map);

	/** 清除临时表中数据  */
	public void deleteLinksusTaskInteractWeiboMid();

}
