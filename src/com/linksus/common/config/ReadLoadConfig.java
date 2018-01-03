package com.linksus.common.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ReadLoadConfig {
public Map<String,InterfaceParams> interfaceMap =new HashMap<String,InterfaceParams>();
private static Long weiboId =0L;
public static Long getPrimaryKey(){
	Long key=0L;
	if(weiboId==0L){
		weiboId =weiboId+1;
		key =key+1;
	}else{
		key =weiboId+1;
		weiboId++;
	}
	return key;
	
}
public static void main(String[] args) {
	Long ss = ReadLoadConfig.getPrimaryKey();
	System.out.println(ss);
	Long ss1 =ReadLoadConfig.getPrimaryKey();
	System.out.println(ss1);
	Long ss2 =ReadLoadConfig.getPrimaryKey();
	System.out.println(ss2);
}

//加载接口配置文件
public Map<String,InterfaceParams> loadInterfaceCongfig(){
	SAXReader reader =new SAXReader();
	try {
			Document doc = reader.read(ReadLoadConfig.class.getClass()
					.getClassLoader()
					.getResource("config/interface-config.xml"));
		//获取element根节点目录 
		Element root =doc.getRootElement();
		//获取root节点下的ingterfaces节点
		Element configs =root.element("interfaces");
		for(Iterator iter=configs.elementIterator("interface");iter.hasNext();){
			Element config =(Element)iter.next();
			String taskType =config.elementText("taskType");
			String taskName =config.elementText("taskName");
			String className =config.elementText("className");
            String status =config.elementText("status");	
            Element params = config.element("params");
		}
	} catch (DocumentException e) {
		e.printStackTrace();
	}
	return interfaceMap;
}
}
