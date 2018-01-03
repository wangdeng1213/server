package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusTaskException;

public interface LinksusTaskExceptionMapper{

	/** �б��ѯ */
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
