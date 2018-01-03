package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusGovAppraiseMapper;
import com.linksus.entity.LinksusGovAppraise;
import com.linksus.service.LinksusGovAppraiseService;

@Service("linksusGovAppraiseService")
public class LinksusGovAppraiseServiceImpl implements LinksusGovAppraiseService{

	@Autowired
	private LinksusGovAppraiseMapper linksusGovAppraiseMapper;

	/** 查询列表 */
	public List<LinksusGovAppraise> getLinksusGovAppraiseList(){
		return linksusGovAppraiseMapper.getLinksusGovAppraiseList();
	}

	/** 主键查询 */
	public LinksusGovAppraise getLinksusGovAppraiseById(Long pid){
		return linksusGovAppraiseMapper.getLinksusGovAppraiseById(pid);
	}

	/** 新增 */
	public Integer insertLinksusGovAppraise(LinksusGovAppraise entity){
		return linksusGovAppraiseMapper.insertLinksusGovAppraise(entity);
	}

	/** 更新 */
	public Integer updateLinksusGovAppraise(LinksusGovAppraise entity){
		return linksusGovAppraiseMapper.updateLinksusGovAppraise(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusGovAppraiseById(Long pid){
		return linksusGovAppraiseMapper.deleteLinksusGovAppraiseById(pid);
	}
}