package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusWx;

public interface LinksusWxMapper{

	/** �б��ѯ */
	public List<LinksusWx> getLinksusWxList();

	/** ������ѯ */
	public LinksusWx getLinksusWxById(Long pid);

	/** ���� */
	public Integer insertLinksusWx(LinksusWx entity);

	/** ���� */
	public Integer updateLinksusWx(LinksusWx entity);

	/** ����ɾ�� */
	public Integer deleteLinksusWxById(Long pid);

	/** ��ѯ����״̬Ϊδִ��״̬��΢������ */
	public List<LinksusWx> getWeiXinTaskList(LinksusWx entity);

	/** �޸�����״̬ */
	public void updateWeiXinTaskList(LinksusWx entity);

	/** ��ȡ΢��Ⱥ����Ϣ�û�token����Ϣ */
	public LinksusWx getLinksusWXUserAndInfo(Long pid);

	/**  �޸�΢�ŷ������*/
	public void updateWeiXinTaskErrmsg(LinksusWx entity);

	/**  ͨ��΢��Ⱥ����Ϣ���޸�΢�ŷ������*/
	public void updateWeiXinTaskErrmsgByGroup(Long pid);

	/** �޸�΢�ŷ��������״̬*/
	public void updateWeiXinTaskstatusAndErrmsg(LinksusWx entity);

}
