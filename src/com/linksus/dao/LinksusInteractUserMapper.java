package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusInteractUser;

public interface LinksusInteractUserMapper{

	/** 列表查询 */
	public List<LinksusInteractUser> getLinksusInteractUserList();

	/** 主键查询 */
	public LinksusInteractUser getLinksusInteractUserById(Long pid);

	/** 新增 */
	public Integer insertLinksusInteractUser(LinksusInteractUser entity);

	/** 更新 */
	public Integer updateLinksusInteractUser(LinksusInteractUser entity);

	/** 主键删除 */
	public Integer deleteLinksusInteractUserById(Long pid);

	/**检查互动用户 是否 存在 */
	public LinksusInteractUser checkUserDataIsExsit(LinksusInteractUser entity);

	public List getInteractUserImageList(Map tempMap);

}
