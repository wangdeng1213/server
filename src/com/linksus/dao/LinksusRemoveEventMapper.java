package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusRemoveEvent;

public interface LinksusRemoveEventMapper{

	/** 列表查询 
	 * @param removeEvent */
	public List<LinksusRemoveEvent> getRemoveWeiboList(LinksusRemoveEvent removeEvent);

	/** 主键查询 */
	public LinksusRemoveEvent getLinksusRemoveEventById(Long pid);

	/** 新增 */
	public Integer insertLinksusRemoveEvent(LinksusRemoveEvent entity);

	/** 更新 */
	public Integer updateLinksusRemoveEvent(LinksusRemoveEvent entity);

	/** 主键删除 */
	public Integer deleteLinksusRemoveEventById(Long pid);

	public void updateRemoveEventStatus(LinksusRemoveEvent updtEvent);

}
