package com.linksus.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusInteractAttentionReplyMapper;
import com.linksus.entity.LinksusInteractAttentionReply;
import com.linksus.service.LinksusInteractAttentionReplyService;

@Service("linksusInteractAttentionReplyService")
public class LinksusInteractAttentionReplyServiceImpl implements LinksusInteractAttentionReplyService{

	@Autowired
	private LinksusInteractAttentionReplyMapper linksusInteractAttentionReplyMapper;

	/** 查询列表 */
	public List<LinksusInteractAttentionReply> getLinksusInteractAttentionReplyList(){
		return linksusInteractAttentionReplyMapper.getLinksusInteractAttentionReplyList();
	}

	/** 主键查询 */
	public LinksusInteractAttentionReply getLinksusInteractAttentionReplyById(Long pid){
		return linksusInteractAttentionReplyMapper.getLinksusInteractAttentionReplyById(pid);
	}

	/** 新增 */
	public Integer insertLinksusInteractAttentionReply(LinksusInteractAttentionReply entity){
		return linksusInteractAttentionReplyMapper.insertLinksusInteractAttentionReply(entity);
	}

	/** 更新 */
	public Integer updateLinksusInteractAttentionReply(LinksusInteractAttentionReply entity){
		return linksusInteractAttentionReplyMapper.updateLinksusInteractAttentionReply(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusInteractAttentionReplyById(Long pid){
		return linksusInteractAttentionReplyMapper.deleteLinksusInteractAttentionReplyById(pid);
	}

	/** 通过主键及平台类型查询 */
	public LinksusInteractAttentionReply getLinksusInteractAttentionReplyByIdAndType(Map map){
		return linksusInteractAttentionReplyMapper.getLinksusInteractAttentionReplyByIdAndType(map);
	}
}