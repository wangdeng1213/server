package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusWxObjectSupply;

public interface LinksusWxObjectSupplyMapper{

	/** 列表查询 */
	public List<LinksusWxObjectSupply> getLinksusWxObjectSupplyList();

	/** 主键查询 */
	public LinksusWxObjectSupply getLinksusWxObjectSupplyById(Long pid);

	/** 新增 */
	public Integer insertLinksusWxObjectSupply(LinksusWxObjectSupply entity);

	/** 更新 */
	public Integer updateLinksusWxObjectSupply(LinksusWxObjectSupply entity);

	/** 主键删除 */
	public Integer deleteLinksusWxObjectSupplyById(Long pid);

	/** 查询单个发送微信内容 */
	public LinksusWxObjectSupply getSingleLinksusWxObjectSupplyById(Long pid);

	/** 查询多个发送微信内容*/
	public List<LinksusWxObjectSupply> getMoreLinksusWxObjectSupplyById(Long pid);

}
