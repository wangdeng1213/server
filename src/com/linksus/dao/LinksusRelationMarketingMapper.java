package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusRelationMarketing;

public interface LinksusRelationMarketingMapper{

	/**��ѯ����ͨ������Ҫ�������۵�Ӫ���б�*/
	public List<LinksusRelationMarketing> getSendCommentsList(Map paraMap);

	/**����Ӫ��������������ʧ�ܳɹ�����*/
	public void updateLinksysUserMarketing(LinksusRelationMarketing linksusRelationMarketing);

	/**��ѯ����ͨ�������˻���Ѷ�˺����͵�Ӫ���б�*/
	public List<LinksusRelationMarketing> getSendCommentsByUserList(LinksusRelationMarketing linksusRelationMarketing);

	public void updateMentionsStatus(LinksusRelationMarketing marketing);
}