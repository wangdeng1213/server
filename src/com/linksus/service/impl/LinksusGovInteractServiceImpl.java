package com.linksus.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.common.Constants;
import com.linksus.dao.LinksusGovInteractMapper;
import com.linksus.entity.LinksusGovInteract;
import com.linksus.service.BaseService;
import com.linksus.service.LinksusGovInteractService;

@Service("linksusGovInteractService")
public class LinksusGovInteractServiceImpl extends BaseService implements LinksusGovInteractService{

	@Autowired
	private LinksusGovInteractMapper linksusGovInteractMapper;

	/** 查询列表 */
	public List<LinksusGovInteract> getLinksusGovInteractList(){
		return linksusGovInteractMapper.getLinksusGovInteractList();
	}

	/** 主键查询 */
	public LinksusGovInteract getLinksusGovInteractById(Long pid){
		return linksusGovInteractMapper.getLinksusGovInteractById(pid);
	}

	/** 新增 */
	public Integer insertLinksusGovInteract(LinksusGovInteract entity){
		return linksusGovInteractMapper.insertLinksusGovInteract(entity);
	}

	/** 更新 */
	public Integer updateLinksusGovInteract(LinksusGovInteract entity){
		return linksusGovInteractMapper.updateLinksusGovInteract(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusGovInteractById(Long pid){
		return linksusGovInteractMapper.deleteLinksusGovInteractById(pid);
	}

	@Override
	public Integer deleteLinksusGovInteractByMap(Map map){
		return linksusGovInteractMapper.deleteLinksusGovInteractByMap(map);
	}

	@Override
	public List<LinksusGovInteract> getInteractGovListByMid(Map map){
		return linksusGovInteractMapper.getInteractGovListByMid(map);
	}

	@Override
	public void updateSendReplyStatus(LinksusGovInteract govInteract){
		linksusGovInteractMapper.updateSendReplyStatus(govInteract);
	}

	@Override
	public void updateSendReplySucc(LinksusGovInteract govInteract){
		linksusGovInteractMapper.updateSendReplySucc(govInteract);
	}

	@Override
	public List<LinksusGovInteract> getLinksusGovInteractTaskList(LinksusGovInteract entity){
		return linksusGovInteractMapper.getLinksusGovInteractTaskList(entity);
	}

	@Override
	public void saveLinksusGovInteract(List<LinksusGovInteract> list,String operType){
		saveEntity(list, operType);
	}

	@Override
	public void saveEntity(List dataList,String operType){
		for(int i = 0; i < dataList.size(); i++){
			LinksusGovInteract entity = (LinksusGovInteract) dataList.get(i);
			if(operType.equals(Constants.OPER_TYPE_INSERT)){
				sqlSession.insert("com.linksus.dao.LinksusGovInteractMapper.insertLinksusGovInteract", entity);
			}else if(operType.equals(Constants.OPER_TYPE_UPDATE)){
				sqlSession.update("com.linksus.dao.LinksusGovInteractMapper.updateLinksusGovInteract", entity);
			}
		}
	}
}