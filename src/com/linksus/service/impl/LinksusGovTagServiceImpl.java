package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusGovTagMapper;
import com.linksus.entity.LinksusGovTag;
import com.linksus.service.LinksusGovTagService;

@Service("linksusGovTagService")
public class LinksusGovTagServiceImpl implements LinksusGovTagService{

	@Autowired
	private LinksusGovTagMapper linksusGovTagMapper;

	/** 查询列表 */
	public List<LinksusGovTag> getLinksusGovTagList(){
		return linksusGovTagMapper.getLinksusGovTagList();
	}

	/** 主键查询 */
	public LinksusGovTag getLinksusGovTagById(Long pid){
		return linksusGovTagMapper.getLinksusGovTagById(pid);
	}

	/** 新增 */
	public Integer insertLinksusGovTag(LinksusGovTag entity){
		return linksusGovTagMapper.insertLinksusGovTag(entity);
	}

	/** 更新 */
	public Integer updateLinksusGovTag(LinksusGovTag entity){
		return linksusGovTagMapper.updateLinksusGovTag(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusGovTagById(Long pid){
		return linksusGovTagMapper.deleteLinksusGovTagById(pid);
	}
}