package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusWxMassGroup;

public interface LinksusWxMassGroupService{

	/** ��ѯ�б� */
	public List<LinksusWxMassGroup> getLinksusWxMassGroupList();

	/** ������ѯ */
	public LinksusWxMassGroup getLinksusWxMassGroupById(Long pid);

	/** ���� */
	public Integer insertLinksusWxMassGroup(LinksusWxMassGroup entity);

	/** ���� */
	public Integer updateLinksusWxMassGroup(LinksusWxMassGroup entity);

	/** ����ɾ�� */
	public Integer deleteLinksusWxMassGroupById(Long pid);

	/** ����Ⱥ����� */
	public Integer updateMassResultGroup(LinksusWxMassGroup entity);

	/** ͨ����Ϣmsgid��ȡwx_id  */
	public LinksusWxMassGroup getLinksusWxMassGroupByMsgId(Map mapInfo);

	/**ͨ��΢��id��ȡmsgId*/
	public List<LinksusWxMassGroup> getLinksusWxMassGroupByWxId(Long wxId);

}
