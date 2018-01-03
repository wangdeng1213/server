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

	/** 查询列表 */
	public List<LinksusInteractUser> getLinksusInteractUserList(){
		return linksusInteractUserMapper.getLinksusInteractUserList();
	}

	/** 主键查询 */
	public LinksusInteractUser getLinksusInteractUserById(Long pid){
		return linksusInteractUserMapper.getLinksusInteractUserById(pid);
	}

	/** 新增 */
	public Integer insertLinksusInteractUser(LinksusInteractUser entity){
		return linksusInteractUserMapper.insertLinksusInteractUser(entity);
	}

	/** 更新 */
	//	public Integer updateLinksusInteractUser(LinksusInteractUser entity){
	//		return linksusInteractUserMapper.updateLinksusInteractUser(entity);
	//	}

	/** 主键删除 */
	public Integer deleteLinksusInteractUserById(Long pid){
		return linksusInteractUserMapper.deleteLinksusInteractUserById(pid);
	}

	/**检查互动用户 是否 存在 */
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