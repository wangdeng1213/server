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

	/** ��ѯ�б� */
	public List<LinksusInteractWeixin> getLinksusInteractWeixinList(){
		return linksusInteractWeixinMapper.getLinksusInteractWeixinList();
	}

	/** ������ѯ */
	public LinksusInteractWeixin getLinksusInteractWeixinById(Long pid){
		return linksusInteractWeixinMapper.getLinksusInteractWeixinById(pid);
	}

	/** ���� */
	public Integer insertLinksusInteractWeixin(LinksusInteractWeixin entity){
		return linksusInteractWeixinMapper.insertLinksusInteractWeixin(entity);
	}

	/** ���� */
	public Integer updateLinksusInteractWeixin(LinksusInteractWeixin entity){
		return linksusInteractWeixinMapper.updateLinksusInteractWeixin(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusInteractWeixinById(Long pid){
		return linksusInteractWeixinMapper.deleteLinksusInteractWeixinById(pid);
	}

	/** �������ѯ */
	public LinksusInteractWeixin getLinksusInteractWeixinByMap(Map map){
		return linksusInteractWeixinMapper.getLinksusInteractWeixinByMap(map);
	}

	/** ��ѯ��ʱ�ظ�΢�������б� */
	public List<LinksusInteractWeixin> getTaskWeixinAtImmediate(LinksusInteractWeixin entity){
		return linksusInteractWeixinMapper.getTaskWeixinAtImmediate(entity);
	}

	/** ��ѯ��ʱ�ظ�΢�������б� */
	public List<LinksusInteractWeixin> getTaskWeixinAtRegularTime(LinksusInteractWeixin entity){
		return linksusInteractWeixinMapper.getTaskWeixinAtRegularTime(entity);
	}

	public List getInteractWeixinListByIds(List weixinMsgs){
		return linksusInteractWeixinMapper.getInteractWeixinListByIds(weixinMsgs);
	}

	/**  �޸ķ���״̬������ʱ��*/
	public Integer updateLinksusInteractWeixinStatus(LinksusInteractWeixin entity){
		return linksusInteractWeixinMapper.updateLinksusInteractWeixinStatus(entity);
	}

}