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

	/** 查询列表 */
	public List<LinksusInteractWxMenuAcct> getLinksusInteractWxMenuAcctList(){
		return linksusInteractWxMenuAcctMapper.getLinksusInteractWxMenuAcctList();
	}

	/** 主键查询 */
	public LinksusInteractWxMenuAcct getLinksusInteractWxMenuAcctById(Long pid){
		return linksusInteractWxMenuAcctMapper.getLinksusInteractWxMenuAcctById(pid);
	}

	/** 新增 */
	public Integer insertLinksusInteractWxMenuAcct(LinksusInteractWxMenuAcct entity){
		return linksusInteractWxMenuAcctMapper.insertLinksusInteractWxMenuAcct(entity);
	}

	/** 更新 */
	public Integer updateLinksusInteractWxMenuAcct(LinksusInteractWxMenuAcct entity){
		return linksusInteractWxMenuAcctMapper.updateLinksusInteractWxMenuAcct(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusInteractWxMenuAcctById(Long pid){
		return linksusInteractWxMenuAcctMapper.deleteLinksusInteractWxMenuAcctById(pid);
	}

	/** 根据account_id查询所有菜单内容信息 */
	public LinksusInteractWxMenuAcct getLinksusInteractWxMenuAcctByAccountId(Long accountId){
		return linksusInteractWxMenuAcctMapper.getLinksusInteractWxMenuAcctByAccountId(accountId);
	}
}