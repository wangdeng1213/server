package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusInteractKeywordReplyAcct;

public interface LinksusInteractKeywordReplyAcctService{

	/** ��ѯ�б� */
	public List<LinksusInteractKeywordReplyAcct> getLinksusInteractKeywordReplyAcctList();

	/** ������ѯ */
	public LinksusInteractKeywordReplyAcct getLinksusInteractKeywordReplyAcctById(Long pid);

	/** ���� */
	public Integer insertLinksusInteractKeywordReplyAcct(LinksusInteractKeywordReplyAcct entity);

	/** ���� */
	public Integer updateLinksusInteractKeywordReplyAcct(LinksusInteractKeywordReplyAcct entity);

	/** ����ɾ�� */
	public Integer deleteLinksusInteractKeywordReplyAcctById(Long pid);

}
