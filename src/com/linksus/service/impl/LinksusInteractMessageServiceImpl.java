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

	/** 查询列表 */
	public List<LinksusInteractMessage> getLinksusInteractMessageList(){
		return linksusInteractMessageMapper.getLinksusInteractMessageList();
	}

	/** 主键查询 */
	public LinksusInteractMessage getLinksusInteractMessageById(Long pid){
		return linksusInteractMessageMapper.getLinksusInteractMessageById(pid);
	}

	/** 新增 */
	public Integer insertLinksusInteractMessage(LinksusInteractMessage entity){
		return linksusInteractMessageMapper.insertLinksusInteractMessage(entity);
	}

	/** 私信读数据 */
	public Integer addInteractReadMessage(LinksusInteractMessage entity){
		return linksusInteractMessageMapper.addInteractReadMessage(entity);
	}

	/** 更新 */
	public Integer updateLinksusInteractMessage(LinksusInteractMessage entity){
		return linksusInteractMessageMapper.updateLinksusInteractMessage(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusInteractMessageById(Long pid){
		return linksusInteractMessageMapper.deleteLinksusInteractMessageById(pid);
	}

	/** 根据消息ID查询记录数 */
	public Integer getLinksusInteractMessageByMsgId(Long msgId){
		return linksusInteractMessageMapper.getLinksusInteractMessageByMsgId(msgId);
	}

	/** 查询需要发送的私信数据 */
	public List<LinksusInteractMessage> getSendMessageList(LinksusInteractMessage message){
		return linksusInteractMessageMapper.getSendMessageList(message);
	}

	/** 更新私信发布状态 */
	public Integer updateInteractMessageStatus(Map message){
		return linksusInteractMessageMapper.updateInteractMessageStatus(message);
	}

	/** 更新私信回复次数 */
	public Integer updateInteractMessageReplyCount(Long pid){
		return linksusInteractMessageMapper.updateInteractMessageReplyCount(pid);
	}

	/** 查询私信是否已存在  */
	public LinksusInteractMessage getMessageIsExists(Map map){
		return linksusInteractMessageMapper.getMessageIsExists(map);
	}

	/** 根据主键查询需要发送的私信数据 */
	public LinksusInteractMessage getSendMessageById(Long pid){
		return linksusInteractMessageMapper.getSendMessageById(pid);
	}

	/** 查询需要定时发送的私信数据 */
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