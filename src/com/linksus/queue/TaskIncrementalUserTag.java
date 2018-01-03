package com.linksus.queue;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.module.UserTagCommon;
import com.linksus.common.module.WeiboUserCommon;

/**
 * 处理增量用户标签
 * Auth：wangdeng
 * */
public class TaskIncrementalUserTag extends BaseQueue{

	private String accountType;
	protected static final Logger logger = LoggerFactory.getLogger(WeiboUserCommon.class);

	public String getAccountType(){
		return accountType;
	}

	public void setAccountType(String accountType){
		this.accountType = accountType;
	}

	protected TaskIncrementalUserTag(String taskType) {
		super(taskType);
	}

	@Override
	protected List flushTaskQueue(){
		return null;
	}

	@Override
	protected String parseClientData(Map dataMap) throws Exception{
		if(!dataMap.isEmpty()){
			for(Iterator itor = dataMap.entrySet().iterator(); itor.hasNext();){
				Entry entry = (Entry) itor.next();
				Long userId = (Long) entry.getKey();
				Set tagSet = (Set) entry.getValue();
				if(!tagSet.isEmpty()){
					//直接向用户标签表中插入数据 查询出标签id 
					UserTagCommon.userTagInsert(tagSet, "1", userId.toString());
				}
			}
		}
		return "success";
	}
}
