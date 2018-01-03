package com.linksus.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusTypeAreaCodeMapper;
import com.linksus.entity.LinksusTypeAreaCode;
import com.linksus.service.LinksusTypeAreaCodeService;

@Service("linksusTypeAreaCodeService")
public class LinksusTypeAreaCodeServiceImpl implements LinksusTypeAreaCodeService{

	@Autowired
	private LinksusTypeAreaCodeMapper linksusTypeAreaCodeMapper;

	/** ��ѯ�б� */
	public List<LinksusTypeAreaCode> getLinksusTypeAreaCodeList(){
		return linksusTypeAreaCodeMapper.getLinksusTypeAreaCodeList();
	}

	/** ������ѯ */
	public LinksusTypeAreaCode getLinksusTypeAreaCodeById(Long pid){
		return linksusTypeAreaCodeMapper.getLinksusTypeAreaCodeById(pid);
	}

	/** ���� */
	public Integer insertLinksusTypeAreaCode(LinksusTypeAreaCode entity){
		return linksusTypeAreaCodeMapper.insertLinksusTypeAreaCode(entity);
	}

	/** ���� */
	public Integer updateLinksusTypeAreaCode(LinksusTypeAreaCode entity){
		return linksusTypeAreaCodeMapper.updateLinksusTypeAreaCode(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusTypeAreaCodeById(Long pid){
		return linksusTypeAreaCodeMapper.deleteLinksusTypeAreaCodeById(pid);
	}

	/** ��ȡ��������*/
	public List<LinksusTypeAreaCode> getLinksusTypeAreaCodeListByCode(Map codeMap){
		return linksusTypeAreaCodeMapper.getLinksusTypeAreaCodeListByCode(codeMap);

	}

	/** ������������ */
	public void replaceLinksusTypeAreaCodeInfo(List allArea){
		linksusTypeAreaCodeMapper.replaceLinksusTypeAreaCodeInfo(allArea);
	}

	/** �����ѯ */
	public LinksusTypeAreaCode getLinksusTypeAreaCode(Map codeMap){
		return linksusTypeAreaCodeMapper.getLinksusTypeAreaCode(codeMap);
	}

	public LinksusTypeAreaCode getProvicesInfo(Map codeMap){
		return linksusTypeAreaCodeMapper.getProvicesInfo(codeMap);
	}

	public Integer updateSinaAreaCodeByCode(Map codeMap){
		return linksusTypeAreaCodeMapper.updateSinaAreaCodeByCode(codeMap);
	}
}