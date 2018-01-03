package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusWxValidate;

public interface LinksusWxValidateService{

	/** ��ѯ�б� */
	public List<LinksusWxValidate> getLinksusWxValidateList();

	/** ������ѯ */
	public LinksusWxValidate getLinksusWxValidateById(Long pid);

	/** ���� */
	public Integer insertLinksusWxValidate(LinksusWxValidate entity);

	/** ���� */
	public Integer updateLinksusWxValidate(LinksusWxValidate entity);

	/** ����ɾ�� */
	public Integer deleteLinksusWxValidateById(Long pid);

	/** ͨ��΢�źŲ�ѯ*/
	public LinksusWxValidate getLinksusWxValidateByWxnum(String wxnum);

}
