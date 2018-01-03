package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusInteractWeibo;

public interface LinksusInteractWeiboService{

	/** ��ѯ�б� */
	public List<LinksusInteractWeibo> getLinksusInteractWeiboList();

	/** ������ѯ */
	public LinksusInteractWeibo getLinksusInteractWeiboById(Long pid);

	/** ���� */
	public Integer insertLinksusInteractWeibo(LinksusInteractWeibo entity);

	/** ���� */
	//public Integer updateLinksusInteractWeibo(LinksusInteractWeibo entity);

	/** ����ɾ�� */
	public Integer deleteLinksusInteractWeiboById(Long pid);

	/** <!--ȡ���ظ���Ϣ�������б�  -->*/
	public List<LinksusInteractWeibo> getLinksusInteractWeiboTaskList(LinksusInteractWeibo entity);

	public void updateSendReplyStatus(LinksusInteractWeibo entity);

	//���·��ص� ��Ϣ
	public void updateSendReplySucc(LinksusInteractWeibo entity);

	/** <!--ȡ���ظ���Ϣ�Ķ�ʱ���������б�  -->*/
	//public List<LinksusInteractWeibo> getWeiboReplyPrepare(LinksusInteractWeibo linksusWeibo);
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
