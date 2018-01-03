package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusWxValidateMapper;
import com.linksus.entity.LinksusWxValidate;
import com.linksus.service.LinksusWxValidateService;

@Service("linksusWxValidateService")
public class LinksusWxValidateServiceImpl implements LinksusWxValidateService{

	@Autowired
	private LinksusWxValidateMapper linksusWxValidateMapper;

	/** ��ѯ�б� */
	public List<LinksusWxValidate> getLinksusWxValidateList(){
		return linksusWxValidateMapper.getLinksusWxValidateList();
	}

	/** ������ѯ */
	public LinksusWxValidate getLinksusWxValidateById(Long pid){
		return linksusWxValidateMapper.getLinksusWxValidateById(pid);
	}

	/** ���� */
	public Integer insertLinksusWxValidate(LinksusWxValidate entity){
		return linksusWxValidateMapper.insertLinksusWxValidate(entity);
	}

	/** ���� */
	public Integer updateLinksusWxValidate(LinksusWxValidate entity){
		return linksusWxValidateMapper.updateLinksusWxValidate(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusWxValidateById(Long pid){
		return linksusWxValidateMapper.deleteLinksusWxValidateById(pid);
	}

	/** ͨ��΢�źŲ�ѯ*/
	public LinksusWxValidate getLinksusWxValidateByWxnum(String wxnum){
		return linksusWxValidateMapper.getLinksusWxValidateByWxnum(wxnum);
	}
}