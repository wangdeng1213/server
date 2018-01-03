package com.linksus.interfaces;

import java.net.InetAddress;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.transport.socket.SocketAcceptor;

import com.linksus.common.config.LoadConfig;
import com.linksus.common.module.TaskStatisticCommon;
import com.linksus.common.util.CacheUtil;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.StringUtil;
import com.linksus.entity.LinksusTaskIncrementUser;
import com.linksus.mina.WhitelistFilter;
import com.linksus.queue.QueueFactory;
import com.linksus.service.LinksusTaskIncrementUserService;
import com.linksus.task.TaskQueueStatistic;

public class TaskServerToolInterface extends BaseInterface{

	private String toolType;

	public void setToolType(String toolType){
		this.toolType = toolType;
	}

	public String cal(Map paramsMap) throws Exception{
		String rs = "";
		if("1".equals(toolType)){
			LoadConfig.reload();
			rs = "$配置刷新完成!";
		}else if("2".equals(toolType)){
			TaskQueueStatistic taskStatis = new TaskQueueStatistic();
			taskStatis.emailTaskStatistic((String) paramsMap.get("email"));
			rs = "$任务统计邮件已发送!";
		}else if("3".equals(toolType)){
			CacheUtil.loadAllCache();
			rs = "$系统缓存刷新完成!";
		}else if("4".equals(toolType)){
			rs = "$" + TaskStatisticCommon.taskStatistic();
		}else if("5".equals(toolType)){
			SocketAcceptor acceptor = (SocketAcceptor) ContextUtil.getBean("ioAcceptorWithSSL");
			//获取默认过滤器
			DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
			WhitelistFilter whiteFilter = (WhitelistFilter) chain.get("whitelistFilter");
			if(!StringUtils.isBlank((String) paramsMap.get("add"))){//添加地址
				String ipAddr = (String) paramsMap.get("add");
				if(!StringUtil.ipCheck(ipAddr)){
					return "20037";
				}
				whiteFilter.allow(InetAddress.getByName(ipAddr));
			}
			if(!StringUtils.isBlank((String) paramsMap.get("remove"))){//移除地址
				String ipAddr = (String) paramsMap.get("remove");
				if(!StringUtil.ipCheck(ipAddr)){
					return "20037";
				}
				whiteFilter.disallow(InetAddress.getByName(ipAddr));
			}
			StringBuffer buff = new StringBuffer("$当前合法客户端IP地址为: ");
			List list = whiteFilter.getCurrentWhiteList();
			for(int i = 0; i < list.size(); i++){
				buff.append("[").append(list.get(i)).append("],");
			}
			buff.deleteCharAt(buff.length() - 1).append("<br>被拒绝的IP地址:");
			Set refusedIps = whiteFilter.getRefusedIps();
			for(Iterator itor = refusedIps.iterator(); itor.hasNext();){
				buff.append("[").append(itor.next()).append("],");
			}
			if(!refusedIps.isEmpty()){
				buff.deleteCharAt(buff.length() - 1);
			}
			return buff.toString();
		}else if("99".equals(toolType)){//测试接口
			String id = (String) paramsMap.get("pid");
			LinksusTaskIncrementUserService increUserService = (LinksusTaskIncrementUserService) ContextUtil
					.getBean("linksusTaskIncrementUserService");
			LinksusTaskIncrementUser user = increUserService.getTaskIncrementUserByAccountId(new Long(id));
			if(user != null){
				if(StringUtils.equals(user.getAppType().toString(), "1")){
					QueueFactory.addQueueTaskData("Queue011", user);
					QueueFactory.addQueueTaskData("Queue007", user);
				}else{
					QueueFactory.addQueueTaskData("Queue012", user);
					QueueFactory.addQueueTaskData("Queue008", user);
				}
			}
		}
		return rs;
	}
}
