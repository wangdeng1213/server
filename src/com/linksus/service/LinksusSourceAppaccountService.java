package com.linksus.service;

import com.linksus.entity.LinksusSourceAppaccount;

public interface LinksusSourceAppaccountService{

	public LinksusSourceAppaccount getTokenByAccount(LinksusSourceAppaccount account);

	/** 更新用户token */
	public Integer updateAppaccountToken(LinksusSourceAppaccount account);

}
