package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusInteractMessage;

public interface LinksusInteractMessageService{

	/** 查询列表 */
	public List<LinksusInteractMessage> getLinksusInteractMessageList();

	/** 主键查询 */
	public LinksusInteractMessage getLinksusInteractMessageById(Long pid);

	/** 新增 */
	public Integer insertLinksusInteractMessage(LinksusInteractMessage entity);

	/** 私信读数据 */
	public Integer addInteractReadMessage(LinksusInteractMessage entity);

	/** 更新 */
	public Integer updateLinksusInteractMessage(LinksusInteractMessage entity);

	/** 主键删除 */
	public Integer deleteLinksusInteractMessageById(Long pid);

	/** 根据消息ID查询记录数 */
	public Integer getLinksusInteractMessageByMsgId(Long msgId);

	/** 查询需要发送的私信数据 */
	public List<LinksusInteractMessage> getSendMessageList(LinksusInteractMessage message);

	/** 更新私信发布状态 */
	public Integer updateInteractMessageStatus(Map message);

	/** 更新私信回复次数 */
	public Integer updateInteractMessageReplyCount(Long pid);

	/** 查询私信是否已存在  */
	public LinksusInteractMessage getMessageIsExists(Map map);

	/** 根据主键查询需要发送的私信数据 */
	public LinksusInteractMessage getSendMessageById(Long pid);

	/** 查询需要定时发送的私信数据 */
	public List getMessagePrepareList(LinksusInteractMessage queryMsg);

	public List getInteractMessageListByIds(List letterMsgs);

}
