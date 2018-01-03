package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusRelationPersonTagMapper;
import com.linksus.entity.LinksusRelationPersonTag;
import com.linksus.service.LinksusRelationPersonTagService;

@Service("linksusRelationPersonTagService")
public class LinksusRelationPersonTagServiceImpl implements LinksusRelationPersonTagService{

	@Autowired
	private LinksusRelationPersonTagMapper linksusRelationPersonTagMapper;

	/** 查询列表 */
	public List<LinksusRelationPersonTag> getLinksusRelationPersonTagList(){
		return linksusRelationPersonTagMapper.getLinksusRelationPersonTagList();
	}

	/** 主键查询 */
	public LinksusRelationPersonTag getLinksusRelationPersonTagById(Long pid){
		return linksusRelationPersonTagMapper.getLinksusRelationPersonTagById(pid);
	}

	/** 新增 */
	public Integer insertLinksusRelationPersonTag(LinksusRelationPersonTag entity){
		return linksusRelationPersonTagMapper.insertLinksusRelationPersonTag(entity);
	}

	/** 更新 */
	public Integer updateLinksusRelationPersonTag(LinksusRelationPersonTag entity){
		return linksusRelationPersonTagMapper.updateLinksusRelationPersonTag(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusRelationPersonTagById(Long pid){
		return linksusRelationPersonTagMapper.deleteLinksusRelationPersonTagById(pid);
	}

	/** 删除personId的数据 */
	public Integer deleteByPersonId(Long personId){
		return linksusRelationPersonTagMapper.deleteByPersonId(personId);
	}
}