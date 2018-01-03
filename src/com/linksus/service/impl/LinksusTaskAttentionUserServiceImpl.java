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

	/** ��ѯ�б� */
	public List<LinksusTaskAttentionUser> getLinksusTaskAttentionUserList(){
		return linksusTaskAttentionUserMapper.getLinksusTaskAttentionUserList();
	}

	/** ������ѯ */
	public LinksusTaskAttentionUser getLinksusTaskAttentionUserById(Long pid){
		return linksusTaskAttentionUserMapper.getLinksusTaskAttentionUserById(pid);
	}

	/** �жϷ�˿�����Ƿ���� */
	public Integer countLinksusTaskAttentionUser(LinksusTaskAttentionUser entity){
		return linksusTaskAttentionUserMapper.countLinksusTaskAttentionUser(entity);
	}

	/** ���� */
	public Integer insertLinksusTaskAttentionUser(LinksusTaskAttentionUser entity){
		return linksusTaskAttentionUserMapper.insertLinksusTaskAttentionUser(entity);
	}

	/** ���� */
	public Integer updateLinksusTaskAttentionUser(LinksusTaskAttentionUser entity){
		return linksusTaskAttentionUserMapper.updateLinksusTaskAttentionUser(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusTaskAttentionUserById(Long pid){
		return linksusTaskAttentionUserMapper.deleteLinksusTaskAttentionUserById(pid);
	}

	/** ��ѯ�б� */
	public List<LinksusTaskAttentionUser> getAllAttentionByUserList(LinksusTaskAttentionUser entity){
		return linksusTaskAttentionUserMapper.getAllAttentionByUserList(entity);
	}
}