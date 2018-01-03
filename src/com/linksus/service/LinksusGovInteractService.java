package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusGovInteract;

public interface LinksusGovInteractService{

	/** 查询列表 */
	public List<LinksusGovInteract> getLinksusGovInteractList();

	/** 主键查询 */
	public LinksusGovInteract getLinksusGovInteractById(Long pid);

	/** 新增 */
	public Integer insertLinksusGovInteract(LinksusGovInteract entity);

	/** 更新 */
	public Integer updateLinksusGovInteract(LinksusGovInteract entity);

	/** 主键删除 */
	public Integer deleteLinksusGovInteractById(Long pid);

	/**comment_id和account_type 删除微博互动信息*/
	public Integer deleteLinksusGovInteractByMap(Map map);

	public List<LinksusGovInteract> getInteractGovListByMid(Map map);

	/**
	 * 更新回复状态
	 * @param govInteract
	 */
	public void updateSendReplyStatus(LinksusGovInteract govInteract);

	public void updateSendReplySucc(LinksusGovInteract govInteract);

	/** <!--取出回复消息的任务列表  -->*/
	public List<LinksusGovInteract> getLinksusGovInteractTaskList(LinksusGovInteract entity);

	public void saveLinksusGovInteract(List<LinksusGovInteract> list,String operType);
}
