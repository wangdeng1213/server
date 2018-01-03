package com.linksus.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.linksus.common.util.ContextUtil;
import com.linksus.entity.LinksusTypeAreaCode;
import com.linksus.service.LinksusTypeAreaCodeService;

public class localhost{

	LinksusTypeAreaCodeService linksusTypeAreaCodeService = (LinksusTypeAreaCodeService) ContextUtil
			.getBean("linksusTypeAreaCodeService");

	public void run(){
		//localhost ll = new localhost();
		List<LinksusTypeAreaCode> allCount = new ArrayList<LinksusTypeAreaCode>();
		SAXReader reader = new SAXReader();
		try{
			Document document = reader.read(new File("D:\\LocList1.xml"));
			Element root = document.getRootElement(); // 获取根节点
			int countynum = 0;
			long first = 0;
			for(Iterator iter = root.elementIterator(); iter.hasNext();){
				Element element = (Element) iter.next();
				countynum++;
				first++;
				long county = 0;
				Attribute ageAttr = element.attribute("Name");
				Attribute Code = element.attribute("Code");
				LinksusTypeAreaCode code = new LinksusTypeAreaCode();
				code.setAreaCode(first);
				code.setParentCode(county);
				code.setAreaName(ageAttr.getValue());
				code.setSinaAreaCode(Code.getValue());
				code.setTencentAreaCode(Code.getValue());
				code.setOrdervalue(countynum);
				allCount.add(code);
				long parentCode1 = first;
				int stateNum = 0;
				for(Iterator iterInner = element.elementIterator(); iterInner.hasNext();){
					Element elementInner = (Element) iterInner.next();
					Attribute StateName = elementInner.attribute("Name");
					Attribute StateCode = elementInner.attribute("Code");
					if(StateName != null && StateCode != null){
						LinksusTypeAreaCode code1 = new LinksusTypeAreaCode();
						code1.setParentCode(parentCode1);
						stateNum++;
						first++;
						code1.setAreaCode(first);
						code1.setAreaName(StateName.getValue());
						code1.setSinaAreaCode(Code.getValue() + "-" + StateCode.getValue());
						code1.setTencentAreaCode(Code.getValue() + "-" + StateCode.getValue());
						code1.setOrdervalue(stateNum);
						allCount.add(code1);
					}
					long parentCode2 = first;
					int cityNum = 0;
					for(Iterator inInner = elementInner.elementIterator(); inInner.hasNext();){
						Element elementINInner = (Element) inInner.next();
						Attribute cityName = elementINInner.attribute("Name");
						Attribute cityCode = elementINInner.attribute("Code");

						if(cityName != null && cityCode != null){
							LinksusTypeAreaCode code2 = new LinksusTypeAreaCode();
							code2.setParentCode(parentCode2);
							first++;
							cityNum++;
							code2.setAreaCode(first);
							code2.setAreaName(cityName.getValue());
							code2.setOrdervalue(cityNum);
							if(StateCode != null){
								code2.setSinaAreaCode(Code.getValue() + "-" + StateCode.getValue() + "-"
										+ cityCode.getValue());
								code2.setTencentAreaCode(Code.getValue() + "-" + StateCode.getValue() + "-"
										+ cityCode.getValue());
							}else{
								code2.setSinaAreaCode(Code.getValue() + "-" + cityCode.getValue());
								code2.setTencentAreaCode(Code.getValue() + "-" + cityCode.getValue());
							}
							allCount.add(code2);
						}
					}
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		linksusTypeAreaCodeService.replaceLinksusTypeAreaCodeInfo(allCount);
		//	for(LinksusTypeAreaCode areaCode : allCount){
		//		
		//		System.out.println(areaCode.getAreaCode() + "|" + areaCode.getParentCode() + "|" + areaCode.getAreaName()
		//				+ "|" + areaCode.getSinaAreaCode() + "|" + areaCode.getOrdervalue());
		//	}
	}

	public static void main(String[] args){
		//localhost ll = new localhost();
		//ll.run();
		//		CacheUtil cache=CacheUtil.getInstance();
		//		System.out.println("------------------------" + cache.getSinaAreaCode("CHE-JU"));
	}
}
