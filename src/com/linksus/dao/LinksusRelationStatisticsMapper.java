package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusRelationStatistics;

public interface LinksusRelationStatisticsMapper{

	/** �б��ѯ */
	public List<LinksusRelationStatistics> getLinksusRelationStatisticsList();

	/** ������ѯ */
	public LinksusRelationStatistics getLinksusRelationStatisticsById(Long pid);

	/** ���� */
	public Integer insertLinksusRelationStatistics(LinksusRelationStatistics entity);

	/** ���� */
	public Integer updateLinksusRelationStatistics(LinksusRelationStatistics entity);

	/** ����ɾ�� */
	public Integer deleteLinksusRelationStatisticsById(Long pid);

	/** ���˺�ɾ�� */
	public Integer deleteLinksusRelationStatisticsByMap(Map map);

	/** �������� */
	public Integer insertALLLinksusRelationStatistics(LinksusRelationStatistics entity);

	/** ��ѯ�б� */
	public List<LinksusRelationStatistics> getLinksusRelationStatisticsListByBean(LinksusRelationStatistics entity);
}
