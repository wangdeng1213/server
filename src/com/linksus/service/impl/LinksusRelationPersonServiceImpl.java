package com.linksus.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.common.Constants;
import com.linksus.dao.LinksusRelationPersonMapper;
import com.linksus.entity.LinksusRelationPerson;
import com.linksus.service.BaseService;
import com.linksus.service.LinksusRelationPersonService;

@Service("linksusRelationPersonService")
public class LinksusRelationPersonServiceImpl extends BaseService implements LinksusRelationPersonService{

	@Autowired
	private LinksusRelationPersonMapper linksusRelationPersonMapper;
	@Resource
	private SqlSession sqlSession;

	public void setSqlSession(SqlSessionTemplate sqlSession){
		this.sqlSession = sqlSession;
	}

	/**��ҳ��ѯ��ͷ���û��б� */
	public List<LinksusRelationPerson> getLinksusRelationPersonAddImageList(LinksusRelationPerson person){
		return linksusRelationPersonMapper.getLinksusRelationPersonAddImageList(person);
	}

	/**������Աͷ��·�� */
	public void updateLinksusRelationPersonHeadUrl(LinksusRelationPerson person){
		linksusRelationPersonMapper.updateLinksusRelationPerson(person);
	}

	/** ��ѯ�б� */
	public List<LinksusRelationPerson> getLinksusRelationPersonList(){
		return linksusRelationPersonMapper.getLinksusRelationPersonList();
	}

	/** ������ѯ */
	public LinksusRelationPerson getLinksusRelationPersonById(Long pid){
		return linksusRelationPersonMapper.getLinksusRelationPersonById(pid);
	}

	/** ���� */
	/*
	 * public Integer insertLinksusRelationPerson(LinksusRelationPerson entity){
	 * return linksusRelationPersonMapper.insertLinksusRelationPerson(entity); }
	 */

	/*
	 * public void saveLinksusRelationPerson(List personList,String operType){
	 * LinksusRelationPersonMapper personMapper =
	 * sqlSession.getMapper(LinksusRelationPersonMapper.class); for (int i = 0;
	 * i < personList.size(); i++) { LinksusRelationPerson
	 * entity=(LinksusRelationPerson) personList.get(i);
	 * if(operType.equals(Constants.OPER_TYPE_INSERT)){
	 * personMapper.insertLinksusRelationPerson(entity); }else
	 * if(operType.equals(Constants.OPER_TYPE_UPDATE)){
	 * personMapper.updateLinksusRelationPerson(entity); } } }
	 */

	/** ���� */
	/*
	 * public Integer updateLinksusRelationPerson(LinksusRelationPerson entity){
	 * return linksusRelationPersonMapper.updateLinksusRelationPerson(entity); }
	 */

	/** ����ɾ�� */
	public Integer deleteLinksusRelationPersonById(Long pid){
		return linksusRelationPersonMapper.deleteLinksusRelationPersonById(pid);
	}

	/** ������Ա����΢���û� */
	public void insertWeiXinUser(LinksusRelationPerson entity){
		linksusRelationPersonMapper.insertWeiXinUser(entity);
	}

	/** ��ҳ��ѯ�û��б�  */
	public List<LinksusRelationPerson> getLinksusRelationPersons(LinksusRelationPerson person){
		return linksusRelationPersonMapper.getLinksusRelationPersons(person);
	}

	/**������Ա�������Ϣ */
	public void updateLinksusRelationPersonInfo(LinksusRelationPerson person){
		linksusRelationPersonMapper.updateLinksusRelationPersonInfo(person);
	}

	/**
	 * �������±������
	 */
	public void saveEntity(List dataList,String operType){
		LinksusRelationPersonMapper personMapper = sqlSession.getMapper(LinksusRelationPersonMapper.class);
		for(int i = 0; i < dataList.size(); i++){
			LinksusRelationPerson entity = (LinksusRelationPerson) dataList.get(i);
			logger.info(">>>>>>>>>>>>>>>>������Ա����:{}-{}-{}", operType, entity.getPersonId(), entity.getSynctime());
			if(operType.equals(Constants.OPER_TYPE_INSERT)){
				personMapper.insertLinksusRelationPerson(entity);
			}else if(operType.equals(Constants.OPER_TYPE_UPDATE)){
				personMapper.updateLinksusRelationPerson(entity);
			}else if(operType.equals(Constants.OPER_TYPE_UPDATE_INFO)){
				personMapper.updateLinksusRelationPersonInfo(entity);
			}
		}
	}
}