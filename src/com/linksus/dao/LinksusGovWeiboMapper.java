package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusGovWeibo;

public interface LinksusGovWeiboMapper{

	/** 列表查询 */
	public List<LinksusGovWeibo> getLinksusGovWeiboList();

	/** 主键查询 */
	public LinksusGovWeibo getLinksusGovWeiboById(Long pid);

	/** 新增 */
	public Integer insertLinksusGovWeibo(LinksusGovWeibo entity);

	/** 更新 */
	public Integer updateLinksusGovWeibo(LinksusGovWeibo entity);

	/** 主键删除 */
	public Integer deleteLinksusGovWeiboById(Long pid);

	public List<LinksusGovWeibo> getWeiboImmeSend(LinksusGovWeibo entity);

	public void updateSendWeiboStatus(LinksusGovWeibo entity);

	public void updateSendWeiboSucc(LinksusGovWeibo entity);

}
