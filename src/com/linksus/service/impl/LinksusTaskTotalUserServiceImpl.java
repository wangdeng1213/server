package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusTaskTotalUserMapper;
import com.linksus.entity.LinksusTaskTotalUser;
import com.linksus.service.LinksusTaskTotalUserService;

@Service("linksusTaskTotalUserService")
public class LinksusTaskTotalUserServiceImpl implements LinksusTaskTotalUserService{

	@Autowired
	private LinksusTaskTotalUserMapper linksusTaskTotalUserMapper;

	/** 查询列表 */
	public List<LinksusTaskTotalUser> getLinksusTaskTotalUserList(){
		return linksusTaskTotalUserMapper.getLinksusTaskTotalUserList();
	}

	/** 主键查询 */
	public LinksusTaskTotalUser getLinksusTaskTotalUserById(LinksusTaskTotalUser totalUser){
		return linksusTaskTotalUserMapper.getLinksusTaskTotalUserById(totalUser);
	}

	/** 新增 */
	public Integer insertLinksusTaskTotalUser(LinksusTaskTotalUser entity){
		return linksusTaskTotalUserMapper.insertLinksusTaskTotalUser(entity);
	}

	/** 更新 */
	public Integer updateLinksusTaskTotalUser(LinksusTaskTotalUser entity){
		return linksusTaskTotalUserMapper.updateLinksusTaskTotalUser(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusTaskTotalUserById(Long pid){
		return linksusTaskTotalUserMapper.deleteLinksusTaskTotalUserById(pid);
	}

	/** 获取根据类型未执行全量任务表*/
	public List<LinksusTaskTotalUser> getLinksusTaskTotalUsersByType(LinksusTaskTotalUser totalUser){
		return linksusTaskTotalUserMapper.getLinksusTaskTotalUsersByType(totalUser);
	}

	/** 更新任务状态*/
	public Integer updateLinksusTaskTotalUsersStatus(LinksusTaskTotalUser totalUser){
		return linksusTaskTotalUserMapper.updateLinksusTaskTotalUsersStatus(totalUser);
	}

	/** 更新最后一条用户id */
	public Integer updateLinksusTaskTotalUsersCursor(LinksusTaskTotalUser totalUser){
		return linksusTaskTotalUserMapper.updateLinksusTaskTotalUsersCursor(totalUser);
	}
}