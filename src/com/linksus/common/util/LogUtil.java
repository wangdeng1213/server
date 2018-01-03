package com.linksus.common.util;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.Constants;
import com.linksus.common.config.LoadConfig;
import com.linksus.entity.LinksusTaskException;
import com.linksus.service.LinksusTaskExceptionService;

public class LogUtil{

	protected static final Logger log = LoggerFactory.getLogger(LogUtil.class);
	private static LinksusTaskExceptionService exceptionService = (LinksusTaskExceptionService) ContextUtil
			.getBean("linksusTaskExceptionService");
	private static int logRows = 50;
	private static int logSize = 2000;
	//�������
	protected static CacheUtil cache = CacheUtil.getInstance();

	/**
	 * ��־�ļ���¼�쳣��ջ��Ϣ �����쳣��Ϣ�����ݿ�
	 * @param logger
	 * @param exception
	 */
	public static void saveException(Logger logger,Throwable exception){
		try{
			String errmsg = getExceptionStackMsg(exception);
			logger.error(errmsg);
			if("1".equals(LoadConfig.getString("saveExceptionToDb"))){
				String ipAppr = (String) cache.getCache(Constants.CACHE_LOCAL_IP_ADDR);
				saveExcptionToDb(ipAppr, errmsg, Constants.TASK_EXCPTION_TYPE_SERVER, DateUtil.getUnixDate(new Date()));
			}
		}catch (Exception e){
			log.error(getExceptionStackMsg(e));
		}
	}

	/**
	 * ȡ���쳣��ջ��Ϣ
	 * @param exception
	 * @return
	 */
	public static String getExceptionStackMsg(Throwable exception){
		String errmsg = "";
		if(exception != null){
			StringBuffer buffer = new StringBuffer();
			StackTraceElement[] stem = exception.getStackTrace();
			for(int i = 0; i < stem.length; i++){
				buffer.append(stem[i].toString() + System.getProperty("line.separator"));
				if(i > logRows){
					break;
				}
			}
			buffer.insert(0, "�����쳣:" + exception.getMessage() + "  ");
			errmsg = buffer.toString();
			if(errmsg.getBytes().length > logSize){
				errmsg = new String(errmsg.getBytes(), 0, logSize - 1);
			}
		}
		return errmsg;
	}

	/**
	 * ���������쳣��Ϣ
	 * @param ipAddr
	 * @param content
	 * @param type
	 * @param addTime
	 */
	public static void saveExcptionToDb(String ipAddr,String content,int type,int addTime){
		try{
			Long pid = PrimaryKeyGen.getPrimaryKey("linksus_task_exception", "pid");
			LinksusTaskException taskException = new LinksusTaskException();
			taskException.setPid(pid);
			taskException.setSourceIp(ipAddr);
			taskException.setType(type);
			taskException.setContent(content);
			taskException.setAddTime(addTime);
			exceptionService.insertLinksusTaskException(taskException);
		}catch (Exception e){
			log.error(getExceptionStackMsg(e));
		}
	}

	/**
	 * ����ͻ��˷��͵��쳣��Ϣ
	 * @param jsonContent
	 * @param ipAddr
	 */
	public static void saveClientExcptionToDb(Map dataMap,String ipAddr){
		try{
			LinksusTaskException exception = (LinksusTaskException) dataMap.get("clientData");
			Long pid = PrimaryKeyGen.getPrimaryKey("linksus_task_exception", "pid");
			exception.setPid(pid);
			exception.setSourceIp(ipAddr);
			exception.setType(1);
			exceptionService.insertLinksusTaskException(exception);
		}catch (Exception e){
			log.error(getExceptionStackMsg(e));
		}
	}
}
