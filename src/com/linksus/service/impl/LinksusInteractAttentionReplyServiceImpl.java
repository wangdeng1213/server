package com.linksus.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusInteractAttentionReplyMapper;
import com.linksus.entity.LinksusInteractAttentionReply;
import com.linksus.service.LinksusInteractAttentionReplyService;

@Service("linksusInteractAttentionReplyService")
public class LinksusInteractAttentionReplyServiceImpl implements LinksusInteractAttentionReplyService{

	@Autowired
	private LinksusInteractAttentionReplyMapper linksusInteractAttentionReplyMapper;

	/** ��ѯ�б� */
	public List<LinksusInteractAttentionReply> getLinksusInteractAttentionReplyList(){
		return linksusInteractAttentionReplyMapper.getLinksusInteractAttentionReplyList();
	}

	/** ������ѯ */
	public LinksusInteractAttentionReply getLinksusInteractAttentionReplyById(Long pid){
		return linksusInteractAttentionReplyMapper.getLinksusInteractAttentionReplyById(pid);
	}

	/** ���� */
	public Integer insertLinksusInteractAttentionReply(LinksusInteractAttentionReply entity){
		return linksusInteractAttentionReplyMapper.insertLinksusInteractAttentionReply(entity);
	}

	/** ���� */
	public Integer updateLinksusInteractAttentionReply(LinksusInteractAttentionReply entity){
		return linksusInteractAttentionReplyMapper.updateLinksusInteractAttentionReply(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusInteractAttentionReplyById(Long pid){
		return linksusInteractAttentionReplyMapper.deleteLinksusInteractAttentionReplyById(pid);
	}

	/** ͨ��������ƽ̨���Ͳ�ѯ */
	public LinksusInteractAttentionReply getLinksusInteractAttentionReplyByIdAndType(Map map){
		return linksusInteractAttentionReplyMapper.getLinksusInteractAttentionReplyByIdAndType(map);
	}
}