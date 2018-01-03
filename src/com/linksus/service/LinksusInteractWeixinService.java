package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusInteractWeixin;

public interface LinksusInteractWeixinService{

	/** 查询列表 */
	public List<LinksusInteractWeixin> getLinksusInteractWeixinList();

	/** 主键查询 */
	public LinksusInteractWeixin getLinksusInteractWeixinById(Long pid);

	/** 新增 */
	public Integer insertLinksusInteractWeixin(LinksusInteractWeixin entity);

	/** 更新 */
	public Integer updateLinksusInteractWeixin(LinksusInteractWeixin entity);

	/** 主键删除 */
	public Integer deleteLinksusInteractWeixinById(Long pid);

	/** 多参数查询 */
	public LinksusInteractWeixin getLinksusInteractWeixinByMap(Map map);

	/** 查询即时回复微信任务列表 */
	public List<LinksusInteractWeixin> getTaskWeixinAtImmediate(LinksusInteractWeixin entity);

	/** 查询定时回复微信任务列表 */
	public List<LinksusInteractWeixin> getTaskWeixinAtRegularTime(LinksusInteractWeixin entity);

	public List getInteractWeixinListByIds(List weixinMsgs);

	/**  修改发布状态、互动时间*/
	public Integer updateLinksusInteractWeixinStatus(LinksusInteractWeixin entity);
}
