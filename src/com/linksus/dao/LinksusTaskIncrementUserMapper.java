package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusTaskIncrementUser;

public interface LinksusTaskIncrementUserMapper{

	/** �б��ѯ */
	public List<LinksusTaskIncrementUser> getLinksusTaskIncrementUserList();

	/** ������ѯ */
	public LinksusTaskIncrementUser getTaskIncrementUserByAccountId(Long accountId);

	/** ���� */
	public Integer insertLinksusTaskIncrementUser(LinksusTaskIncrementUser entity);

	/** ���� */
	public Integer updateLinksusTaskIncrementUser(LinksusTaskIncrementUser entity);

	/** ���� */
	public Integer updateLinksusTaskIncrementUserInfo(LinksusTaskIncrementUser entity);

	/** ����ɾ�� */
	public Integer deleteLinksusTaskIncrementUserById(Long pid);

	public Integer getCountByaccountId(Long pid);

	//��ѯ�˺ŵ�������˿�����б���˺ŵĵ�����id 
	public List<LinksusTaskIncrementUser> getIncrementalUserTaskList(LinksusTaskIncrementUser entity);

	//����������˿
	public Integer updateLinksusTaskIncrementFans(LinksusTaskIncrementUser entity);

	//����������ע
	public Integer updateLinksusTaskIncrementFollow(LinksusTaskIncrementUser entity);

}
