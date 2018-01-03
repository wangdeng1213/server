package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusTaskInteractGovMid;

public interface LinksusTaskInteractGovMidMapper{

	/** 列表查询 */
	public List<LinksusTaskInteractGovMid> getLinksusTaskInteractGovMidList();

	/** 主键查询 */
	public LinksusTaskInteractGovMid getLinksusTaskInteractGovMidById(LinksusTaskInteractGovMid pid);

	/** 新增 */
	public Integer insertLinksusTaskInteractGovMid(LinksusTaskInteractGovMid entity);

	/** 更新 */
	public Integer updateLinksusTaskInteractGovMid(LinksusTaskInteractGovMid entity);

	/** 主键删除 */
	public Integer deleteLinksusTaskInteractGovMidById(LinksusTaskInteractGovMid pid);

	public List<LinksusTaskInteractGovMid> getGovMidTempListByMap(Map map);

	public Integer deleteLinksusTaskInteractGovMid();

	/** 根据MID查询 */
	public List<LinksusTaskInteractGovMid> getLinksusTaskInteractGovMidListByMid(Long mid);

	/**
	 * 根据MID删除中间表
	 * @param mid
	 * @return
	 */
	public Integer deleteLinkSusTaskInteractGovMidByMid(Long mid);

	/**
	 * 获取多问微博
	 * @return
	 */
	public List<Long> getDuoWenInteractGovMid();

	/**
	 * 取得评论并转发的记录
	 * @return
	 */
	public List<LinksusTaskInteractGovMid> getLinksusCommentAndRelayGovMidList();

	/** 更新互动类型***/
	public void updateInteractType(LinksusTaskInteractGovMid govMid);
}
