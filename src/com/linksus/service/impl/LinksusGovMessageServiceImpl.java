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

	/** ��ѯ�б� */
	public List<LinksusGovMessage> getLinksusGovMessageList(){
		return linksusGovMessageMapper.getLinksusGovMessageList();
	}

	/** ������ѯ */
	public LinksusGovMessage getLinksusGovMessageById(Long pid){
		return linksusGovMessageMapper.getLinksusGovMessageById(pid);
	}

	/** ���� */
	public Integer insertLinksusGovMessage(LinksusGovMessage entity){
		return linksusGovMessageMapper.insertLinksusGovMessage(entity);
	}

	/** ���� */
	public Integer updateLinksusGovMessage(LinksusGovMessage entity){
		return linksusGovMessageMapper.updateLinksusGovMessage(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusGovMessageById(Long pid){
		return linksusGovMessageMapper.deleteLinksusGovMessageById(pid);
	}

	/** ������ϢID��ѯ��¼�� */
	public Integer getLinksusInteractMessageCountByMsgId(Long msgId){
		return linksusGovMessageMapper.getLinksusInteractMessageCountByMsgId(msgId);
	}

	/** ˽�Ŷ����� */
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