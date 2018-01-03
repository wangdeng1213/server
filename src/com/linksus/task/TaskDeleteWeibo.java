package com.linksus.task;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.linksus.common.Constants;
import com.linksus.common.config.LoadConfig;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.DateUtil;
import com.linksus.common.util.HttpUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.StringUtil;
import com.linksus.entity.LinksusRemoveEvent;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.entity.UrlEntity;
import com.linksus.service.CommonService;
import com.linksus.service.LinksusRemoveEventService;

public class TaskDeleteWeibo extends BaseTask{

	private LinksusRemoveEventService removeService = (LinksusRemoveEventService) ContextUtil
			.getBean("linksusRemoveEventService");
	private CommonService commonService = (CommonService) ContextUtil.getBean("commonService");

	@Override
	public void cal(){
		LinksusRemoveEvent removeEvent = new LinksusRemoveEvent();
		int startCount = (currentPage - 1) * pageSize;
		removeEvent.setPageSize(pageSize);
		removeEvent.setStartCount(startCount);
		removeEvent.setType(accountType);
		List<LinksusRemoveEvent> list = removeService.getRemoveWeiboList(removeEvent);
		for(LinksusRemoveEvent event : list){
			removeWeibo(event);
		}
		checkTaskListEnd(list);//判断任务是否轮询完成
	}

	/**
	 * 根据ID删除微博,前台接口使用
	 * @param id
	 * @return
	 */
	public String removeWeiboById(String id){
		LinksusRemoveEvent event = removeService.getLinksusRemoveEventById(new Long(id));
		if(event == null){
			return "10203";
		}
		if(!"0".equals(event.getStatus() + "")){
			return "10201";
		}
		return removeWeibo(event);
	}

	/**
	 * 删除单条微博记录
	 * @param event
	 * @return
	 */
	private String removeWeibo(LinksusRemoveEvent event){
		String rsData = "";
		boolean hasError = false;
		Long mid = event.getMid();
		LinksusTaskErrorCode error = null;
		if("1".equals(event.getType() + "")){//新浪
			UrlEntity url = LoadConfig.getUrlEntity("deleteWeibo");
			Map paramMap = new HashMap();
			paramMap.put("access_token", event.getToken());
			paramMap.put("id", mid);
			rsData = HttpUtil.postRequest(url, paramMap);
			error = StringUtil.parseSinaErrorCode(rsData);
			if(error != null){//删除错误
				hasError = true;
			}
		}else if("2".equals(event.getType() + "")){//腾讯
			UrlEntity url = LoadConfig.getUrlEntity("TCDeleteWeibo");
			Map paramMap = new HashMap();
			paramMap.put("access_token", event.getToken());
			paramMap.put("oauth_consumer_key", cache.getAppInfo(Constants.CACHE_TENCENT_APPINFO).getAppKey());
			paramMap.put("openid", event.getAppid());
			paramMap.put("clientip", Constants.TENCENT_CLIENT_IP);
			paramMap.put("oauth_version", "2.a");
			paramMap.put("format", "json");
			paramMap.put("id", mid);
			rsData = HttpUtil.postRequest(url, paramMap);
			error = StringUtil.parseTencentErrorCode(rsData);
			if(error != null){
				hasError = true;
			}
		}
		if(hasError){
			//修改执行状态
			LinksusRemoveEvent updtEvent = new LinksusRemoveEvent();
			updtEvent.setId(event.getId());
			updtEvent.setStatus(2);
			updtEvent.setOperateTime(DateUtil.getUnixDate(new Date()));
			removeService.updateRemoveEventStatus(updtEvent);
			return error.getErrorCode().toString();
		}
		//执行后处理命令
		String execCmd = event.getExecCmd();
		int updtCount = 0;
		try{
			if(!StringUtils.isBlank(execCmd)){
				updtCount = commonService.execUpdateSql(execCmd);
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
		//修改执行状态
		LinksusRemoveEvent updtEvent = new LinksusRemoveEvent();
		updtEvent.setId(event.getId());
		if(updtCount > 0){
			updtEvent.setStatus(1);
		}else{
			updtEvent.setStatus(2);//后处理SQL未更新到数据
		}
		updtEvent.setOperateTime(DateUtil.getUnixDate(new Date()));
		removeService.updateRemoveEventStatus(updtEvent);
		return "";
	}

	public static void main(String[] args){
		TaskDeleteWeibo weibo = new TaskDeleteWeibo();
		weibo.setAccountType("2");
		weibo.cal();
		/*
		 * String execCmd="update linksus_weibo set STATUS=98 where id=2040";
		 * CommonService
		 * commonService=(CommonService)ContextUtil.getBean("commonService");
		 * int i=commonService.execUpdateSql(execCmd); System.out.println(i);
		 */
	}
}
