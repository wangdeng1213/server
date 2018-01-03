package com.linksus.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.common.Constants;
import com.linksus.dao.LinksusTaskInteractGovMidMapper;
import com.linksus.entity.LinksusTaskInteractGovMid;
import com.linksus.service.BaseService;
import com.linksus.service.LinksusTaskInteractGovMidService;

@Service("linksusTaskInteractGovMidService")
public class LinksusTaskInteractGovMidServiceImpl extends BaseService implements LinksusTaskInteractGovMidService{

	@Autowired
	private LinksusTaskInteractGovMidMapper linksusTaskInteractGovMidMapper;

	/** ��ѯ�б� */
	public List<LinksusTaskInteractGovMid> getLinksusTaskInteractGovMidList(){
		return linksusTaskInteractGovMidMapper.getLinksusTaskInteractGovMidList();
	}

	/** ������ѯ */
	public LinksusTaskInteractGovMid getLinksusTaskInteractGovMidById(LinksusTaskInteractGovMid govMid){
		return linksusTaskInteractGovMidMapper.getLinksusTaskInteractGovMidById(govMid);
	}

	/** ���� */
	public Integer insertLinksusTaskInteractGovMid(LinksusTaskInteractGovMid entity){
		return linksusTaskInteractGovMidMapper.insertLinksusTaskInteractGovMid(entity);
	}

	/** ���� */
	public Integer updateLinksusTaskInteractGovMid(LinksusTaskInteractGovMid entity){
		return linksusTaskInteractGovMidMapper.updateLinksusTaskInteractGovMid(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusTaskInteractGovMidById(LinksusTaskInteractGovMid pid){
		return linksusTaskInteractGovMidMapper.deleteLinksusTaskInteractGovMidById(pid);
	}

	/**�������ͺ��˺Ų�ѯ����ʱ���л���������*/
	public List<LinksusTaskInteractGovMid> getGovMidTempListByMap(Map map){
		return linksusTaskInteractGovMidMapper.getGovMidTempListByMap(map);
	}

	/** �����ʱ��������  */
	public void deleteLinksusTaskInteractGovMid(){
		linksusTaskInteractGovMidMapper.deleteLinksusTaskInteractGovMid();
	}

	/**����MID��ѯ*/
	public LinksusTaskInteractGovMid getLinksusTaskInteractGovMidListByMid(Long mid){
		List<LinksusTaskInteractGovMid> list = linksusTaskInteractGovMidMapper
				.getLinksusTaskInteractGovMidListByMid(mid);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public Integer deleteLinkSusTaskInteractGovMidByMid(Long mid){
		return linksusTaskInteractGovMidMapper.deleteLinkSusTaskInteractGovMidByMid(mid);
	}

	@Override
	public List<Long> getDuoWenInteractGovMid(){
		return linksusTaskInteractGovMidMapper.getDuoWenInteractGovMid();
	}

	@Override
	public List<LinksusTaskInteractGovMid> getLinksusCommentAndRelayGovMidList(){
		return linksusTaskInteractGovMidMapper.getLinksusCommentAndRelayGovMidList();
	}

	@Override
	public void updateInteractType(LinksusTaskInteractGovMid govMid){
		linksusTaskInteractGovMidMapper.updateInteractType(govMid);
	}

	@Override
	public Integer saveLinksusInteractGovMid(List<LinksusTaskInteractGovMid> list,String operType){
		saveEntity(list, operType);
		return 0;
	}

	@Override
	public void saveEntity(List dataList,String operType){
		for(int i = 0; i < dataList.size(); i++){
			LinksusTaskInteractGovMid entity = (LinksusTaskInteractGovMid) dataList.get(i);
			if(operType.equals(Constants.OPER_TYPE_INSERT)){
				sqlSession.insert("com.linksus.dao.LinksusTaskInteractGovMidMapper.insertLinksusTaskInteractGovMid",
						entity);
			}else if(operType.equals(Constants.OPER_TYPE_UPDATE)){
				sqlSession.update("com.linksus.dao.LinksusTaskInteractGovMidMapper.updateLinksusTaskInteractGovMid",
						entity);
			}
		}
	}
}