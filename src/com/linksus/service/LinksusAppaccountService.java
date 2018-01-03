package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusAppaccount;

/**
 * linksus_appaccountȡ���û���token�ӿ�
 * @author  
 *
 */
public interface LinksusAppaccountService{

	public Map<String, LinksusAppaccount> getAccountTokenMap();

	/**��ȡƽ̨�˺��б���������������*/
	public List<LinksusAppaccount> getLinksusAppaccountList(LinksusAppaccount linksusAppaccount);

	/**ͨ����������ͬ��ʼ�������ڡ���Ȩ����,ȡ�õ�ǰ�˻���Ϣ*/
	public LinksusAppaccount getLinksusAppaccountTokenById(Long id);

	/** ���»����˺Ż�����Ϣ�����б�*/
	public List<LinksusAppaccount> getInstitutionUserTaskList(String accountType);

	public List<LinksusAppaccount> getUserList(String account_type);

	public void updateLinksusAppaccount(LinksusAppaccount linksusAppaccount);

	/**ͨ��institution_id��appid��ѯ�û�Token*/
	public LinksusAppaccount getLinksusAppaccountTokenByAppid(Map params);

	/**��ȡ΢���û�*/
	public List<LinksusAppaccount> getLinksusAppaccountWXUser(LinksusAppaccount wxAppaccount);

	/** ����΢���û�token */
	public void updateWXUserToken(LinksusAppaccount linksusAppaccount);

	/** ��ȡ����Ȩ΢���û� */
	public List<LinksusAppaccount> getAllLinksusAppaccountWXUser(LinksusAppaccount linksusAppaccount);

	/** ����account_name��ȡ΢���˻�Token */
	//public LinksusAppaccount getLinksusAppaccountWXUserByAccountName(LinksusAppaccount linksusAppaccount);
	/** ����΢�źŲ�ѯ�˺���Ϣ*/
	public LinksusAppaccount getWxAppaccountByAccountName(String accountName);

	/** ����rpsId ���� name ��accountType��ѯappaccount��  */
	public LinksusAppaccount getAppaccountByIdOrName(Map paraMap);

	/** ͨ��������ѯ�û� */
	public LinksusAppaccount getLinksusAppaccountById(Long id);

	/** ��ҳ��ѯ��Ѷ(˽��/����)����Ȩ���˺ż������ڵ� */
	public List<LinksusAppaccount> getTencentInteractAppaccount(LinksusAppaccount linksusAppaccount);

	/** ��ѯ����˽���˺ż������ϢID */
	public List<LinksusAppaccount> getLinksusAppaccountListWithMsgId(LinksusAppaccount linksusAppaccount);

	/** ȡ��ϵͳ��ʼ��app_key */
	public List getSystemDefaultAppKey();

	/** ȡƽ̨�û���ע�û�����*/
	public List<LinksusAppaccount> getLinksusAppaccountAttentionList(LinksusAppaccount linksusAppaccount);

	/**  ͨ��������ѯ�û���΢���˻���userId */
	public LinksusAppaccount getLinksusAppaccountAndUserIdById(Long id);

	/**
	 * ��ȡ����ƽ̨�ʺ�
	 * @param linksusAppaccount
	 * @return
	 */
	public List<LinksusAppaccount> getLinksusOrgAppaccountList(LinksusAppaccount linksusAppaccount);

	/** ��ѯ���������˽���˺ż������ϢID */
	public List<LinksusAppaccount> getLinksusGovAppaccountListWithMsgId(LinksusAppaccount linksusAppaccount);

	/**   ����桪��ͨ����������Ȩ����,ȡ�õ�ǰ�˻���Ϣ */
	public LinksusAppaccount getGovLinksusAppaccountTokenById(Long id);

	/** ͨ��appid��ѯ�û� */
	public LinksusAppaccount getAppaccountByAppid(String appid);
}
