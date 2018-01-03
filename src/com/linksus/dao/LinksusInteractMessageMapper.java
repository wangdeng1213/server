package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusInteractMessage;

public interface LinksusInteractMessageMapper{

	/** �б���ѯ */
	public List<LinksusInteractMessage> getLinksusInteractMessageList();

	/** ������ѯ */
	public LinksusInteractMessage getLinksusInteractMessageById(Long pid);

	/** ���� */
	public Integer insertLinksusInteractMessage(LinksusInteractMessage entity);

	/** ���� */
	public Integer updateLinksusInteractMessage(LinksusInteractMessage entity);

	/** ����ɾ�� */
	public Integer deleteLinksusInteractMessageById(Long pid);

	/** ������ϢID��ѯ��¼�� */
	public Integer getLinksusInteractMessageByMsgId(Long msgId);

	public Integer addInteractReadMessage(LinksusInteractMessage entity);

	/** ��ѯ��Ҫ���͵�˽������ */
	public List<LinksusInteractMessage> getSendMessageList(LinksusInteractMessage message);

	/** ����˽�ŷ���״̬ */
	public Integer updateInteractMessageStatus(Map message);

	/** ����˽�Żظ����� */
	public Integer updateInteractMessageReplyCount(Long pid);

	/** ��ѯ˽���Ƿ��Ѵ���  */
	public LinksusInteractMessage getMessageIsExists(Map map);

	/** ����������ѯ��Ҫ���͵�˽������ */
	public LinksusInteractMessage getSendMessageById(Long pid);

	/** ��ѯ��Ҫ��ʱ���͵�˽������ */
	public List getMessagePrepareList(LinksusInteractMessage queryMsg);

	public List getInteractMessageListByIds(List letterMsgs);

}