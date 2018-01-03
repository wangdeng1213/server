package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusGovInteract;

public interface LinksusGovInteractService{

	/** ��ѯ�б� */
	public List<LinksusGovInteract> getLinksusGovInteractList();

	/** ������ѯ */
	public LinksusGovInteract getLinksusGovInteractById(Long pid);

	/** ���� */
	public Integer insertLinksusGovInteract(LinksusGovInteract entity);

	/** ���� */
	public Integer updateLinksusGovInteract(LinksusGovInteract entity);

	/** ����ɾ�� */
	public Integer deleteLinksusGovInteractById(Long pid);

	/**comment_id��account_type ɾ��΢��������Ϣ*/
	public Integer deleteLinksusGovInteractByMap(Map map);

	public List<LinksusGovInteract> getInteractGovListByMid(Map map);

	/**
	 * ���»ظ�״̬
	 * @param govInteract
	 */
	public void updateSendReplyStatus(LinksusGovInteract govInteract);

	public void updateSendReplySucc(LinksusGovInteract govInteract);

	/** <!--ȡ���ظ���Ϣ�������б�  -->*/
	public List<LinksusGovInteract> getLinksusGovInteractTaskList(LinksusGovInteract entity);

	public void saveLinksusGovInteract(List<LinksusGovInteract> list,String operType);
}
