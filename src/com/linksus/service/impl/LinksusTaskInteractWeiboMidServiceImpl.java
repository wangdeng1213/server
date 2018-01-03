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

	/** ��ѯ�б� */
	public List<LinksusTaskInteractWeiboMid> getLinksusTaskInteractWeiboMidList(){
		return linksusTaskInteractWeiboMidMapper.getLinksusTaskInteractWeiboMidList();
	}

	/** ������ѯ */
	public LinksusTaskInteractWeiboMid getLinksusTaskInteractWeiboMidById(Long pid){
		return linksusTaskInteractWeiboMidMapper.getLinksusTaskInteractWeiboMidById(pid);
	}

	/** ���� */
	public Integer insertLinksusTaskInteractWeiboMid(LinksusTaskInteractWeiboMid entity){
		Integer insertResult = 0;
		try{
			insertResult = linksusTaskInteractWeiboMidMapper.insertLinksusTaskInteractWeiboMid(entity);
			return insertResult;
		}catch (Exception e){
			return insertResult;
		}
	}

	/** ���� */
	public Integer updateLinksusTaskInteractWeiboMid(LinksusTaskInteractWeiboMid entity){
		return linksusTaskInteractWeiboMidMapper.updateLinksusTaskInteractWeiboMid(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusTaskInteractWeiboMidById(Long pid){
		return linksusTaskInteractWeiboMidMapper.deleteLinksusTaskInteractWeiboMidById(pid);
	}

	/**�������ͺ��˺Ų�ѯ����ʱ���л���������*/
	public List<LinksusTaskInteractWeiboMid> getWeiboMidTempListByMap(Map map){
		return linksusTaskInteractWeiboMidMapper.getWeiboMidTempListByMap(map);
	}

	/** �����ʱ��������  */
	public void deleteLinksusTaskInteractWeiboMid(){
		linksusTaskInteractWeiboMidMapper.deleteLinksusTaskInteractWeiboMid();
	}

}