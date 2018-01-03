package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusRelationUserAccount;

/**
 * 关系用户接口
 * @author wangdeng
 *
 */
public interface LinksusRelationUserAccountMapper{

	//获取用户关注的粉丝 或者 相互关注的粉丝微博信息
	public List<LinksusRelationUserAccount> getLinksysWeiboRelationByFlag(String accountType);

	public void updateRelationUserAccount(LinksusRelationUserAccount linksusRelationUserAccount);

	public Integer getCountLinksusRelationUserAccount(LinksusRelationUserAccount entity);

	//更新用户账户关系表互动次数
	public void updateLinksusRelationUserAccountNum(LinksusRelationUserAccount linksusRelationUserAccount);

	public List<LinksusRelationUserAccount> getLinksusRelationUserAccountList(
			LinksusRelationUserAccount linksusRelationUserAccount);

	/**插入新内容*/
	public void insertLinksusRelationUserAccount(LinksusRelationUserAccount linksusRelationUserAccount);

	/**更新内容*/
	public void updateLinksusRelationUserAccount(LinksusRelationUserAccount linksusRelationUserAccount);

	/** 判段用户关系 */
	public LinksusRelationUserAccount getUserAccountByAccountId(Map map);

	//根据条件取出用户信息
	public LinksusRelationUserAccount getRelationUser(Map map);

	public void deleteLinksusRelationUserAccount(Long pid);

	public void dealFlagRelation(Map map);

	//判断账号用户关系
	public LinksusRelationUserAccount getIsExsitWeiboUserAccount(Map map);

	//通过主键更新用户关系标志
	public void updateFlagRelationByPid(LinksusRelationUserAccount linksusRelationUserAccount);

	//获取用户关注的单个粉丝微博关系信息
	public LinksusRelationUserAccount getLinksusWeiboRelation(LinksusRelationUserAccount linksusRelationUserAccount);

	//插入微信新用户 
	public void insertWeiXineToLinksusRelationUserAccount(LinksusRelationUserAccount account);

	/** 根据account_id、account_type、user_id删除记录 */
	public void deleteLinksusRelationUserAccountByKey(Map map);

	//获取所有关系的账号
	public List<LinksusRelationUserAccount> getALLRelationUserAccountMap(Map map);
}
