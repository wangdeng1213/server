package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusTaskConnLimitMapper;
import com.linksus.entity.LinksusTaskConnLimit;
import com.linksus.service.LinksusTaskConnLimitService;

@Service("linksysTaskConnLimitService")
public class LinksusTaskConnLimitImpl implements LinksusTaskConnLimitService{

	@Autowired
	private LinksusTaskConnLimitMapper linksysTaskConnLimitMapper;

	@Override
	public void addLinksusTaskConnLimit(LinksusTaskConnLimit linksysTaskConnLimit){
		linksysTaskConnLimitMapper.addLinksusTaskConnLimit(linksysTaskConnLimit);
	}

	@Override
	public void deleteByLimitDate(String limitDate){
		linksysTaskConnLimitMapper.deleteByLimitDate(limitDate);
	}

	@Override
	public List<LinksusTaskConnLimit> getLinksusTaskConnLimit(){
		return linksysTaskConnLimitMapper.getLinksusTaskConnLimit();
	}

}
