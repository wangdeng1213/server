package com.linksus.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusGovMessageMapper;
import com.linksus.entity.LinksusGovMessage;
import com.linksus.service.LinksusGovMessageService;

@Service("linksusGovMessageService")
public class LinksusGovMessageServiceImpl implements LinksusGovMessageService{

	@Autowired
	private LinksusGovMessageMapper linksusGovMessageMapper;

	/** 查询列表 */
	public List<LinksusGovMessage> getLinksusGovMessageList(){
		return linksusGovMessageMapper.getLinksusGovMessageList();
	}

	/** 主键查询 */
	public LinksusGovMessage getLinksusGovMessageById(Long pid){
		return linksusGovMessageMapper.getLinksusGovMessageById(pid);
	}

	/** 新增 */
	public Integer insertLinksusGovMessage(LinksusGovMessage entity){
		return linksusGovMessageMapper.insertLinksusGovMessage(entity);
	}

	/** 更新 */
	public Integer updateLinksusGovMessage(LinksusGovMessage entity){
		return linksusGovMessageMapper.updateLinksusGovMessage(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusGovMessageById(Long pid){
		return linksusGovMessageMapper.deleteLinksusGovMessageById(pid);
	}

	/** 根据消息ID查询记录数 */
	public Integer getLinksusInteractMessageCountByMsgId(Long msgId){
		return linksusGovMessageMapper.getLinksusInteractMessageCountByMsgId(msgId);
	}

	/** 私信读数据 */
	public Integer addInteractReadMessage(LinksusGovMessage entity){
		return linksusGovMessageMapper.addInteractReadMessage(entity);
	}

	@Override
	public List<LinksusGovMessage> getSendMessageList(LinksusGovMessage queryMsg){
		return linksusGovMessageMapper.getSendMessageList(queryMsg);
	}

	public List<LinksusGovMessage> getMessagePrepareList(LinksusGovMessage queryMsg){
		return linksusGovMessageMapper.getSendMessageList(queryMsg);
	}

	@Override
	public void updateLinksusGovMessageStatus(Map map){
		linksusGovMessageMapper.updateLinksusGovMessageStatus(map);
	}

	@Override
	public LinksusGovMessage getLinksusGovMessageByMsgId(Long msgId){
		List<LinksusGovMessage> list = linksusGovMessageMapper.getLinksusGovMessageByMsgId(msgId);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
}