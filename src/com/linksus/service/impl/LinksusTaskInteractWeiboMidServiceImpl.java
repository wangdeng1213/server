package com.linksus.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusTaskInteractWeiboMidMapper;
import com.linksus.entity.LinksusTaskInteractWeiboMid;
import com.linksus.service.LinksusTaskInteractWeiboMidService;

@Service("linksusTaskInteractWeiboMidService")
public class LinksusTaskInteractWeiboMidServiceImpl implements LinksusTaskInteractWeiboMidService{

	@Autowired
	private LinksusTaskInteractWeiboMidMapper linksusTaskInteractWeiboMidMapper;

	/** 查询列表 */
	public List<LinksusTaskInteractWeiboMid> getLinksusTaskInteractWeiboMidList(){
		return linksusTaskInteractWeiboMidMapper.getLinksusTaskInteractWeiboMidList();
	}

	/** 主键查询 */
	public LinksusTaskInteractWeiboMid getLinksusTaskInteractWeiboMidById(Long pid){
		return linksusTaskInteractWeiboMidMapper.getLinksusTaskInteractWeiboMidById(pid);
	}

	/** 新增 */
	public Integer insertLinksusTaskInteractWeiboMid(LinksusTaskInteractWeiboMid entity){
		Integer insertResult = 0;
		try{
			insertResult = linksusTaskInteractWeiboMidMapper.insertLinksusTaskInteractWeiboMid(entity);
			return insertResult;
		}catch (Exception e){
			return insertResult;
		}
	}

	/** 更新 */
	public Integer updateLinksusTaskInteractWeiboMid(LinksusTaskInteractWeiboMid entity){
		return linksusTaskInteractWeiboMidMapper.updateLinksusTaskInteractWeiboMid(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusTaskInteractWeiboMidById(Long pid){
		return linksusTaskInteractWeiboMidMapper.deleteLinksusTaskInteractWeiboMidById(pid);
	}

	/**根据类型和账号查询出临时表中互动的数据*/
	public List<LinksusTaskInteractWeiboMid> getWeiboMidTempListByMap(Map map){
		return linksusTaskInteractWeiboMidMapper.getWeiboMidTempListByMap(map);
	}

	/** 清除临时表中数据  */
	public void deleteLinksusTaskInteractWeiboMid(){
		linksusTaskInteractWeiboMidMapper.deleteLinksusTaskInteractWeiboMid();
	}

}