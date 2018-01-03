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

	/** 查询列表 */
	public List<LinksusWxValidate> getLinksusWxValidateList(){
		return linksusWxValidateMapper.getLinksusWxValidateList();
	}

	/** 主键查询 */
	public LinksusWxValidate getLinksusWxValidateById(Long pid){
		return linksusWxValidateMapper.getLinksusWxValidateById(pid);
	}

	/** 新增 */
	public Integer insertLinksusWxValidate(LinksusWxValidate entity){
		return linksusWxValidateMapper.insertLinksusWxValidate(entity);
	}

	/** 更新 */
	public Integer updateLinksusWxValidate(LinksusWxValidate entity){
		return linksusWxValidateMapper.updateLinksusWxValidate(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusWxValidateById(Long pid){
		return linksusWxValidateMapper.deleteLinksusWxValidateById(pid);
	}

	/** 通过微信号查询*/
	public LinksusWxValidate getLinksusWxValidateByWxnum(String wxnum){
		return linksusWxValidateMapper.getLinksusWxValidateByWxnum(wxnum);
	}
}