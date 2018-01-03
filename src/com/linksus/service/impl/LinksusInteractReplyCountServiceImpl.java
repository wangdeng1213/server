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

	/** 查询列表 */
	public List<LinksusInteractReplyCount> getLinksusInteractReplyCountList(){
		return linksusInteractReplyCountMapper.getLinksusInteractReplyCountList();
	}

	/** 主键查询 */
	public LinksusInteractReplyCount getLinksusInteractReplyCountById(Long pid){
		return linksusInteractReplyCountMapper.getLinksusInteractReplyCountById(pid);
	}

	/** 新增 */
	public Integer insertLinksusInteractReplyCount(LinksusInteractReplyCount entity){
		return linksusInteractReplyCountMapper.insertLinksusInteractReplyCount(entity);
	}

	/** 更新 */
	public Integer updateLinksusInteractReplyCount(LinksusInteractReplyCount entity){
		return linksusInteractReplyCountMapper.updateLinksusInteractReplyCount(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusInteractReplyCountById(Long pid){
		return linksusInteractReplyCountMapper.deleteLinksusInteractReplyCountById(pid);
	}

	/** 业务组合主键查询 */
	public LinksusInteractReplyCount getLinksusInteractReplyCountByMap(Map map){
		return linksusInteractReplyCountMapper.getLinksusInteractReplyCountByMap(map);
	}

}