package com.linksus.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.common.Constants;
import com.linksus.dao.LinksusWeiboPoolMapper;
import com.linksus.entity.LinksusWeibo;
import com.linksus.entity.LinksusWeiboPool;
import com.linksus.service.BaseService;
import com.linksus.service.LinksusWeiboPoolService;

@Service("linksusWeiboPoolService")
public class LinksusWeiboPoolServiceImpl extends BaseService implements LinksusWeiboPoolService{

	@Autowired
	private LinksusWeiboPoolMapper linksusWeiboPoolMapper;

	/** 查询列表 */
	public List<LinksusWeiboPool> getLinksusWeiboPoolList(){
		return linksusWeiboPoolMapper.getLinksusWeiboPoolList();
	}

	/** 主键查询 */
	public LinksusWeiboPool getLinksusWeiboPoolById(Long pid){
		return linksusWeiboPoolMapper.getLinksusWeiboPoolById(pid);
	}

	//	/** 新增 */
	//	public Integer insertLinksusWeiboPool(LinksusWeiboPool entity){
	//		return linksusWeiboPoolMapper.insertLinksusWeiboPool(entity);
	//	}

	//	/** 更新 */
	//	public Integer updateLinksusWeiboPool(LinksusWeiboPool entity){
	//		return linksusWeiboPoolMapper.updateLinksusWeiboPool(entity);
	//	}

	/** 微博互动更新微博评论数和转发数*/
	public Integer updateLinksusWeiboPoolDataCount(LinksusWeiboPool entity){
		return linksusWeiboPoolMapper.updateLinksusWeiboPoolDataCount(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusWeiboPoolById(Long pid){
		return linksusWeiboPoolMapper.deleteLinksusWeiboPoolById(pid);
	}

	public List<LinksusWeibo> getLinksusWeiboPoolSend(LinksusWeibo entity){
		return linksusWeiboPoolMapper.getLinksusWeiboPoolSend(entity);
	}

	public LinksusWeibo getLinksusWeiboPoolByMap(Map map){
		return linksusWeiboPoolMapper.getLinksusWeiboPoolByMap(map);
	}

	public Integer updateLinksusWeiboPoolCount(LinksusWeibo entity){
		return linksusWeiboPoolMapper.updateLinksusWeiboPoolCount(entity);
	}

	/** 通过mid 和 accountType 判断微博是否存在*/
	public LinksusWeiboPool checkWeiboPoolIsExsit(Map paraMap){
		return linksusWeiboPoolMapper.checkWeiboPoolIsExsit(paraMap);
	}

	//批量插入微博池数据
	public void saveEntity(List dataList,String operType) throws Exception{
		try{
			for(int i = 0; i < dataList.size(); i++){
				LinksusWeiboPool entity = (LinksusWeiboPool) dataList.get(i);
				if(operType.equals(Constants.OPER_TYPE_INSERT)){
					sqlSession.insert("com.linksus.dao.LinksusWeiboPoolMapper.insertLinksusWeiboPool", entity);
				}else if(operType.equals(Constants.OPER_TYPE_UPDATE)){
					sqlSession.update("com.linksus.dao.LinksusWeiboPoolMapper.updateLinksusWeiboPoolCount", entity);
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