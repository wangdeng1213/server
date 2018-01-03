package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusGovInteract;

public interface LinksusGovInteractMapper{

	/** �б��ѯ */
	public List<LinksusGovInteract> getLinksusGovInteractList();

	/** ������ѯ */
	public LinksusGovInteract getLinksusGovInteractById(Long pid);

	/** ���� */
	public Integer insertLinksusGovInteract(LinksusGovInteract entity);

	/** ���� */
	public Integer updateLinksusGovInteract(LinksusGovInteract entity);

	/** ����ɾ�� */
	public Integer deleteLinksusGovInteractById(Long pid);

	public Integer deleteLinksusGovInteractByMap(Map map);

	public List<LinksusGovInteract> getInteractGovListByMid(Map map);

	public void updateSendReplyStatus(LinksusGovInteract govInteract);

	public void updateSendReplySucc(LinksusGovInteract govInteract);

	public List<LinksusGovInteract> getLinksusGovInteractTaskList(LinksusGovInteract entity);

}
