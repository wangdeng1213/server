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

	/** ��ѯ�б� */
	public List<LinksusRemoveEvent> getRemoveWeiboList(LinksusRemoveEvent removeEvent){
		return linksusRemoveEventMapper.getRemoveWeiboList(removeEvent);
	}

	/** ������ѯ */
	public LinksusRemoveEvent getLinksusRemoveEventById(Long pid){
		return linksusRemoveEventMapper.getLinksusRemoveEventById(pid);
	}

	/** ���� */
	public Integer insertLinksusRemoveEvent(LinksusRemoveEvent entity){
		return linksusRemoveEventMapper.insertLinksusRemoveEvent(entity);
	}

	/** ���� */
	public Integer updateLinksusRemoveEvent(LinksusRemoveEvent entity){
		return linksusRemoveEventMapper.updateLinksusRemoveEvent(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusRemoveEventById(Long pid){
		return linksusRemoveEventMapper.deleteLinksusRemoveEventById(pid);
	}

	@Override
	public void updateRemoveEventStatus(LinksusRemoveEvent updtEvent){
		linksusRemoveEventMapper.updateRemoveEventStatus(updtEvent);
	}
}