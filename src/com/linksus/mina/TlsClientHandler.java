package com.linksus.mina;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.util.LogUtil;

/**
 * MINA数据处理
 * @author zhangew
 *
 */
public class TlsClientHandler extends IoHandlerAdapter{

	protected static final Logger logger = LoggerFactory.getLogger(TlsClientHandler.class);

	/**
	 * 接收消息
	 */
	public void messageReceived(IoSession session,Object message) throws Exception{
		try{
			//TransferEntity entity = (TransferEntity) message;
			logger.info(">>>received data");
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
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
