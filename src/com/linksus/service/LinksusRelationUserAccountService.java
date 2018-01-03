package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusRelationUserAccount;

/**
 * �˻��û���ϵ��ӿ�
 * @author wangdeng
 *
 */
public interface LinksusRelationUserAccountService{

	//��ȡ�û���ע�ķ�˿ ���� �໥��ע�ķ�˿΢����Ϣ
	public List<LinksusRelationUserAccount> getLinksysWeiboRelationByFlag(String accountType);

	//�����ҵĹ�ע״̬
	public void updateRelationUserAccount(LinksusRelationUserAccount linksusRelationUserAccount);

	public LinksusRelationUserAccount getUserAccountByAccountId(Map map);

	//����userId ��  ����id ��ѯ�û��˺Ź�ϵ�����Ƿ��й�ϵ
	public Integer getCountLinksusRelationUserAccount(LinksusRelationUserAccount entity);

	//��ȡ�û���ע�ķ�˿��Ϣ
	public List<LinksusRelationUserAccount> getLinksusRelationUserAccountList(
			LinksusRelationUserAccount linksusRelationUserAccount);

	/**��������*/
	//public void updateLinksusRelationUserAccount(LinksusRelationUserAccount linksusRelationUserAccount);
	/**����������*/
	//public void insertLinksusRelationUserAccount(LinksusRelationUserAccount linksusRelationUserAccount);

	//public void saveLinksusRelationUserAccount(List UserAccountList,String operType);
	//�����û��˻���ϵ��������
	//public void  updateLinksusRelationUserAccountNum(LinksusRelationUserAccount linksusRelationUserAccount);
	public void updateLinksusRelationUserAccountNum(List list);

	public LinksusRelationUserAccount getRelationUser(Map map);

	public void deleteLinksusRelationUserAccount(Long id);

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
