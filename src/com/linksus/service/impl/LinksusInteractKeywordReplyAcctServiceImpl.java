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

	/** ��ѯ�б� */
	public List<LinksusInteractKeywordReplyAcct> getLinksusInteractKeywordReplyAcctList(){
		return linksusInteractKeywordReplyAcctMapper.getLinksusInteractKeywordReplyAcctList();
	}

	/** ������ѯ */
	public LinksusInteractKeywordReplyAcct getLinksusInteractKeywordReplyAcctById(Long pid){
		return linksusInteractKeywordReplyAcctMapper.getLinksusInteractKeywordReplyAcctById(pid);
	}

	/** ���� */
	public Integer insertLinksusInteractKeywordReplyAcct(LinksusInteractKeywordReplyAcct entity){
		return linksusInteractKeywordReplyAcctMapper.insertLinksusInteractKeywordReplyAcct(entity);
	}

	/** ���� */
	public Integer updateLinksusInteractKeywordReplyAcct(LinksusInteractKeywordReplyAcct entity){
		return linksusInteractKeywordReplyAcctMapper.updateLinksusInteractKeywordReplyAcct(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusInteractKeywordReplyAcctById(Long pid){
		return linksusInteractKeywordReplyAcctMapper.deleteLinksusInteractKeywordReplyAcctById(pid);
	}

}