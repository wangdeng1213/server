package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusInteractKeywordReplyAcctMapper;
import com.linksus.entity.LinksusInteractKeywordReplyAcct;
import com.linksus.service.LinksusInteractKeywordReplyAcctService;

@Service("linksusInteractKeywordReplyAcctService")
public class LinksusInteractKeywordReplyAcctServiceImpl implements LinksusInteractKeywordReplyAcctService{

	@Autowired
	private LinksusInteractKeywordReplyAcctMapper linksusInteractKeywordReplyAcctMapper;

	/** 查询列表 */
	public List<LinksusInteractKeywordReplyAcct> getLinksusInteractKeywordReplyAcctList(){
		return linksusInteractKeywordReplyAcctMapper.getLinksusInteractKeywordReplyAcctList();
	}

	/** 主键查询 */
	public LinksusInteractKeywordReplyAcct getLinksusInteractKeywordReplyAcctById(Long pid){
		return linksusInteractKeywordReplyAcctMapper.getLinksusInteractKeywordReplyAcctById(pid);
	}

	/** 新增 */
	public Integer insertLinksusInteractKeywordReplyAcct(LinksusInteractKeywordReplyAcct entity){
		return linksusInteractKeywordReplyAcctMapper.insertLinksusInteractKeywordReplyAcct(entity);
	}

	/** 更新 */
	public Integer updateLinksusInteractKeywordReplyAcct(LinksusInteractKeywordReplyAcct entity){
		return linksusInteractKeywordReplyAcctMapper.updateLinksusInteractKeywordReplyAcct(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusInteractKeywordReplyAcctById(Long pid){
		return linksusInteractKeywordReplyAcctMapper.deleteLinksusInteractKeywordReplyAcctById(pid);
	}

}