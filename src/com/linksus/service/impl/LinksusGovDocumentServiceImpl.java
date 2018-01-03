package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusGovDocumentMapper;
import com.linksus.entity.LinksusGovDocument;
import com.linksus.service.LinksusGovDocumentService;

@Service("linksusGovDocumentService")
public class LinksusGovDocumentServiceImpl implements LinksusGovDocumentService{

	@Autowired
	private LinksusGovDocumentMapper linksusGovDocumentMapper;

	/** 查询列表 */
	public List<LinksusGovDocument> getLinksusGovDocumentList(){
		return linksusGovDocumentMapper.getLinksusGovDocumentList();
	}

	/** 主键查询 */
	public LinksusGovDocument getLinksusGovDocumentById(Long pid){
		return linksusGovDocumentMapper.getLinksusGovDocumentById(pid);
	}

	/** 新增 */
	public Integer insertLinksusGovDocument(LinksusGovDocument entity){
		return linksusGovDocumentMapper.insertLinksusGovDocument(entity);
	}

	/** 更新 */
	public Integer updateLinksusGovDocument(LinksusGovDocument entity){
		return linksusGovDocumentMapper.updateLinksusGovDocument(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusGovDocumentById(Long pid){
		return linksusGovDocumentMapper.deleteLinksusGovDocumentById(pid);
	}

	@Override
	public List<LinksusGovDocument> getLinksusGovDocumentListByAccount(Long accountId){
		return linksusGovDocumentMapper.getLinksusGovDocumentListByAccount(accountId);
	}
}