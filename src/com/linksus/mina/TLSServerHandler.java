package com.linksus.mina;

import java.util.Map;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.Constants;
import com.linksus.common.config.LoadConfig;
import com.linksus.common.module.TaskStatisticCommon;
import com.linksus.common.util.LogUtil;
import com.linksus.queue.BaseQueue;
import com.linksus.queue.QueueFactory;

public class TLSServerHandler extends IoHandlerAdapter{

	protected static final Logger logger = LoggerFactory.getLogger(TLSServerHandler.class);

	public void messageReceived(IoSession session,Object message) throws Exception{
		if(!"1".equals(LoadConfig.getString("queueServer"))){
			return;
		}
		String ipAddr = getIpAddrFromSession(session);
		TransferEntity entity = (TransferEntity) message;
		String taskType = entity.getTaskType();
		String requestType = entity.getRequestType();
		if(Constants.REQUEST_TYPE_TASK_LIST.equals(requestType)){//请求任务队列
			logger.info("----------->客户端{}请求任务数据{}", ipAddr, taskType);
			BaseQueue queue = QueueFactory.getQueueInstance(taskType);
			if(queue == null){
				return;
			}
			Map rsData = queue.getTaskListFromQueue();
			TransferEntity rsEntity = new TransferEntity();
			rsEntity.setTaskType(taskType);
			rsEntity.setTransferData(rsData);
			logger.info("----------->发送客户端{}任务数据{}", ipAddr, taskType);
			session.write(rsEntity);
			return;
		}else if(Constants.REQUEST_TYPE_DATA_RETURN.equals(requestType)){//返回客户端结果
			Map data = entity.getTransferData();
			if(Constants.CLIENT_CONFIG_INFO.equals(taskType)){//刷新客户端配置
				Map rsMap = LoadConfig.getClientConfigInfo();
				TransferEntity rsEntity = new TransferEntity();
				rsEntity.setTaskType(taskType);
				rsEntity.setTransferData(rsMap);
				session.write(rsEntity);
				logger.info("----------->向客户端{}发送配置数据", ipAddr);
				return;
			}
			if(Constants.CLIENT_EXCEPTION_DATA.equals(taskType)){//取得客户端异常
				logger.info("----------->取得客户端异常:{}", ipAddr);
				LogUtil.saveClientExcptionToDb(data, ipAddr);
				return;
			}
			if(Constants.CLIENT_STATISTIC_DATA.equals(taskType)){//取得客户端统计数据
				logger.info("----------->取得客户端统计数据:{}", ipAddr);
				TaskStatisticCommon.saveClientStatisticData(data, ipAddr);
				return;
			}
			if(Constants.CLIENT_LIMIT_TIME.equals(taskType)){//取得客户端API超限时间
				logger.info("----------->取得客户端API超限时间:{}", ipAddr);
				TaskStatisticCommon.saveClientLimitTime(data, ipAddr);
				return;
			}
			if(Constants.REMOTE_QUEUE_TASK.equals(taskType)){//添加远程队列
				QueueFactory.addQueueTaskData((String) data.get("queueType"), data.get("queueObj"));
				logger.debug("添加远程队列:{}", taskType);
				return;
			}
			BaseQueue queue = QueueFactory.getQueueInstance(taskType);
			if(queue == null){
				logger.info("----------->客户端{}返回任务数据{},队列任务不存在或禁用", ipAddr, taskType);
				return;
			}
			queue.respondClientData(data);
		}
	}

	private String getIpAddrFromSession(IoSession session){
		String ipAddr = null;
		if(session.getRemoteAddress() != null){
			ipAddr = session.getRemoteAddress().toString().substring(1);
			ipAddr = ipAddr.substring(0, ipAddr.indexOf(":"));
		}
		return ipAddr;
	}

	public void sessionCreated(IoSession session) throws Exception{
	}

	public void sessionOpened(IoSession session) throws Exception{
	}

	public void sessionClosed(IoSession session) throws Exception{
	}

	public void sessionIdle(IoSession session,IdleStatus status) throws Exception{
	}

	public void exceptionCaught(IoSession session,Throwable cause) throws Exception{
		LogUtil.saveException(logger, cause);
	}

	public void messageSent(IoSession session,Object message) throws Exception{
	}
}