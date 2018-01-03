package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusAppaccount;

/**
 * linksus_appaccount取得用户的token接口
 * @author  
 *
 */
public interface LinksusAppaccountService{

	public Map<String, LinksusAppaccount> getAccountTokenMap();

	/**获取平台账号列表用来处理互动数据*/
	public List<LinksusAppaccount> getLinksusAppaccountList(LinksusAppaccount linksusAppaccount);

	/**通过主键、合同开始结束日期、授权日期,取得当前账户信息*/
	public LinksusAppaccount getLinksusAppaccountTokenById(Long id);

	/** 更新机构账号基本信息任务列表*/
	public List<LinksusAppaccount> getInstitutionUserTaskList(String accountType);

	public List<LinksusAppaccount> getUserList(String account_type);

	public void updateLinksusAppaccount(LinksusAppaccount linksusAppaccount);

	/**通过institution_id、appid查询用户Token*/
	public LinksusAppaccount getLinksusAppaccountTokenByAppid(Map params);

	/**获取微信用户*/
	public List<LinksusAppaccount> getLinksusAppaccountWXUser(LinksusAppaccount wxAppaccount);

	/** 更新微信用户token */
	public void updateWXUserToken(LinksusAppaccount linksusAppaccount);

	/** 获取已授权微信用户 */
	public List<LinksusAppaccount> getAllLinksusAppaccountWXUser(LinksusAppaccount linksusAppaccount);

	/** 根据account_name获取微信账户Token */
	//public LinksusAppaccount getLinksusAppaccountWXUserByAccountName(LinksusAppaccount linksusAppaccount);
	/** 根据微信号查询账号信息*/
	public LinksusAppaccount getWxAppaccountByAccountName(String accountName);

	/** 关联rpsId 或者 name 和accountType查询appaccount表  */
	public LinksusAppaccount getAppaccountByIdOrName(Map paraMap);

	/** 通过主键查询用户 */
	public LinksusAppaccount getLinksusAppaccountById(Long id);

	/** 分页查询腾讯(私信/互动)已授权的账号及互动节点 */
	public List<LinksusAppaccount> getTencentInteractAppaccount(LinksusAppaccount linksusAppaccount);

	/** 查询新浪私信账号及最大消息ID */
	public List<LinksusAppaccount> getLinksusAppaccountListWithMsgId(LinksusAppaccount linksusAppaccount);

	/** 取得系统初始化app_key */
	public List getSystemDefaultAppKey();

	/** 取平台用户关注用户任务*/
	public List<LinksusAppaccount> getLinksusAppaccountAttentionList(LinksusAppaccount linksusAppaccount);

	/**  通过主键查询用户、微博账户表userId */
	public LinksusAppaccount getLinksusAppaccountAndUserIdById(Long id);

	/**
	 * 获取政务平台帐号
	 * @param linksusAppaccount
	 * @return
	 */
	public List<LinksusAppaccount> getLinksusOrgAppaccountList(LinksusAppaccount linksusAppaccount);

	/** 查询政务版新浪私信账号及最大消息ID */
	public List<LinksusAppaccount> getLinksusGovAppaccountListWithMsgId(LinksusAppaccount linksusAppaccount);

	/**   政务版——通过主键、授权日期,取得当前账户信息 */
	public LinksusAppaccount getGovLinksusAppaccountTokenById(Long id);

	/** 通过appid查询用户 */
	public LinksusAppaccount getAppaccountByAppid(String appid);
}
