package com.linksus.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusRelationMarketingMapper;
import com.linksus.entity.LinksusRelationMarketing;
import com.linksus.service.LinksusRelationMarketingService;

@Service("linksusRelationMarketingService")
public class LinksusRelationMarketingServiceImpl implements LinksusRelationMarketingService{

	@Autowired
	private LinksusRelationMarketingMapper linksusRelationMarketingMapper;

	/**��ѯ����ͨ������Ҫ�������۵�Ӫ���б�*/
	public List<LinksusRelationMarketing> getSendCommentsList(Map paraMap){
		return linksusRelationMarketingMapper.getSendCommentsList(paraMap);
	}

	/**����Ӫ��������������ʧ�ܳɹ�����*/
	public void updateLinksysUserMarketing(LinksusRelationMarketing linksusRelationMarketing){
		linksusRelationMarketingMapper.updateLinksysUserMarketing(linksusRelationMarketing);
	}

	/**��ѯ����ͨ�������˻���Ѷ�˺����͵�Ӫ���б�*/
	public List<LinksusRelationMarketing> getSendCommentsByUserList(LinksusRelationMarketing linksusRelationMarketing){
		return linksusRelationMarketingMapper.getSendCommentsByUserList(linksusRelationMarketing);
	}

	public void updateMentionsStatus(LinksusRelationMarketing marketing){
		linksusRelationMarketingMapper.updateMentionsStatus(marketing);
	}
}