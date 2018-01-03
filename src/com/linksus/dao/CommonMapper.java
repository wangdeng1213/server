package com.linksus.dao;

import java.util.Map;

public interface CommonMapper{

	public Long getPrimaryKeyFromTable(Map map);

	public Integer execUpdateSql(String sql);

	public Long getSequencesPrimaryKeyByName(Map map);

	public Integer updateSequencesPrimaryKeyByName(Map map);

	public Integer insertSequencesPrimaryKeyByName(Map map);
}
