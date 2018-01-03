package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusRelationOperate;

public interface LinksusRelationOperateService{

	/** ��ѯ�б� */
	public List<LinksusRelationOperate> getLinksusRelationOperateList();

	/** ������ѯ */
	public LinksusRelationOperate getLinksusRelationOperateById(Long pid);

	/** ���� */
	public Integer insertLinksusRelationOperate(LinksusRelationOperate entity);

	/** ���� */
	public Integer updateLinksusRelationOperate(LinksusRelationOperate entity);

	/** ����ɾ�� */
	public Integer deleteLinksusRelationOperateById(Long pid);

	//�������²�����ʧ�ܴ���
	public Integer updateLinksusRelationOperFailNum(Long pid);

	//�������²����ɹ�����
	public Integer updateLinksusRelationOperSuccessNum(Long pid);

	//�ж����������Ƿ�ͨ������
	public LinksusRelationOperate checkIsExsitRelationOperate(String paraStr);

}
