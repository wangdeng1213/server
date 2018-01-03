package com.linksus.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusRelationEmailConfMapper;
import com.linksus.entity.LinksusRelationEmailConf;
import com.linksus.service.LinksusRelationEmailConfService;

@Service("linksusRelationEmailConfService")
public class LinksusRelationEmailConfServiceImpl implements LinksusRelationEmailConfService{

	@Autowired
	private LinksusRelationEmailConfMapper linksusRelationEmailConfMapper;

	/** 通过机构ID查询机构邮箱设置表 */
	public LinksusRelationEmailConf getLinksusRelationEmailConfInfo(long id){
		return linksusRelationEmailConfMapper.getLinksusRelationEmailConfInfo(id);
	}
}