package com.linksus.task;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.PrimaryKeyGen;
import com.linksus.entity.LinksusTaskSytimeFlag;
import com.linksus.entity.LinksusWeibo;
import com.linksus.entity.LinksusWeiboTopic;
import com.linksus.service.LinksusTaskSytimeFlagService;
import com.linksus.service.LinksusWeiboService;
import com.linksus.service.LinksusWeiboTopicService;

public class TaskTopicStatistic extends BaseTask{

	protected static final Logger logger = LoggerFactory.getLogger(TaskTopicStatistic.class);

	//判断话题正则表达式
	public String JUDGE_TOPIC = "#(.*?)#";

	LinksusWeiboService linksusWeiboService = (LinksusWeiboService) ContextUtil.getBean("linksusWeiboService");
	LinksusWeiboTopicService linksusWeiboTopicService = (LinksusWeiboTopicService) ContextUtil
			.getBean("linksusWeiboTopicService");
	LinksusTaskSytimeFlagService linksusTaskSytimeFlagService = (LinksusTaskSytimeFlagService) ContextUtil
			.getBean("linksusTaskSytimeFlagService");

	public static final void main(String[] args){
		//		TaskTopicStatistic tic = new TaskTopicStatistic();
		//		tic.updateWeiboInfoToTopic();
		//		
		//		System.exit(0);

	}

	@Override
	public void cal(){
		int taskSize = 0;
		try{
			taskSize = updateWeiboInfoToTopic();
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
		this.setTaskListEndTime(taskSize);
	}

	//判断微博内容划分话题，根据账户统计话题数
	public int updateWeiboInfoToTopic() throws Exception{

		//读取微博表内容
		List<LinksusWeibo> weibos = null;
		int firstReadTime = 0;
		if(linksusTaskSytimeFlagService.getWeiboTopicLastTime(1) != null){
			firstReadTime = linksusTaskSytimeFlagService.getWeiboTopicLastTime(1).getLastSendTime();
		}
		//第一次读取
		if(firstReadTime == 0){
			weibos = linksusWeiboService.getLinksusWeiboHasSendSuccess();
		}else{
			weibos = linksusWeiboService.getLinksusWeiboHasSendSuccessCon(firstReadTime);
		}

		List<LinksusWeiboTopic> topics = new ArrayList<LinksusWeiboTopic>();

		if(weibos != null && weibos.size() > 0){
			for(int i = 0; i < weibos.size(); i++){
				LinksusWeibo weibo = weibos.get(i);
				List<String> weiboInTopics = new ArrayList<String>();

				//取微博内容、账户、发送时间
				String content = weibo.getContent();
				long institutionId = weibo.getInstitutionId();
				int createdTime = weibo.getSendTime();

				//查看微博内容中的话题
				if(StringUtils.isNotBlank(content)){
					Pattern topicPattern = Pattern.compile(JUDGE_TOPIC);
					Matcher topicMatcher = topicPattern.matcher(content);
					while (topicMatcher.find()){
						weiboInTopics.add(topicMatcher.group(1));
					}
				}
				//通过话题内容添加话题列表
				if(weiboInTopics != null && weiboInTopics.size() != 0){
					for(int k = 0; k < weiboInTopics.size(); k++){
						String topicContent = weiboInTopics.get(k);
						//查看话题列表topics中对象是否存在，如果存在话题总数加1，否则添加新对象
						//第一次循环添加话题信息
						if(topics.size() == 0){
							LinksusWeiboTopic newTopic = new LinksusWeiboTopic();
							newTopic.setTopicName(topicContent);
							newTopic.setInstitutionId(institutionId);
							newTopic.setUsedNum(1);
							newTopic.setCreatedTime(createdTime);
							topics.add(newTopic);
						}
						for(int m = 0; m < topics.size(); m++){
							LinksusWeiboTopic topic = topics.get(m);
							if(topic.getTopicName().equals(topicContent) && topic.getInstitutionId() == institutionId){
								int count = topic.getUsedNum();
								topic.setLastUsedTime(createdTime);
								topic.setUsedNum(count + 1);
							}else{
								LinksusWeiboTopic newTopic = new LinksusWeiboTopic();
								newTopic.setTopicName(topicContent);
								newTopic.setInstitutionId(institutionId);
								newTopic.setUsedNum(1);
								newTopic.setCreatedTime(createdTime);
								topics.add(newTopic);
							}
						}
					}
				}

				//循环最后一次时，将微博发送时间插入标识表中.第一次时，插入否则更新
				if(i == (weibos.size() - 1)){
					LinksusTaskSytimeFlag taskSytimeFlag = new LinksusTaskSytimeFlag();
					taskSytimeFlag.setTaskType(1);
					taskSytimeFlag.setLastSendTime(createdTime);
					if(firstReadTime == 0){
						linksusTaskSytimeFlagService.insertWeiboTopicLastTime(taskSytimeFlag);
					}else{
						linksusTaskSytimeFlagService.updateWeiboTopicLastTime(taskSytimeFlag);
					}
				}
			}
		}

		//话题汇总内容入数据库
		if(topics.size() != 0){
			for(int j = 0; j < topics.size(); j++){
				//查询话题表中账户是否发表此话题
				LinksusWeiboTopic topic = topics.get(j);
				LinksusWeiboTopic istopics = linksusWeiboTopicService.getIsLinksusWeiboTopic(topic);

				//存在更新话题总数，否则插入新账户及话题
				if(istopics != null && istopics.getId() != null){//加判断id != 0
					istopics.setUsedNum(istopics.getUsedNum() + topic.getUsedNum());
					linksusWeiboTopicService.updateLinksusWeiboTopicByUsedNum(istopics);
				}
				if(istopics == null){
					//获取自动生成话题id
					long topicid = PrimaryKeyGen.getPrimaryKey("linksus_appaccount", "id");
					topic.setId(topicid);
					linksusWeiboTopicService.insertintoLinksusWeiboTopic(topic);
				}
			}
		}
		if(weibos != null){
			return weibos.size();
		}else{
			return 0;
		}
	}

}
