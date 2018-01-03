package com.linksus.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusRelationGroupMapper;
import com.linksus.entity.LinksusRelationGroup;
import com.linksus.service.LinksusRelationGroupService;

@Service("linksusRelationGroupService")
public class LinksusRelationGroupServiceImpl implements LinksusRelationGroupService{

	@Autowired
	private LinksusRelationGroupMapper linksusRelationGroupMapper;

	/** 查询列表 */
	public List<LinksusRelationGroup> getLinksusRelationGroupList(){
		return linksusRelationGroupMapper.getLinksusRelationGroupList();
	}

	/** 主键查询 */
	public LinksusRelationGroup getLinksusRelationGroupById(Long pid){
		return linksusRelationGroupMapper.getLinksusRelationGroupById(pid);
	}

	/** 新增 */
	public Integer insertLinksusRelationGroup(LinksusRelationGroup entity){
		return linksusRelationGroupMapper.insertLinksusRelationGroup(entity);
	}

	/** 更新 */
	public Integer updateLinksusRelationGroup(LinksusRelationGroup entity){
		return linksusRelationGroupMapper.updateLinksusRelationGroup(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusRelationGroupById(Long pid){
		return linksusRelationGroupMapper.deleteLinksusRelationGroupById(pid);
	}

	/** 单独查询列 */
	public LinksusRelationGroup getLinksusRelationGroup(LinksusRelationGroup dto){
		// TODO Auto-generated method stub
		return linksusRelationGroupMapper.getLinksusRelationGroup(dto);
	}

	/** 查询该机构是否存在"未分组"组 */
	public LinksusRelationGroup getPersonGroupInfoByInstIdAndGroupType(Map paramMap){
		return linksusRelationGroupMapper.getPersonGroupInfoByInstIdAndGroupType(paramMap);
	}

	/** 查询该机构是否存在"**"组 */
	public LinksusRelationGroup getPersonGroupInfoType(Map map){
		return linksusRelationGroupMapper.getPersonGroupInfoType(map);
	}

	/** 插入未分组信息 */
	public Integer insertNoGroupInfo(LinksusRelationGroup entity){
		return linksusRelationGroupMapper.insertNoGroupInfo(entity);
	}
}