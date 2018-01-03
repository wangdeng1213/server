package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusGovYixinMapper;
import com.linksus.entity.LinksusGovYixin;
import com.linksus.service.LinksusGovYixinService;

@Service("linksusGovYixinService")
public class LinksusGovYixinServiceImpl implements LinksusGovYixinService{

	@Autowired
	private LinksusGovYixinMapper linksusGovYixinMapper;

	/** 查询列表 */
	public List<LinksusGovYixin> getLinksusGovYixinList(){
		return linksusGovYixinMapper.getLinksusGovYixinList();
	}

	/** 主键查询 */
	public LinksusGovYixin getLinksusGovYixinById(Long pid){
		return linksusGovYixinMapper.getLinksusGovYixinById(pid);
	}

	/** 新增 */
	public Integer insertLinksusGovYixin(LinksusGovYixin entity){
		return linksusGovYixinMapper.insertLinksusGovYixin(entity);
	}

	/** 更新 */
	public Integer updateLinksusGovYixin(LinksusGovYixin entity){
		return linksusGovYixinMapper.updateLinksusGovYixin(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusGovYixinById(Long pid){
		return linksusGovYixinMapper.deleteLinksusGovYixinById(pid);
	}
}