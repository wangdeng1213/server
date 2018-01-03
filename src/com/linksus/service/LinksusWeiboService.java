package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusWeibo;

public interface LinksusWeiboService{

	public LinksusWeibo getLinksusWeiboById(Long pid);

	public LinksusWeibo getLinksusWeiboByMap(Map map);

	public List<LinksusWeibo> getWeiboImmeSend(LinksusWeibo linksusWeibo);

	public List<LinksusWeibo> getWeiboRecordPrepare(LinksusWeibo linksusWeibo);

	public void updateSendWeiboSucc(LinksusWeibo linksusWeibo);

	//public void updateSendWeiboCount(LinksusWeibo linksusWeibo);

	public void deletePurchaseWeibo(Long pid);

	public void updateSendWeiboFailed(Long pid,String errmsg);

	public List<LinksusWeibo> getLinksusWeiboHasSend(LinksusWeibo linksusWeibo);

	/** ÐÂÔö */
	public Integer getCountByaccount(Long accountId);

	public Integer insertLinksusWeibo(LinksusWeibo entity);

	public List<LinksusWeibo> getLinksusWeiboHasSendSuccess();

	public List<LinksusWeibo> getLinksusWeiboHasSendSuccessCon(Integer sendTime);

	public void updatePurchaseWeiboFailed(Long referId);

	public void updatePurchaseWeiboSucc(Map paramMap);

}
