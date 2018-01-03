package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusTaskInvalidRecord;

public interface LinksusTaskInvalidRecordMapper{

	/** �б��ѯ */
	public List<LinksusTaskInvalidRecord> getLinksusTaskInvalidRecordList();

	/** ������ѯ */
	public LinksusTaskInvalidRecord getLinksusTaskInvalidRecordById(Long pid);

	/** ���� */
	public Integer insertLinksusTaskInvalidRecord(LinksusTaskInvalidRecord entity);

	/** ���� */
	public Integer updateLinksusTaskInvalidRecord(LinksusTaskInvalidRecord entity);

	/** ����ɾ�� */
	public Integer deleteLinksusTaskInvalidRecordById(Long pid);

}
