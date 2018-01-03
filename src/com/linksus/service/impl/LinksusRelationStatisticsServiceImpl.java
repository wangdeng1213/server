package com.linksus.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusRelationStatisticsMapper;
import com.linksus.entity.LinksusRelationStatistics;
import com.linksus.service.LinksusRelationStatisticsService;

@Service("linksusRelationStatisticsService")
public class LinksusRelationStatisticsServiceImpl implements LinksusRelationStatisticsService{

	@Autowired
	private LinksusRelationStatisticsMapper linksusRelationStatisticsMapper;

	/** ��ѯ�б� */
	public List<LinksusRelationStatistics> getLinksusRelationStatisticsList(){
		return linksusRelationStatisticsMapper.getLinksusRelationStatisticsList();
	}

	/** ������ѯ */
	public LinksusRelationStatistics getLinksusRelationStatisticsById(Long pid){
		return linksusRelationStatisticsMapper.getLinksusRelationStatisticsById(pid);
	}

	/** ���� */
	public Integer insertLinksusRelationStatistics(LinksusRelationStatistics entity){
		return linksusRelationStatisticsMapper.insertLinksusRelationStatistics(entity);
	}

	/** ���� */
	public Integer updateLinksusRelationStatistics(LinksusRelationStatistics entity){
		return linksusRelationStatisticsMapper.updateLinksusRelationStatistics(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusRelationStatisticsById(Long pid){
		return linksusRelationStatisticsMapper.deleteLinksusRelationStatisticsById(pid);
	}

	/** ���˺�ɾ�� */
	public Integer deleteLinksusRelationStatisticsByMap(Map map){
		return linksusRelationStatisticsMapper.deleteLinksusRelationStatisticsByMap(map);
	}

	/** �������� */
	public Integer insertALLLinksusRelationStatistics(LinksusRelationStatistics entity){
		return linksusRelationStatisticsMapper.insertALLLinksusRelationStatistics(entity);
	}

	/** ��ѯ�б� */
	public List<LinksusRelationStatistics> getLinksusRelationStatisticsListByBean(LinksusRelationStatistics entity){
		return linksusRelationStatisticsMapper.getLinksusRelationStatisticsListByBean(entity);
	}
}