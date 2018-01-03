package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusRelationStatistics;

public interface LinksusRelationStatisticsMapper{

	/** 列表查询 */
	public List<LinksusRelationStatistics> getLinksusRelationStatisticsList();

	/** 主键查询 */
	public LinksusRelationStatistics getLinksusRelationStatisticsById(Long pid);

	/** 新增 */
	public Integer insertLinksusRelationStatistics(LinksusRelationStatistics entity);

	/** 更新 */
	public Integer updateLinksusRelationStatistics(LinksusRelationStatistics entity);

	/** 主键删除 */
	public Integer deleteLinksusRelationStatisticsById(Long pid);

	/** 以账号删除 */
	public Integer deleteLinksusRelationStatisticsByMap(Map map);

	/** 批量新增 */
	public Integer insertALLLinksusRelationStatistics(LinksusRelationStatistics entity);

	/** 查询列表 */
	public List<LinksusRelationStatistics> getLinksusRelationStatisticsListByBean(LinksusRelationStatistics entity);
}
