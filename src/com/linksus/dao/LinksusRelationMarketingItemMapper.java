package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusRelationMarketingItem;

public interface LinksusRelationMarketingItemMapper{

	/**����Ӫ��������ѯӪ������*/
	public List<LinksusRelationMarketingItem> getItemsByMarketingID(long marketingId);

	/**����Ӫ��״̬*/
	public void updateMarketingItemStatus(LinksusRelationMarketingItem item);

	/** ��������Ӫ���ӱ�״̬ */
	public Integer updateMoreMarketingItemStatus(LinksusRelationMarketingItem item);

}
