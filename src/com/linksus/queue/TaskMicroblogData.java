package com.linksus.queue;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.linksus.common.config.LoadConfig;
import com.linksus.common.util.ContextUtil;
import com.linksus.data.QueueDataSave;
import com.linksus.entity.LinksusRelationWeibouser;
import com.linksus.service.LinksusRelationWeibouserService;

/**
 * 处理每隔指定天数抓取一次粉丝微博任务的响应数据
 * @author wutingfang
 *
 */
public class TaskMicroblogData extends BaseQueue{

	/** 账号类型 */
	private String accountType;
	/** 更新粉丝列表索引标识位*/
	private Long lastUserId =0L;

	private LinksusRelationWeibouserService relationWeiboUserService = (LinksusRelationWeibouserService) ContextUtil
			.getBean("linksusRelationWeibouserService");

	/**
	 * 带参构造器
	 * @param taskType 任务类型
	 */
	protected TaskMicroblogData(String taskType) {
		super(taskType);
	}

	/**
	 * 刷新数据队列
	 * 
	 */
	protected List flushTaskQueue(){
		LinksusRelationWeibouser weibouser = new LinksusRelationWeibouser();
		weibouser.setPageSize(pageSize);
		//设置用户类型  1,新浪 2,腾讯
		weibouser.setUserType(new Integer(accountType));
		//获取更新粉丝微博的频率天数
		String readSinaWeiboFrequencyDay = LoadConfig.getString("readWeiboFrequencyDay");
		Calendar calendar = Calendar.getInstance();
		//将当期时间减去配置的频率天数等于指定天数以前的时间点，查询时把该时间点与数据的微博最后更新时间对比，
		//如果比数据库中的时间还晚，说明该粉丝在指定的天数期间信息未修改过
		calendar.add(Calendar.DAY_OF_MONTH, -Integer.valueOf(readSinaWeiboFrequencyDay));
		Integer weiboLastSytime = Integer.parseInt(String.valueOf(calendar.getTime().getTime() / 1000));
		weibouser.setWeiboLastSytime(weiboLastSytime);
		weibouser.setLastUserId(lastUserId);
		List<LinksusRelationWeibouser> weiboUserlist = relationWeiboUserService
				.getLinksusWeibouserListByLimitDay(weibouser);
		if(weiboUserlist.size() < pageSize){
			currentPage = 1;
			setTaskFinishFlag();
			this.lastUserId = 0L;
		}else{
			currentPage++;
			this.lastUserId = weiboUserlist.get(weiboUserlist.size() - 1).getUserId();
		}
		return weiboUserlist;
	}

	/**
	 * 根据客户端返回的用户信息，更新粉丝表
	 * @param rsData 客户端返回的数据
	 */
	@SuppressWarnings("unchecked")
	protected String parseClientData(Map dataMap) throws Exception{
		LinksusRelationWeibouser weiboUser = (LinksusRelationWeibouser) dataMap.get("weiboUser");
		Integer currentTime = Integer.valueOf(String.valueOf(new Date().getTime() / 1000));
		//更新其微博最后同步时间为当前时间
		weiboUser.setWeiboLastSytime(currentTime);
		QueueDataSave.addDataToQueue(weiboUser, "updatePagetimeAndMid");
		return "success";
	}

	public void setAccountType(String accountType){
		this.accountType = accountType;
	}

}
