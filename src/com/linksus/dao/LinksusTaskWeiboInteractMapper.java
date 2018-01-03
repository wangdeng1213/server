package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusTaskWeiboInteract;

public interface LinksusTaskWeiboInteractMapper{

	/** 列表查询 */
	public List<LinksusTaskWeiboInteract> getLinksusTaskWeiboInteractList();

	/** 主键查询 */
	public LinksusTaskWeiboInteract getLinksusTaskWeiboInteractById(Long pid);

	/** 新增 */
	public Integer insertLinksusTaskWeiboInteract(LinksusTaskWeiboInteract entity);

	/** 更新 */
	public Integer updateLinksusTaskWeiboInteract(LinksusTaskWeiboInteract entity);

	/** 主键删除 */
	public Integer deleteLinksusTaskWeiboInteractById(Long pid);

	public LinksusTaskWeiboInteract getMaxIdByAccountId(LinksusTaskWeiboInteract linksusTaskWeiboInteract);

	/** 设置当前账号翻页信息*/
	public Integer updateLinksusTaskWeiboInteractPageInfo(LinksusTaskWeiboInteract linksusTaskWeiboInteract);
}
