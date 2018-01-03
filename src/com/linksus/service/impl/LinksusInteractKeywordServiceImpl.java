package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusInteractKeywordMapper;
import com.linksus.entity.LinksusInteractKeyword;
import com.linksus.service.LinksusInteractKeywordService;

@Service("linksusInteractKeywordService")
public class LinksusInteractKeywordServiceImpl implements LinksusInteractKeywordService{

	@Autowired
	private LinksusInteractKeywordMapper linksusInteractKeywordMapper;

	/** 查询列表 */
	public List<LinksusInteractKeyword> getLinksusInteractKeywordList(){
		return linksusInteractKeywordMapper.getLinksusInteractKeywordList();
	}

	/** 主键查询 */
	public LinksusInteractKeyword getLinksusInteractKeywordById(Long pid){
		return linksusInteractKeywordMapper.getLinksusInteractKeywordById(pid);
	}

	/** 新增 */
	public Integer insertLinksusInteractKeyword(LinksusInteractKeyword entity){
		return linksusInteractKeywordMapper.insertLinksusInteractKeyword(entity);
	}

	/** 更新 */
	public Integer updateLinksusInteractKeyword(LinksusInteractKeyword entity){
		return linksusInteractKeywordMapper.updateLinksusInteractKeyword(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusInteractKeywordById(Long pid){
		return linksusInteractKeywordMapper.deleteLinksusInteractKeywordById(pid);
	}
}