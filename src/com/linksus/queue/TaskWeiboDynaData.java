package com.linksus.queue;

import java.util.List;
import java.util.Map;

import com.linksus.common.Constants;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.data.QueueDataSave;
import com.linksus.entity.LinksusAttentionWeibo;
import com.linksus.entity.LinksusWeibo;
import com.linksus.entity.LinksusWeiboPool;
import com.linksus.entity.ResponseAndRecordDTO;
import com.linksus.service.LinksusAttentionWeiboService;
import com.linksus.service.LinksusWeiboPoolService;
import com.linksus.service.LinksusWeiboService;

public class TaskWeiboDynaData extends BaseQueue{

	private String dataType;// 微博/关注用户微博
	private String accountType;// 账号类型

	public void setAccountType(String accountType){
		this.accountType = accountType;
	}

	public void setDataType(String dataType){
		this.dataType = dataType;
	}

	protected TaskWeiboDynaData(String taskType) {
		super(taskType);
	}

	LinksusWeiboService linksusWeiboService = (LinksusWeiboService) ContextUtil.getBean("linksusWeiboService");
	LinksusAttentionWeiboService linksusAttentionWeiboService = (LinksusAttentionWeiboService) ContextUtil
			.getBean("linksusAttentionWeiboService");
	LinksusWeiboPoolService linksusWeiboPoolService = (LinksusWeiboPoolService) ContextUtil
			.getBean("linksusWeiboPoolService");

	@Override
	public List flushTaskQueue(){
		// 从服务器端采用分页取数据
		int currenCount = 0;
		currenCount = (currentPage - 1) * pageSize;
		// 通过分页取得直发列表
		LinksusWeibo weibo = new LinksusWeibo();
		weibo.setStartCount(currenCount);
		weibo.setPageSize(pageSize);
		weibo.setAccountType(new Integer(accountType));// 1,新浪 2,腾讯
		List<LinksusWeibo> weiboList = null;
		if("1".equals(dataType)){//记录账户所发布的所有微博信息
			weiboList = linksusWeiboService.getLinksusWeiboHasSend(weibo);
		}else if("2".equals(dataType)){//帐号关注微博列表信息
			weiboList = linksusAttentionWeiboService.getLinksusAttentionWeiboHasSend(weibo);
		}else if("3".equals(dataType)){//发布，关系，互动，矩阵，舆情微博信息表
			weiboList = linksusWeiboPoolService.getLinksusWeiboPoolSend(weibo);
		}else{
			LogUtil.saveException(logger, new Exception("dataType参数错误"));
		}
		if(weiboList == null || weiboList.size() < pageSize){// 任务循环完成 下次重新开始
			currentPage = 1;
			setTaskFinishFlag();
		}else{
			currentPage++;
		}
		return weiboList;
	}

	@Override
	protected String parseClientData(Map rsDataMap){
		List<ResponseAndRecordDTO> linksusWeibos = (List<ResponseAndRecordDTO>) rsDataMap.get("weiboList");
		if(linksusWeibos != null && linksusWeibos.size() > 0){
			for(ResponseAndRecordDTO dto : linksusWeibos){
				if("1".equals(dataType)){
					//微博发布
					LinksusWeibo linksusWeibo = new LinksusWeibo();
					linksusWeibo.setCommentCount(dto.getComments());
					linksusWeibo.setRepostCount(dto.getReposts());
					linksusWeibo.setMid(dto.getId());
					linksusWeibo.setAccountType(Integer.parseInt(accountType));
					//linksusWeiboService.updateSendWeiboCount(linksusWeibo);
					QueueDataSave.addDataToQueue(linksusWeibo, Constants.OPER_TYPE_UPDATE);
				}else if("2".equals(dataType)){
					//关注用户
					LinksusAttentionWeibo linksusAttentionWeibo = new LinksusAttentionWeibo();
					linksusAttentionWeibo.setCommentCount(dto.getComments());
					linksusAttentionWeibo.setRepostCount(dto.getReposts());
					linksusAttentionWeibo.setMid(dto.getId());
					linksusAttentionWeibo.setAccountType(Integer.parseInt(accountType));
					//linksusAttentionWeiboService.updateSendWeiboCount(linksusWeibo);
					QueueDataSave.addDataToQueue(linksusAttentionWeibo, Constants.OPER_TYPE_UPDATE);
				}else if("3".equals(dataType)){
					//微博池
					LinksusWeiboPool linksusWeiboPool = new LinksusWeiboPool();
					linksusWeiboPool.setCommentCount(dto.getComments());
					linksusWeiboPool.setRepostCount(dto.getReposts());
					linksusWeiboPool.setMid(dto.getId());
					linksusWeiboPool.setAccountType(Integer.parseInt(accountType));
					//linksusWeiboPoolService.updateLinksusWeiboPoolCount(linksusWeibo);
					QueueDataSave.addDataToQueue(linksusWeiboPool, Constants.OPER_TYPE_UPDATE);
				}
			}
		}
		return "success";
	}
}
