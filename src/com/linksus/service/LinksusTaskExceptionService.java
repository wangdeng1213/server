package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusTaskException;

public interface LinksusTaskExceptionService{

	/** ��ѯ�б� */
	public List<LinksusTaskException> getLinksusTaskExceptionList();

	/** ������ѯ */
	public LinksusTaskException getLinksusTaskExceptionById(Long pid);

	/** ���� */
	public Integer insertLinksusTaskException(LinksusTaskException entity);

	/** ���� */
	public Integer updateLinksusTaskException(LinksusTaskException entity);

	/** ����ɾ�� */
	public Integer deleteLinksusTaskExceptionById(Long pid);

}
