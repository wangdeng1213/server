package com.linksus.common.util;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Decoder;

import com.linksus.common.Constants;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.service.LinksusTaskErrorCodeService;

public class StringUtil{

	private static CacheUtil cache = CacheUtil.getInstance();
	protected static final Logger logger = LoggerFactory.getLogger(StringUtil.class);
	private static Random randGen = null;
	private static char[] numbersAndLetters = null;
	private static Set<Character.UnicodeBlock> UNICODE_BLOCK_SET = new HashSet<Character.UnicodeBlock>();
	static{
		//�ж�˫�ֽ��ַ�
		UNICODE_BLOCK_SET.add(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS); //CJK ͳһ�������
		UNICODE_BLOCK_SET.add(Character.UnicodeBlock.GENERAL_PUNCTUATION);
		UNICODE_BLOCK_SET.add(Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS);
		UNICODE_BLOCK_SET.add(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A);
		UNICODE_BLOCK_SET.add(Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION); //CJK ���źͱ��
		UNICODE_BLOCK_SET.add(Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS); //��Ǽ�ȫ����ʽ
		//�����ַ�
		UNICODE_BLOCK_SET.add(Character.UnicodeBlock.BASIC_LATIN);
		UNICODE_BLOCK_SET.add(Character.UnicodeBlock.LATIN_1_SUPPLEMENT);
		UNICODE_BLOCK_SET.add(Character.UnicodeBlock.LATIN_EXTENDED_A);
		UNICODE_BLOCK_SET.add(Character.UnicodeBlock.LATIN_EXTENDED_B);
		//�����е�ƽ������Ƭ����
		UNICODE_BLOCK_SET.add(Character.UnicodeBlock.KATAKANA);
		UNICODE_BLOCK_SET.add(Character.UnicodeBlock.HIRAGANA);
		UNICODE_BLOCK_SET.add(Character.UnicodeBlock.BOPOMOFO);
	}

	public static String utf8CharFilter(String rsStr){
		if(rsStr == null){
			return "";
		}
		StringBuffer buffer = new StringBuffer();
		for(int i = 0; i < rsStr.length(); i++){
			char ch = rsStr.charAt(i);
			if(isCJKCharacter(ch)){
				buffer.append(String.valueOf(rsStr.charAt(i)));
			}
		}
		return buffer.toString();
	}

	/** 
	 * �жϸ����ַ��Ƿ�Ϊһ���Ϸ���CJK˫�ֽ��ַ������������ŵȣ� 
	 * 
	 * @param c �����ַ� 
	 * @return 
	 */
	public static boolean isCJKCharacter(char c){
		return UNICODE_BLOCK_SET.contains(Character.UnicodeBlock.of(c));
	}

	public static boolean isEmpty(String string){
		if(string == null || string.trim().equals("")){
			return true;
		}else{
			return false;
		}
	}

	public static boolean isContain(String[] columns,String col){
		for(int i = 0; i < columns.length; i++){
			if(columns[i].endsWith(col)){
				return true;
			}
		}
		return false;
	}

	/**
	 * ǰ�˵��ýӿڷ���JSON�ַ���:{errcode:"",msg:""},rsMapΪ��������,û�д��������errcode
	 * @param columns
	 * @param col
	 * @return
	 */
	public static String getHttpResultStr(Map rsMap){
		if(rsMap == null){
			rsMap = new HashMap();
		}
		if(rsMap.get("errcode") == null){//��ȷ����
			rsMap.put("errcode", "0");
			rsMap.put("msg", "success");
		}else{
			String errorCode = rsMap.get("errcode").toString();
			if(errorCode.startsWith("#")){
				rsMap.put("errcode", "10000");
				//ȡ�ô�������
				rsMap.put("msg", errorCode.substring(1));
				return JsonUtil.map2json(rsMap);
			}else{
				//ȡ�ô�������
				CacheUtil cache = CacheUtil.getInstance();
				LinksusTaskErrorCode error = cache.getErrorCode(errorCode);
				if(error != null){
					if(error.getDisplayType().intValue() == 1){
						rsMap.put("errcode", "10000");
						rsMap.put("msg", "ϵͳ����");
					}else{
						rsMap.put("msg", error.getErrorMsg());
					}
				}else{
					rsMap.put("errcode", "10000");
					rsMap.put("msg", "�������" + errorCode + "������");
				}
			}
		}
		return JsonUtil.map2json(rsMap);
	}

