package com.linksus.service;

/**
 * ȡ�ò���������id ֵ
 * @author  
 */
public interface CommonService{

	public Long getPrimaryKeyFromTable(String tableName,String columnName);

	public Integer execUpdateSql(String sql);

	public Long getSequencesPrimaryKeyByName(String paraName);

	public Integer updateSequencesPrimaryKeyByName(String paraName,Long cacheSize);

	public Integer insertSequencesPrimaryKeyByName(String paraName);

	public Long getSequenceTableId(String paraName,Long cacheSize);
}
