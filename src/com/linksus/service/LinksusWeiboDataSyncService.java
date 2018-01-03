package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusWeiboDataSync;

/**
 * 抓取微博信息界限表接口
 * @author wangdeng
 *
 */
public interface LinksusWeiboDataSyncService{

	public List<LinksusWeiboDataSync> getLinksusWeiboDataSyncList();

	public void updateLinksusWeiboDataSync(List<LinksusWeiboDataSync> list);

	public void updateLinksusWeiboDataSyncSingle(LinksusWeiboDataSync linksusWeiboDataSync);

	public Integer getLinksusWeiboDataSyncCount(LinksusWeiboDataSync linksusWeiboDataSync);

	public void insertLinksusWeiboDataSyncSingle(LinksusWeiboDataSync linksusWeiboDataSync);
}
