package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusInteractRecord;

public interface LinksusInteractRecordService{

	/** ��ѯ�б� */
	public List<LinksusInteractRecord> getLinksusInteractRecordList();

	/** ������ѯ */
	public LinksusInteractRecord getLinksusInteractRecordById(Long pid);

	/** ���� */
	public Integer insertLinksusInteractRecord(LinksusInteractRecord entity);

	/** ���� */
	public Integer updateLinksusInteractRecord(LinksusInteractRecord entity);

	/** ����ɾ�� */
	public Integer deleteLinksusInteractRecordById(Long pid);

	/** �ж�΢����¼�����Ƿ��л�����¼ */
	public LinksusInteractRecord checkIsExsitRecord(LinksusInteractRecord entity);

	/** �����������¼�¼ʱ�估�û����»�����ϢID */
	public Integer updateLinksusInteractRecordById(Map params);

}
