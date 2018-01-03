package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusRelationOperateItemMapper;
import com.linksus.entity.LinksusRelationOperateItem;
import com.linksus.service.LinksusRelationOperateItemService;

@Service("linksusRelationOperateItemService")
public class LinksusRelationOperateItemServiceImpl implements LinksusRelationOperateItemService{

	@Autowired
	private LinksusRelationOperateItemMapper linksusRelationOperateItemMapper;

	/** ��ѯ�б� */
	public List<LinksusRelationOperateItem> getLinksusRelationOperateItemList(){
		return linksusRelationOperateItemMapper.getLinksusRelationOperateItemList();
	}

	/** ������ѯ */
	public LinksusRelationOperateItem getLinksusRelationOperateItemById(Long pid){
		return linksusRelationOperateItemMapper.getLinksusRelationOperateItemById(pid);
	}

	/** ���� */
	public Integer insertLinksusRelationOperateItem(LinksusRelationOperateItem entity){
		return linksusRelationOperateItemMapper.insertLinksusRelationOperateItem(entity);
	}

	/** ���� */
	public Integer updateLinksusRelationOperateItem(LinksusRelationOperateItem entity){
		return linksusRelationOperateItemMapper.updateLinksusRelationOperateItem(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusRelationOperateItemById(Long pid){
		return linksusRelationOperateItemMapper.deleteLinksusRelationOperateItemById(pid);
	}
}