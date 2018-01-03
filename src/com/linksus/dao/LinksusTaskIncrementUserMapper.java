package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusTaskIncrementUser;

public interface LinksusTaskIncrementUserMapper{

	/** 列表查询 */
	public List<LinksusTaskIncrementUser> getLinksusTaskIncrementUserList();

	/** 主键查询 */
	public LinksusTaskIncrementUser getTaskIncrementUserByAccountId(Long accountId);

	/** 新增 */
	public Integer insertLinksusTaskIncrementUser(LinksusTaskIncrementUser entity);

	/** 更新 */
	public Integer updateLinksusTaskIncrementUser(LinksusTaskIncrementUser entity);

	/** 更新 */
	public Integer updateLinksusTaskIncrementUserInfo(LinksusTaskIncrementUser entity);

	/** 主键删除 */
	public Integer deleteLinksusTaskIncrementUserById(Long pid);

	public Integer getCountByaccountId(Long pid);

	//查询账号的增量粉丝任务列表和账号的第三方id 
	public List<LinksusTaskIncrementUser> getIncrementalUserTaskList(LinksusTaskIncrementUser entity);

	//更新增量粉丝
	public Integer updateLinksusTaskIncrementFans(LinksusTaskIncrementUser entity);

	//更新增量关注
	public Integer updateLinksusTaskIncrementFollow(LinksusTaskIncrementUser entity);

}
