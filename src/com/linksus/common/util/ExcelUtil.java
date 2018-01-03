package com.linksus.common.util;

import java.io.File;
import java.io.OutputStream;

import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExcelUtil{

	/**转义字符的使用	
	  \n 回车(
	) 
	  \t 水平制表符(\u0009) 
	  \b 空格(\u0008) 
	  \r 换行(
	) 
	  \f 换页(\u000c) 
	  \' 单引号(\u0027) 
	  \" 双引号(\u0022) 
	  \\ 反斜杠(\u005c) 
	  \ddd 三位八进制 
	  \udddd 四位十六进制 
	 */

	// 创建excel文件
	public static void writeExcel(OutputStream os) throws Exception{
		WritableWorkbook wwb = Workbook.createWorkbook(os);
		WritableSheet ws = wwb.createSheet("TestSheet1", 1);
		Label labelC = new Label(0, 0, "如果");
		ws.addCell(labelC);
		WritableFont wfc = new WritableFont(WritableFont.ARIAL, 20, WritableFont.BOLD, false,
				UnderlineStyle.NO_UNDERLINE, Colour.GREEN);
		WritableCellFormat wcfFC = new WritableCellFormat(wfc);
		wcfFC.setBackground(Colour.RED);
		labelC = new Label(6, 0, "假如", wcfFC);

		ws.addCell(labelC);
		labelC = new Label(7, 0, "结果");
		ws.addCell(labelC);
		// 写入Exel工作表
		wwb.write();
		// 关闭Excel工作薄对象
		wwb.close();
	}

	// 读取excel文件
	public static void readExcel(File file) throws Exception{
		Workbook workbook = null;

		try{
			workbook = Workbook.getWorkbook(file);
		}catch (Exception e){
			throw new Exception("file to import not found!");
		}

		Sheet sheet = workbook.getSheet(0);
		int columnCount = 10;//看具体文件的列数量
		int rowCount = sheet.getRows();
		for(int i = 0; i < rowCount; i++){
			for(int j = 0; j < columnCount; j++){
				sheet.getCell(j, i);
			}
		}
		// 关闭它，否则会有内存泄露
		workbook.close();

	}

	public static void main(String[] args) throws Exception{
		File f = new File("d:\\20070910学生缴费.xls");
		try{

			f.createNewFile();
			// writeExcel(new FileOutputStream(f));
			readExcel(f);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

}
