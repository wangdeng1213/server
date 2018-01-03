package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusInteractAttentionReplyAcct;

public interface LinksusInteractAttentionReplyAcctService{

	/** ��ѯ�б� */
	public List<LinksusInteractAttentionReplyAcct> getLinksusInteractAttentionReplyAcctList();

	/** ������ѯ */
	public LinksusInteractAttentionReplyAcct getLinksusInteractAttentionReplyAcctById(Long pid);

	/** ���� */
	public Integer insertLinksusInteractAttentionReplyAcct(LinksusInteractAttentionReplyAcct entity);

	/** ���� */
	public Integer updateLinksusInteractAttentionReplyAcct(LinksusInteractAttentionReplyAcct entity);

	/** ����ɾ�� */
	public Integer deleteLinksusInteractAttentionReplyAcctById(Long pid);

	/** accountId������ѯ */
	public LinksusInteractAttentionReplyAcct getLinksusInteractAttentionReplyAcctByAccountId(Long accountId);

}
