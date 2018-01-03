package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusRelationMarketingItemMapper;
import com.linksus.entity.LinksusRelationMarketingItem;
import com.linksus.service.LinksusRelationMarketingItemService;

@Service("linksusRelationMarketingItemService")
public class LinksusRelationMarketingItemServiceImpl implements LinksusRelationMarketingItemService{

	@Autowired
	private LinksusRelationMarketingItemMapper linksusRelationMarketingItemMapper;

	/**根据营销主键查询营销对象*/
	public List<LinksusRelationMarketingItem> getItemsByMarketingID(long marketingId){
		return linksusRelationMarketingItemMapper.getItemsByMarketingID(marketingId);
	}

	/**更新营销状态*/
	public void updateMarketingItemStatus(LinksusRelationMarketingItem item){
		linksusRelationMarketingItemMapper.updateMarketingItemStatus(item);
	}

	/** 批量更新营销子表状态 */
	public Integer updateMoreMarketingItemStatus(LinksusRelationMarketingItem item){
		return linksusRelationMarketingItemMapper.updateMoreMarketingItemStatus(item);
	}

}