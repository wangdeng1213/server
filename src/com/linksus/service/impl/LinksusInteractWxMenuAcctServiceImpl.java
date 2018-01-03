package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusInteractWxMenuAcctMapper;
import com.linksus.entity.LinksusInteractWxMenuAcct;
import com.linksus.service.LinksusInteractWxMenuAcctService;

@Service("linksusInteractWxMenuAcctService")
public class LinksusInteractWxMenuAcctServiceImpl implements LinksusInteractWxMenuAcctService{

	@Autowired
	private LinksusInteractWxMenuAcctMapper linksusInteractWxMenuAcctMapper;

	/** ��ѯ�б� */
	public List<LinksusInteractWxMenuAcct> getLinksusInteractWxMenuAcctList(){
		return linksusInteractWxMenuAcctMapper.getLinksusInteractWxMenuAcctList();
	}

	/** ������ѯ */
	public LinksusInteractWxMenuAcct getLinksusInteractWxMenuAcctById(Long pid){
		return linksusInteractWxMenuAcctMapper.getLinksusInteractWxMenuAcctById(pid);
	}

	/** ���� */
	public Integer insertLinksusInteractWxMenuAcct(LinksusInteractWxMenuAcct entity){
		return linksusInteractWxMenuAcctMapper.insertLinksusInteractWxMenuAcct(entity);
	}

	/** ���� */
	public Integer updateLinksusInteractWxMenuAcct(LinksusInteractWxMenuAcct entity){
		return linksusInteractWxMenuAcctMapper.updateLinksusInteractWxMenuAcct(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusInteractWxMenuAcctById(Long pid){
		return linksusInteractWxMenuAcctMapper.deleteLinksusInteractWxMenuAcctById(pid);
	}

	/** ����account_id��ѯ���в˵�������Ϣ */
	public LinksusInteractWxMenuAcct getLinksusInteractWxMenuAcctByAccountId(Long accountId){
		return linksusInteractWxMenuAcctMapper.getLinksusInteractWxMenuAcctByAccountId(accountId);
	}
}