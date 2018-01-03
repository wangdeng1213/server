package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusRelationPerson;

public interface LinksusRelationPersonMapper{

	/**分页查询无头像用户列表 */
	public List<LinksusRelationPerson> getLinksusRelationPersonAddImageList(LinksusRelationPerson person);

	/**新增人员头像路径 */
	public void updateLinksusRelationPersonHeadUrl(LinksusRelationPerson person);

	/** 列表查询 */
	public List<LinksusRelationPerson> getLinksusRelationPersonList();

	/** 主键查询 */
	public LinksusRelationPerson getLinksusRelationPersonById(Long pid);

	/** 新增 */
	public Integer insertLinksusRelationPerson(LinksusRelationPerson entity);

	/** 更新 */
	public Integer updateLinksusRelationPerson(LinksusRelationPerson entity);

	/** 主键删除 */
	public Integer deleteLinksusRelationPersonById(Long pid);

	/** 插入人员主表微信用户 */
	public void insertWeiXinUser(LinksusRelationPerson entity);

	/** 更新人员表基本信息  */
	public void updateLinksusRelationPersonInfo(LinksusRelationPerson person);

	/** 分页查询用户列表  */
	public List<LinksusRelationPerson> getLinksusRelationPersons(LinksusRelationPerson person);

}
