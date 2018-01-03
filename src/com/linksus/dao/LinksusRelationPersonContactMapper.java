package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusRelationPersonContact;

public interface LinksusRelationPersonContactMapper{

	/** 列表查询 */
	public List<LinksusRelationPersonContact> getLinksusRelationPersonContactList();

	/** 主键查询 */
	public LinksusRelationPersonContact getLinksusRelationPersonContactById(Long pid);

	/** 新增 */
	public Integer insertLinksusRelationPersonContact(LinksusRelationPersonContact entity);

	/** 更新 */
	public Integer updateLinksusRelationPersonContact(LinksusRelationPersonContact entity);

	/** 主键删除 */
	public Integer deleteLinksusRelationPersonContactById(Long pid);

	/** 删除personId的数据 */
	public Integer deleteByPersonId(Long personId);

	/** 通过person_id查询需要发短信用户信息 */
	public List<LinksusRelationPersonContact> getPersonContactMobileListByPersonId(long personId);

	/** 通过person_id查询需要发邮件用户信息 */
	public List<LinksusRelationPersonContact> getPersonContactEmailListByPersonId(long personId);

}
