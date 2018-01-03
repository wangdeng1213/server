package com.linksus.common.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.jxls.reader.ReaderBuilder;
import net.sf.jxls.reader.ReaderConfig;
import net.sf.jxls.reader.XLSReadStatus;
import net.sf.jxls.reader.XLSReader;
import net.sf.jxls.transformer.XLSTransformer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelJxlsUtils{

	protected static final Logger logger = LoggerFactory.getLogger(ExcelJxlsUtils.class);

	public static File getExcelFile(String templateFileName,List reportList,HttpServletResponse response,
			String itemName,Integer sequence) throws Exception{
		String templatePath = "d:\\" + File.separator + "template" + File.separator + templateFileName;
		String excelPath = "d:\\";
		// 生成excel文件
		XLSTransformer transformer = new XLSTransformer();
		Map beans = new HashMap();
		beans.put("subList", reportList);
		String tempFilePath = excelPath + File.separator + itemName
				+ (sequence == null ? System.currentTimeMillis() : sequence.longValue()) + ".xls";
		File file_temp = new File(tempFilePath);
		transformer.transformXLS(templatePath, beans, tempFilePath);
		return file_temp;
	}

	// 读取XLS文件
	public List readExcelFile(InputStream inputXLS) throws Exception{
		List lstRst = new ArrayList();
		try{
			ReaderConfig.getInstance().setSkipErrors(true);

			InputStream inputXML = new BufferedInputStream(this.getClass().getResourceAsStream(
					"/mdl_AccountBalance.xml"));
			XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML);

			List mdlAccountBalances = new ArrayList();
			Map beans = new HashMap();
			beans.put("mdlAccountBalances", mdlAccountBalances);
			XLSReadStatus readStatus = mainReader.read(inputXLS, beans);
			inputXLS.close();
			lstRst.add(readStatus);
			lstRst.addAll(mdlAccountBalances);
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
		return lstRst;
	}

	// 读取XLS文件
	public List readExcelFile(InputStream inputfilestream,String xmlConfig) throws Exception{
		List personInfoList = new ArrayList();
		try{
			ReaderConfig.getInstance().setSkipErrors(true);
			BufferedInputStream inputXML = new BufferedInputStream(new FileInputStream(new File(xmlConfig)));
			BufferedInputStream inputXLS = new BufferedInputStream(inputfilestream);
			XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML);
			Map beans = new HashMap();
			beans.put("personInfoList", personInfoList);
			mainReader.read(inputXLS, beans);
			inputXLS.close();
			//			lstRst.add(readStatus);
			//			lstRst.addAll(personInfoList);
			for(int i = 0; i < personInfoList.size(); i++){
				personInfoList.get(i);
			}

		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
		return personInfoList;
	}

	// 读取XLS文件
	public List readExcelFile(String dataXLS,String xmlConfig) throws Exception{
		List personInfoList = new ArrayList();
		try{
			ReaderConfig.getInstance().setSkipErrors(true);
			BufferedInputStream inputXML = new BufferedInputStream(new FileInputStream(new File(xmlConfig)));
			BufferedInputStream inputXLS = new BufferedInputStream(new FileInputStream(new File(dataXLS)));
			XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML);
			Map beans = new HashMap();
			beans.put("personInfoList", personInfoList);
			mainReader.read(inputXLS, beans);
			inputXLS.close();
			//			lstRst.add(readStatus);
			//			lstRst.addAll(personInfoList);
			for(int i = 0; i < personInfoList.size(); i++){
				personInfoList.get(i);
			}

			String claName = "com.linksus.entity.PersonInfo";
			System.err.println("claName=" + claName);

		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
		return personInfoList;
	}

	// 读取XLS文件
	public List readExcel(String dataXLS,String xmlConfig) throws Exception{
		List lstRst = new ArrayList();
		try{
			InputStream inputXML = new BufferedInputStream(getClass().getResourceAsStream(xmlConfig));
			XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML);
			InputStream inputXLS = new BufferedInputStream(getClass().getResourceAsStream(dataXLS));
			// Department department = new Department();
			// Department hrDepartment = new Department();
			List departments = new ArrayList();
			Map beans = new HashMap();
			// beans.put("department", department);
			// beans.put("hrDepartment", hrDepartment);
			beans.put("departments", departments);
			XLSReadStatus readStatus = mainReader.read(inputXLS, beans);
			// inputXLS.close();
			lstRst.add(readStatus);
			// lstRst.addAll(mdlAccountBalances);
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
		return lstRst;
	}

	public static void main(String[] args) throws Exception{
		ExcelJxlsUtils exj = new ExcelJxlsUtils();
		String dataXLS = "d:\\导入用户.xlsx";
		String xmlConfig = "d:\\Person.xml";
		try{
			exj.readExcelFile(dataXLS, xmlConfig);

		}catch (Exception e){

			e.printStackTrace();
		}
	}
}
