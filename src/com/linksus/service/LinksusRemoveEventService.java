package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusRemoveEvent;

public interface LinksusRemoveEventService{

	/** 查询列表 
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
