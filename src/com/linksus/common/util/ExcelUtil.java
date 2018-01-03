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

	/**ת���ַ���ʹ��	
	  \n �س�(
	) 
	  \t ˮƽ�Ʊ��(\u0009) 
	  \b �ո�(\u0008) 
	  \r ����(
	) 
	  \f ��ҳ(\u000c) 
	  \' ������(\u0027) 
	  \" ˫����(\u0022) 
	  \\ ��б��(\u005c) 
	  \ddd ��λ�˽��� 
	  \udddd ��λʮ������ 
	 */

	// ����excel�ļ�
	public static void writeExcel(OutputStream os) throws Exception{
		WritableWorkbook wwb = Workbook.createWorkbook(os);
		WritableSheet ws = wwb.createSheet("TestSheet1", 1);
		Label labelC = new Label(0, 0, "���");
		ws.addCell(labelC);
		WritableFont wfc = new WritableFont(WritableFont.ARIAL, 20, WritableFont.BOLD, false,
				UnderlineStyle.NO_UNDERLINE, Colour.GREEN);
		WritableCellFormat wcfFC = new WritableCellFormat(wfc);
		wcfFC.setBackground(Colour.RED);
		labelC = new Label(6, 0, "����", wcfFC);

		ws.addCell(labelC);
		labelC = new Label(7, 0, "���");
		ws.addCell(labelC);
		// д��Exel������
		wwb.write();
		// �ر�Excel����������
		wwb.close();
	}

	// ��ȡexcel�ļ�
	public static void readExcel(File file) throws Exception{
		Workbook workbook = null;

		try{
			workbook = Workbook.getWorkbook(file);
		}catch (Exception e){
			throw new Exception("file to import not found!");
		}

		Sheet sheet = workbook.getSheet(0);
		int columnCount = 10;//�������ļ���������
		int rowCount = sheet.getRows();
		for(int i = 0; i < rowCount; i++){
			for(int j = 0; j < columnCount; j++){
				sheet.getCell(j, i);
			}
		}
		// �ر�������������ڴ�й¶
		workbook.close();

	}

	public static void main(String[] args) throws Exception{
		File f = new File("d:\\20070910ѧ���ɷ�.xls");
		try{

			f.createNewFile();
			// writeExcel(new FileOutputStream(f));
			readExcel(f);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

}
