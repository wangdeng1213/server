package com.linksus.dao;

import com.linksus.entity.LinksusSourceAppaccount;

public interface LinksusSourceAppaccountMapper{

	LinksusSourceAppaccount getTokenByAccount(LinksusSourceAppaccount account);

	/** �����û�token */
	public Integer updateAppaccountToken(LinksusSourceAppaccount account);

}