package com.linksus.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusInteractReplyCountMapper;
import com.linksus.entity.LinksusInteractReplyCount;
import com.linksus.service.LinksusInteractReplyCountService;

@Service("linksusInteractReplyCountService")
public class LinksusInteractReplyCountServiceImpl implements LinksusInteractReplyCountService{

	@Autowired
	private LinksusInteractReplyCountMapper linksusInteractReplyCountMapper;

	/** ��ѯ�б� */
	public List<LinksusInteractReplyCount> getLinksusInteractReplyCountList(){
		return linksusInteractReplyCountMapper.getLinksusInteractReplyCountList();
	}

	/** ������ѯ */
	public LinksusInteractReplyCount getLinksusInteractReplyCountById(Long pid){
		return linksusInteractReplyCountMapper.getLinksusInteractReplyCountById(pid);
	}

	/** ���� */
	public Integer insertLinksusInteractReplyCount(LinksusInteractReplyCount entity){
		return linksusInteractReplyCountMapper.insertLinksusInteractReplyCount(entity);
	}

	/** ���� */
	public Integer updateLinksusInteractReplyCount(LinksusInteractReplyCount entity){
		return linksusInteractReplyCountMapper.updateLinksusInteractReplyCount(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusInteractReplyCountById(Long pid){
		return linksusInteractReplyCountMapper.deleteLinksusInteractReplyCountById(pid);
	}

	/** ҵ�����������ѯ */
	public LinksusInteractReplyCount getLinksusInteractReplyCountByMap(Map map){
		return linksusInteractReplyCountMapper.getLinksusInteractReplyCountByMap(map);
	}

}