package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusTaskTotalUser;

public interface LinksusTaskTotalUserMapper{

	/** 列表查询 */
	public List<LinksusTaskTotalUser> getLinksusTaskTotalUserList();

	/** 主键查询 */
	public LinksusTaskTotalUser getLinksusTaskTotalUserById(LinksusTaskTotalUser totalUser);

	/** 新增 */
	public Integer insertLinksusTaskTotalUser(LinksusTaskTotalUser entity);

	/** 更新 */
	public Integer updateLinksusTaskTotalUser(LinksusTaskTotalUser entity);

	/** 主键删除 */
	public Integer deleteLinksusTaskTotalUserById(Long pid);

	/** 分页获取根据类型未执行全量任务表*/
	public List<LinksusTaskTotalUser> getLinksusTaskTotalUsersByType(LinksusTaskTotalUser totalUser);

	/** 更新任务状态*/
	public Integer updateLinksusTaskTotalUsersStatus(LinksusTaskTotalUser totalUser);

	/** 更新最后一条用户id */
	public Integer updateLinksusTaskTotalUsersCursor(LinksusTaskTotalUser totalUser);

}
