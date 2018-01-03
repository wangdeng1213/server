package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusTaskIncrementUserMapper;
import com.linksus.entity.LinksusTaskIncrementUser;
import com.linksus.service.LinksusTaskIncrementUserService;

@Service("linksusTaskIncrementUserService")
public class LinksusTaskIncrementUserServiceImpl implements LinksusTaskIncrementUserService{

	@Autowired
	private LinksusTaskIncrementUserMapper linksusTaskIncrementUserMapper;

	/** 查询列表 */
	public List<LinksusTaskIncrementUser> getLinksusTaskIncrementUserList(){
		return linksusTaskIncrementUserMapper.getLinksusTaskIncrementUserList();
	}

	/** 主键查询 */
	public LinksusTaskIncrementUser getTaskIncrementUserByAccountId(Long pid){
		return linksusTaskIncrementUserMapper.getTaskIncrementUserByAccountId(pid);
	}

	/** 新增 */
	public Integer insertLinksusTaskIncrementUser(LinksusTaskIncrementUser entity){
		return linksusTaskIncrementUserMapper.insertLinksusTaskIncrementUser(entity);
	}

	/** 更新 */
	public Integer updateLinksusTaskIncrementUser(LinksusTaskIncrementUser entity){
		return linksusTaskIncrementUserMapper.updateLinksusTaskIncrementUser(entity);
	}

	/** 更新 */
	public Integer updateLinksusTaskIncrementUserInfo(LinksusTaskIncrementUser entity){
		return linksusTaskIncrementUserMapper.updateLinksusTaskIncrementUserInfo(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusTaskIncrementUserById(Long pid){
		return linksusTaskIncrementUserMapper.deleteLinksusTaskIncrementUserById(pid);
	}

	public Integer getCountByaccountId(Long pid){
		return linksusTaskIncrementUserMapper.getCountByaccountId(pid);
	}

	//查询账号的增量粉丝任务列表和账号的第三方id 
	public List<LinksusTaskIncrementUser> getIncrementalUserTaskList(LinksusTaskIncrementUser entity){

		return linksusTaskIncrementUserMapper.getIncrementalUserTaskList(entity);
	}

	//更新增量粉丝
	public Integer updateLinksusTaskIncrementFans(LinksusTaskIncrementUser entity){
		return linksusTaskIncrementUserMapper.updateLinksusTaskIncrementFans(entity);
	}

	//更新增量关注
	public Integer updateLinksusTaskIncrementFollow(LinksusTaskIncrementUser entity){
		return linksusTaskIncrementUserMapper.updateLinksusTaskIncrementFollow(entity);
	}
}