package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusRelationPersonInfoMapper;
import com.linksus.entity.LinksusRelationPersonInfo;
import com.linksus.service.LinksusRelationPersonInfoService;

@Service("linksusRelationPersonInfoService")
public class LinksusRelationPersonInfoServiceImpl implements LinksusRelationPersonInfoService{

	@Autowired
	private LinksusRelationPersonInfoMapper linksusRelationPersonInfoMapper;

	/** ��ѯ�б� */
	public List<LinksusRelationPersonInfo> getLinksusRelationPersonInfoList(){
		return linksusRelationPersonInfoMapper.getLinksusRelationPersonInfoList();
	}

	/** ������ѯ */
	public LinksusRelationPersonInfo getLinksusRelationPersonInfoById(Long pid){
		return linksusRelationPersonInfoMapper.getLinksusRelationPersonInfoById(pid);
	}

	/** ���� */
	public Integer insertLinksusRelationPersonInfo(LinksusRelationPersonInfo entity){
		return linksusRelationPersonInfoMapper.insertLinksusRelationPersonInfo(entity);
	}

	/** ���� */
	public Integer updateLinksusRelationPersonInfo(LinksusRelationPersonInfo entity){
		return linksusRelationPersonInfoMapper.updateLinksusRelationPersonInfo(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusRelationPersonInfoById(Long pid){
		return linksusRelationPersonInfoMapper.deleteLinksusRelationPersonInfoById(pid);
	}

}