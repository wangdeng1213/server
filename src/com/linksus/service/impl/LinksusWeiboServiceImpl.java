package com.linksus.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.common.Constants;
import com.linksus.common.config.LoadConfig;
import com.linksus.common.util.CacheUtil;
import com.linksus.dao.LinksusWeiboMapper;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.entity.LinksusWeibo;
import com.linksus.service.BaseService;
import com.linksus.service.LinksusWeiboService;

@Service("linksusWeiboService")
public class LinksusWeiboServiceImpl extends BaseService implements LinksusWeiboService{

	private String trialFlag = LoadConfig.getString("trialFlag");
	@Autowired
	private LinksusWeiboMapper linksusWeiboMapper;

	@Override
	public LinksusWeibo getLinksusWeiboById(Long pid){
		return linksusWeiboMapper.getLinksusWeiboById(pid);
	}

	@Override
	public Integer getCountByaccount(Long accountId){
		return linksusWeiboMapper.getCountByaccount(accountId);
	}

	@Override
	public LinksusWeibo getLinksusWeiboByMap(Map map){
		return linksusWeiboMapper.getLinksusWeiboByMap(map);
	}

	@Override
	public List<LinksusWeibo> getWeiboRecordPrepare(LinksusWeibo linksusWeibo){
		if(!"1".equals(trialFlag)){
			return linksusWeiboMapper.getWeiboRecordPrepare(linksusWeibo);
		}else{
			return linksusWeiboMapper.getWeiboRecordPrepareTrial(linksusWeibo);
		}
	}

	@Override
	public void updateSendWeiboFailed(Long pid,String errorCode){
		String errorMsg = "";
		if(errorCode.equals(Constants.INVALID_RECORD_EXCEPTION)){
			errorMsg = "服务器错误(错误码：10000)";
		}else{
			CacheUtil cache = CacheUtil.getInstance();
			LinksusTaskErrorCode error = cache.getErrorCode(errorCode);
			if(error.getDisplayType().intValue() == 0){
				errorMsg = error.getErrorMsg();
			}else{
				errorMsg = "服务器错误(错误码：" + errorCode + ")";
			}
		}
		LinksusWeibo weibo = new LinksusWeibo();
		weibo.setId(pid);
		weibo.setErrmsg(errorMsg);
		linksusWeiboMapper.updateSendWeiboFailed(weibo);
	}

	@Override
	public void updateSendWeiboSucc(LinksusWeibo linksusWeibo){
		linksusWeiboMapper.updateSendWeiboSucc(linksusWeibo);
	}

	//	@Override
	//	public void updateSendWeiboCount(LinksusWeibo linksusWeibo){
	//		linksusWeiboMapper.updateSendWeiboCount(linksusWeibo);
	//	}

	@Override
	public List<LinksusWeibo> getLinksusWeiboHasSend(LinksusWeibo linksusWeibo){
		return linksusWeiboMapper.getLinksusWeiboHasSend(linksusWeibo);
	}

	@Override
	public List<LinksusWeibo> getWeiboImmeSend(LinksusWeibo linksusWeibo){
		if(!"1".equals(trialFlag)){
			return linksusWeiboMapper.getWeiboImmeSend(linksusWeibo);
		}else{
			return linksusWeiboMapper.getWeiboImmeSendTrial(linksusWeibo);
		}
	}

	/** 新增 */
	public Integer insertLinksusWeibo(LinksusWeibo entity){
		return linksusWeiboMapper.insertLinksusWeibo(entity);
	}

	public List<LinksusWeibo> getLinksusWeiboHasSendSuccess(){
		return linksusWeiboMapper.getLinksusWeiboHasSendSuccess();
	}

	public List<LinksusWeibo> getLinksusWeiboHasSendSuccessCon(Integer sendTime){
		return linksusWeiboMapper.getLinksusWeiboHasSendSuccessCon(sendTime);
	}

	public void updatePurchaseWeiboFailed(Long referId){
		linksusWeiboMapper.updatePurchaseWeiboFailed(referId);
	}

	public void updatePurchaseWeiboSucc(Map paramMap){
		linksusWeiboMapper.updatePurchaseWeiboSucc(paramMap);
	}

	/** 微博发布批量任务*/
	public void saveEntity(List dataList,String operType) throws Exception{
		try{
			for(int i = 0; i < dataList.size(); i++){
				LinksusWeibo entity = (LinksusWeibo) dataList.get(i);
				if(operType.equals(Constants.OPER_TYPE_UPDATE)){
					sqlSession.update("com.linksus.dao.LinksusWeiboMapper.updateSendWeiboCount", entity);
				}
			}
		}catch (Exception e){
			throw e;
		}
	}

	@Override
	public void deletePurchaseWeibo(Long pid){
		linksusWeiboMapper.deletePurchaseWeibo(pid);
	}
}
