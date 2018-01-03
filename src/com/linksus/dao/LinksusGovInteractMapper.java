package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusGovInteract;

public interface LinksusGovInteractMapper{

	/** 列表查询 */
	public List<LinksusGovInteract> getLinksusGovInteractList();

	/** 主键查询 */
	public LinksusGovInteract getLinksusGovInteractById(Long pid);

	/** 新增 */
	public Integer insertLinksusGovInteract(LinksusGovInteract entity);

	/** 更新 */
	public Integer updateLinksusGovInteract(LinksusGovInteract entity);

	/** 主键删除 */
	public Integer deleteLinksusGovInteractById(Long pid);

	public Integer deleteLinksusGovInteractByMap(Map map);

	public List<LinksusGovInteract> getInteractGovListByMid(Map map);

	public void updateSendReplyStatus(LinksusGovInteract govInteract);

	public void updateSendReplySucc(LinksusGovInteract govInteract);

	public List<LinksusGovInteract> getLinksusGovInteractTaskList(LinksusGovInteract entity);

}
