package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusInteractWxMenuAcct;

public interface LinksusInteractWxMenuAcctService{

	/** 查询列表 */
	public List<LinksusInteractWxMenuAcct> getLinksusInteractWxMenuAcctList();

	/** 主键查询 */
	public LinksusInteractWxMenuAcct getLinksusInteractWxMenuAcctById(Long pid);

	/** 新增 */
	public Integer insertLinksusInteractWxMenuAcct(LinksusInteractWxMenuAcct entity);

	/** 更新 */
	public Integer updateLinksusInteractWxMenuAcct(LinksusInteractWxMenuAcct entity);

	/** 主键删除 */
	public Integer deleteLinksusInteractWxMenuAcctById(Long pid);

	/** 根据account_id查询所有菜单内容信息 */
	public LinksusInteractWxMenuAcct getLinksusInteractWxMenuAcctByAccountId(Long accountId);

}
