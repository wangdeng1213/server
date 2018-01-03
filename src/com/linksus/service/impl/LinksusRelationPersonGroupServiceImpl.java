package com.linksus.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusRelationPersonGroupMapper;
import com.linksus.entity.LinksusRelationPersonGroup;
import com.linksus.service.LinksusRelationPersonGroupService;

@Service("linksusRelationPersonGroupService")
public class LinksusRelationPersonGroupServiceImpl implements LinksusRelationPersonGroupService{

	@Autowired
	private LinksusRelationPersonGroupMapper linksusRelationPersonGroupMapper;

	/** 查询列表 */
	public List<LinksusRelationPersonGroup> getLinksusRelationPersonGroupList(){
		return linksusRelationPersonGroupMapper.getLinksusRelationPersonGroupList();
	}

	/** 主键查询 */
	public LinksusRelationPersonGroup getLinksusRelationPersonGroupById(Map paramMap){
		return linksusRelationPersonGroupMapper.getLinksusRelationPersonGroupById(paramMap);
	}

	/** 新增 */
	public Integer insertLinksusRelationPersonGroup(LinksusRelationPersonGroup entity){
		return linksusRelationPersonGroupMapper.insertLinksusRelationPersonGroup(entity);
	}

	/** 更新 */
	public Integer updateLinksusRelationPersonGroup(LinksusRelationPersonGroup entity){
		return linksusRelationPersonGroupMapper.updateLinksusRelationPersonGroup(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusRelationPersonGroupById(Long pid){
		return linksusRelationPersonGroupMapper.deleteLinksusRelationPersonGroupById(pid);
	}

	/**删除person_group 根据personId 和institutionId*/
	public Integer deleteByHashMap(Map map){
		return linksusRelationPersonGroupMapper.deleteByHashMap(map);
	}

	/** 删除personId的数据 */
	public Integer deleteByPersonId(Long personId){
		return linksusRelationPersonGroupMapper.deleteByPersonId(personId);
	}

	/**Map查询 */
	public List<LinksusRelationPersonGroup> getLinksusRelationPersonGroupByMap(Map map){
		// TODO Auto-generated method stub
		return linksusRelationPersonGroupMapper.getLinksusRelationPersonGroupByMap(map);
	}

	public LinksusRelationPersonGroup searchRelationPersonGroupByMap(Map map){

		return linksusRelationPersonGroupMapper.searchRelationPersonGroupByMap(map);
	}

	/** 组合主键删除 */
	public Integer deleteLinksusRelationPersonGroupByMap(Map map){
		return linksusRelationPersonGroupMapper.deleteLinksusRelationPersonGroupByMap(map);
	}
}