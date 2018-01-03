package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.common.Constants;
import com.linksus.dao.LinksusGovRunningMapper;
import com.linksus.entity.LinksusGovRunning;
import com.linksus.service.BaseService;
import com.linksus.service.LinksusGovRunningService;

@Service("linksusGovRunningService")
public class LinksusGovRunningServiceImpl extends BaseService implements LinksusGovRunningService{

	@Autowired
	private LinksusGovRunningMapper linksusGovRunningMapper;

	/** ��ѯ�б� */
	public List<LinksusGovRunning> getLinksusGovRunningList(){
		return linksusGovRunningMapper.getLinksusGovRunningList();
	}

	/** ������ѯ */
	public LinksusGovRunning getLinksusGovRunningById(Long pid){
		return linksusGovRunningMapper.getLinksusGovRunningById(pid);
	}

	/** ���� */
	public Integer insertLinksusGovRunning(LinksusGovRunning entity){
		return linksusGovRunningMapper.insertLinksusGovRunning(entity);
	}

	/** ���� */
	public Integer updateLinksusGovRunning(LinksusGovRunning entity){
		return linksusGovRunningMapper.updateLinksusGovRunning(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusGovRunningById(Long pid){
		return linksusGovRunningMapper.deleteLinksusGovRunningById(pid);
	}

	@Override
	public void saveLinksusGovRunning(List<LinksusGovRunning> list,String operType){
		saveEntity(list, operType);
	}

	@Override
	public void saveEntity(List dataList,String operType){
		for(int i = 0; i < dataList.size(); i++){
			LinksusGovRunning entity = (LinksusGovRunning) dataList.get(i);
			if(operType.equals(Constants.OPER_TYPE_INSERT)){
				sqlSession.insert("com.linksus.dao.LinksusGovRunningMapper.insertLinksusGovRunning", entity);
			}else if(operType.equals(Constants.OPER_TYPE_UPDATE)){
				sqlSession.update("com.linksus.dao.LinksusGovRunningMapper.updateLinksusGovRunning", entity);
			}
		}
	}

}