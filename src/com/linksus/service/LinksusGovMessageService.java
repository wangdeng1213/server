package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusGovMessage;

public interface LinksusGovMessageService{

	/** 查询列表 */
	public List<LinksusGovMessage> getLinksusGovMessageList();

	/** 主键查询 */
	public LinksusGovMessage getLinksusGovMessageById(Long pid);

	/** 新增 */
	public Integer insertLinksusGovMessage(LinksusGovMessage entity);

	/** 更新 */
	public Integer updateLinksusGovMessage(LinksusGovMessage entity);

	/** 主键删除 */
	public Integer deleteLinksusGovMessageById(Long pid);

	/** 根据消息ID查询记录数 */
	public Integer getLinksusInteractMessageCountByMsgId(Long msgId);

	public Integer addInteractReadMessage(LinksusGovMessage entity);

	public List<LinksusGovMessage> getSendMessageList(LinksusGovMessage queryMsg);

	public List<LinksusGovMessage> getMessagePrepareList(LinksusGovMessage queryMsg);

	/** 更新发布状态**/
	public void updateLinksusGovMessageStatus(Map map);

	public LinksusGovMessage getLinksusGovMessageByMsgId(Long msgId);

}
