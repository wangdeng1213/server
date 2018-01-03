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

	/** 查询列表 */
	public List<LinksusTaskWeiboInteract> getLinksusTaskWeiboInteractList(){
		return linksusTaskWeiboInteractMapper.getLinksusTaskWeiboInteractList();
	}

	/** 主键查询 */
	public LinksusTaskWeiboInteract getLinksusTaskWeiboInteractById(Long pid){
		return linksusTaskWeiboInteractMapper.getLinksusTaskWeiboInteractById(pid);
	}

	/** 新增 */
	public Integer insertLinksusTaskWeiboInteract(LinksusTaskWeiboInteract entity){
		return linksusTaskWeiboInteractMapper.insertLinksusTaskWeiboInteract(entity);
	}

	/** 更新 */
	public Integer updateLinksusTaskWeiboInteract(LinksusTaskWeiboInteract entity){
		return linksusTaskWeiboInteractMapper.updateLinksusTaskWeiboInteract(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusTaskWeiboInteractById(Long pid){
		return linksusTaskWeiboInteractMapper.deleteLinksusTaskWeiboInteractById(pid);
	}

	public LinksusTaskWeiboInteract getMaxIdByAccountId(LinksusTaskWeiboInteract linksusTaskWeiboInteract){
		return linksusTaskWeiboInteractMapper.getMaxIdByAccountId(linksusTaskWeiboInteract);

	}

	/** 设置当前账号翻页信息*/ 
	public Integer updateLinksusTaskWeiboInteractPageInfo(LinksusTaskWeiboInteract linksusTaskWeiboInteract){
		return linksusTaskWeiboInteractMapper.updateLinksusTaskWeiboInteractPageInfo(linksusTaskWeiboInteract);
	}
}