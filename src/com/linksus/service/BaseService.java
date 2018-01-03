package com.linksus.service;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.Constants;
import com.linksus.common.config.DataConfig;
import com.linksus.common.config.LoadConfig;
import com.linksus.common.util.RedisUtil;

public abstract class BaseService{

	protected static final Logger logger = LoggerFactory.getLogger(BaseService.class);

	@Resource
	protected SqlSession sqlSession;

	public void setSqlSession(SqlSessionTemplate sqlSession){
		this.sqlSession = sqlSession;
	}

	/**
	 * 新增批量提交错误时删除缓存中对应数据
	 * @param obj
	 * @param operType
	 * @throws Exception
	 */
	public void removeRedisKey(Object obj,String operType) throws Exception{
		if(!operType.equals(Constants.OPER_TYPE_INSERT)){//只处理新增数据
			return;
		}
		String mapKey = obj.getClass().getSimpleName() + "-" + operType;
		Map datasMap = LoadConfig.getDatasMap();
		DataConfig config = (DataConfig) datasMap.get(mapKey);
		if(config == null){
			return;
		}
		String[] fieldKeys = config.getFieldKey().split(",");
		StringBuffer keyValue = new StringBuffer("");
		if(fieldKeys.length > 1){
			for(int i = 0; i < fieldKeys.length; i++){
				String fieldKey = fieldKeys[i];
				String getMethodName = "get" + fieldKey.substring(0, 1).toUpperCase() + fieldKey.substring(1);
				Method getFeildMethod = obj.getClass().getMethod(getMethodName, null);
				Object rsObj = getFeildMethod.invoke(obj, null);
				if(rsObj != null){
					keyValue.append(rsObj.toString()).append(config.getFieldSeparator());
				}
			}
			if(keyValue.length() > 0){
				keyValue.deleteCharAt(keyValue.length() - 1);
			}
		}else{
			String getMethodName = "get" + fieldKeys[0].substring(0, 1).toUpperCase() + fieldKeys[0].substring(1);
			Method getFeildMethod = obj.getClass().getMethod(getMethodName, null);
			Object rsObj = getFeildMethod.invoke(obj, null);
			if(rsObj != null){
				keyValue.append(rsObj.toString());
			}
		}
		String hashKey = config.getHashKey();
		String fieldKey = keyValue.toString();
		logger.info(">>>>>>>>>>>>>>>>新增出现异常,删除缓存数据 {}:{}", hashKey, fieldKey);
		RedisUtil.delFieldKey(hashKey, fieldKey);
	}

	public void saveEntity(List dataList,String operType) throws Exception{
		try{
			for(int i = 0; i < dataList.size(); i++){
				Object entity = dataList.get(i);
				if(operType.equals(Constants.OPER_TYPE_INSERT)){
					sqlSession.insert(entity.getClass().getName(), entity);
				}else if(operType.equals(Constants.OPER_TYPE_UPDATE)){
					sqlSession.update(entity.getClass().getName(), entity);
				}
			}
		}catch (Exception e){
			//判断类型为新增的 删除缓存
			if(operType.equals(Constants.OPER_TYPE_INSERT)){
				for(int i = 0; i < dataList.size(); i++){
					removeRedisKey(dataList.get(i), operType);
				}
			}
			throw e;
		}
	}
}
