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

	/** ��ѯ�б� */
	public List<LinksusTaskException> getLinksusTaskExceptionList(){
		return linksusTaskExceptionMapper.getLinksusTaskExceptionList();
	}

	/** ������ѯ */
	public LinksusTaskException getLinksusTaskExceptionById(Long pid){
		return linksusTaskExceptionMapper.getLinksusTaskExceptionById(pid);
	}

	/** ���� */
	public Integer insertLinksusTaskException(LinksusTaskException entity){
		return linksusTaskExceptionMapper.insertLinksusTaskException(entity);
	}

	/** ���� */
	public Integer updateLinksusTaskException(LinksusTaskException entity){
		return linksusTaskExceptionMapper.updateLinksusTaskException(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusTaskExceptionById(Long pid){
		return linksusTaskExceptionMapper.deleteLinksusTaskExceptionById(pid);
	}
}