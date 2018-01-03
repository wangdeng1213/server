package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusTaskInteractWeiboMid;

public interface LinksusTaskInteractWeiboMidService{

	/** 查询列表 */
	public List<LinksusTaskInteractWeiboMid> getLinksusTaskInteractWeiboMidList();

	/** 主键查询 */
	public LinksusTaskInteractWeiboMid getLinksusTaskInteractWeiboMidById(Long pid);

	/** 新增 */
	public Integer insertLinksusTaskInteractWeiboMid(LinksusTaskInteractWeiboMid entity);

	/** 更新 */
	public Integer updateLinksusTaskInteractWeiboMid(LinksusTaskInteractWeiboMid entity);

	/** 主键删除 */
	public Integer deleteLinksusTaskInteractWeiboMidById(Long pid);

	/**根据类型和账号查询出临时表中互动的数据*/
	public List<LinksusTaskInteractWeiboMid> getWeiboMidTempListByMap(Map map);

	/** 清除临时表中数据  */
	public void deleteLinksusTaskInteractWeiboMid();

}
