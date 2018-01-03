package com.linksus.service;

import com.linksus.entity.LinksusRelationEmailConf;

public interface LinksusRelationEmailConfService{

	/** 通过机构ID查询机构邮箱设置表 */
	public LinksusRelationEmailConf getLinksusRelationEmailConfInfo(long id);

}
