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

	/** ��ѯ�б� */
	public List<LinksusTaskTotalUser> getLinksusTaskTotalUserList(){
		return linksusTaskTotalUserMapper.getLinksusTaskTotalUserList();
	}

	/** ������ѯ */
	public LinksusTaskTotalUser getLinksusTaskTotalUserById(LinksusTaskTotalUser totalUser){
		return linksusTaskTotalUserMapper.getLinksusTaskTotalUserById(totalUser);
	}

	/** ���� */
	public Integer insertLinksusTaskTotalUser(LinksusTaskTotalUser entity){
		return linksusTaskTotalUserMapper.insertLinksusTaskTotalUser(entity);
	}

	/** ���� */
	public Integer updateLinksusTaskTotalUser(LinksusTaskTotalUser entity){
		return linksusTaskTotalUserMapper.updateLinksusTaskTotalUser(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusTaskTotalUserById(Long pid){
		return linksusTaskTotalUserMapper.deleteLinksusTaskTotalUserById(pid);
	}

	/** ��ȡ��������δִ��ȫ�������*/
	public List<LinksusTaskTotalUser> getLinksusTaskTotalUsersByType(LinksusTaskTotalUser totalUser){
		return linksusTaskTotalUserMapper.getLinksusTaskTotalUsersByType(totalUser);
	}

	/** ��������״̬*/
	public Integer updateLinksusTaskTotalUsersStatus(LinksusTaskTotalUser totalUser){
		return linksusTaskTotalUserMapper.updateLinksusTaskTotalUsersStatus(totalUser);
	}

	/** �������һ���û�id */
	public Integer updateLinksusTaskTotalUsersCursor(LinksusTaskTotalUser totalUser){
		return linksusTaskTotalUserMapper.updateLinksusTaskTotalUsersCursor(totalUser);
	}
}