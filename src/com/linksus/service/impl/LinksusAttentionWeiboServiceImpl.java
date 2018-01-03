package com.linksus.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.common.Constants;
import com.linksus.dao.LinksusAttentionWeiboMapper;
import com.linksus.entity.LinksusAttentionWeibo;
import com.linksus.entity.LinksusWeibo;
import com.linksus.service.BaseService;
import com.linksus.service.LinksusAttentionWeiboService;

@Service("linksusAttentionWeiboService")
public class LinksusAttentionWeiboServiceImpl extends BaseService implements LinksusAttentionWeiboService{

	@Autowired
	private LinksusAttentionWeiboMapper linksusAttentionWeiboMapper;

	@Override
	public LinksusAttentionWeibo getLinksusAttention(){
		// TODO Auto-generated method stub
		return linksusAttentionWeiboMapper.getLinksusAttention();
	}

	@Override
	public void updateLinksusAttentionWeibo(List<LinksusAttentionWeibo> list){
		// TODO Auto-generated method stub
		linksusAttentionWeiboMapper.updateLinksusAttentionWeibo(list);

	}

	@Override
	public void updateLinksusAttentionWeiboSingle(LinksusAttentionWeibo linksusAttentionWeibo){
		linksusAttentionWeiboMapper.updateLinksusAttentionWeiboSingle(linksusAttentionWeibo);

	}

	@Override
	public List<LinksusWeibo> getLinksusAttentionWeiboHasSend(LinksusWeibo weibo){
		return linksusAttentionWeiboMapper.getLinksusAttentionWeiboHasSend(weibo);
	}

	@Override
	public LinksusWeibo getLinksusAttentionWeiboByMap(Map map){
		return linksusAttentionWeiboMapper.getLinksusAttentionWeiboByMap(map);
	}

	@Override
	public void insertLinksusAttentionWeibo(LinksusAttentionWeibo linksusAttentionWeibo){
		linksusAttentionWeiboMapper.insertLinksusAttentionWeibo(linksusAttentionWeibo);
	}

	//	@Override
	//	public void updateSendWeiboCount(LinksusWeibo linksusWeibo){
	//		// TODO Auto-generated method stub
	//		linksusAttentionWeiboMapper.updateSendWeiboCount(linksusWeibo);
	//	}

	//判断关注用户的微博是否存在
	public Integer getAttentionWeiboCount(LinksusAttentionWeibo linksusAttentionWeibo){
		return linksusAttentionWeiboMapper.getAttentionWeiboCount(linksusAttentionWeibo);
	}

	/**关注用户微博动态数据批量队列*/
	public void saveEntity(List dataList,String operType) throws Exception{
		try{
			for(int i = 0; i < dataList.size(); i++){
				LinksusAttentionWeibo entity = (LinksusAttentionWeibo) dataList.get(i);
				if(operType.equals(Constants.OPER_TYPE_UPDATE)){
					sqlSession.update("com.linksus.dao.LinksusAttentionWeiboMapper.updateSendWeiboCount", entity);
				}
			}
		}catch (Exception e){
			throw e;
		}
	}
}
