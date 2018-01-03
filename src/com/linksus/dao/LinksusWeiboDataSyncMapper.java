package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusWeiboDataSync;

/**
 * ��¼ץȡ��Ϣ���޽ӿ�
 * @author wangdeng
 *
 */
public interface LinksusWeiboDataSyncMapper{

	public List<LinksusWeiboDataSync> getLinksusWeiboDataSyncList();

	public void updateLinksusWeiboDataSync(List<LinksusWeiboDataSync> list);

	public void updateLinksusWeiboDataSyncSingle(LinksusWeiboDataSync linksusWeiboDataSync);

	public Integer getLinksusWeiboDataSyncCount(LinksusWeiboDataSync linksusWeiboDataSync);

	public void insertLinksusWeiboDataSyncSingle(LinksusWeiboDataSync linksusWeiboDataSync);
}
