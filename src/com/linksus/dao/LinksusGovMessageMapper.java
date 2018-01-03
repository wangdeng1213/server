package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusGovMessage;

public interface LinksusGovMessageMapper{

	/** �б��ѯ */
	public List<LinksusGovMessage> getLinksusGovMessageList();

	/** ������ѯ */
	public LinksusGovMessage getLinksusGovMessageById(Long pid);

	/** ���� */
	public Integer insertLinksusGovMessage(LinksusGovMessage entity);

	/** ���� */
	public Integer updateLinksusGovMessage(LinksusGovMessage entity);

	/** ����ɾ�� */
	public Integer deleteLinksusGovMessageById(Long pid);

	public Integer getLinksusInteractMessageCountByMsgId(Long msgId);

	public Integer addInteractReadMessage(LinksusGovMessage entity);

	public List<LinksusGovMessage> getSendMessageList(LinksusGovMessage queryMsg);

	public List<LinksusGovMessage> getMessagePrepareList(LinksusGovMessage queryMsg);

	public void updateLinksusGovMessageStatus(Map map);

	public List<LinksusGovMessage> getLinksusGovMessageByMsgId(Long msgId);
}
