package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusInteractWxMenuAcct;

public interface LinksusInteractWxMenuAcctMapper{

	/** �б��ѯ */
	public List<LinksusInteractWxMenuAcct> getLinksusInteractWxMenuAcctList();

	/** ������ѯ */
	public LinksusInteractWxMenuAcct getLinksusInteractWxMenuAcctById(Long pid);

	/** ���� */
	public Integer insertLinksusInteractWxMenuAcct(LinksusInteractWxMenuAcct entity);

	/** ���� */
	public Integer updateLinksusInteractWxMenuAcct(LinksusInteractWxMenuAcct entity);

	/** ����ɾ�� */
	public Integer deleteLinksusInteractWxMenuAcctById(Long pid);

	/** ����account_id��ѯ���в˵�������Ϣ */
	public LinksusInteractWxMenuAcct getLinksusInteractWxMenuAcctByAccountId(Long accountId);

}
