package com.linksus.common.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.config.LoadConfig;
import com.linksus.entity.UrlEntity;
import com.linksus.service.CommonService;

public class PrimaryKeyGen{

	protected static final Logger logger = LoggerFactory.getLogger(PrimaryKeyGen.class);
	protected CommonService commonService = (CommonService) ContextUtil.getBean("commonService");
	private static Map generator = new HashMap();
	private static String queueServer = LoadConfig.getString("queueServer");
	private static UrlEntity remoteUrl = LoadConfig.getUrlEntity("remoteUrl");
	private static long cacheSize = new Long(LoadConfig.getString("pkCacheSize"));
	private long maxCount;
	private long currCount;
	private String tableName;
	private String columnName;

	/**
	 * �ж��Ƿ�Ϊ�������
	 * 
	 * @return
	 */
	public boolean checkCountEnd(){
		if(currCount == 0){
			return true;
		}else if(maxCount > currCount){
			return false;
		}
		return true;
	}

	/**
	 * ���û����ʹ�õ��������ֵ
	 * 
	 * @param currCount
	 * @param cacheSize
	 */
	private void setMaxCount(long currCount,long cacheSize){
		this.currCount = currCount;
		this.maxCount = currCount + cacheSize - 1;
	}

	private PrimaryKeyGen(String tableName, String columnName) {
		this.tableName = tableName;
		this.columnName = columnName;
		this.currCount = 0;
		this.maxCount = 0;
	}

	private static synchronized PrimaryKeyGen putGenerator(String tableName,String columnName){
		if(generator.containsKey(tableName)){
			return (PrimaryKeyGen) generator.get(tableName);
		}
		PrimaryKeyGen gen = new PrimaryKeyGen(tableName, columnName);
		generator.put(tableName, gen);
		return gen;
	}

	/**
	* 
	* ��ȡlinksus_Sequences���е�weibo_id
	* */
	public static Long getSequencesPrimaryKey(String tableName,String sequeueName) throws Exception{

		PrimaryKeyGen gen = null;
		if(generator.containsKey(tableName)){
			gen = (PrimaryKeyGen) generator.get(tableName);
		}else{
			gen = putGenerator(tableName, sequeueName);
		}
		return gen.getSequencesPrimaryKey(sequeueName);
	}

	//��ȡ���е��������ֵ
	private Long getSequenceTableId(String sequeueName){
		Long sequeueKey = commonService.getSequenceTableId(sequeueName, cacheSize);
		this.maxCount = sequeueKey + 1;
		this.currCount = sequeueKey - cacheSize + 1;
		return sequeueKey;
	}

	private synchronized Long getSequencesPrimaryKey(String sequeueName) throws Exception{
		long rsKey = 0;
		// �жϵ�ǰ����ֵ�Ƿ�ʹ����
		if(checkCountEnd()){
			Long count = getSequenceTableId(sequeueName);
			if(count == null){
				count = 0L;
			}
			rsKey = currCount;
			currCount = currCount + 1;
			return rsKey;
		}else{
			if(currCount > 0){
				rsKey = currCount;
				currCount = currCount + 1;
			}
			return rsKey;
		}
	}

	/**
	 * ������ȡ���� �ж��Ǳ��ػ�Զ�̻�ȡ
	 * 
	 * @param tableName
	 * @param columnName
	 * @return
	 * @throws Exception
	 */
	public static Long getPrimaryKey(String tableName,String columnName) throws Exception{
		LoadConfig.getString("remotePrimaryKeyFlag");
		PrimaryKeyGen gen = null;
		if(generator.containsKey(tableName)){
			gen = (PrimaryKeyGen) generator.get(tableName);
		}else{
			gen = putGenerator(tableName, columnName);
		}
		if(!"1".equals(queueServer)){// Զ�̻�ȡ
			return gen.getPrimaryKey();
		}else{
			return gen.getPrimaryKey(1);
		}
	}

