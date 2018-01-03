package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.common.Constants;
import com.linksus.dao.LinksusRelationUserCompanyMapper;
import com.linksus.entity.LinksusRelationUserCompany;
import com.linksus.service.BaseService;
import com.linksus.service.LinksusRelationUserCompanyService;

@Service("linksusRelationUserCompanyService")
public class LinksusRelationUserCompanyServiceImpl extends BaseService implements LinksusRelationUserCompanyService{

	@Autowired
	private LinksusRelationUserCompanyMapper linksusRelationUserCompanyMapper;

	/** ��ѯ�б� */
	public List<LinksusRelationUserCompany> getLinksusRelationUserCompanyList(){
		return linksusRelationUserCompanyMapper.getLinksusRelationUserCompanyList();
	}

	/** ������ѯ */
	public LinksusRelationUserCompany getLinksusRelationUserCompanyById(Long pid){
		return linksusRelationUserCompanyMapper.getLinksusRelationUserCompanyById(pid);
	}

	/** ���� */
	public Integer insertLinksusRelationUserCompany(LinksusRelationUserCompany entity){
		return linksusRelationUserCompanyMapper.insertLinksusRelationUserCompany(entity);
	}

	/** ���� */
	public Integer updateLinksusRelationUserCompany(LinksusRelationUserCompany entity){
		return linksusRelationUserCompanyMapper.updateLinksusRelationUserCompany(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusRelationUserCompanyById(Long pid){
		return linksusRelationUserCompanyMapper.deleteLinksusRelationUserCompanyById(pid);
	}

	@Override
	public Integer deleteLinksusRelationUserCompanyByUserId(Long userId){
		return linksusRelationUserCompanyMapper.deleteLinksusRelationUserCompanyByUserId(userId);
	}

	public void saveEntity(List dataList,String operType) throws Exception{
		try{
			for(int i = 0; i < dataList.size(); i++){
				LinksusRelationUserCompany entity = (LinksusRelationUserCompany) dataList.get(i);
				if(operType.equals(Constants.OPER_TYPE_INSERT)){
					sqlSession
							.insert(
									"com.linksus.dao.LinksusRelationUserCompanyMapper.insertLinksusRelationUserCompany",
									entity);
				}else if(operType.equals(Constants.OPER_TYPE_UPDATE)){
					sqlSession
							.update(
									"com.linksus.dao.LinksusRelationUserCompanyMapper.updateLinksusRelationUserCompany",
									entity);
				}
			}
		}catch (Exception e){
			throw e;
		}
	}
}