package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusInteractRecord;

public interface LinksusInteractRecordService{

	/** 查询列表 */
	public List<LinksusInteractRecord> getLinksusInteractRecordList();

	/** 主键查询 */
	public LinksusInteractRecord getLinksusInteractRecordById(Long pid);

	/** 新增 */
	public Integer insertLinksusInteractRecord(LinksusInteractRecord entity);

	/** 更新 */
	public Integer updateLinksusInteractRecord(LinksusInteractRecord entity);

	/** 主键删除 */
	public Integer deleteLinksusInteractRecordById(Long pid);

	/** 判断微博记录表中是否有互动记录 */
	public LinksusInteractRecord checkIsExsitRecord(LinksusInteractRecord entity);

	/** 根据主键更新记录时间及用户最新互动消息ID */
	public Integer updateLinksusInteractRecordById(Map params);

}
