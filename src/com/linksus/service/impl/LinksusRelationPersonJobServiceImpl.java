package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusRelationPersonJobMapper;
import com.linksus.entity.LinksusRelationPersonJob;
import com.linksus.service.LinksusRelationPersonJobService;

@Service("linksusRelationPersonJobService")
public class LinksusRelationPersonJobServiceImpl implements LinksusRelationPersonJobService{

	@Autowired
	private LinksusRelationPersonJobMapper linksusRelationPersonJobMapper;

	/** 查询列表 */
	public List<LinksusRelationPersonJob> getLinksusRelationPersonJobList(){
		return linksusRelationPersonJobMapper.getLinksusRelationPersonJobList();
	}

	/** 主键查询 */
	public LinksusRelationPersonJob getLinksusRelationPersonJobById(Long pid){
		return linksusRelationPersonJobMapper.getLinksusRelationPersonJobById(pid);
	}

	/** 新增 */
	public Integer insertLinksusRelationPersonJob(LinksusRelationPersonJob entity){
		return linksusRelationPersonJobMapper.insertLinksusRelationPersonJob(entity);
	}

	/** 更新 */
	public Integer updateLinksusRelationPersonJob(LinksusRelationPersonJob entity){
		return linksusRelationPersonJobMapper.updateLinksusRelationPersonJob(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusRelationPersonJobById(Long pid){
		return linksusRelationPersonJobMapper.deleteLinksusRelationPersonJobById(pid);
	}

	/** 删除personId的数据 */
	public Integer deleteByPersonId(Long personId){
		return linksusRelationPersonJobMapper.deleteByPersonId(personId);
	}
}