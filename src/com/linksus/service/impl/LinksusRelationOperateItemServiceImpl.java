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

	/** 查询列表 */
	public List<LinksusRelationOperateItem> getLinksusRelationOperateItemList(){
		return linksusRelationOperateItemMapper.getLinksusRelationOperateItemList();
	}

	/** 主键查询 */
	public LinksusRelationOperateItem getLinksusRelationOperateItemById(Long pid){
		return linksusRelationOperateItemMapper.getLinksusRelationOperateItemById(pid);
	}

	/** 新增 */
	public Integer insertLinksusRelationOperateItem(LinksusRelationOperateItem entity){
		return linksusRelationOperateItemMapper.insertLinksusRelationOperateItem(entity);
	}

	/** 更新 */
	public Integer updateLinksusRelationOperateItem(LinksusRelationOperateItem entity){
		return linksusRelationOperateItemMapper.updateLinksusRelationOperateItem(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusRelationOperateItemById(Long pid){
		return linksusRelationOperateItemMapper.deleteLinksusRelationOperateItemById(pid);
	}
}