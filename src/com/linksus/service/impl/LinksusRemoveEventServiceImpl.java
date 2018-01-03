package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusRemoveEventMapper;
import com.linksus.entity.LinksusRemoveEvent;
import com.linksus.service.LinksusRemoveEventService;

@Service("linksusRemoveEventService")
public class LinksusRemoveEventServiceImpl implements LinksusRemoveEventService{

	@Autowired
	private LinksusRemoveEventMapper linksusRemoveEventMapper;

	/** 查询列表 */
	public List<LinksusRemoveEvent> getRemoveWeiboList(LinksusRemoveEvent removeEvent){
		return linksusRemoveEventMapper.getRemoveWeiboList(removeEvent);
	}

	/** 主键查询 */
	public LinksusRemoveEvent getLinksusRemoveEventById(Long pid){
		return linksusRemoveEventMapper.getLinksusRemoveEventById(pid);
	}

	/** 新增 */
	public Integer insertLinksusRemoveEvent(LinksusRemoveEvent entity){
		return linksusRemoveEventMapper.insertLinksusRemoveEvent(entity);
	}

	/** 更新 */
	public Integer updateLinksusRemoveEvent(LinksusRemoveEvent entity){
		return linksusRemoveEventMapper.updateLinksusRemoveEvent(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusRemoveEventById(Long pid){
		return linksusRemoveEventMapper.deleteLinksusRemoveEventById(pid);
	}

	@Override
	public void updateRemoveEventStatus(LinksusRemoveEvent updtEvent){
		linksusRemoveEventMapper.updateRemoveEventStatus(updtEvent);
	}
}