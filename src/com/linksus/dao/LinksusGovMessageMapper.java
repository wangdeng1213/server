package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusGovMessage;

public interface LinksusGovMessageMapper{

	/** 列表查询 */
	public List<LinksusGovMessage> getLinksusGovMessageList();

	/** 主键查询 */
	public LinksusGovMessage getLinksusGovMessageById(Long pid);

	/** 新增 */
	public Integer insertLinksusGovMessage(LinksusGovMessage entity);

	/** 更新 */
	public Integer updateLinksusGovMessage(LinksusGovMessage entity);

	/** 主键删除 */
	public Integer deleteLinksusGovMessageById(Long pid);

	public Integer getLinksusInteractMessageCountByMsgId(Long msgId);

	public Integer addInteractReadMessage(LinksusGovMessage entity);

	public List<LinksusGovMessage> getSendMessageList(LinksusGovMessage queryMsg);

	public List<LinksusGovMessage> getMessagePrepareList(LinksusGovMessage queryMsg);

	public void updateLinksusGovMessageStatus(Map map);

	public List<LinksusGovMessage> getLinksusGovMessageByMsgId(Long msgId);
}
