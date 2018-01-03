package com.linksus.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.common.Constants;
import com.linksus.dao.LinksusInteractMessageMapper;
import com.linksus.entity.LinksusInteractMessage;
import com.linksus.service.BaseService;
import com.linksus.service.LinksusInteractMessageService;

@Service("linksusInteractMessageService")
public class LinksusInteractMessageServiceImpl extends BaseService implements LinksusInteractMessageService{

	@Autowired
	private LinksusInteractMessageMapper linksusInteractMessageMapper;

	/** ��ѯ�б� */
	public List<LinksusInteractMessage> getLinksusInteractMessageList(){
		return linksusInteractMessageMapper.getLinksusInteractMessageList();
	}

	/** ������ѯ */
	public LinksusInteractMessage getLinksusInteractMessageById(Long pid){
		return linksusInteractMessageMapper.getLinksusInteractMessageById(pid);
	}

	/** ���� */
	public Integer insertLinksusInteractMessage(LinksusInteractMessage entity){
		return linksusInteractMessageMapper.insertLinksusInteractMessage(entity);
	}

	/** ˽�Ŷ����� */
	public Integer addInteractReadMessage(LinksusInteractMessage entity){
		return linksusInteractMessageMapper.addInteractReadMessage(entity);
	}

	/** ���� */
	public Integer updateLinksusInteractMessage(LinksusInteractMessage entity){
		return linksusInteractMessageMapper.updateLinksusInteractMessage(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusInteractMessageById(Long pid){
		return linksusInteractMessageMapper.deleteLinksusInteractMessageById(pid);
	}

	/** ������ϢID��ѯ��¼�� */
	public Integer getLinksusInteractMessageByMsgId(Long msgId){
		return linksusInteractMessageMapper.getLinksusInteractMessageByMsgId(msgId);
	}

	/** ��ѯ��Ҫ���͵�˽������ */
	public List<LinksusInteractMessage> getSendMessageList(LinksusInteractMessage message){
		return linksusInteractMessageMapper.getSendMessageList(message);
	}

	/** ����˽�ŷ���״̬ */
	public Integer updateInteractMessageStatus(Map message){
		return linksusInteractMessageMapper.updateInteractMessageStatus(message);
	}

	/** ����˽�Żظ����� */
	public Integer updateInteractMessageReplyCount(Long pid){
		return linksusInteractMessageMapper.updateInteractMessageReplyCount(pid);
	}

	/** ��ѯ˽���Ƿ��Ѵ���  */
	public LinksusInteractMessage getMessageIsExists(Map map){
		return linksusInteractMessageMapper.getMessageIsExists(map);
	}

	/** ����������ѯ��Ҫ���͵�˽������ */
	public LinksusInteractMessage getSendMessageById(Long pid){
		return linksusInteractMessageMapper.getSendMessageById(pid);
	}

	/** ��ѯ��Ҫ��ʱ���͵�˽������ */
	public List getMessagePrepareList(LinksusInteractMessage queryMsg){
		return linksusInteractMessageMapper.getMessagePrepareList(queryMsg);
	}

	@Override
	public List getInteractMessageListByIds(List letterMsgs){
		return linksusInteractMessageMapper.getInteractMessageListByIds(letterMsgs);
	}

	@Override
	public void saveEntity(List dataList,String operType) throws Exception{
		for(int i = 0; i < dataList.size(); i++){
			LinksusInteractMessage entity = (LinksusInteractMessage) dataList.get(i);
			if(operType.equals(Constants.OPER_TYPE_INSERT)){
				sqlSession.insert("com.linksus.dao.LinksusInteractMessageMapper.insertLinksusInteractMessage", entity);
			}
		}
	}
}