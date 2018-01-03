package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.common.Constants;
import com.linksus.dao.LinksusRelationUserEduMapper;
import com.linksus.entity.LinksusRelationUserEdu;
import com.linksus.service.BaseService;
import com.linksus.service.LinksusRelationUserEduService;

@Service("linksusRelationUserEduService")
public class LinksusRelationUserEduServiceImpl extends BaseService implements LinksusRelationUserEduService{

	@Autowired
	private LinksusRelationUserEduMapper linksusRelationUserEduMapper;

	/** ��ѯ�б� */
	public List<LinksusRelationUserEdu> getLinksusRelationUserEduList(){
		return linksusRelationUserEduMapper.getLinksusRelationUserEduList();
	}

	/** ������ѯ */
	public LinksusRelationUserEdu getLinksusRelationUserEduById(Long pid){
		return linksusRelationUserEduMapper.getLinksusRelationUserEduById(pid);
	}

	/** ���� */
	public Integer insertLinksusRelationUserEdu(LinksusRelationUserEdu entity){
		return linksusRelationUserEduMapper.insertLinksusRelationUserEdu(entity);
	}

	/** ���� */
	public Integer updateLinksusRelationUserEdu(LinksusRelationUserEdu entity){
		return linksusRelationUserEduMapper.updateLinksusRelationUserEdu(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusRelationUserEduById(Long pid){
		return linksusRelationUserEduMapper.deleteLinksusRelationUserEduById(pid);
	}

	@Override
	public Integer deleteLinksusRelationUserEduByUserId(Long userId){
		return linksusRelationUserEduMapper.deleteLinksusRelationUserEduByUserId(userId);
	}

	public void saveEntity(List dataList,String operType) throws Exception{
		try{
			for(int i = 0; i < dataList.size(); i++){
				LinksusRelationUserEdu entity = (LinksusRelationUserEdu) dataList.get(i);
				if(operType.equals(Constants.OPER_TYPE_INSERT)){
					sqlSession.insert("com.linksus.dao.LinksusRelationUserEduMapper.insertLinksusRelationUserEdu",
							entity);
				}else if(operType.equals(Constants.OPER_TYPE_UPDATE)){
					sqlSession.update("com.linksus.dao.LinksusRelationUserEduMapper.updateLinksusRelationUserEdu",
							entity);
				}
			}
		}catch (Exception e){
			throw e;
		}
	}
}