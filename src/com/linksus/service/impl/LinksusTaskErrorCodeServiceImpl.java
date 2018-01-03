package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusTaskErrorCodeMapper;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.service.LinksusTaskErrorCodeService;

@Service("linksusTaskErrorCodeService")
public class LinksusTaskErrorCodeServiceImpl implements LinksusTaskErrorCodeService{

	@Autowired
	private LinksusTaskErrorCodeMapper linksusTaskErrorCodeMapper;

	/** 查询列表 */
	public List<LinksusTaskErrorCode> getLinksusTaskErrorCodeList(){
		return linksusTaskErrorCodeMapper.getLinksusTaskErrorCodeList();
	}

	/** 主键查询 */
	public LinksusTaskErrorCode getLinksusTaskErrorCodeById(Long pid){
		return linksusTaskErrorCodeMapper.getLinksusTaskErrorCodeById(pid);
	}

	/** 新增 */
	public Integer insertLinksusTaskErrorCode(LinksusTaskErrorCode entity){
		return linksusTaskErrorCodeMapper.insertLinksusTaskErrorCode(entity);
	}

	/** 更新 */
	public Integer updateLinksusTaskErrorCode(LinksusTaskErrorCode entity){
		return linksusTaskErrorCodeMapper.updateLinksusTaskErrorCode(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusTaskErrorCodeById(Long pid){
		return linksusTaskErrorCodeMapper.deleteLinksusTaskErrorCodeById(pid);
	}
}