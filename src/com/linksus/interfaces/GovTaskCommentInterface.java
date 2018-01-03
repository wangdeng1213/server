package com.linksus.interfaces;

import java.util.Map;

import com.linksus.task.GovTaskComment;
import com.linksus.task.GovTaskSendWeibo;

public class GovTaskCommentInterface extends BaseInterface{

	private String jobType;

	public String getJobType(){
		return jobType;
	}

	public void setJobType(String jobType){
		this.jobType = jobType;
	}

	/**  
	 * 	回复评论
	 * 	回复私信 
	 * 	公示发布
	*/
	public Object cal(Map paramsMap) throws Exception{
		GovTaskComment task = new GovTaskComment();
		GovTaskSendWeibo weiboTask = new GovTaskSendWeibo();
		String errorCode = "";
		//回复评论
		if(jobType.equals("1")){
			errorCode = task.replyPingLun(paramsMap);
			//回复私信
		}else if(jobType.equals("2")){
			return task.publishMsg(paramsMap);//Map
			//发布公示
		}else if(jobType.equals("3")){
			errorCode = weiboTask.sendImmediate(paramsMap);
		}
		return errorCode;
	}
}
