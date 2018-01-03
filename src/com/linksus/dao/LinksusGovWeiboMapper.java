package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusGovWeibo;

public interface LinksusGovWeiboMapper{

	/** �б��ѯ */
	public List<LinksusGovWeibo> getLinksusGovWeiboList();

	/** ������ѯ */
	public LinksusGovWeibo getLinksusGovWeiboById(Long pid);

	/** ���� */
	public Integer insertLinksusGovWeibo(LinksusGovWeibo entity);

	/** ���� */
	public Integer updateLinksusGovWeibo(LinksusGovWeibo entity);

	/** ����ɾ�� */
	public Integer deleteLinksusGovWeiboById(Long pid);

	public List<LinksusGovWeibo> getWeiboImmeSend(LinksusGovWeibo entity);

	public void updateSendWeiboStatus(LinksusGovWeibo entity);

	public void updateSendWeiboSucc(LinksusGovWeibo entity);

}
