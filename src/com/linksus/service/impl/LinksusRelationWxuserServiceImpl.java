package com.linksus.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusRelationWxuserMapper;
import com.linksus.entity.LinksusRelationWxuser;
import com.linksus.service.LinksusRelationWxuserService;

@Service("linksusRelationWxuserService")
public class LinksusRelationWxuserServiceImpl implements LinksusRelationWxuserService{

	@Autowired
	private LinksusRelationWxuserMapper linksusRelationWxuserMapper;

	/** 查询列表 */
	public List<LinksusRelationWxuser> getLinksusRelationWxuserList(){
		return linksusRelationWxuserMapper.getLinksusRelationWxuserList();
	}

	/** 主键查询 */
	public LinksusRelationWxuser getLinksusRelationWxuserById(Long pid){
		return linksusRelationWxuserMapper.getLinksusRelationWxuserById(pid);
	}

	/** 新增 */
	public Integer insertLinksusRelationWxuser(LinksusRelationWxuser entity){
		return linksusRelationWxuserMapper.insertLinksusRelationWxuser(entity);
	}

	/** 更新 */
	public Integer updateLinksusRelationWxuser(LinksusRelationWxuser entity){
		return linksusRelationWxuserMapper.updateLinksusRelationWxuser(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusRelationWxuserById(Long pid){
		return linksusRelationWxuserMapper.deleteLinksusRelationWxuserById(pid);
	}

	/**  根据主键查询微信用户信息*/
	public LinksusRelationWxuser getLinksusRelationWxuserInfo(long userId){
		return linksusRelationWxuserMapper.getLinksusRelationWxuserInfo(userId);
	}

	/** 多条件查询单个 */
	public LinksusRelationWxuser getLinksusRelationWxuserByMap(Map map){
		return linksusRelationWxuserMapper.getLinksusRelationWxuserByMap(map);
	}

	/** 更新PersonId */
	public Integer updatePersonId(LinksusRelationWxuser entity){
		return linksusRelationWxuserMapper.updatePersonId(entity);
	}
}