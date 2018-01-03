package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusTypeAreaCode;

public interface LinksusTypeAreaCodeMapper{

	/** �б��ѯ */
	public List<LinksusTypeAreaCode> getLinksusTypeAreaCodeList();

	/** ������ѯ */
	public LinksusTypeAreaCode getLinksusTypeAreaCodeById(Long pid);

	/** ���� */
	public Integer insertLinksusTypeAreaCode(LinksusTypeAreaCode entity);

	/** ���� */
	public Integer updateLinksusTypeAreaCode(LinksusTypeAreaCode entity);

	/** ����ɾ�� */
	public Integer deleteLinksusTypeAreaCodeById(Long pid);

	/**��ȡ��������*/
	public List<LinksusTypeAreaCode> getLinksusTypeAreaCodeListByCode(Map codeMap);

	/** ������������ */
	public void replaceLinksusTypeAreaCodeInfo(List allArea);

	/** �����ѯ */
	public LinksusTypeAreaCode getLinksusTypeAreaCode(Map codeMap);

	public LinksusTypeAreaCode getProvicesInfo(Map codeMap);

	public Integer updateSinaAreaCodeByCode(Map codeMap);
}
