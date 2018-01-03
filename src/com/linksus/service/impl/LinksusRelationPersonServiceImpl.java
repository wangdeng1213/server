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

	/**分页查询无头像用户列表 */
	public List<LinksusRelationPerson> getLinksusRelationPersonAddImageList(LinksusRelationPerson person){
		return linksusRelationPersonMapper.getLinksusRelationPersonAddImageList(person);
	}

	/**新增人员头像路径 */
	public void updateLinksusRelationPersonHeadUrl(LinksusRelationPerson person){
		linksusRelationPersonMapper.updateLinksusRelationPerson(person);
	}

	/** 查询列表 */
	public List<LinksusRelationPerson> getLinksusRelationPersonList(){
		return linksusRelationPersonMapper.getLinksusRelationPersonList();
	}

	/** 主键查询 */
	public LinksusRelationPerson getLinksusRelationPersonById(Long pid){
		return linksusRelationPersonMapper.getLinksusRelationPersonById(pid);
	}

	/** 新增 */
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

	/** 更新 */
	/*
	 * public Integer updateLinksusRelationPerson(LinksusRelationPerson entity){
	 * return linksusRelationPersonMapper.updateLinksusRelationPerson(entity); }
	 */

	/** 主键删除 */
	public Integer deleteLinksusRelationPersonById(Long pid){
		return linksusRelationPersonMapper.deleteLinksusRelationPersonById(pid);
	}

	/** 插入人员主表微信用户 */
	public void insertWeiXinUser(LinksusRelationPerson entity){
		linksusRelationPersonMapper.insertWeiXinUser(entity);
	}

	/** 分页查询用户列表  */
	public List<LinksusRelationPerson> getLinksusRelationPersons(LinksusRelationPerson person){
		return linksusRelationPersonMapper.getLinksusRelationPersons(person);
	}

	/**更新人员表基本信息 */
	public void updateLinksusRelationPersonInfo(LinksusRelationPerson person){
		linksusRelationPersonMapper.updateLinksusRelationPersonInfo(person);
	}

	/**
	 * 批量更新保存对象
	 */
	public void saveEntity(List dataList,String operType){
		LinksusRelationPersonMapper personMapper = sqlSession.getMapper(LinksusRelationPersonMapper.class);
		for(int i = 0; i < dataList.size(); i++){
			LinksusRelationPerson entity = (LinksusRelationPerson) dataList.get(i);
			logger.info(">>>>>>>>>>>>>>>>保存人员数据:{}-{}-{}", operType, entity.getPersonId(), entity.getSynctime());
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