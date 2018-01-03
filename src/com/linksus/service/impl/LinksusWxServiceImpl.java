package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusWxMapper;
import com.linksus.entity.LinksusWx;
import com.linksus.service.LinksusWxService;

@Service("linksusWxService")
public class LinksusWxServiceImpl implements LinksusWxService{

	@Autowired
	private LinksusWxMapper linksusWxMapper;

	/** 查询列表 */
	public List<LinksusWx> getLinksusWxList(){
		return linksusWxMapper.getLinksusWxList();
	}

	/** 主键查询 */
	public LinksusWx getLinksusWxById(Long pid){
		return linksusWxMapper.getLinksusWxById(pid);
	}

	/** 新增 */
	public Integer insertLinksusWx(LinksusWx entity){
		return linksusWxMapper.insertLinksusWx(entity);
	}

	/** 更新 */
	public Integer updateLinksusWx(LinksusWx entity){
		return linksusWxMapper.updateLinksusWx(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusWxById(Long pid){
		return linksusWxMapper.deleteLinksusWxById(pid);
	}

	/** 查询所有状态为未执行状态的微信任务 */
	public List<LinksusWx> getWeiXinTaskList(LinksusWx entity){
		return linksusWxMapper.getWeiXinTaskList(entity);
	}

	/** 修改任务状态 */
	public void updateWeiXinTaskList(LinksusWx entity){
		linksusWxMapper.updateWeiXinTaskList(entity);
	}

	/** 获取微信群发信息用户token及信息 */
	public LinksusWx getLinksusWXUserAndInfo(Long pid){
		return linksusWxMapper.getLinksusWXUserAndInfo(pid);
	}

	/**  修改微信发布结果*/
	public void updateWeiXinTaskErrmsg(LinksusWx entity){
		linksusWxMapper.updateWeiXinTaskErrmsg(entity);
	}

	/**  通过微信群发信息表修改微信发布结果*/
	public void updateWeiXinTaskErrmsgByGroup(Long pid){
		linksusWxMapper.updateWeiXinTaskErrmsgByGroup(pid);
	}

	/** 修改微信发布结果及状态*/
	public void updateWeiXinTaskstatusAndErrmsg(LinksusWx entity){
		linksusWxMapper.updateWeiXinTaskstatusAndErrmsg(entity);
	}
}