package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusRelationUserAccount;

/**
 * ��ϵ�û��ӿ�
 * @author wangdeng
 *
 */
public interface LinksusRelationUserAccountMapper{

	//��ȡ�û���ע�ķ�˿ ���� �໥��ע�ķ�˿΢����Ϣ
	public List<LinksusRelationUserAccount> getLinksysWeiboRelationByFlag(String accountType);

	public void updateRelationUserAccount(LinksusRelationUserAccount linksusRelationUserAccount);

	public Integer getCountLinksusRelationUserAccount(LinksusRelationUserAccount entity);

	//�����û��˻���ϵ��������
	public void updateLinksusRelationUserAccountNum(LinksusRelationUserAccount linksusRelationUserAccount);

	public List<LinksusRelationUserAccount> getLinksusRelationUserAccountList(
			LinksusRelationUserAccount linksusRelationUserAccount);

	/**����������*/
	public void insertLinksusRelationUserAccount(LinksusRelationUserAccount linksusRelationUserAccount);

	/**��������*/
	public void updateLinksusRelationUserAccount(LinksusRelationUserAccount linksusRelationUserAccount);

	/** �ж��û���ϵ */
	public LinksusRelationUserAccount getUserAccountByAccountId(Map map);

	//��������ȡ���û���Ϣ
	public LinksusRelationUserAccount getRelationUser(Map map);

	public void deleteLinksusRelationUserAccount(Long pid);

	public void dealFlagRelation(Map map);

	//�ж��˺��û���ϵ
	public LinksusRelationUserAccount getIsExsitWeiboUserAccount(Map map);

	//ͨ�����������û���ϵ��־
	public void updateFlagRelationByPid(LinksusRelationUserAccount linksusRelationUserAccount);

	//��ȡ�û���ע�ĵ�����˿΢����ϵ��Ϣ
	public LinksusRelationUserAccount getLinksusWeiboRelation(LinksusRelationUserAccount linksusRelationUserAccount);

	//����΢�����û� 
	public void insertWeiXineToLinksusRelationUserAccount(LinksusRelationUserAccount account);

	/** ����account_id��account_type��user_idɾ����¼ */
	public void deleteLinksusRelationUserAccountByKey(Map map);

	//��ȡ���й�ϵ���˺�
	public List<LinksusRelationUserAccount> getALLRelationUserAccountMap(Map map);
}
