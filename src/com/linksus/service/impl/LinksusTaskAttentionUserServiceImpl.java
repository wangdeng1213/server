package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusTaskAttentionUserMapper;
import com.linksus.entity.LinksusTaskAttentionUser;
import com.linksus.service.LinksusTaskAttentionUserService;

@Service("linksusTaskAttentionUserService")
public class LinksusTaskAttentionUserServiceImpl implements LinksusTaskAttentionUserService{

	@Autowired
	private LinksusTaskAttentionUserMapper linksusTaskAttentionUserMapper;

	/** 查询列表 */
	public List<LinksusTaskAttentionUser> getLinksusTaskAttentionUserList(){
		return linksusTaskAttentionUserMapper.getLinksusTaskAttentionUserList();
	}

	/** 主键查询 */
	public LinksusTaskAttentionUser getLinksusTaskAttentionUserById(Long pid){
		return linksusTaskAttentionUserMapper.getLinksusTaskAttentionUserById(pid);
	}

	/** 判断粉丝表中是否存在 */
	public Integer countLinksusTaskAttentionUser(LinksusTaskAttentionUser entity){
		return linksusTaskAttentionUserMapper.countLinksusTaskAttentionUser(entity);
	}

	/** 新增 */
	public Integer insertLinksusTaskAttentionUser(LinksusTaskAttentionUser entity){
		return linksusTaskAttentionUserMapper.insertLinksusTaskAttentionUser(entity);
	}

	/** 更新 */
	public Integer updateLinksusTaskAttentionUser(LinksusTaskAttentionUser entity){
		return linksusTaskAttentionUserMapper.updateLinksusTaskAttentionUser(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusTaskAttentionUserById(Long pid){
		return linksusTaskAttentionUserMapper.deleteLinksusTaskAttentionUserById(pid);
	}

	/** 查询列表 */
	public List<LinksusTaskAttentionUser> getAllAttentionByUserList(LinksusTaskAttentionUser entity){
		return linksusTaskAttentionUserMapper.getAllAttentionByUserList(entity);
	}
}