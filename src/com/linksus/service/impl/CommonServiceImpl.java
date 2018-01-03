package com.linksus.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.CommonMapper;
import com.linksus.service.CommonService;

@Service("commonService")
public class CommonServiceImpl implements CommonService{

	@Autowired
	private CommonMapper commonMapper;

	public Long getPrimaryKeyFromTable(String tableName,String columnName){
		Map map = new HashMap();
		map.put("tableName", tableName);
		map.put("columnName", columnName);
		return commonMapper.getPrimaryKeyFromTable(map);
	}

	@Override
	public Integer execUpdateSql(String sql){
		return commonMapper.execUpdateSql(sql);
	}

	@Override
	public Long getSequencesPrimaryKeyByName(String paraName){
		Map map = new HashMap();
		map.put("paraName", paraName);
		return commonMapper.getSequencesPrimaryKeyByName(map);
	}

	@Override
	public Integer updateSequencesPrimaryKeyByName(String paraName,Long cacheSize){
		Map map = new HashMap();
		map.put("paraName", paraName);
		map.put("size", cacheSize.toString());
		return commonMapper.updateSequencesPrimaryKeyByName(map);
	}

	@Override
	public Integer insertSequencesPrimaryKeyByName(String paraName){
		Map map = new HashMap();
		map.put("paraName", paraName);
		map.put("timestamp", new Date().getTime() / 1000);
		return commonMapper.insertSequencesPrimaryKeyByName(map);
	}

	public Long getSequenceTableId(String paraName,Long cacheSize){
		Long sequeueKey = 0L;
		try{
			sequeueKey = getSequencesPrimaryKeyByName(paraName);
			if(sequeueKey == null){
				sequeueKey = 0L;
				insertSequencesPrimaryKeyByName(paraName);
			}
			updateSequencesPrimaryKeyByName(paraName, cacheSize);
			sequeueKey = sequeueKey + cacheSize;
		}catch (Exception e){
			e.printStackTrace();
		}
		return sequeueKey;
	}
}
