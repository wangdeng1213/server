package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusRelationOperateMapper;
import com.linksus.entity.LinksusRelationOperate;
import com.linksus.service.LinksusRelationOperateService;

@Service("linksusRelationOperateService")
public class LinksusRelationOperateServiceImpl implements LinksusRelationOperateService{

	@Autowired
	private LinksusRelationOperateMapper linksusRelationOperateMapper;

	/** ��ѯ�б� */
	public List<LinksusRelationOperate> getLinksusRelationOperateList(){
		return linksusRelationOperateMapper.getLinksusRelationOperateList();
	}

	/** ������ѯ */
	public LinksusRelationOperate getLinksusRelationOperateById(Long pid){
		return linksusRelationOperateMapper.getLinksusRelationOperateById(pid);
	}

	/** ���� */
	public Integer insertLinksusRelationOperate(LinksusRelationOperate entity){
		return linksusRelationOperateMapper.insertLinksusRelationOperate(entity);
	}

	/** ���� */
	public Integer updateLinksusRelationOperate(LinksusRelationOperate entity){
		return linksusRelationOperateMapper.updateLinksusRelationOperate(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusRelationOperateById(Long pid){
		return linksusRelationOperateMapper.deleteLinksusRelationOperateById(pid);
	}

	//�������²�����ʧ�ܴ���
	public Integer updateLinksusRelationOperFailNum(Long pid){
		return linksusRelationOperateMapper.updateLinksusRelationOperFailNum(pid);
	}

	//�������²����ɹ�����
	public Integer updateLinksusRelationOperSuccessNum(Long pid){
		return linksusRelationOperateMapper.updateLinksusRelationOperSuccessNum(pid);
	}

	public LinksusRelationOperate checkIsExsitRelationOperate(String paraStr){
		return linksusRelationOperateMapper.checkIsExsitRelationOperate(paraStr);

	}
}