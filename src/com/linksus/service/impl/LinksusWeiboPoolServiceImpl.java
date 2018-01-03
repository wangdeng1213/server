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

	/** ��ѯ�б� */
	public List<LinksusWeiboPool> getLinksusWeiboPoolList(){
		return linksusWeiboPoolMapper.getLinksusWeiboPoolList();
	}

	/** ������ѯ */
	public LinksusWeiboPool getLinksusWeiboPoolById(Long pid){
		return linksusWeiboPoolMapper.getLinksusWeiboPoolById(pid);
	}

	//	/** ���� */
	//	public Integer insertLinksusWeiboPool(LinksusWeiboPool entity){
	//		return linksusWeiboPoolMapper.insertLinksusWeiboPool(entity);
	//	}

	//	/** ���� */
	//	public Integer updateLinksusWeiboPool(LinksusWeiboPool entity){
	//		return linksusWeiboPoolMapper.updateLinksusWeiboPool(entity);
	//	}

	/** ΢����������΢����������ת����*/
	public Integer updateLinksusWeiboPoolDataCount(LinksusWeiboPool entity){
		return linksusWeiboPoolMapper.updateLinksusWeiboPoolDataCount(entity);
	}

	/** ����ɾ�� */
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

	/** ͨ��mid �� accountType �ж�΢���Ƿ����*/
	public LinksusWeiboPool checkWeiboPoolIsExsit(Map paraMap){
		return linksusWeiboPoolMapper.checkWeiboPoolIsExsit(paraMap);
	}

	//��������΢��������
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
			//�ж�����Ϊ������ ɾ������
			if(operType.equals(Constants.OPER_TYPE_INSERT)){
				for(int i = 0; i < dataList.size(); i++){
					removeRedisKey(dataList.get(i), operType);
				}
			}
			throw e;
		}
	}
}