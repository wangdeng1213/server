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

	/** 查询列表 */
	public List<LinksusTaskInvalidRecord> getLinksusTaskInvalidRecordList(){
		return linksusTaskInvalidRecordMapper.getLinksusTaskInvalidRecordList();
	}

	/** 主键查询 */
	public LinksusTaskInvalidRecord getLinksusTaskInvalidRecordById(Long pid){
		return linksusTaskInvalidRecordMapper.getLinksusTaskInvalidRecordById(pid);
	}

	/** 新增 */
	public Integer insertLinksusTaskInvalidRecord(LinksusTaskInvalidRecord entity){
		return linksusTaskInvalidRecordMapper.insertLinksusTaskInvalidRecord(entity);
	}

	/** 更新 */
	public Integer updateLinksusTaskInvalidRecord(LinksusTaskInvalidRecord entity){
		return linksusTaskInvalidRecordMapper.updateLinksusTaskInvalidRecord(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusTaskInvalidRecordById(Long pid){
		return linksusTaskInvalidRecordMapper.deleteLinksusTaskInvalidRecordById(pid);
	}
}