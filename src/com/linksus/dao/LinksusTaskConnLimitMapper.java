package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusTaskConnLimit;

/**
 * 
 * SINA�ӿڲ������Ʊ�
 *
 */
public interface LinksusTaskConnLimitMapper{

	public List<LinksusTaskConnLimit> getLinksusTaskConnLimit();

	public void addLinksusTaskConnLimit(LinksusTaskConnLimit linksysTaskConnLimit);

	public void deleteByLimitDate(String limitDate);
}
