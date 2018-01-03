package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusAttentionWeibo;
import com.linksus.entity.LinksusWeibo;

/**
 *linksus_attention_weibo��ע�û�΢��service
 * @author  wangdeng
 *
 */
public interface LinksusAttentionWeiboService{

	public LinksusAttentionWeibo getLinksusAttention();

	//list���µ���ע�û�΢������ 
	public void updateLinksusAttentionWeibo(List<LinksusAttentionWeibo> list);

	public void updateLinksusAttentionWeiboSingle(LinksusAttentionWeibo linksusAttentionWeibo);

	public List<LinksusWeibo> getLinksusAttentionWeiboHasSend(LinksusWeibo weibo);

	public LinksusWeibo getLinksusAttentionWeiboByMap(Map map);

	//public void updateSendWeiboCount(LinksusWeibo linksusWeibo);

	//����ע�û����ص�΢�����ݸ��µ���ע΢������
	public void insertLinksusAttentionWeibo(LinksusAttentionWeibo linksusAttentionWeibo);

	//�жϹ�ע�û���΢���Ƿ����
	public Integer getAttentionWeiboCount(LinksusAttentionWeibo linksusAttentionWeibo);
}
