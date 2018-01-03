package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusGovWeibo;

public interface LinksusGovWeiboService{

	/** ��ѯ�б� */
	public List<LinksusGovWeibo> getLinksusGovWeiboList();

	/** ������ѯ */
	public LinksusGovWeibo getLinksusGovWeiboById(Long pid);

	/** ���� */
	public Integer insertLinksusGovWeibo(LinksusGovWeibo entity);

	/** ���� */
	public Integer updateLinksusGovWeibo(LinksusGovWeibo entity);

	/** ����ɾ�� */
	public Integer deleteLinksusGovWeiboById(Long pid);

	/**
	 * ��ȡҪ������΢��
	 * @param entity
	 * @return
	 */
	public List<LinksusGovWeibo> getWeiboImmeSend(LinksusGovWeibo entity);

	/**
	 * ���·���״̬
	 * @param id
	 * @param status
	 */
	public void updateSendWeiboStatus(LinksusGovWeibo entity);

	public void updateSendWeiboSucc(LinksusGovWeibo entity);

}
