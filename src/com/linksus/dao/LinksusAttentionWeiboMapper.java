package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusAttentionWeibo;
import com.linksus.entity.LinksusWeibo;

public interface LinksusAttentionWeiboMapper{

	public LinksusAttentionWeibo getLinksusAttention();

	public void updateLinksusAttentionWeibo(List<LinksusAttentionWeibo> list);

	public void updateLinksusAttentionWeiboSingle(LinksusAttentionWeibo linksusAttentionWeibo);

	public List<LinksusWeibo> getLinksusAttentionWeiboHasSend(LinksusWeibo weibo);

	public LinksusWeibo getLinksusAttentionWeiboByMap(Map map);

	public void updateSendWeiboCount(LinksusWeibo linksusWeibo);

	public void insertLinksusAttentionWeibo(LinksusAttentionWeibo linksusAttentionWeibo);

	//�жϹ�ע�û���΢���Ƿ����
	public Integer getAttentionWeiboCount(LinksusAttentionWeibo linksusAttentionWeibo);
}
