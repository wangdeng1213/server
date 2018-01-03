package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusWx;

public interface LinksusWxMapper{

	/** 列表查询 */
	public List<LinksusWx> getLinksusWxList();

	/** 主键查询 */
	public LinksusWx getLinksusWxById(Long pid);

	/** 新增 */
	public Integer insertLinksusWx(LinksusWx entity);

	/** 更新 */
	public Integer updateLinksusWx(LinksusWx entity);

	/** 主键删除 */
	public Integer deleteLinksusWxById(Long pid);

	/** 查询所有状态为未执行状态的微信任务 */
	public List<LinksusWx> getWeiXinTaskList(LinksusWx entity);

	/** 修改任务状态 */
	public void updateWeiXinTaskList(LinksusWx entity);

	/** 获取微信群发信息用户token及信息 */
	public LinksusWx getLinksusWXUserAndInfo(Long pid);

	/**  修改微信发布结果*/
	public void updateWeiXinTaskErrmsg(LinksusWx entity);

	/**  通过微信群发信息表修改微信发布结果*/
	public void updateWeiXinTaskErrmsgByGroup(Long pid);

	/** 修改微信发布结果及状态*/
	public void updateWeiXinTaskstatusAndErrmsg(LinksusWx entity);

}
