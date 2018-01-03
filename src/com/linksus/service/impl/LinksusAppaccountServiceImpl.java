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

	/**��ȡƽ̨�˺��б���������������*/
	public List<LinksusAppaccount> getLinksusAppaccountList(LinksusAppaccount linksusAppaccount){
		List<LinksusAppaccount> accountList = LinksusAppaccountMapper.getLinksusAppaccountList(linksusAppaccount);
		return accountList;
	}

	@Override
	public List<LinksusAppaccount> getUserList(String account_type){
		List<LinksusAppaccount> accountList = LinksusAppaccountMapper.getUserList(account_type);
		return accountList;
	}

	/** ���»����˺Ż�����Ϣ�����б�*/
	public List<LinksusAppaccount> getInstitutionUserTaskList(String accountType){
		List<LinksusAppaccount> appAccountList = LinksusAppaccountMapper.getInstitutionUserTaskList(accountType);
		return appAccountList;
	}

	public void updateLinksusAppaccount(LinksusAppaccount linksusAppaccount){
		LinksusAppaccountMapper.updateLinksusAppaccount(linksusAppaccount);
	}

	/**ͨ����������ͬ��ʼ�������ڡ���Ȩ����,ȡ�õ�ǰ�˻���Ϣ*/
	public LinksusAppaccount getLinksusAppaccountTokenById(Long id){
		return LinksusAppaccountMapper.getLinksusAppaccountTokenById(id);
	}

	/**ͨ��institution_id��appid��ѯ�û�Token*/
	public LinksusAppaccount getLinksusAppaccountTokenByAppid(Map params){
		return LinksusAppaccountMapper.getLinksusAppaccountTokenByAppid(params);
	}

	/**��ȡ΢���û�*/
	public List<LinksusAppaccount> getLinksusAppaccountWXUser(LinksusAppaccount wxAppaccount){
		return LinksusAppaccountMapper.getLinksusAppaccountWXUser(wxAppaccount);
	}

	/** ����΢���û�token */
	public void updateWXUserToken(LinksusAppaccount linksusAppaccount){
		LinksusAppaccountMapper.updateWXUserToken(linksusAppaccount);
	}

	/** ��ȡ����Ȩ΢���û� */
	public List<LinksusAppaccount> getAllLinksusAppaccountWXUser(LinksusAppaccount linksusAppaccount){
		return LinksusAppaccountMapper.getAllLinksusAppaccountWXUser(linksusAppaccount);
	}

	/** ����account_name��ȡ΢���˻�Token */
	/*
	 * public LinksusAppaccount
	 * getLinksusAppaccountWXUserByAccountName(LinksusAppaccount
	 * linksusAppaccount){ return
	 * LinksusAppaccountMapper.getLinksusAppaccountWXUserByAccountName
	 * (linksusAppaccount); }
	 */

	/** ����account_name��ȡ΢���˻���Ϣ */
	public LinksusAppaccount getWxAppaccountByAccountName(String accountName){
		return LinksusAppaccountMapper.getWxAppaccountByAccountName(accountName);
	}

	/** ����rpsId ���� name ��accountType��ѯappaccount��  */

	public LinksusAppaccount getAppaccountByIdOrName(Map paraMap){
		return LinksusAppaccountMapper.getAppaccountByIdOrName(paraMap);

	}

	/** ͨ��������ѯ�û� */
	public LinksusAppaccount getLinksusAppaccountById(Long id){
		return LinksusAppaccountMapper.getLinksusAppaccountById(id);
	}

	/** ��ѯ�����û� */
	public List<LinksusAppaccount> getALLLinksusAppaccountList(Map map){
		return LinksusAppaccountMapper.getALLLinksusAppaccountList(map);
	}

	/** ��ҳ��ѯ��Ѷ(˽��/����)����Ȩ���˺ż������ڵ� */
	public List<LinksusAppaccount> getTencentInteractAppaccount(LinksusAppaccount linksusAppaccount){
		return LinksusAppaccountMapper.getTencentInteractAppaccount(linksusAppaccount);
	}

	/** ȡ��ϵͳ��ʼ��app_key */
	public List getSystemDefaultAppKey(){
		return LinksusAppaccountMapper.getSystemDefaultAppKey();
	}

	/** ��ѯ����˽���˺ż������ϢID */
	public List<LinksusAppaccount> getLinksusAppaccountListWithMsgId(LinksusAppaccount linksusAppaccount){
		return LinksusAppaccountMapper.getLinksusAppaccountListWithMsgId(linksusAppaccount);
	}

	/** ȡƽ̨�û���ע�û�����*/
	public List<LinksusAppaccount> getLinksusAppaccountAttentionList(LinksusAppaccount linksusAppaccount){
		return LinksusAppaccountMapper.getLinksusAppaccountAttentionList(linksusAppaccount);
	}

	/**  ͨ��������ѯ�û���΢���˻���userId */
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

	/**   ����桪��ͨ����������Ȩ����,ȡ�õ�ǰ�˻���Ϣ */
	public LinksusAppaccount getGovLinksusAppaccountTokenById(Long id){
		return LinksusAppaccountMapper.getGovLinksusAppaccountTokenById(id);
	}

	/** ͨ��appid��ѯ�û� */
	public LinksusAppaccount getAppaccountByAppid(String appid){
		return LinksusAppaccountMapper.getAppaccountByAppid(appid);
	}
}
