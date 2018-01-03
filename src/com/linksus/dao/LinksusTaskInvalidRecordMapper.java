package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusTaskInvalidRecord;

public interface LinksusTaskInvalidRecordMapper{

	/** 列表查询 */
	public List<LinksusTaskInvalidRecord> getLinksusTaskInvalidRecordList();

	/** 主键查询 */
	public LinksusTaskInvalidRecord getLinksusTaskInvalidRecordById(Long pid);

	/** 新增 */
	public Integer insertLinksusTaskInvalidRecord(LinksusTaskInvalidRecord entity);

	/** 更新 */
	public Integer updateLinksusTaskInvalidRecord(LinksusTaskInvalidRecord entity);

	/** 主键删除 */
	public Integer deleteLinksusTaskInvalidRecordById(Long pid);

}
