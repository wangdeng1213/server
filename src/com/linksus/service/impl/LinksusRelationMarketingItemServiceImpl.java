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

	/**����Ӫ��������ѯӪ������*/
	public List<LinksusRelationMarketingItem> getItemsByMarketingID(long marketingId){
		return linksusRelationMarketingItemMapper.getItemsByMarketingID(marketingId);
	}

	/**����Ӫ��״̬*/
	public void updateMarketingItemStatus(LinksusRelationMarketingItem item){
		linksusRelationMarketingItemMapper.updateMarketingItemStatus(item);
	}

	/** ��������Ӫ���ӱ�״̬ */
	public Integer updateMoreMarketingItemStatus(LinksusRelationMarketingItem item){
		return linksusRelationMarketingItemMapper.updateMoreMarketingItemStatus(item);
	}

}