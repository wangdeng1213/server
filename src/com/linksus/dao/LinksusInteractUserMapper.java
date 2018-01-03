package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusInteractUser;

public interface LinksusInteractUserMapper{

	/** �б��ѯ */
	public List<LinksusInteractUser> getLinksusInteractUserList();

	/** ������ѯ */
	public LinksusInteractUser getLinksusInteractUserById(Long pid);

	/** ���� */
	public Integer insertLinksusInteractUser(LinksusInteractUser entity);

	/** ���� */
	public Integer updateLinksusInteractUser(LinksusInteractUser entity);

	/** ����ɾ�� */
	public Integer deleteLinksusInteractUserById(Long pid);

	/**��黥���û� �Ƿ� ���� */
	public LinksusInteractUser checkUserDataIsExsit(LinksusInteractUser entity);

	public List getInteractUserImageList(Map tempMap);

}
