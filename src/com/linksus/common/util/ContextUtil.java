package com.linksus.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ContextUtil implements ApplicationContextAware{

	private static ApplicationContext context;
	private static ApplicationContext minaContext;

	/*
	 * private static ClassPathXmlApplicationContext context; static { //context
	 * = new ClassPathXmlApplicationContext("config/applicationContext-*.xml");
	 * context = new ClassPathXmlApplicationContext(new String[] {
	 * "config/applicationContext-mail.xml",
	 * "config/applicationContext-spring.xml" }); }
	 */

	public static Object getBean(String beanName){
		if(context == null){//本地测试使用
			context = new ClassPathXmlApplicationContext(new String[] { "config/applicationContext-mail.xml",
					"config/applicationContext-spring.xml" });
		}
		return context.getBean(beanName);
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException{
		ContextUtil.context = context;
	}

	public static Object getMinaClientBean(String beanName){
		if(minaContext == null){
			minaContext = new ClassPathXmlApplicationContext(new String[] { "config/mina-client.xml" });
		}
		return minaContext.getBean(beanName);
	}
}
