package com.linksus.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.common.Constants;
import com.linksus.dao.LinksusInteractPersonMapper;
import com.linksus.entity.InteractListQueryObj;
import com.linksus.entity.LinksusInteractPerson;
import com.linksus.service.BaseService;
import com.linksus.service.LinksusInteractPersonService;

@Service("linksusInteractPersonService")
public class LinksusInteractPersonServiceImpl extends BaseService implements LinksusInteractPersonService{

	@Autowired
	private LinksusInteractPersonMapper linksusInteractPersonMapper;

	/** ��ѯ�б� */
	public List<LinksusInteractPerson> getLinksusInteractPersonList(){
		return linksusInteractPersonMapper.getLinksusInteractPersonList();
	}

	/** ������ѯ */
	public LinksusInteractPerson getLinksusInteractPersonById(Long pid){
		return linksusInteractPersonMapper.getLinksusInteractPersonById(pid);
	}

	/** ���� */
	public Integer insertLinksusInteractPerson(LinksusInteractPerson entity){
		return linksusInteractPersonMapper.insertLinksusInteractPerson(entity);
	}

	/** ���� */
	//	public Integer updateLinksusInteractPerson(LinksusInteractPerson entity){
	//		return linksusInteractPersonMapper.updateLinksusInteractPerson(entity);
	//	}

	/** ����ɾ�� */
	public Integer deleteLinksusInteractPersonById(Long pid){
		return linksusInteractPersonMapper.deleteLinksusInteractPersonById(pid);
	}

	/** ��������Ƿ���ڻ�����Ա���� */
	public LinksusInteractPerson checkDataIsExsitInteractPerson(LinksusInteractPerson entity){
		return linksusInteractPersonMapper.checkDataIsExsitInteractPerson(entity);

	}

	/** ��ѯ�����б�*/
	public List queryInteractDataList(InteractListQueryObj queryObj){
		return linksusInteractPersonMapper.queryInteractDataList(queryObj);
	}

	/** ��ѯ�����б�����*/
	public Integer queryInteractDataListCount(InteractListQueryObj queryObj){
		return linksusInteractPersonMapper.queryInteractDataListCount(queryObj);
	}

	@Override
	public Map queryInteractDataList2(Map map){
		return linksusInteractPersonMapper.queryInteractDataList2(map);
	}

	/**�»����б��ѯ*/
	public List newQueryInteractDataList(InteractListQueryObj queryObj){
		return linksusInteractPersonMapper.newQueryInteractDataList(queryObj);
	}

	@Override
	public void saveEntity(List dataList,String operType) throws Exception{
		for(int i = 0; i < dataList.size(); i++){
			LinksusInteractPerson entity = (LinksusInteractPerson) dataList.get(i);
			if(operType.equals(Constants.OPER_TYPE_UPDATE)){
				sqlSession.update("com.linksus.dao.LinksusInteractPersonMapper.updateLinksusInteractPerson", entity);
			}
		}
	}
}