package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusInteractAttentionReply;

public interface LinksusInteractAttentionReplyMapper{

	/** �б��ѯ */
	public List<LinksusInteractAttentionReply> getLinksusInteractAttentionReplyList();

	/** ������ѯ */
	public LinksusInteractAttentionReply getLinksusInteractAttentionReplyById(Long pid);

	/** ���� */
	public Integer insertLinksusInteractAttentionReply(LinksusInteractAttentionReply entity);

	/** ���� */
	public Integer updateLinksusInteractAttentionReply(LinksusInteractAttentionReply entity);

	/** ����ɾ�� */
	public Integer deleteLinksusInteractAttentionReplyById(Long pid);

	/** ͨ��������ƽ̨���Ͳ�ѯ */
	public LinksusInteractAttentionReply getLinksusInteractAttentionReplyByIdAndType(Map map);
}
