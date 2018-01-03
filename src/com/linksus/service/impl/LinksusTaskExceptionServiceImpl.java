package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusTaskExceptionMapper;
import com.linksus.entity.LinksusTaskException;
import com.linksus.service.LinksusTaskExceptionService;

@Service("linksusTaskExceptionService")
public class LinksusTaskExceptionServiceImpl implements LinksusTaskExceptionService{

	@Autowired
	private LinksusTaskExceptionMapper linksusTaskExceptionMapper;

	/** 查询列表 */
	public List<LinksusTaskException> getLinksusTaskExceptionList(){
		return linksusTaskExceptionMapper.getLinksusTaskExceptionList();
	}

	/** 主键查询 */
	public LinksusTaskException getLinksusTaskExceptionById(Long pid){
		return linksusTaskExceptionMapper.getLinksusTaskExceptionById(pid);
	}

	/** 新增 */
	public Integer insertLinksusTaskException(LinksusTaskException entity){
		return linksusTaskExceptionMapper.insertLinksusTaskException(entity);
	}

	/** 更新 */
	public Integer updateLinksusTaskException(LinksusTaskException entity){
		return linksusTaskExceptionMapper.updateLinksusTaskException(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusTaskExceptionById(Long pid){
		return linksusTaskExceptionMapper.deleteLinksusTaskExceptionById(pid);
	}
}