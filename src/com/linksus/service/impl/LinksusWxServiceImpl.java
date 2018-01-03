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

	/** ��ѯ�б� */
	public List<LinksusWx> getLinksusWxList(){
		return linksusWxMapper.getLinksusWxList();
	}

	/** ������ѯ */
	public LinksusWx getLinksusWxById(Long pid){
		return linksusWxMapper.getLinksusWxById(pid);
	}

	/** ���� */
	public Integer insertLinksusWx(LinksusWx entity){
		return linksusWxMapper.insertLinksusWx(entity);
	}

	/** ���� */
	public Integer updateLinksusWx(LinksusWx entity){
		return linksusWxMapper.updateLinksusWx(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusWxById(Long pid){
		return linksusWxMapper.deleteLinksusWxById(pid);
	}

	/** ��ѯ����״̬Ϊδִ��״̬��΢������ */
	public List<LinksusWx> getWeiXinTaskList(LinksusWx entity){
		return linksusWxMapper.getWeiXinTaskList(entity);
	}

	/** �޸�����״̬ */
	public void updateWeiXinTaskList(LinksusWx entity){
		linksusWxMapper.updateWeiXinTaskList(entity);
	}

	/** ��ȡ΢��Ⱥ����Ϣ�û�token����Ϣ */
	public LinksusWx getLinksusWXUserAndInfo(Long pid){
		return linksusWxMapper.getLinksusWXUserAndInfo(pid);
	}

	/**  �޸�΢�ŷ������*/
	public void updateWeiXinTaskErrmsg(LinksusWx entity){
		linksusWxMapper.updateWeiXinTaskErrmsg(entity);
	}

	/**  ͨ��΢��Ⱥ����Ϣ���޸�΢�ŷ������*/
	public void updateWeiXinTaskErrmsgByGroup(Long pid){
		linksusWxMapper.updateWeiXinTaskErrmsgByGroup(pid);
	}

	/** �޸�΢�ŷ��������״̬*/
	public void updateWeiXinTaskstatusAndErrmsg(LinksusWx entity){
		linksusWxMapper.updateWeiXinTaskstatusAndErrmsg(entity);
	}
}