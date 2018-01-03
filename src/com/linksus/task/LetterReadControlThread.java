package com.linksus.task;

import java.nio.channels.Selector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.util.LogUtil;

/**
 * 本线程用于控制NIO线程的5分钟中断重连功能
 * @author zhangew
 *
 */
public class LetterReadControlThread implements Runnable{

	protected static final Logger logger = LoggerFactory.getLogger(LetterReadControlThread.class);
	private static final int sleepSec = 300;//5分钟连接断开重连
	private Selector selector;

	public LetterReadControlThread(Selector selector) {
		super();
		this.selector = selector;
	}

	@Override
	public void run(){
		try{
			Thread.sleep(sleepSec * 1000);
			logger.info("达到5分钟连接时间限制,将断开重新开始连接!");
			if(selector != null && selector.isOpen()){
				selector.close();
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
	}

}
