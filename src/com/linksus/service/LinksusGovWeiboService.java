package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusGovWeibo;

public interface LinksusGovWeiboService{

	/** 查询列表 */
	public List<LinksusGovWeibo> getLinksusGovWeiboList();

	/** 主键查询 */
	public LinksusGovWeibo getLinksusGovWeiboById(Long pid);

	/** 新增 */
	public Integer insertLinksusGovWeibo(LinksusGovWeibo entity);

	/** 更新 */
	public Integer updateLinksusGovWeibo(LinksusGovWeibo entity);

	/** 主键删除 */
	public Integer deleteLinksusGovWeiboById(Long pid);

	/**
	 * 获取要发布的微博
	 * @param entity
	 * @return
	 */
	public List<LinksusGovWeibo> getWeiboImmeSend(LinksusGovWeibo entity);

	/**
	 * 更新发布状态
	 * @param id
	 * @param status
	 */
	public void updateSendWeiboStatus(LinksusGovWeibo entity);

	public void updateSendWeiboSucc(LinksusGovWeibo entity);

}
