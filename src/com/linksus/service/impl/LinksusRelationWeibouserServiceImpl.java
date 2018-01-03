package com.linksus.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.common.Constants;
import com.linksus.common.util.LogUtil;
import com.linksus.dao.LinksusRelationWeibouserMapper;
import com.linksus.entity.LinksusRelationWeibouser;
import com.linksus.service.BaseService;
import com.linksus.service.LinksusRelationWeibouserService;

@Service("linksusRelationWeibouserService")
public class LinksusRelationWeibouserServiceImpl extends BaseService implements LinksusRelationWeibouserService{

	@Autowired
	private LinksusRelationWeibouserMapper linksusRelationWeibouserMapper;

	/** 根据找好类型查询列表 */
	public List<LinksusRelationWeibouser> getLinksusRelationWeibouserList(String accountType){
		return linksusRelationWeibouserMapper.getLinksusRelationWeibouserList(accountType);
	}

	/** 主键查询 */
	public LinksusRelationWeibouser getLinksusRelationWeibouserById(Long pid){
		return linksusRelationWeibouserMapper.getLinksusRelationWeibouserById(pid);
	}

	/** 新增 */
	/*
	 * public void saveLinksusRelationWeibouser(List userList,String operType) {
	 * }
	 */

	/** 更新 */
	/*
	 * public void updateLinksusRelationWeibouser( LinksusRelationWeibouser
	 * entity) { return linksusRelationWeibouserMapper
	 * .updateLinksusRelationWeibouser(entity); }
	 */

	/** 主键删除 */
	public Integer deleteLinksusRelationWeibouserById(Long pid){
		return linksusRelationWeibouserMapper.deleteLinksusRelationWeibouserById(pid);
	}

	//查询一个月以前的微博用户数据 执行更新
	public List<LinksusRelationWeibouser> getLinksusWeibouserListByTime(LinksusRelationWeibouser entity){
		return linksusRelationWeibouserMapper.getLinksusWeibouserListByTime(entity);

	}

	/**
	 * 查询指定天数以前的微博用户
	 * 
	 */
	public List<LinksusRelationWeibouser> getLinksusWeibouserListByLimitDay(LinksusRelationWeibouser entity){
		return linksusRelationWeibouserMapper.getLinksusWeibouserListByDay(entity);
	}

	/**
	 * 更新微博粉丝的最后更新时间
	 * 
	 */
	public Integer updateWeibouserLastSytime(LinksusRelationWeibouser entity){
		return linksusRelationWeibouserMapper.updateWeibouserLastSytime(entity);
	}

	/**
	 * 更新微博粉丝的最后更新时间
	 * 
	 */
	public void updateWeibouserLastSytimeList(List<LinksusRelationWeibouser> dataList,String operType){
		try{
			this.saveEntity(dataList, operType);
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
	}

	/**
	 * 更新微博粉丝的微博最大ID
	 * 
	 */
	public Integer updateWeibouserLastWeiboId(LinksusRelationWeibouser entity){
		return linksusRelationWeibouserMapper.updateWeibouserLastWeiboId(entity);
	}

	public List<LinksusRelationWeibouser> getLinksusTencentWeibouserList(LinksusRelationWeibouser entity){
		return linksusRelationWeibouserMapper.getLinksusTencentWeibouserList(entity);
	}

	public Integer updateLinksusRelationWeibouserById(LinksusRelationWeibouser entity){
		return linksusRelationWeibouserMapper.updateLinksusRelationWeibouserById(entity);
	}

	public int getCountByrpsIdAndType(Map map){

		return linksusRelationWeibouserMapper.getCountByrpsIdAndType(map).intValue();
	}

	public LinksusRelationWeibouser getWeibouserByrpsIdAndType(Map paraMap){

		return linksusRelationWeibouserMapper.getWeibouserByrpsIdAndType(paraMap);
	}

	public Integer updateSinaLinksusRelationWeibouser(LinksusRelationWeibouser entity){
		return linksusRelationWeibouserMapper.updateSinaLinksusRelationWeibouser(entity);
	}

	public Integer updateCententLinksusRelationWeibouser(LinksusRelationWeibouser entity){

		return linksusRelationWeibouserMapper.updateCententLinksusRelationWeibouser(entity);
	}

	/** 业务主键查询 */
	public LinksusRelationWeibouser getLinksusRelationWeibouser(LinksusRelationWeibouser dto){
		return linksusRelationWeibouserMapper.getLinksusRelationWeibouser(dto);
	}

	@Override
	public LinksusRelationWeibouser getRelationWeibouserByUserId(long parseLong){
		// TODO Auto-generated method stub
		return linksusRelationWeibouserMapper.getRelationWeibouserByUserId(parseLong);
	}

	public Integer checkUserIsExsit(Map paraMap){
		return linksusRelationWeibouserMapper.checkUserIsExsit(paraMap);
	}

	/** 更新PersonId */
	public Integer updatePersonId(LinksusRelationWeibouser entity){
		return linksusRelationWeibouserMapper.updatePersonId(entity);
	}

	//列表
	public List<LinksusRelationWeibouser> getLinksusWeibouserListByMap(Map map){
		return linksusRelationWeibouserMapper.getLinksusWeibouserListByMap(map);
	}

	/** 查询新浪账户的昵称 */
	public String getWeibouserNameByAccountId(Long accountId){
		return linksusRelationWeibouserMapper.getWeibouserNameByAccountId(accountId);
	}

	@Override
	public void saveEntity(List dataList,String operType) throws Exception{
		try{
			for(int i = 0; i < dataList.size(); i++){
				LinksusRelationWeibouser entity = (LinksusRelationWeibouser) dataList.get(i);
				if(entity.getWeiboCreateTime() == null){
					entity.setWeiboCreateTime(0);
				}
				if(entity.getWeiboLastMid() == null){
					entity.setWeiboLastMid(0L);
				}
				if(entity.getWeiboLastSytime() == null){
					entity.setWeiboLastSytime(0);
				}
				//logger.info(">>>>>>>>>>>>>>>>保存用户数据:{}-{}",operType,entity.getUserId());
				if(operType.equals(Constants.OPER_TYPE_INSERT)){
					sqlSession.insert("com.linksus.dao.LinksusRelationWeibouserMapper.insertLinksusRelationWeibouser",
							entity);
				}else if(operType.equals(Constants.OPER_TYPE_UPDATE)){
					sqlSession.update("com.linksus.dao.LinksusRelationWeibouserMapper.updateLinksusRelationWeibouser",
							entity);
				}else if(operType.equals("updateLastSytime")){
					sqlSession.update("com.linksus.dao.LinksusRelationWeibouserMapper.updateWeibouserLastSytime",
							entity);
				}else if(operType.equals("updatePagetimeAndMid")){
					sqlSession.update("com.linksus.dao.LinksusRelationWeibouserMapper.updateWeibouserLastWeiboId",
							entity);
				}else if(operType.equals("updateSinaWeiboUser")){
					sqlSession
							.update(
									"com.linksus.dao.LinksusRelationWeibouserMapper.updateSinaLinksusRelationWeibouser",
									entity);
				}else if(operType.equals("updateCententWeiboUser")){
					sqlSession.update(
							"com.linksus.dao.LinksusRelationWeibouserMapper.updateCententLinksusRelationWeibouser",
							entity);
				}
			}
		}catch (Exception e){
			//判断类型为新增的 删除缓存
			if(operType.equals(Constants.OPER_TYPE_INSERT)){
				for(int i = 0; i < dataList.size(); i++){
					removeRedisKey(dataList.get(i), operType);
				}
			}
			throw e;
		}
	}

}