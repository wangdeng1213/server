package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusRelationMarketingItem;

public interface LinksusRelationMarketingItemMapper{

	/**根据营销主键查询营销对象*/
	public List<LinksusRelationMarketingItem> getItemsByMarketingID(long marketingId);

	/**更新营销状态*/
	public void updateMarketingItemStatus(LinksusRelationMarketingItem item);

	/** 批量更新营销子表状态 */
	public Integer updateMoreMarketingItemStatus(LinksusRelationMarketingItem item);

}
