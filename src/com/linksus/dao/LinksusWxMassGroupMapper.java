package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusWxMassGroup;

public interface LinksusWxMassGroupMapper{

	/** 列表查询 */
	public List<LinksusWxMassGroup> getLinksusWxMassGroupList();

	/** 主键查询 */
	public LinksusWxMassGroup getLinksusWxMassGroupById(Long pid);

	/** 新增 */
	public Integer insertLinksusWxMassGroup(LinksusWxMassGroup entity);

	/** 更新 */
	public Integer updateLinksusWxMassGroup(LinksusWxMassGroup entity);

	/** 主键删除 */
	public Integer deleteLinksusWxMassGroupById(Long pid);

	/** 更新群发结果 */
	public Integer updateMassResultGroup(LinksusWxMassGroup entity);

	/** 通过消息msgid获取wx_id  */
	public LinksusWxMassGroup getLinksusWxMassGroupByMsgId(Map mapInfo);

	/**通过微信id获取msgId*/
	public List<LinksusWxMassGroup> getLinksusWxMassGroupByWxId(Long wxId);
}
