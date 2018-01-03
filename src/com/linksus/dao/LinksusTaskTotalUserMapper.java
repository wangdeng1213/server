package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusTaskTotalUser;

public interface LinksusTaskTotalUserMapper{

	/** �б��ѯ */
	public List<LinksusTaskTotalUser> getLinksusTaskTotalUserList();

	/** ������ѯ */
	public LinksusTaskTotalUser getLinksusTaskTotalUserById(LinksusTaskTotalUser totalUser);

	/** ���� */
	public Integer insertLinksusTaskTotalUser(LinksusTaskTotalUser entity);

	/** ���� */
	public Integer updateLinksusTaskTotalUser(LinksusTaskTotalUser entity);

	/** ����ɾ�� */
	public Integer deleteLinksusTaskTotalUserById(Long pid);

	/** ��ҳ��ȡ��������δִ��ȫ�������*/
	public List<LinksusTaskTotalUser> getLinksusTaskTotalUsersByType(LinksusTaskTotalUser totalUser);

	/** ��������״̬*/
	public Integer updateLinksusTaskTotalUsersStatus(LinksusTaskTotalUser totalUser);

	/** �������һ���û�id */
	public Integer updateLinksusTaskTotalUsersCursor(LinksusTaskTotalUser totalUser);

}
