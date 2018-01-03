package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusRelationUserTagCountMapper;
import com.linksus.entity.LinksusRelationUserTagCount;
import com.linksus.service.LinksusRelationUserTagCountService;

@Service("linksusRelationUserTagCountService")
public class LinksusRelationUserTagCountServiceImpl implements LinksusRelationUserTagCountService{

	@Autowired
	private LinksusRelationUserTagCountMapper linksusRelationUserTagCountMapper;

	/** 查询列表 */
	public List<LinksusRelationUserTagCount> getLinksusRelationUserTagCountList(){
		return linksusRelationUserTagCountMapper.getLinksusRelationUserTagCountList();
	}

	/** 主键查询 */
	public LinksusRelationUserTagCount getLinksusRelationUserTagCountById(Long pid){
		return linksusRelationUserTagCountMapper.getLinksusRelationUserTagCountById(pid);
	}

	/** 新增 */
	public Integer insertLinksusRelationUserTagCount(LinksusRelationUserTagCount entity){
		return linksusRelationUserTagCountMapper.insertLinksusRelationUserTagCount(entity);
	}

	/** 更新 */
	public Integer updateLinksusRelationUserTagCount(LinksusRelationUserTagCount entity){
		return linksusRelationUserTagCountMapper.updateLinksusRelationUserTagCount(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusRelationUserTagCountById(Long pid){
		return linksusRelationUserTagCountMapper.deleteLinksusRelationUserTagCountById(pid);
	}

	/** 全表删除 */
	public Integer deleteALLLinksusRelationUserTagCount(){
		return linksusRelationUserTagCountMapper.deleteALLLinksusRelationUserTagCount();
	}

	/** 全表新增 */
	public Integer insertALLLinksusRelationUserTagCount(){
		return linksusRelationUserTagCountMapper.insertALLLinksusRelationUserTagCount();
	}
}