package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusInteractAttentionReplyAcctMapper;
import com.linksus.entity.LinksusInteractAttentionReplyAcct;
import com.linksus.service.LinksusInteractAttentionReplyAcctService;

@Service("linksusInteractAttentionReplyAcctService")
public class LinksusInteractAttentionReplyAcctServiceImpl implements LinksusInteractAttentionReplyAcctService{

	@Autowired
	private LinksusInteractAttentionReplyAcctMapper linksusInteractAttentionReplyAcctMapper;

	/** 查询列表 */
	public List<LinksusInteractAttentionReplyAcct> getLinksusInteractAttentionReplyAcctList(){
		return linksusInteractAttentionReplyAcctMapper.getLinksusInteractAttentionReplyAcctList();
	}

	/** 主键查询 */
	public LinksusInteractAttentionReplyAcct getLinksusInteractAttentionReplyAcctById(Long pid){
		return linksusInteractAttentionReplyAcctMapper.getLinksusInteractAttentionReplyAcctById(pid);
	}

	/** 新增 */
	public Integer insertLinksusInteractAttentionReplyAcct(LinksusInteractAttentionReplyAcct entity){
		return linksusInteractAttentionReplyAcctMapper.insertLinksusInteractAttentionReplyAcct(entity);
	}

	/** 更新 */
	public Integer updateLinksusInteractAttentionReplyAcct(LinksusInteractAttentionReplyAcct entity){
		return linksusInteractAttentionReplyAcctMapper.updateLinksusInteractAttentionReplyAcct(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusInteractAttentionReplyAcctById(Long pid){
		return linksusInteractAttentionReplyAcctMapper.deleteLinksusInteractAttentionReplyAcctById(pid);
	}

	/** accountId主键查询 */
	public LinksusInteractAttentionReplyAcct getLinksusInteractAttentionReplyAcctByAccountId(Long accountId){
		return linksusInteractAttentionReplyAcctMapper.getLinksusInteractAttentionReplyAcctByAccountId(accountId);
	}

}