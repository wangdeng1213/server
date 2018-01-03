package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusWxObjectSupplyMapper;
import com.linksus.entity.LinksusWxObjectSupply;
import com.linksus.service.LinksusWxObjectSupplyService;

@Service("linksusWxObjectSupplyService")
public class LinksusWxObjectSupplyServiceImpl implements LinksusWxObjectSupplyService{

	@Autowired
	private LinksusWxObjectSupplyMapper linksusWxObjectSupplyMapper;

	/** 查询列表 */
	public List<LinksusWxObjectSupply> getLinksusWxObjectSupplyList(){
		return linksusWxObjectSupplyMapper.getLinksusWxObjectSupplyList();
	}

	/** 主键查询 */
	public LinksusWxObjectSupply getLinksusWxObjectSupplyById(Long pid){
		return linksusWxObjectSupplyMapper.getLinksusWxObjectSupplyById(pid);
	}

	/** 新增 */
	public Integer insertLinksusWxObjectSupply(LinksusWxObjectSupply entity){
		return linksusWxObjectSupplyMapper.insertLinksusWxObjectSupply(entity);
	}

	/** 更新 */
	public Integer updateLinksusWxObjectSupply(LinksusWxObjectSupply entity){
		return linksusWxObjectSupplyMapper.updateLinksusWxObjectSupply(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusWxObjectSupplyById(Long pid){
		return linksusWxObjectSupplyMapper.deleteLinksusWxObjectSupplyById(pid);
	}

	/** 查询单个发送微信内容 */
	public LinksusWxObjectSupply getSingleLinksusWxObjectSupplyById(Long pid){
		return linksusWxObjectSupplyMapper.getSingleLinksusWxObjectSupplyById(pid);
	}

	/** 查询多个发送微信内容*/
	public List<LinksusWxObjectSupply> getMoreLinksusWxObjectSupplyById(Long pid){
		return linksusWxObjectSupplyMapper.getMoreLinksusWxObjectSupplyById(pid);
	}
}