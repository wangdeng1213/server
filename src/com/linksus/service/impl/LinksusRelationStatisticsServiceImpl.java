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

	/** 查询列表 */
	public List<LinksusRelationStatistics> getLinksusRelationStatisticsList(){
		return linksusRelationStatisticsMapper.getLinksusRelationStatisticsList();
	}

	/** 主键查询 */
	public LinksusRelationStatistics getLinksusRelationStatisticsById(Long pid){
		return linksusRelationStatisticsMapper.getLinksusRelationStatisticsById(pid);
	}

	/** 新增 */
	public Integer insertLinksusRelationStatistics(LinksusRelationStatistics entity){
		return linksusRelationStatisticsMapper.insertLinksusRelationStatistics(entity);
	}

	/** 更新 */
	public Integer updateLinksusRelationStatistics(LinksusRelationStatistics entity){
		return linksusRelationStatisticsMapper.updateLinksusRelationStatistics(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusRelationStatisticsById(Long pid){
		return linksusRelationStatisticsMapper.deleteLinksusRelationStatisticsById(pid);
	}

	/** 以账号删除 */
	public Integer deleteLinksusRelationStatisticsByMap(Map map){
		return linksusRelationStatisticsMapper.deleteLinksusRelationStatisticsByMap(map);
	}

	/** 批量新增 */
	public Integer insertALLLinksusRelationStatistics(LinksusRelationStatistics entity){
		return linksusRelationStatisticsMapper.insertALLLinksusRelationStatistics(entity);
	}

	/** 查询列表 */
	public List<LinksusRelationStatistics> getLinksusRelationStatisticsListByBean(LinksusRelationStatistics entity){
		return linksusRelationStatisticsMapper.getLinksusRelationStatisticsListByBean(entity);
	}
}