package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusRelationWxaccount;

public interface LinksusRelationWxaccountMapper{

	/** �б��ѯ */
	public List<LinksusRelationWxaccount> getLinksusRelationWxaccountList();

	/** ������ѯ */
	public LinksusRelationWxaccount getLinksusRelationWxaccountById(Long pid);

	/** ���� */
	public Integer insertLinksusRelationWxaccount(LinksusRelationWxaccount entity);

	/** ���� */
	public Integer updateLinksusRelationWxaccount(LinksusRelationWxaccount entity);

	/** ����ɾ�� */
	public Integer deleteLinksusRelationWxaccountById(Long pid);

	/** ��ѯMsg_MD5�Ƿ���� */
	public LinksusRelationWxaccount getLinksusRelationWxaccountByMsgMD5(String msgMd5);

	/** ���ݸ���������ѯ  */
	public LinksusRelationWxaccount getLinksusRelationWxaccountByPrimary(Map map);

}
