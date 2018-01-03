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
	 * 判断是否为最大主键
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
	 * 设置缓存可使用的最大主键值
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
	* 获取linksus_Sequences表中的weibo_id
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

	//获取表中的主键最大值
	private Long getSequenceTableId(String sequeueName){
		Long sequeueKey = commonService.getSequenceTableId(sequeueName, cacheSize);
		this.maxCount = sequeueKey + 1;
		this.currCount = sequeueKey - cacheSize + 1;
		return sequeueKey;
	}

	private synchronized Long getSequencesPrimaryKey(String sequeueName) throws Exception{
		long rsKey = 0;
		// 判断当前主键值是否使用完
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
	 * 单个获取主键 判断是本地或远程获取
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
		if(!"1".equals(queueServer)){// 远程获取
			return gen.getPrimaryKey();
		}else{
			return gen.getPrimaryKey(1);
		}
	}

	/**
	 * 接口批量获取主键
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
	 * 远程取得主键值
	 * 
	 * @param size
	 * @return
	 * @throws Exception
	 */
	private synchronized Long getPrimaryKey() throws Exception{
		if(!"1".equals(queueServer)){// 远程获取
			// 判断当前主键值是否使用完
			if(checkCountEnd()){
				Map paramMap = new HashMap();
				paramMap.put("tableName", tableName);
				paramMap.put("columnName", columnName);
				paramMap.put("cacheSize", cacheSize);
				paramMap.put("taskType", "101");
				String str = HttpUtil.getRequest(remoteUrl, paramMap);
				if(!"0".equals(JsonUtil.getNodeValueByName(str, "errcode"))){// 存在错误
					throw new Exception("获取主键出错:" + str);
				}
				String id = JsonUtil.getNodeValueByName(str, "id");
				setMaxCount(Long.parseLong(id), cacheSize);
				logger.debug(">>>>>>>>>>>>>>>>>>>从服务器获得主键值:{}[{}][{}]--当前值:{},最大值{}", tableName, columnName, cacheSize,
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
	 * 本地取得主键值
	 * 
	 * @param size
	 * @return
	 */
	private synchronized Long getPrimaryKey(Integer size){
		long rsKey = 0;
		if(currCount > 0){
			rsKey = currCount + 1;
			currCount = currCount + size;
			logger.debug(">>>>>>>>>>>>>>>>>>>从缓存获得主键值:{}[{}][{}]={}", tableName, columnName, cacheSize, rsKey);
		}else{// 查询数据库
			Long count = commonService.getPrimaryKeyFromTable(tableName, columnName);
			if(count == null){
				count = 0L;
			}
			rsKey = count + 1;
			currCount = count + size;
			logger.debug(">>>>>>>>>>>>>>>>>>>从数据库获得主键值:{}[{}][{}]={}", tableName, columnName, cacheSize, rsKey);
		}
		return rsKey;
	}

	public static Long GenerateSerialNum(){
		//String cruDate = Long.valueOf(String.valueOf(new Date().getTime())).toString(); //流水号18位
		String cruDate = DateUtil.getUnixDate(new Date()).toString();//流水号15位
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
