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
	 * 	�ظ�����
	 * 	�ظ�˽�� 
	 * 	��ʾ����
	*/
	public Object cal(Map paramsMap) throws Exception{
		GovTaskComment task = new GovTaskComment();
		GovTaskSendWeibo weiboTask = new GovTaskSendWeibo();
		String errorCode = "";
		//�ظ�����
		if(jobType.equals("1")){
			errorCode = task.replyPingLun(paramsMap);
			//�ظ�˽��
		}else if(jobType.equals("2")){
			return task.publishMsg(paramsMap);//Map
			//������ʾ
		}else if(jobType.equals("3")){
			errorCode = weiboTask.sendImmediate(paramsMap);
		}
		return errorCode;
	}
}
