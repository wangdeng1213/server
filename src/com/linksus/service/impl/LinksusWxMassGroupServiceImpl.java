package com.linksus.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.common.Constants;
import com.linksus.dao.LinksusWxMassGroupMapper;
import com.linksus.entity.LinksusWxMassGroup;
import com.linksus.service.BaseService;
import com.linksus.service.LinksusWxMassGroupService;

@Service("linksusWxMassGroupService")
public class LinksusWxMassGroupServiceImpl extends BaseService implements LinksusWxMassGroupService{

	@Autowired
	private LinksusWxMassGroupMapper linksusWxMassGroupMapper;

	/** ��ѯ�б� */
	public List<LinksusWxMassGroup> getLinksusWxMassGroupList(){
		return linksusWxMassGroupMapper.getLinksusWxMassGroupList();
	}

	/** ������ѯ */
	public LinksusWxMassGroup getLinksusWxMassGroupById(Long pid){
		return linksusWxMassGroupMapper.getLinksusWxMassGroupById(pid);
	}

	/** ���� */
	public Integer insertLinksusWxMassGroup(LinksusWxMassGroup entity){
		return linksusWxMassGroupMapper.insertLinksusWxMassGroup(entity);
	}

	/** ���� */
	public Integer updateLinksusWxMassGroup(LinksusWxMassGroup entity){
		return linksusWxMassGroupMapper.updateLinksusWxMassGroup(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusWxMassGroupById(Long pid){
		return linksusWxMassGroupMapper.deleteLinksusWxMassGroupById(pid);
	}

	/** ����Ⱥ����� */
	public Integer updateMassResultGroup(LinksusWxMassGroup entity){
		return linksusWxMassGroupMapper.updateMassResultGroup(entity);
	}

	/** ͨ����Ϣmsgid��ȡwx_id  */
	public LinksusWxMassGroup getLinksusWxMassGroupByMsgId(Map mapInfo){
		return linksusWxMassGroupMapper.getLinksusWxMassGroupByMsgId(mapInfo);
	}

	/**ͨ��΢��id��ȡmsgId*/
	public List<LinksusWxMassGroup> getLinksusWxMassGroupByWxId(Long wxId){
		return linksusWxMassGroupMapper.getLinksusWxMassGroupByWxId(wxId);
	}

	@Override
	public void saveEntity(List dataList,String operType) throws Exception{
		for(int i = 0; i < dataList.size(); i++){
			LinksusWxMassGroup entity = (LinksusWxMassGroup) dataList.get(i);
			if(operType.equals(Constants.OPER_TYPE_INSERT)){
				sqlSession.update("com.linksus.dao.LinksusWxMassGroupMapper.insertLinksusWxMassGroup", entity);
			}
		}
	}
}