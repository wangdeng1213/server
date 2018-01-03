package com.linksus.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusInteractWeixinMapper;
import com.linksus.entity.LinksusInteractWeixin;
import com.linksus.service.LinksusInteractWeixinService;

@Service("linksusInteractWeixinService")
public class LinksusInteractWeixinServiceImpl implements LinksusInteractWeixinService{

	@Autowired
	private LinksusInteractWeixinMapper linksusInteractWeixinMapper;

	/** 查询列表 */
	public List<LinksusInteractWeixin> getLinksusInteractWeixinList(){
		return linksusInteractWeixinMapper.getLinksusInteractWeixinList();
	}

	/** 主键查询 */
	public LinksusInteractWeixin getLinksusInteractWeixinById(Long pid){
		return linksusInteractWeixinMapper.getLinksusInteractWeixinById(pid);
	}

	/** 新增 */
	public Integer insertLinksusInteractWeixin(LinksusInteractWeixin entity){
		return linksusInteractWeixinMapper.insertLinksusInteractWeixin(entity);
	}

	/** 更新 */
	public Integer updateLinksusInteractWeixin(LinksusInteractWeixin entity){
		return linksusInteractWeixinMapper.updateLinksusInteractWeixin(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusInteractWeixinById(Long pid){
		return linksusInteractWeixinMapper.deleteLinksusInteractWeixinById(pid);
	}

	/** 多参数查询 */
	public LinksusInteractWeixin getLinksusInteractWeixinByMap(Map map){
		return linksusInteractWeixinMapper.getLinksusInteractWeixinByMap(map);
	}

	/** 查询即时回复微信任务列表 */
	public List<LinksusInteractWeixin> getTaskWeixinAtImmediate(LinksusInteractWeixin entity){
		return linksusInteractWeixinMapper.getTaskWeixinAtImmediate(entity);
	}

	/** 查询定时回复微信任务列表 */
	public List<LinksusInteractWeixin> getTaskWeixinAtRegularTime(LinksusInteractWeixin entity){
		return linksusInteractWeixinMapper.getTaskWeixinAtRegularTime(entity);
	}

	public List getInteractWeixinListByIds(List weixinMsgs){
		return linksusInteractWeixinMapper.getInteractWeixinListByIds(weixinMsgs);
	}

	/**  修改发布状态、互动时间*/
	public Integer updateLinksusInteractWeixinStatus(LinksusInteractWeixin entity){
		return linksusInteractWeixinMapper.updateLinksusInteractWeixinStatus(entity);
	}

}