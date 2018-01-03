package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusAppaccount;

public interface LinksusAppaccountMapper{

	public List<LinksusAppaccount> getLinksusAppaccountToken(Integer type);

	public List<LinksusAppaccount> getUserList(String account_type);

	/**��ȡƽ̨�˺��б���������������*/
	public List<LinksusAppaccount> getLinksusAppaccountList(LinksusAppaccount linksusAppaccount);

	/**ͨ����������ͬ��ʼ�������ڡ���Ȩ����,ȡ�õ�ǰ�˻���Ϣ*/
	public LinksusAppaccount getLinksusAppaccountTokenById(Long id);

	/** ���»����˺Ż�����Ϣ�����б�*/
	public List<LinksusAppaccount> getInstitutionUserTaskList(String accountType);

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

	public LinksusAppaccount getWxAppaccountByAccountName(String accountName);

	/** ����rpsId ���� name ��accountType��ѯappaccount��  */

	public LinksusAppaccount getAppaccountByIdOrName(Map paraMap);

	/** ͨ��������ѯ�û� */
	public LinksusAppaccount getLinksusAppaccountById(Long id);

	/** ��ѯ�����û� */
	public List<LinksusAppaccount> getALLLinksusAppaccountList(Map map);

	/** ��ҳ��ѯ��Ѷ(˽��/����)����Ȩ���˺ż������ڵ� */
	public List<LinksusAppaccount> getTencentInteractAppaccount(LinksusAppaccount linksusAppaccount);

	/** ȡ��ϵͳ��ʼ��app_key */
	public List getSystemDefaultAppKey();

	/** ��ѯ����˽���˺ż������ϢID */
	public List<LinksusAppaccount> getLinksusAppaccountListWithMsgId(LinksusAppaccount linksusAppaccount);

	/** ��ѯ���������˽���˺ż������ϢID */
	public List<LinksusAppaccount> getLinksusGovAppaccountListWithMsgId(LinksusAppaccount linksusAppaccount);

	/** ȡƽ̨�û���ע�û�����*/
	public List<LinksusAppaccount> getLinksusAppaccountAttentionList(LinksusAppaccount linksusAppaccount);

	/**  ͨ��������ѯ�û���΢���˻���userId */
	public LinksusAppaccount getLinksusAppaccountAndUserIdById(Long id);

	public List<LinksusAppaccount> getLinksusOrgAppaccountList(LinksusAppaccount linksusAppaccount);

	/**   ����桪��ͨ����������Ȩ����,ȡ�õ�ǰ�˻���Ϣ */
	public LinksusAppaccount getGovLinksusAppaccountTokenById(Long id);

	/** ͨ��appid��ѯ�û� */
	public LinksusAppaccount getAppaccountByAppid(String appid);
}