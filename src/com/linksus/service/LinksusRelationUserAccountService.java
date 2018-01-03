package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusRelationUserAccount;

/**
 * 账户用户关系表接口
 * @author wangdeng
 *
 */
public interface LinksusRelationUserAccountService{

	//获取用户关注的粉丝 或者 相互关注的粉丝微博信息
	public List<LinksusRelationUserAccount> getLinksysWeiboRelationByFlag(String accountType);

	//更新我的关注状态
	public void updateRelationUserAccount(LinksusRelationUserAccount linksusRelationUserAccount);

	public LinksusRelationUserAccount getUserAccountByAccountId(Map map);

	//根据userId 和  机构id 查询用户账号关系表中是否有关系
	public Integer getCountLinksusRelationUserAccount(LinksusRelationUserAccount entity);

	//获取用户关注的粉丝信息
	public List<LinksusRelationUserAccount> getLinksusRelationUserAccountList(
			LinksusRelationUserAccount linksusRelationUserAccount);

	/**更新内容*/
	//public void updateLinksusRelationUserAccount(LinksusRelationUserAccount linksusRelationUserAccount);
	/**插入新内容*/
	//public void insertLinksusRelationUserAccount(LinksusRelationUserAccount linksusRelationUserAccount);

	//public void saveLinksusRelationUserAccount(List UserAccountList,String operType);
	//更新用户账户关系表互动次数
	//public void  updateLinksusRelationUserAccountNum(LinksusRelationUserAccount linksusRelationUserAccount);
	public void updateLinksusRelationUserAccountNum(List list);

	public LinksusRelationUserAccount getRelationUser(Map map);

	public void deleteLinksusRelationUserAccount(Long id);

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
