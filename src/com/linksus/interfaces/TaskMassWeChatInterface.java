package com.linksus.interfaces;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linksus.common.config.LoadConfig;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.HttpUtil;
import com.linksus.common.util.StringUtil;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.entity.LinksusWx;
import com.linksus.entity.LinksusWxMassGroup;
import com.linksus.service.LinksusWxMassGroupService;
import com.linksus.service.LinksusWxService;
import com.linksus.task.TaskSendWeiXin;

public class TaskMassWeChatInterface extends BaseInterface{

	private String jobType;

	public String getJobType(){
		return jobType;
	}

	public void setJobType(String jobType){
		this.jobType = jobType;
	}

	public Object cal(Map paramsMap) throws Exception{
		Object obj = null;
		TaskSendWeiXin sendWeiXin = new TaskSendWeiXin();
		if(jobType.equals("1")){
			obj = sendWeiXin.sendMassInfo(paramsMap);
			//Object obj = sendWeiXin.sendImmediateMassInfo(paramsMap);
		}else if(jobType.equals("2")){
			obj = deleteMassInfo(paramsMap);
		}
		return obj;
	}

	LinksusWxService wxService = (LinksusWxService) ContextUtil.getBean("linksusWxService");
	LinksusWxMassGroupService wxMassGroupService = (LinksusWxMassGroupService) ContextUtil
			.getBean("linksusWxMassGroupService");

	public String deleteMassInfo(Map paramsMap){
		Long id = (Long) paramsMap.get("wxId");
		LinksusWx wx = wxService.getLinksusWxById(id);
		if(wx == null || wx.getStatus() != 3){
			//获取access_token时AppSecret错误，或者access_token无效，或者消息状态不为已发送
			return "40001";
		}
		if(wx.getType() == 1 || wx.getType() == 2){
			List<LinksusWxMassGroup> massGroupList = wxMassGroupService.getLinksusWxMassGroupByWxId(id);
			for(int i = 0; i < massGroupList.size(); i++){
				LinksusWxMassGroup massGroup = massGroupList.get(i);
				Map parms = new HashMap();
				parms.put("massId", massGroup.getMsgId());
				parms.put("access_token", wx.getToken());
				String rsData = HttpUtil.postRequest(LoadConfig.getUrlEntity("WeiXinMassDelete"), parms);
				LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(rsData);
				if(error.getErrorCode() == 0){
					wx.setStatus(98);
					wx.setErrmsg("");
					wxService.updateWeiXinTaskstatusAndErrmsg(wx);
				}
			}

		}
		return null;
	}
}
