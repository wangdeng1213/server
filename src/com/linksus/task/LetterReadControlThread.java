package com.linksus.task;

import java.nio.channels.Selector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.util.LogUtil;

/**
 * ���߳����ڿ���NIO�̵߳�5�����ж���������
 * @author zhangew
 *
 */
public class LetterReadControlThread implements Runnable{

	protected static final Logger logger = LoggerFactory.getLogger(LetterReadControlThread.class);
	private static final int sleepSec = 300;//5�������ӶϿ�����
	private Selector selector;

	public LetterReadControlThread(Selector selector) {
		super();
		this.selector = selector;
	}

	@Override
	public void run(){
		try{
			Thread.sleep(sleepSec * 1000);
			logger.info("�ﵽ5��������ʱ������,���Ͽ����¿�ʼ����!");
			if(selector != null && selector.isOpen()){
				selector.close();
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
	}

}
