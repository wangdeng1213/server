package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusInteractKeywordReply;

public interface LinksusInteractKeywordReplyMapper{

	/** �б��ѯ */
	public List<LinksusInteractKeywordReply> getLinksusInteractKeywordReplyList();

	/** ������ѯ */
	public LinksusInteractKeywordReply getLinksusInteractKeywordReplyById(Long pid);

	/** ���� */
	public Integer insertLinksusInteractKeywordReply(LinksusInteractKeywordReply entity);

	/** ���� */
	public Integer updateLinksusInteractKeywordReply(LinksusInteractKeywordReply entity);

	/** ����ɾ�� */
	public Integer deleteLinksusInteractKeywordReplyById(Long pid);

	/** ͨ��������ѯ����ѯ�˻������йؼ������� */
	public LinksusInteractKeywordReply getAllKeywordsByAccountId(Map accountId);

}
