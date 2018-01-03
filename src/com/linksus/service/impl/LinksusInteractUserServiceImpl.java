package com.linksus.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.common.Constants;
import com.linksus.dao.LinksusInteractUserMapper;
import com.linksus.entity.LinksusInteractUser;
import com.linksus.service.BaseService;
import com.linksus.service.LinksusInteractUserService;

@Service("linksusInteractUserService")
public class LinksusInteractUserServiceImpl extends BaseService implements LinksusInteractUserService{

	@Autowired
	private LinksusInteractUserMapper linksusInteractUserMapper;

	/** ��ѯ�б� */
	public List<LinksusInteractUser> getLinksusInteractUserList(){
		return linksusInteractUserMapper.getLinksusInteractUserList();
	}

	/** ������ѯ */
	public LinksusInteractUser getLinksusInteractUserById(Long pid){
		return linksusInteractUserMapper.getLinksusInteractUserById(pid);
	}

	/** ���� */
	public Integer insertLinksusInteractUser(LinksusInteractUser entity){
		return linksusInteractUserMapper.insertLinksusInteractUser(entity);
	}

	/** ���� */
	//	public Integer updateLinksusInteractUser(LinksusInteractUser entity){
	//		return linksusInteractUserMapper.updateLinksusInteractUser(entity);
	//	}

	/** ����ɾ�� */
	public Integer deleteLinksusInteractUserById(Long pid){
		return linksusInteractUserMapper.deleteLinksusInteractUserById(pid);
	}

	/**��黥���û� �Ƿ� ���� */
	public LinksusInteractUser checkUserDataIsExsit(LinksusInteractUser entity){
		return linksusInteractUserMapper.checkUserDataIsExsit(entity);
	}

	public List getInteractUserImageList(Map tempMap){
		return linksusInteractUserMapper.getInteractUserImageList(tempMap);
	}

	@Override
	public void saveEntity(List dataList,String operType) throws Exception{
		for(int i = 0; i < dataList.size(); i++){
			LinksusInteractUser entity = (LinksusInteractUser) dataList.get(i);
			if(operType.equals(Constants.OPER_TYPE_UPDATE)){
				sqlSession.update("com.linksus.dao.LinksusInteractUserMapper.updateLinksusInteractUser", entity);
			}
		}
	}
}