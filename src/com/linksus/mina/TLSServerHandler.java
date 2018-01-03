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
		if(Constants.REQUEST_TYPE_TASK_LIST.equals(requestType)){//�����������
			logger.info("----------->�ͻ���{}������������{}", ipAddr, taskType);
			BaseQueue queue = QueueFactory.getQueueInstance(taskType);
			if(queue == null){
				return;
			}
			Map rsData = queue.getTaskListFromQueue();
			TransferEntity rsEntity = new TransferEntity();
			rsEntity.setTaskType(taskType);
			rsEntity.setTransferData(rsData);
			logger.info("----------->���Ϳͻ���{}��������{}", ipAddr, taskType);
			session.write(rsEntity);
			return;
		}else if(Constants.REQUEST_TYPE_DATA_RETURN.equals(requestType)){//���ؿͻ��˽��
			Map data = entity.getTransferData();
			if(Constants.CLIENT_CONFIG_INFO.equals(taskType)){//ˢ�¿ͻ�������
				Map rsMap = LoadConfig.getClientConfigInfo();
				TransferEntity rsEntity = new TransferEntity();
				rsEntity.setTaskType(taskType);
				rsEntity.setTransferData(rsMap);
				session.write(rsEntity);
				logger.info("----------->��ͻ���{}������������", ipAddr);
				return;
			}
			if(Constants.CLIENT_EXCEPTION_DATA.equals(taskType)){//ȡ�ÿͻ����쳣
				logger.info("----------->ȡ�ÿͻ����쳣:{}", ipAddr);
				LogUtil.saveClientExcptionToDb(data, ipAddr);
				return;
			}
			if(Constants.CLIENT_STATISTIC_DATA.equals(taskType)){//ȡ�ÿͻ���ͳ������
				logger.info("----------->ȡ�ÿͻ���ͳ������:{}", ipAddr);
				TaskStatisticCommon.saveClientStatisticData(data, ipAddr);
				return;
			}
			if(Constants.CLIENT_LIMIT_TIME.equals(taskType)){//ȡ�ÿͻ���API����ʱ��
				logger.info("----------->ȡ�ÿͻ���API����ʱ��:{}", ipAddr);
				TaskStatisticCommon.saveClientLimitTime(data, ipAddr);
				return;
			}
			if(Constants.REMOTE_QUEUE_TASK.equals(taskType)){//���Զ�̶���
				QueueFactory.addQueueTaskData((String) data.get("queueType"), data.get("queueObj"));
				logger.debug("���Զ�̶���:{}", taskType);
				return;
			}
			BaseQueue queue = QueueFactory.getQueueInstance(taskType);
			if(queue == null){
				logger.info("----------->�ͻ���{}������������{},�������񲻴��ڻ����", ipAddr, taskType);
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