package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusTaskAttentionUser;

public interface LinksusTaskAttentionUserService{

	/** 查询列表 */
	public List<LinksusTaskAttentionUser> getLinksusTaskAttentionUserList();

	/** 主键查询 */
	public LinksusTaskAttentionUser getLinksusTaskAttentionUserById(Long pid);

	/** 判断粉丝表中是否存在 */
	public Integer countLinksusTaskAttentionUser(LinksusTaskAttentionUser entity);

	/** 新增 */
	public Integer insertLinksusTaskAttentionUser(LinksusTaskAttentionUser entity);

	/** 更新 */
	public Integer updateLinksusTaskAttentionUser(LinksusTaskAttentionUser entity);

	/** 主键删除 */
	public Integer deleteLinksusTaskAttentionUserById(Long pid);

	/** 查询列表 */
	public List<LinksusTaskAttentionUser> getAllAttentionByUserList(LinksusTaskAttentionUser entity);
}
