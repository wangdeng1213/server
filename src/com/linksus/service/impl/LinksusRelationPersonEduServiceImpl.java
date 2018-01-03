package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusRelationPersonEduMapper;
import com.linksus.entity.LinksusRelationPersonEdu;
import com.linksus.service.LinksusRelationPersonEduService;

@Service("linksusRelationPersonEduService")
public class LinksusRelationPersonEduServiceImpl implements LinksusRelationPersonEduService{

	@Autowired
	private LinksusRelationPersonEduMapper linksusRelationPersonEduMapper;

	/** 查询列表 */
	public List<LinksusRelationPersonEdu> getLinksusRelationPersonEduList(){
		return linksusRelationPersonEduMapper.getLinksusRelationPersonEduList();
	}

	/** 主键查询 */
	public LinksusRelationPersonEdu getLinksusRelationPersonEduById(Long pid){
		return linksusRelationPersonEduMapper.getLinksusRelationPersonEduById(pid);
	}

	/** 新增 */
	public Integer insertLinksusRelationPersonEdu(LinksusRelationPersonEdu entity){
		return linksusRelationPersonEduMapper.insertLinksusRelationPersonEdu(entity);
	}

	/** 更新 */
	public Integer updateLinksusRelationPersonEdu(LinksusRelationPersonEdu entity){
		return linksusRelationPersonEduMapper.updateLinksusRelationPersonEdu(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusRelationPersonEduById(Long pid){
		return linksusRelationPersonEduMapper.deleteLinksusRelationPersonEduById(pid);
	}

	/** 删除personId的数据 */
	public Integer deleteByPersonId(Long personId){
		return linksusRelationPersonEduMapper.deleteByPersonId(personId);
	}
}