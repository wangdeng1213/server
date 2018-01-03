package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.InteractListQueryObj;
import com.linksus.entity.LinksusInteractPerson;

public interface LinksusInteractPersonMapper{

	/** �б��ѯ */
	public List<LinksusInteractPerson> getLinksusInteractPersonList();

	/** ������ѯ */
	public LinksusInteractPerson getLinksusInteractPersonById(Long pid);

	/** ���� */
	public Integer insertLinksusInteractPerson(LinksusInteractPerson entity);

	/** ���� */
	public Integer updateLinksusInteractPerson(LinksusInteractPerson entity);

	/** ����ɾ�� */
	public Integer deleteLinksusInteractPersonById(Long pid);

	/** ��������Ƿ���ڻ�����Ա���� */
	public LinksusInteractPerson checkDataIsExsitInteractPerson(LinksusInteractPerson entity);

	/** ��ѯ�����б�*/
	public List queryInteractDataList(InteractListQueryObj queryObj);

	/** ��ѯ�����б�����*/
	public Integer queryInteractDataListCount(InteractListQueryObj queryObj);

	public Map queryInteractDataList2(Map paramMap);

	/**�»����б��ѯ*/
	public List newQueryInteractDataList(InteractListQueryObj queryObj);
}
