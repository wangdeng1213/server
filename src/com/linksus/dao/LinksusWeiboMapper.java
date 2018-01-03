package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusWeibo;

/**
 * 
 * 微博表接口
 *
 */
public interface LinksusWeiboMapper{

	LinksusWeibo getLinksusWeiboById(Long pid);

	public LinksusWeibo getLinksusWeiboByMap(Map map);

	List<LinksusWeibo> getLinksusWeiboHasSend(LinksusWeibo linksusWeibo);

	List<LinksusWeibo> getWeiboImmeSend(LinksusWeibo linksusWeibo);

	List<LinksusWeibo> getWeiboRecordPrepare(LinksusWeibo linksusWeibo);

	void updateSendWeiboFailed(LinksusWeibo weibo);

	public Integer getCountByaccount(Long accountId);

	void updateSendWeiboSucc(LinksusWeibo linksusWeibo);

	void updateSendWeiboCount(LinksusWeibo linksusWeibo);

	List<LinksusWeibo> getLinksusWeiboHasSendSuccess();

	List<LinksusWeibo> getLinksusWeiboHasSendSuccessCon(Integer sendTime);

	/** 新增 */
	public Integer insertLinksusWeibo(LinksusWeibo entity);

	//试运行使用
	List<LinksusWeibo> getWeiboRecordPrepareTrial(LinksusWeibo linksusWeibo);

	List<LinksusWeibo> getWeiboImmeSendTrial(LinksusWeibo linksusWeibo);

	Object updatePurchaseWeiboFailed(Long referId);

	Object updatePurchaseWeiboSucc(Map paramMap);

	void deletePurchaseWeibo(Long pid);

}
