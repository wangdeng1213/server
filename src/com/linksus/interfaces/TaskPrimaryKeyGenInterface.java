package com.linksus.interfaces;

import java.util.HashMap;
import java.util.Map;

import com.linksus.common.util.PrimaryKeyGen;

public class TaskPrimaryKeyGenInterface extends BaseInterface{

	private String interfaceType;

	public void setInterfaceType(String interfaceType){
		this.interfaceType = interfaceType;
	}

	// 取得表主键
	public Map cal(Map paramsMap) throws Exception{
		Map rsMap = new HashMap();
		try{
			if("1".equals(interfaceType)){
				String idType = (String) paramsMap.get("idType");
				Integer size = (Integer) paramsMap.get("cacheSize");
				if(size == null){
					size = 1;
				}
				if("1".equals(idType)){
					Long primaryKey = PrimaryKeyGen.getPrimaryKey("linksus_relation_group", "group_id", size);
					rsMap.put("id", primaryKey);
				}else if("2".equals(idType)){
					Long primaryKey = PrimaryKeyGen.getPrimaryKey("linksus_relation_weibouser", "user_id", size);
					rsMap.put("id", primaryKey);
				}else if("3".equals(idType)){
					Long primaryKey = PrimaryKeyGen.getPrimaryKey("linksus_relation_person", "person_id", size);
					rsMap.put("id", primaryKey);
				}else if("4".equals(idType)){
					Long primaryKey = PrimaryKeyGen.getPrimaryKey("linksus_interact_wx_material", "material_id", size);
					rsMap.put("id", primaryKey);
				}else if("5".equals(idType)){
					Long primaryKey = PrimaryKeyGen.getPrimaryKey("linksus_interact_wx_article", "pid", size);
					rsMap.put("id", primaryKey);
				}else if("6".equals(idType)){
					Long primaryKey = PrimaryKeyGen.getPrimaryKey("linksus_gov_interact", "interact_id", size);
					rsMap.put("id", primaryKey);
				}else if("7".equals(idType)){
					Long primaryKey = PrimaryKeyGen.getPrimaryKey("linksus_gov_message", "pid", size);
					rsMap.put("id", primaryKey);
				}
				logger.debug(">>>>>>>>>>>>>>>>>>>获取主键:idType={},id={}", idType, rsMap.get("id"));
			}else{
				String tableName = (String) paramsMap.get("tableName");
				String columnName = (String) paramsMap.get("columnName");
				Integer size = (Integer) paramsMap.get("cacheSize");
				Long primaryKey = PrimaryKeyGen.getPrimaryKey(tableName, columnName, size);
				rsMap.put("id", primaryKey);
				logger.debug(">>>>>>>>>>>>>>>>>>>获取主键:{}[{}][{}]={}", paramsMap.get("tableName"), paramsMap
						.get("columnName"), paramsMap.get("cacheSize"), rsMap.get("id"));
			}
		}catch (Exception e){
			throw e;
		}
		return rsMap;
	}
}