	/**
	 * �ӿ�������ȡ����
	 * 
	 * @param tableName
	 * @param columnName
	 * @param size
	 * @return
	 */
	public static Long getPrimaryKey(String tableName,String columnName,Integer size){
		PrimaryKeyGen gen = null;
		if(generator.containsKey(tableName)){
			gen = (PrimaryKeyGen) generator.get(tableName);
		}else{
			gen = putGenerator(tableName, columnName);
		}
		return gen.getPrimaryKey(size);
	}

	/**
	 * Զ��ȡ������ֵ
	 * 
	 * @param size
	 * @return
	 * @throws Exception
	 */
	private synchronized Long getPrimaryKey() throws Exception{
		if(!"1".equals(queueServer)){// Զ�̻�ȡ
			// �жϵ�ǰ����ֵ�Ƿ�ʹ����
			if(checkCountEnd()){
				Map paramMap = new HashMap();
				paramMap.put("tableName", tableName);
				paramMap.put("columnName", columnName);
				paramMap.put("cacheSize", cacheSize);
				paramMap.put("taskType", "101");
				String str = HttpUtil.getRequest(remoteUrl, paramMap);
				if(!"0".equals(JsonUtil.getNodeValueByName(str, "errcode"))){// ���ڴ���
					throw new Exception("��ȡ��������:" + str);
				}
				String id = JsonUtil.getNodeValueByName(str, "id");
				setMaxCount(Long.parseLong(id), cacheSize);
				logger.debug(">>>>>>>>>>>>>>>>>>>�ӷ������������ֵ:{}[{}][{}]--��ǰֵ:{},���ֵ{}", tableName, columnName, cacheSize,
						id, maxCount);
				return Long.parseLong(id);
			}else{
				return getPrimaryKey(1);
			}
		}else{
			return getPrimaryKey(1);
		}
	}

	/**
	 * ����ȡ������ֵ
	 * 
	 * @param size
	 * @return
	 */
	private synchronized Long getPrimaryKey(Integer size){
		long rsKey = 0;
		if(currCount > 0){
			rsKey = currCount + 1;
			currCount = currCount + size;
			logger.debug(">>>>>>>>>>>>>>>>>>>�ӻ���������ֵ:{}[{}][{}]={}", tableName, columnName, cacheSize, rsKey);
		}else{// ��ѯ���ݿ�
			Long count = commonService.getPrimaryKeyFromTable(tableName, columnName);
			if(count == null){
				count = 0L;
			}
			rsKey = count + 1;
			currCount = count + size;
			logger.debug(">>>>>>>>>>>>>>>>>>>�����ݿ�������ֵ:{}[{}][{}]={}", tableName, columnName, cacheSize, rsKey);
		}
		return rsKey;
	}

	public static Long GenerateSerialNum(){
		//String cruDate = Long.valueOf(String.valueOf(new Date().getTime())).toString(); //��ˮ��18λ
		String cruDate = DateUtil.getUnixDate(new Date()).toString();//��ˮ��15λ
		int[] radom = GetRandomSequence(5);
		String serialNum = "";
		for(int i = 0; i < radom.length; i++){
			serialNum = serialNum + radom[i];
		}
		return new Long(cruDate + serialNum);
	}

	private static int[] GetRandomSequence(int total){
		int[] sequence = new int[total];
		int[] output = new int[total];
		for(int i = 0; i < total; i++){
			sequence[i] = i;
		}
		Random random = new Random();
		int end = total - 1;
		for(int i = 0; i < total; i++){
			int num = random.nextInt(end + 1);
			output[i] = sequence[num];
			sequence[num] = sequence[end];
			end--;
		}
		return output;
	}

	public static void main(String[] args) throws Exception{
		for(int i = 0; i < 100; i++){
			System.out.println(PrimaryKeyGen.getSequencesPrimaryKey("linksus_Sequences", "weibod"));
		}
	}

}