	/**
	 * ʹ��errorcode����JSON�ַ���:{errcode:"",msg:""}
	 * @param errorCode
	 * @return
	 */
	public static String getHttpResultErrorStr(String errorCode){
		Map rsMap = null;
		if(StringUtils.isBlank(errorCode)){
			return getHttpResultStr(null);
		}else if(errorCode.startsWith("#")){
			rsMap = new HashMap();
			rsMap.put("errcode", "10000");
			rsMap.put("msg", errorCode.substring(1));
			return JsonUtil.map2json(rsMap);
		}else if(errorCode.equals(Constants.INVALID_RECORD_EXCEPTION)){
			rsMap = new HashMap();
			rsMap.put("errcode", "10000");
			rsMap.put("msg", "����������(10000)");
			return JsonUtil.map2json(rsMap);
		}else{
			rsMap = new HashMap();
			rsMap.put("errcode", errorCode);
			LinksusTaskErrorCode error = cache.getErrorCode(errorCode);
			if(error != null){
				if(error.getDisplayType().intValue() == 1){
					rsMap.put("errcode", "10000");
					rsMap.put("msg", "����������(" + errorCode + ")");
				}else{
					rsMap.put("msg", error.getErrorMsg());
				}
			}else{
				rsMap.put("errcode", "10000");
				rsMap.put("msg", "����������(" + errorCode + ")");
			}
			return JsonUtil.map2json(rsMap);
		}
	}

	/**
	 * �ӿڷ����쳣��Ϣ
	 * @param exception
	 * @return
	 */
	public static String getHttpResultForException(Exception exception){
		Map rsMap = new HashMap();
		int logRows = 10;
		int logSize = 500;
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
		rsMap.put("errcode", "90000");
		rsMap.put("msg", errmsg);
		return JsonUtil.map2json(rsMap);
	}

	/**
	 * ȡ��ָ�����ȵ�����ַ���
	 * @param length
	 * @return
	 */
	public static final String randomString(int length){
		if(length < 1){
			return null;
		}
		if(randGen == null){
			randGen = new Random();
			numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz" + "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ")
					.toCharArray();
		}
		char[] randBuffer = new char[length];
		for(int i = 0; i < randBuffer.length; i++){
			randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
		}
		return new String(randBuffer);
	}

