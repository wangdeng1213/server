package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusRelationMarketingItem;

public interface LinksusRelationMarketingItemService{

	/**����Ӫ��������ѯӪ������*/
	public List<LinksusRelationMarketingItem> getItemsByMarketingID(long marketingId);

	/**����Ӫ��״̬*/
	public void updateMarketingItemStatus(LinksusRelationMarketingItem item);

	/** ��������Ӫ���ӱ�״̬ */
	public Integer updateMoreMarketingItemStatus(LinksusRelationMarketingItem item);

}
