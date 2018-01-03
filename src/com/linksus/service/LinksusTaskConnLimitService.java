package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusTaskConnLimit;

public interface LinksusTaskConnLimitService{

	public List<LinksusTaskConnLimit> getLinksusTaskConnLimit();

	public void addLinksusTaskConnLimit(LinksusTaskConnLimit linksysTaskConnLimit);

	public void deleteByLimitDate(String limitDate);
}
