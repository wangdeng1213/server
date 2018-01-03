package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusAttentionWeibo;
import com.linksus.entity.LinksusWeibo;

/**
 *linksus_attention_weibo关注用户微博service
 * @author  wangdeng
 *
 */
public interface LinksusAttentionWeiboService{

	public LinksusAttentionWeibo getLinksusAttention();

	//list更新到关注用户微博表中 
	public void updateLinksusAttentionWeibo(List<LinksusAttentionWeibo> list);

	public void updateLinksusAttentionWeiboSingle(LinksusAttentionWeibo linksusAttentionWeibo);

	public List<LinksusWeibo> getLinksusAttentionWeiboHasSend(LinksusWeibo weibo);

	public LinksusWeibo getLinksusAttentionWeiboByMap(Map map);

	//public void updateSendWeiboCount(LinksusWeibo linksusWeibo);

	//将关注用户返回的微博数据更新到关注微博表中
	public void insertLinksusAttentionWeibo(LinksusAttentionWeibo linksusAttentionWeibo);

	//判断关注用户的微博是否存在
	public Integer getAttentionWeiboCount(LinksusAttentionWeibo linksusAttentionWeibo);
}
