package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusRelationUserEdu;

public interface LinksusRelationUserEduService{

	/** ��ѯ�б� */
	public List<LinksusRelationUserEdu> getLinksusRelationUserEduList();

	/** ������ѯ */
	public LinksusRelationUserEdu getLinksusRelationUserEduById(Long pid);

	/** ���� */
	public Integer insertLinksusRelationUserEdu(LinksusRelationUserEdu entity);

	/** ���� */
	public Integer updateLinksusRelationUserEdu(LinksusRelationUserEdu entity);

	/** ����ɾ�� */
	public Integer deleteLinksusRelationUserEduById(Long pid);

	public Integer deleteLinksusRelationUserEduByUserId(Long userId);

}
