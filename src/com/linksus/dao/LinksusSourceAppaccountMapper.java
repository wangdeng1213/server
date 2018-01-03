package com.linksus.dao;

import com.linksus.entity.LinksusSourceAppaccount;

public interface LinksusSourceAppaccountMapper{

	LinksusSourceAppaccount getTokenByAccount(LinksusSourceAppaccount account);

	/** 更新用户token */
	public Integer updateAppaccountToken(LinksusSourceAppaccount account);

}