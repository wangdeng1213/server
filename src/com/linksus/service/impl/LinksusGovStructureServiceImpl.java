package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusGovStructureMapper;
import com.linksus.entity.LinksusGovStructure;
import com.linksus.service.LinksusGovStructureService;

@Service("linksusGovStructureService")
public class LinksusGovStructureServiceImpl implements LinksusGovStructureService{

	@Autowired
	private LinksusGovStructureMapper linksusGovStructureMapper;

	/** 查询列表 */
	public List<LinksusGovStructure> getLinksusGovStructureList(){
		return linksusGovStructureMapper.getLinksusGovStructureList();
	}

	/** 主键查询 */
	public LinksusGovStructure getLinksusGovStructureById(Long pid){
		return linksusGovStructureMapper.getLinksusGovStructureById(pid);
	}

	/** 新增 */
	public Integer insertLinksusGovStructure(LinksusGovStructure entity){
		return linksusGovStructureMapper.insertLinksusGovStructure(entity);
	}

	/** 更新 */
	public Integer updateLinksusGovStructure(LinksusGovStructure entity){
		return linksusGovStructureMapper.updateLinksusGovStructure(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusGovStructureById(Long pid){
		return linksusGovStructureMapper.deleteLinksusGovStructureById(pid);
	}

	@Override
	public List<LinksusGovStructure> getLinksusGovStructureByOrgId(){
		// TODO Auto-generated method stub
		return linksusGovStructureMapper.getLinksusGovStructureByOrgId();
	}

	/** 根据account_id查询对应最小的gid */
	public LinksusGovStructure getMinGidByAccountId(Long accountId){
		return linksusGovStructureMapper.getMinGidByAccountId(accountId);
	}
}