package com.linksus.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusAppaccountMapper;
import com.linksus.entity.LinksusAppaccount;
import com.linksus.service.LinksusAppaccountService;

@Service("linksusAppaccountService")
public class LinksusAppaccountServiceImpl implements LinksusAppaccountService{

	@Autowired
	private LinksusAppaccountMapper LinksusAppaccountMapper;

	@Override
	public Map<String, LinksusAppaccount> getAccountTokenMap(){
		Map<String, LinksusAppaccount> map = new HashMap<String, LinksusAppaccount>();
		List<LinksusAppaccount> accountList = LinksusAppaccountMapper.getLinksusAppaccountToken(new Integer(1));
		if(accountList.size() > 0){
			map.put("1", accountList.get(0));
		}
		accountList = LinksusAppaccountMapper.getLinksusAppaccountToken(new Integer(2));
		if(accountList.size() > 0){
			map.put("2", accountList.get(0));
		}
		return map;
	}

	/**获取平台账号列表用来处理互动数据*/
	public List<LinksusAppaccount> getLinksusAppaccountList(LinksusAppaccount linksusAppaccount){
		List<LinksusAppaccount> accountList = LinksusAppaccountMapper.getLinksusAppaccountList(linksusAppaccount);
		return accountList;
	}

	@Override
	public List<LinksusAppaccount> getUserList(String account_type){
		List<LinksusAppaccount> accountList = LinksusAppaccountMapper.getUserList(account_type);
		return accountList;
	}

	/** 更新机构账号基本信息任务列表*/
	public List<LinksusAppaccount> getInstitutionUserTaskList(String accountType){
		List<LinksusAppaccount> appAccountList = LinksusAppaccountMapper.getInstitutionUserTaskList(accountType);
		return appAccountList;
	}

	public void updateLinksusAppaccount(LinksusAppaccount linksusAppaccount){
		LinksusAppaccountMapper.updateLinksusAppaccount(linksusAppaccount);
	}

	/**通过主键、合同开始结束日期、授权日期,取得当前账户信息*/
	public LinksusAppaccount getLinksusAppaccountTokenById(Long id){
		return LinksusAppaccountMapper.getLinksusAppaccountTokenById(id);
	}

	/**通过institution_id、appid查询用户Token*/
	public LinksusAppaccount getLinksusAppaccountTokenByAppid(Map params){
		return LinksusAppaccountMapper.getLinksusAppaccountTokenByAppid(params);
	}

	/**获取微信用户*/
	public List<LinksusAppaccount> getLinksusAppaccountWXUser(LinksusAppaccount wxAppaccount){
		return LinksusAppaccountMapper.getLinksusAppaccountWXUser(wxAppaccount);
	}

	/** 更新微信用户token */
	public void updateWXUserToken(LinksusAppaccount linksusAppaccount){
		LinksusAppaccountMapper.updateWXUserToken(linksusAppaccount);
	}

	/** 获取已授权微信用户 */
	public List<LinksusAppaccount> getAllLinksusAppaccountWXUser(LinksusAppaccount linksusAppaccount){
		return LinksusAppaccountMapper.getAllLinksusAppaccountWXUser(linksusAppaccount);
	}

	/** 根据account_name获取微信账户Token */
	/*
	 * public LinksusAppaccount
	 * getLinksusAppaccountWXUserByAccountName(LinksusAppaccount
	 * linksusAppaccount){ return
	 * LinksusAppaccountMapper.getLinksusAppaccountWXUserByAccountName
	 * (linksusAppaccount); }
	 */

	/** 根据account_name获取微信账户信息 */
	public LinksusAppaccount getWxAppaccountByAccountName(String accountName){
		return LinksusAppaccountMapper.getWxAppaccountByAccountName(accountName);
	}

	/** 关联rpsId 或者 name 和accountType查询appaccount表  */

	public LinksusAppaccount getAppaccountByIdOrName(Map paraMap){
		return LinksusAppaccountMapper.getAppaccountByIdOrName(paraMap);

	}

	/** 通过主键查询用户 */
	public LinksusAppaccount getLinksusAppaccountById(Long id){
		return LinksusAppaccountMapper.getLinksusAppaccountById(id);
	}

	/** 查询所有用户 */
	public List<LinksusAppaccount> getALLLinksusAppaccountList(Map map){
		return LinksusAppaccountMapper.getALLLinksusAppaccountList(map);
	}

	/** 分页查询腾讯(私信/互动)已授权的账号及互动节点 */
	public List<LinksusAppaccount> getTencentInteractAppaccount(LinksusAppaccount linksusAppaccount){
		return LinksusAppaccountMapper.getTencentInteractAppaccount(linksusAppaccount);
	}

	/** 取得系统初始化app_key */
	public List getSystemDefaultAppKey(){
		return LinksusAppaccountMapper.getSystemDefaultAppKey();
	}

	/** 查询新浪私信账号及最大消息ID */
	public List<LinksusAppaccount> getLinksusAppaccountListWithMsgId(LinksusAppaccount linksusAppaccount){
		return LinksusAppaccountMapper.getLinksusAppaccountListWithMsgId(linksusAppaccount);
	}

	/** 取平台用户关注用户任务*/
	public List<LinksusAppaccount> getLinksusAppaccountAttentionList(LinksusAppaccount linksusAppaccount){
		return LinksusAppaccountMapper.getLinksusAppaccountAttentionList(linksusAppaccount);
	}

	/**  通过主键查询用户、微博账户表userId */
	public LinksusAppaccount getLinksusAppaccountAndUserIdById(Long id){
		return LinksusAppaccountMapper.getLinksusAppaccountAndUserIdById(id);
	}

	@Override
	public List<LinksusAppaccount> getLinksusOrgAppaccountList(LinksusAppaccount linksusAppaccount){
		return LinksusAppaccountMapper.getLinksusOrgAppaccountList(linksusAppaccount);
	}

	@Override
	public List<LinksusAppaccount> getLinksusGovAppaccountListWithMsgId(LinksusAppaccount linksusAppaccount){
		return LinksusAppaccountMapper.getLinksusGovAppaccountListWithMsgId(linksusAppaccount);
	}

	/**   政务版——通过主键、授权日期,取得当前账户信息 */
	public LinksusAppaccount getGovLinksusAppaccountTokenById(Long id){
		return LinksusAppaccountMapper.getGovLinksusAppaccountTokenById(id);
	}

	/** 通过appid查询用户 */
	public LinksusAppaccount getAppaccountByAppid(String appid){
		return LinksusAppaccountMapper.getAppaccountByAppid(appid);
	}
}
