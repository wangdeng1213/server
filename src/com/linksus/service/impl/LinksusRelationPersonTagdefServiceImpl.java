package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusRelationPersonTagdefMapper;
import com.linksus.entity.LinksusRelationPersonTagdef;
import com.linksus.service.LinksusRelationPersonTagdefService;

@Service("linksusRelationPersonTagdefService")
public class LinksusRelationPersonTagdefServiceImpl implements LinksusRelationPersonTagdefService{

	@Autowired
	private LinksusRelationPersonTagdefMapper linksusRelationPersonTagdefMapper;

	/** 查询列表 */
	public List<LinksusRelationPersonTagdef> getLinksusRelationPersonTagdefList(){
		return linksusRelationPersonTagdefMapper.getLinksusRelationPersonTagdefList();
	}

	/** 主键查询 */
	public LinksusRelationPersonTagdef getLinksusRelationPersonTagdefById(Long pid){
		return linksusRelationPersonTagdefMapper.getLinksusRelationPersonTagdefById(pid);
	}

	/** 新增 */
	public Integer insertLinksusRelationPersonTagdef(LinksusRelationPersonTagdef entity){
		return linksusRelationPersonTagdefMapper.insertLinksusRelationPersonTagdef(entity);
	}

	/** 更新 */
	public Integer updateLinksusRelationPersonTagdef(LinksusRelationPersonTagdef entity){
		return linksusRelationPersonTagdefMapper.updateLinksusRelationPersonTagdef(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusRelationPersonTagdefById(Long pid){
		return linksusRelationPersonTagdefMapper.deleteLinksusRelationPersonTagdefById(pid);
	}

	/** tag_name查询 */
	public LinksusRelationPersonTagdef getLinksusRelationPersonTagdefByTagName(String tag_name){
		return linksusRelationPersonTagdefMapper.getLinksusRelationPersonTagdefByTagName(tag_name);
	}

}