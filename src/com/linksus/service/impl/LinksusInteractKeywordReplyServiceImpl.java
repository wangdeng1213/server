package com.linksus.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusInteractKeywordReplyMapper;
import com.linksus.entity.LinksusInteractKeywordReply;
import com.linksus.service.LinksusInteractKeywordReplyService;

@Service("linksusInteractKeywordReplyService")
public class LinksusInteractKeywordReplyServiceImpl implements LinksusInteractKeywordReplyService{

	@Autowired
	private LinksusInteractKeywordReplyMapper linksusInteractKeywordReplyMapper;

	/** 查询列表 */
	public List<LinksusInteractKeywordReply> getLinksusInteractKeywordReplyList(){
		return linksusInteractKeywordReplyMapper.getLinksusInteractKeywordReplyList();
	}

	/** 主键查询 */
	public LinksusInteractKeywordReply getLinksusInteractKeywordReplyById(Long pid){
		return linksusInteractKeywordReplyMapper.getLinksusInteractKeywordReplyById(pid);
	}

	/** 新增 */
	public Integer insertLinksusInteractKeywordReply(LinksusInteractKeywordReply entity){
		return linksusInteractKeywordReplyMapper.insertLinksusInteractKeywordReply(entity);
	}

	/** 更新 */
	public Integer updateLinksusInteractKeywordReply(LinksusInteractKeywordReply entity){
		return linksusInteractKeywordReplyMapper.updateLinksusInteractKeywordReply(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusInteractKeywordReplyById(Long pid){
		return linksusInteractKeywordReplyMapper.deleteLinksusInteractKeywordReplyById(pid);
	}

	/** 通过关联查询，查询账户下所有关键字内容 */
	public LinksusInteractKeywordReply getAllKeywordsByAccountId(Map accountId){
		return linksusInteractKeywordReplyMapper.getAllKeywordsByAccountId(accountId);
	}
}