package com.linksus.interfaces;

import java.util.Map;

import com.linksus.task.TaskInterfaceComment;

public class TaskCommentInterface extends BaseInterface{

	private String jobType;

	public String getJobType(){
		return jobType;
	}

	public void setJobType(String jobType){
		this.jobType = jobType;
	}

	/** 15删除新浪微博评论 
	 * 	18通过微博mid和类型查询评论列表 
	 * 	20 前台评论发布接口/进行常用回复统计(微博评论) 
	 * 	21 前台评论发布接口(私信) 
	 * 	22 前台评论发布接口(微信)
	 *  25前台微博评论重发接口
	 *  27微信重发接口
	 *  28私信重发接口
	*/
	public Object cal(Map paramsMap) throws Exception{
		TaskInterfaceComment task = new TaskInterfaceComment();
		String errorCode = "";
		if(jobType.equals("15")){
			errorCode = task.removeWeiboComment(paramsMap);
		}else if(jobType.equals("18")){
			return task.getInteractWeiboListByMid(paramsMap);//Map
		}else if(jobType.equals("20")){
			errorCode = task.publishComment(paramsMap);
		}else if(jobType.equals("21")){
			errorCode = task.publishMsg(paramsMap);
		}else if(jobType.equals("22")){
			Integer msgType = (Integer) paramsMap.get("msgType");
			if(msgType == 1 || msgType == 2 || msgType == 3){
				errorCode = task.publishWeixin(paramsMap);
			}else{
				errorCode = "20027";
			}
		}else if(jobType.equals("25")){
			errorCode = task.againReplyComment(paramsMap);
		}else if(jobType.equals("27")){
			errorCode = task.againReplyWeixin(paramsMap);
		}else if(jobType.equals("28")){
			errorCode = task.againReplyMessage(paramsMap);
		}
		return errorCode;
	}
}
