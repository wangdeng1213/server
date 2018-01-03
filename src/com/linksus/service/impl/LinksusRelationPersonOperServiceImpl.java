package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusRelationPersonOperMapper;
import com.linksus.entity.LinksusRelationPersonOper;
import com.linksus.service.LinksusRelationPersonOperService;

@Service("linksusRelationPersonOperService")
public class LinksusRelationPersonOperServiceImpl implements LinksusRelationPersonOperService{

	@Autowired
	private LinksusRelationPersonOperMapper linksusRelationPersonOperMapper;

	/** 查询列表 */
	public List<LinksusRelationPersonOper> getLinksusRelationPersonOperList(){
		return linksusRelationPersonOperMapper.getLinksusRelationPersonOperList();
	}

	/** 主键查询 */
	public LinksusRelationPersonOper getLinksusRelationPersonOperById(Long pid){
		return linksusRelationPersonOperMapper.getLinksusRelationPersonOperById(pid);
	}

	/** 新增 */
	public Integer insertLinksusRelationPersonOper(LinksusRelationPersonOper entity){
		return linksusRelationPersonOperMapper.insertLinksusRelationPersonOper(entity);
	}

	/** 更新 */
	public Integer updateLinksusRelationPersonOper(LinksusRelationPersonOper entity){
		return linksusRelationPersonOperMapper.updateLinksusRelationPersonOper(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusRelationPersonOperById(Long pid){
		return linksusRelationPersonOperMapper.deleteLinksusRelationPersonOperById(pid);
	}
}