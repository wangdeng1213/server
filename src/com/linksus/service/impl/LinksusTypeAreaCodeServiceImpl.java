package com.linksus.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusTypeAreaCodeMapper;
import com.linksus.entity.LinksusTypeAreaCode;
import com.linksus.service.LinksusTypeAreaCodeService;

@Service("linksusTypeAreaCodeService")
public class LinksusTypeAreaCodeServiceImpl implements LinksusTypeAreaCodeService{

	@Autowired
	private LinksusTypeAreaCodeMapper linksusTypeAreaCodeMapper;

	/** 查询列表 */
	public List<LinksusTypeAreaCode> getLinksusTypeAreaCodeList(){
		return linksusTypeAreaCodeMapper.getLinksusTypeAreaCodeList();
	}

	/** 主键查询 */
	public LinksusTypeAreaCode getLinksusTypeAreaCodeById(Long pid){
		return linksusTypeAreaCodeMapper.getLinksusTypeAreaCodeById(pid);
	}

	/** 新增 */
	public Integer insertLinksusTypeAreaCode(LinksusTypeAreaCode entity){
		return linksusTypeAreaCodeMapper.insertLinksusTypeAreaCode(entity);
	}

	/** 更新 */
	public Integer updateLinksusTypeAreaCode(LinksusTypeAreaCode entity){
		return linksusTypeAreaCodeMapper.updateLinksusTypeAreaCode(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusTypeAreaCodeById(Long pid){
		return linksusTypeAreaCodeMapper.deleteLinksusTypeAreaCodeById(pid);
	}

	/** 获取地区对象*/
	public List<LinksusTypeAreaCode> getLinksusTypeAreaCodeListByCode(Map codeMap){
		return linksusTypeAreaCodeMapper.getLinksusTypeAreaCodeListByCode(codeMap);

	}

	/** 批量插入数据 */
	public void replaceLinksusTypeAreaCodeInfo(List allArea){
		linksusTypeAreaCodeMapper.replaceLinksusTypeAreaCodeInfo(allArea);
	}

	/** 代码查询 */
	public LinksusTypeAreaCode getLinksusTypeAreaCode(Map codeMap){
		return linksusTypeAreaCodeMapper.getLinksusTypeAreaCode(codeMap);
	}

	public LinksusTypeAreaCode getProvicesInfo(Map codeMap){
		return linksusTypeAreaCodeMapper.getProvicesInfo(codeMap);
	}

	public Integer updateSinaAreaCodeByCode(Map codeMap){
		return linksusTypeAreaCodeMapper.updateSinaAreaCodeByCode(codeMap);
	}
}