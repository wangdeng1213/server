package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusWxValidate;

public interface LinksusWxValidateMapper{

	/** 列表查询 */
	public List<LinksusWxValidate> getLinksusWxValidateList();

	/** 主键查询 */
	public LinksusWxValidate getLinksusWxValidateById(Long pid);

	/** 新增 */
	public Integer insertLinksusWxValidate(LinksusWxValidate entity);

	/** 更新 */
	public Integer updateLinksusWxValidate(LinksusWxValidate entity);

	/** 主键删除 */
	public Integer deleteLinksusWxValidateById(Long pid);

	/** 通过微信号查询*/
	public LinksusWxValidate getLinksusWxValidateByWxnum(String wxnum);

}
