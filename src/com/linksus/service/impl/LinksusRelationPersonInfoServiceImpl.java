package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusRelationPersonInfoMapper;
import com.linksus.entity.LinksusRelationPersonInfo;
import com.linksus.service.LinksusRelationPersonInfoService;

@Service("linksusRelationPersonInfoService")
public class LinksusRelationPersonInfoServiceImpl implements LinksusRelationPersonInfoService{

	@Autowired
	private LinksusRelationPersonInfoMapper linksusRelationPersonInfoMapper;

	/** 查询列表 */
	public List<LinksusRelationPersonInfo> getLinksusRelationPersonInfoList(){
		return linksusRelationPersonInfoMapper.getLinksusRelationPersonInfoList();
	}

	/** 主键查询 */
	public LinksusRelationPersonInfo getLinksusRelationPersonInfoById(Long pid){
		return linksusRelationPersonInfoMapper.getLinksusRelationPersonInfoById(pid);
	}

	/** 新增 */
	public Integer insertLinksusRelationPersonInfo(LinksusRelationPersonInfo entity){
		return linksusRelationPersonInfoMapper.insertLinksusRelationPersonInfo(entity);
	}

	/** 更新 */
	public Integer updateLinksusRelationPersonInfo(LinksusRelationPersonInfo entity){
		return linksusRelationPersonInfoMapper.updateLinksusRelationPersonInfo(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusRelationPersonInfoById(Long pid){
		return linksusRelationPersonInfoMapper.deleteLinksusRelationPersonInfoById(pid);
	}

}