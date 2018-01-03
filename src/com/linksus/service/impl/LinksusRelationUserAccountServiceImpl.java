package com.linksus.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.common.Constants;
import com.linksus.dao.LinksusRelationUserAccountMapper;
import com.linksus.entity.LinksusRelationUserAccount;
import com.linksus.service.BaseService;
import com.linksus.service.LinksusRelationUserAccountService;

/**
 * �˻��û���ϵ��ӿ�
 * 
 * @author wangdeng
 * 
 */
@Service("linksusRelationUserAccountService")
public class LinksusRelationUserAccountServiceImpl extends BaseService implements LinksusRelationUserAccountService{

	@Autowired
	private LinksusRelationUserAccountMapper linksusRelationUserAccountMapper;
	@Resource
	private SqlSession sqlSession;

	public void setSqlSession(SqlSessionTemplate sqlSession){
		this.sqlSession = sqlSession;
	}

	// ��ȡ�û���ע�ķ�˿ ���� �໥��ע�ķ�˿΢����Ϣ
	public List<LinksusRelationUserAccount> getLinksysWeiboRelationByFlag(String accountType){
		List<LinksusRelationUserAccount> list = linksusRelationUserAccountMapper
				.getLinksysWeiboRelationByFlag(accountType);
		return list;
	}

	//����userId ��  ����id ��ѯ�û��˺Ź�ϵ�����Ƿ��й�ϵ
	public Integer getCountLinksusRelationUserAccount(LinksusRelationUserAccount entity){
		return linksusRelationUserAccountMapper.getCountLinksusRelationUserAccount(entity);
	}

	public void updateRelationUserAccount(LinksusRelationUserAccount linksusRelationUserAccount){
		linksusRelationUserAccountMapper.updateRelationUserAccount(linksusRelationUserAccount);
	}

	@Override
	public List<LinksusRelationUserAccount> getLinksusRelationUserAccountList(
			LinksusRelationUserAccount linksusRelationUserAccount){
		// TODO Auto-generated method stub
		return linksusRelationUserAccountMapper.getLinksusRelationUserAccountList(linksusRelationUserAccount);
	}

	@Override
	public LinksusRelationUserAccount getUserAccountByAccountId(Map map){
		// TODO Auto-generated method stub
		return linksusRelationUserAccountMapper.getUserAccountByAccountId(map);

	}

	public LinksusRelationUserAccount getRelationUser(Map map){
		return linksusRelationUserAccountMapper.getRelationUser(map);

	}

	//�����û��˻���ϵ��������
	public void updateLinksusRelationUserAccountNum(List userAccountList){
		for(int i = 0; i < userAccountList.size(); i++){
			LinksusRelationUserAccount entity = (LinksusRelationUserAccount) userAccountList.get(i);
			sqlSession.update("com.linksus.dao.LinksusRelationUserAccountMapper.updateLinksusRelationUserAccountNum",
					entity);
		}
	}

	public void deleteLinksusRelationUserAccount(Long pid){
		linksusRelationUserAccountMapper.deleteLinksusRelationUserAccount(pid);
	}

	public void dealFlagRelation(Map map){
		linksusRelationUserAccountMapper.dealFlagRelation(map);
	}

	public LinksusRelationUserAccount getLinksusWeiboRelation(LinksusRelationUserAccount linksusRelationUserAccount){
		return linksusRelationUserAccountMapper.getLinksusWeiboRelation(linksusRelationUserAccount);
	}

	public LinksusRelationUserAccount getIsExsitWeiboUserAccount(Map map){

		return linksusRelationUserAccountMapper.getIsExsitWeiboUserAccount(map);

	}

	public void updateFlagRelationByPid(LinksusRelationUserAccount linksusRelationUserAccount){
		linksusRelationUserAccountMapper.updateFlagRelationByPid(linksusRelationUserAccount);
	}

	//����΢�����û� 
	public void insertWeiXineToLinksusRelationUserAccount(LinksusRelationUserAccount account){
		linksusRelationUserAccountMapper.insertWeiXineToLinksusRelationUserAccount(account);
	}

	/** ����account_id��account_type��user_idɾ����¼ */
	public void deleteLinksusRelationUserAccountByKey(Map map){
		linksusRelationUserAccountMapper.deleteLinksusRelationUserAccountByKey(map);
	}

	//��ȡ���й�ϵ���˺�
	public List<LinksusRelationUserAccount> getALLRelationUserAccountMap(Map map){
		return linksusRelationUserAccountMapper.getALLRelationUserAccountMap(map);
	}

	/**
	 * ����/�������
	 * @throws Exception 
	 */
	public void saveEntity(List dataList,String operType) throws Exception{
		try{
			for(int i = 0; i < dataList.size(); i++){
				LinksusRelationUserAccount entity = (LinksusRelationUserAccount) dataList.get(i);
				//logger.info(">>>>>>>>>>>>>>>>�����û��˺Ź�ϵ����:{}:{}-{}",operType,entity.getAccountId(),entity.getUserId());
				if(operType.equals(Constants.OPER_TYPE_INSERT)){
					sqlSession
							.insert(
									"com.linksus.dao.LinksusRelationUserAccountMapper.insertLinksusRelationUserAccount",
									entity);
				}else if(operType.equals(Constants.OPER_TYPE_UPDATE)){
					sqlSession.update("com.linksus.dao.LinksusRelationUserAccountMapper.updateFlagRelationByPid",
							entity);
				}
			}
		}catch (Exception e){
			//�ж�����Ϊ������ ɾ������
			if(operType.equals(Constants.OPER_TYPE_INSERT)){
				for(int i = 0; i < dataList.size(); i++){
					removeRedisKey(dataList.get(i), operType);
				}
			}
			throw e;
		}
	}
}
