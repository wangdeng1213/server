package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusRelationOperateMapper;
import com.linksus.entity.LinksusRelationOperate;
import com.linksus.service.LinksusRelationOperateService;

@Service("linksusRelationOperateService")
public class LinksusRelationOperateServiceImpl implements LinksusRelationOperateService{

	@Autowired
	private LinksusRelationOperateMapper linksusRelationOperateMapper;

	/** 查询列表 */
	public List<LinksusRelationOperate> getLinksusRelationOperateList(){
		return linksusRelationOperateMapper.getLinksusRelationOperateList();
	}

	/** 主键查询 */
	public LinksusRelationOperate getLinksusRelationOperateById(Long pid){
		return linksusRelationOperateMapper.getLinksusRelationOperateById(pid);
	}

	/** 新增 */
	public Integer insertLinksusRelationOperate(LinksusRelationOperate entity){
		return linksusRelationOperateMapper.insertLinksusRelationOperate(entity);
	}

	/** 更新 */
	public Integer updateLinksusRelationOperate(LinksusRelationOperate entity){
		return linksusRelationOperateMapper.updateLinksusRelationOperate(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusRelationOperateById(Long pid){
		return linksusRelationOperateMapper.deleteLinksusRelationOperateById(pid);
	}

	//主键更新操作表失败次数
	public Integer updateLinksusRelationOperFailNum(Long pid){
		return linksusRelationOperateMapper.updateLinksusRelationOperFailNum(pid);
	}

	//主键更新操作成功次数
	public Integer updateLinksusRelationOperSuccessNum(Long pid){
		return linksusRelationOperateMapper.updateLinksusRelationOperSuccessNum(pid);
	}

	public LinksusRelationOperate checkIsExsitRelationOperate(String paraStr){
		return linksusRelationOperateMapper.checkIsExsitRelationOperate(paraStr);

	}
}