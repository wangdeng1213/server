package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusTaskInvalidRecordMapper;
import com.linksus.entity.LinksusTaskInvalidRecord;
import com.linksus.service.LinksusTaskInvalidRecordService;

@Service("linksusTaskInvalidRecordService")
public class LinksusTaskInvalidRecordServiceImpl implements LinksusTaskInvalidRecordService{

	@Autowired
	private LinksusTaskInvalidRecordMapper linksusTaskInvalidRecordMapper;

	/** ��ѯ�б� */
	public List<LinksusTaskInvalidRecord> getLinksusTaskInvalidRecordList(){
		return linksusTaskInvalidRecordMapper.getLinksusTaskInvalidRecordList();
	}

	/** ������ѯ */
	public LinksusTaskInvalidRecord getLinksusTaskInvalidRecordById(Long pid){
		return linksusTaskInvalidRecordMapper.getLinksusTaskInvalidRecordById(pid);
	}

	/** ���� */
	public Integer insertLinksusTaskInvalidRecord(LinksusTaskInvalidRecord entity){
		return linksusTaskInvalidRecordMapper.insertLinksusTaskInvalidRecord(entity);
	}

	/** ���� */
	public Integer updateLinksusTaskInvalidRecord(LinksusTaskInvalidRecord entity){
		return linksusTaskInvalidRecordMapper.updateLinksusTaskInvalidRecord(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusTaskInvalidRecordById(Long pid){
		return linksusTaskInvalidRecordMapper.deleteLinksusTaskInvalidRecordById(pid);
	}
}