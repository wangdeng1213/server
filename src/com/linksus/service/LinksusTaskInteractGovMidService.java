package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusTaskInteractGovMid;

public interface LinksusTaskInteractGovMidService{

	/** 查询列表 */
	public List<LinksusTaskInteractGovMid> getLinksusTaskInteractGovMidList();

	/** 主键查询 */
	public LinksusTaskInteractGovMid getLinksusTaskInteractGovMidById(LinksusTaskInteractGovMid govMid);

	/** 新增 */
	public Integer insertLinksusTaskInteractGovMid(LinksusTaskInteractGovMid entity);

	/**
	 * 批量保存
	 * @param list
	 * @param operType
	 * @return
	 */
	public Integer saveLinksusInteractGovMid(List<LinksusTaskInteractGovMid> list,String operType);

	/** 更新 */
	public Integer updateLinksusTaskInteractGovMid(LinksusTaskInteractGovMid entity);

	/** 主键删除 */
	public Integer deleteLinksusTaskInteractGovMidById(LinksusTaskInteractGovMid pid);

	public List<LinksusTaskInteractGovMid> getGovMidTempListByMap(Map map);

	public void deleteLinksusTaskInteractGovMid();

	/**
	 * 根据MID进行查询
	 * @param mid
	 * @return
	 */
	public LinksusTaskInteractGovMid getLinksusTaskInteractGovMidListByMid(Long mid);

	/**
	 * 根据Mid进行删除
	 * @param mid
	 * @return
	 */
	public Integer deleteLinkSusTaskInteractGovMidByMid(Long mid);

	/**
	 * 获取多问微博对于的mid
	 * @return
	 */
	public List<Long> getDuoWenInteractGovMid();

	/**
	 * 取得评论并转发的记录
	 * @return
	 */
	public List<LinksusTaskInteractGovMid> getLinksusCommentAndRelayGovMidList();

	/**
	 * 更新互动类型
	 */
	public void updateInteractType(LinksusTaskInteractGovMid govMid);

}
