package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusTypeAreaCode;

public interface LinksusTypeAreaCodeMapper{

	/** 列表查询 */
	public List<LinksusTypeAreaCode> getLinksusTypeAreaCodeList();

	/** 主键查询 */
	public LinksusTypeAreaCode getLinksusTypeAreaCodeById(Long pid);

	/** 新增 */
	public Integer insertLinksusTypeAreaCode(LinksusTypeAreaCode entity);

	/** 更新 */
	public Integer updateLinksusTypeAreaCode(LinksusTypeAreaCode entity);

	/** 主键删除 */
	public Integer deleteLinksusTypeAreaCodeById(Long pid);

	/**获取地区对象*/
	public List<LinksusTypeAreaCode> getLinksusTypeAreaCodeListByCode(Map codeMap);

	/** 批量插入数据 */
	public void replaceLinksusTypeAreaCodeInfo(List allArea);

	/** 代码查询 */
	public LinksusTypeAreaCode getLinksusTypeAreaCode(Map codeMap);

	public LinksusTypeAreaCode getProvicesInfo(Map codeMap);

	public Integer updateSinaAreaCodeByCode(Map codeMap);
}
