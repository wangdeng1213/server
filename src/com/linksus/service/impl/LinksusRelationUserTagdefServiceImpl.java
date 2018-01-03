package com.linksus.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.common.Constants;
import com.linksus.dao.LinksusRelationUserTagdefMapper;
import com.linksus.entity.LinksusRelationUserTagdef;
import com.linksus.service.BaseService;
import com.linksus.service.LinksusRelationUserTagdefService;

@Service("linksusRelationUserTagdefService")
public class LinksusRelationUserTagdefServiceImpl extends BaseService implements LinksusRelationUserTagdefService{

	@Autowired
	private LinksusRelationUserTagdefMapper linksusRelationUserTagdefMapper;

	/** ��ѯ�б� */
	public List<LinksusRelationUserTagdef> getLinksusRelationUserTagdefList(){
		return linksusRelationUserTagdefMapper.getLinksusRelationUserTagdefList();
	}

	/** ������ѯ */
	public LinksusRelationUserTagdef getLinksusRelationUserTagdefById(Long pid){
		return linksusRelationUserTagdefMapper.getLinksusRelationUserTagdefById(pid);
	}

	/** ���� */
	public Integer insertLinksusRelationUserTagdef(LinksusRelationUserTagdef entity){
		return linksusRelationUserTagdefMapper.insertLinksusRelationUserTagdef(entity);
	}

	public void saveEntity(List dataList,String operType) throws Exception{
		try{
			for(int i = 0; i < dataList.size(); i++){
				LinksusRelationUserTagdef entity = (LinksusRelationUserTagdef) dataList.get(i);
				if(operType.equals(Constants.OPER_TYPE_INSERT)){
					sqlSession.insert(
							"com.linksus.dao.linksusRelationUserTagdefMapper.insertLinksusRelationUserTagdef", entity);
				}
			}
		}catch (Exception e){
			throw e;
		}
	}

	/** ���� */
	public Integer updateLinksusRelationUserTagdef(LinksusRelationUserTagdef entity){
		return linksusRelationUserTagdefMapper.updateLinksusRelationUserTagdef(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusRelationUserTagdefById(Long pid){
		return linksusRelationUserTagdefMapper.deleteLinksusRelationUserTagdefById(pid);
	}

	/** ��ѯ����list�еı�ǩ���� */
	public List<LinksusRelationUserTagdef> getUserTagdefBySet(Map tagParaMap){
		return linksusRelationUserTagdefMapper.getUserTagdefBySet(tagParaMap);
	}

	/**ͨ����ǩ �� �û������жϱ�ǩ�������Ƿ����*/

	public LinksusRelationUserTagdef checkIsExsitByTagAndaccoutType(Map paraMap){
		return linksusRelationUserTagdefMapper.checkIsExsitByTagAndaccoutType(paraMap);
	}

	/** ������ѯ */
	public LinksusRelationUserTagdef getLinksusRelationUserTagdefByTagName(String tagname){
		return linksusRelationUserTagdefMapper.getLinksusRelationUserTagdefByTagName(tagname);
	}

	/** ��ǩʹ�ô���ͳ�Ƹ��� */
	public Integer updateLinksusRelationUserTagdefUseCount(){
		return linksusRelationUserTagdefMapper.updateLinksusRelationUserTagdefUseCount();
	}

	/** ����û���ǩͳ��ʹ�õ��м�� */
	public Integer clearUserTagMiddleTable(){
		return linksusRelationUserTagdefMapper.clearUserTagMiddleTable();
	}
}