package com.linksus.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil{

	/**
	 * ȡ�ø�ʽ�������ַ�������ʽ2013��-11��-8��
	 */
	public String getFormateDate(Date date){
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy��MM��dd��");
		String strDate = dateformat.format(date);
		return strDate;

	}

	public static Date parse(String str,String pattern,Locale locale){
		if(str == null || pattern == null){
			return null;
		}
		try{
			return new SimpleDateFormat(pattern, locale).parse(str);
		}catch (ParseException e){
			e.printStackTrace();
		}
		return null;
	}

	public static String format(Date date,String pattern,Locale locale){
		if(date == null || pattern == null){
			return null;
		}
		return new SimpleDateFormat(pattern, locale).format(date);
	}

	public static void main(String[] args) throws ParseException{
		String datetime = "Tue Nov 26 09:52:10 +0800 2013";
		parse(datetime, "EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
		new Date(Long.parseLong("1374593820") * 1000);
	}

	public static String formatDate(Long time){
		SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
		String dateTime = df.format(new Date(time * 1000));
		return dateTime;
	}

	/**
	 * ����ʽȡ��ǰ����
	 * @param format
	 * @return
	 */
	public static String getCurrDate(String format){
		Date date_time = new Date();
		return formatDate(date_time, format);
	}

	/**
	 * DateתUnixʱ���
	 * @param date
	 * @return
	 */
	public static Integer getUnixDate(Date date){
		long s = date.getTime();
		String dateStr = String.valueOf(s).substring(0, 10);
		return new Integer(dateStr);
	}

	/**
	 * Unixʱ���תDate
	 * @param date
	 * @return
	 */
	public static Date parseUnixDate(Integer time){
		Long timestamp = new Long(time) * 1000;
		Date date = new java.util.Date(timestamp);
		return date;
	}

	/**
	 * �����ڽ��и�ʽ��
	 * @param date ����
	 * @param sf ���ڸ�ʽ
	 * @return �ַ���
	 */
	public static String formatDate(Date date,String sf){
		if(date == null){
			return "";
		}
		SimpleDateFormat dateformat = new SimpleDateFormat(sf);
		return dateformat.format(date);
	}

	public static Date getFormatDate(String dateStr,String format) throws ParseException{
		Date date = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		date = dateFormat.parse(dateStr);
		return date;
	}

}
