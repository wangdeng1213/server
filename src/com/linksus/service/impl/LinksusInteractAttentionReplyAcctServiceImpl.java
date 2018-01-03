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

	/** ��ѯ�б� */
	public List<LinksusInteractAttentionReplyAcct> getLinksusInteractAttentionReplyAcctList(){
		return linksusInteractAttentionReplyAcctMapper.getLinksusInteractAttentionReplyAcctList();
	}

	/** ������ѯ */
	public LinksusInteractAttentionReplyAcct getLinksusInteractAttentionReplyAcctById(Long pid){
		return linksusInteractAttentionReplyAcctMapper.getLinksusInteractAttentionReplyAcctById(pid);
	}

	/** ���� */
	public Integer insertLinksusInteractAttentionReplyAcct(LinksusInteractAttentionReplyAcct entity){
		return linksusInteractAttentionReplyAcctMapper.insertLinksusInteractAttentionReplyAcct(entity);
	}

	/** ���� */
	public Integer updateLinksusInteractAttentionReplyAcct(LinksusInteractAttentionReplyAcct entity){
		return linksusInteractAttentionReplyAcctMapper.updateLinksusInteractAttentionReplyAcct(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusInteractAttentionReplyAcctById(Long pid){
		return linksusInteractAttentionReplyAcctMapper.deleteLinksusInteractAttentionReplyAcctById(pid);
	}

	/** accountId������ѯ */
	public LinksusInteractAttentionReplyAcct getLinksusInteractAttentionReplyAcctByAccountId(Long accountId){
		return linksusInteractAttentionReplyAcctMapper.getLinksusInteractAttentionReplyAcctByAccountId(accountId);
	}

}