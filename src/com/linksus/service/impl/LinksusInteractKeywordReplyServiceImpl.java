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

	/** ��ѯ�б� */
	public List<LinksusInteractKeywordReply> getLinksusInteractKeywordReplyList(){
		return linksusInteractKeywordReplyMapper.getLinksusInteractKeywordReplyList();
	}

	/** ������ѯ */
	public LinksusInteractKeywordReply getLinksusInteractKeywordReplyById(Long pid){
		return linksusInteractKeywordReplyMapper.getLinksusInteractKeywordReplyById(pid);
	}

	/** ���� */
	public Integer insertLinksusInteractKeywordReply(LinksusInteractKeywordReply entity){
		return linksusInteractKeywordReplyMapper.insertLinksusInteractKeywordReply(entity);
	}

	/** ���� */
	public Integer updateLinksusInteractKeywordReply(LinksusInteractKeywordReply entity){
		return linksusInteractKeywordReplyMapper.updateLinksusInteractKeywordReply(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusInteractKeywordReplyById(Long pid){
		return linksusInteractKeywordReplyMapper.deleteLinksusInteractKeywordReplyById(pid);
	}

	/** ͨ��������ѯ����ѯ�˻������йؼ������� */
	public LinksusInteractKeywordReply getAllKeywordsByAccountId(Map accountId){
		return linksusInteractKeywordReplyMapper.getAllKeywordsByAccountId(accountId);
	}
}