	public static String decodeBase64(String str) throws IOException{
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] b = decoder.decodeBuffer(str);
		return new String(b);
	}

	public static String Md5To16Bit(String plainText){
		String bits = "";
		try{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for(int offset = 0; offset < b.length; offset++){
				i = b[offset];
				if(i < 0){
					i += 256;
				}
				if(i < 16){
					buf.append("0");
				}
				buf.append(Integer.toHexString(i));
			}
			bits = buf.toString().substring(8, 24);//16λ�ļ��� 
		}catch (NoSuchAlgorithmException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return bits;
	}

	public static String Md5To32Bit(String plainText){
		String bits = "";
		try{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for(int offset = 0; offset < b.length; offset++){
				i = b[offset];
				if(i < 0){
					i += 256;
				}
				if(i < 16){
					buf.append("0");
				}
				buf.append(Integer.toHexString(i));
			}
			bits = buf.toString();//32λ�ļ��� 
		}catch (NoSuchAlgorithmException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return bits;
	}

	/**
	* ��֤ip�Ƿ�Ϸ�
	* 
	* @param text
	*            ip��ַ
	* @return ��֤��Ϣ
	*/
	public static boolean ipCheck(String text){
		if(text != null && !text.isEmpty()){
			// ����������ʽ
			String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
					+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
					+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
			// �ж�ip��ַ�Ƿ���������ʽƥ��
			if(text.matches(regex)){
				// �����ж���Ϣ
				return true;
			}else{
				// �����ж���Ϣ
				return false;
			}
		}
		// �����ж���Ϣ
		return false;
	}

	/**
	 * �������˴������ ����ϵͳ�������
	 * @param rsData
	 * @return
	 */
	public static LinksusTaskErrorCode parseSinaErrorCode(String rsData){
		if(StringUtils.isBlank(rsData)){
			Map errorCacheMap = (Map) cache.getCache(Constants.CACHE_ERROR_CODE);
			return (LinksusTaskErrorCode) errorCacheMap.get(Constants.CACHE_ERROR_CODE_NULL);
		}
		String errorCode = JsonUtil.getNodeValueByName(rsData, "error_code");
		if(!StringUtils.isBlank(errorCode)){
			Map sinaCacheMap = (Map) cache.getCache(Constants.CACHE_ERROR_CODE_SINA);
			LinksusTaskErrorCode error = (LinksusTaskErrorCode) sinaCacheMap.get(errorCode);
			//��ͳ�ƴ��󷵻���
			if(error != null){
				return error;
			}else{
				//�¼�������
				LinksusTaskErrorCodeService codeService = (LinksusTaskErrorCodeService) ContextUtil
						.getBean("linksusTaskErrorCodeService");
				try{
					Long error_code = PrimaryKeyGen.getPrimaryKey("linksus_task_error_code", "error_code");
					String msg = JsonUtil.getNodeValueByName(rsData, "error");
					LinksusTaskErrorCode newErrorCode = new LinksusTaskErrorCode();
					newErrorCode.setErrorCode(error_code);
					newErrorCode.setErrorMsg(msg + "(�����룺" + error_code + ")");
					newErrorCode.setErrorType(1);
					newErrorCode.setSrcCode(errorCode);
					newErrorCode.setSrcMsg(msg);
					newErrorCode.setDisplayType(0);
					codeService.insertLinksusTaskErrorCode(newErrorCode);
					//���뻺��
					sinaCacheMap.put(errorCode, newErrorCode);
					Map errorCacheMap = (Map) cache.getCache(Constants.CACHE_ERROR_CODE);
					errorCacheMap.put(error_code.toString(), newErrorCode);
					return newErrorCode;
				}catch (Exception e){
					LogUtil.saveException(logger, e);
				}
			}
		}
		return null;
	}

	/**
	 * ������Ѷ�������
	 * @param rsData
	 * @return
	 */
	public static LinksusTaskErrorCode parseTencentErrorCode(String rsData){
		if(StringUtils.isBlank(rsData)){
			Map errorCacheMap = (Map) cache.getCache(Constants.CACHE_ERROR_CODE);
			return (LinksusTaskErrorCode) errorCacheMap.get(Constants.CACHE_ERROR_CODE_NULL);
		}
		Object obj = JsonUtil.getNodeValueByName(rsData, "ret");
		if(obj == null){
			return null;
		}
		String ret = obj.toString();
		if(!"0".equals(ret)){
			String errorCode = ret + "_" + JsonUtil.getNodeValueByName(rsData, "errcode");
			Map tencentCacheMap = (Map) cache.getCache(Constants.CACHE_ERROR_CODE_TENCENT);
			LinksusTaskErrorCode error = (LinksusTaskErrorCode) tencentCacheMap.get(errorCode);
			if(error != null){
				return error;
			}else{
				//�¼�������
				LinksusTaskErrorCodeService codeService = (LinksusTaskErrorCodeService) ContextUtil
						.getBean("linksusTaskErrorCodeService");
				try{
					Long error_code = PrimaryKeyGen.getPrimaryKey("linksus_task_error_code", "error_code");
					String msg = JsonUtil.getNodeValueByName(rsData, "msg");
					LinksusTaskErrorCode newErrorCode = new LinksusTaskErrorCode();
					newErrorCode.setErrorCode(error_code);
					newErrorCode.setErrorMsg(msg + "(�����룺" + error_code + ")");
					newErrorCode.setErrorType(2);
					newErrorCode.setSrcCode(errorCode);
					newErrorCode.setSrcMsg(msg);
					newErrorCode.setDisplayType(0);
					codeService.insertLinksusTaskErrorCode(newErrorCode);
					//���뻺��
					tencentCacheMap.put(errorCode, newErrorCode);
					Map errorCacheMap = (Map) cache.getCache(Constants.CACHE_ERROR_CODE);
					errorCacheMap.put(error_code.toString(), newErrorCode);
					return newErrorCode;
				}catch (Exception e){
					LogUtil.saveException(logger, e);
				}
			}
		}
		return null;
	}

	public static void main(String[] args) throws IOException{
		parseTencentErrorCode("{\"errcode\":\"33333\",\"ret\":\"1\",\"msg\":\"����\"}");
		parseTencentErrorCode("{\"errcode\":\"53333\",\"ret\":\"1\",\"msg\":\"����\"}");

	}
}
