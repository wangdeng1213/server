package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusGovWeiboMapper;
import com.linksus.entity.LinksusGovWeibo;
import com.linksus.service.LinksusGovWeiboService;

@Service("linksusGovWeiboService")
public class LinksusGovWeiboServiceImpl implements LinksusGovWeiboService{

	@Autowired
	private LinksusGovWeiboMapper linksusGovWeiboMapper;

	/** 查询列表 */
	public List<LinksusGovWeibo> getLinksusGovWeiboList(){
		return linksusGovWeiboMapper.getLinksusGovWeiboList();
	}

	/** 主键查询 */
	public LinksusGovWeibo getLinksusGovWeiboById(Long pid){
		return linksusGovWeiboMapper.getLinksusGovWeiboById(pid);
	}

	/** 新增 */
	public Integer insertLinksusGovWeibo(LinksusGovWeibo entity){
		return linksusGovWeiboMapper.insertLinksusGovWeibo(entity);
	}

	/** 更新 */
	public Integer updateLinksusGovWeibo(LinksusGovWeibo entity){
		return linksusGovWeiboMapper.updateLinksusGovWeibo(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusGovWeiboById(Long pid){
		return linksusGovWeiboMapper.deleteLinksusGovWeiboById(pid);
	}

	@Override
	public List<LinksusGovWeibo> getWeiboImmeSend(LinksusGovWeibo entity){
		return linksusGovWeiboMapper.getWeiboImmeSend(entity);
	}

	@Override
	public void updateSendWeiboStatus(LinksusGovWeibo entity){
		linksusGovWeiboMapper.updateSendWeiboStatus(entity);

	}

	@Override
	public void updateSendWeiboSucc(LinksusGovWeibo entity){
		linksusGovWeiboMapper.updateSendWeiboSucc(entity);
	}
}