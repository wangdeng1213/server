package com.linksus.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.common.Constants;
import com.linksus.dao.LinksusInteractWeiboMapper;
import com.linksus.entity.LinksusInteractWeibo;
import com.linksus.service.BaseService;
import com.linksus.service.LinksusInteractWeiboService;

@Service("linksusInteractWeiboService")
public class LinksusInteractWeiboServiceImpl extends BaseService implements LinksusInteractWeiboService{

	@Autowired
	private LinksusInteractWeiboMapper linksusInteractWeiboMapper;

	/** 查询列表 */
	public List<LinksusInteractWeibo> getLinksusInteractWeiboList(){
		return linksusInteractWeiboMapper.getLinksusInteractWeiboList();
	}

	/** 主键查询 */
	public LinksusInteractWeibo getLinksusInteractWeiboById(Long pid){
		return linksusInteractWeiboMapper.getLinksusInteractWeiboById(pid);
	}

	/** 新增 */

	public Integer insertLinksusInteractWeibo(LinksusInteractWeibo entity){
		return linksusInteractWeiboMapper.insertLinksusInteractWeibo(entity);
	}

	/** 更新 */
	/*
	 * public Integer updateLinksusInteractWeibo(LinksusInteractWeibo entity){
	 * return linksusInteractWeiboMapper.updateLinksusInteractWeibo(entity); }
	 */

	/** 主键删除 */
	public Integer deleteLinksusInteractWeiboById(Long pid){
		return linksusInteractWeiboMapper.deleteLinksusInteractWeiboById(pid);
	}

	/** 主键删除 */
	public Integer deleteLinksusInteractWeiboByMap(Map map){
		return linksusInteractWeiboMapper.deleteLinksusInteractWeiboByMap(map);
	}

	/** <!--取出回复消息的任务列表  -->*/
	public List<LinksusInteractWeibo> getLinksusInteractWeiboTaskList(LinksusInteractWeibo entity){
		return linksusInteractWeiboMapper.getLinksusInteractWeiboTaskList(entity);

	}

	/** <!--取出回复消息的定时发布任务列表  -->*/
	/*
	 * public List<LinksusInteractWeibo>
	 * getWeiboReplyPrepare(LinksusInteractWeibo entity){ return
	 * linksusInteractWeiboMapper.getWeiboReplyPrepare(entity); }
	 */
	//更新返回的 信息
	public void updateSendReplySucc(LinksusInteractWeibo entity){
		linksusInteractWeiboMapper.updateSendReplySucc(entity);
	}

	@Override
	public void updateSendReplyStatus(LinksusInteractWeibo entity){
		linksusInteractWeiboMapper.updateSendReplyStatus(entity);
	}

	/**查询微博互动记录是否存在*/
	public LinksusInteractWeibo getInteractWeiboIsExists(Map map){
		return linksusInteractWeiboMapper.getInteractWeiboIsExists(map);
	}

	/** 根据mid查询评论列表 */
	public List getInteractWeiboListByMid(Map map){
		return linksusInteractWeiboMapper.getInteractWeiboListByMid(map);
	}

	/** 根据主键查询需要回复评论的微博*/
	public LinksusInteractWeibo getReplyWeiboById(Long pid){
		return linksusInteractWeiboMapper.getReplyWeiboById(pid);
	}

	public List getInteractWeiboListByIds(List weiboMsgs){
		return linksusInteractWeiboMapper.getInteractWeiboListByIds(weiboMsgs);
	}

	public void saveEntity(List dataList,String operType) throws Exception{
		try{
			for(int i = 0; i < dataList.size(); i++){
				LinksusInteractWeibo entity = (LinksusInteractWeibo) dataList.get(i);
				if(operType.equals(Constants.OPER_TYPE_INSERT)){
					sqlSession.insert("com.linksus.dao.LinksusInteractWeiboMapper.insertLinksusInteractWeibo", entity);
				}
			}
		}catch (Exception e){
			throw e;
		}
	}
}