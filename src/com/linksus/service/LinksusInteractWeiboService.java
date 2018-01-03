package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusInteractWeibo;

public interface LinksusInteractWeiboService{

	/** 查询列表 */
	public List<LinksusInteractWeibo> getLinksusInteractWeiboList();

	/** 主键查询 */
	public LinksusInteractWeibo getLinksusInteractWeiboById(Long pid);

	/** 新增 */
	public Integer insertLinksusInteractWeibo(LinksusInteractWeibo entity);

	/** 更新 */
	//public Integer updateLinksusInteractWeibo(LinksusInteractWeibo entity);

	/** 主键删除 */
	public Integer deleteLinksusInteractWeiboById(Long pid);

	/** <!--取出回复消息的任务列表  -->*/
	public List<LinksusInteractWeibo> getLinksusInteractWeiboTaskList(LinksusInteractWeibo entity);

	public void updateSendReplyStatus(LinksusInteractWeibo entity);

	//更新返回的 信息
	public void updateSendReplySucc(LinksusInteractWeibo entity);

	/** <!--取出回复消息的定时发布任务列表  -->*/
	//public List<LinksusInteractWeibo> getWeiboReplyPrepare(LinksusInteractWeibo linksusWeibo);
	/**comment_id和account_type 删除*/
	public Integer deleteLinksusInteractWeiboByMap(Map map);

	/**查询微博互动记录是否存在*/
	public LinksusInteractWeibo getInteractWeiboIsExists(Map map);

	/** 根据mid查询评论列表 */
	public List getInteractWeiboListByMid(Map map);

	/** 根据主键查询需要回复评论的微博*/
	public LinksusInteractWeibo getReplyWeiboById(Long pid);

	public List getInteractWeiboListByIds(List weiboMsgs);
}
