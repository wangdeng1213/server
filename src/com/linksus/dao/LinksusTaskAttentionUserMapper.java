package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusTaskAttentionUser;

public interface LinksusTaskAttentionUserMapper{

	/** �б��ѯ */
	public List<LinksusTaskAttentionUser> getLinksusTaskAttentionUserList();

	/** ������ѯ */
	public LinksusTaskAttentionUser getLinksusTaskAttentionUserById(Long pid);

	/** �жϷ�˿�����Ƿ���� */
	public Integer countLinksusTaskAttentionUser(LinksusTaskAttentionUser entity);

	/** ���� */
	public Integer insertLinksusTaskAttentionUser(LinksusTaskAttentionUser entity);

	/** ���� */
	public Integer updateLinksusTaskAttentionUser(LinksusTaskAttentionUser entity);

	/** ����ɾ�� */
	public Integer deleteLinksusTaskAttentionUserById(Long pid);

	/** ��ѯ�б� */
	public List<LinksusTaskAttentionUser> getAllAttentionByUserList(LinksusTaskAttentionUser entity);
}
