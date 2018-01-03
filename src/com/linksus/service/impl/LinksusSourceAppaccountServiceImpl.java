package com.linksus.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusSourceAppaccountMapper;
import com.linksus.entity.LinksusSourceAppaccount;
import com.linksus.service.LinksusSourceAppaccountService;

@Service("linksusSourceAppaccountService")
public class LinksusSourceAppaccountServiceImpl implements LinksusSourceAppaccountService{

	@Autowired
	LinksusSourceAppaccountMapper mapper;

	@Override
	public LinksusSourceAppaccount getTokenByAccount(LinksusSourceAppaccount account){
		return mapper.getTokenByAccount(account);
	}

	/** 更新用户token */
	public Integer updateAppaccountToken(LinksusSourceAppaccount account){
		return mapper.updateAppaccountToken(account);
	}

}
