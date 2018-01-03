package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusInteractWeibo;

public interface LinksusInteractWeiboMapper{

	/** �б��ѯ */
	public List<LinksusInteractWeibo> getLinksusInteractWeiboList();

	/** ������ѯ */
	public LinksusInteractWeibo getLinksusInteractWeiboById(Long pid);

	/** ���� */
	public Integer insertLinksusInteractWeibo(LinksusInteractWeibo entity);

	/** ���� */
	public Integer updateLinksusInteractWeibo(LinksusInteractWeibo entity);

	/** ����ɾ�� */
	public Integer deleteLinksusInteractWeiboById(Long pid);

	/** <!--ȡ���ظ���Ϣ�������б�  -->*/
	public List<LinksusInteractWeibo> getLinksusInteractWeiboTaskList(LinksusInteractWeibo entity);

	//���·��ص� ��Ϣ
	public void updateSendReplySucc(LinksusInteractWeibo entity);

	public void updateSendReplyStatus(LinksusInteractWeibo entity);

	/** <!--ȡ���ظ���Ϣ�Ķ�ʱ���������б�  -->*/
	//public List<LinksusInteractWeibo> getWeiboReplyPrepare(LinksusInteractWeibo entity);
	/**comment_id��account_type ɾ��*/
	public Integer deleteLinksusInteractWeiboByMap(Map map);

	/**��ѯ΢��������¼�Ƿ����*/
	public LinksusInteractWeibo getInteractWeiboIsExists(Map map);

	/** ����mid��ѯ�����б� */
	public List getInteractWeiboListByMid(Map map);

	/** ����������ѯ��Ҫ�ظ����۵�΢��*/
	public LinksusInteractWeibo getReplyWeiboById(Long pid);

	public List getInteractWeiboListByIds(List weiboMsgs);

}
