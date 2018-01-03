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

	/** ��ѯ�б� */
	public List<LinksusTaskIncrementUser> getLinksusTaskIncrementUserList(){
		return linksusTaskIncrementUserMapper.getLinksusTaskIncrementUserList();
	}

	/** ������ѯ */
	public LinksusTaskIncrementUser getTaskIncrementUserByAccountId(Long pid){
		return linksusTaskIncrementUserMapper.getTaskIncrementUserByAccountId(pid);
	}

	/** ���� */
	public Integer insertLinksusTaskIncrementUser(LinksusTaskIncrementUser entity){
		return linksusTaskIncrementUserMapper.insertLinksusTaskIncrementUser(entity);
	}

	/** ���� */
	public Integer updateLinksusTaskIncrementUser(LinksusTaskIncrementUser entity){
		return linksusTaskIncrementUserMapper.updateLinksusTaskIncrementUser(entity);
	}

	/** ���� */
	public Integer updateLinksusTaskIncrementUserInfo(LinksusTaskIncrementUser entity){
		return linksusTaskIncrementUserMapper.updateLinksusTaskIncrementUserInfo(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusTaskIncrementUserById(Long pid){
		return linksusTaskIncrementUserMapper.deleteLinksusTaskIncrementUserById(pid);
	}

	public Integer getCountByaccountId(Long pid){
		return linksusTaskIncrementUserMapper.getCountByaccountId(pid);
	}

	//��ѯ�˺ŵ�������˿�����б���˺ŵĵ�����id 
	public List<LinksusTaskIncrementUser> getIncrementalUserTaskList(LinksusTaskIncrementUser entity){

		return linksusTaskIncrementUserMapper.getIncrementalUserTaskList(entity);
	}

	//����������˿
	public Integer updateLinksusTaskIncrementFans(LinksusTaskIncrementUser entity){
		return linksusTaskIncrementUserMapper.updateLinksusTaskIncrementFans(entity);
	}

	//����������ע
	public Integer updateLinksusTaskIncrementFollow(LinksusTaskIncrementUser entity){
		return linksusTaskIncrementUserMapper.updateLinksusTaskIncrementFollow(entity);
	}
}