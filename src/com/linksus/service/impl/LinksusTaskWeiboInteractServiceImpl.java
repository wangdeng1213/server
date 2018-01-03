package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusTaskWeiboInteractMapper;
import com.linksus.entity.LinksusTaskWeiboInteract;
import com.linksus.service.BaseService;
import com.linksus.service.LinksusTaskWeiboInteractService;

@Service("linksusTaskWeiboInteractService")
public class LinksusTaskWeiboInteractServiceImpl extends BaseService implements LinksusTaskWeiboInteractService{

	@Autowired
	private LinksusTaskWeiboInteractMapper linksusTaskWeiboInteractMapper;

	/** ��ѯ�б� */
	public List<LinksusTaskWeiboInteract> getLinksusTaskWeiboInteractList(){
		return linksusTaskWeiboInteractMapper.getLinksusTaskWeiboInteractList();
	}

	/** ������ѯ */
	public LinksusTaskWeiboInteract getLinksusTaskWeiboInteractById(Long pid){
		return linksusTaskWeiboInteractMapper.getLinksusTaskWeiboInteractById(pid);
	}

	/** ���� */
	public Integer insertLinksusTaskWeiboInteract(LinksusTaskWeiboInteract entity){
		return linksusTaskWeiboInteractMapper.insertLinksusTaskWeiboInteract(entity);
	}

	/** ���� */
	public Integer updateLinksusTaskWeiboInteract(LinksusTaskWeiboInteract entity){
		return linksusTaskWeiboInteractMapper.updateLinksusTaskWeiboInteract(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusTaskWeiboInteractById(Long pid){
		return linksusTaskWeiboInteractMapper.deleteLinksusTaskWeiboInteractById(pid);
	}

	public LinksusTaskWeiboInteract getMaxIdByAccountId(LinksusTaskWeiboInteract linksusTaskWeiboInteract){
		return linksusTaskWeiboInteractMapper.getMaxIdByAccountId(linksusTaskWeiboInteract);

	}

	/** ���õ�ǰ�˺ŷ�ҳ��Ϣ*/ 
	public Integer updateLinksusTaskWeiboInteractPageInfo(LinksusTaskWeiboInteract linksusTaskWeiboInteract){
		return linksusTaskWeiboInteractMapper.updateLinksusTaskWeiboInteractPageInfo(linksusTaskWeiboInteract);
	}
}