package com.linksus.task;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.DateUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.entity.LinksusWx;
import com.linksus.service.LinksusWxService;

public class SendMassWeChatTimer extends TaskSendWeiXin implements Job{

	LinksusWxService linksusWxService = (LinksusWxService) ContextUtil.getBean("linksusWxService");

	protected static final Logger logger = LoggerFactory.getLogger(SendMassWeChatTimer.class);

	public void execute(JobExecutionContext arg0) throws JobExecutionException{
		JobDataMap map = arg0.getJobDetail().getJobDataMap();// 取参数
		Long pid = map.getLong("pid");// 微信id主键
		//Long groupId = map.getLong("groupId");
		LinksusWx wx = linksusWxService.getLinksusWxById(pid);
		boolean flag = false;
		String errcode = "";
		if(wx != null){
			//if(wx.getSendTime() < DateUtil.getUnixDate(new Date())){
			if(wx.getStatus().intValue() == 1 && wx.getApplyStatus().equals("11")){
				try{
					sendMessWx(wx);
				}catch (Exception e){
					LogUtil.saveException(logger, e);
					e.printStackTrace();
				}
			}else{
				flag = true;
				errcode = "20043";
				logger.error("该群发微信状态不是待发布状态,不能发布:currentstatus={}", wx.getStatus());
			}
		}else{
			flag = true;
			errcode = "10132";
		}
		//}else{
		//	flag = true;
		//	errcode = "10203";
		//	logger.error("定时群发微信发布失败,该微信不存在:pid={}", pid);
		//}
		if(flag){
			LinksusWx entity = new LinksusWx();
			entity.setId(wx.getId());
			entity.setStatus(2);
			String errmsg = "";
			LinksusTaskErrorCode error = cache.getErrorCode(errcode);
			if(error != null){
				errmsg = error.getErrorMsg();
			}
			entity.setErrmsg(errmsg);
			linksusWxService.updateWeiXinTaskstatusAndErrmsg(entity);
		}
	}
}
