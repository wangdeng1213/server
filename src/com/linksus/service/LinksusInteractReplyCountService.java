package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusInteractReplyCount;

public interface LinksusInteractReplyCountService{

	/** ��ѯ�б� */
	public List<LinksusInteractReplyCount> getLinksusInteractReplyCountList();

	/** ������ѯ */
	public LinksusInteractReplyCount getLinksusInteractReplyCountById(Long pid);

	/** ���� */
	public Integer insertLinksusInteractReplyCount(LinksusInteractReplyCount entity);

	/** ���� */
	public Integer updateLinksusInteractReplyCount(LinksusInteractReplyCount entity);

	/** ����ɾ�� */
	public Integer deleteLinksusInteractReplyCountById(Long pid);

	/** ҵ�����������ѯ */
	public LinksusInteractReplyCount getLinksusInteractReplyCountByMap(Map map);

}
