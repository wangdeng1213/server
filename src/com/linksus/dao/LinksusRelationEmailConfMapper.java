package com.linksus.dao;

import com.linksus.entity.LinksusRelationEmailConf;

public interface LinksusRelationEmailConfMapper{

	/** 通过机构ID查询机构邮箱设置表 */
	public LinksusRelationEmailConf getLinksusRelationEmailConfInfo(long id);

}
